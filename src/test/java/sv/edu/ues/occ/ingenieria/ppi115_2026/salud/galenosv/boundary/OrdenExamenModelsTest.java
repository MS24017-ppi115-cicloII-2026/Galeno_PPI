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
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.OrdenExamenDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ConsultaProcedimientoPaso;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.OrdenExamen;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrdenExamenModelsTest {

    @Mock
    private OrdenExamenDAO ordenExamenDAO;

    @Mock
    private FacesContext fc;

    @InjectMocks
    private OrdenExamenModels model;

    private OrdenExamen registro;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        registro = new OrdenExamen(UUID.randomUUID());
        registro.setIdConsultaProcedimientoPaso(
                new ConsultaProcedimientoPaso(UUID.randomUUID()));
        registro.setFechaCreacion(new Date());

        when(ordenExamenDAO.findRange(0, 100)).thenReturn(List.of());
    }


    @Test
    void crearSinPasoDeConsultaMuestraError() {

        registro.setIdConsultaProcedimientoPaso(null);
        model.setRegistro(registro);

        model.btnCrearhandler(null);

        verify(ordenExamenDAO, never()).crear(any());
        verify(fc).validationFailed();
    }

    @Test
    void crearSinFechaDeCreacionMuestraError() {

        registro.setFechaCreacion(null);
        model.setRegistro(registro);

        model.btnCrearhandler(null);

        verify(ordenExamenDAO, never()).crear(any());
        verify(fc).validationFailed();
    }

    @Test
    void crearConDatosValidosGuarda() {

        model.setRegistro(registro);
        model.btnCrearhandler(null);

        verify(ordenExamenDAO).crear(registro);
        assertNull(model.getRegistro());
        verify(fc, never()).validationFailed();
    }

}
