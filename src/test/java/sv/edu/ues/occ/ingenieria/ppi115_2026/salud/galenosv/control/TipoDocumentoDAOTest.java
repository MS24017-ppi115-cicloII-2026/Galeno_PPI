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
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.TipoDocumento;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TipoDocumentoDAOTest {

    @Mock
    private EntityManager em;

    @Mock
    private TypedQuery<TipoDocumento> query;

    @InjectMocks
    private TipoDocumentoDAO dao;

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

        TipoDocumento tipoDocumento = new TipoDocumento();

        tipoDocumento.setNombre("DUI");

        when(em.createNamedQuery(
                "TipoDocumento.findByNombre",
                TipoDocumento.class
        )).thenReturn(query);

        when(query.getResultList())
                .thenReturn(List.of(tipoDocumento));

        List<TipoDocumento> resultado =
                dao.buscarPorNombre("DUI");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(
                "DUI",
                resultado.get(0).getNombre()
        );

        verify(em).createNamedQuery(
                "TipoDocumento.findByNombre",
                TipoDocumento.class
        );
        verify(query).setParameter(
                "nombre",
                "DUI"
        );
        verify(query).getResultList();
    }

    @Test
    void buscarPorNombreSinResultados() {

        when(em.createNamedQuery(
                "TipoDocumento.findByNombre",
                TipoDocumento.class
        )).thenReturn(query);

        when(query.getResultList())
                .thenReturn(List.of());

        List<TipoDocumento> resultado =
                dao.buscarPorNombre("Inexistente");

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(query).setParameter(
                "nombre",
                "Inexistente"
        );
    }

    @Test
    void buscarPorNombreErrorEnConsulta() {

        PersistenceException causa =
                new PersistenceException("fallo de BD");

        when(em.createNamedQuery(
                "TipoDocumento.findByNombre",
                TipoDocumento.class
        )).thenReturn(query);

        when(query.getResultList())
                .thenThrow(causa);

        DAOException e = assertThrows(
                DAOException.class,
                () -> dao.buscarPorNombre("DUI")
        );
        assertSame(causa, e.getCause());
    }

    @Test
    void buscarPorNombreSinEntityManager() {

        TipoDocumentoDAO daoSinEm = new TipoDocumentoDAO();

        IllegalStateException e = assertThrows(
                IllegalStateException.class,
                () -> daoSinEm.buscarPorNombre("DUI")
        );
        assertTrue(e.getMessage().contains(
                "EntityManager no está disponible"));
    }

    // ---------- buscarPorIndicaciones ----------

    @Test
    void buscarPorIndicacionesNulo() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> dao.buscarPorIndicaciones(null)
        );
        assertEquals("Las indicaciones no pueden ser nulas o vacías", e.getMessage());
        verifyNoInteractions(em);
    }

    @Test
    void buscarPorIndicacionesVacio() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> dao.buscarPorIndicaciones("")
        );
        assertEquals("Las indicaciones no pueden ser nulas o vacías", e.getMessage());
        verifyNoInteractions(em);
    }

    @Test
    void buscarPorIndicacionesBlanco() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> dao.buscarPorIndicaciones("   ")
        );
        assertEquals("Las indicaciones no pueden ser nulas o vacías", e.getMessage());
        verifyNoInteractions(em);
    }

    @Test
    void buscarPorIndicacionesCorrectamente() {

        TipoDocumento tipoDocumento = new TipoDocumento();

        tipoDocumento.setNombre("DUI");
        tipoDocumento.setIndicaciones(
                "Documento de identidad"
        );

        when(em.createNamedQuery(
                "TipoDocumento.findByIndicaciones",
                TipoDocumento.class
        )).thenReturn(query);

        when(query.getResultList())
                .thenReturn(List.of(tipoDocumento));

        List<TipoDocumento> resultado =
                dao.buscarPorIndicaciones(
                        "Documento de identidad"
                );

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(
                "Documento de identidad",
                resultado.get(0).getIndicaciones()
        );

        verify(em).createNamedQuery(
                "TipoDocumento.findByIndicaciones",
                TipoDocumento.class
        );
        verify(query).setParameter(
                "indicaciones",
                "Documento de identidad"
        );
        verify(query).getResultList();
    }

    @Test
    void buscarPorIndicacionesSinResultados() {

        when(em.createNamedQuery(
                "TipoDocumento.findByIndicaciones",
                TipoDocumento.class
        )).thenReturn(query);

        when(query.getResultList())
                .thenReturn(List.of());

        List<TipoDocumento> resultado =
                dao.buscarPorIndicaciones("Inexistente");

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(query).setParameter(
                "indicaciones",
                "Inexistente"
        );
    }

    @Test
    void buscarPorIndicacionesErrorEnConsulta() {

        PersistenceException causa =
                new PersistenceException("fallo de BD");

        when(em.createNamedQuery(
                "TipoDocumento.findByIndicaciones",
                TipoDocumento.class
        )).thenReturn(query);

        when(query.getResultList())
                .thenThrow(causa);

        DAOException e = assertThrows(
                DAOException.class,
                () -> dao.buscarPorIndicaciones("Documento de identidad")
        );
        assertSame(causa, e.getCause());
    }

    @Test
    void buscarPorIndicacionesSinEntityManager() {

        TipoDocumentoDAO daoSinEm = new TipoDocumentoDAO();

        IllegalStateException e = assertThrows(
                IllegalStateException.class,
                () -> daoSinEm.buscarPorIndicaciones("Documento de identidad")
        );
        assertTrue(e.getMessage().contains(
                "EntityManager no está disponible"));
    }

    // ---------- buscarPorExpresionRegular ----------

    @Test
    void buscarPorExpresionRegularNula() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> dao.buscarPorExpresionRegular(null)
        );
        assertEquals("La expresión regular no puede ser nula o vacía", e.getMessage());
        verifyNoInteractions(em);
    }

    @Test
    void buscarPorExpresionRegularVacia() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> dao.buscarPorExpresionRegular("")
        );
        assertEquals("La expresión regular no puede ser nula o vacía", e.getMessage());
        verifyNoInteractions(em);
    }

    @Test
    void buscarPorExpresionRegularBlanca() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> dao.buscarPorExpresionRegular("   ")
        );
        assertEquals("La expresión regular no puede ser nula o vacía", e.getMessage());
        verifyNoInteractions(em);
    }

    @Test
    void buscarPorExpresionRegularCorrectamente() {

        TipoDocumento tipoDocumento = new TipoDocumento();

        tipoDocumento.setNombre("DUI");
        tipoDocumento.setExpresionRegular(
                "^[0-9]{8}-[0-9]$"
        );

        when(em.createNamedQuery(
                "TipoDocumento.findByExpresionRegular",
                TipoDocumento.class
        )).thenReturn(query);

        when(query.getResultList())
                .thenReturn(List.of(tipoDocumento));

        List<TipoDocumento> resultado =
                dao.buscarPorExpresionRegular(
                        "^[0-9]{8}-[0-9]$"
                );

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(
                "^[0-9]{8}-[0-9]$",
                resultado.get(0).getExpresionRegular()
        );

        verify(em).createNamedQuery(
                "TipoDocumento.findByExpresionRegular",
                TipoDocumento.class
        );
        verify(query).setParameter(
                "expresionRegular",
                "^[0-9]{8}-[0-9]$"
        );
        verify(query).getResultList();
    }

    @Test
    void buscarPorExpresionRegularSinResultados() {

        when(em.createNamedQuery(
                "TipoDocumento.findByExpresionRegular",
                TipoDocumento.class
        )).thenReturn(query);

        when(query.getResultList())
                .thenReturn(List.of());

        List<TipoDocumento> resultado =
                dao.buscarPorExpresionRegular(
                        "^[0-9]{8}-[0-9]$"
                );

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(query).setParameter(
                "expresionRegular",
                "^[0-9]{8}-[0-9]$"
        );
    }

    @Test
    void buscarPorExpresionRegularErrorEnConsulta() {

        PersistenceException causa =
                new PersistenceException("fallo de BD");

        when(em.createNamedQuery(
                "TipoDocumento.findByExpresionRegular",
                TipoDocumento.class
        )).thenReturn(query);

        when(query.getResultList())
                .thenThrow(causa);

        DAOException e = assertThrows(
                DAOException.class,
                () -> dao.buscarPorExpresionRegular("^[0-9]{8}-[0-9]$")
        );
        assertSame(causa, e.getCause());
    }

    @Test
    void buscarPorExpresionRegularSinEntityManager() {

        TipoDocumentoDAO daoSinEm = new TipoDocumentoDAO();

        IllegalStateException e = assertThrows(
                IllegalStateException.class,
                () -> daoSinEm.buscarPorExpresionRegular("^[0-9]{8}-[0-9]$")
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

        TipoDocumento tipoDocumento = new TipoDocumento();

        tipoDocumento.setNombre("DUI");
        tipoDocumento.setActivo(true);

        when(em.createNamedQuery(
                "TipoDocumento.findByActivo",
                TipoDocumento.class
        )).thenReturn(query);

        when(query.getResultList())
                .thenReturn(List.of(tipoDocumento));

        List<TipoDocumento> resultado =
                dao.buscarPorActivo(true);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).getActivo());

        verify(em).createNamedQuery(
                "TipoDocumento.findByActivo",
                TipoDocumento.class
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
                "TipoDocumento.findByActivo",
                TipoDocumento.class
        )).thenReturn(query);

        when(query.getResultList())
                .thenReturn(List.of());

        List<TipoDocumento> resultado =
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
                "TipoDocumento.findByActivo",
                TipoDocumento.class
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

        TipoDocumentoDAO daoSinEm = new TipoDocumentoDAO();

        IllegalStateException e = assertThrows(
                IllegalStateException.class,
                () -> daoSinEm.buscarPorActivo(true)
        );
        assertTrue(e.getMessage().contains(
                "EntityManager no está disponible"));
    }
}
