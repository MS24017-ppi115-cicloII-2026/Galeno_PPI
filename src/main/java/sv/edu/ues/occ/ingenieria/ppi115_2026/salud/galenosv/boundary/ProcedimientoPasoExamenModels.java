package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.List;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DAOInterface;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ExamenDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ProcedimientoPasoExamenDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Examen;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ProcedimientoPaso;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ProcedimientoPasoExamen;

@Named
@ViewScoped
public class ProcedimientoPasoExamenModels extends AbstractModel<ProcedimientoPasoExamen> {

    @Inject
    ProcedimientoPasoExamenDAO procedimientoPasoExamenDAO;

    @Inject
    ExamenDAO examenDAO;

    @Override
    protected DAOInterface<ProcedimientoPasoExamen> getDAO() {
        return procedimientoPasoExamenDAO;
    }

    @Override
    protected ProcedimientoPasoExamen crearRegistroNuevo() {
        ProcedimientoPasoExamen ppe =
                new ProcedimientoPasoExamen(UUID.randomUUID());

        ppe.setActivo(Boolean.TRUE);

        return ppe;
    }

    @Override
    protected UUID obtenerId(ProcedimientoPasoExamen registro) {
        return registro.getIdProcedimientoPasoExamen();
    }

    /**
     * Prepara un nuevo examen para el paso actual.
     */
    public void prepararNuevoParaPaso(UUID idProcedimientoPaso) {

        this.registro = crearRegistroNuevo();

        if (idProcedimientoPaso != null) {
            this.registro.setIdProcedimientoPaso(
                    new ProcedimientoPaso(idProcedimientoPaso)
            );
        }

        this.estado = Estado_CRUD.CREAR;
    }

    /**
     * Devuelve únicamente los exámenes asignados
     * al paso seleccionado.
     */
    public List<ProcedimientoPasoExamen> getRegistrosPorPaso(
            UUID idProcedimientoPaso) {

        if (idProcedimientoPaso == null) {
            return List.of();
        }

        return procedimientoPasoExamenDAO
                .buscarPorProcedimientoPaso(idProcedimientoPaso);
    }

    public List<Examen> getExamenes() {
        return examenDAO.findRange(0, 1000);
    }
}