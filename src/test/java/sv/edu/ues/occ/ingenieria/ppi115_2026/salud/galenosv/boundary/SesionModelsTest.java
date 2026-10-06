package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ClinicaDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.PersonaRolDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.RolDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Clinica;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Persona;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.PersonaRol;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Rol;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SesionModelsTest {

    @Mock
    private PersonaRolDAO personaRolDAO;

    @Mock
    private RolDAO rolDAO;

    @Mock
    private ClinicaDAO clinicaDAO;

    @InjectMocks
    private SesionModels model;

    private Rol rol;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        rol = new Rol();
        rol.setIdRol(UUID.randomUUID());
        rol.setNombre("DOCTOR");
    }

    private Persona crearPersona(String nombres, String apellidos) {
        Persona persona = new Persona(UUID.randomUUID());
        persona.setNombres(nombres);
        persona.setApellidos(apellidos);
        return persona;
    }

    private PersonaRol crearPersonaRol(String nombres, String apellidos,
            Rol rol, Clinica clinica) {
        PersonaRol personaRol = new PersonaRol(UUID.randomUUID());
        personaRol.setIdPersona(crearPersona(nombres, apellidos));
        personaRol.setIdRol(rol);
        personaRol.setIdClinica(clinica);
        return personaRol;
    }

    private Clinica crearClinica() {
        Clinica clinica = new Clinica(UUID.randomUUID());
        clinica.setNombre("Clinica Norte");
        return clinica;
    }


    @Test
    void getPersonasParaIniciarSesionExcluyeElRolPaciente() {

        Rol paciente = new Rol();
        paciente.setIdRol(UUID.randomUUID());
        paciente.setNombre("paciente");

        when(rolDAO.findRange(0, 1000)).thenReturn(List.of(rol, paciente));
        when(personaRolDAO.buscarPorRol(rol))
                .thenReturn(List.of(crearPersonaRol("Ana", "Lopez", rol, null)));
        when(personaRolDAO.buscarPorRol(paciente))
                .thenReturn(List.of(crearPersonaRol("Luis", "Perez", paciente, null)));

        List<PersonaRol> resultado = model.getPersonasParaIniciarSesion();

        assertEquals(1, resultado.size());
        assertEquals("Ana", resultado.get(0).getIdPersona().getNombres());
        verify(personaRolDAO, never()).buscarPorRol(paciente);
    }

    @Test
    void getPersonasParaIniciarSesionOrdenaPorNombreCompleto() {

        when(rolDAO.findRange(0, 1000)).thenReturn(List.of(rol));
        when(personaRolDAO.buscarPorRol(rol)).thenReturn(List.of(
                crearPersonaRol("Zoe", "Alvarez", rol, null),
                crearPersonaRol("Ana", "Lopez", rol, null),
                crearPersonaRol("Luis", "Perez", rol, null)
        ));

        List<PersonaRol> resultado = model.getPersonasParaIniciarSesion();

        assertEquals("Ana", resultado.get(0).getIdPersona().getNombres());
        assertEquals("Luis", resultado.get(1).getIdPersona().getNombres());
        assertEquals("Zoe", resultado.get(2).getIdPersona().getNombres());
    }

    @Test
    void getPersonasParaIniciarSesionIgnoraRolesSinNombre() {

        Rol sinNombre = new Rol();
        sinNombre.setIdRol(UUID.randomUUID());

        when(rolDAO.findRange(0, 1000)).thenReturn(List.of(sinNombre, rol));

        List<PersonaRol> resultado = model.getPersonasParaIniciarSesion();

        assertTrue(resultado.isEmpty());
        verify(personaRolDAO).buscarPorRol(rol);
        verify(personaRolDAO, never()).buscarPorRol(sinNombre);
    }


    // ---------- iniciarSesion ----------

    @Test
    void iniciarSesionConNuloNoCambiaNada() {

        model.iniciarSesion(null);

        assertFalse(model.isSesionIniciada());
        verify(personaRolDAO, never()).buscarConRelaciones(any());
    }

    @Test
    void iniciarSesionConRegistroInexistenteNoCambiaNada() {

        PersonaRol seleccionado = new PersonaRol(UUID.randomUUID());
        when(personaRolDAO.buscarConRelaciones(seleccionado.getIdPersonaRol()))
                .thenReturn(null);

        model.iniciarSesion(seleccionado);

        assertFalse(model.isSesionIniciada());
    }

    @Test
    void iniciarSesionGuardaLosDatosDeLaSesion() {

        Clinica clinica = crearClinica();
        PersonaRol seleccionado = crearPersonaRol(
                "Ana", "Lopez", rol, clinica);
        UUID id = seleccionado.getIdPersonaRol();

        when(personaRolDAO.buscarConRelaciones(id))
                .thenReturn(seleccionado);

        model.iniciarSesion(seleccionado);

        assertTrue(model.isSesionIniciada());
        assertEquals(id, model.getIdPersonaRolActual());
        assertEquals(clinica.getIdClinica(), model.getIdClinicaActual());
        assertEquals("Ana Lopez", model.getNombrePersona());
        assertEquals("DOCTOR", model.getNombreRol());
        assertEquals("Clinica Norte", model.getNombreClinica());
    }

    @Test
    void iniciarSesionSinClinicaDejaLaClinicaActualNula() {

        PersonaRol seleccionado = crearPersonaRol(
                "Ana", "Lopez", rol, null);

        when(personaRolDAO.buscarConRelaciones(seleccionado.getIdPersonaRol()))
                .thenReturn(seleccionado);

        model.iniciarSesion(seleccionado);

        assertTrue(model.isSesionIniciada());
        assertNull(model.getIdClinicaActual());
        assertNull(model.getNombreClinica());
    }

    @Test
    void iniciarSesionSinRolDejaElNombreDelRolNulo() {

        PersonaRol seleccionado = crearPersonaRol(
                "Ana", "Lopez", null, crearClinica());

        when(personaRolDAO.buscarConRelaciones(seleccionado.getIdPersonaRol()))
                .thenReturn(seleccionado);

        model.iniciarSesion(seleccionado);

        assertNull(model.getNombreRol());
        assertNotNull(model.getIdClinicaActual());
    }


    // ---------- cerrarSesion ----------

    @Test
    void cerrarSesionLimpiaTodo() {

        PersonaRol seleccionado = crearPersonaRol(
                "Ana", "Lopez", rol, crearClinica());
        when(personaRolDAO.buscarConRelaciones(seleccionado.getIdPersonaRol()))
                .thenReturn(seleccionado);
        model.iniciarSesion(seleccionado);

        model.setClinicaSeleccionada(crearClinica());
        model.setPersonaRolSeleccionado(seleccionado);

        model.cerrarSesion();

        assertFalse(model.isSesionIniciada());
        assertNull(model.getIdPersonaRolActual());
        assertNull(model.getIdClinicaActual());
        assertNull(model.getNombrePersona());
        assertNull(model.getNombreRol());
        assertNull(model.getNombreClinica());
        assertNull(model.getClinicaSeleccionada());
        assertNull(model.getPersonaRolSeleccionado());
    }


    // ---------- Cambiar de clinica ----------

    @Test
    void getClinicasDevuelveSoloLasActivas() {

        Clinica clinica = crearClinica();
        when(clinicaDAO.buscarPorActivo(Boolean.TRUE))
                .thenReturn(List.of(clinica));

        assertEquals(1, model.getClinicas().size());
        verify(clinicaDAO).buscarPorActivo(Boolean.TRUE);
    }

    @Test
    void setClinicaSeleccionadaLimpiaLaPersonaElegida() {

        PersonaRol seleccionado = crearPersonaRol(
                "Ana", "Lopez", rol, null);
        model.setPersonaRolSeleccionado(seleccionado);

        model.setClinicaSeleccionada(crearClinica());

        assertNull(model.getPersonaRolSeleccionado());
    }

    @Test
    void getPersonasRolesPorClinicaExcluyePacientes() {

        Clinica clinica = crearClinica();

        Rol paciente = new Rol();
        paciente.setIdRol(UUID.randomUUID());
        paciente.setNombre("Paciente");

        when(clinicaDAO.buscar(clinica.getIdClinica()))
                .thenReturn(clinica);
        when(personaRolDAO.buscarPorClinica(clinica)).thenReturn(List.of(
                crearPersonaRol("Ana", "Lopez", rol, clinica),
                crearPersonaRol("Luis", "Perez", paciente, clinica),
                crearPersonaRol("Sin", "Rol", null, clinica)
        ));

        model.setClinicaSeleccionada(clinica);

        List<PersonaRol> resultado = model.getPersonasRolesPorClinica();

        assertEquals(1, resultado.size());
        assertEquals("Ana", resultado.get(0).getIdPersona().getNombres());
    }

    @Test
    void getPersonasRolesPorClinicaOrdenaPorNombreCompleto() {

        Clinica clinica = crearClinica();

        when(clinicaDAO.buscar(clinica.getIdClinica()))
                .thenReturn(clinica);
        when(personaRolDAO.buscarPorClinica(clinica)).thenReturn(List.of(
                crearPersonaRol("Zoe", "Alvarez", rol, clinica),
                crearPersonaRol("Ana", "Lopez", rol, clinica)
        ));

        model.setClinicaSeleccionada(clinica);

        List<PersonaRol> resultado = model.getPersonasRolesPorClinica();

        assertEquals("Ana", resultado.get(0).getIdPersona().getNombres());
        assertEquals("Zoe", resultado.get(1).getIdPersona().getNombres());
    }


    // ---------- aplicarRol ----------

    @Test
    void aplicarRolIniciaLaSesionConLaPersonaElegida() {

        Clinica clinica = crearClinica();
        PersonaRol seleccionado = crearPersonaRol(
                "Ana", "Lopez", rol, clinica);

        when(personaRolDAO.buscarConRelaciones(seleccionado.getIdPersonaRol()))
                .thenReturn(seleccionado);

        model.setPersonaRolSeleccionado(seleccionado);
        model.aplicarRol();

        assertTrue(model.isSesionIniciada());
        assertEquals(clinica.getIdClinica(), model.getIdClinicaActual());
    }

    @Test
    void aplicarRolSinPersonaElegidaNoIniciaSesion() {

        model.aplicarRol();

        assertFalse(model.isSesionIniciada());
    }
}
