package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ExamenTipoExamen;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ExamenTipoExamenDAOTest {

    @Test
    public void testGetEntityManger() {
        ExamenTipoExamenDAO cut = new ExamenTipoExamenDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        cut.em = mockEM; 
        assertEquals(mockEM, cut.getEntityManger());
    }

    @Test
    public void testBuscarPorExamen() {
        UUID id = UUID.randomUUID();
        ExamenTipoExamenDAO cut = new ExamenTipoExamenDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        @SuppressWarnings("unchecked")
        TypedQuery<ExamenTipoExamen> mockQuery = Mockito.mock(TypedQuery.class);
        
        Mockito.when(mockEM.createNamedQuery("ExamenTipoExamen.findByExamenId", ExamenTipoExamen.class)).thenReturn(mockQuery);
        Mockito.when(mockQuery.getResultList()).thenReturn(Collections.singletonList(new ExamenTipoExamen()));
        
        ExamenTipoExamenDAO espia = Mockito.spy(cut);
        Mockito.doReturn(mockEM).when(espia).getEntityManger();
        
        assertFalse(espia.buscarPorExamen(id).isEmpty());
        Mockito.verify(mockQuery).setParameter("id", id);
    }

    @Test
    public void testBuscarPorTipoExamen() {
        UUID id = UUID.randomUUID();
        ExamenTipoExamenDAO cut = new ExamenTipoExamenDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        @SuppressWarnings("unchecked")
        TypedQuery<ExamenTipoExamen> mockQuery = Mockito.mock(TypedQuery.class);
        
        Mockito.when(mockEM.createNamedQuery("ExamenTipoExamen.findByTipoExamenId", ExamenTipoExamen.class)).thenReturn(mockQuery);
        Mockito.when(mockQuery.getResultList()).thenReturn(Collections.singletonList(new ExamenTipoExamen()));
        
        ExamenTipoExamenDAO espia = Mockito.spy(cut);
        Mockito.doReturn(mockEM).when(espia).getEntityManger();
        
        assertFalse(espia.buscarPorTipoExamen(id).isEmpty());
        Mockito.verify(mockQuery).setParameter("id", id);
    }

    @Test
    public void testBuscarNulos() {
        ExamenTipoExamenDAO cut = new ExamenTipoExamenDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);

        cut.em = mockEM;

        IllegalArgumentException excepcionExamen = assertThrows(
                IllegalArgumentException.class,
                () -> cut.buscarPorExamen(null)
        );
        assertEquals("El idExamen no puede ser nulo", excepcionExamen.getMessage());

        IllegalArgumentException excepcionTipoExamen = assertThrows(
                IllegalArgumentException.class,
                () -> cut.buscarPorTipoExamen(null)
        );
        assertEquals("El idTipoExamen no puede ser nulo", excepcionTipoExamen.getMessage());

        Mockito.verifyNoInteractions(mockEM);
    }

    // ---------- buscarPorExamen: casos adicionales ----------

    @Test
    public void testBuscarPorExamenResultadoVacio() {
        ExamenTipoExamenDAO cut = new ExamenTipoExamenDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        @SuppressWarnings("unchecked")
        TypedQuery<ExamenTipoExamen> mockQuery = Mockito.mock(TypedQuery.class);

        Mockito.when(mockEM.createNamedQuery("ExamenTipoExamen.findByExamenId", ExamenTipoExamen.class)).thenReturn(mockQuery);
        Mockito.when(mockQuery.getResultList()).thenReturn(Collections.emptyList());

        cut.em = mockEM;

        List<ExamenTipoExamen> resultado = cut.buscarPorExamen(UUID.randomUUID());
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    public void testBuscarPorExamenExcepcionPersistencia() {
        ExamenTipoExamenDAO cut = new ExamenTipoExamenDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        @SuppressWarnings("unchecked")
        TypedQuery<ExamenTipoExamen> mockQuery = Mockito.mock(TypedQuery.class);

        PersistenceException causa = new PersistenceException("fallo de base de datos");

        Mockito.when(mockEM.createNamedQuery("ExamenTipoExamen.findByExamenId", ExamenTipoExamen.class)).thenReturn(mockQuery);
        Mockito.when(mockQuery.getResultList()).thenThrow(causa);

        cut.em = mockEM;

        DAOException excepcion = assertThrows(DAOException.class, () -> cut.buscarPorExamen(UUID.randomUUID()));
        assertSame(causa, excepcion.getCause());
    }

    @Test
    public void testBuscarPorExamenEntityManagerNulo() {
        ExamenTipoExamenDAO cut = new ExamenTipoExamenDAO();

        IllegalStateException excepcion = assertThrows(
                IllegalStateException.class,
                () -> cut.buscarPorExamen(UUID.randomUUID())
        );
        assertTrue(excepcion.getMessage().contains("EntityManager no está disponible"));
    }

    // ---------- buscarPorTipoExamen: casos adicionales ----------

    @Test
    public void testBuscarPorTipoExamenResultadoVacio() {
        ExamenTipoExamenDAO cut = new ExamenTipoExamenDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        @SuppressWarnings("unchecked")
        TypedQuery<ExamenTipoExamen> mockQuery = Mockito.mock(TypedQuery.class);

        Mockito.when(mockEM.createNamedQuery("ExamenTipoExamen.findByTipoExamenId", ExamenTipoExamen.class)).thenReturn(mockQuery);
        Mockito.when(mockQuery.getResultList()).thenReturn(Collections.emptyList());

        cut.em = mockEM;

        List<ExamenTipoExamen> resultado = cut.buscarPorTipoExamen(UUID.randomUUID());
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    public void testBuscarPorTipoExamenExcepcionPersistencia() {
        ExamenTipoExamenDAO cut = new ExamenTipoExamenDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        @SuppressWarnings("unchecked")
        TypedQuery<ExamenTipoExamen> mockQuery = Mockito.mock(TypedQuery.class);

        PersistenceException causa = new PersistenceException("fallo de base de datos");

        Mockito.when(mockEM.createNamedQuery("ExamenTipoExamen.findByTipoExamenId", ExamenTipoExamen.class)).thenReturn(mockQuery);
        Mockito.when(mockQuery.getResultList()).thenThrow(causa);

        cut.em = mockEM;

        DAOException excepcion = assertThrows(DAOException.class, () -> cut.buscarPorTipoExamen(UUID.randomUUID()));
        assertSame(causa, excepcion.getCause());
    }

    @Test
    public void testBuscarPorTipoExamenEntityManagerNulo() {
        ExamenTipoExamenDAO cut = new ExamenTipoExamenDAO();

        IllegalStateException excepcion = assertThrows(
                IllegalStateException.class,
                () -> cut.buscarPorTipoExamen(UUID.randomUUID())
        );
        assertTrue(excepcion.getMessage().contains("EntityManager no está disponible"));
    }
}
