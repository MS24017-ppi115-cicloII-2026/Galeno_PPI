package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProcedimientoPasoModelsTest {

    @Mock
    private ProcedimientoPasoDAO procedimientoPasoDAO;

    @Mock
    private ProcedimientoDAO procedimientoDAO;

    @Mock
    private RolDAO rolDAO;

    @Mock
    private ProcedimientoPasoSecuenciaDAO procedimientoPasoSecuenciaDAO;

    @Mock
    private ProcedimientoPasoExamenDAO procedimientoPasoExamenDAO;

    @Mock
    private ExamenDAO examenDAO;

    @Mock
    private ProcedimientoPasoExamenModels procedimientoPasoExamenModels;

    @Mock
    private FacesContext fc;

    @InjectMocks
    private ProcedimientoPasoModels model;

    private UUID idProcedimiento;
    private Procedimiento procedimiento;
    private ProcedimientoPaso paso;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        idProcedimiento = UUID.randomUUID();

        procedimiento = new Procedimiento(idProcedimiento);
        procedimiento.setNombre("Radiografia");
        procedimiento.setActivo(Boolean.TRUE);

        paso = new ProcedimientoPaso(UUID.randomUUID());
        paso.setNombre("Tomografia");
        paso.setIdProcedimiento(procedimiento);

        when(procedimientoDAO.buscar(idProcedimiento))
                .thenReturn(procedimiento);
        when(procedimientoPasoDAO.buscarPorProcedimiento(idProcedimiento))
                .thenReturn(List.of(paso));
        when(procedimientoPasoDAO.findRange(0, 100)).thenReturn(List.of());
    }


    @Test
    void prepararNuevoParaProcedimientoAsignaElProcedimiento() {

        model.prepararNuevoParaProcedimiento(idProcedimiento);

        assertEquals(Estado_CRUD.CREAR, model.getEstado());
        assertEquals(idProcedimiento,
                model.getRegistro().getIdProcedimiento().getIdProcedimiento());
        assertNull(model.getIdPasoPadre());
        assertNull(model.getIdExamenNuevo());
    }

    @Test
    void prepararNuevoDependienteTomaElUltimoPasoComoPadre() {

        ProcedimientoPaso ultimo = new ProcedimientoPaso(UUID.randomUUID());
        ultimo.setNombre("Ultimo");

        when(procedimientoPasoDAO.buscarPorProcedimiento(idProcedimiento))
                .thenReturn(List.of(paso, ultimo));

        model.prepararNuevoDependiente(idProcedimiento);

        assertEquals(ultimo.getIdProcedimientoPaso(), model.getIdPasoPadre());
    }

    @Test
    void prepararNuevoDependienteSinPasosNoTienePadre() {

        when(procedimientoPasoDAO.buscarPorProcedimiento(idProcedimiento))
                .thenReturn(List.of());

        model.prepararNuevoDependiente(idProcedimiento);

        assertNull(model.getIdPasoPadre());
    }


    // ---------- Consultas ----------

    @Test
    void getNombrePasoPadre() {

        UUID idPadre = UUID.randomUUID();
        when(procedimientoPasoDAO.buscar(idPadre)).thenReturn(paso);

        model.setIdPasoPadre(idPadre);

        assertEquals("Tomografia", model.getNombrePasoPadre());
    }

    @Test
    void hayPasoFinal() {

        when(procedimientoPasoDAO.buscarPorProcedimiento(idProcedimiento))
                .thenReturn(List.of(paso));

        assertFalse(model.hayPasoFinal(idProcedimiento));
    }

    @Test
    void hayPasoFinalCuandoUnPasoIndicaFin() {

        paso.setIndicaFin(Boolean.TRUE);
        when(procedimientoPasoDAO.buscarPorProcedimiento(idProcedimiento))
                .thenReturn(List.of(paso));

        assertTrue(model.hayPasoFinal(idProcedimiento));
    }

    @Test
    void getProcedimientosSoloDevuelveLosActivos() {

        Procedimiento inactivo = new Procedimiento(UUID.randomUUID());
        inactivo.setNombre("Inactivo");
        inactivo.setActivo(Boolean.FALSE);

        when(procedimientoDAO.findRange(0, 1000))
                .thenReturn(List.of(procedimiento, inactivo));

        List<Procedimiento> resultado = model.getProcedimientos();

        assertEquals(1, resultado.size());
        assertSame(procedimiento, resultado.get(0));
    }

    @Test
    void getProcedimientosAgregaElProcedimientoInactivoDelRegistro() {

        Procedimiento inactivo = new Procedimiento(UUID.randomUUID());
        inactivo.setNombre("Inactivo");
        inactivo.setActivo(Boolean.FALSE);

        when(procedimientoDAO.findRange(0, 1000))
                .thenReturn(List.of(procedimiento));
        when(procedimientoDAO.buscar(inactivo.getIdProcedimiento()))
                .thenReturn(inactivo);

        model.setRegistro(new ProcedimientoPaso(UUID.randomUUID()));
        model.getRegistro().setIdProcedimiento(inactivo);

        List<Procedimiento> resultado = model.getProcedimientos();

        assertEquals(2, resultado.size());
        assertSame(inactivo, resultado.get(1));
    }

    @Test
    void getRolesDevuelveLosRolesActivos() {

        Rol rol = new Rol();
        rol.setNombre("DOCTOR");
        when(rolDAO.buscarRolesActivos()).thenReturn(List.of(rol));

        assertEquals(1, model.getRoles().size());
        verify(rolDAO).buscarRolesActivos();
    }

    @Test
    void seleccionarRegistroCargaLaDependenciaExistente() {

        UUID idReferencia = UUID.randomUUID();
        ProcedimientoPasoSecuencia secuencia = new ProcedimientoPasoSecuencia(
                UUID.randomUUID());
        secuencia.setIdProcedimientoPasoReferencia(idReferencia);

        model.setRegistros(List.of(paso));
        when(procedimientoPasoSecuenciaDAO.buscarPorProcedimientoPaso(
                paso.getIdProcedimientoPaso()))
                .thenReturn(List.of(secuencia));

        model.btnSeleccionarRegistro(paso.getIdProcedimientoPaso());

        assertSame(paso, model.getRegistro());
        assertEquals(Estado_CRUD.MODIFICAR, model.getEstado());
        assertEquals(idReferencia, model.getIdPasoPadre());
        verify(procedimientoPasoExamenModels)
                .setIdAsociacionSeleccionada(null);
    }

    @Test
    void seleccionarRegistroSinDependenciasNoTienePadre() {

        model.setRegistros(List.of(paso));
        when(procedimientoPasoSecuenciaDAO.buscarPorProcedimientoPaso(any()))
                .thenReturn(List.of());

        model.btnSeleccionarRegistro(paso.getIdProcedimientoPaso());

        assertNull(model.getIdPasoPadre());
    }


    // ---------- Validaciones ----------

    @Test
    void crearSinNombreMuestraError() {

        paso.setNombre("   ");
        model.setRegistro(paso);

        model.btnCrearhandler(null);

        verify(procedimientoPasoDAO, never()).crear(any());
        verify(fc).addMessage(isNull(), any(FacesMessage.class));
        verify(fc).validationFailed();
    }

    @Test
    void crearConProcedimientoInactivoMuestraError() {

        procedimiento.setActivo(Boolean.FALSE);
        model.prepararNuevoParaProcedimiento(idProcedimiento);
        model.getRegistro().setNombre("Nuevo");

        model.btnCrearhandler(null);

        verify(procedimientoPasoDAO, never()).crear(any());
        verify(fc).validationFailed();
    }

    @Test
    void crearConRolInactivoMuestraError() {

        Rol rol = new Rol();
        rol.setNombre("ENFERMERIA");
        rol.setActivo(Boolean.FALSE);
        when(rolDAO.buscar(rol.getIdRol())).thenReturn(rol);

        model.prepararNuevoParaProcedimiento(idProcedimiento);
        model.getRegistro().setNombre("Nuevo");
        model.getRegistro().setIdRol(rol);

        model.btnCrearhandler(null);

        verify(procedimientoPasoDAO, never()).crear(any());
        verify(fc).validationFailed();
    }

    @Test
    void crearUnPasoQueDependeDeSiMismoMuestraError() {

        model.prepararNuevoParaProcedimiento(idProcedimiento);
        model.getRegistro().setNombre("Nuevo");
        model.setIdPasoPadre(model.getRegistro().getIdProcedimientoPaso());

        model.btnCrearhandler(null);

        verify(procedimientoPasoDAO, never()).crear(any());
        verify(fc).validationFailed();
    }

    @Test
    void crearConNombreDuplicadoMuestraError() {

        model.prepararNuevoParaProcedimiento(idProcedimiento);
        model.getRegistro().setNombre("  tomografia  ");

        model.btnCrearhandler(null);

        verify(procedimientoPasoDAO, never()).crear(any());
        verify(fc).validationFailed();
    }

    @Test
    void crearConDatosValidosGuardaYRecortaElNombre() {

        model.prepararNuevoParaProcedimiento(idProcedimiento);
        model.getRegistro().setNombre("  Ecografia  ");
        when(procedimientoPasoDAO.buscarPorProcedimiento(idProcedimiento))
                .thenReturn(List.of());

        model.btnCrearhandler(null);

        ArgumentCaptor<ProcedimientoPaso> captor =
                ArgumentCaptor.forClass(ProcedimientoPaso.class);

        verify(procedimientoPasoDAO).crear(captor.capture());

        assertEquals("Ecografia", captor.getValue().getNombre());
        assertNull(model.getRegistro());
        verify(fc, never()).validationFailed();
    }


    // ---------- Secuencia y examen ----------

    @Test
    void crearConPasoPadreGeneraLaDependencia() {

        UUID idReferencia = UUID.randomUUID();

        model.prepararNuevoParaProcedimiento(idProcedimiento);
        model.getRegistro().setNombre("Ecografia");
        when(procedimientoPasoDAO.buscarPorProcedimiento(idProcedimiento))
                .thenReturn(List.of());
        model.setIdPasoPadre(idReferencia);

        model.btnCrearhandler(null);

        ArgumentCaptor<ProcedimientoPasoSecuencia> captor =
                ArgumentCaptor.forClass(ProcedimientoPasoSecuencia.class);

        verify(procedimientoPasoSecuenciaDAO).crear(captor.capture());

        ProcedimientoPasoSecuencia secuencia = captor.getValue();
        assertEquals("AFTER", secuencia.getTipoSecuencia());
        assertEquals(idReferencia,
                secuencia.getIdProcedimientoPasoReferencia());
    }

    @Test
    void crearSinPasoPadreNoGeneraDependencia() {

        model.prepararNuevoParaProcedimiento(idProcedimiento);
        model.getRegistro().setNombre("Ecografia");
        when(procedimientoPasoDAO.buscarPorProcedimiento(idProcedimiento))
                .thenReturn(List.of());

        model.btnCrearhandler(null);

        verify(procedimientoPasoSecuenciaDAO, never()).crear(any());
    }

    @Test
    void crearConExamenActivoLoAsocia() {

        UUID idExamen = UUID.randomUUID();
        Examen examen = new Examen(idExamen);
        examen.setActivo(Boolean.TRUE);

        model.prepararNuevoParaProcedimiento(idProcedimiento);
        model.getRegistro().setNombre("Ecografia");
        when(procedimientoPasoDAO.buscarPorProcedimiento(idProcedimiento))
                .thenReturn(List.of());
        when(examenDAO.buscar(idExamen)).thenReturn(examen);
        model.setIdExamenNuevo(idExamen);

        model.btnCrearhandler(null);

        verify(procedimientoPasoExamenDAO).crear(any());
    }

    @Test
    void crearConExamenInactivoNoLoAsocia() {

        UUID idExamen = UUID.randomUUID();
        Examen examen = new Examen(idExamen);
        examen.setActivo(Boolean.FALSE);

        model.prepararNuevoParaProcedimiento(idProcedimiento);
        model.getRegistro().setNombre("Ecografia");
        when(procedimientoPasoDAO.buscarPorProcedimiento(idProcedimiento))
                .thenReturn(List.of());
        when(examenDAO.buscar(idExamen)).thenReturn(examen);
        model.setIdExamenNuevo(idExamen);

        model.btnCrearhandler(null);

        verify(procedimientoPasoExamenDAO, never()).crear(any());
    }

    @Test
    void modificarSincronizaLaDependenciaCambiada() {

        UUID idReferenciaNueva = UUID.randomUUID();

        model.setRegistro(paso);
        when(procedimientoPasoDAO.buscarPorProcedimiento(idProcedimiento))
                .thenReturn(List.of(paso));
        when(procedimientoPasoSecuenciaDAO.buscarPorProcedimientoPaso(
                paso.getIdProcedimientoPaso()))
                .thenReturn(List.of());
        model.setIdPasoPadre(idReferenciaNueva);

        model.btnModificarHandler();

        verify(procedimientoPasoSecuenciaDAO).crear(any());
        verify(procedimientoPasoDAO).actualizar(paso);
    }

    @Test
    void modificarNoRepiteLaDependenciaSiYaEsLaMisma() {

        UUID idReferencia = UUID.randomUUID();

        ProcedimientoPasoSecuencia secuencia = new ProcedimientoPasoSecuencia(
                UUID.randomUUID());
        secuencia.setIdProcedimientoPasoReferencia(idReferencia);

        model.setRegistro(paso);
        when(procedimientoPasoDAO.buscarPorProcedimiento(idProcedimiento))
                .thenReturn(List.of(paso));
        when(procedimientoPasoSecuenciaDAO.buscarPorProcedimientoPaso(
                paso.getIdProcedimientoPaso()))
                .thenReturn(List.of(secuencia));
        model.setIdPasoPadre(idReferencia);

        model.btnModificarHandler();

        verify(procedimientoPasoSecuenciaDAO, never()).eliminar(any());
        verify(procedimientoPasoSecuenciaDAO, never()).crear(any());
    }

    // ---------- sincronizarSecuencia: elimina la dependencia anterior ----------

    @Test
    void modificarEliminaLaDependenciaAnteriorAntesDeCrearLaNueva() {

        UUID idReferenciaVieja = UUID.randomUUID();
        UUID idReferenciaNueva = UUID.randomUUID();

        ProcedimientoPasoSecuencia anterior =
                new ProcedimientoPasoSecuencia(UUID.randomUUID());
        anterior.setIdProcedimientoPasoReferencia(idReferenciaVieja);

        model.setRegistro(paso);
        when(procedimientoPasoDAO.buscarPorProcedimiento(idProcedimiento))
                .thenReturn(List.of(paso));
        when(procedimientoPasoSecuenciaDAO.buscarPorProcedimientoPaso(
                paso.getIdProcedimientoPaso()))
                .thenReturn(List.of(anterior));
        model.setIdPasoPadre(idReferenciaNueva);

        model.btnModificarHandler();

        verify(procedimientoPasoSecuenciaDAO).eliminar(
                anterior.getIdProcedimientoPasoSecuencia());
        verify(procedimientoPasoSecuenciaDAO).crear(any());
    }

    @Test
    void modificarQuitaLaDependenciaCuandoSeEligeNoDepender() {

        ProcedimientoPasoSecuencia anterior =
                new ProcedimientoPasoSecuencia(UUID.randomUUID());
        anterior.setIdProcedimientoPasoReferencia(UUID.randomUUID());

        model.setRegistro(paso);
        when(procedimientoPasoDAO.buscarPorProcedimiento(idProcedimiento))
                .thenReturn(List.of(paso));
        when(procedimientoPasoSecuenciaDAO.buscarPorProcedimientoPaso(
                paso.getIdProcedimientoPaso()))
                .thenReturn(List.of(anterior));
        model.setIdPasoPadre(null);

        model.btnModificarHandler();

        verify(procedimientoPasoSecuenciaDAO).eliminar(
                anterior.getIdProcedimientoPasoSecuencia());
        verify(procedimientoPasoSecuenciaDAO, never()).crear(any());
    }


    // ---------- catch: el paso se guarda aunque la dependencia falle ----------

    @Test
    void crearAunConFalloAlCrearLaDependencia() {

        model.prepararNuevoParaProcedimiento(idProcedimiento);
        model.getRegistro().setNombre("Ecografia");
        when(procedimientoPasoDAO.buscarPorProcedimiento(idProcedimiento))
                .thenReturn(List.of());
        model.setIdPasoPadre(UUID.randomUUID());

        doThrow(new RuntimeException("fk"))
                .when(procedimientoPasoSecuenciaDAO).crear(any());

        model.btnCrearhandler(null);

        verify(procedimientoPasoDAO).crear(any());
        assertTrue(haMostradoAviso("Paso guardado, pero no se pudo crear la dependencia"));
    }

    @Test
    void modificarAunConFalloAlSincronizarLaDependencia() {

        model.setRegistro(paso);
        when(procedimientoPasoDAO.buscarPorProcedimiento(idProcedimiento))
                .thenReturn(List.of(paso));
        model.setIdPasoPadre(UUID.randomUUID());

        when(procedimientoPasoSecuenciaDAO.buscarPorProcedimientoPaso(any()))
                .thenThrow(new RuntimeException("boom"));

        model.btnModificarHandler();

        verify(procedimientoPasoDAO).actualizar(paso);
        assertTrue(haMostradoAviso("Paso guardado, pero no se pudo actualizar la dependencia"));
    }

    @Test
    void crearAunConFalloAlAsociarElExamen() {

        UUID idExamen = UUID.randomUUID();
        Examen examen = new Examen(idExamen);
        examen.setActivo(Boolean.TRUE);

        model.prepararNuevoParaProcedimiento(idProcedimiento);
        model.getRegistro().setNombre("Ecografia");
        when(procedimientoPasoDAO.buscarPorProcedimiento(idProcedimiento))
                .thenReturn(List.of());
        model.setIdExamenNuevo(idExamen);

        when(examenDAO.buscar(idExamen))
                .thenThrow(new RuntimeException("boom"));

        model.btnCrearhandler(null);

        verify(procedimientoPasoDAO).crear(any());
        assertTrue(haMostradoAviso("Paso guardado, pero no se pudo asociar el examen"));
    }

    @Test
    void crearConExamenNoEncontradoNoLoAsocia() {

        UUID idExamen = UUID.randomUUID();

        model.prepararNuevoParaProcedimiento(idProcedimiento);
        model.getRegistro().setNombre("Ecografia");
        when(procedimientoPasoDAO.buscarPorProcedimiento(idProcedimiento))
                .thenReturn(List.of());
        model.setIdExamenNuevo(idExamen);

        when(examenDAO.buscar(idExamen)).thenReturn(null);

        model.btnCrearhandler(null);

        verify(procedimientoPasoExamenDAO, never()).crear(any());
        verify(procedimientoPasoDAO).crear(any());
    }

    @Test
    void prepararNuevoDependienteSinProcedimientoNoTienePadre() {

        model.prepararNuevoDependiente(null);

        assertNull(model.getIdPasoPadre());
        assertEquals("", model.getNombrePasoPadre());
    }

    @Test
    void seleccionarRegistroSinListaNoCargaDependencia() {

        model.setRegistros(null);

        model.btnSeleccionarRegistro(UUID.randomUUID());

        assertNull(model.getRegistro());
        assertNull(model.getIdPasoPadre());
        verify(procedimientoPasoExamenModels)
                .setIdAsociacionSeleccionada(null);
    }

    // ---------- helpers ----------

    private boolean haMostradoAviso(String resumenEsperado) {

        ArgumentCaptor<FacesMessage> captor =
                ArgumentCaptor.forClass(FacesMessage.class);

        verify(fc, atLeastOnce()).addMessage(isNull(), captor.capture());

        return captor.getAllValues().stream()
                .anyMatch(m -> resumenEsperado.equals(m.getSummary()));
    }
}
