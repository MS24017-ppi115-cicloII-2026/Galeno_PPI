package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ConsultaProcedimientoDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ProcedimientoDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ProcedimientoPasoDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ProcedimientoPasoSecuenciaDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Consulta;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ConsultaProcedimiento;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Procedimiento;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ProcedimientoPaso;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ProcedimientoPasoSecuencia;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Rol;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ConsultaProcedimientoModelsTest {

    @Mock
    private ConsultaProcedimientoDAO consultaProcedimientoDAO;

    @Mock
    private ConsultaModels consultaModels;

    @Mock
    private ProcedimientoDAO procedimientoDAO;

    @Mock
    private ProcedimientoPasoDAO procedimientoPasoDAO;

    @Mock
    private ProcedimientoPasoSecuenciaDAO procedimientoPasoSecuenciaDAO;

    @Mock
    private FacesContext fc;

    @InjectMocks
    private ConsultaProcedimientoModels model;

    private UUID idConsulta;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        idConsulta = UUID.randomUUID();
    }

    private Consulta consultaSeleccionada() {
        Consulta consulta = new Consulta(idConsulta);
        when(consultaModels.getConsultaSeleccionada()).thenReturn(consulta);
        return consulta;
    }

    private ConsultaProcedimiento nuevoRegistro(UUID idProcedimiento, Date fechaInicio) {
        ConsultaProcedimiento cp = new ConsultaProcedimiento(UUID.randomUUID());
        cp.setIdConsulta(consultaSeleccionada());
        cp.setIdProcedimiento(idProcedimiento);
        cp.setFechaInicio(fechaInicio);
        return cp;
    }

    private Procedimiento activo() {
        Procedimiento procedimiento = new Procedimiento(UUID.randomUUID());
        procedimiento.setNombre("Resonancia Magnética");
        procedimiento.setActivo(true);
        return procedimiento;
    }

    private Date fecha(int anio, int mes, int dia, int hora) {
        Calendar calendario = Calendar.getInstance();
        calendario.set(anio, mes, dia, hora, 0, 0);
        calendario.set(Calendar.MILLISECOND, 0);
        return calendario.getTime();
    }

    // ---------- Listado de la pestaña ----------

    @Test
    void getRegistrosDeConsultaFiltraPorLaConsultaSeleccionada() {

        consultaSeleccionada();

        ConsultaProcedimiento asociado = new ConsultaProcedimiento(UUID.randomUUID());
        when(consultaProcedimientoDAO.buscarPorConsulta(idConsulta))
                .thenReturn(List.of(asociado));

        assertEquals(List.of(asociado), model.getRegistrosDeConsulta());
        verify(consultaProcedimientoDAO).buscarPorConsulta(idConsulta);
        verify(consultaProcedimientoDAO, never()).findRange(anyInt(), anyInt());
    }

    @Test
    void getRegistrosDeConsultaSinSeleccionDevuelveLaListaCompleta() {

        when(consultaModels.getConsultaSeleccionada()).thenReturn(null);

        ConsultaProcedimiento deOtro = new ConsultaProcedimiento(UUID.randomUUID());
        when(consultaProcedimientoDAO.findRange(0, 100)).thenReturn(List.of(deOtro));

        assertEquals(List.of(deOtro), model.getRegistrosDeConsulta());
        verify(consultaProcedimientoDAO, never()).buscarPorConsulta(any());
    }

    @Test
    void getRegistrosDeConsultaCargaUnaSoloVez() {

        consultaSeleccionada();
        when(consultaProcedimientoDAO.buscarPorConsulta(idConsulta)).thenReturn(List.of());

        model.getRegistrosDeConsulta();
        model.getRegistrosDeConsulta();

        verify(consultaProcedimientoDAO, times(1)).buscarPorConsulta(idConsulta);
    }

    @Test
    void getRegistrosDeConsultaRecargaAlCambiarDeConsulta() {

        consultaSeleccionada();
        when(consultaProcedimientoDAO.buscarPorConsulta(idConsulta)).thenReturn(List.of());
        model.getRegistrosDeConsulta();

        UUID otraConsulta = UUID.randomUUID();
        when(consultaModels.getConsultaSeleccionada()).thenReturn(new Consulta(otraConsulta));
        when(consultaProcedimientoDAO.buscarPorConsulta(otraConsulta)).thenReturn(List.of());

        model.getRegistrosDeConsulta();

        verify(consultaProcedimientoDAO).buscarPorConsulta(otraConsulta);
    }

    // ---------- Procedimientos activos y presentación ----------

    @Test
    void getProcedimientosActivosDelegaAlDao() {

        Procedimiento activo = activo();
        when(procedimientoDAO.buscarPorActivo(true)).thenReturn(List.of(activo));

        assertEquals(List.of(activo), model.getProcedimientosActivos());
        verify(procedimientoDAO).buscarPorActivo(true);
    }

    @Test
    void getNombreProcedimientoConsultaElDaoSoloUnaVez() {

        UUID idProcedimiento = activo().getIdProcedimiento();
        Procedimiento guardado = activo();
        when(procedimientoDAO.buscar(idProcedimiento)).thenReturn(guardado);

        assertEquals("Resonancia Magnética", model.getNombreProcedimiento(idProcedimiento));
        assertEquals("Resonancia Magnética", model.getNombreProcedimiento(idProcedimiento));

        verify(procedimientoDAO, times(1)).buscar(idProcedimiento);
    }

    @Test
    void getPrimerPasoEsElQueNoDependeDeNadie() {

        UUID idProcedimiento = UUID.randomUUID();
        UUID idPaso1 = UUID.randomUUID();
        UUID idPaso2 = UUID.randomUUID();

        ProcedimientoPaso preparacion = new ProcedimientoPaso(idPaso1);
        preparacion.setNombre("Preparación");
        ProcedimientoPaso aplicacion = new ProcedimientoPaso(idPaso2);
        aplicacion.setNombre("Aplicación");

        when(procedimientoPasoDAO.buscarPorProcedimiento(idProcedimiento))
                .thenReturn(List.of(preparacion, aplicacion));

        ProcedimientoPasoSecuencia dependencia = new ProcedimientoPasoSecuencia(UUID.randomUUID());
        dependencia.setIdProcedimientoPaso(aplicacion);
        dependencia.setIdProcedimientoPasoReferencia(idPaso1);

        when(procedimientoPasoSecuenciaDAO.buscarPorProcedimientoPaso(idPaso1))
                .thenReturn(List.of());
        when(procedimientoPasoSecuenciaDAO.buscarPorProcedimientoPaso(idPaso2))
                .thenReturn(List.of(dependencia));

        assertSame(preparacion, model.getPrimerPaso(idProcedimiento));
    }

    @Test
    void getEncargadoDePrimerPasoDevuelveElNombreDelRol() {

        UUID idProcedimiento = UUID.randomUUID();
        UUID idPaso = UUID.randomUUID();

        ProcedimientoPaso paso = new ProcedimientoPaso(idPaso);
        paso.setNombre("Preparación");
        Rol rol = new Rol(UUID.randomUUID());
        rol.setNombre("Enfermería");
        paso.setIdRol(rol);

        when(procedimientoPasoDAO.buscarPorProcedimiento(idProcedimiento))
                .thenReturn(List.of(paso));
        when(procedimientoPasoSecuenciaDAO.buscarPorProcedimientoPaso(idPaso))
                .thenReturn(List.of());

        assertEquals("Enfermería", model.getEncargadoDePrimerPaso(idProcedimiento));
    }

    // ---------- Alta ----------

    @Test
    void crearAsignaLaFechaAutomaticaYLaConsultaSeleccionada() {

        consultaSeleccionada();

        model.btnNuevoHandler(null);

        assertNotNull(model.getRegistro().getFechaInicio());
        assertSame(consultaModels.getConsultaSeleccionada(), model.getRegistro().getIdConsulta());
        assertEquals(Estado_CRUD.CREAR, model.getEstado());
    }

    @Test
    void crearSinConsultaSeleccionadaDejaLaConsultaParaElegirlaALaMano() {

        when(consultaModels.getConsultaSeleccionada()).thenReturn(null);

        model.btnNuevoHandler(null);

        assertNotNull(model.getRegistro().getFechaInicio());
        assertNull(model.getRegistro().getIdConsulta());
    }

    @Test
    void crearProcedimientoNoActivoRechaza() {

        Procedimiento inactivo = new Procedimiento(UUID.randomUUID());
        inactivo.setNombre("Cirugía suspendida");
        when(procedimientoDAO.buscarPorActivo(true)).thenReturn(List.of());

        model.setRegistro(nuevoRegistro(inactivo.getIdProcedimiento(),
                fecha(2026, Calendar.JANUARY, 10, 9)));
        model.btnCrearhandler(null);

        verify(consultaProcedimientoDAO, never()).crear(any());
        verify(fc).validationFailed();
    }

    @Test
    void crearProcedimientoConFechaFinAnteriorRechaza() {

        Procedimiento activo = activo();
        when(procedimientoDAO.buscarPorActivo(true)).thenReturn(List.of(activo));

        model.setRegistro(nuevoRegistro(activo.getIdProcedimiento(),
                fecha(2026, Calendar.JANUARY, 20, 9)));
        model.getRegistro().setFechaFin(fecha(2026, Calendar.JANUARY, 10, 9));

        model.btnCrearhandler(null);

        verify(consultaProcedimientoDAO, never()).crear(any());
        verify(fc).validationFailed();
    }

    @Test
    void crearProcedimientoDuplicadoRechaza() {

        Procedimiento activo = activo();
        when(procedimientoDAO.buscarPorActivo(true)).thenReturn(List.of(activo));

        ConsultaProcedimiento yaAsociado = new ConsultaProcedimiento(UUID.randomUUID());
        yaAsociado.setIdProcedimiento(activo.getIdProcedimiento());
        when(consultaProcedimientoDAO.buscarPorConsulta(idConsulta))
                .thenReturn(List.of(yaAsociado));

        model.setRegistro(nuevoRegistro(activo.getIdProcedimiento(),
                fecha(2026, Calendar.JANUARY, 10, 9)));

        model.btnCrearhandler(null);

        verify(consultaProcedimientoDAO, never()).crear(any());
        verify(fc).validationFailed();
    }

    @Test
    void crearProcedimientoValidoGuarda() {

        Procedimiento activo = activo();
        when(procedimientoDAO.buscarPorActivo(true)).thenReturn(List.of(activo));
        when(consultaProcedimientoDAO.buscarPorConsulta(idConsulta)).thenReturn(List.of());
        when(consultaProcedimientoDAO.findRange(0, 100)).thenReturn(List.of());

        ConsultaProcedimiento registro = nuevoRegistro(activo.getIdProcedimiento(),
                fecha(2026, Calendar.JANUARY, 10, 9));
        model.setRegistro(registro);

        model.btnCrearhandler(null);

        verify(consultaProcedimientoDAO).crear(registro);
        verify(fc, never()).validationFailed();
        assertNull(model.getRegistro());
        assertEquals(Estado_CRUD.NINGUNO, model.getEstado());
    }
}
