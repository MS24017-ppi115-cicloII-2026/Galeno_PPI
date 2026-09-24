package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;
 
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DAOInterface;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ConsultaDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Consulta;
 
@Named
@ViewScoped
public class ConsultaModels extends AbstractModel<Consulta> {
 
    @Inject
    ConsultaDAO consultaDAO;
 
    @Override
    protected DAOInterface<Consulta> getDAO() {
        return consultaDAO;
    }
 
    @Override
    protected Consulta crearRegistroNuevo() {
        Consulta c = new Consulta(UUID.randomUUID());
        return c;
    }
 
    @Override
    protected UUID obtenerId(Consulta registro) {
        return registro.getIdConsulta();
    }
}