package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import java.io.Serializable;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DAOInterface;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.OrdenExamenDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.OrdenExamen;

@Path("orden_examen")
public class OrdenExamenResource extends DefaultResource<OrdenExamen> implements Serializable {

    @Inject
    OrdenExamenDAO ordenExamenDAO;

    @Override
    protected DAOInterface<OrdenExamen> getDAO() {
        return ordenExamenDAO;
    }

    @Override
    protected UUID obtenerId(OrdenExamen registro) {
        return registro.getIdOrdenExamen();
    }

    @Override
    protected void asignarId(OrdenExamen registro, UUID id) {
        registro.setIdOrdenExamen(id);
    }
}