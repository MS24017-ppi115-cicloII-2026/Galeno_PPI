package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Rol;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Cubre el CRUD heredado de DefaultDAO usando RolDAO como
 * implementación concreta (no sobreescribe crear/actualizar).
 */
public class DefaultDAOTest {

    @Mock
    private EntityManager em;

    @Mock
    private TypedQuery<Rol> query;

    @Mock
    private CriteriaBuilder cb;

    @Mock
    private CriteriaQuery<Rol> cq;

    @Mock
    private Root<Rol> root;

    @InjectMocks
    private RolDAO dao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ---------- crear ----------

    @Test
    void crearPersisteElRegistro() {

        Rol rol = new Rol();

        dao.crear(rol);

        verify(em).persist(rol);
    }

    @Test
    void crearConErrorDePersistencia() {

        Rol rol = new Rol();

        doThrow(new RuntimeException("boom"))
                .when(em).persist(rol);

        IllegalStateException e = assertThrows(
                IllegalStateException.class,
                () -> dao.crear(rol)
        );

        assertEquals("Error al crear el regsitro", e.getMessage());
        assertInstanceOf(RuntimeException.class, e.getCause());
    }

    // ---------- actualizar ----------

    @Test
    void actualizarHaceMerge() {

        Rol rol = new Rol();

        dao.actualizar(rol);

        verify(em).merge(rol);
    }

    @Test
    void actualizarConErrorDeMerge() {

        Rol rol = new Rol();

        doThrow(new RuntimeException("boom"))
                .when(em).merge(rol);

        IllegalStateException e = assertThrows(
                IllegalStateException.class,
                () -> dao.actualizar(rol)
        );

        assertEquals("Error al actualizar el registro", e.getMessage());
    }

    // ---------- buscar ----------

    @Test
    void buscarDevuelveLaEntidad() {

        UUID id = UUID.randomUUID();
        Rol esperado = new Rol(id);

        when(em.find(Rol.class, id)).thenReturn(esperado);

        assertSame(esperado, dao.buscar(id));

        verify(em).find(Rol.class, id);
    }

    @Test
    void buscarConError() {

        UUID id = UUID.randomUUID();

        when(em.find(Rol.class, id))
                .thenThrow(new PersistenceException("boom"));

        IllegalStateException e = assertThrows(
                IllegalStateException.class,
                () -> dao.buscar(id)
        );

        assertEquals("Error al buscar el registro", e.getMessage());
    }

    // ---------- eliminar ----------

    @Test
    void eliminarBorraYHaceFlush() {

        UUID id = UUID.randomUUID();
        Rol gestionado = new Rol(id);

        when(em.find(Rol.class, id)).thenReturn(gestionado);

        dao.eliminar(id);

        verify(em).remove(gestionado);
        verify(em).flush();
    }

    @Test
    void eliminarCuandoNoExiste() {

        UUID id = UUID.randomUUID();

        when(em.find(Rol.class, id)).thenReturn(null);

        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> dao.eliminar(id)
        );

        assertEquals("No existe un registro con ese id", e.getMessage());

        verify(em, never()).remove(any(Rol.class));
        verify(em, never()).flush();
    }

    @Test
    void eliminarCuandoEstaEnUsoPorOtraTabla() {

        UUID id = UUID.randomUUID();

        when(em.find(Rol.class, id))
                .thenThrow(new PersistenceException("fk violation"));

        DAOException e = assertThrows(
                DAOException.class,
                () -> dao.eliminar(id)
        );

        assertEquals(
                "No se puede eliminar: el registro esta siendo utilizado"
                + " en otra parte del sistema",
                e.getMessage()
        );

        verify(em, never()).flush();
    }

    @Test
    void eliminarConErrorInesperado() {

        UUID id = UUID.randomUUID();
        Rol gestionado = new Rol(id);

        when(em.find(Rol.class, id)).thenReturn(gestionado);

        doThrow(new RuntimeException("boom"))
                .when(em).remove(gestionado);

        DAOException e = assertThrows(
                DAOException.class,
                () -> dao.eliminar(id)
        );

        assertEquals("Error al eliminar el registro", e.getMessage());
    }

    // ---------- findRange ----------

    @Test
    void findRangeDevuelveElRango() {

        Rol rol = new Rol();

        when(em.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery(Rol.class)).thenReturn(cq);
        when(cq.from(Rol.class)).thenReturn(root);
        when(cq.select(root)).thenReturn(cq);
        when(em.createQuery(cq)).thenReturn(query);
        when(query.setFirstResult(0)).thenReturn(query);
        when(query.setMaxResults(10)).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(rol));

        List<Rol> resultado = dao.findRange(0, 10);

        assertEquals(1, resultado.size());

        verify(query).setFirstResult(0);
        verify(query).setMaxResults(10);
    }

    @Test
    void findRangeConFirstNegativo() {

        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> dao.findRange(-1, 10)
        );

        assertEquals(
                "Los parámetros de paginación (first/max) no son válidos",
                e.getMessage()
        );

        verifyNoInteractions(em);
    }

    @Test
    void findRangeConMaxCero() {

        assertThrows(
                IllegalArgumentException.class,
                () -> dao.findRange(0, 0)
        );

        verifyNoInteractions(em);
    }

    @Test
    void findRangeConMaxNegativo() {

        assertThrows(
                IllegalArgumentException.class,
                () -> dao.findRange(0, -5)
        );

        verifyNoInteractions(em);
    }

    @Test
    void findRangeConError() {

        when(em.getCriteriaBuilder())
                .thenThrow(new RuntimeException("boom"));

        IllegalStateException e = assertThrows(
                IllegalStateException.class,
                () -> dao.findRange(0, 10)
        );

        assertEquals(
                "Error al obtener el rango de registros",
                e.getMessage()
        );
    }
}
