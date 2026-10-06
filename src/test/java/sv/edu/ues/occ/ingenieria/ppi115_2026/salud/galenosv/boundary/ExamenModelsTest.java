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
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Examen;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ExamenModelsTest {

    @Mock
    private ExamenDAO examenDAO;

    @Mock
    private FacesContext fc;

    @InjectMocks
    private ExamenModels model;

    private Examen examen;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        examen = new Examen(UUID.randomUUID());
        examen.setNombre("Hemograma");
        examen.setActivo(Boolean.TRUE);

        when(examenDAO.findRange(0, 100)).thenReturn(List.of());
        when(examenDAO.buscarPorNombre(any())).thenReturn(List.of());
    }

    private Examen crearOtro(String nombre) {
        Examen otro = new Examen(UUID.randomUUID());
        otro.setNombre(nombre);
        return otro;
    }


    @Test
    void crearSinNombreMuestraError() {

        examen.setNombre("  ");
        model.setRegistro(examen);

        model.btnCrearhandler(null);

        verify(examenDAO, never()).crear(any());
        verify(fc).addMessage(isNull(), any(FacesMessage.class));
        verify(fc).validationFailed();
    }

    @Test
    void crearConNombreDuplicadoMuestraError() {

        when(examenDAO.buscarPorNombre("Hemograma"))
                .thenReturn(List.of(crearOtro("Hemograma")));
        model.setRegistro(examen);

        model.btnCrearhandler(null);

        verify(examenDAO, never()).crear(any());
        verify(fc).validationFailed();
    }

    @Test
    void crearConNombreValidoGuardaYLoRecorta() {

        examen.setNombre("  Hemograma  ");
        model.setRegistro(examen);

        model.btnCrearhandler(null);

        assertEquals("Hemograma", examen.getNombre());
        verify(examenDAO).crear(examen);
        assertNull(model.getRegistro());
    }

    @Test
    void modificarSinNombreMuestraError() {

        examen.setNombre(null);
        model.setRegistro(examen);

        model.btnModificarHandler();

        verify(examenDAO, never()).actualizar(any());
        verify(fc).validationFailed();
    }
}
