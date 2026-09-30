package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DAOInterface;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ExamenResultadoDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ExamenResultado;

@Named
@ViewScoped
public class ExamenResultadoModels extends AbstractModel<ExamenResultado> {

    @Inject
    ExamenResultadoDAO examenResultadoDAO;

    @Override
    protected DAOInterface<ExamenResultado> getDAO() {
        return examenResultadoDAO;
    }

    @Override
    protected ExamenResultado crearRegistroNuevo() {
        return new ExamenResultado(UUID.randomUUID());
    }

    @Override
    protected UUID obtenerId(ExamenResultado registro) {
        return registro.getIdExamenResultado();
    }

    private boolean validarRegistro() {
        if (registro == null) {
            return mostrarError(
                    "Registro inválido",
                    "El resultado de examen no puede ser nulo."
            );
        }

        if (registro.getIdOrdenExamen() == null) {
            return mostrarError(
                    "Orden de examen requerida",
                    "Debe seleccionar una orden de examen."
            );
        }

        if (registro.getResultado() == null
                || registro.getResultado().isBlank()) {
            return mostrarError(
                    "Resultado requerido",
                    "El resultado no puede estar vacío."
            );
        }
        if (registro.getFechaCreacion() == null) {
            return mostrarError(
                    "Fecha requerida",
                    "El resultado de examen debe tener una fecha de creación."
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
