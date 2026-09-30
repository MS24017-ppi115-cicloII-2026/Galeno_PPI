package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DAOInterface;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.TipoExamenDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.TipoExamen;

@Named
@ViewScoped
public class TipoExamenModels extends AbstractModel<TipoExamen> {

    @Inject
    TipoExamenDAO tipoExamenDAO;

    @Override
    protected DAOInterface<TipoExamen> getDAO() {
        return tipoExamenDAO;
    }

    @Override
    protected TipoExamen crearRegistroNuevo() {
        TipoExamen te = new TipoExamen(UUID.randomUUID());
        te.setActivo(Boolean.TRUE);
        return te;
    }

    @Override
    protected UUID obtenerId(TipoExamen registro) {
        return registro.getIdTipoExamen();
    }

    private boolean nombreValido() {
        if (registro.getNombre() == null || registro.getNombre().isBlank()) {
            rechazar("El nombre es obligatorio.");
            return false;
        }
        String nombre = registro.getNombre().trim();
        registro.setNombre(nombre);
        boolean duplicado = tipoExamenDAO.buscarPorNombre(nombre).stream()
                .anyMatch(x -> x.getNombre().trim().equalsIgnoreCase(nombre)
                        && !x.getIdTipoExamen().equals(registro.getIdTipoExamen()));
        if (duplicado) {
            rechazar("Ya existe un tipo de examen llamado \"" + nombre + "\".");
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