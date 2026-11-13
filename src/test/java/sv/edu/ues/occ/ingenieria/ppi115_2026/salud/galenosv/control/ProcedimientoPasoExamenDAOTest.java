package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ProcedimientoPasoExamen;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ProcedimientoPasoExamenDAOTest {

    @Test
    public void testGetEntityManger() {
        ProcedimientoPasoExamenDAO cut = new ProcedimientoPasoExamenDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        cut.em = mockEM; 
        assertEquals(mockEM, cut.getEntityManger());
    }

    // --- buscarPorExamen ---

    @Test
    public void testBuscarPorExamen() {
        UUID id = UUID.randomUUID();
        ProcedimientoPasoExamenDAO cut = new ProcedimientoPasoExamenDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        @SuppressWarnings("unchecked")
        TypedQuery<ProcedimientoPasoExamen> mockQuery = Mockito.mock(TypedQuery.class);
        
        Mockito.when(mockEM.createNamedQuery(
                "ProcedimientoPasoExamen.findByExamenId", ProcedimientoPasoExamen.class))
                .thenReturn(mockQuery);
        Mockito.when(mockQuery.getResultList())
                .thenReturn(Collections.singletonList(new ProcedimientoPasoExamen()));
        
        ProcedimientoPasoExamenDAO espia = Mockito.spy(cut);
        Mockito.doReturn(mockEM).when(espia).getEntityManger();
        
        assertFalse(espia.buscarPorExamen(id).isEmpty());
        Mockito.verify(mockEM)
                .createNamedQuery("ProcedimientoPasoExamen.findByExamenId", ProcedimientoPasoExamen.class);
        Mockito.verify(mockQuery).setParameter("id", id);
    }

    @Test
    public void testBuscarPorExamenListaVacia() {
        UUID id = UUID.randomUUID();
        ProcedimientoPasoExamenDAO cut = new ProcedimientoPasoExamenDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        @SuppressWarnings("unchecked")
        TypedQuery<ProcedimientoPasoExamen> mockQuery = Mockito.mock(TypedQuery.class);
        
        Mockito.when(mockEM.createNamedQuery(
                "ProcedimientoPasoExamen.findByExamenId", ProcedimientoPasoExamen.class))
                .thenReturn(mockQuery);
        Mockito.when(mockQuery.getResultList())
                .thenReturn(Collections.emptyList());
        
        ProcedimientoPasoExamenDAO espia = Mockito.spy(cut);
        Mockito.doReturn(mockEM).when(espia).getEntityManger();
        
        List<ProcedimientoPasoExamen> resultado = espia.buscarPorExamen(id);
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    public void testBuscarPorExamenExcepcionServidor() {
        UUID id = UUID.randomUUID();
        ProcedimientoPasoExamenDAO cut = new ProcedimientoPasoExamenDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        @SuppressWarnings("unchecked")
        TypedQuery<ProcedimientoPasoExamen> mockQuery = Mockito.mock(TypedQuery.class);
        
        Mockito.when(mockEM.createNamedQuery(
                "ProcedimientoPasoExamen.findByExamenId", ProcedimientoPasoExamen.class))
                .thenReturn(mockQuery);
        
        PersistenceException causa = new PersistenceException("Fallo simulado de BD");
        Mockito.when(mockQuery.getResultList()).thenThrow(causa);
        
        ProcedimientoPasoExamenDAO espia = Mockito.spy(cut);
        Mockito.doReturn(mockEM).when(espia).getEntityManger();
        
        DAOException excepcion = assertThrows(DAOException.class, () -> {
            espia.buscarPorExamen(id);
        });
        assertSame(causa, excepcion.getCause());
    }

    @Test
    public void testBuscarPorExamenEntityManagerNulo() {
        ProcedimientoPasoExamenDAO cut = new ProcedimientoPasoExamenDAO();
        cut.em = null;
        
        IllegalStateException excepcion = assertThrows(IllegalStateException.class, () -> {
            cut.buscarPorExamen(UUID.randomUUID());
        });
        assertTrue(excepcion.getMessage()
                .contains("EntityManager no está disponible"));
    }

    // --- buscarPorProcedimientoPaso ---

    @Test
    public void testBuscarPorProcedimientoPaso() {
        UUID id = UUID.randomUUID();
        ProcedimientoPasoExamenDAO cut = new ProcedimientoPasoExamenDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        @SuppressWarnings("unchecked")
        TypedQuery<ProcedimientoPasoExamen> mockQuery = Mockito.mock(TypedQuery.class);
        
        Mockito.when(mockEM.createNamedQuery(
                "ProcedimientoPasoExamen.findByProcedimientoPasoId", ProcedimientoPasoExamen.class))
                .thenReturn(mockQuery);
        Mockito.when(mockQuery.getResultList())
                .thenReturn(Collections.singletonList(new ProcedimientoPasoExamen()));
        
        ProcedimientoPasoExamenDAO espia = Mockito.spy(cut);
        Mockito.doReturn(mockEM).when(espia).getEntityManger();
        
        assertFalse(espia.buscarPorProcedimientoPaso(id).isEmpty());
        Mockito.verify(mockEM)
                .createNamedQuery("ProcedimientoPasoExamen.findByProcedimientoPasoId", ProcedimientoPasoExamen.class);
        Mockito.verify(mockQuery).setParameter("id", id);
    }

    @Test
    public void testBuscarPorProcedimientoPasoListaVacia() {
        UUID id = UUID.randomUUID();
        ProcedimientoPasoExamenDAO cut = new ProcedimientoPasoExamenDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        @SuppressWarnings("unchecked")
        TypedQuery<ProcedimientoPasoExamen> mockQuery = Mockito.mock(TypedQuery.class);
        
        Mockito.when(mockEM.createNamedQuery(
                "ProcedimientoPasoExamen.findByProcedimientoPasoId", ProcedimientoPasoExamen.class))
                .thenReturn(mockQuery);
        Mockito.when(mockQuery.getResultList())
                .thenReturn(Collections.emptyList());
        
        ProcedimientoPasoExamenDAO espia = Mockito.spy(cut);
        Mockito.doReturn(mockEM).when(espia).getEntityManger();
        
        List<ProcedimientoPasoExamen> resultado = espia.buscarPorProcedimientoPaso(id);
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    public void testBuscarPorProcedimientoPasoExcepcionServidor() {
        UUID id = UUID.randomUUID();
        ProcedimientoPasoExamenDAO cut = new ProcedimientoPasoExamenDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        @SuppressWarnings("unchecked")
        TypedQuery<ProcedimientoPasoExamen> mockQuery = Mockito.mock(TypedQuery.class);
        
        Mockito.when(mockEM.createNamedQuery(
                "ProcedimientoPasoExamen.findByProcedimientoPasoId", ProcedimientoPasoExamen.class))
                .thenReturn(mockQuery);
        
        PersistenceException causa = new PersistenceException("Fallo simulado de BD");
        Mockito.when(mockQuery.getResultList()).thenThrow(causa);
        
        ProcedimientoPasoExamenDAO espia = Mockito.spy(cut);
        Mockito.doReturn(mockEM).when(espia).getEntityManger();
        
        DAOException excepcion = assertThrows(DAOException.class, () -> {
            espia.buscarPorProcedimientoPaso(id);
        });
        assertSame(causa, excepcion.getCause());
    }

    @Test
    public void testBuscarPorProcedimientoPasoEntityManagerNulo() {
        ProcedimientoPasoExamenDAO cut = new ProcedimientoPasoExamenDAO();
        cut.em = null;
        
        IllegalStateException excepcion = assertThrows(IllegalStateException.class, () -> {
            cut.buscarPorProcedimientoPaso(UUID.randomUUID());
        });
        assertTrue(excepcion.getMessage()
                .contains("EntityManager no está disponible"));
    }

    // --- validaciones de argumentos ---

    @Test
    public void testBuscarNulos() {
        ProcedimientoPasoExamenDAO cut = new ProcedimientoPasoExamenDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        cut.em = mockEM;

        IllegalArgumentException excepcion = assertThrows(
                IllegalArgumentException.class, () -> cut.buscarPorExamen(null));
        assertEquals("El idExamen no puede ser nulo", excepcion.getMessage());

        excepcion = assertThrows(
                IllegalArgumentException.class, () -> cut.buscarPorProcedimientoPaso(null));
        assertEquals("El idProcedimientoPaso no puede ser nulo", excepcion.getMessage());

        Mockito.verifyNoInteractions(mockEM);
    }
}
