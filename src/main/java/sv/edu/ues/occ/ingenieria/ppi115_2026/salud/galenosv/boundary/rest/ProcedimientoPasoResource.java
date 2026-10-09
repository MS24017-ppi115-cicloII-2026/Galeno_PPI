package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import java.io.Serializable;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DAOInterface;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ProcedimientoPasoDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ProcedimientoPaso;

@Path("procedimiento_paso")
public class ProcedimientoPasoResource extends DefaultResource<ProcedimientoPaso> implements Serializable {

    @Inject
    ProcedimientoPasoDAO procedimientoPasoDAO;

    @Override
    protected DAOInterface<ProcedimientoPaso> getDAO() {
        return procedimientoPasoDAO;
    }

    @Override
    protected UUID obtenerId(ProcedimientoPaso registro) {
        return registro.getIdProcedimientoPaso();
    }

    @Override
    protected void asignarId(ProcedimientoPaso registro, UUID id) {
        registro.setIdProcedimientoPaso(id);
    }
}