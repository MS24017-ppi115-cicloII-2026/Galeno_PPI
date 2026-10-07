package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.regex.PatternSyntaxException;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DAOInterface;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DocumentoDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.TipoDocumentoDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Documento;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Persona;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.TipoDocumento;

@Named
@ViewScoped
public class DocumentoModels extends AbstractModel<Documento> {

    @Inject
    DocumentoDAO documentoDAO;
    @Inject
    TipoDocumentoDAO tipoDocumentoDAO;
    @Inject
    PersonaModels personaModel;

    private static final Set<String> TIPOS_UNICOS = Set.of("dui");
    private static final Set<String> TIPOS_GLOBALES_UNICOS = Set.of("dui");

    private UUID personaCargada;
    private boolean cargado;

    @Override
    protected DAOInterface<Documento> getDAO() {
        return documentoDAO;
    }

    @Override
    protected Documento crearRegistroNuevo() {
        Documento documento = new Documento(UUID.randomUUID());
        documento.setIdPersona(personaModel.getPersonaSeleccionada());
        return documento;
    }

    @Override
    protected UUID obtenerId(Documento registro) {
        return registro.getIdDocumento();
    }

    // ---- Lista filtrada por la persona seleccionada ----
    private List<Documento> cargarFiltrado() {
        Persona persona = personaModel.getPersonaSeleccionada();
        if (persona == null) {
            // Sin persona seleccionada (pantalla Documento.xhtml): lista completa
            return documentoDAO.findRange(0, 100);
        }
        return documentoDAO.buscarPorPersona(persona.getIdPersona());
    }

    /**
     * Valor de la tabla de la pestaña: recarga sola cuando cambia la persona.
     */
    public List<Documento> getRegistrosDePersona() {
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

    public List<TipoDocumento> getTiposActivos() {
        return tipoDocumentoDAO.buscarPorActivo(true);
    }

    private boolean validarRegistro() {
        if (registro == null) {
            return mostrarError(
                    "Registro inválido",
                    "El documento no puede ser nulo."
            );
        }

        if (registro.getIdPersona() == null) {
            return mostrarError(
                    "Persona requerida",
                    "Debe seleccionar una persona."
            );
        }

        if (registro.getIdTipoDocumento() == null) {
            return mostrarError(
                    "Tipo de documento requerido",
                    "Debe seleccionar un tipo de documento."
            );
        }

        if (registro.getValor() == null
                || registro.getValor().isBlank()) {
            return mostrarError(
                    "Valor requerido",
                    "El valor del documento no puede estar vacío."
            );
        }
        registro.setValor(registro.getValor().trim());

        TipoDocumento tipo = tipoDocumentoDAO.buscar(
                registro.getIdTipoDocumento().getIdTipoDocumento()
        );

        if (tipo == null) {
            return mostrarError(
                    "Tipo de documento inválido",
                    "El tipo de documento seleccionado no existe."
            );
        }

        if (!Boolean.TRUE.equals(tipo.getActivo())) {
            return mostrarError(
                    "Tipo de documento inactivo",
                    "No se puede asignar un tipo de documento inactivo."
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
                        "El tipo de documento tiene una expresión regular mal configurada."
                );
            }
        }

        boolean duplicado = documentoDAO.existeDuplicado(
                registro.getIdPersona().getIdPersona(),
                registro.getIdTipoDocumento().getIdTipoDocumento(),
                registro.getValor(),
                registro.getIdDocumento()
        );

        if (duplicado) {
            return mostrarError(
                    "Documento duplicado",
                    "Esta persona ya tiene registrado un documento de este tipo con ese mismo valor."
            );
        }

        String nombreTipo = tipo.getNombre() == null
                ? "" : tipo.getNombre().trim().toLowerCase();

        if (TIPOS_UNICOS.contains(nombreTipo)
                && documentoDAO.existeOtroDocumentoDelTipo(
                        registro.getIdPersona().getIdPersona(),
                        registro.getIdTipoDocumento().getIdTipoDocumento(),
                        registro.getIdDocumento())) {
            return mostrarError(
                    "Documento ya registrado",
                    "Esta persona ya tiene un documento de tipo "
                    + tipo.getNombre() + " registrado."
            );
        }

        if (TIPOS_GLOBALES_UNICOS.contains(nombreTipo)
                && documentoDAO.existeValorEnOtraPersona(
                        registro.getIdPersona().getIdPersona(),
                        registro.getIdTipoDocumento().getIdTipoDocumento(),
                        registro.getValor(),
                        registro.getIdDocumento())) {
            return mostrarError(
                    "Documento duplicado",
                    "Otra persona ya tiene registrado un documento de tipo "
                    + tipo.getNombre() + " con este valor."
            );
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