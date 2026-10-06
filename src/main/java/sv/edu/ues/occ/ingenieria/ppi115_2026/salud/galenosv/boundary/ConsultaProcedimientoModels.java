package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DAOInterface;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ConsultaProcedimientoDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ProcedimientoDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ProcedimientoPasoDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ProcedimientoPasoSecuenciaDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Consulta;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ConsultaProcedimiento;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Procedimiento;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ProcedimientoPaso;

@Named
@ViewScoped
public class ConsultaProcedimientoModels extends AbstractModel<ConsultaProcedimiento> {

    @Inject
    ConsultaProcedimientoDAO consultaProcedimientoDAO;

    @Inject
    ConsultaModels consultaModels;

    @Inject
    ProcedimientoDAO procedimientoDAO;

    @Inject
    ProcedimientoPasoDAO procedimientoPasoDAO;

    @Inject
    ProcedimientoPasoSecuenciaDAO procedimientoPasoSecuenciaDAO;

    private UUID consultaCargada;
    private boolean cargado;

    @Override
    protected DAOInterface<ConsultaProcedimiento> getDAO() {
        return consultaProcedimientoDAO;
    }

    @Override
    protected ConsultaProcedimiento crearRegistroNuevo() {
        ConsultaProcedimiento nuevo = new ConsultaProcedimiento(UUID.randomUUID());
        nuevo.setFechaInicio(new Date());
        Consulta consulta = consultaModels.getConsultaSeleccionada();
        if (consulta != null) {
            nuevo.setIdConsulta(consulta);
        }
        return nuevo;
    }

    @Override
    protected UUID obtenerId(ConsultaProcedimiento registro) {
        return registro.getIdConsultaProcedimiento();
    }

    // ---- Lista filtrada por la consulta seleccionada ----
    private List<ConsultaProcedimiento> cargarFiltrado() {
        Consulta consulta = consultaModels.getConsultaSeleccionada();
        if (consulta == null) {
            // Sin consulta seleccionada (pantalla ConsultaProcedimiento.xhtml): lista completa
            return consultaProcedimientoDAO.findRange(0, 100);
        }
        return consultaProcedimientoDAO.buscarPorConsulta(consulta.getIdConsulta());
    }

    /**
     * Valor de la tabla de la pestaña: recarga sola cuando cambia la consulta.
     */
    public List<ConsultaProcedimiento> getRegistrosDeConsulta() {
        Consulta consulta = consultaModels.getConsultaSeleccionada();
        UUID idActual = (consulta == null) ? null : consulta.getIdConsulta();

        if (!cargado || !Objects.equals(idActual, consultaCargada)) {
            consultaCargada = idActual;
            cargado = true;
            setRegistros(cargarFiltrado());
            this.registro = null;
            this.estado = Estado_CRUD.NINGUNO;
        }
        return getregistros();
    }

    public List<Procedimiento> getProcedimientosActivos() {
        return procedimientoDAO.buscarPorActivo(true);
    }

    // ---- Datos de presentación de la pestaña ----

    private final Map<UUID, String> nombresProcedimiento = new HashMap<>();
    private final Map<UUID, ProcedimientoPaso> primerPasoPorProcedimiento = new HashMap<>();

    public String getNombreProcedimiento(UUID idProcedimiento) {
        if (idProcedimiento == null) {
            return "";
        }
        return nombresProcedimiento.computeIfAbsent(idProcedimiento, id -> {
            try {
                Procedimiento guardado = procedimientoDAO.buscar(id);
                return (guardado == null || guardado.getNombre() == null)
                        ? id.toString()
                        : guardado.getNombre();
            } catch (Exception ex) {
                return id.toString();
            }
        });
    }

    public ProcedimientoPaso getPrimerPaso(UUID idProcedimiento) {
        if (idProcedimiento == null) {
            return null;
        }
        return primerPasoPorProcedimiento.computeIfAbsent(idProcedimiento,
                id -> calcularPrimerPaso(id));
    }

