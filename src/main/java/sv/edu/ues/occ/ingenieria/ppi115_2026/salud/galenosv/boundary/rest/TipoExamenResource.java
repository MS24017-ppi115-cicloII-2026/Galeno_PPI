package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import java.io.Serializable;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DAOInterface;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.TipoExamenDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.TipoExamen;

@Path("tipo_examen")
public class TipoExamenResource extends DefaultResource<TipoExamen> implements Serializable {

    @Inject
    TipoExamenDAO tipoExamenDAO;

    @Override
    protected DAOInterface<TipoExamen> getDAO() {
        return tipoExamenDAO;
    }

    @Override
    protected UUID obtenerId(TipoExamen registro) {
        return registro.getIdTipoExamen();
    }

    @Override
    protected void asignarId(TipoExamen registro, UUID id) {
        registro.setIdTipoExamen(id);
    }

    // Todo registro nuevo nace activo (igual que en el ejemplo del ingeniero)
    @Override
    protected void antesDeCrear(TipoExamen nuevo) {
        nuevo.setActivo(Boolean.TRUE);
    }
}
