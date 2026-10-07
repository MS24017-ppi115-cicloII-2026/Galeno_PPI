package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DAOInterface;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ExamenDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ProcedimientoDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ProcedimientoPasoDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ProcedimientoPasoExamenDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ProcedimientoPasoSecuenciaDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.RolDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Examen;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Procedimiento;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ProcedimientoPaso;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ProcedimientoPasoExamen;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ProcedimientoPasoSecuencia;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Rol;

@Named
@ViewScoped
public class ProcedimientoPasoModels extends AbstractModel<ProcedimientoPaso> {

    private static final String ROL_PACIENTE = "paciente";

    @Inject
    ProcedimientoPasoDAO procedimientoPasoDAO;

    @Inject
    ProcedimientoDAO procedimientoDAO;

    @Inject
    RolDAO rolDAO;

    @Inject
    ProcedimientoPasoSecuenciaDAO procedimientoPasoSecuenciaDAO;

    @Inject
    ProcedimientoPasoExamenDAO procedimientoPasoExamenDAO;

    @Inject
    ExamenDAO examenDAO;

    @Inject
    ProcedimientoPasoExamenModels procedimientoPasoExamenModels;

    private UUID idPasoPadre;

    private UUID idExamenNuevo;

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

    public UUID getIdPasoPadre() {
        return idPasoPadre;
    }

    public void setIdPasoPadre(UUID idPasoPadre) {
        this.idPasoPadre = idPasoPadre;
    }

    public UUID getIdExamenNuevo() {
        return idExamenNuevo;
    }

    public void setIdExamenNuevo(UUID idExamenNuevo) {
        this.idExamenNuevo = idExamenNuevo;
    }

    
    public void prepararNuevoParaProcedimiento(UUID idProcedimiento) {

        this.registro = crearRegistroNuevo();

        if (idProcedimiento != null) {
            this.registro.setIdProcedimiento(
                    new Procedimiento(idProcedimiento)
            );
        }

        this.idPasoPadre = null;
        this.idExamenNuevo = null;
        this.procedimientoPasoExamenModels.setIdAsociacionSeleccionada(null);
        this.estado = Estado_CRUD.CREAR;
    }

   
    public void prepararNuevoDependiente(UUID idProcedimiento) {
        prepararNuevoParaProcedimiento(idProcedimiento);
        this.idPasoPadre = null;
        if (idProcedimiento != null) {
            List<ProcedimientoPaso> pasos = getRegistrosPorProcedimiento(idProcedimiento);
            if (!pasos.isEmpty()) {
                this.idPasoPadre = pasos.get(pasos.size() - 1).getIdProcedimientoPaso();
            }
        }
    }

    
    public String getNombrePasoPadre() {
        if (idPasoPadre == null) {
            return "";
        }
        ProcedimientoPaso padre = procedimientoPasoDAO.buscar(idPasoPadre);
        return (padre != null && padre.getNombre() != null) ? padre.getNombre() : "";
    }

    
    public boolean hayPasoFinal(UUID idProcedimiento) {
        if (idProcedimiento == null) {
            return false;
        }
        return getRegistrosPorProcedimiento(idProcedimiento).stream()
                .anyMatch(x -> Boolean.TRUE.equals(x.getIndicaFin()));
    }

    @Override
    public void btnSeleccionarRegistro(UUID id) {
        super.btnSeleccionarRegistro(id);
        this.idPasoPadre = null;
        this.idExamenNuevo = null;
        this.procedimientoPasoExamenModels.setIdAsociacionSeleccionada(null);
        if (this.registro != null) {
            List<ProcedimientoPasoSecuencia> dependencias = procedimientoPasoSecuenciaDAO
                    .buscarPorProcedimientoPaso(this.registro.getIdProcedimientoPaso());
            if (!dependencias.isEmpty()) {
                this.idPasoPadre = dependencias.get(0).getIdProcedimientoPasoReferencia();
            }
        }
    }

   
    public List<ProcedimientoPaso> getRegistrosPorProcedimiento(UUID idProcedimiento) {

        if (idProcedimiento == null) {
            return List.of();
        }

        return procedimientoPasoDAO.buscarPorProcedimiento(idProcedimiento);
    }

    
    public List<Procedimiento> getProcedimientos() {
        List<Procedimiento> lista = new ArrayList<>(procedimientoDAO.findRange(0, 1000).stream()
                .filter(p -> Boolean.TRUE.equals(p.getActivo()))
                .toList());
        if (registro != null && registro.getIdProcedimiento() != null
                && lista.stream().noneMatch(p -> p.getIdProcedimiento()
                        .equals(registro.getIdProcedimiento().getIdProcedimiento()))) {
            Procedimiento actual = procedimientoDAO.buscar(registro.getIdProcedimiento().getIdProcedimiento());
            if (actual != null) {
                lista.add(actual);
            }
        }
        return lista;
    }

   
    public List<Rol> getRoles() {
        return rolDAO.buscarRolesActivos().stream()
                .filter(r -> r.getNombre() == null
                        || !r.getNombre().trim().equalsIgnoreCase(ROL_PACIENTE))
                .toList();
    }

