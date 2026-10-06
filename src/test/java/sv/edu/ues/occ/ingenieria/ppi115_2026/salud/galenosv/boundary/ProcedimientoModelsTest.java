package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ProcedimientoDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ProcedimientoPasoDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ProcedimientoPasoExamenDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Examen;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Procedimiento;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ProcedimientoPaso;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ProcedimientoPasoExamen;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProcedimientoModelsTest {

    @Mock
    private ProcedimientoDAO procedimientoDAO;

    @Mock
    private ProcedimientoPasoDAO procedimientoPasoDAO;

    @Mock
    private ProcedimientoPasoExamenDAO procedimientoPasoExamenDAO;

    @Mock
    private ProcedimientoPasoModels procedimientoPasoModels;

    @Mock
    private FacesContext fc;

    @Mock
    private ExternalContext externalContext;

    @InjectMocks
    private ProcedimientoModels model;

    private Procedimiento procedimiento;
    private ProcedimientoPaso paso;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        procedimiento = new Procedimiento(UUID.randomUUID());
        procedimiento.setNombre("Radiografia");
        procedimiento.setActivo(Boolean.TRUE);

        paso = new ProcedimientoPaso(UUID.randomUUID());
        paso.setNombre("Tomografia");
        paso.setIdProcedimiento(procedimiento);

        when(fc.getExternalContext()).thenReturn(externalContext);
        when(procedimientoDAO.findRange(0, 100)).thenReturn(List.of());
    }

    private void parametros(String idProc) {

        Map<String, String> mapa = new HashMap<>();

        if (idProc != null) {
            mapa.put("idProc", idProc);
        }

        when(externalContext.getRequestParameterMap()).thenReturn(mapa);
    }


    // ---------- Creacion ----------

    @Test
    void crearRegistroNuevoAsignaIdYActivo() {

        Procedimiento nuevo = model.crearRegistroNuevo();

        assertNotNull(nuevo.getIdProcedimiento());
        assertEquals(Boolean.TRUE, nuevo.getActivo());
    }

    @Test
    void btnNuevoHandlerLimpiaLosPasos() {

        model.setNodoSeleccionado(
                new DefaultTreeNode<>("paso", new ProcedimientoModels.Nodo(
                        null, paso, List.of(), 0), null));

        model.btnNuevoHandler(null);

        assertEquals(Estado_CRUD.CREAR, model.getEstado());
        assertNotNull(model.getRegistro());
        assertNull(model.getNodoSeleccionado());
        verify(procedimientoPasoModels).btnCancelar();
    }


    // ---------- Validaciones de nombre ----------

    @Test
    void crearSinNombreMuestraError() {

        procedimiento.setNombre("  ");
        model.setRegistro(procedimiento);

        model.btnCrearhandler(null);

        verify(procedimientoDAO, never()).crear(any());
        verify(fc).validationFailed();
    }

    @Test
    void crearConNombreDuplicadoMuestraError() {

        Procedimiento otro = new Procedimiento(UUID.randomUUID());
        otro.setNombre("Radiografia");

        when(procedimientoDAO.buscarPorNombre("Radiografia"))
                .thenReturn(List.of(otro));
        model.setRegistro(procedimiento);

        model.btnCrearhandler(null);

        verify(procedimientoDAO, never()).crear(any());
        verify(fc).addMessage(isNull(), any(FacesMessage.class));
        verify(fc).validationFailed();
    }

    @Test
    void crearConNombreValidoGuardaYLoRecorta() {

        procedimiento.setNombre("  Radiografia  ");
        when(procedimientoDAO.buscarPorNombre("Radiografia"))
                .thenReturn(List.of());

        model.setRegistro(procedimiento);
        model.btnCrearhandler(null);

        verify(procedimientoDAO).crear(procedimiento);
        assertEquals("Radiografia", procedimiento.getNombre());
        assertNull(model.getRegistro());
    }


    // ---------- Editar ----------

    @Test
    void editarProcedimientoSeleccionaElRegistro() {

        model.setRegistros(List.of(procedimiento));
        model.setNodoSeleccionado(
                new DefaultTreeNode<>("paso", new ProcedimientoModels.Nodo(
                        null, paso, List.of(), 0), null));

        model.editarProcedimiento(procedimiento.getIdProcedimiento());

        assertSame(procedimiento, model.getRegistro());
        assertEquals(Estado_CRUD.MODIFICAR, model.getEstado());
        assertNull(model.getNodoSeleccionado());
        verify(procedimientoPasoModels).btnCancelar();
    }

    @Test
    void editarProcedimientoQueNoEstaEnLaTablaMuestraError() {

        model.setRegistros(List.of(procedimiento));

        model.editarProcedimiento(UUID.randomUUID());

        assertNull(model.getRegistro());
        verify(fc).validationFailed();
    }

    @Test
    void editarSeleccionadoUsaElRegistroActual() {

        model.setRegistros(List.of(procedimiento));
        model.setRegistro(procedimiento);

        model.editarSeleccionado();

        assertEquals(Estado_CRUD.MODIFICAR, model.getEstado());
        assertSame(procedimiento, model.getRegistro());
    }

    @Test
    void abrirPorParametroAbreElProcedimiento() {

        model.setRegistros(List.of(procedimiento));
        parametros(procedimiento.getIdProcedimiento().toString());

        model.abrirPorParametro();

        assertSame(procedimiento, model.getRegistro());
        verify(fc, never()).validationFailed();
    }

    @Test
    void abrirPorParametroConParametroInvalidoMuestraError() {

        model.setRegistros(List.of(procedimiento));
        parametros("no-es-uuid");

        model.abrirPorParametro();

        assertNull(model.getRegistro());
        verify(fc).validationFailed();
    }

    @Test
    void abrirPorParametroSinParametroMuestraError() {

        model.setRegistros(List.of(procedimiento));
        parametros(null);

        model.abrirPorParametro();

        assertNull(model.getRegistro());
        verify(fc).validationFailed();
    }


    // ---------- Abrir el editor de un paso ----------

    @Test
    void abrirEditorPasoSeleccionaElPasoDelNodo() {

        model.setNodoSeleccionado(
                new DefaultTreeNode<>("paso", new ProcedimientoModels.Nodo(
                        null, paso, List.of(), 0), null));

        model.abrirEditorPaso();

        verify(procedimientoPasoModels).btnSeleccionarRegistro(
                paso.getIdProcedimientoPaso());
    }

    @Test
    void abrirEditorPasoSinNodoMuestraError() {

        model.setNodoSeleccionado(null);

        model.abrirEditorPaso();

        verify(procedimientoPasoModels, never())
                .btnSeleccionarRegistro(any());
        verify(fc).validationFailed();
    }

    @Test
    void getArbolPasosSinRegistroDevuelveLaRaiz() {

        TreeNode<ProcedimientoModels.Nodo> arbol = model.getArbolPasos();

        assertNotNull(arbol);
        assertTrue(arbol.getChildren().isEmpty());
        verify(procedimientoPasoDAO, never()).buscarPorProcedimiento(any());
    }

    @Test
    void getArbolPasosEncadenaLosPasos() {

        ProcedimientoPaso segundo = new ProcedimientoPaso(UUID.randomUUID());
        segundo.setNombre("Ecografia");
        segundo.setIdProcedimiento(procedimiento);

        model.setRegistro(procedimiento);

        when(procedimientoPasoDAO.buscarPorProcedimiento(
                procedimiento.getIdProcedimiento()))
                .thenReturn(List.of(paso, segundo));
        when(procedimientoPasoExamenDAO.buscarPorProcedimientoPaso(any()))
                .thenReturn(List.of());

        TreeNode<ProcedimientoModels.Nodo> arbol = model.getArbolPasos();

        assertEquals(1, arbol.getChildren().size());

        TreeNode<ProcedimientoModels.Nodo> primero = arbol.getChildren().get(0);
        TreeNode<ProcedimientoModels.Nodo> segundoNodo =
                primero.getChildren().get(0);

        assertEquals("Tomografia", primero.getData().getPaso().getNombre());
        assertEquals("Ecografia", segundoNodo.getData().getPaso().getNombre());

        assertEquals(1, primero.getChildren().size());
        assertTrue(segundoNodo.getChildren().isEmpty());
        assertSame(arbol, primero.getParent());
        assertSame(primero, segundoNodo.getParent());
    }

    @Test
    void getArbolPasosIncluyeLosExamenesDeCadaPaso() {

        Examen examen = new Examen(UUID.randomUUID());
        examen.setNombre("Hemograma");

        ProcedimientoPasoExamen asociacion = new ProcedimientoPasoExamen(
                UUID.randomUUID());
        asociacion.setIdExamen(examen);

        model.setRegistro(procedimiento);

        when(procedimientoPasoDAO.buscarPorProcedimiento(
                procedimiento.getIdProcedimiento()))
                .thenReturn(List.of(paso));
        when(procedimientoPasoExamenDAO.buscarPorProcedimientoPaso(
                paso.getIdProcedimientoPaso()))
                .thenReturn(List.of(asociacion));

        TreeNode<ProcedimientoModels.Nodo> arbol = model.getArbolPasos();

        ProcedimientoModels.Nodo nodo = arbol.getChildren().get(0).getData();

        assertEquals(1, nodo.getExamenes().size());
        assertEquals("Hemograma", nodo.getNombresExamenes());
    }


    // ---------- Nodo ----------

    @Test
    void nodoExponeSusDatos() {

        ProcedimientoPasoExamen asociacion = new ProcedimientoPasoExamen(
                UUID.randomUUID());
        Examen examen = new Examen(UUID.randomUUID());
        examen.setNombre("Hemograma");
        asociacion.setIdExamen(examen);

        ProcedimientoModels.Nodo nodo = new ProcedimientoModels.Nodo(
                procedimiento, paso, List.of(asociacion), 3);

        assertSame(procedimiento, nodo.getProcedimiento());
        assertSame(paso, nodo.getPaso());
        assertEquals(1, nodo.getExamenes().size());
        assertEquals(3, nodo.getTotalPasos());
        assertEquals("Hemograma", nodo.getNombresExamenes());
    }

    @Test
    void nodoSinExamenesDevuelveTextoVacio() {

        ProcedimientoModels.Nodo nodo = new ProcedimientoModels.Nodo(
                procedimiento, paso, List.of(), 0);

        assertEquals("", nodo.getNombresExamenes());
    }

    @Test
    void modificarConDatosValidosActualiza() {

        when(procedimientoDAO.buscarPorNombre("Radiografia"))
                .thenReturn(List.of());

        model.setRegistros(List.of(procedimiento));
        model.btnSeleccionarRegistro(procedimiento.getIdProcedimiento());

        model.btnModificarHandler();

        verify(procedimientoDAO).actualizar(procedimiento);
        assertNull(model.getRegistro());
        assertEquals(Estado_CRUD.NINGUNO, model.getEstado());
        verify(fc, never()).validationFailed();
    }

    @Test
    void modificarConNombreDuplicadoMuestraError() {

        Procedimiento repetido = new Procedimiento(UUID.randomUUID());
        repetido.setNombre("radiografia");

        when(procedimientoDAO.buscarPorNombre("Radiografia"))
                .thenReturn(List.of(repetido));

        model.setRegistros(List.of(procedimiento));
        model.btnSeleccionarRegistro(procedimiento.getIdProcedimiento());

        model.btnModificarHandler();

        verify(procedimientoDAO, never()).actualizar(any());
        verify(fc).validationFailed();
    }

}
