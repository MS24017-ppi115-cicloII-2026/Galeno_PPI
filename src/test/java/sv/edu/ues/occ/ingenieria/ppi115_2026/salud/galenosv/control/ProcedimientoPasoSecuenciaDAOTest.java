package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ProcedimientoPasoSecuencia;
import java.util.Collections;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class ProcedimientoPasoSecuenciaDAOTest {

    @Test
    public void testGetEntityManger() {
        ProcedimientoPasoSecuenciaDAO cut = new ProcedimientoPasoSecuenciaDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        cut.em = mockEM; 
        assertEquals(mockEM, cut.getEntityManger());
    }

    @Test
    public void testBuscarPorProcedimientoPaso() {
        UUID id = UUID.randomUUID();
        ProcedimientoPasoSecuenciaDAO cut = new ProcedimientoPasoSecuenciaDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        TypedQuery mockQuery = Mockito.mock(TypedQuery.class);
        
        Mockito.when(mockEM.createQuery(any(String.class), eq(ProcedimientoPasoSecuencia.class))).thenReturn(mockQuery);
        Mockito.when(mockQuery.getResultList()).thenReturn(Collections.singletonList(new ProcedimientoPasoSecuencia()));
        
        ProcedimientoPasoSecuenciaDAO espia = Mockito.spy(cut);
        Mockito.doReturn(mockEM).when(espia).getEntityManger();
        
        assertFalse(espia.buscarPorProcedimientoPaso(id).isEmpty());
    }

    @Test
    public void testBuscarPorPasoReferencia() {
        UUID id = UUID.randomUUID();
        ProcedimientoPasoSecuenciaDAO cut = new ProcedimientoPasoSecuenciaDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        TypedQuery mockQuery = Mockito.mock(TypedQuery.class);
        
        Mockito.when(mockEM.createQuery(any(String.class), eq(ProcedimientoPasoSecuencia.class))).thenReturn(mockQuery);
        Mockito.when(mockQuery.getResultList()).thenReturn(Collections.singletonList(new ProcedimientoPasoSecuencia()));
        
        ProcedimientoPasoSecuenciaDAO espia = Mockito.spy(cut);
        Mockito.doReturn(mockEM).when(espia).getEntityManger();
        
        assertFalse(espia.buscarPorPasoReferencia(id).isEmpty());
    }

    @Test
    public void testBuscarNulos() {
        ProcedimientoPasoSecuenciaDAO cut = new ProcedimientoPasoSecuenciaDAO();
        assertThrows(IllegalArgumentException.class, () -> cut.buscarPorProcedimientoPaso(null));
        assertThrows(IllegalArgumentException.class, () -> cut.buscarPorPasoReferencia(null));
    }
}