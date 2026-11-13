package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Persona;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PersonaDAOTest {

    @Mock
    private EntityManager em;

    @Mock
    private TypedQuery<Persona> query;

    @InjectMocks
    private PersonaDAO dao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ---------- buscarPorNombres ----------

    @Test
    void buscarPorNombresNulo() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> dao.buscarPorNombres(null)
        );
        assertEquals("Los nombres no pueden ser nulos o vacíos", e.getMessage());
        verifyNoInteractions(em);
    }

    @Test
    void buscarPorNombresVacio() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> dao.buscarPorNombres("")
        );
        assertEquals("Los nombres no pueden ser nulos o vacíos", e.getMessage());
        verifyNoInteractions(em);
    }

    @Test
    void buscarPorNombresBlanco() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> dao.buscarPorNombres("   ")
        );
        assertEquals("Los nombres no pueden ser nulos o vacíos", e.getMessage());
        verifyNoInteractions(em);
    }

    @Test
    void buscarPorNombresCorrectamente() {

        Persona persona = new Persona();

        persona.setNombres("Juan Carlos");

        when(em.createNamedQuery(
                "Persona.findByNombresLike",
                Persona.class
        )).thenReturn(query);

        when(query.getResultList())
                .thenReturn(List.of(persona));

        List<Persona> resultado =
                dao.buscarPorNombres("Juan");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(
                "Juan Carlos",
                resultado.get(0).getNombres()
        );

        verify(em).createNamedQuery(
                "Persona.findByNombresLike",
                Persona.class
        );
        verify(query).setParameter(
                "nombres",
                "%Juan%"
        );
        verify(query).getResultList();
    }

    @Test
    void buscarPorNombresSinResultados() {

        when(em.createNamedQuery(
                "Persona.findByNombresLike",
                Persona.class
        )).thenReturn(query);

        when(query.getResultList())
                .thenReturn(List.of());

        List<Persona> resultado =
                dao.buscarPorNombres("Inexistente");

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(query).setParameter(
                "nombres",
                "%Inexistente%"
        );
    }

    @Test
    void buscarPorNombresErrorEnConsulta() {

        PersistenceException causa =
                new PersistenceException("fallo de BD");

        when(em.createNamedQuery(
                "Persona.findByNombresLike",
                Persona.class
        )).thenReturn(query);

        when(query.getResultList())
                .thenThrow(causa);

        DAOException e = assertThrows(
                DAOException.class,
                () -> dao.buscarPorNombres("Juan")
        );
        assertSame(causa, e.getCause());
    }

    @Test
    void buscarPorNombresSinEntityManager() {

        PersonaDAO daoSinEm = new PersonaDAO();

        IllegalStateException e = assertThrows(
                IllegalStateException.class,
                () -> daoSinEm.buscarPorNombres("Juan")
        );
        assertTrue(e.getMessage().contains(
                "EntityManager no está disponible"));
    }

    // ---------- buscarPorApellidos ----------

    @Test
    void buscarPorApellidosNulo() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> dao.buscarPorApellidos(null)
        );
        assertEquals("Los apellidos no pueden ser nulos o vacíos", e.getMessage());
        verifyNoInteractions(em);
    }

    @Test
    void buscarPorApellidosVacio() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> dao.buscarPorApellidos("")
        );
        assertEquals("Los apellidos no pueden ser nulos o vacíos", e.getMessage());
        verifyNoInteractions(em);
    }

    @Test
    void buscarPorApellidosBlanco() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> dao.buscarPorApellidos("   ")
        );
        assertEquals("Los apellidos no pueden ser nulos o vacíos", e.getMessage());
        verifyNoInteractions(em);
    }

    @Test
    void buscarPorApellidosCorrectamente() {

        Persona persona = new Persona();

        persona.setNombres("Juan Carlos");
        persona.setApellidos("García López");

        when(em.createNamedQuery(
                "Persona.findByApellidosLike",
                Persona.class
        )).thenReturn(query);

        when(query.getResultList())
                .thenReturn(List.of(persona));

        List<Persona> resultado =
                dao.buscarPorApellidos("García");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(
                "García López",
                resultado.get(0).getApellidos()
        );

        verify(em).createNamedQuery(
                "Persona.findByApellidosLike",
                Persona.class
        );
        verify(query).setParameter(
                "apellidos",
                "%García%"
        );
        verify(query).getResultList();
    }

    @Test
    void buscarPorApellidosSinResultados() {

        when(em.createNamedQuery(
                "Persona.findByApellidosLike",
                Persona.class
        )).thenReturn(query);

        when(query.getResultList())
                .thenReturn(List.of());

        List<Persona> resultado =
                dao.buscarPorApellidos("Inexistente");

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(query).setParameter(
                "apellidos",
                "%Inexistente%"
        );
    }

    @Test
    void buscarPorApellidosErrorEnConsulta() {

        PersistenceException causa =
                new PersistenceException("fallo de BD");

        when(em.createNamedQuery(
                "Persona.findByApellidosLike",
                Persona.class
        )).thenReturn(query);

        when(query.getResultList())
                .thenThrow(causa);

        DAOException e = assertThrows(
                DAOException.class,
                () -> dao.buscarPorApellidos("García")
        );
        assertSame(causa, e.getCause());
    }

    @Test
    void buscarPorApellidosSinEntityManager() {

        PersonaDAO daoSinEm = new PersonaDAO();

        IllegalStateException e = assertThrows(
                IllegalStateException.class,
                () -> daoSinEm.buscarPorApellidos("García")
        );
        assertTrue(e.getMessage().contains(
                "EntityManager no está disponible"));
    }

    // ---------- buscarPersonasSinRol ----------

    @Test
    void buscarPersonasSinRolDevuelveLasQueNoTienenRol() {

        Persona persona = new Persona();

        persona.setNombres("Ana");

        when(em.createNamedQuery(
                "Persona.findPersonasSinRol",
                Persona.class
        )).thenReturn(query);

        when(query.getResultList())
                .thenReturn(List.of(persona));

        List<Persona> resultado =
                dao.buscarPersonasSinRol();

        assertEquals(1, resultado.size());
        assertEquals("Ana", resultado.get(0).getNombres());

        verify(em).createNamedQuery(
                "Persona.findPersonasSinRol",
                Persona.class
        );
        verify(query).getResultList();
    }

    @Test
    void buscarPersonasSinRolSinResultados() {

        when(em.createNamedQuery(
                "Persona.findPersonasSinRol",
                Persona.class
        )).thenReturn(query);

        when(query.getResultList())
                .thenReturn(List.of());

        List<Persona> resultado = dao.buscarPersonasSinRol();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void buscarPersonasSinRolErrorEnConsulta() {

        PersistenceException causa =
                new PersistenceException("fallo de BD");

        when(em.createNamedQuery(
                "Persona.findPersonasSinRol",
                Persona.class
        )).thenReturn(query);

        when(query.getResultList())
                .thenThrow(causa);

        DAOException e = assertThrows(
                DAOException.class,
                () -> dao.buscarPersonasSinRol()
        );
        assertSame(causa, e.getCause());
    }

    @Test
    void buscarPersonasSinRolSinEntityManager() {

        PersonaDAO daoSinEm = new PersonaDAO();

        IllegalStateException e = assertThrows(
                IllegalStateException.class,
                () -> daoSinEm.buscarPersonasSinRol()
        );
        assertTrue(e.getMessage().contains(
                "EntityManager no está disponible"));
    }
}
