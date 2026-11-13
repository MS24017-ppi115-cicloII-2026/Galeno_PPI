package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Rol;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Cubre el CRUD heredado de DefaultDAO usando RolDAO como
 * implementación concreta (no sobreescribe crear/actualizar),
 * además de los helpers comunes agregados a DefaultDAO.
 */
public class DefaultDAOTest {

    @Mock
    private EntityManager em;

    @Mock
    private TypedQuery<Rol> query;

    @Mock
    private jakarta.persistence.criteria.CriteriaBuilder cb;

    @Mock
    private jakarta.persistence.criteria.CriteriaQuery<Rol> cq;

    @Mock
    private jakarta.persistence.criteria.Root<Rol> root;

    @InjectMocks
    private RolDAO dao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Implementación mínima para probar los helpers protegidos
     * de DefaultDAO directamente.
     */
    static class TestDAO extends DefaultDAO<Rol> {

        EntityManager em;

        @Override
        public EntityManager getEntityManger() {
            return em;
        }

        List<Rol> lista(String nombreConsulta) {
            return buscarConNamedQuery(nombreConsulta, Rol.class,
                    Map.of("id", UUID.randomUUID()));
        }

        Rol unico(String nombreConsulta) {
            return buscarUnicoConNamedQuery(nombreConsulta, Rol.class,
                    Map.of("id", UUID.randomUUID()));
        }

        void texto(String valor, String mensaje) {
            validarTexto(valor, mensaje);
        }

        void noNulo(Object valor, String mensaje) {
            validarNoNulo(valor, mensaje);
        }

        void rangoFechas(Date desde, Date hasta) {
            validarRangoFechas(desde, hasta);
        }

        void paginacion(int first, int max) {
            validarPaginacion(first, max);
        }
    }

    // ---------- helpers: requireEntityManager ----------

    @Test
    void requireEntityManagerConEntityManagerNulo() {

        TestDAO testDao = new TestDAO();
        testDao.em = null;

        IllegalStateException e = assertThrows(
                IllegalStateException.class,
                () -> testDao.buscar(UUID.randomUUID())
        );

        assertTrue(e.getMessage().contains("EntityManager no está disponible"));
    }

    // ---------- helpers: validarNoNulo / validarTexto ----------

    @Test
    void validarNoNuloRechazaNulos() {

        TestDAO testDao = new TestDAO();

        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> testDao.noNulo(null, "El valor no puede ser nulo")
        );

