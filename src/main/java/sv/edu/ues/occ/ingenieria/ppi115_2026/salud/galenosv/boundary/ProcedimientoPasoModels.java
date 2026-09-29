package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.List;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DAOInterface;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ProcedimientoDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ProcedimientoPasoDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.RolDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Procedimiento;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ProcedimientoPaso;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Rol;

@Named
@ViewScoped
public class ProcedimientoPasoModels extends AbstractModel<ProcedimientoPaso> {

    @Inject
    ProcedimientoPasoDAO procedimientoPasoDAO;

    @Inject
    ProcedimientoDAO procedimientoDAO;

    @Inject
    RolDAO rolDAO;

    @Override
    protected DAOInterface<ProcedimientoPaso> getDAO() {
        return procedimientoPasoDAO;
    }

    @Override
    protected ProcedimientoPaso crearRegistroNuevo() {
        ProcedimientoPaso paso = new ProcedimientoPaso(UUID.randomUUID());
        paso.setIndicaFin(Boolean.FALSE);
        return paso;
    }

    @Override
    protected UUID obtenerId(ProcedimientoPaso registro) {
        return registro.getIdProcedimientoPaso();
    }

    /**
     * Prepara un nuevo paso para el procedimiento seleccionado.
     */
    public void prepararNuevoParaProcedimiento(UUID idProcedimiento) {

        this.registro = crearRegistroNuevo();

        if (idProcedimiento != null) {
            this.registro.setIdProcedimiento(
                    new Procedimiento(idProcedimiento)
            );
        }

        this.estado = Estado_CRUD.CREAR;
    }

    /**
     * Devuelve únicamente los pasos pertenecientes
     * al procedimiento actual.
     */
    public List<ProcedimientoPaso> getRegistrosPorProcedimiento(UUID idProcedimiento) {

        if (idProcedimiento == null) {
            return List.of();
        }

        return procedimientoPasoDAO.buscarPorProcedimiento(idProcedimiento);
    }

    /**
     * Devuelve solo procedimientos activos.
     */
    public List<Procedimiento> getProcedimientos() {
        return procedimientoDAO.findRange(0, 1000).stream()
                .filter(p -> Boolean.TRUE.equals(p.getActivo()))
                .toList();
    }

    public List<Rol> getRoles() {
        return rolDAO.findRange(0, 1000);
    }

    private boolean vinculoValido() {
        Procedimiento pr = (registro.getIdProcedimiento() != null)
                ? procedimientoDAO.buscar(registro.getIdProcedimiento().getIdProcedimiento()) : null;

        if (pr != null && !Boolean.TRUE.equals(pr.getActivo())) {
            rechazar("El procedimiento \"" + pr.getNombre() + "\" está inactivo.");
            return false;
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