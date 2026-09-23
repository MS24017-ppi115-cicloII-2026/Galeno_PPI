package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DAOInterface;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ProcedimientoPasoSecuenciaDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ProcedimientoPasoSecuencia;

@Named
@ViewScoped
public class ProcedimientoPasoSecuenciaModels extends AbstractModel<ProcedimientoPasoSecuencia> {

    @Inject
    ProcedimientoPasoSecuenciaDAO procedimientoPasoSecuenciaDAO;

    @Override
    protected DAOInterface<ProcedimientoPasoSecuencia> getDAO() {
        return procedimientoPasoSecuenciaDAO;
    }

    @Override
    protected ProcedimientoPasoSecuencia crearRegistroNuevo() {
        ProcedimientoPasoSecuencia pps = new ProcedimientoPasoSecuencia(UUID.randomUUID());
        return pps;
    }

    @Override
    protected UUID obtenerId(ProcedimientoPasoSecuencia registro) {
        // Verifica que el getter coincida con el generado en tu entidad
        return registro.getIdProcedimientoPasoSecuencia();
    }
}