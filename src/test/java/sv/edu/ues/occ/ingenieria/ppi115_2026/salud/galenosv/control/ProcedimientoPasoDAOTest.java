package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ProcedimientoPaso;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ProcedimientoPasoDAOTest {

    @Test
    public void testGetEntityManger() {
        System.out.println("getEntityManger");
        ProcedimientoPasoDAO cut = new ProcedimientoPasoDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        
        cut.em = mockEM; 
        assertEquals(mockEM, cut.getEntityManger());
    }

    // --- buscarPorProcedimiento ---

    @Test
    public void testBuscarPorProcedimientoExito() {
        System.out.println("buscarPorProcedimiento éxito");
        UUID id = UUID.randomUUID();
        ProcedimientoPasoDAO cut = new ProcedimientoPasoDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        @SuppressWarnings("unchecked")
        TypedQuery<ProcedimientoPaso> mockQuery = Mockito.mock(TypedQuery.class);
        
        Mockito.when(mockEM.createNamedQuery(
                "ProcedimientoPaso.findByProcedimientoId", ProcedimientoPaso.class))
                .thenReturn(mockQuery);
        Mockito.when(mockQuery.getResultList())
                .thenReturn(Collections.singletonList(new ProcedimientoPaso()));
        
        ProcedimientoPasoDAO espia = Mockito.spy(cut);
        Mockito.doReturn(mockEM).when(espia).getEntityManger();
        
        List<ProcedimientoPaso> resultado = espia.buscarPorProcedimiento(id);
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        Mockito.verify(mockEM)
                .createNamedQuery("ProcedimientoPaso.findByProcedimientoId", ProcedimientoPaso.class);
        Mockito.verify(mockQuery).setParameter("id", id);
    }

    @Test
    public void testBuscarPorProcedimientoNulo() {
        System.out.println("buscarPorProcedimiento nulo");
        ProcedimientoPasoDAO cut = new ProcedimientoPasoDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        cut.em = mockEM;
        
        IllegalArgumentException excepcion = assertThrows(IllegalArgumentException.class, () -> {
            cut.buscarPorProcedimiento(null);
        });
        assertEquals("El idProcedimiento no puede ser nulo", excepcion.getMessage());
        Mockito.verifyNoInteractions(mockEM);
    }

    @Test
    public void testBuscarPorProcedimientoListaVacia() {
        System.out.println("buscarPorProcedimiento lista vacía");
        UUID id = UUID.randomUUID();
        ProcedimientoPasoDAO cut = new ProcedimientoPasoDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        @SuppressWarnings("unchecked")
        TypedQuery<ProcedimientoPaso> mockQuery = Mockito.mock(TypedQuery.class);
        
        Mockito.when(mockEM.createNamedQuery(
                "ProcedimientoPaso.findByProcedimientoId", ProcedimientoPaso.class))
                .thenReturn(mockQuery);
        Mockito.when(mockQuery.getResultList())
                .thenReturn(Collections.emptyList());
        
        ProcedimientoPasoDAO espia = Mockito.spy(cut);
        Mockito.doReturn(mockEM).when(espia).getEntityManger();
        
        List<ProcedimientoPaso> resultado = espia.buscarPorProcedimiento(id);
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    public void testBuscarPorProcedimientoExcepcionServidor() {
        System.out.println("buscarPorProcedimiento lanza DAOException");
        UUID id = UUID.randomUUID();
        ProcedimientoPasoDAO cut = new ProcedimientoPasoDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        @SuppressWarnings("unchecked")
        TypedQuery<ProcedimientoPaso> mockQuery = Mockito.mock(TypedQuery.class);
        
        Mockito.when(mockEM.createNamedQuery(
                "ProcedimientoPaso.findByProcedimientoId", ProcedimientoPaso.class))
                .thenReturn(mockQuery);
        
        PersistenceException causa = new PersistenceException("Fallo simulado de BD");
        Mockito.when(mockQuery.getResultList()).thenThrow(causa);
        
        ProcedimientoPasoDAO espia = Mockito.spy(cut);
        Mockito.doReturn(mockEM).when(espia).getEntityManger();
        
        DAOException excepcion = assertThrows(DAOException.class, () -> {
            espia.buscarPorProcedimiento(id);
        });
        assertSame(causa, excepcion.getCause());
    }

    @Test
    public void testBuscarPorProcedimientoEntityManagerNulo() {
        System.out.println("buscarPorProcedimiento EntityManager nulo");
        ProcedimientoPasoDAO cut = new ProcedimientoPasoDAO();
        cut.em = null;
        
        IllegalStateException excepcion = assertThrows(IllegalStateException.class, () -> {
            cut.buscarPorProcedimiento(UUID.randomUUID());
        });
        assertTrue(excepcion.getMessage()
                .contains("EntityManager no está disponible"));
    }

    // --- buscarPorRol ---

    @Test
    public void testBuscarPorRolExito() {
        System.out.println("buscarPorRol éxito");
        UUID id = UUID.randomUUID();
        ProcedimientoPasoDAO cut = new ProcedimientoPasoDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        @SuppressWarnings("unchecked")
        TypedQuery<ProcedimientoPaso> mockQuery = Mockito.mock(TypedQuery.class);
        
        Mockito.when(mockEM.createNamedQuery(
                "ProcedimientoPaso.findByRolId", ProcedimientoPaso.class))
                .thenReturn(mockQuery);
        Mockito.when(mockQuery.getResultList())
                .thenReturn(Collections.singletonList(new ProcedimientoPaso()));
        
        ProcedimientoPasoDAO espia = Mockito.spy(cut);
        Mockito.doReturn(mockEM).when(espia).getEntityManger();
        
        List<ProcedimientoPaso> resultado = espia.buscarPorRol(id);
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        Mockito.verify(mockEM)
                .createNamedQuery("ProcedimientoPaso.findByRolId", ProcedimientoPaso.class);
        Mockito.verify(mockQuery).setParameter("id", id);
    }

    @Test
    public void testBuscarPorRolNulo() {
        System.out.println("buscarPorRol nulo");
        ProcedimientoPasoDAO cut = new ProcedimientoPasoDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        cut.em = mockEM;
        
        IllegalArgumentException excepcion = assertThrows(IllegalArgumentException.class, () -> {
            cut.buscarPorRol(null);
        });
        assertEquals("El idRol no puede ser nulo", excepcion.getMessage());
        Mockito.verifyNoInteractions(mockEM);
    }

    @Test
    public void testBuscarPorRolListaVacia() {
        System.out.println("buscarPorRol lista vacía");
        UUID id = UUID.randomUUID();
        ProcedimientoPasoDAO cut = new ProcedimientoPasoDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        @SuppressWarnings("unchecked")
        TypedQuery<ProcedimientoPaso> mockQuery = Mockito.mock(TypedQuery.class);
        
        Mockito.when(mockEM.createNamedQuery(
                "ProcedimientoPaso.findByRolId", ProcedimientoPaso.class))
                .thenReturn(mockQuery);
        Mockito.when(mockQuery.getResultList())
                .thenReturn(Collections.emptyList());
        
        ProcedimientoPasoDAO espia = Mockito.spy(cut);
        Mockito.doReturn(mockEM).when(espia).getEntityManger();
        
        List<ProcedimientoPaso> resultado = espia.buscarPorRol(id);
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    public void testBuscarPorRolExcepcionServidor() {
        System.out.println("buscarPorRol lanza DAOException");
        UUID id = UUID.randomUUID();
        ProcedimientoPasoDAO cut = new ProcedimientoPasoDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        @SuppressWarnings("unchecked")
        TypedQuery<ProcedimientoPaso> mockQuery = Mockito.mock(TypedQuery.class);
        
        Mockito.when(mockEM.createNamedQuery(
                "ProcedimientoPaso.findByRolId", ProcedimientoPaso.class))
                .thenReturn(mockQuery);
        
        PersistenceException causa = new PersistenceException("Fallo simulado de BD");
        Mockito.when(mockQuery.getResultList()).thenThrow(causa);
        
        ProcedimientoPasoDAO espia = Mockito.spy(cut);
        Mockito.doReturn(mockEM).when(espia).getEntityManger();
        
        DAOException excepcion = assertThrows(DAOException.class, () -> {
            espia.buscarPorRol(id);
        });
        assertSame(causa, excepcion.getCause());
    }

    @Test
    public void testBuscarPorRolEntityManagerNulo() {
        System.out.println("buscarPorRol EntityManager nulo");
        ProcedimientoPasoDAO cut = new ProcedimientoPasoDAO();
        cut.em = null;
        
        IllegalStateException excepcion = assertThrows(IllegalStateException.class, () -> {
            cut.buscarPorRol(UUID.randomUUID());
        });
        assertTrue(excepcion.getMessage()
                .contains("EntityManager no está disponible"));
    }
}
