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

    // ---------- buscarPorNombre ----------

    @Test
    void buscarPorNombreNulo() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> dao.buscarPorNombre(null)
        );
        assertEquals("El nombre no puede ser nulo o vacío", e.getMessage());
        verifyNoInteractions(em);
    }

    @Test
    void buscarPorNombreVacio() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> dao.buscarPorNombre("")
        );
        assertEquals("El nombre no puede ser nulo o vacío", e.getMessage());
        verifyNoInteractions(em);
    }

    @Test
    void buscarPorNombreBlanco() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> dao.buscarPorNombre("   ")
        );
        assertEquals("El nombre no puede ser nulo o vacío", e.getMessage());
        verifyNoInteractions(em);
    }

    @Test
    void buscarPorNombreCorrectamente() {

        Clinica clinica = new Clinica();
        clinica.setNombre("Clínica Central");

        when(em.createNamedQuery(
                "Clinica.findByNombreLike",
                Clinica.class
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

        verify(em).createNamedQuery(
                "Clinica.findByNombreLike",
                Clinica.class
        );
        verify(query).setParameter(
                "nombre",
                "%Central%"
        );
        verify(query).getResultList();
    }

    @Test
    void buscarPorNombreSinResultados() {

        when(em.createNamedQuery(
                "Clinica.findByNombreLike",
                Clinica.class
        )).thenReturn(query);

        when(query.getResultList())
                .thenReturn(List.of());

        List<Clinica> resultado =
                dao.buscarPorNombre("Inexistente");

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(query).setParameter(
                "nombre",
                "%Inexistente%"
        );
    }

    @Test
    void buscarPorNombreErrorEnConsulta() {

        PersistenceException causa =
                new PersistenceException("fallo de BD");

        when(em.createNamedQuery(
                "Clinica.findByNombreLike",
                Clinica.class
        )).thenReturn(query);

        when(query.getResultList())
                .thenThrow(causa);

        DAOException e = assertThrows(
                DAOException.class,
                () -> dao.buscarPorNombre("Central")
        );
        assertSame(causa, e.getCause());
    }

    @Test
    void buscarPorNombreSinEntityManager() {

        ClinicaDAO daoSinEm = new ClinicaDAO();

        IllegalStateException e = assertThrows(
                IllegalStateException.class,
                () -> daoSinEm.buscarPorNombre("Central")
        );
        assertTrue(e.getMessage().contains(
                "EntityManager no está disponible"));
    }

    // ---------- buscarPorActivo ----------

    @Test
    void buscarPorActivoNulo() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> dao.buscarPorActivo(null)
        );
        assertEquals("El estado activo no puede ser nulo", e.getMessage());
        verifyNoInteractions(em);
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

        verify(em).createNamedQuery(
                "Clinica.findByActivo",
                Clinica.class
        );
        verify(query).setParameter(
                "activo",
                true
        );
        verify(query).getResultList();
    }

    @Test
    void buscarPorActivoSinResultados() {

        when(em.createNamedQuery(
                "Clinica.findByActivo",
                Clinica.class
        )).thenReturn(query);

        when(query.getResultList())
                .thenReturn(List.of());

        List<Clinica> resultado =
                dao.buscarPorActivo(false);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(query).setParameter(
                "activo",
                false
        );
    }

    @Test
    void buscarPorActivoErrorEnConsulta() {

        PersistenceException causa =
                new PersistenceException("fallo de BD");

        when(em.createNamedQuery(
                "Clinica.findByActivo",
                Clinica.class
        )).thenReturn(query);

        when(query.getResultList())
                .thenThrow(causa);

        DAOException e = assertThrows(
                DAOException.class,
                () -> dao.buscarPorActivo(true)
        );
        assertSame(causa, e.getCause());
    }

    @Test
    void buscarPorActivoSinEntityManager() {

        ClinicaDAO daoSinEm = new ClinicaDAO();

        IllegalStateException e = assertThrows(
                IllegalStateException.class,
                () -> daoSinEm.buscarPorActivo(true)
        );
        assertTrue(e.getMessage().contains(
                "EntityManager no está disponible"));
    }

    // ---------- buscarPorTipo ----------

    @Test
    void buscarPorTipoNulo() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> dao.buscarPorTipo(null)
        );
        assertEquals("El tipo no puede ser nulo o vacío", e.getMessage());
        verifyNoInteractions(em);
    }

    @Test
    void buscarPorTipoVacio() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> dao.buscarPorTipo("")
        );
        assertEquals("El tipo no puede ser nulo o vacío", e.getMessage());
        verifyNoInteractions(em);
    }

    @Test
    void buscarPorTipoBlanco() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> dao.buscarPorTipo("   ")
        );
        assertEquals("El tipo no puede ser nulo o vacío", e.getMessage());
        verifyNoInteractions(em);
    }

    @Test
    void buscarPorTipoCorrectamente() {

        Clinica clinica = new Clinica();
        clinica.setNombre("Hospital Central");
        clinica.setTipo("Hospital");

        when(em.createNamedQuery(
                "Clinica.findByTipoLike",
                Clinica.class
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

        verify(em).createNamedQuery(
                "Clinica.findByTipoLike",
                Clinica.class
        );
        verify(query).setParameter(
                "tipo",
                "%Hospital%"
        );
        verify(query).getResultList();
    }

    @Test
    void buscarPorTipoSinResultados() {

        when(em.createNamedQuery(
                "Clinica.findByTipoLike",
                Clinica.class
        )).thenReturn(query);

        when(query.getResultList())
                .thenReturn(List.of());

        List<Clinica> resultado =
                dao.buscarPorTipo("Laboratorio");

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(query).setParameter(
                "tipo",
                "%Laboratorio%"
        );
    }

    @Test
    void buscarPorTipoErrorEnConsulta() {

        PersistenceException causa =
                new PersistenceException("fallo de BD");

        when(em.createNamedQuery(
                "Clinica.findByTipoLike",
                Clinica.class
        )).thenReturn(query);

        when(query.getResultList())
                .thenThrow(causa);

        DAOException e = assertThrows(
                DAOException.class,
                () -> dao.buscarPorTipo("Hospital")
        );
        assertSame(causa, e.getCause());
    }

    @Test
    void buscarPorTipoSinEntityManager() {

        ClinicaDAO daoSinEm = new ClinicaDAO();

        IllegalStateException e = assertThrows(
                IllegalStateException.class,
                () -> daoSinEm.buscarPorTipo("Hospital")
        );
        assertTrue(e.getMessage().contains(
                "EntityManager no está disponible"));
    }
}
