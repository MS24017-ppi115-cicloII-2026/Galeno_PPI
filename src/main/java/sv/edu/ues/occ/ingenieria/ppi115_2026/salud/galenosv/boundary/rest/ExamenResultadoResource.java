package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import java.io.Serializable;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DAOInterface;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ExamenResultadoDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ExamenResultado;

@Path("examen_resultado")
public class ExamenResultadoResource extends DefaultResource<ExamenResultado> implements Serializable {

    @Inject
    ExamenResultadoDAO examenResultadoDAO;

    @Override
    protected DAOInterface<ExamenResultado> getDAO() {
        return examenResultadoDAO;
    }

    @Override
    protected UUID obtenerId(ExamenResultado registro) {
        return registro.getIdExamenResultado();
    }

    @Override
    protected void asignarId(ExamenResultado registro, UUID id) {
        registro.setIdExamenResultado(id);
    }
}
