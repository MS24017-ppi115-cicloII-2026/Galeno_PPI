package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import java.io.Serializable;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DAOInterface;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ExamenDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Examen;

@Path("examen")
public class ExamenResource extends DefaultResource<Examen> implements Serializable {

    @Inject
    ExamenDAO examenDAO;

    @Override
    protected DAOInterface<Examen> getDAO() {
        return examenDAO;
    }

    @Override
    protected UUID obtenerId(Examen registro) {
        return registro.getIdExamen();
    }

    @Override
    protected void asignarId(Examen registro, UUID id) {
        registro.setIdExamen(id);
    }

    // Todo registro nuevo nace activo (igual que en el ejemplo del ingeniero)
    @Override
    protected void antesDeCrear(Examen nuevo) {
        nuevo.setActivo(Boolean.TRUE);
    }
}
