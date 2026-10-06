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
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ConsultaProcedimientoPasoDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ConsultaProcedimiento;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ConsultaProcedimientoPaso;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.PersonaRol;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ConsultaProcedimientoPasoModelsTest {

    @Mock
    private ConsultaProcedimientoPasoDAO consultaProcedimientoPasoDAO;

    @Mock
    private FacesContext fc;

    @InjectMocks
    private ConsultaProcedimientoPasoModels model;

    private ConsultaProcedimientoPaso registro;
    private PersonaRol responsable;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        responsable = new PersonaRol(UUID.randomUUID());

        registro = new ConsultaProcedimientoPaso(UUID.randomUUID());
        registro.setIdConsultaProcedimiento(
                new ConsultaProcedimiento(UUID.randomUUID()));
        registro.setIdPersonaRol(responsable);
        registro.setEstado("PENDIENTE");
        registro.setFechaInicio(fecha(2026, Calendar.JANUARY, 10, 9));

        when(consultaProcedimientoPasoDAO.findRange(0, 100))
                .thenReturn(List.of());
        when(consultaProcedimientoPasoDAO
                .buscarPersonasExcluyendoRol("paciente"))
                .thenReturn(List.of(responsable));
    }

    private Date fecha(int anio, int mes, int dia, int hora) {
        Calendar calendario = Calendar.getInstance();
        calendario.set(anio, mes, dia, hora, 0, 0);
        return calendario.getTime();
    }


    @Test
    void crearSinConsultaProcedimientoMuestraError() {

        registro.setIdConsultaProcedimiento(null);
        model.setRegistro(registro);

        model.btnCrearhandler(null);

        verify(consultaProcedimientoPasoDAO, never()).crear(any());
        verify(fc).validationFailed();
    }

    @Test
    void crearSinResponsableMuestraError() {

        registro.setIdPersonaRol(null);
        model.setRegistro(registro);

        model.btnCrearhandler(null);

        verify(consultaProcedimientoPasoDAO, never()).crear(any());
        verify(fc).validationFailed();
    }

    @Test
    void crearConUnPacienteComoResponsableMuestraError() {

        PersonaRol paciente = new PersonaRol(UUID.randomUUID());

        when(consultaProcedimientoPasoDAO
                .buscarPersonasExcluyendoRol("paciente"))
                .thenReturn(List.of(responsable));

        registro.setIdPersonaRol(paciente);
        model.setRegistro(registro);

        model.btnCrearhandler(null);

        verify(consultaProcedimientoPasoDAO, never()).crear(any());
        verify(fc).validationFailed();
    }

    @Test
    void crearSinEstadoMuestraError() {

        registro.setEstado("   ");
        model.setRegistro(registro);

        model.btnCrearhandler(null);

        verify(consultaProcedimientoPasoDAO, never()).crear(any());
        verify(fc).validationFailed();
    }

    @Test
    void crearSinFechaMuestraError() {

        registro.setFechaInicio(null);
        model.setRegistro(registro);

        model.btnCrearhandler(null);

        verify(consultaProcedimientoPasoDAO, never()).crear(any());
        verify(fc).validationFailed();
    }

    @Test
    void crearConFechaFinAnteriorMuestraError() {

        registro.setFechaFin(fecha(2026, Calendar.JANUARY, 5, 9));
        model.setRegistro(registro);

        model.btnCrearhandler(null);

        verify(consultaProcedimientoPasoDAO, never()).crear(any());
        verify(fc).validationFailed();
    }

    @Test
    void crearConDatosValidosGuarda() {

        model.setRegistro(registro);
        model.btnCrearhandler(null);

        verify(consultaProcedimientoPasoDAO).crear(registro);
        assertNull(model.getRegistro());
        verify(fc, never()).validationFailed();
    }

}
