package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Examen;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class ExamenDAOTest {

    @Test
    public void testGetEntityManger() {
        System.out.println("getEntityManger");
        ExamenDAO cut = new ExamenDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        
        cut.em = mockEM; 
        assertEquals(mockEM, cut.getEntityManger());
    }

    @Test
    public void testBuscarPorNombreExito() {
        System.out.println("buscarPorNombre éxito");
        String nombre = "Hemograma";
        ExamenDAO cut = new ExamenDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        TypedQuery mockQuery = Mockito.mock(TypedQuery.class);
        
        Mockito.when(mockEM.createQuery(any(String.class), eq(Examen.class))).thenReturn(mockQuery);
        Mockito.when(mockQuery.getResultList()).thenReturn(Collections.singletonList(new Examen()));
        
        ExamenDAO espia = Mockito.spy(cut);
        Mockito.doReturn(mockEM).when(espia).getEntityManger();
        
        List<Examen> resultado = espia.buscarPorNombre(nombre);
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        Mockito.verify(mockQuery).setParameter("nombre", "%" + nombre + "%");
    }

    @Test
    public void testBuscarPorNombreNulo() {
        System.out.println("buscarPorNombre nulo");
        ExamenDAO cut = new ExamenDAO();
        
        IllegalArgumentException excepcion = assertThrows(IllegalArgumentException.class, () -> {
            cut.buscarPorNombre(null);
        });
        assertEquals("El nombre no puede ser nulo o vacío", excepcion.getMessage());
    }

    @Test
    public void testBuscarPorNombreVacio() {
        System.out.println("buscarPorNombre vacio");
        ExamenDAO cut = new ExamenDAO();
        
        IllegalArgumentException excepcion = assertThrows(IllegalArgumentException.class, () -> {
            cut.buscarPorNombre("   ");
        });
        assertEquals("El nombre no puede ser nulo o vacío", excepcion.getMessage());
    }
}