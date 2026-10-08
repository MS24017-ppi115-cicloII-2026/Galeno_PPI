package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import java.io.Serializable;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DAOInterface;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ExamenTipoExamenDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ExamenTipoExamen;

@Path("examen_tipo_examen")
public class ExamenTipoExamenResource extends DefaultResource<ExamenTipoExamen> implements Serializable {

    @Inject
    ExamenTipoExamenDAO examenTipoExamenDAO;

    @Override
    protected DAOInterface<ExamenTipoExamen> getDAO() {
        return examenTipoExamenDAO;
    }

    @Override
    protected UUID obtenerId(ExamenTipoExamen registro) {
        return registro.getIdExamenTipoExamen();
    }

    @Override
    protected void asignarId(ExamenTipoExamen registro, UUID id) {
        registro.setIdExamenTipoExamen(id);
    }
}
