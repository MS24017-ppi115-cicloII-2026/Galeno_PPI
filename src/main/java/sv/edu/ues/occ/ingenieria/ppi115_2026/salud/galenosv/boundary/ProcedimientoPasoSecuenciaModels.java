package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.List;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DAOInterface;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ProcedimientoPasoDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ProcedimientoPasoSecuenciaDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ProcedimientoPaso;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ProcedimientoPasoSecuencia;

@Named
@ViewScoped
public class ProcedimientoPasoSecuenciaModels
        extends AbstractModel<ProcedimientoPasoSecuencia> {

    @Inject
    ProcedimientoPasoSecuenciaDAO procedimientoPasoSecuenciaDAO;

    @Inject
    ProcedimientoPasoDAO procedimientoPasoDAO;

    @Override
    protected DAOInterface<ProcedimientoPasoSecuencia> getDAO() {
        return procedimientoPasoSecuenciaDAO;
    }

    @Override
    protected ProcedimientoPasoSecuencia crearRegistroNuevo() {
        return new ProcedimientoPasoSecuencia(UUID.randomUUID());
    }

    @Override
    protected UUID obtenerId(ProcedimientoPasoSecuencia registro) {
        return registro.getIdProcedimientoPasoSecuencia();
    }

    /**
     * Prepara una nueva secuencia para el paso actual.
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
     * Devuelve únicamente las secuencias del paso actual.
     */
    public List<ProcedimientoPasoSecuencia> getRegistrosPorPaso(
            UUID idProcedimientoPaso) {

        if (idProcedimientoPaso == null) {
            return List.of();
        }

        return procedimientoPasoSecuenciaDAO
                .buscarPorProcedimientoPaso(idProcedimientoPaso);
    }

    /**
     * Devuelve los pasos pertenecientes al procedimiento actual.
     * Se utiliza para seleccionar el paso de referencia.
     */
    public List<ProcedimientoPaso> getPasosPorProcedimiento(
            UUID idProcedimiento) {

        if (idProcedimiento == null) {
            return List.of();
        }

        return procedimientoPasoDAO.buscarPorProcedimiento(idProcedimiento);
    }
}