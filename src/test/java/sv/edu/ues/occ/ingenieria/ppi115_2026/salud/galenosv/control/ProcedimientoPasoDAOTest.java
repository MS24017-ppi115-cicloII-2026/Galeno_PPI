package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import jakarta.persistence.EntityManager;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

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

   

    @Test
    public void testBuscarPorProcedimientoExito() {
        System.out.println("buscarPorProcedimiento éxito");
        UUID id = UUID.randomUUID();
        ProcedimientoPasoDAO cut = new ProcedimientoPasoDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        TypedQuery mockQuery = Mockito.mock(TypedQuery.class);
        
        Mockito.when(mockEM.createQuery(any(String.class), eq(ProcedimientoPaso.class))).thenReturn(mockQuery);
        Mockito.when(mockQuery.getResultList()).thenReturn(Collections.singletonList(new ProcedimientoPaso()));
        
        ProcedimientoPasoDAO espia = Mockito.spy(cut);
        Mockito.doReturn(mockEM).when(espia).getEntityManger();
        
        List<ProcedimientoPaso> resultado = espia.buscarPorProcedimiento(id);
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        Mockito.verify(mockQuery).setParameter("id", id);
    }

    @Test
    public void testBuscarPorProcedimientoNulo() {
        System.out.println("buscarPorProcedimiento nulo");
        ProcedimientoPasoDAO cut = new ProcedimientoPasoDAO();
        
        IllegalArgumentException excepcion = assertThrows(IllegalArgumentException.class, () -> {
            cut.buscarPorProcedimiento(null);
        });
        assertEquals("El idProcedimiento no puede ser nulo", excepcion.getMessage());
    }

    

    @Test
    public void testBuscarPorRolExito() {
        System.out.println("buscarPorRol éxito");
        UUID id = UUID.randomUUID();
        ProcedimientoPasoDAO cut = new ProcedimientoPasoDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        TypedQuery mockQuery = Mockito.mock(TypedQuery.class);
        
        Mockito.when(mockEM.createQuery(any(String.class), eq(ProcedimientoPaso.class))).thenReturn(mockQuery);
        Mockito.when(mockQuery.getResultList()).thenReturn(Collections.singletonList(new ProcedimientoPaso()));
        
        ProcedimientoPasoDAO espia = Mockito.spy(cut);
        Mockito.doReturn(mockEM).when(espia).getEntityManger();
        
        List<ProcedimientoPaso> resultado = espia.buscarPorRol(id);
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        Mockito.verify(mockQuery).setParameter("id", id);
    }

    @Test
    public void testBuscarPorRolNulo() {
        System.out.println("buscarPorRol nulo");
        ProcedimientoPasoDAO cut = new ProcedimientoPasoDAO();
        
        IllegalArgumentException excepcion = assertThrows(IllegalArgumentException.class, () -> {
            cut.buscarPorRol(null);
        });
        assertEquals("El idRol no puede ser nulo", excepcion.getMessage());
    }
}