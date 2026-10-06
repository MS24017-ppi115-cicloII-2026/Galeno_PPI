package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ProcedimientoPasoDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ProcedimientoPasoSecuenciaDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Procedimiento;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ProcedimientoPaso;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ProcedimientoPasoSecuencia;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProcedimientoPasoSecuenciaModelsTest {

    @Mock
    private ProcedimientoPasoSecuenciaDAO procedimientoPasoSecuenciaDAO;

    @Mock
    private ProcedimientoPasoDAO procedimientoPasoDAO;

    @Mock
    private FacesContext fc;

    @InjectMocks
    private ProcedimientoPasoSecuenciaModels model;

    private Procedimiento procedimiento;
    private ProcedimientoPaso primero;
    private ProcedimientoPaso segundo;
    private UUID idPrimero;
    private UUID idSegundo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        procedimiento = new Procedimiento(UUID.randomUUID());
        procedimiento.setNombre("Radiografia");
        procedimiento.setActivo(Boolean.TRUE);

        idPrimero = UUID.randomUUID();
        idSegundo = UUID.randomUUID();

        primero = new ProcedimientoPaso(idPrimero);
        primero.setNombre("Tomografia");
        primero.setIdProcedimiento(procedimiento);

        segundo = new ProcedimientoPaso(idSegundo);
        segundo.setNombre("Ecografia");
        segundo.setIdProcedimiento(procedimiento);

        when(procedimientoPasoDAO.buscar(idPrimero)).thenReturn(primero);
        when(procedimientoPasoDAO.buscar(idSegundo)).thenReturn(segundo);
        when(procedimientoPasoSecuenciaDAO.buscarPorProcedimientoPaso(any()))
                .thenReturn(List.of());
        when(procedimientoPasoSecuenciaDAO.findRange(0, 100))
                .thenReturn(List.of());
    }

    private ProcedimientoPasoSecuencia crearSecuencia(UUID idPaso,
            UUID idReferencia) {
        ProcedimientoPasoSecuencia secuencia = new ProcedimientoPasoSecuencia(
                UUID.randomUUID());
        secuencia.setIdProcedimientoPaso(new ProcedimientoPaso(idPaso));
        secuencia.setIdProcedimientoPasoReferencia(idReferencia);
        secuencia.setTipoSecuencia("AFTER");
        return secuencia;
    }

    private ProcedimientoPasoSecuencia crearVinculoValido() {

        ProcedimientoPasoSecuencia secuencia = model.crearRegistroNuevo();
        secuencia.setIdProcedimientoPaso(primero);
        secuencia.setIdProcedimientoPasoReferencia(idSegundo);
        secuencia.setTipoSecuencia("AFTER");
        return secuencia;
    }


    @Test
    void crearRegistroNuevoAsignaId() {

        ProcedimientoPasoSecuencia nuevo = model.crearRegistroNuevo();

        assertNotNull(nuevo.getIdProcedimientoPasoSecuencia());
    }

    @Test
    void getPasosPorProcedimiento() {

        when(procedimientoPasoDAO.buscarPorProcedimiento(
                procedimiento.getIdProcedimiento()))
                .thenReturn(List.of(primero, segundo));

        assertEquals(2, model.getPasosPorProcedimiento(
                procedimiento.getIdProcedimiento()).size());
    }


    // ---------- Validaciones ----------

    @Test
    void crearSinTipoDeSecuenciaMuestraError() {

        ProcedimientoPasoSecuencia secuencia = model.crearRegistroNuevo();
        secuencia.setTipoSecuencia("  ");

        model.setRegistro(secuencia);
        model.btnCrearhandler(null);

        verify(procedimientoPasoSecuenciaDAO, never()).crear(any());
        verify(fc).validationFailed();
    }

    @Test
    void crearConTipoDeSecuenciaDemasiadoLargoMuestraError() {

        ProcedimientoPasoSecuencia secuencia = model.crearRegistroNuevo();
        secuencia.setTipoSecuencia("A".repeat(101));

        model.setRegistro(secuencia);
        model.btnCrearhandler(null);

        verify(procedimientoPasoSecuenciaDAO, never()).crear(any());
        verify(fc).validationFailed();
    }

    @Test
    void crearNormalizaElTipoDeSecuenciaAMayusculas() {

        ProcedimientoPasoSecuencia secuencia = model.crearRegistroNuevo();
        secuencia.setTipoSecuencia("  after  ");

        model.setRegistro(secuencia);
        model.btnCrearhandler(null);

        assertEquals("AFTER", secuencia.getTipoSecuencia());
        verify(procedimientoPasoSecuenciaDAO).crear(secuencia);
    }

    @Test
    void crearUnPasoQueDependeDeSiMismoMuestraError() {

        ProcedimientoPasoSecuencia secuencia = model.crearRegistroNuevo();
        secuencia.setTipoSecuencia("AFTER");
        secuencia.setIdProcedimientoPaso(primero);
        secuencia.setIdProcedimientoPasoReferencia(idPrimero);

        model.setRegistro(secuencia);
        model.btnCrearhandler(null);

        verify(procedimientoPasoSecuenciaDAO, never()).crear(any());
        verify(fc).validationFailed();
    }

    @Test
    void crearConReferenciaDeOtroProcedimientoMuestraError() {

        Procedimiento otroProcedimiento = new Procedimiento(UUID.randomUUID());
        otroProcedimiento.setNombre("Ecografia");

        ProcedimientoPaso pasoAjeno = new ProcedimientoPaso(UUID.randomUUID());
        pasoAjeno.setNombre("Ajeno");
        pasoAjeno.setIdProcedimiento(otroProcedimiento);

        when(procedimientoPasoDAO.buscar(pasoAjeno.getIdProcedimientoPaso()))
                .thenReturn(pasoAjeno);

        ProcedimientoPasoSecuencia secuencia = model.crearRegistroNuevo();
        secuencia.setTipoSecuencia("AFTER");
        secuencia.setIdProcedimientoPaso(primero);
        secuencia.setIdProcedimientoPasoReferencia(pasoAjeno.getIdProcedimientoPaso());

        model.setRegistro(secuencia);
        model.btnCrearhandler(null);

        verify(procedimientoPasoSecuenciaDAO, never()).crear(any());
        verify(fc).validationFailed();
    }

    @Test
    void crearConDependenciaDuplicadaMuestraError() {

        when(procedimientoPasoSecuenciaDAO.buscarPorProcedimientoPaso(idPrimero))
                .thenReturn(List.of(crearSecuencia(idPrimero, idSegundo)));

        model.setRegistro(crearVinculoValido());
        model.btnCrearhandler(null);

        verify(procedimientoPasoSecuenciaDAO, never()).crear(any());
        verify(fc).validationFailed();
    }

    @Test
    void crearConCicloMuestraError() {

        when(procedimientoPasoSecuenciaDAO.buscarPorProcedimientoPaso(idSegundo))
                .thenReturn(List.of(crearSecuencia(idSegundo, idPrimero)));

        model.setRegistro(crearVinculoValido());
        model.btnCrearhandler(null);

        verify(procedimientoPasoSecuenciaDAO, never()).crear(any());
        verify(fc).validationFailed();
    }

    @Test
    void crearConProcedimientoInactivoMuestraError() {

        procedimiento.setActivo(Boolean.FALSE);

        model.btnNuevoHandler(null);
        model.getRegistro().setTipoSecuencia("AFTER");
        model.getRegistro().setIdProcedimientoPaso(primero);

        model.btnCrearhandler(null);

        verify(procedimientoPasoSecuenciaDAO, never()).crear(any());
        verify(fc).validationFailed();
    }

    @Test
    void crearConDatosValidosGuarda() {

        model.setRegistro(crearVinculoValido());

        model.btnCrearhandler(null);

        verify(procedimientoPasoSecuenciaDAO).crear(any());
        assertNull(model.getRegistro());
        verify(fc, never()).validationFailed();
    }

    @Test
    void modificarConDatosValidosActualiza() {

        ProcedimientoPasoSecuencia vinculo = crearSecuencia(idPrimero, idSegundo);

        model.setRegistros(List.of(vinculo));
        model.btnSeleccionarRegistro(
                vinculo.getIdProcedimientoPasoSecuencia());

        model.btnModificarHandler();

        verify(procedimientoPasoSecuenciaDAO).actualizar(vinculo);
        assertNull(model.getRegistro());
        assertEquals(Estado_CRUD.NINGUNO, model.getEstado());
        verify(fc, never()).validationFailed();
    }

    @Test
    void modificarConVinculoDuplicadoMuestraError() {

        ProcedimientoPasoSecuencia vinculo = crearSecuencia(idPrimero, idSegundo);

        when(procedimientoPasoSecuenciaDAO.buscarPorProcedimientoPaso(idPrimero))
                .thenReturn(List.of(crearSecuencia(idPrimero, idSegundo)));

        model.setRegistros(List.of(vinculo));
        model.btnSeleccionarRegistro(
                vinculo.getIdProcedimientoPasoSecuencia());

        model.btnModificarHandler();

        verify(procedimientoPasoSecuenciaDAO, never()).actualizar(any());
        verify(fc).validationFailed();
    }

}