        assertEquals("El valor no puede ser nulo", e.getMessage());
    }

    @Test
    void validarTextoRechazaNuloYBlanco() {

        TestDAO testDao = new TestDAO();

        assertEquals("El nombre no puede ser nulo o vacío",
                assertThrows(IllegalArgumentException.class,
                        () -> testDao.texto(null, "El nombre no puede ser nulo o vacío"))
                        .getMessage());

        assertEquals("El nombre no puede ser nulo o vacío",
                assertThrows(IllegalArgumentException.class,
                        () -> testDao.texto("   ", "El nombre no puede ser nulo o vacío"))
                        .getMessage());

        // un texto válido no lanza nada
        assertDoesNotThrow(() -> testDao.texto("algo", "El nombre no puede ser nulo o vacío"));
    }

    // ---------- helpers: validarRangoFechas ----------

    @Test
    void validarRangoFechasConFechaNula() {

        TestDAO testDao = new TestDAO();

        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> testDao.rangoFechas(null, new Date())
        );

        assertEquals("Las fechas del rango no pueden ser nulas", e.getMessage());
    }

    @Test
    void validarRangoFechasInvertido() {

        TestDAO testDao = new TestDAO();

        long ahora = System.currentTimeMillis();
        Date desde = new Date(ahora + 10_000);
        Date hasta = new Date(ahora);

        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> testDao.rangoFechas(desde, hasta)
        );

        assertEquals(
                "El rango de fechas no es válido: la fecha inicial "
                + "no puede ser posterior a la fecha final",
                e.getMessage()
        );
    }

    @Test
    void validarRangoFechasValidoNoLanza() {

        TestDAO testDao = new TestDAO();

        long ahora = System.currentTimeMillis();
        assertDoesNotThrow(() -> testDao.rangoFechas(new Date(ahora), new Date(ahora + 10_000)));
    }

    // ---------- helpers: validarPaginacion ----------

    @Test
    void validarPaginacionRechazaValoresInvalidos() {

        TestDAO testDao = new TestDAO();

        assertAll(
                () -> assertEquals(
                        "Los parámetros de paginación (first/max) no son válidos",
                        assertThrows(IllegalArgumentException.class,
                                () -> testDao.paginacion(-1, 10)).getMessage()),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> testDao.paginacion(0, 0)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> testDao.paginacion(0, -5))
        );
    }

    // ---------- helpers: buscarConNamedQuery ----------

    @Nested
    class BuscarConNamedQuery {

        @Test
        void devuelveListaNoNulaConResultados() {

            TestDAO testDao = new TestDAO();
            testDao.em = mock(EntityManager.class);
            @SuppressWarnings("unchecked")
            TypedQuery<Rol> q = mock(TypedQuery.class);

            when(testDao.em.createNamedQuery("Rol.findAll", Rol.class)).thenReturn(q);
            when(q.getResultList()).thenReturn(List.of(new Rol()));

            List<Rol> resultado = testDao.lista("Rol.findAll");

            assertNotNull(resultado);
            assertEquals(1, resultado.size());
        }

        @Test
        void devuelveListaVaciaCuandoNoHayResultados() {

            TestDAO testDao = new TestDAO();
            testDao.em = mock(EntityManager.class);
            @SuppressWarnings("unchecked")
            TypedQuery<Rol> q = mock(TypedQuery.class);

            when(testDao.em.createNamedQuery("Rol.findAll", Rol.class)).thenReturn(q);
            when(q.getResultList()).thenReturn(List.of());

            List<Rol> resultado = testDao.lista("Rol.findAll");

            assertNotNull(resultado);
            assertTrue(resultado.isEmpty());
        }

        @Test
        void devuelveListaVaciaCuandoElDriverDevuelveNull() {

            TestDAO testDao = new TestDAO();
            testDao.em = mock(EntityManager.class);
            @SuppressWarnings("unchecked")
            TypedQuery<Rol> q = mock(TypedQuery.class);

            when(testDao.em.createNamedQuery("Rol.findAll", Rol.class)).thenReturn(q);
            when(q.getResultList()).thenReturn(null);

            List<Rol> resultado = testDao.lista("Rol.findAll");

            assertNotNull(resultado);
            assertTrue(resultado.isEmpty());
        }

        @Test
        void envuelveLasExcepcionesDeJPAEnDAOException() {

            TestDAO testDao = new TestDAO();
            testDao.em = mock(EntityManager.class);
            @SuppressWarnings("unchecked")
            TypedQuery<Rol> q = mock(TypedQuery.class);

            PersistenceException causa = new PersistenceException("boom");
            when(testDao.em.createNamedQuery("Rol.findAll", Rol.class)).thenReturn(q);
            when(q.getResultList()).thenThrow(causa);

            DAOException e = assertThrows(
                    DAOException.class,
                    () -> testDao.lista("Rol.findAll")
            );

            assertEquals("Error al ejecutar la consulta Rol.findAll", e.getMessage());
            assertSame(causa, e.getCause());
        }

        @Test
        void rechazaEntityManagerNulo() {

            TestDAO testDao = new TestDAO();
            testDao.em = null;

            IllegalStateException e = assertThrows(
                    IllegalStateException.class,
                    () -> testDao.lista("Rol.findAll")
            );

            assertTrue(e.getMessage().contains("EntityManager no está disponible"));
        }
    }

    // ---------- helpers: buscarUnicoConNamedQuery ----------

    @Nested
    class BuscarUnicoConNamedQuery {

        @Test
        void devuelveElResultadoUnico() {

            TestDAO testDao = new TestDAO();
            testDao.em = mock(EntityManager.class);
            @SuppressWarnings("unchecked")
            TypedQuery<Rol> q = mock(TypedQuery.class);

            Rol esperado = new Rol();
            when(testDao.em.createNamedQuery("Rol.findAll", Rol.class)).thenReturn(q);
            when(q.getSingleResult()).thenReturn(esperado);

            assertSame(esperado, testDao.unico("Rol.findAll"));
        }

        @Test
        void devuelveNullConNoResultException() {

            TestDAO testDao = new TestDAO();
            testDao.em = mock(EntityManager.class);
            @SuppressWarnings("unchecked")
            TypedQuery<Rol> q = mock(TypedQuery.class);

            when(testDao.em.createNamedQuery("Rol.findAll", Rol.class)).thenReturn(q);
            when(q.getSingleResult()).thenThrow(new NoResultException());

            assertNull(testDao.unico("Rol.findAll"));
        }

        @Test
        void lanzaDAOExceptionConNonUniqueResultException() {

            TestDAO testDao = new TestDAO();
            testDao.em = mock(EntityManager.class);
            @SuppressWarnings("unchecked")
            TypedQuery<Rol> q = mock(TypedQuery.class);

            NonUniqueResultException causa = new NonUniqueResultException();
            when(testDao.em.createNamedQuery("Rol.findAll", Rol.class)).thenReturn(q);
            when(q.getSingleResult()).thenThrow(causa);

            DAOException e = assertThrows(
                    DAOException.class,
                    () -> testDao.unico("Rol.findAll")
            );

            assertTrue(e.getMessage().contains("más de un resultado"));
            assertSame(causa, e.getCause());
        }

        @Test
        void lanzaDAOExceptionConErroresDeJPA() {

            TestDAO testDao = new TestDAO();
            testDao.em = mock(EntityManager.class);
            @SuppressWarnings("unchecked")
            TypedQuery<Rol> q = mock(TypedQuery.class);

            PersistenceException causa = new PersistenceException("boom");
            when(testDao.em.createNamedQuery("Rol.findAll", Rol.class)).thenReturn(q);
            when(q.getSingleResult()).thenThrow(causa);

            DAOException e = assertThrows(
                    DAOException.class,
                    () -> testDao.unico("Rol.findAll")
            );

            assertEquals("Error al ejecutar la consulta Rol.findAll", e.getMessage());
            assertSame(causa, e.getCause());
        }

        @Test
        void rechazaEntityManagerNulo() {

            TestDAO testDao = new TestDAO();
            testDao.em = null;

            IllegalStateException e = assertThrows(
                    IllegalStateException.class,
                    () -> testDao.unico("Rol.findAll")
            );

            assertTrue(e.getMessage().contains("EntityManager no está disponible"));
        }
    }

    // ---------- crear ----------

    @Test
    void crearPersisteElRegistro() {

        Rol rol = new Rol();

        dao.crear(rol);

        verify(em).persist(rol);
    }

    @Test
    void crearConRegistroNulo() {

        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> dao.crear(null)
        );

        assertEquals("El registro no puede ser nulo", e.getMessage());

        verifyNoInteractions(em);
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

    @Test
    void crearConPersistenceExceptionLanzaDAOException() {

        Rol rol = new Rol();

        PersistenceException causa = new PersistenceException("violación de restricción");
        doThrow(causa)
                .when(em).persist(rol);

        DAOException e = assertThrows(
                DAOException.class,
                () -> dao.crear(rol)
        );

        assertEquals("Error al crear el regsitro", e.getMessage());
        assertSame(causa, e.getCause());
    }

    @Test
    void crearConEntityManagerNulo() {

        RolDAO daoSinEm = new RolDAO();

        IllegalStateException e = assertThrows(
                IllegalStateException.class,
                () -> daoSinEm.crear(new Rol())
        );

        assertTrue(e.getMessage().contains("EntityManager no está disponible"));
    }

    // ---------- actualizar ----------

    @Test
    void actualizarHaceMerge() {

        Rol rol = new Rol();

        dao.actualizar(rol);

        verify(em).merge(rol);
    }

    @Test
    void actualizarConRegistroNulo() {

        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> dao.actualizar(null)
        );

        assertEquals("El registro no puede ser nulo", e.getMessage());

        verifyNoInteractions(em);
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

    @Test
    void actualizarConEntityManagerNulo() {

        RolDAO daoSinEm = new RolDAO();

        IllegalStateException e = assertThrows(
                IllegalStateException.class,
                () -> daoSinEm.actualizar(new Rol())
        );

        assertTrue(e.getMessage().contains("EntityManager no está disponible"));
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
    void buscarConIdNulo() {

        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> dao.buscar(null)
        );

        assertEquals("El id no puede ser nulo", e.getMessage());

        verifyNoInteractions(em);
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

    @Test
    void buscarConEntityManagerNulo() {

        RolDAO daoSinEm = new RolDAO();

        IllegalStateException e = assertThrows(
                IllegalStateException.class,
                () -> daoSinEm.buscar(UUID.randomUUID())
        );

        assertTrue(e.getMessage().contains("EntityManager no está disponible"));
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
    void eliminarConIdNulo() {

        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> dao.eliminar(null)
        );

        assertEquals("El id no puede ser nulo", e.getMessage());

        verifyNoInteractions(em);
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

    @Test
    void eliminarConEntityManagerNulo() {

        RolDAO daoSinEm = new RolDAO();

        IllegalStateException e = assertThrows(
                IllegalStateException.class,
                () -> daoSinEm.eliminar(UUID.randomUUID())
        );

        assertTrue(e.getMessage().contains("EntityManager no está disponible"));
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

    @Test
    void findRangeConEntityManagerNulo() {

        RolDAO daoSinEm = new RolDAO();

        IllegalStateException e = assertThrows(
                IllegalStateException.class,
                () -> daoSinEm.findRange(0, 10)
        );

        assertTrue(e.getMessage().contains("EntityManager no está disponible"));
    }
}
