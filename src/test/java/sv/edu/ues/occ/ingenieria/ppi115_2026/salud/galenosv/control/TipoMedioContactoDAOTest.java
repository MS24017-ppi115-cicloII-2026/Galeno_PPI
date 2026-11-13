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
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.TipoMedioContacto;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TipoMedioContactoDAOTest {

    @Mock
    private EntityManager em;

    @Mock
    private TypedQuery<TipoMedioContacto> query;

    @InjectMocks
    private TipoMedioContactoDAO dao;

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

        TipoMedioContacto tipoMedioContacto =
                new TipoMedioContacto();

        tipoMedioContacto.setNombre("Correo electrónico");

        when(em.createNamedQuery(
                "TipoMedioContacto.findByNombreLike",
                TipoMedioContacto.class
        )).thenReturn(query);

        when(query.getResultList())
                .thenReturn(List.of(tipoMedioContacto));

        List<TipoMedioContacto> resultado =
                dao.buscarPorNombre("Correo");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(
                "Correo electrónico",
                resultado.get(0).getNombre()
        );

        verify(em).createNamedQuery(
                "TipoMedioContacto.findByNombreLike",
                TipoMedioContacto.class
        );
        verify(query).setParameter(
                "nombre",
                "%Correo%"
        );
        verify(query).getResultList();
    }

    @Test
    void buscarPorNombreSinResultados() {

        when(em.createNamedQuery(
                "TipoMedioContacto.findByNombreLike",
                TipoMedioContacto.class
        )).thenReturn(query);

        when(query.getResultList())
                .thenReturn(List.of());

        List<TipoMedioContacto> resultado =
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
                "TipoMedioContacto.findByNombreLike",
                TipoMedioContacto.class
        )).thenReturn(query);

        when(query.getResultList())
                .thenThrow(causa);

        DAOException e = assertThrows(
                DAOException.class,
                () -> dao.buscarPorNombre("Correo")
        );
        assertSame(causa, e.getCause());
    }

    @Test
    void buscarPorNombreSinEntityManager() {

        TipoMedioContactoDAO daoSinEm = new TipoMedioContactoDAO();

        IllegalStateException e = assertThrows(
                IllegalStateException.class,
                () -> daoSinEm.buscarPorNombre("Correo")
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

        TipoMedioContacto tipoMedioContacto =
                new TipoMedioContacto();

        tipoMedioContacto.setNombre("Correo electrónico");
        tipoMedioContacto.setActivo(true);

        when(em.createNamedQuery(
                "TipoMedioContacto.findByActivo",
                TipoMedioContacto.class
        )).thenReturn(query);

        when(query.getResultList())
                .thenReturn(List.of(tipoMedioContacto));

        List<TipoMedioContacto> resultado =
                dao.buscarPorActivo(true);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(
                "Correo electrónico",
                resultado.get(0).getNombre()
        );
        assertTrue(resultado.get(0).getActivo());

        verify(em).createNamedQuery(
                "TipoMedioContacto.findByActivo",
                TipoMedioContacto.class
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
                "TipoMedioContacto.findByActivo",
                TipoMedioContacto.class
        )).thenReturn(query);

        when(query.getResultList())
                .thenReturn(List.of());

        List<TipoMedioContacto> resultado =
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
                "TipoMedioContacto.findByActivo",
                TipoMedioContacto.class
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

        TipoMedioContactoDAO daoSinEm = new TipoMedioContactoDAO();

        IllegalStateException e = assertThrows(
                IllegalStateException.class,
                () -> daoSinEm.buscarPorActivo(true)
        );
        assertTrue(e.getMessage().contains(
                "EntityManager no está disponible"));
    }
}
