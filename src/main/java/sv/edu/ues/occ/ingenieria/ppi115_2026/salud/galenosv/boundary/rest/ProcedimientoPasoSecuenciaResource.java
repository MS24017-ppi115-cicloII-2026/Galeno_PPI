package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import java.io.Serializable;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DAOInterface;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ProcedimientoPasoSecuenciaDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ProcedimientoPasoSecuencia;

@Path("procedimiento_paso_secuencia")
public class ProcedimientoPasoSecuenciaResource extends DefaultResource<ProcedimientoPasoSecuencia> implements Serializable {

    @Inject
    ProcedimientoPasoSecuenciaDAO procedimientoPasoSecuenciaDAO;

    @Override
    protected DAOInterface<ProcedimientoPasoSecuencia> getDAO() {
        return procedimientoPasoSecuenciaDAO;
    }

    @Override
    protected UUID obtenerId(ProcedimientoPasoSecuencia registro) {
        return registro.getIdProcedimientoPasoSecuencia();
    }

    @Override
    protected void asignarId(ProcedimientoPasoSecuencia registro, UUID id) {
        registro.setIdProcedimientoPasoSecuencia(id);
    }
}