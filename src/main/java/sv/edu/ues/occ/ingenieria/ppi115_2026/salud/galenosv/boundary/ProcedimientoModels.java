package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DAOInterface;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ProcedimientoDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Procedimiento;

@Named
@ViewScoped
public class ProcedimientoModels extends AbstractModel<Procedimiento> {

    @Inject
    ProcedimientoDAO procedimientoDAO;

    @Override
    protected DAOInterface<Procedimiento> getDAO() {
        return procedimientoDAO;
    }

    @Override
    protected Procedimiento crearRegistroNuevo() {
        Procedimiento p = new Procedimiento(UUID.randomUUID());
        p.setActivo(Boolean.TRUE);
        return p;
    }

    @Override
    protected UUID obtenerId(Procedimiento registro) {
        return registro.getIdProcedimiento();
    }

    private boolean nombreValido() {
        if (registro.getNombre() == null || registro.getNombre().isBlank()) {
            rechazar("El nombre es obligatorio.");
            return false;
        }
        String nombre = registro.getNombre().trim();
        registro.setNombre(nombre);
        boolean duplicado = procedimientoDAO.buscarPorNombre(nombre).stream()
                .anyMatch(x -> x.getNombre().trim().equalsIgnoreCase(nombre)
                        && !x.getIdProcedimiento().equals(registro.getIdProcedimiento()));
        if (duplicado) {
            rechazar("Ya existe un procedimiento llamado \"" + nombre + "\".");
            return false;
        }
        return true;
    }

    private void rechazar(String detalle) {
        fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                "No se puede guardar", detalle));
        fc.validationFailed();
    }

    @Override
    public void btnCrearhandler(ActionEvent ae) {
        if (registro != null && !nombreValido()) {
            return;
        }
        super.btnCrearhandler(ae);
    }

    @Override
    public void btnModificarHandler() {
        if (registro != null && !nombreValido()) {
            return;
        }
        super.btnModificarHandler();
    }
}