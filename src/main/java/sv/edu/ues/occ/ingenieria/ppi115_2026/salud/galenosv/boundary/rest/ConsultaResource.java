package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import java.io.Serializable;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ConsultaDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DAOInterface;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Consulta;

@Path("consulta")
public class ConsultaResource extends DefaultResource<Consulta> implements Serializable {

    @Inject
    ConsultaDAO consultaDAO;

    @Override
    protected DAOInterface<Consulta> getDAO() {
        return consultaDAO;
    }

    @Override
    protected UUID obtenerId(Consulta registro) {
        return registro.getIdConsulta();
    }

    @Override
    protected void asignarId(Consulta registro, UUID id) {
        registro.setIdConsulta(id);
    }
}
