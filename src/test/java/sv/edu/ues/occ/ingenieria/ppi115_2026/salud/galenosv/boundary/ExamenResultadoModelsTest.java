package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import jakarta.faces.context.FacesContext;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ExamenResultadoDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ExamenResultado;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.OrdenExamen;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ExamenResultadoModelsTest {

    @Mock
    private ExamenResultadoDAO examenResultadoDAO;

    @Mock
    private FacesContext fc;

    @InjectMocks
    private ExamenResultadoModels model;

    private ExamenResultado registro;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        registro = new ExamenResultado(UUID.randomUUID());
        registro.setIdOrdenExamen(new OrdenExamen(UUID.randomUUID()));
        registro.setResultado("Negativo");
        registro.setFechaCreacion(new Date());

        when(examenResultadoDAO.findRange(0, 100)).thenReturn(List.of());
    }


    @Test
    void crearSinOrdenDeExamenMuestraError() {

        registro.setIdOrdenExamen(null);
        model.setRegistro(registro);

        model.btnCrearhandler(null);

        verify(examenResultadoDAO, never()).crear(any());
        verify(fc).validationFailed();
    }

    @Test
    void crearSinResultadoMuestraError() {

        registro.setResultado("   ");
        model.setRegistro(registro);

        model.btnCrearhandler(null);

        verify(examenResultadoDAO, never()).crear(any());
        verify(fc).validationFailed();
    }

    @Test
    void crearSinFechaDeCreacionMuestraError() {

        registro.setFechaCreacion(null);
        model.setRegistro(registro);

        model.btnCrearhandler(null);

        verify(examenResultadoDAO, never()).crear(any());
        verify(fc).validationFailed();
    }

    @Test
    void crearConDatosValidosGuarda() {

        model.setRegistro(registro);
        model.btnCrearhandler(null);

        verify(examenResultadoDAO).crear(registro);
        assertNull(model.getRegistro());
        verify(fc, never()).validationFailed();
    }

}
