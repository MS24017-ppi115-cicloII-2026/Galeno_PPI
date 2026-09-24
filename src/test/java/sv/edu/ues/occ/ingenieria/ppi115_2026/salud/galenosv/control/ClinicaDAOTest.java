package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Clinica;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ClinicaDAOTest {

    @Mock
    private EntityManager em;

    @Mock
    private TypedQuery<Clinica> query;

    @InjectMocks
    private ClinicaDAO dao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void buscarPorNombreNulo() {

        assertThrows(
                IllegalArgumentException.class,
                () -> dao.buscarPorNombre(null)
        );
    }

    @Test
    void buscarPorNombreVacio() {

        assertThrows(
                IllegalArgumentException.class,
                () -> dao.buscarPorNombre("")
        );
    }

    @Test
    void buscarPorNombreCorrectamente() {

        Clinica clinica = new Clinica();
        clinica.setNombre("Clínica Central");

        when(em.createQuery(
                anyString(),
                eq(Clinica.class)
        )).thenReturn(query);

        when(query.getResultList())
                .thenReturn(List.of(clinica));

        List<Clinica> resultado =
                dao.buscarPorNombre("Central");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(
                "Clínica Central",
                resultado.get(0).getNombre()
        );

        verify(query).setParameter(
                "nombre",
                "%Central%"
        );

        verify(query).getResultList();
    }

    @Test
    void buscarPorActivoNulo() {

        assertThrows(
                IllegalArgumentException.class,
                () -> dao.buscarPorActivo(null)
        );
    }

    @Test
    void buscarPorActivoCorrectamente() {

        Clinica clinica = new Clinica();
        clinica.setNombre("Clínica Central");
        clinica.setActivo(true);

        when(em.createNamedQuery(
                "Clinica.findByActivo",
                Clinica.class
        )).thenReturn(query);

        when(query.getResultList())
                .thenReturn(List.of(clinica));

        List<Clinica> resultado =
                dao.buscarPorActivo(true);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).getActivo());

        verify(query).setParameter(
                "activo",
                true
        );

        verify(query).getResultList();
    }

    @Test
    void buscarPorTipoNulo() {

        assertThrows(
                IllegalArgumentException.class,
                () -> dao.buscarPorTipo(null)
        );
    }

    @Test
    void buscarPorTipoVacio() {

        assertThrows(
                IllegalArgumentException.class,
                () -> dao.buscarPorTipo("")
        );
    }

    @Test
    void buscarPorTipoCorrectamente() {

        Clinica clinica = new Clinica();
        clinica.setNombre("Hospital Central");
        clinica.setTipo("Hospital");

        when(em.createQuery(
                anyString(),
                eq(Clinica.class)
        )).thenReturn(query);

        when(query.getResultList())
                .thenReturn(List.of(clinica));

        List<Clinica> resultado =
                dao.buscarPorTipo("Hospital");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(
                "Hospital",
                resultado.get(0).getTipo()
        );

        verify(query).setParameter(
                "tipo",
                "%Hospital%"
        );

        verify(query).getResultList();
    }
}