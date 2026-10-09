package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DAOInterface;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.RolDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Rol;

@Path("rol")
public class RolResource extends DefaultResource<Rol> implements Serializable {

    @Inject
    RolDAO rolDAO;

    @Override
    protected DAOInterface<Rol> getDAO() {
        return rolDAO;
    }

    @Override
    protected UUID obtenerId(Rol registro) {
        return registro.getIdRol();
    }

    @Override
    protected void asignarId(Rol registro, UUID id) {
        registro.setIdRol(id);
    }

    // Todo registro nuevo nace activo (igual que en el ejemplo del ingeniero)
    @Override
    protected void antesDeCrear(Rol nuevo) {
        nuevo.setActivo(Boolean.TRUE);
    }

    // GET .../rol/activos
    @GET
    @Path("activos")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Rol> activos() {
        return rolDAO.buscarRolesActivos();
    }
}
