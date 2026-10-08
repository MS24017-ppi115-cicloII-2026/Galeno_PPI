package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary.rest;




import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary.rest.DefaultResource;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ClinicaDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DAOInterface;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Clinica;

@Path("clinica")
public class ClinicaResource extends DefaultResource<Clinica> implements Serializable {

    @Inject
    ClinicaDAO clinicaDAO;

    @Override
    protected DAOInterface<Clinica> getDAO() {
        return clinicaDAO;
    }

    @Override
    protected UUID obtenerId(Clinica registro) {
        return registro.getIdClinica();
    }

    @Override
    protected void asignarId(Clinica registro, UUID id) {
        registro.setIdClinica(id);
    }

    // Todo registro nuevo nace activo (igual que en el ejemplo del ingeniero)
    @Override
    protected void antesDeCrear(Clinica nuevo) {
        nuevo.setActivo(Boolean.TRUE);
    }

    // GET .../clinica/activos
    @GET
    @Path("activos")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Clinica> activos() {
        return clinicaDAO.buscarPorActivo(true);
    }
}
