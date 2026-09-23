package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ExamenTipoExamen;
import java.util.Collections;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

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
        TypedQuery mockQuery = Mockito.mock(TypedQuery.class);
        
        Mockito.when(mockEM.createQuery(any(String.class), eq(ExamenTipoExamen.class))).thenReturn(mockQuery);
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
        TypedQuery mockQuery = Mockito.mock(TypedQuery.class);
        
        Mockito.when(mockEM.createQuery(any(String.class), eq(ExamenTipoExamen.class))).thenReturn(mockQuery);
        Mockito.when(mockQuery.getResultList()).thenReturn(Collections.singletonList(new ExamenTipoExamen()));
        
        ExamenTipoExamenDAO espia = Mockito.spy(cut);
        Mockito.doReturn(mockEM).when(espia).getEntityManger();
        
        assertFalse(espia.buscarPorTipoExamen(id).isEmpty());
        Mockito.verify(mockQuery).setParameter("id", id);
    }

    @Test
    public void testBuscarNulos() {
        ExamenTipoExamenDAO cut = new ExamenTipoExamenDAO();
        assertThrows(IllegalArgumentException.class, () -> cut.buscarPorExamen(null));
        assertThrows(IllegalArgumentException.class, () -> cut.buscarPorTipoExamen(null));
    }
}