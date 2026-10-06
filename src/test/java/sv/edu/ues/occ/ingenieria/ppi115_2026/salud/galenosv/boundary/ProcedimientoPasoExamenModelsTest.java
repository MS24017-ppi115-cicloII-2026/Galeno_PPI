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
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ExamenDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ProcedimientoPasoDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ProcedimientoPasoExamenDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Examen;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Procedimiento;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ProcedimientoPaso;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ProcedimientoPasoExamen;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProcedimientoPasoExamenModelsTest {

    @Mock
    private ProcedimientoPasoExamenDAO procedimientoPasoExamenDAO;

    @Mock
    private ExamenDAO examenDAO;

    @Mock
    private ProcedimientoPasoDAO procedimientoPasoDAO;

    @Mock
    private FacesContext fc;

    @InjectMocks
    private ProcedimientoPasoExamenModels model;

    private Examen examen;
    private ProcedimientoPaso paso;
    private UUID idExamen;
    private UUID idPaso;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        idExamen = UUID.randomUUID();
        idPaso = UUID.randomUUID();

        examen = new Examen(idExamen);
        examen.setNombre("Hemograma");
        examen.setActivo(Boolean.TRUE);

        Procedimiento procedimiento = new Procedimiento(UUID.randomUUID());
        procedimiento.setNombre("Radiografia");
        procedimiento.setActivo(Boolean.TRUE);

        paso = new ProcedimientoPaso(idPaso);
        paso.setNombre("Tomografia");
        paso.setIdProcedimiento(procedimiento);

        when(examenDAO.buscar(idExamen)).thenReturn(examen);
        when(examenDAO.findRange(0, 1000)).thenReturn(List.of());
        when(procedimientoPasoDAO.buscar(idPaso)).thenReturn(paso);
        when(procedimientoPasoExamenDAO.buscarPorProcedimientoPaso(idPaso))
                .thenReturn(List.of());
        when(procedimientoPasoExamenDAO.findRange(0, 100))
                .thenReturn(List.of());
    }

    private ProcedimientoPasoExamen crearAsociacion(UUID idExamen,
            UUID idPaso) {
        ProcedimientoPasoExamen asociacion = new ProcedimientoPasoExamen(
                UUID.randomUUID());
        asociacion.setIdExamen(new Examen(idExamen));
        asociacion.setIdProcedimientoPaso(new ProcedimientoPaso(idPaso));
        return asociacion;
    }


    // ---------- Creacion ----------

    @Test
    void crearRegistroNuevoAsignaIdYActivo() {

        ProcedimientoPasoExamen nuevo = model.crearRegistroNuevo();

        assertNotNull(nuevo.getIdProcedimientoPasoExamen());
        assertEquals(Boolean.TRUE, nuevo.getActivo());
    }

    @Test
    void prepararNuevoParaPasoAsignaElPaso() {

        model.prepararNuevoParaPaso(idPaso);

        assertEquals(Estado_CRUD.CREAR, model.getEstado());
        assertEquals(idPaso, model.getRegistro()
                .getIdProcedimientoPaso().getIdProcedimientoPaso());
    }

    @Test
    void prepararNuevoParaPasoNuloNoAsignaPaso() {

        model.prepararNuevoParaPaso(null);

        assertEquals(Estado_CRUD.CREAR, model.getEstado());
        assertNull(model.getRegistro().getIdProcedimientoPaso());
    }


    // ---------- Consultas ----------

    @Test
    void getRegistrosPorPasoNuloDevuelveVacio() {

        assertTrue(model.getRegistrosPorPaso(null).isEmpty());
        verify(procedimientoPasoExamenDAO, never())
                .buscarPorProcedimientoPaso(any());
    }

    @Test
    void getRegistrosPorPaso() {

        ProcedimientoPasoExamen asociacion = crearAsociacion(idExamen, idPaso);
        when(procedimientoPasoExamenDAO.buscarPorProcedimientoPaso(idPaso))
                .thenReturn(List.of(asociacion));

        assertEquals(1, model.getRegistrosPorPaso(idPaso).size());
    }

    @Test
    void getExamenesDevuelveSoloLosActivos() {

        Examen inactivo = new Examen(UUID.randomUUID());
        inactivo.setNombre("Inactivo");
        inactivo.setActivo(Boolean.FALSE);

        when(examenDAO.findRange(0, 1000))
                .thenReturn(List.of(examen, inactivo));

        List<Examen> resultado = model.getExamenes();

        assertEquals(1, resultado.size());
        assertSame(examen, resultado.get(0));
    }

    @Test
    void getExamenesAgregaElExamenInactivoDelRegistro() {

        Examen inactivo = new Examen(UUID.randomUUID());
        inactivo.setNombre("Inactivo");
        inactivo.setActivo(Boolean.FALSE);

        when(examenDAO.findRange(0, 1000)).thenReturn(List.of(examen));
        when(examenDAO.buscar(inactivo.getIdExamen())).thenReturn(inactivo);

        model.prepararNuevoParaPaso(idPaso);
        model.getRegistro().setIdExamen(inactivo);

        List<Examen> resultado = model.getExamenes();

        assertEquals(2, resultado.size());
        assertSame(inactivo, resultado.get(1));
    }


    // ---------- Seleccion en la lista ----------

    @Test
    void eliminarSeleccionadoSinSeleccionMuestraError() {

        model.eliminarSeleccionado();

        verify(procedimientoPasoExamenDAO, never()).eliminar(any());
        verify(fc).addMessage(isNull(), any(FacesMessage.class));
        verify(fc).validationFailed();
    }

    @Test
    void eliminarSeleccionadoEliminaYLimpiaLaSeleccion() {

        ProcedimientoPasoExamen asociacion = crearAsociacion(idExamen, idPaso);
        UUID id = asociacion.getIdProcedimientoPasoExamen();

        model.setRegistros(List.of(asociacion));
        model.setIdAsociacionSeleccionada(id);

        model.eliminarSeleccionado();

        verify(procedimientoPasoExamenDAO).eliminar(id);
        assertNull(model.getIdAsociacionSeleccionada());
    }


    // ---------- Validaciones ----------

    @Test
    void crearConExamenInactivoMuestraError() {

        examen.setActivo(Boolean.FALSE);

        model.prepararNuevoParaPaso(idPaso);
        model.getRegistro().setIdExamen(examen);

        model.btnCrearhandler(null);

        verify(procedimientoPasoExamenDAO, never()).crear(any());
        verify(fc).validationFailed();
    }

    @Test
    void crearConProcedimientoInactivoMuestraError() {

        paso.getIdProcedimiento().setActivo(Boolean.FALSE);

        model.prepararNuevoParaPaso(idPaso);
        model.getRegistro().setIdExamen(examen);

        model.btnCrearhandler(null);

        verify(procedimientoPasoExamenDAO, never()).crear(any());
        verify(fc).validationFailed();
    }

    @Test
    void crearConExamenDuplicadoMuestraError() {

        when(procedimientoPasoExamenDAO.buscarPorProcedimientoPaso(idPaso))
                .thenReturn(List.of(crearAsociacion(idExamen, idPaso)));

        model.prepararNuevoParaPaso(idPaso);
        model.getRegistro().setIdExamen(examen);

        model.btnCrearhandler(null);

        verify(procedimientoPasoExamenDAO, never()).crear(any());
        verify(fc).validationFailed();
    }

    @Test
    void crearConDatosValidosGuarda() {

        model.prepararNuevoParaPaso(idPaso);
        model.getRegistro().setIdExamen(examen);

        model.btnCrearhandler(null);

        verify(procedimientoPasoExamenDAO).crear(any());
        assertNull(model.getRegistro());
        verify(fc, never()).validationFailed();
    }

    @Test
    void modificarConExamenDuplicadoNoActualiza() {

        when(procedimientoPasoExamenDAO.buscarPorProcedimientoPaso(idPaso))
                .thenReturn(List.of(crearAsociacion(idExamen, idPaso)));

        ProcedimientoPasoExamen registro = model.crearRegistroNuevo();
        registro.setIdExamen(examen);
        registro.setIdProcedimientoPaso(new ProcedimientoPaso(idPaso));

        model.setRegistro(registro);
        model.btnModificarHandler();

        verify(procedimientoPasoExamenDAO, never()).actualizar(any());
        verify(fc).validationFailed();
    }

    @Test
    void obtenerIdDevuelveElIdDelVinculo() {

        ProcedimientoPasoExamen vinculo =
                crearAsociacion(idExamen, idPaso);

        assertEquals(vinculo.getIdProcedimientoPasoExamen(),
                model.obtenerId(vinculo));
    }

    // ---------- btnModificarHandler (sin cobertura previa) ----------

    @Test
    void modificarConDatosValidosActualiza() {

        ProcedimientoPasoExamen asociacion = crearAsociacion(idExamen, idPaso);

        when(procedimientoPasoExamenDAO.buscarPorProcedimientoPaso(idPaso))
                .thenReturn(List.of());

        model.setRegistros(List.of(asociacion));
        model.btnSeleccionarRegistro(
                asociacion.getIdProcedimientoPasoExamen());

        model.btnModificarHandler();

        verify(procedimientoPasoExamenDAO).actualizar(asociacion);
        assertNull(model.getRegistro());
        assertEquals(Estado_CRUD.NINGUNO, model.getEstado());
        verify(fc, never()).validationFailed();
    }

    @Test
    void modificarConExamenDuplicadoMuestraError() {

        ProcedimientoPasoExamen asociacion = crearAsociacion(idExamen, idPaso);

        when(procedimientoPasoExamenDAO.buscarPorProcedimientoPaso(idPaso))
                .thenReturn(List.of(crearAsociacion(idExamen, idPaso)));

        model.setRegistros(List.of(asociacion));
        model.btnSeleccionarRegistro(
                asociacion.getIdProcedimientoPasoExamen());

        model.btnModificarHandler();

        verify(procedimientoPasoExamenDAO, never()).actualizar(any());
        verify(fc).validationFailed();
    }

    @Test
    void modificarIgnoraElProcedimientoInactivoPorqueNoEsCreacion() {

        Procedimiento procedimiento = new Procedimiento(UUID.randomUUID());
        procedimiento.setNombre("Radiografia");
        procedimiento.setActivo(Boolean.FALSE);

        ProcedimientoPaso pasoInactivo = new ProcedimientoPaso(idPaso);
        pasoInactivo.setIdProcedimiento(procedimiento);

        when(procedimientoPasoDAO.buscar(idPaso)).thenReturn(pasoInactivo);
        when(procedimientoPasoExamenDAO.buscarPorProcedimientoPaso(idPaso))
                .thenReturn(List.of());

        ProcedimientoPasoExamen asociacion = crearAsociacion(idExamen, idPaso);

        model.setRegistros(List.of(asociacion));
        model.btnSeleccionarRegistro(
                asociacion.getIdProcedimientoPasoExamen());

        model.btnModificarHandler();

        verify(procedimientoPasoExamenDAO).actualizar(asociacion);
        verify(fc, never()).validationFailed();
    }

}