    private boolean vinculoValido() {
        if (registro.getNombre() == null || registro.getNombre().isBlank()) {
            rechazar("El nombre es obligatorio.");
            return false;
        }
        String nombre = registro.getNombre().trim();
        registro.setNombre(nombre);

        Procedimiento pr = (registro.getIdProcedimiento() != null)
                ? procedimientoDAO.buscar(registro.getIdProcedimiento().getIdProcedimiento()) : null;

        if (estado == Estado_CRUD.CREAR && pr != null && !Boolean.TRUE.equals(pr.getActivo())) {
            rechazar("El procedimiento \"" + pr.getNombre() + "\" está inactivo.");
            return false;
        }

        Rol rl = (registro.getIdRol() != null)
                ? rolDAO.buscar(registro.getIdRol().getIdRol()) : null;

        if (estado == Estado_CRUD.CREAR && rl != null && !Boolean.TRUE.equals(rl.getActivo())) {
            rechazar("El rol \"" + rl.getNombre() + "\" está inactivo.");
            return false;
        }

        String nombreRol = (rl == null || rl.getNombre() == null)
                ? "" : rl.getNombre().trim();

        if (ROL_PACIENTE.equalsIgnoreCase(nombreRol)) {
            rechazar("El rol \"paciente\" no puede ser responsable de un paso.");
            return false;
        }
        if (idPasoPadre != null && idPasoPadre.equals(registro.getIdProcedimientoPaso())) {
            rechazar("Un paso no puede depender de sí mismo.");
            return false;
        }
        if (pr != null) {
            boolean duplicado = getRegistrosPorProcedimiento(pr.getIdProcedimiento()).stream()
                    .anyMatch(x -> x.getNombre() != null
                            && x.getNombre().trim().equalsIgnoreCase(nombre)
                            && !x.getIdProcedimientoPaso().equals(registro.getIdProcedimientoPaso()));
            if (duplicado) {
                rechazar("El procedimiento \"" + pr.getNombre() + "\" ya tiene un paso llamado \"" + nombre + "\".");
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
        UUID idNuevo = (registro != null) ? registro.getIdProcedimientoPaso() : null;
        UUID referencia = this.idPasoPadre;
        UUID examen = this.idExamenNuevo;
        super.btnCrearhandler(ae);
        boolean guardado = (this.registro == null);
        if (guardado && idNuevo != null) {
            if (referencia != null && !referencia.equals(idNuevo)) {
                crearSecuencia(idNuevo, referencia);
            }
            if (examen != null) {
                asociarExamen(idNuevo, examen);
            }
        }
        this.idPasoPadre = null;
        this.idExamenNuevo = null;
    }

    @Override
    public void btnModificarHandler() {
        if (registro != null && !vinculoValido()) {
            return;
        }
        UUID idPaso = (registro != null) ? registro.getIdProcedimientoPaso() : null;
        UUID referencia = this.idPasoPadre;
        super.btnModificarHandler();
        boolean guardado = (this.registro == null);
        if (guardado && idPaso != null) {
            sincronizarSecuencia(idPaso, referencia);
        }
    }

    private void crearSecuencia(UUID idPaso, UUID idReferencia) {
        try {
            ProcedimientoPasoSecuencia secuencia =
                    new ProcedimientoPasoSecuencia(UUID.randomUUID());
            secuencia.setIdProcedimientoPaso(new ProcedimientoPaso(idPaso));
            secuencia.setIdProcedimientoPasoReferencia(idReferencia);
            secuencia.setTipoSecuencia("AFTER");
            procedimientoPasoSecuenciaDAO.crear(secuencia);
        } catch (Exception ex) {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Paso guardado, pero no se pudo crear la dependencia", ex.getMessage()));
        }
    }

    private void sincronizarSecuencia(UUID idPaso, UUID idReferencia) {
        try {
            List<ProcedimientoPasoSecuencia> actuales = procedimientoPasoSecuenciaDAO
                    .buscarPorProcedimientoPaso(idPaso);
            UUID referenciaActual = actuales.isEmpty() ? null
                    : actuales.get(0).getIdProcedimientoPasoReferencia();
            if (Objects.equals(referenciaActual, idReferencia)) {
                return;
            }
            for (ProcedimientoPasoSecuencia s : actuales) {
                procedimientoPasoSecuenciaDAO.eliminar(s.getIdProcedimientoPasoSecuencia());
            }
            if (idReferencia != null && !idReferencia.equals(idPaso)) {
                crearSecuencia(idPaso, idReferencia);
            }
        } catch (Exception ex) {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Paso guardado, pero no se pudo actualizar la dependencia", ex.getMessage()));
        }
    }

    private void asociarExamen(UUID idPaso, UUID idExamen) {
        try {
            Examen examen = examenDAO.buscar(idExamen);
            if (examen == null || !Boolean.TRUE.equals(examen.getActivo())) {
                return;
            }
            ProcedimientoPasoExamen asociacion =
                    new ProcedimientoPasoExamen(UUID.randomUUID());
            asociacion.setIdProcedimientoPaso(new ProcedimientoPaso(idPaso));
            asociacion.setIdExamen(new Examen(idExamen));
            asociacion.setActivo(Boolean.TRUE);
            procedimientoPasoExamenDAO.crear(asociacion);
        } catch (Exception ex) {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Paso guardado, pero no se pudo asociar el examen", ex.getMessage()));
        }
    }
}