package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import java.io.Serializable;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ConsultaProcedimientoDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DAOInterface;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ConsultaProcedimiento;

@Path("consulta_procedimiento")
public class ConsultaProcedimientoResource extends DefaultResource<ConsultaProcedimiento> implements Serializable {

    @Inject
    ConsultaProcedimientoDAO consultaProcedimientoDAO;

    @Override
    protected DAOInterface<ConsultaProcedimiento> getDAO() {
        return consultaProcedimientoDAO;
    }

    @Override
    protected UUID obtenerId(ConsultaProcedimiento registro) {
        return registro.getIdConsultaProcedimiento();
    }

    @Override
    protected void asignarId(ConsultaProcedimiento registro, UUID id) {
        registro.setIdConsultaProcedimiento(id);
    }
}
