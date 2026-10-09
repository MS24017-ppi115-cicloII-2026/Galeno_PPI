package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import java.io.Serializable;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DAOInterface;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ProcedimientoPasoExamenDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ProcedimientoPasoExamen;

@Path("procedimiento_paso_examen")
public class ProcedimientoPasoExamenResource extends DefaultResource<ProcedimientoPasoExamen> implements Serializable {

    @Inject
    ProcedimientoPasoExamenDAO procedimientoPasoExamenDAO;

    @Override
    protected DAOInterface<ProcedimientoPasoExamen> getDAO() {
        return procedimientoPasoExamenDAO;
    }

    @Override
    protected UUID obtenerId(ProcedimientoPasoExamen registro) {
        return registro.getIdProcedimientoPasoExamen();
    }

    @Override
    protected void asignarId(ProcedimientoPasoExamen registro, UUID id) {
        registro.setIdProcedimientoPasoExamen(id);
    }

    // Todo registro nuevo nace activo (igual que en el ejemplo del ingeniero)
    @Override
    protected void antesDeCrear(ProcedimientoPasoExamen nuevo) {
        nuevo.setActivo(Boolean.TRUE);
    }
}
