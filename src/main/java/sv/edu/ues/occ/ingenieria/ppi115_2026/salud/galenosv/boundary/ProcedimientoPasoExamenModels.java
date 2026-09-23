package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DAOInterface;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ProcedimientoPasoExamenDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ProcedimientoPasoExamen;

@Named
@ViewScoped
public class ProcedimientoPasoExamenModels extends AbstractModel<ProcedimientoPasoExamen> {

    @Inject
    ProcedimientoPasoExamenDAO procedimientoPasoExamenDAO;

    @Override
    protected DAOInterface<ProcedimientoPasoExamen> getDAO() {
        return procedimientoPasoExamenDAO;
    }

    @Override
    protected ProcedimientoPasoExamen crearRegistroNuevo() {
        ProcedimientoPasoExamen ppe = new ProcedimientoPasoExamen(UUID.randomUUID());
        return ppe;
    }

    @Override
    protected UUID obtenerId(ProcedimientoPasoExamen registro) {
        return registro.getIdProcedimientoPasoExamen();
    }
}