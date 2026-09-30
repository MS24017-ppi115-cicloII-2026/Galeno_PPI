package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DAOInterface;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ConsultaProcedimientoDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ConsultaProcedimiento;

@Named
@ViewScoped
public class ConsultaProcedimientoModels extends AbstractModel<ConsultaProcedimiento> {

    @Inject
    ConsultaProcedimientoDAO consultaProcedimientoDAO;

    @Override
    protected DAOInterface<ConsultaProcedimiento> getDAO() {
        return consultaProcedimientoDAO;
    }

    @Override
    protected ConsultaProcedimiento crearRegistroNuevo() {
        return new ConsultaProcedimiento(UUID.randomUUID());
    }

    @Override
    protected UUID obtenerId(ConsultaProcedimiento registro) {
        return registro.getIdConsultaProcedimiento();
    }

    private boolean validarRegistro() {
        if (registro == null) {
            return mostrarError(
                    "Registro inválido",
                    "El registro no puede ser nulo."
            );
        }

        if (registro.getIdConsulta() == null) {
            return mostrarError(
                    "Consulta requerida",
                    "Debe seleccionar una consulta."
            );
        }

        if (registro.getIdProcedimiento() == null) {
            return mostrarError(
                    "Procedimiento requerido",
                    "Debe seleccionar un procedimiento."
            );
        }
        if (registro.getFechaInicio() == null) {
            return mostrarError(
                    "Fecha requerida",
                    "El procedimiento de la consulta debe tener una fecha de inicio."
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
