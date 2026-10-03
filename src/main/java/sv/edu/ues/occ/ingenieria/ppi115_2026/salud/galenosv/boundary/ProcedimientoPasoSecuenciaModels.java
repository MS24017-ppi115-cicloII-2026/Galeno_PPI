package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

    private static final int MAX_TIPO_SECUENCIA = 100;

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
    
    public List<ProcedimientoPasoSecuencia> getRegistrosPorPaso(
            UUID idProcedimientoPaso) {

        if (idProcedimientoPaso == null) {
            return List.of();
        }

        return procedimientoPasoSecuenciaDAO
                .buscarPorProcedimientoPaso(idProcedimientoPaso);
    }

   
    public List<ProcedimientoPaso> getPasosPorProcedimiento(
            UUID idProcedimiento) {

        if (idProcedimiento == null) {
            return List.of();
        }

        return procedimientoPasoDAO.buscarPorProcedimiento(idProcedimiento);
    }

    
    private boolean hayCiclo(UUID paso, UUID ref) {
        Set<UUID> visitados = new HashSet<>();
        Deque<UUID> pendientes = new ArrayDeque<>();
        pendientes.push(ref);
        while (!pendientes.isEmpty()) {
            UUID actual = pendientes.pop();
            if (actual.equals(paso)) {
                return true;
            }
            if (!visitados.add(actual)) {
                continue;
            }
            for (ProcedimientoPasoSecuencia s : getRegistrosPorPaso(actual)) {
                if (s.getIdProcedimientoPasoReferencia() != null
                        && !s.getIdProcedimientoPasoSecuencia().equals(registro.getIdProcedimientoPasoSecuencia())) {
                    pendientes.push(s.getIdProcedimientoPasoReferencia());
                }
            }
        }
        return false;
    }

    private boolean vinculoValido() {
        if (registro.getTipoSecuencia() == null || registro.getTipoSecuencia().isBlank()) {
            rechazar("El tipo de secuencia es obligatorio.");
            return false;
        }
        String tipo = registro.getTipoSecuencia().trim().toUpperCase();
        if (tipo.length() > MAX_TIPO_SECUENCIA) {
            rechazar("El tipo de secuencia no puede exceder " + MAX_TIPO_SECUENCIA + " caracteres.");
            return false;
        }
        registro.setTipoSecuencia(tipo);

        ProcedimientoPaso paso = (registro.getIdProcedimientoPaso() != null)
                ? procedimientoPasoDAO.buscar(registro.getIdProcedimientoPaso().getIdProcedimientoPaso()) : null;
        ProcedimientoPaso ref = (registro.getIdProcedimientoPasoReferencia() != null)
                ? procedimientoPasoDAO.buscar(registro.getIdProcedimientoPasoReferencia()) : null;

        if (paso != null && ref != null) {
            if (paso.getIdProcedimientoPaso().equals(ref.getIdProcedimientoPaso())) {
                rechazar("Un paso no puede depender de sí mismo.");
                return false;
            }
            if (paso.getIdProcedimiento() != null && ref.getIdProcedimiento() != null
                    && !paso.getIdProcedimiento().getIdProcedimiento()
                            .equals(ref.getIdProcedimiento().getIdProcedimiento())) {
                rechazar("El paso de referencia debe pertenecer al mismo procedimiento.");
                return false;
            }
            boolean duplicado = getRegistrosPorPaso(paso.getIdProcedimientoPaso()).stream()
                    .anyMatch(x -> ref.getIdProcedimientoPaso().equals(x.getIdProcedimientoPasoReferencia())
                            && !x.getIdProcedimientoPasoSecuencia().equals(registro.getIdProcedimientoPasoSecuencia()));
            if (duplicado) {
                rechazar("Esta dependencia ya existe para el paso \"" + paso.getNombre() + "\".");
                return false;
            }
            if (hayCiclo(paso.getIdProcedimientoPaso(), ref.getIdProcedimientoPaso())) {
                rechazar("Dependencia circular: \"" + ref.getNombre() + "\" ya depende, directa o indirectamente, de \""
                        + paso.getNombre() + "\".");
                return false;
            }
        }
        if (estado == Estado_CRUD.CREAR && paso != null && paso.getIdProcedimiento() != null
                && !Boolean.TRUE.equals(paso.getIdProcedimiento().getActivo())) {
            rechazar("El procedimiento \"" + paso.getIdProcedimiento().getNombre() + "\" está inactivo.");
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