package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import java.io.Serializable;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DAOInterface;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ProcedimientoDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Procedimiento;

@Path("procedimiento")
public class ProcedimientoResource extends DefaultResource<Procedimiento> implements Serializable {

    @Inject
    ProcedimientoDAO procedimientoDAO;

    @Override
    protected DAOInterface<Procedimiento> getDAO() {
        return procedimientoDAO;
    }

    @Override
    protected UUID obtenerId(Procedimiento registro) {
        return registro.getIdProcedimiento();
    }

    @Override
    protected void asignarId(Procedimiento registro, UUID id) {
        registro.setIdProcedimiento(id);
    }

    // Todo registro nuevo nace activo (igual que en el ejemplo del ingeniero)
    @Override
    protected void antesDeCrear(Procedimiento nuevo) {
        nuevo.setActivo(Boolean.TRUE);
    }
}
