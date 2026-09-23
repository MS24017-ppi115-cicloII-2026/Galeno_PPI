package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ProcedimientoPasoExamen;
import java.util.Collections;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class ProcedimientoPasoExamenDAOTest {

    @Test
    public void testGetEntityManger() {
        ProcedimientoPasoExamenDAO cut = new ProcedimientoPasoExamenDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        cut.em = mockEM; 
        assertEquals(mockEM, cut.getEntityManger());
    }

    @Test
    public void testBuscarPorExamen() {
        UUID id = UUID.randomUUID();
        ProcedimientoPasoExamenDAO cut = new ProcedimientoPasoExamenDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        TypedQuery mockQuery = Mockito.mock(TypedQuery.class);
        
        Mockito.when(mockEM.createQuery(any(String.class), eq(ProcedimientoPasoExamen.class))).thenReturn(mockQuery);
        Mockito.when(mockQuery.getResultList()).thenReturn(Collections.singletonList(new ProcedimientoPasoExamen()));
        
        ProcedimientoPasoExamenDAO espia = Mockito.spy(cut);
        Mockito.doReturn(mockEM).when(espia).getEntityManger();
        
        assertFalse(espia.buscarPorExamen(id).isEmpty());
    }

    @Test
    public void testBuscarPorProcedimientoPaso() {
        UUID id = UUID.randomUUID();
        ProcedimientoPasoExamenDAO cut = new ProcedimientoPasoExamenDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        TypedQuery mockQuery = Mockito.mock(TypedQuery.class);
        
        Mockito.when(mockEM.createQuery(any(String.class), eq(ProcedimientoPasoExamen.class))).thenReturn(mockQuery);
        Mockito.when(mockQuery.getResultList()).thenReturn(Collections.singletonList(new ProcedimientoPasoExamen()));
        
        ProcedimientoPasoExamenDAO espia = Mockito.spy(cut);
        Mockito.doReturn(mockEM).when(espia).getEntityManger();
        
        assertFalse(espia.buscarPorProcedimientoPaso(id).isEmpty());
    }

    @Test
    public void testBuscarNulos() {
        ProcedimientoPasoExamenDAO cut = new ProcedimientoPasoExamenDAO();
        assertThrows(IllegalArgumentException.class, () -> cut.buscarPorExamen(null));
        assertThrows(IllegalArgumentException.class, () -> cut.buscarPorProcedimientoPaso(null));
    }
}