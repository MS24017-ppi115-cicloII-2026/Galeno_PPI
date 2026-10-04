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
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ClinicaDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DAOInterface;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.PersonaRolDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.RolDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Clinica;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Persona;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.PersonaRol;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Rol;

@Named("personaRolModel")
@ViewScoped
public class PersonaRolModels extends AbstractModel<PersonaRol> {

    @Inject
    private PersonaRolDAO personaRolDAO;
    @Inject
    private RolDAO rolDAO;
    @Inject
    private ClinicaDAO clinicaDAO;
    @Inject
    private PersonaModels personaModel;

    private UUID personaCargada;
    private boolean cargado;

    @Override
    protected DAOInterface<PersonaRol> getDAO() {
        return personaRolDAO;
    }

    @Override
    protected PersonaRol crearRegistroNuevo() {
        PersonaRol personaRol = new PersonaRol();
        personaRol.setIdPersonaRol(UUID.randomUUID());
        personaRol.setFechaCreacion(new Date());
        personaRol.setIdPersona(personaModel.getPersonaSeleccionada());
        return personaRol;
    }

    @Override
    protected UUID obtenerId(PersonaRol registro) {
        return registro.getIdPersonaRol();
    }

    // ---- Lista filtrada por la persona seleccionada ----
    private List<PersonaRol> cargarFiltrado() {
        Persona persona = personaModel.getPersonaSeleccionada();
        if (persona == null) {
            // Sin persona seleccionada (pantalla PersonaRol.xhtml): lista completa
            return personaRolDAO.findRange(0, 100);
        }
        return personaRolDAO.buscarPorPersona(persona);
    }

    public List<PersonaRol> getRegistrosDePersona() {
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

    public List<Rol> getRolesActivos() {
        return rolDAO.buscarRolesActivos();
    }

    public List<Clinica> getClinicasActivas() {
        return clinicaDAO.buscarPorActivo(true);
    }

    private boolean validarRegistro() {
        if (registro == null) {
            return mostrarError(
                    "Registro inválido",
                    "La asignación de rol no puede ser nula."
            );
        }

        if (registro.getIdPersona() == null) {
            return mostrarError(
                    "Persona requerida",
                    "Debe seleccionar una persona."
            );
        }

        if (registro.getIdRol() == null) {
            return mostrarError(
                    "Rol requerido",
                    "Debe seleccionar un rol."
            );
        }

        if (registro.getIdClinica() == null) {
            return mostrarError(
                    "Clínica requerida",
                    "Debe seleccionar una clínica."
            );
        }

        Rol rol = rolDAO.buscar(registro.getIdRol().getIdRol());

        if (rol == null) {
            return mostrarError(
                    "Rol inválido",
                    "El rol seleccionado no existe."
            );
        }

        if (!Boolean.TRUE.equals(rol.getActivo())) {
            return mostrarError(
                    "Rol inactivo",
                    "No se puede asignar un rol inactivo."
            );
        }

        Clinica clinica = clinicaDAO.buscar(registro.getIdClinica().getIdClinica());

        if (clinica == null) {
            return mostrarError(
                    "Clínica inválida",
                    "La clínica seleccionada no existe."
            );
        }

        if (!Boolean.TRUE.equals(clinica.getActivo())) {
            return mostrarError(
                    "Clínica inactiva",
                    "No se puede asignar una clínica inactiva."
            );
        }

        if (existeMismoRolEnClinica()) {
            return mostrarError(
                    "Rol duplicado",
                    "Esta persona ya tiene ese rol asignado en esa clínica."
            );
        }

        if (registro.getFechaCreacion() == null) {
            registro.setFechaCreacion(new Date());
        }

        return true;
    }

    private boolean existeMismoRolEnClinica() {
        List<PersonaRol> actuales = personaRolDAO.buscarPorPersona(registro.getIdPersona());
        if (actuales == null) {
            return false;
        }
        UUID idRol = registro.getIdRol().getIdRol();
        UUID idClinica = registro.getIdClinica().getIdClinica();

        return actuales.stream().anyMatch(pr
                -> !Objects.equals(pr.getIdPersonaRol(), registro.getIdPersonaRol())
                && pr.getIdRol() != null
                && pr.getIdClinica() != null
                && Objects.equals(pr.getIdRol().getIdRol(), idRol)
                && Objects.equals(pr.getIdClinica().getIdClinica(), idClinica));
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
