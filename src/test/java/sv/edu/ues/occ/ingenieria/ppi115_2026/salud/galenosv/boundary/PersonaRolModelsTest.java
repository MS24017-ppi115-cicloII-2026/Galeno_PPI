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
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ClinicaDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.PersonaRolDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.RolDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Clinica;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Persona;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.PersonaRol;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Rol;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PersonaRolModelsTest {

    @Mock
    private PersonaRolDAO personaRolDAO;

    @Mock
    private RolDAO rolDAO;

    @Mock
    private ClinicaDAO clinicaDAO;

    @Mock
    private PersonaModels personaModel;

    @Mock
    private FacesContext fc;

    @InjectMocks
    private PersonaRolModels model;

    private Persona persona;
    private Rol rol;
    private Clinica clinica;
    private PersonaRol personaRol;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        persona = new Persona(UUID.randomUUID());
        persona.setNombres("Ana");
        persona.setApellidos("Lopez");

        rol = new Rol();
        rol.setIdRol(UUID.randomUUID());
        rol.setNombre("DOCTOR");
        rol.setActivo(Boolean.TRUE);

        clinica = new Clinica(UUID.randomUUID());
        clinica.setNombre("Clinica Norte");
        clinica.setActivo(Boolean.TRUE);

        personaRol = new PersonaRol(UUID.randomUUID());
        personaRol.setIdPersona(persona);
        personaRol.setIdRol(rol);
        personaRol.setIdClinica(clinica);

        when(personaModel.getPersonaSeleccionada()).thenReturn(persona);
    }


    // ---------- Listas ----------

    @Test
    void getRegistrosDePersonaUsaBusquedaPorPersona() {

        when(personaRolDAO.buscarPorPersona(persona))
                .thenReturn(List.of(personaRol));

        assertEquals(1, model.getRegistrosDePersona().size());
        verify(personaRolDAO).buscarPorPersona(persona);
    }

    @Test
    void getRegistrosDePersonaNoRecargaSiLaPersonaNoCambio() {

        when(personaRolDAO.buscarPorPersona(persona))
                .thenReturn(List.of(personaRol));

        model.getRegistrosDePersona();
        model.getRegistrosDePersona();

        verify(personaRolDAO, times(1)).buscarPorPersona(persona);
    }

    @Test
    void getRolesActivos() {

        when(rolDAO.buscarRolesActivos()).thenReturn(List.of(rol));

        assertEquals(1, model.getRolesActivos().size());
        verify(rolDAO).buscarRolesActivos();
    }

    @Test
    void getClinicasActivas() {

        when(clinicaDAO.buscarPorActivo(true))
                .thenReturn(List.of(clinica));

        assertEquals(1, model.getClinicasActivas().size());
        verify(clinicaDAO).buscarPorActivo(true);
    }


    @Test
    void crearSinPersonaMuestraError() {

        personaRol.setIdPersona(null);
        model.setRegistro(personaRol);

        model.btnCrearhandler(null);

        verify(personaRolDAO, never()).crear(any());
        verify(fc).validationFailed();
    }

    @Test
    void crearSinRolMuestraError() {

        personaRol.setIdRol(null);
        model.setRegistro(personaRol);

        model.btnCrearhandler(null);

        verify(personaRolDAO, never()).crear(any());
        verify(fc).validationFailed();
    }

    @Test
    void crearSinClinicaMuestraError() {

        personaRol.setIdClinica(null);
        model.setRegistro(personaRol);

        model.btnCrearhandler(null);

        verify(personaRolDAO, never()).crear(any());
        verify(fc).validationFailed();
    }

    @Test
    void crearConRolInexistenteMuestraError() {

        when(rolDAO.buscar(rol.getIdRol())).thenReturn(null);
        model.setRegistro(personaRol);

        model.btnCrearhandler(null);

        verify(personaRolDAO, never()).crear(any());
        verify(fc).validationFailed();
    }

    @Test
    void crearConRolInactivoMuestraError() {

        rol.setActivo(Boolean.FALSE);
        when(rolDAO.buscar(rol.getIdRol())).thenReturn(rol);
        model.setRegistro(personaRol);

        model.btnCrearhandler(null);

        verify(personaRolDAO, never()).crear(any());
        verify(fc).validationFailed();
    }

    @Test
    void crearConClinicaInexistenteMuestraError() {

        when(rolDAO.buscar(rol.getIdRol())).thenReturn(rol);
        when(clinicaDAO.buscar(clinica.getIdClinica())).thenReturn(null);
        model.setRegistro(personaRol);

        model.btnCrearhandler(null);

        verify(personaRolDAO, never()).crear(any());
        verify(fc).validationFailed();
    }

    @Test
    void crearConClinicaInactivaMuestraError() {

        clinica.setActivo(Boolean.FALSE);
        when(rolDAO.buscar(rol.getIdRol())).thenReturn(rol);
        when(clinicaDAO.buscar(clinica.getIdClinica())).thenReturn(clinica);
        model.setRegistro(personaRol);

        model.btnCrearhandler(null);

        verify(personaRolDAO, never()).crear(any());
        verify(fc).validationFailed();
    }

    @Test
    void crearConRolDuplicadoEnLaMismaClinicaMuestraError() {

        when(rolDAO.buscar(rol.getIdRol())).thenReturn(rol);
        when(clinicaDAO.buscar(clinica.getIdClinica())).thenReturn(clinica);

        PersonaRol previo = new PersonaRol(UUID.randomUUID());
        previo.setIdPersona(persona);
        previo.setIdRol(rol);
        previo.setIdClinica(clinica);

        when(personaRolDAO.buscarPorPersona(persona))
                .thenReturn(List.of(previo));

        model.setRegistro(personaRol);

        model.btnCrearhandler(null);

        verify(personaRolDAO, never()).crear(any());
        verify(fc).validationFailed();
    }

    @Test
    void crearIgnoraRolesDeOtraClinica() {

        when(rolDAO.buscar(rol.getIdRol())).thenReturn(rol);
        when(clinicaDAO.buscar(clinica.getIdClinica())).thenReturn(clinica);

        PersonaRol previo = new PersonaRol(UUID.randomUUID());
        previo.setIdPersona(persona);
        previo.setIdRol(rol);
        previo.setIdClinica(new Clinica(UUID.randomUUID()));

        when(personaRolDAO.buscarPorPersona(persona))
                .thenReturn(List.of(previo));
        when(personaRolDAO.findRange(0, 100)).thenReturn(List.of());

        model.setRegistro(personaRol);

        model.btnCrearhandler(null);

        verify(personaRolDAO).crear(personaRol);
    }

    @Test
    void crearConTodosLosDatosValidosGuarda() {

        when(rolDAO.buscar(rol.getIdRol())).thenReturn(rol);
        when(clinicaDAO.buscar(clinica.getIdClinica())).thenReturn(clinica);
        when(personaRolDAO.buscarPorPersona(persona))
                .thenReturn(List.of());
        when(personaRolDAO.findRange(0, 100)).thenReturn(List.of());

        model.setRegistro(personaRol);

        model.btnCrearhandler(null);

        verify(personaRolDAO).crear(personaRol);
        assertNull(model.getRegistro());
        assertEquals(Estado_CRUD.NINGUNO, model.getEstado());
        assertNotNull(personaRol.getFechaCreacion());
    }


    // ---------- Modificar y eliminar ----------

    @Test
    void modificarConDatosValidosActualiza() {

        when(rolDAO.buscar(rol.getIdRol())).thenReturn(rol);
        when(clinicaDAO.buscar(clinica.getIdClinica())).thenReturn(clinica);
        when(personaRolDAO.buscarPorPersona(persona))
                .thenReturn(List.of(personaRol));
        when(personaRolDAO.findRange(0, 100)).thenReturn(List.of());

        model.setRegistro(personaRol);

        model.btnModificarHandler();

        verify(personaRolDAO).actualizar(personaRol);
        assertNull(model.getRegistro());
    }

    @Test
    void modificarConRolInactivoNoActualiza() {

        rol.setActivo(Boolean.FALSE);
        when(rolDAO.buscar(rol.getIdRol())).thenReturn(rol);

        model.setRegistro(personaRol);
        model.btnModificarHandler();

        verify(personaRolDAO, never()).actualizar(any());
        verify(fc).validationFailed();
    }

    @Test
    void eliminarRecargaLaListaDeLaPersona() {

        when(personaRolDAO.buscarPorPersona(persona))
                .thenReturn(List.of(personaRol));

        model.setRegistros(List.of(personaRol));

        model.btnEliminarHandler(personaRol.getIdPersonaRol());

        verify(personaRolDAO).eliminar(personaRol.getIdPersonaRol());
        verify(personaRolDAO).buscarPorPersona(persona);
    }
}
