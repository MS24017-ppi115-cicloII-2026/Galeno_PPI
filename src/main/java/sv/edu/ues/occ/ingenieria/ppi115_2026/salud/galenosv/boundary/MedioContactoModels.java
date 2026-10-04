package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.PatternSyntaxException;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DAOInterface;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.MedioContactoDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.TipoMedioContactoDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.MedioContacto;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Persona;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.TipoMedioContacto;

@Named("medioContactoModel")
@ViewScoped
public class MedioContactoModels extends AbstractModel<MedioContacto> {

    @Inject
    MedioContactoDAO medioContactoDAO;
    @Inject
    TipoMedioContactoDAO tipoMedioContactoDAO;
    @Inject
    PersonaModels personaModel;

    private UUID personaCargada;
    private boolean cargado;

    @Override
    protected DAOInterface<MedioContacto> getDAO() {
        return medioContactoDAO;
    }

    @Override
    protected MedioContacto crearRegistroNuevo() {
        MedioContacto mc = new MedioContacto(UUID.randomUUID());
        mc.setFechaCreacion(new Date());
        mc.setIdPersona(personaModel.getPersonaSeleccionada());
        return mc;
    }

    @Override
    protected UUID obtenerId(MedioContacto registro) {
        return registro.getIdMedioContacto();
    }

    // ---- Lista filtrada por la persona seleccionada ----
    private List<MedioContacto> cargarFiltrado() {
        Persona persona = personaModel.getPersonaSeleccionada();
        if (persona == null) {
            // Sin persona seleccionada (pantalla MedioContacto.xhtml): lista completa
            return medioContactoDAO.findRange(0, 100);
        }
        return medioContactoDAO.buscarPorPersona(persona);
    }

    public List<MedioContacto> getRegistrosDePersona() {
        Persona persona = personaModel.getPersonaSeleccionada();
        UUID idActual = (persona == null) ? null : persona.getIdPersona();

        if (!cargado || !Objects.equals(idActual, personaCargada)) {
            personaCargada = idActual;
            cargado = true;
            setRegistros(cargarFiltrado());
            this.registro = null;
            this.estado = Estado_CRUD.NINGUNO;
        }
        return getregistros();
    }

    public List<TipoMedioContacto> getTiposActivos() {
        return tipoMedioContactoDAO.buscarPorActivo(true);
    }

    private boolean validarRegistro() {
        if (registro == null) {
            return mostrarError(
                    "Registro inválido",
                    "El medio de contacto no puede ser nulo."
            );
        }

        if (registro.getIdPersona() == null) {
            return mostrarError(
                    "Persona requerida",
                    "Debe seleccionar una persona."
            );
        }

        if (registro.getIdTipoMedioContacto() == null) {
            return mostrarError(
                    "Tipo requerido",
                    "Debe seleccionar un tipo de medio de contacto."
            );
        }

        if (registro.getValor() == null
                || registro.getValor().isBlank()) {
            return mostrarError(
                    "Valor requerido",
                    "El valor del medio de contacto no puede estar vacío."
            );
        }
        registro.setValor(registro.getValor().trim());

        TipoMedioContacto tipo = tipoMedioContactoDAO.buscar(
                registro.getIdTipoMedioContacto().getIdTipoMedioContacto()
        );

        if (tipo == null) {
            return mostrarError(
                    "Tipo inválido",
                    "El tipo de medio de contacto seleccionado no existe."
            );
        }

        if (!Boolean.TRUE.equals(tipo.getActivo())) {
            return mostrarError(
                    "Tipo inactivo",
                    "No se puede asignar un tipo de medio de contacto inactivo."
            );
        }

        String regex = tipo.getExpresionRegular();

        if (regex != null && !regex.isBlank()) {
            try {
                if (!registro.getValor().matches(regex)) {
                    String ayuda = (tipo.getIndicaciones() == null
                            || tipo.getIndicaciones().isBlank())
                            ? "El valor no tiene el formato válido para " + tipo.getNombre() + "."
                            : tipo.getIndicaciones();

                    return mostrarError("Formato inválido", ayuda);
                }
            } catch (PatternSyntaxException e) {
                return mostrarError(
                        "Expresión regular inválida",
                        "El tipo de medio de contacto tiene una expresión regular mal configurada."
                );
            }
        }

        if (registro.getFechaCreacion() == null) {
            registro.setFechaCreacion(new Date());
        }

        return true;
    }

    private boolean mostrarError(String resumen, String detalle) {
        fc.addMessage(
                null,
                new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        resumen,
                        detalle
                )
        );

        fc.validationFailed();
        return false;
    }

    // ---- Tras cada operación, la tabla vuelve a filtrarse por la persona ----
    @Override
    public void btnCrearhandler(ActionEvent ae) {
        if (validarRegistro()) {
            super.btnCrearhandler(ae);
            setRegistros(cargarFiltrado());
        }
    }

    @Override
    public void btnModificarHandler() {
        if (validarRegistro()) {
            super.btnModificarHandler();
            setRegistros(cargarFiltrado());
        }
    }

    @Override
    public void btnEliminarHandler(UUID id) {
        super.btnEliminarHandler(id);
        setRegistros(cargarFiltrado());
    }
}
