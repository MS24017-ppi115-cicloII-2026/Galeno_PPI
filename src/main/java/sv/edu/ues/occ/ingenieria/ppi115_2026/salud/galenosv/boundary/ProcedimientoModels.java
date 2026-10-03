package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DAOInterface;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ProcedimientoDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ProcedimientoPasoDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ProcedimientoPasoExamenDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Procedimiento;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ProcedimientoPaso;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ProcedimientoPasoExamen;

@Named
@ViewScoped
public class ProcedimientoModels extends AbstractModel<Procedimiento> {

    @Inject
    ProcedimientoDAO procedimientoDAO;

    @Inject
    ProcedimientoPasoDAO procedimientoPasoDAO;

    @Inject
    ProcedimientoPasoExamenDAO procedimientoPasoExamenDAO;

    @Inject
    ProcedimientoPasoModels procedimientoPasoModels;

   
    private TreeNode<Nodo> nodoSeleccionado;

    public TreeNode<Nodo> getNodoSeleccionado() {
        return nodoSeleccionado;
    }

    public void setNodoSeleccionado(TreeNode<Nodo> nodoSeleccionado) {
        this.nodoSeleccionado = nodoSeleccionado;
    }

   
    public void abrirEditorPaso() {
        Nodo datos = (nodoSeleccionado != null) ? nodoSeleccionado.getData() : null;
        if (datos == null || datos.getPaso() == null) {
            rechazar("Seleccione un paso de la tabla.");
            return;
        }
        procedimientoPasoModels.btnSeleccionarRegistro(
                datos.getPaso().getIdProcedimientoPaso());
    }

    
    public void editarProcedimiento(UUID id) {
        this.registro = null;
        this.estado = Estado_CRUD.NINGUNO;
        if (id == null || getregistros() == null
                || getregistros().stream().noneMatch(r -> id.equals(obtenerId(r)))) {
            rechazar("Seleccione un procedimiento de la tabla.");
            return;
        }
        btnSeleccionarRegistro(id);
        procedimientoPasoModels.btnCancelar();
        this.nodoSeleccionado = null;
    }

   
    public void editarSeleccionado() {
        UUID id = (registro != null) ? registro.getIdProcedimiento() : null;
        editarProcedimiento(id);
    }

   
    public void abrirPorParametro() {
        String crudo = fc.getExternalContext().getRequestParameterMap().get("idProc");
        UUID id = null;
        try {
            id = (crudo != null && !crudo.isBlank()) ? UUID.fromString(crudo) : null;
        } catch (IllegalArgumentException ex) {
            id = null;
        }
        editarProcedimiento(id);
    }

    @Override
    public void btnNuevoHandler(ActionEvent ae) {
        super.btnNuevoHandler(ae);
        procedimientoPasoModels.btnCancelar();
        this.nodoSeleccionado = null;
    }

    
    public TreeNode<Nodo> getArbolPasos() {
        DefaultTreeNode<Nodo> raiz = new DefaultTreeNode<>("root", null, null);

        if (registro == null || registro.getIdProcedimiento() == null) {
            return raiz;
        }

        List<ProcedimientoPaso> pasos = procedimientoPasoDAO
                .buscarPorProcedimiento(registro.getIdProcedimiento());

        TreeNode<Nodo> padre = raiz;
        for (ProcedimientoPaso paso : pasos) {
            DefaultTreeNode<Nodo> nodoPaso = new DefaultTreeNode<>("paso",
                    new Nodo(null, paso,
                            procedimientoPasoExamenDAO.buscarPorProcedimientoPaso(
                                    paso.getIdProcedimientoPaso()),
                            0),
                    padre);
            padre = nodoPaso;
        }

        return raiz;
    }

    @Override
    protected DAOInterface<Procedimiento> getDAO() {
        return procedimientoDAO;
    }

    @Override
    protected Procedimiento crearRegistroNuevo() {
        Procedimiento p = new Procedimiento(UUID.randomUUID());
        p.setActivo(Boolean.TRUE);
        return p;
    }

    @Override
    protected UUID obtenerId(Procedimiento registro) {
        return registro.getIdProcedimiento();
    }

    private boolean nombreValido() {
        if (registro.getNombre() == null || registro.getNombre().isBlank()) {
            rechazar("El nombre es obligatorio.");
            return false;
        }
        String nombre = registro.getNombre().trim();
        registro.setNombre(nombre);
        boolean duplicado = procedimientoDAO.buscarPorNombre(nombre).stream()
                .anyMatch(x -> x.getNombre().trim().equalsIgnoreCase(nombre)
                        && !x.getIdProcedimiento().equals(registro.getIdProcedimiento()));
        if (duplicado) {
            rechazar("Ya existe un procedimiento llamado \"" + nombre + "\".");
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
        if (registro != null && !nombreValido()) {
            return;
        }
        super.btnCrearhandler(ae);
    }

    @Override
    public void btnModificarHandler() {
        if (registro != null && !nombreValido()) {
            return;
        }
        super.btnModificarHandler();
    }

    
    public static class Nodo implements Serializable {

        private static final long serialVersionUID = 1L;

        private final Procedimiento procedimiento;
        private final ProcedimientoPaso paso;
        private final List<ProcedimientoPasoExamen> examenes;
        private final int totalPasos;

        public Nodo(Procedimiento procedimiento, ProcedimientoPaso paso,
                List<ProcedimientoPasoExamen> examenes, int totalPasos) {
            this.procedimiento = procedimiento;
            this.paso = paso;
            this.examenes = examenes;
            this.totalPasos = totalPasos;
        }

        public Procedimiento getProcedimiento() {
            return procedimiento;
        }

        public ProcedimientoPaso getPaso() {
            return paso;
        }

        public List<ProcedimientoPasoExamen> getExamenes() {
            return examenes;
        }

        public int getTotalPasos() {
            return totalPasos;
        }

        public String getNombresExamenes() {
            return examenes.stream()
                    .map(x -> x.getIdExamen().getNombre())
                    .collect(Collectors.joining(", "));
        }
    }
}
