package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DAOInterface;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.OrdenExamenDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.OrdenExamen;

@Named
@ViewScoped
public class OrdenExamenModels extends AbstractModel<OrdenExamen> {

    @Inject
    OrdenExamenDAO ordenExamenDAO;

    @Override
    protected DAOInterface<OrdenExamen> getDAO() {
        return ordenExamenDAO;
    }

    @Override
    protected OrdenExamen crearRegistroNuevo() {
        return new OrdenExamen(UUID.randomUUID());
    }

    @Override
    protected UUID obtenerId(OrdenExamen registro) {
        return registro.getIdOrdenExamen();
    }

    private boolean validarRegistro() {
        if (registro == null) {
            return mostrarError(
                    "Registro inválido",
                    "La orden de examen no puede ser nula."
            );
        }

        if (registro.getIdConsultaProcedimientoPaso() == null) {
            return mostrarError(
                    "Paso de consulta requerido",
                    "Debe seleccionar el paso de la consulta-procedimiento."
            );
        }
        if (registro.getFechaCreacion() == null) {
    return mostrarError(
            "Fecha requerida",
            "La orden de examen debe tener una fecha de creación."
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