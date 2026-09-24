package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import jakarta.persistence.EntityManager;
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

    @Test
    void buscarPorNombresNulo() {
        assertThrows(
                IllegalArgumentException.class,
                () -> dao.buscarPorNombres(null)
        );
    }

    @Test
    void buscarPorNombresVacio() {
        assertThrows(
                IllegalArgumentException.class,
                () -> dao.buscarPorNombres("")
        );
    }

    @Test
    void buscarPorNombresCorrectamente() {

        Persona persona = new Persona();

        persona.setNombres("Juan Carlos");

        when(em.createQuery(
                anyString(),
                eq(Persona.class)
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

        verify(query).setParameter(
                "nombres",
                "%Juan%"
        );

        verify(query).getResultList();
    }

    @Test
    void buscarPorApellidosNulo() {
        assertThrows(
                IllegalArgumentException.class,
                () -> dao.buscarPorApellidos(null)
        );
    }

    @Test
    void buscarPorApellidosVacio() {
        assertThrows(
                IllegalArgumentException.class,
                () -> dao.buscarPorApellidos("")
        );
    }

    @Test
    void buscarPorApellidosCorrectamente() {

        Persona persona = new Persona();

        persona.setNombres("Juan Carlos");
        persona.setApellidos("García López");

        when(em.createQuery(
                anyString(),
                eq(Persona.class)
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

        verify(query).setParameter(
                "apellidos",
                "%García%"
        );

        verify(query).getResultList();
    }
}