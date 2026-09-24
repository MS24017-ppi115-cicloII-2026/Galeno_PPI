package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;
 
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
        OrdenExamen oe = new OrdenExamen(UUID.randomUUID());
        return oe;
    }
 
    @Override
    protected UUID obtenerId(OrdenExamen registro) {
        return registro.getIdOrdenExamen();
    }
}