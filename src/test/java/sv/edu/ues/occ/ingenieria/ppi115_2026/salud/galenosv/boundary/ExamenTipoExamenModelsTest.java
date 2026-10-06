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
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ExamenTipoExamenDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.TipoExamenDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Examen;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ExamenTipoExamen;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.TipoExamen;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ExamenTipoExamenModelsTest {

    @Mock
    private ExamenTipoExamenDAO examenTipoExamenDAO;

    @Mock
    private ExamenDAO examenDAO;

    @Mock
    private TipoExamenDAO tipoExamenDAO;

    @Mock
    private FacesContext fc;

    @InjectMocks
    private ExamenTipoExamenModels model;

    private Examen examen;
    private TipoExamen tipo;
    private UUID idExamen;
    private UUID idTipo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        idExamen = UUID.randomUUID();
        idTipo = UUID.randomUUID();

        examen = new Examen(idExamen);
        examen.setNombre("Hemograma");
        examen.setActivo(Boolean.TRUE);

        tipo = new TipoExamen(idTipo);
        tipo.setNombre("Sangre");
        tipo.setActivo(Boolean.TRUE);

        when(examenDAO.buscar(idExamen)).thenReturn(examen);
        when(examenDAO.findRange(0, 1000)).thenReturn(List.of());
        when(tipoExamenDAO.buscar(idTipo)).thenReturn(tipo);
        when(tipoExamenDAO.findRange(0, 1000)).thenReturn(List.of());
        when(examenTipoExamenDAO.buscarPorExamen(idExamen))
                .thenReturn(List.of());
        when(examenTipoExamenDAO.findRange(0, 100)).thenReturn(List.of());
    }

    private ExamenTipoExamen crearVinculo() {
        ExamenTipoExamen vinculo = new ExamenTipoExamen(UUID.randomUUID());
        vinculo.setIdExamen(new Examen(idExamen));
        vinculo.setIdTipoExamen(new TipoExamen(idTipo));
        return vinculo;
    }


    @Test
    void crearRegistroNuevoAsignaId() {

        ExamenTipoExamen nuevo = model.crearRegistroNuevo();

        assertNotNull(nuevo.getIdExamenTipoExamen());
    }

    @Test
    void prepararNuevoParaExamenAsignaElExamen() {

        model.prepararNuevoParaExamen(idExamen);

        assertEquals(Estado_CRUD.CREAR, model.getEstado());
        assertEquals(idExamen,
                model.getRegistro().getIdExamen().getIdExamen());
    }

    @Test
    void getExamenesDevuelveSoloLosActivos() {

        Examen inactivo = new Examen(UUID.randomUUID());
        inactivo.setActivo(Boolean.FALSE);

        when(examenDAO.findRange(0, 1000))
                .thenReturn(List.of(examen, inactivo));

        assertEquals(1, model.getExamenes().size());
    }

    @Test
    void getTiposExamenDevuelveSoloLosActivos() {

        TipoExamen inactivo = new TipoExamen(UUID.randomUUID());
        inactivo.setActivo(Boolean.FALSE);

        when(tipoExamenDAO.findRange(0, 1000))
                .thenReturn(List.of(tipo, inactivo));

        assertEquals(1, model.getTiposExamen().size());
    }

    @Test
    void getTiposExamenAgregaElTipoInactivoDelRegistro() {

        TipoExamen inactivo = new TipoExamen(UUID.randomUUID());
        inactivo.setNombre("Inactivo");
        inactivo.setActivo(Boolean.FALSE);

        when(tipoExamenDAO.findRange(0, 1000)).thenReturn(List.of(tipo));
        when(tipoExamenDAO.buscar(inactivo.getIdTipoExamen()))
                .thenReturn(inactivo);

        model.prepararNuevoParaExamen(idExamen);
        model.getRegistro().setIdTipoExamen(inactivo);

        assertEquals(2, model.getTiposExamen().size());
    }


    @Test
    void crearConExamenInactivoMuestraError() {

        examen.setActivo(Boolean.FALSE);

        model.prepararNuevoParaExamen(idExamen);
        model.getRegistro().setIdTipoExamen(tipo);

        model.btnCrearhandler(null);

        verify(examenTipoExamenDAO, never()).crear(any());
        verify(fc).validationFailed();
    }

    @Test
    void crearConTipoInactivoMuestraError() {

        tipo.setActivo(Boolean.FALSE);

        model.prepararNuevoParaExamen(idExamen);
        model.getRegistro().setIdTipoExamen(tipo);

        model.btnCrearhandler(null);

        verify(examenTipoExamenDAO, never()).crear(any());
        verify(fc).validationFailed();
    }

    @Test
    void crearConVinculoDuplicadoMuestraError() {

        when(examenTipoExamenDAO.buscarPorExamen(idExamen))
                .thenReturn(List.of(crearVinculo()));

        model.prepararNuevoParaExamen(idExamen);
        model.getRegistro().setIdTipoExamen(tipo);

        model.btnCrearhandler(null);

        verify(examenTipoExamenDAO, never()).crear(any());
        verify(fc).validationFailed();
    }

    @Test
    void crearConDatosValidosGuarda() {

        model.prepararNuevoParaExamen(idExamen);
        model.getRegistro().setIdTipoExamen(tipo);

        model.btnCrearhandler(null);

        verify(examenTipoExamenDAO).crear(any());
        assertNull(model.getRegistro());
        verify(fc, never()).validationFailed();
    }


    // ---------- getExamenes: ramas no cubiertas ----------

    @Test
    void getExamenesAgregaElExamenInactivoDelRegistro() {

        Examen inactivo = new Examen(UUID.randomUUID());
        inactivo.setNombre("Inactivo");
        inactivo.setActivo(Boolean.FALSE);

        when(examenDAO.findRange(0, 1000)).thenReturn(List.of());
        when(examenDAO.buscar(inactivo.getIdExamen()))
                .thenReturn(inactivo);

        model.prepararNuevoParaExamen(idExamen);
        model.getRegistro().setIdExamen(inactivo);

        assertEquals(1, model.getExamenes().size());
    }

    @Test
    void getTiposExamenNoAgregaSiElDAONoEncuentraElTipo() {

        UUID ausente = UUID.randomUUID();

        TipoExamen otro = new TipoExamen(ausente);
        otro.setNombre("No persistido");
        otro.setActivo(Boolean.FALSE);

        when(tipoExamenDAO.findRange(0, 1000)).thenReturn(List.of());
        when(tipoExamenDAO.buscar(ausente)).thenReturn(null);

        model.prepararNuevoParaExamen(idExamen);
        model.getRegistro().setIdTipoExamen(otro);

        assertTrue(model.getTiposExamen().isEmpty());
    }

    @Test
    void elVinculoActualNoCuentaComoDuplicado() {

        ExamenTipoExamen vinculo = crearVinculo();

        when(examenTipoExamenDAO.buscarPorExamen(idExamen))
                .thenReturn(List.of(vinculo));

        model.setRegistro(vinculo);

        model.btnCrearhandler(null);

        verify(examenTipoExamenDAO).crear(vinculo);
        verify(fc, never()).validationFailed();
    }

    @Test
    void modificarConDatosValidosActualiza() {

        ExamenTipoExamen vinculo = crearVinculo();

        model.setRegistros(List.of(vinculo));
        model.btnSeleccionarRegistro(vinculo.getIdExamenTipoExamen());

        model.btnModificarHandler();

        verify(examenTipoExamenDAO).actualizar(vinculo);
        assertNull(model.getRegistro());
        assertEquals(Estado_CRUD.NINGUNO, model.getEstado());
        verify(fc, never()).validationFailed();
    }

    @Test
    void modificarConVinculoDuplicadoMuestraError() {

        ExamenTipoExamen vinculo = crearVinculo();

        when(examenTipoExamenDAO.buscarPorExamen(idExamen))
                .thenReturn(List.of(crearVinculo()));

        model.setRegistros(List.of(vinculo));
        model.btnSeleccionarRegistro(vinculo.getIdExamenTipoExamen());
        model.getRegistro().setIdTipoExamen(tipo);

        model.btnModificarHandler();

        verify(examenTipoExamenDAO, never()).actualizar(any());
        verify(fc).validationFailed();
    }

}
