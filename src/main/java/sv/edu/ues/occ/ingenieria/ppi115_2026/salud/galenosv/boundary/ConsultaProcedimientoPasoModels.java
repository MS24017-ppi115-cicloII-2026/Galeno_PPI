package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DAOInterface;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ConsultaProcedimientoPasoDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ConsultaProcedimientoPaso;

@Named
@ViewScoped
public class ConsultaProcedimientoPasoModels extends AbstractModel<ConsultaProcedimientoPaso> {

    @Inject
    ConsultaProcedimientoPasoDAO consultaProcedimientoPasoDAO;

    @Override
    protected DAOInterface<ConsultaProcedimientoPaso> getDAO() {
        return consultaProcedimientoPasoDAO;
    }

    @Override
    protected ConsultaProcedimientoPaso crearRegistroNuevo() {
        return new ConsultaProcedimientoPaso(UUID.randomUUID());
    }

    @Override
    protected UUID obtenerId(ConsultaProcedimientoPaso registro) {
        return registro.getIdConsultaProcedimientoPaso();
    }

    private boolean validarRegistro() {
        if (registro == null) {
            return mostrarError(
                    "Registro inválido",
                    "El registro no puede ser nulo."
            );
        }

        if (registro.getIdConsultaProcedimiento() == null) {
            return mostrarError(
                    "Consulta-procedimiento requerido",
                    "Debe seleccionar una consulta-procedimiento."
            );
        }

        if (registro.getIdPersonaRol() == null) {
            return mostrarError(
                    "Persona requerida",
                    "Debe seleccionar la persona responsable del paso."
            );
        }

        if (registro.getIdPersonaRol().getIdPersona() == null) {
            return mostrarError(
                    "Persona requerida",
                    "La persona seleccionada no tiene una persona asociada."
            );
        }

        if (registro.getEstado() == null
                || registro.getEstado().isBlank()) {
            return mostrarError(
                    "Estado requerido",
                    "El estado no puede estar vacío."
            );
        }
        if (registro.getFechaInicio() == null) {
            return mostrarError(
                    "Fecha requerida",
                    "El paso de la consulta debe tener una fecha de inicio."
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
