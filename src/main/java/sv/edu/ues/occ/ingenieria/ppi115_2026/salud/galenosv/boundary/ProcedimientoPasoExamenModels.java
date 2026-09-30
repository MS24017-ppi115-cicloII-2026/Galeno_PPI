package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DAOInterface;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ExamenDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ProcedimientoPasoDAO;
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

    @Inject
    ProcedimientoPasoDAO procedimientoPasoDAO;

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

    /**
     * Devuelve exámenes activos más el actual del registro.
     */
    public List<Examen> getExamenes() {
        List<Examen> lista = new ArrayList<>(examenDAO.findRange(0, 1000).stream()
                .filter(e -> Boolean.TRUE.equals(e.getActivo()))
                .toList());
        if (registro != null && registro.getIdExamen() != null
                && lista.stream().noneMatch(e -> e.getIdExamen().equals(registro.getIdExamen().getIdExamen()))) {
            Examen actual = examenDAO.buscar(registro.getIdExamen().getIdExamen());
            if (actual != null) {
                lista.add(actual);
            }
        }
        return lista;
    }

    private boolean vinculoValido() {
        Examen ex = (registro.getIdExamen() != null)
                ? examenDAO.buscar(registro.getIdExamen().getIdExamen()) : null;
        ProcedimientoPaso pp = (registro.getIdProcedimientoPaso() != null)
                ? procedimientoPasoDAO.buscar(registro.getIdProcedimientoPaso().getIdProcedimientoPaso()) : null;

        boolean crear = estado == Estado_CRUD.CREAR;

        if (crear && ex != null && !Boolean.TRUE.equals(ex.getActivo())) {
            rechazar("El examen \"" + ex.getNombre() + "\" está inactivo.");
            return false;
        }
        if (crear && pp != null && pp.getIdProcedimiento() != null
                && !Boolean.TRUE.equals(pp.getIdProcedimiento().getActivo())) {
            rechazar("El procedimiento \"" + pp.getIdProcedimiento().getNombre() + "\" está inactivo.");
            return false;
        }
        if (ex != null && pp != null) {
            boolean duplicado = getRegistrosPorPaso(pp.getIdProcedimientoPaso()).stream()
                    .anyMatch(x -> x.getIdExamen().getIdExamen().equals(ex.getIdExamen())
                            && !x.getIdProcedimientoPasoExamen().equals(registro.getIdProcedimientoPasoExamen()));
            if (duplicado) {
                rechazar("El examen \"" + ex.getNombre() + "\" ya está asignado a este paso.");
                return false;
            }
        }
        return true;
    }

    private void rechazar(String detalle) {
        fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                "No se puede guardar", detalle));
        fc.validationFailed();
    }

    @Override
    public void btnCrearhandler(ActionEvent ae) {
        if (registro != null && !vinculoValido()) {
            return;
        }
        super.btnCrearhandler(ae);
    }

    @Override
    public void btnModificarHandler() {
        if (registro != null && !vinculoValido()) {
            return;
        }
        super.btnModificarHandler();
    }
}