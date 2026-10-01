package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.List;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DAOInterface;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ConsultaDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Consulta;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.PersonaRol;

@Named
@ViewScoped
public class ConsultaModels extends AbstractModel<Consulta> {

    @Inject
    ConsultaDAO consultaDAO;

    @Override
    protected DAOInterface<Consulta> getDAO() {
        return consultaDAO;
    }
    private static final String ROL_PACIENTE = "paciente";

    private List<PersonaRol> pacientes;

    public List<PersonaRol> getPacientes() {
        if (pacientes == null) {
            pacientes = consultaDAO.buscarPersonasPorNombreRol(ROL_PACIENTE);
        }
        return pacientes;
    }

    @Override
    protected Consulta crearRegistroNuevo() {
        return new Consulta(UUID.randomUUID());
    }

    @Override
    protected UUID obtenerId(Consulta registro) {
        return registro.getIdConsulta();
    }

    private boolean validarRegistro() {
        if (registro == null) {
            return mostrarError(
                    "Registro inválido",
                    "La consulta no puede ser nula."
            );
        }
        //
        UUID idSeleccionado = registro.getIdPersonaRol().getIdPersonaRol();
        boolean esPaciente = getPacientes().stream()
                .anyMatch(p -> p.getIdPersonaRol().equals(idSeleccionado));

        if (!esPaciente) {
            return mostrarError(
                    "Paciente inválido",
                    "La persona seleccionada no tiene el rol de paciente."
            );
        }
        //
        if (registro.getIdPersonaRol() == null) {
            return mostrarError(
                    "Persona requerida",
                    "Debe seleccionar la persona de la consulta."
            );
        }

        if (registro.getFechaInicio() == null) {
            return mostrarError(
                    "Fecha requerida",
                    "La consulta debe tener una fecha de inicio."
            );
        }
        if (registro.getFechaFin() != null
                && registro.getFechaFin().before(registro.getFechaInicio())) {
            return mostrarError(
                    "Fechas inválidas",
                    "La fecha de fin no puede ser anterior a la fecha de inicio."
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

    @Override
    public void btnCrearhandler(ActionEvent ae) {
        if (validarRegistro()) {
            super.btnCrearhandler(ae);
        }
    }

    @Override
    public void btnModificarHandler() {
        if (validarRegistro()) {
            super.btnModificarHandler();
        }
    }
}
