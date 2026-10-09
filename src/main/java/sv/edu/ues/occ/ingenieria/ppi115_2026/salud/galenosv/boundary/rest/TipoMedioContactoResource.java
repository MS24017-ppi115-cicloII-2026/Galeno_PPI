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
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.TipoMedioContactoDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.TipoMedioContacto;

@Path("tipo_medio_contacto")
public class TipoMedioContactoResource extends DefaultResource<TipoMedioContacto> implements Serializable {

    @Inject
    TipoMedioContactoDAO tipoMedioContactoDAO;

    @Override
    protected DAOInterface<TipoMedioContacto> getDAO() {
        return tipoMedioContactoDAO;
    }

    @Override
    protected UUID obtenerId(TipoMedioContacto registro) {
        return registro.getIdTipoMedioContacto();
    }

    @Override
    protected void asignarId(TipoMedioContacto registro, UUID id) {
        registro.setIdTipoMedioContacto(id);
    }

    // Todo registro nuevo nace activo (igual que en el ejemplo del ingeniero)
    @Override
    protected void antesDeCrear(TipoMedioContacto nuevo) {
        nuevo.setActivo(Boolean.TRUE);
    }

    // GET .../tipo_medio_contacto/activos
    @GET
    @Path("activos")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TipoMedioContacto> activos() {
        return tipoMedioContactoDAO.buscarPorActivo(true);
    }
}
