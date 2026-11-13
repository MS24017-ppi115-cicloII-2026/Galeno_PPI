package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ProcedimientoPasoSecuencia;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ProcedimientoPasoSecuenciaDAOTest {

    @Test
    public void testGetEntityManger() {
        ProcedimientoPasoSecuenciaDAO cut = new ProcedimientoPasoSecuenciaDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        cut.em = mockEM; 
        assertEquals(mockEM, cut.getEntityManger());
    }

    // --- buscarPorProcedimientoPaso ---

    @Test
    public void testBuscarPorProcedimientoPaso() {
        UUID id = UUID.randomUUID();
        ProcedimientoPasoSecuenciaDAO cut = new ProcedimientoPasoSecuenciaDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        @SuppressWarnings("unchecked")
        TypedQuery<ProcedimientoPasoSecuencia> mockQuery = Mockito.mock(TypedQuery.class);
        
        Mockito.when(mockEM.createNamedQuery(
                "ProcedimientoPasoSecuencia.findByProcedimientoPasoId", ProcedimientoPasoSecuencia.class))
                .thenReturn(mockQuery);
        Mockito.when(mockQuery.getResultList())
                .thenReturn(Collections.singletonList(new ProcedimientoPasoSecuencia()));
        
        ProcedimientoPasoSecuenciaDAO espia = Mockito.spy(cut);
        Mockito.doReturn(mockEM).when(espia).getEntityManger();
        
        assertFalse(espia.buscarPorProcedimientoPaso(id).isEmpty());
        Mockito.verify(mockEM)
                .createNamedQuery("ProcedimientoPasoSecuencia.findByProcedimientoPasoId", ProcedimientoPasoSecuencia.class);
        Mockito.verify(mockQuery).setParameter("id", id);
    }

    @Test
    public void testBuscarPorProcedimientoPasoListaVacia() {
        UUID id = UUID.randomUUID();
        ProcedimientoPasoSecuenciaDAO cut = new ProcedimientoPasoSecuenciaDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        @SuppressWarnings("unchecked")
        TypedQuery<ProcedimientoPasoSecuencia> mockQuery = Mockito.mock(TypedQuery.class);
        
        Mockito.when(mockEM.createNamedQuery(
                "ProcedimientoPasoSecuencia.findByProcedimientoPasoId", ProcedimientoPasoSecuencia.class))
                .thenReturn(mockQuery);
        Mockito.when(mockQuery.getResultList())
                .thenReturn(Collections.emptyList());
        
        ProcedimientoPasoSecuenciaDAO espia = Mockito.spy(cut);
        Mockito.doReturn(mockEM).when(espia).getEntityManger();
        
        List<ProcedimientoPasoSecuencia> resultado = espia.buscarPorProcedimientoPaso(id);
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    public void testBuscarPorProcedimientoPasoExcepcionServidor() {
        UUID id = UUID.randomUUID();
        ProcedimientoPasoSecuenciaDAO cut = new ProcedimientoPasoSecuenciaDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        @SuppressWarnings("unchecked")
        TypedQuery<ProcedimientoPasoSecuencia> mockQuery = Mockito.mock(TypedQuery.class);
        
        Mockito.when(mockEM.createNamedQuery(
                "ProcedimientoPasoSecuencia.findByProcedimientoPasoId", ProcedimientoPasoSecuencia.class))
                .thenReturn(mockQuery);
        
        PersistenceException causa = new PersistenceException("Fallo simulado de BD");
        Mockito.when(mockQuery.getResultList()).thenThrow(causa);
        
        ProcedimientoPasoSecuenciaDAO espia = Mockito.spy(cut);
        Mockito.doReturn(mockEM).when(espia).getEntityManger();
        
        DAOException excepcion = assertThrows(DAOException.class, () -> {
            espia.buscarPorProcedimientoPaso(id);
        });
        assertSame(causa, excepcion.getCause());
    }

    @Test
    public void testBuscarPorProcedimientoPasoEntityManagerNulo() {
        ProcedimientoPasoSecuenciaDAO cut = new ProcedimientoPasoSecuenciaDAO();
        cut.em = null;
        
        IllegalStateException excepcion = assertThrows(IllegalStateException.class, () -> {
            cut.buscarPorProcedimientoPaso(UUID.randomUUID());
        });
        assertTrue(excepcion.getMessage()
                .contains("EntityManager no está disponible"));
    }

    // --- buscarPorPasoReferencia ---

    @Test
    public void testBuscarPorPasoReferencia() {
        UUID id = UUID.randomUUID();
        ProcedimientoPasoSecuenciaDAO cut = new ProcedimientoPasoSecuenciaDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        @SuppressWarnings("unchecked")
        TypedQuery<ProcedimientoPasoSecuencia> mockQuery = Mockito.mock(TypedQuery.class);
        
        Mockito.when(mockEM.createNamedQuery(
                "ProcedimientoPasoSecuencia.findByPasoReferenciaId", ProcedimientoPasoSecuencia.class))
                .thenReturn(mockQuery);
        Mockito.when(mockQuery.getResultList())
                .thenReturn(Collections.singletonList(new ProcedimientoPasoSecuencia()));
        
        ProcedimientoPasoSecuenciaDAO espia = Mockito.spy(cut);
        Mockito.doReturn(mockEM).when(espia).getEntityManger();
        
        assertFalse(espia.buscarPorPasoReferencia(id).isEmpty());
        Mockito.verify(mockEM)
                .createNamedQuery("ProcedimientoPasoSecuencia.findByPasoReferenciaId", ProcedimientoPasoSecuencia.class);
        Mockito.verify(mockQuery).setParameter("id", id);
    }

    @Test
    public void testBuscarPorPasoReferenciaListaVacia() {
        UUID id = UUID.randomUUID();
        ProcedimientoPasoSecuenciaDAO cut = new ProcedimientoPasoSecuenciaDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        @SuppressWarnings("unchecked")
        TypedQuery<ProcedimientoPasoSecuencia> mockQuery = Mockito.mock(TypedQuery.class);
        
        Mockito.when(mockEM.createNamedQuery(
                "ProcedimientoPasoSecuencia.findByPasoReferenciaId", ProcedimientoPasoSecuencia.class))
                .thenReturn(mockQuery);
        Mockito.when(mockQuery.getResultList())
                .thenReturn(Collections.emptyList());
        
        ProcedimientoPasoSecuenciaDAO espia = Mockito.spy(cut);
        Mockito.doReturn(mockEM).when(espia).getEntityManger();
        
        List<ProcedimientoPasoSecuencia> resultado = espia.buscarPorPasoReferencia(id);
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    public void testBuscarPorPasoReferenciaExcepcionServidor() {
        UUID id = UUID.randomUUID();
        ProcedimientoPasoSecuenciaDAO cut = new ProcedimientoPasoSecuenciaDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        @SuppressWarnings("unchecked")
        TypedQuery<ProcedimientoPasoSecuencia> mockQuery = Mockito.mock(TypedQuery.class);
        
        Mockito.when(mockEM.createNamedQuery(
                "ProcedimientoPasoSecuencia.findByPasoReferenciaId", ProcedimientoPasoSecuencia.class))
                .thenReturn(mockQuery);
        
        PersistenceException causa = new PersistenceException("Fallo simulado de BD");
        Mockito.when(mockQuery.getResultList()).thenThrow(causa);
        
        ProcedimientoPasoSecuenciaDAO espia = Mockito.spy(cut);
        Mockito.doReturn(mockEM).when(espia).getEntityManger();
        
        DAOException excepcion = assertThrows(DAOException.class, () -> {
            espia.buscarPorPasoReferencia(id);
        });
        assertSame(causa, excepcion.getCause());
    }

    @Test
    public void testBuscarPorPasoReferenciaEntityManagerNulo() {
        ProcedimientoPasoSecuenciaDAO cut = new ProcedimientoPasoSecuenciaDAO();
        cut.em = null;
        
        IllegalStateException excepcion = assertThrows(IllegalStateException.class, () -> {
            cut.buscarPorPasoReferencia(UUID.randomUUID());
        });
        assertTrue(excepcion.getMessage()
                .contains("EntityManager no está disponible"));
    }

    // --- validaciones de argumentos ---

    @Test
    public void testBuscarNulos() {
        ProcedimientoPasoSecuenciaDAO cut = new ProcedimientoPasoSecuenciaDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        cut.em = mockEM;

        IllegalArgumentException excepcion = assertThrows(
                IllegalArgumentException.class, () -> cut.buscarPorProcedimientoPaso(null));
        assertEquals("El idProcedimientoPaso no puede ser nulo", excepcion.getMessage());

        excepcion = assertThrows(
                IllegalArgumentException.class, () -> cut.buscarPorPasoReferencia(null));
        assertEquals("El idPasoReferencia no puede ser nulo", excepcion.getMessage());

        Mockito.verifyNoInteractions(mockEM);
    }
}
