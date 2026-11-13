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
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Rol;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RolDAOTest {

    @Mock
    private EntityManager em;

    @Mock
    private TypedQuery<Rol> query;

    @InjectMocks
    private RolDAO dao;

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

        Rol rol = new Rol();

        rol.setNombre("Administrador");

        when(em.createNamedQuery(
                "Rol.findByNombreLike",
                Rol.class
        )).thenReturn(query);

        when(query.getResultList())
                .thenReturn(List.of(rol));

        List<Rol> resultado =
                dao.buscarPorNombre("Admin");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(
                "Administrador",
                resultado.get(0).getNombre()
        );

        verify(em).createNamedQuery(
                "Rol.findByNombreLike",
                Rol.class
        );
        verify(query).setParameter(
                "nombre",
                "%Admin%"
        );
        verify(query).getResultList();
    }

    @Test
    void buscarPorNombreSinResultados() {

        when(em.createNamedQuery(
                "Rol.findByNombreLike",
                Rol.class
        )).thenReturn(query);

        when(query.getResultList())
                .thenReturn(List.of());

        List<Rol> resultado =
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
                "Rol.findByNombreLike",
                Rol.class
        )).thenReturn(query);

        when(query.getResultList())
                .thenThrow(causa);

        DAOException e = assertThrows(
                DAOException.class,
                () -> dao.buscarPorNombre("Admin")
        );
        assertSame(causa, e.getCause());
    }

    @Test
    void buscarPorNombreSinEntityManager() {

        RolDAO daoSinEm = new RolDAO();

        IllegalStateException e = assertThrows(
                IllegalStateException.class,
                () -> daoSinEm.buscarPorNombre("Admin")
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

        Rol rol = new Rol();

        rol.setNombre("Administrador");
        rol.setActivo(true);

        when(em.createNamedQuery(
                "Rol.findByActivo",
                Rol.class
        )).thenReturn(query);

        when(query.getResultList())
                .thenReturn(List.of(rol));

        List<Rol> resultado =
                dao.buscarPorActivo(true);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(
                "Administrador",
                resultado.get(0).getNombre()
        );
        assertTrue(resultado.get(0).getActivo());

        verify(em).createNamedQuery(
                "Rol.findByActivo",
                Rol.class
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
                "Rol.findByActivo",
                Rol.class
        )).thenReturn(query);

        when(query.getResultList())
                .thenReturn(List.of());

        List<Rol> resultado =
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
                "Rol.findByActivo",
                Rol.class
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

        RolDAO daoSinEm = new RolDAO();

        IllegalStateException e = assertThrows(
                IllegalStateException.class,
                () -> daoSinEm.buscarPorActivo(true)
        );
        assertTrue(e.getMessage().contains(
                "EntityManager no está disponible"));
    }

    // ---------- buscarRolesActivos ----------

    @Test
    void buscarRolesActivos() {

        Rol activo = new Rol();

        activo.setNombre("Administrador");
        activo.setActivo(true);

        when(em.createNamedQuery(
                "Rol.findActivosOrdenadosPorNombre",
                Rol.class
        )).thenReturn(query);

        when(query.getResultList())
                .thenReturn(List.of(activo));

        List<Rol> resultado =
                dao.buscarRolesActivos();

        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).getActivo());

        verify(em).createNamedQuery(
                "Rol.findActivosOrdenadosPorNombre",
                Rol.class
        );
        verify(query).getResultList();
    }

    @Test
    void buscarRolesActivosSinResultados() {

        when(em.createNamedQuery(
                "Rol.findActivosOrdenadosPorNombre",
                Rol.class
        )).thenReturn(query);

        when(query.getResultList())
                .thenReturn(List.of());

        List<Rol> resultado = dao.buscarRolesActivos();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void buscarRolesActivosErrorEnConsulta() {

        PersistenceException causa =
                new PersistenceException("fallo de BD");

        when(em.createNamedQuery(
                "Rol.findActivosOrdenadosPorNombre",
                Rol.class
        )).thenReturn(query);

        when(query.getResultList())
                .thenThrow(causa);

        DAOException e = assertThrows(
                DAOException.class,
                () -> dao.buscarRolesActivos()
        );
        assertSame(causa, e.getCause());
    }

    @Test
    void buscarRolesActivosSinEntityManager() {

        RolDAO daoSinEm = new RolDAO();

        IllegalStateException e = assertThrows(
                IllegalStateException.class,
                () -> daoSinEm.buscarRolesActivos()
        );
        assertTrue(e.getMessage().contains(
                "EntityManager no está disponible"));
    }
}
