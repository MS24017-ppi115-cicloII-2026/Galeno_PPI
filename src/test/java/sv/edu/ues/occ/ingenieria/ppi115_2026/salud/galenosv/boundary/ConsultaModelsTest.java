package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import jakarta.faces.application.FacesMessage;
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
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ConsultaDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Clinica;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Consulta;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.PersonaRol;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ConsultaModelsTest {

    @Mock
    private ConsultaDAO consultaDAO;

    @Mock
    private SesionModels sesionModels;

    @Mock
    private FacesContext fc;

    @InjectMocks
    private ConsultaModels model;

    private UUID idClinicaA;
    private UUID idClinicaB;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        idClinicaA = UUID.randomUUID();
        idClinicaB = UUID.randomUUID();

        when(sesionModels.getIdClinicaActual()).thenReturn(null);
    }

    private PersonaRol crearPaciente(UUID idPersonaRol, UUID idClinica) {
        PersonaRol paciente = new PersonaRol(UUID.randomUUID());
        paciente.setIdPersonaRol(idPersonaRol);
        if (idClinica != null) {
            paciente.setIdClinica(new Clinica(idClinica));
        }
        return paciente;
    }

    private Consulta crearConsulta(UUID idPersonaRol, UUID idClinica,
            Date fechaInicio) {
        Consulta consulta = new Consulta(UUID.randomUUID());
        consulta.setIdPersonaRol(crearPaciente(idPersonaRol, idClinica));
        consulta.setFechaInicio(fechaInicio);
        return consulta;
    }

    private Date fecha(int anio, int mes, int dia, int hora) {
        Calendar calendario = Calendar.getInstance();
        calendario.set(anio, mes, dia, hora, 0, 0);
        calendario.set(Calendar.MILLISECOND, 0);
        return calendario.getTime();
    }


    // ---------- getPacientes ----------

    @Test
    void getPacientesSinSesionDevuelveTodos() {

        PersonaRol primero = crearPaciente(UUID.randomUUID(), idClinicaA);
        PersonaRol segundo = crearPaciente(UUID.randomUUID(), idClinicaB);

        when(consultaDAO.buscarPersonasPorNombreRol("paciente"))
                .thenReturn(List.of(primero, segundo));

        assertEquals(2, model.getPacientes().size());
    }

    @Test
    void getPacientesFiltraPorLaClinicaDeLaSesion() {

        PersonaRol primero = crearPaciente(UUID.randomUUID(), idClinicaA);
        PersonaRol segundo = crearPaciente(UUID.randomUUID(), idClinicaB);

        when(consultaDAO.buscarPersonasPorNombreRol("paciente"))
                .thenReturn(List.of(primero, segundo));
        when(sesionModels.getIdClinicaActual()).thenReturn(idClinicaA);

        List<PersonaRol> resultado = model.getPacientes();

        assertEquals(1, resultado.size());
        assertSame(primero, resultado.get(0));
    }

    @Test
    void getPacientesIgnoraLosQueNoTienenClinica() {

        PersonaRol sinClinica = crearPaciente(UUID.randomUUID(), null);
        PersonaRol conClinica = crearPaciente(UUID.randomUUID(), idClinicaA);

        when(consultaDAO.buscarPersonasPorNombreRol("paciente"))
                .thenReturn(List.of(sinClinica, conClinica));
        when(sesionModels.getIdClinicaActual()).thenReturn(idClinicaA);

        List<PersonaRol> resultado = model.getPacientes();

        assertEquals(1, resultado.size());
        assertSame(conClinica, resultado.get(0));
    }

    @Test
    void getregistrosSinBaseDevuelveListaVacia() {

        model.setRegistros(null);

        assertTrue(model.getregistros().isEmpty());
    }

    @Test
    void getregistrosFiltraPorLaClinicaDeLaSesion() {

        Consulta deA = crearConsulta(UUID.randomUUID(), idClinicaA,
                fecha(2026, Calendar.JANUARY, 10, 9));
        Consulta deB = crearConsulta(UUID.randomUUID(), idClinicaB,
                fecha(2026, Calendar.JANUARY, 10, 9));

        model.setRegistros(List.of(deA, deB));
        when(sesionModels.getIdClinicaActual()).thenReturn(idClinicaA);

        List<Consulta> resultado = model.getregistros();

        assertEquals(1, resultado.size());
        assertSame(deA, resultado.get(0));
    }

    @Test
    void getregistrosFiltraPorFechaDesde() {

        Consulta antes = crearConsulta(UUID.randomUUID(), null,
                fecha(2026, Calendar.JANUARY, 5, 9));
        Consulta despues = crearConsulta(UUID.randomUUID(), null,
                fecha(2026, Calendar.JANUARY, 20, 9));

        model.setRegistros(List.of(antes, despues));
        model.setFechaDesde(fecha(2026, Calendar.JANUARY, 10, 0));

        List<Consulta> resultado = model.getregistros();

        assertEquals(1, resultado.size());
        assertSame(despues, resultado.get(0));
    }

    @Test
    void getregistrosFiltraPorFechaHastaIncluyendoTodoElDia() {

        Consulta manana = crearConsulta(UUID.randomUUID(), null,
                fecha(2026, Calendar.JANUARY, 11, 9));
        Consulta hoy = crearConsulta(UUID.randomUUID(), null,
                fecha(2026, Calendar.JANUARY, 10, 18));

        model.setRegistros(List.of(manana, hoy));
        model.setFechaHasta(fecha(2026, Calendar.JANUARY, 10, 0));

        List<Consulta> resultado = model.getregistros();

        assertEquals(1, resultado.size());
        assertSame(hoy, resultado.get(0));
    }

    @Test
    void getregistrosAplicaLosDosFiltrosDeFecha() {

        Consulta dentro = crearConsulta(UUID.randomUUID(), null,
                fecha(2026, Calendar.JANUARY, 15, 9));
        Consulta antes = crearConsulta(UUID.randomUUID(), null,
                fecha(2026, Calendar.JANUARY, 1, 9));
        Consulta despues = crearConsulta(UUID.randomUUID(), null,
                fecha(2026, Calendar.JANUARY, 31, 9));

        model.setRegistros(List.of(dentro, antes, despues));
        model.setFechaDesde(fecha(2026, Calendar.JANUARY, 10, 0));
        model.setFechaHasta(fecha(2026, Calendar.JANUARY, 20, 0));

        assertEquals(1, model.getregistros().size());
    }

    @Test
    void crearConPersonaQueNoEsPacienteMuestraError() {

        PersonaRol noPaciente = crearPaciente(UUID.randomUUID(), null);
        when(consultaDAO.buscarPersonasPorNombreRol("paciente"))
                .thenReturn(List.of());

        Consulta consulta = new Consulta(UUID.randomUUID());
        consulta.setIdPersonaRol(noPaciente);
        model.setRegistro(consulta);

        model.btnCrearhandler(null);

        verify(consultaDAO, never()).crear(any());
        verify(fc).validationFailed();
    }

    @Test
    void crearSinFechaDeInicioMuestraError() {

        PersonaRol paciente = crearPaciente(UUID.randomUUID(), null);
        when(consultaDAO.buscarPersonasPorNombreRol("paciente"))
                .thenReturn(List.of(paciente));

        Consulta consulta = new Consulta(UUID.randomUUID());
        consulta.setIdPersonaRol(paciente);
        model.setRegistro(consulta);

        model.btnCrearhandler(null);

        verify(consultaDAO, never()).crear(any());
        verify(fc).validationFailed();
    }

    @Test
    void crearConFechaFinAnteriorMuestraError() {

        PersonaRol paciente = crearPaciente(UUID.randomUUID(), null);
        when(consultaDAO.buscarPersonasPorNombreRol("paciente"))
                .thenReturn(List.of(paciente));

        Consulta consulta = new Consulta(UUID.randomUUID());
        consulta.setIdPersonaRol(paciente);
        consulta.setFechaInicio(fecha(2026, Calendar.JANUARY, 20, 9));
        consulta.setFechaFin(fecha(2026, Calendar.JANUARY, 10, 9));
        model.setRegistro(consulta);

        model.btnCrearhandler(null);

        verify(consultaDAO, never()).crear(any());
        verify(fc).addMessage(isNull(), any(FacesMessage.class));
        verify(fc).validationFailed();
    }

    @Test
    void crearConDatosValidosGuarda() {

        PersonaRol paciente = crearPaciente(UUID.randomUUID(), null);
        when(consultaDAO.buscarPersonasPorNombreRol("paciente"))
                .thenReturn(List.of(paciente));
        when(consultaDAO.findRange(0, 100)).thenReturn(List.of());

        Consulta consulta = new Consulta(UUID.randomUUID());
        consulta.setIdPersonaRol(paciente);
        consulta.setFechaInicio(fecha(2026, Calendar.JANUARY, 10, 9));
        model.setRegistro(consulta);

        model.btnCrearhandler(null);

        verify(consultaDAO).crear(consulta);
        assertNull(model.getRegistro());
        verify(fc, never()).validationFailed();
    }

    @Test
    void fechaDesdeYHastaPorDefectoSonNulas() {

        assertNull(model.getFechaDesde());
        assertNull(model.getFechaHasta());
    }

    @Test
    void modificarConDatosValidosActualiza() {

        UUID idPersonaRol = UUID.randomUUID();

        PersonaRol paciente = crearPaciente(idPersonaRol, idClinicaA);

        when(consultaDAO.buscarPersonasPorNombreRol("paciente"))
                .thenReturn(List.of(paciente));

        Consulta consulta = crearConsulta(idPersonaRol, idClinicaA,
                fecha(2026, Calendar.MARCH, 15, 9));

        model.setRegistros(List.of(consulta));
        model.btnSeleccionarRegistro(consulta.getIdConsulta());

        model.btnModificarHandler();

        verify(consultaDAO).actualizar(consulta);
        verify(fc, never()).validationFailed();
    }

    @Test
    void modificarConPersonaQueNoEsPacienteMuestraError() {

        UUID idConsultado = UUID.randomUUID();
        UUID idOtro = UUID.randomUUID();

        PersonaRol paciente = crearPaciente(idOtro, idClinicaA);

        when(consultaDAO.buscarPersonasPorNombreRol("paciente"))
                .thenReturn(List.of(paciente));

        Consulta consulta = crearConsulta(idConsultado, idClinicaA,
                fecha(2026, Calendar.MARCH, 15, 9));

        model.setRegistros(List.of(consulta));
        model.btnSeleccionarRegistro(consulta.getIdConsulta());

        model.btnModificarHandler();

        verify(consultaDAO, never()).actualizar(any());
        verify(fc).validationFailed();
    }
}
