package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DAOInterface;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ExamenDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Examen;

@Named
@ViewScoped
public class ExamenModels extends AbstractModel<Examen> {

    @Inject
    ExamenDAO examenDAO;

    @Override
    protected DAOInterface<Examen> getDAO() {
        return examenDAO;
    }

    @Override
    protected Examen crearRegistroNuevo() {
        Examen e = new Examen(UUID.randomUUID());
        // Inicializamos el campo activo que mostraste en tu entidad Examen
        e.setActivo(Boolean.TRUE); 
        return e;
    }

    @Override
    protected UUID obtenerId(Examen registro) {
        return registro.getIdExamen();
    }

    private boolean nombreValido() {
        if (registro.getNombre() == null || registro.getNombre().isBlank()) {
            rechazar("El nombre es obligatorio.");
            return false;
        }
        String nombre = registro.getNombre().trim();
        registro.setNombre(nombre);
        boolean duplicado = examenDAO.buscarPorNombre(nombre).stream()
                .anyMatch(x -> x.getNombre().trim().equalsIgnoreCase(nombre)
                        && !x.getIdExamen().equals(registro.getIdExamen()));
        if (duplicado) {
            rechazar("Ya existe un examen llamado \"" + nombre + "\".");
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