    public String getEncargadoDePrimerPaso(UUID idProcedimiento) {
        ProcedimientoPaso paso = getPrimerPaso(idProcedimiento);
        if (paso == null || paso.getIdRol() == null) {
            return "";
        }
        return paso.getIdRol().getNombre();
    }

    private ProcedimientoPaso calcularPrimerPaso(UUID idProcedimiento) {
        List<ProcedimientoPaso> pasos = procedimientoPasoDAO.buscarPorProcedimiento(idProcedimiento);
        if (pasos.isEmpty()) {
            return null;
        }

        for (ProcedimientoPaso paso : pasos) {
            boolean dependeDeOtro
                    = !procedimientoPasoSecuenciaDAO
                            .buscarPorProcedimientoPaso(paso.getIdProcedimientoPaso())
                            .isEmpty();
            if (!dependeDeOtro) {
                return paso;
            }
        }
        return pasos.get(0);
    }

    private boolean validarRegistro() {
        if (registro == null) {
            return mostrarError(
                    "Registro inválido",
                    "El registro no puede ser nulo."
            );
        }

        if (registro.getIdConsulta() == null) {
            return mostrarError(
                    "Consulta requerida",
                    "Debe seleccionar una consulta."
            );
        }

        if (registro.getIdProcedimiento() == null) {
            return mostrarError(
                    "Procedimiento requerido",
                    "Debe seleccionar un procedimiento."
            );
        }

        if (!esProcedimientoActivo(registro.getIdProcedimiento())) {
            return mostrarError(
                    "Procedimiento inactivo",
                    "Solo puede asociar procedimientos activos."
            );
        }

        if (registro.getFechaInicio() == null) {
            return mostrarError(
                    "Fecha requerida",
                    "El procedimiento de la consulta debe tener una fecha de inicio."
            );
        }
        if (registro.getFechaFin() != null
                && registro.getFechaFin().before(registro.getFechaInicio())) {
            return mostrarError(
                    "Fechas inválidas",
                    "La fecha de fin no puede ser anterior a la fecha de inicio."
            );
        }

        if (estaAsociadoYa()) {
            return mostrarError(
                    "Procedimiento duplicado",
                    "Este procedimiento ya está asociado a la consulta."
            );
        }

        return true;
    }

    private boolean esProcedimientoActivo(UUID idProcedimiento) {
        return procedimientoDAO.buscarPorActivo(true).stream()
                .anyMatch(p -> idProcedimiento.equals(p.getIdProcedimiento()));
    }

    private boolean estaAsociadoYa() {
        if (registro.getIdConsulta() == null || registro.getIdProcedimiento() == null) {
            return false;
        }
        for (ConsultaProcedimiento existente
                : consultaProcedimientoDAO.buscarPorConsulta(registro.getIdConsulta().getIdConsulta())) {
            if (!registro.getIdProcedimiento().equals(existente.getIdProcedimiento())) {
                continue;
            }
            boolean esElMismoRegistro = Objects.equals(
                    registro.getIdConsultaProcedimiento(),
                    existente.getIdConsultaProcedimiento());
            if (!esElMismoRegistro) {
                return true;
            }
        }
        return false;
    }

    private boolean mostrarError(String resumen, String detalle) {
        fc.addMessage(
                null,
                new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        resumen,
                        detalle
                )
        );

        fc.validationFailed();
        return false;
    }

    @Override
    public void btnCrearhandler(ActionEvent ae) {
        if (validarRegistro()) {
            super.btnCrearhandler(ae);
            setRegistros(cargarFiltrado());
        }
    }

    @Override
    public void btnModificarHandler() {
        if (validarRegistro()) {
            super.btnModificarHandler();
            setRegistros(cargarFiltrado());
        }
    }

    @Override
    public void btnEliminarHandler(UUID id) {
        super.btnEliminarHandler(id);
        setRegistros(cargarFiltrado());
    }
}
