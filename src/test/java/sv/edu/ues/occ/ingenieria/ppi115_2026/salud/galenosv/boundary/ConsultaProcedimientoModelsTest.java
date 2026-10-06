package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

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
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Consulta;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ConsultaProcedimiento;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ConsultaProcedimientoModelsTest {

    @Mock
    private ConsultaProcedimientoDAO consultaProcedimientoDAO;

    @Mock
    private FacesContext fc;

    @InjectMocks
    private ConsultaProcedimientoModels model;

    private ConsultaProcedimiento registro;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        registro = new ConsultaProcedimiento(UUID.randomUUID());
        registro.setIdConsulta(new Consulta(UUID.randomUUID()));
        registro.setIdProcedimiento(UUID.randomUUID());
        registro.setFechaInicio(fecha(2026, Calendar.JANUARY, 10, 9));

        when(consultaProcedimientoDAO.findRange(0, 100))
                .thenReturn(List.of());
    }

    private Date fecha(int anio, int mes, int dia, int hora) {
        Calendar calendario = Calendar.getInstance();
        calendario.set(anio, mes, dia, hora, 0, 0);
        return calendario.getTime();
    }


    @Test
    void crearSinConsultaMuestraError() {

        registro.setIdConsulta(null);
        model.setRegistro(registro);

        model.btnCrearhandler(null);

        verify(consultaProcedimientoDAO, never()).crear(any());
        verify(fc).validationFailed();
    }

    @Test
    void crearSinProcedimientoMuestraError() {

        registro.setIdProcedimiento(null);
        model.setRegistro(registro);

        model.btnCrearhandler(null);

        verify(consultaProcedimientoDAO, never()).crear(any());
        verify(fc).validationFailed();
    }

    @Test
    void crearSinFechaMuestraError() {

        registro.setFechaInicio(null);
        model.setRegistro(registro);

        model.btnCrearhandler(null);

        verify(consultaProcedimientoDAO, never()).crear(any());
        verify(fc).validationFailed();
    }

    @Test
    void crearConFechaFinAnteriorMuestraError() {

        registro.setFechaFin(fecha(2026, Calendar.JANUARY, 5, 9));
        model.setRegistro(registro);

        model.btnCrearhandler(null);

        verify(consultaProcedimientoDAO, never()).crear(any());
        verify(fc).validationFailed();
    }

    @Test
    void crearConDatosValidosGuarda() {

        model.setRegistro(registro);
        model.btnCrearhandler(null);

        verify(consultaProcedimientoDAO).crear(registro);
        assertNull(model.getRegistro());
        verify(fc, never()).validationFailed();
    }

}
