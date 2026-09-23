package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.TipoExamen;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class TipoExamenDAOTest {

    @Test
    public void testCrearRegistroNulo() {
        System.out.println("crear nulo");
        TipoExamenDAO cut = new TipoExamenDAO();
        assertThrows(IllegalArgumentException.class, () -> {
            cut.crear(null);
        });
    }

    @Test
    public void testCrearExito() {
        System.out.println("crear éxito");
        TipoExamen registro = new TipoExamen(UUID.randomUUID());
        TipoExamenDAO cut = new TipoExamenDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        
        TipoExamenDAO espia = Mockito.spy(cut);
        Mockito.doReturn(mockEM).when(espia).getEntityManger();
        
        espia.crear(registro);
        Mockito.verify(mockEM, Mockito.times(1)).persist(registro);
    }
    
    @Test
    public void testBuscarExito() {
        System.out.println("buscar éxito");
        UUID id = UUID.randomUUID();
        TipoExamen esperado = new TipoExamen(id);
        TipoExamenDAO cut = new TipoExamenDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        
        Mockito.when(mockEM.find(TipoExamen.class, id)).thenReturn(esperado);
        
        TipoExamenDAO espia = Mockito.spy(cut);
        Mockito.doReturn(mockEM).when(espia).getEntityManger();
        
        TipoExamen resultado = espia.buscar(id);
        assertEquals(esperado, resultado);
    }

    @Test
    public void testEliminarNoExiste() {
        System.out.println("eliminar no existe");
        UUID id = UUID.randomUUID();
        TipoExamenDAO cut = new TipoExamenDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        
        Mockito.when(mockEM.find(TipoExamen.class, id)).thenReturn(null);
        
        TipoExamenDAO espia = Mockito.spy(cut);
        Mockito.doReturn(mockEM).when(espia).getEntityManger();
        
        assertThrows(IllegalArgumentException.class, () -> {
            espia.eliminar(id);
        });
    }

    @Test
    public void testFindRangeParametrosInvalidos() {
        System.out.println("findRange parámetros inválidos");
        TipoExamenDAO cut = new TipoExamenDAO();
        assertThrows(IllegalArgumentException.class, () -> {
            cut.findRange(-1, 0); // first negativo y max cero
        });
    }

    @Test
    public void testBuscarPorNombreExito() {
        System.out.println("buscarPorNombre éxito");
        String nombre = "Sangre";
        TipoExamenDAO cut = new TipoExamenDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        TypedQuery mockQuery = Mockito.mock(TypedQuery.class);
        
        Mockito.when(mockEM.createQuery(any(String.class), eq(TipoExamen.class))).thenReturn(mockQuery);
        Mockito.when(mockQuery.getResultList()).thenReturn(Collections.singletonList(new TipoExamen()));
        
        TipoExamenDAO espia = Mockito.spy(cut);
        Mockito.doReturn(mockEM).when(espia).getEntityManger();
        
        List<TipoExamen> resultado = espia.buscarPorNombre(nombre);
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        Mockito.verify(mockQuery).setParameter("nombre", "%" + nombre + "%");
    }
    @Test
    public void testGetEntityManger() {
        System.out.println("getEntityManger");
        TipoExamenDAO cut = new TipoExamenDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        
        // Asignamos manualmente el mock a la variable (simulando al servidor)
        cut.em = mockEM; 
        
        // Verificamos que el método devuelva la variable asignada
        assertEquals(mockEM, cut.getEntityManger());
    }

    @Test
    public void testBuscarPorNombreNulo() {
        System.out.println("buscarPorNombre nulo");
        TipoExamenDAO cut = new TipoExamenDAO();
        
        IllegalArgumentException excepcion = assertThrows(IllegalArgumentException.class, () -> {
            cut.buscarPorNombre(null);
        });
        assertEquals("El nombre no puede ser nulo o vacío", excepcion.getMessage());
    }

    @Test
    public void testBuscarPorNombreVacio() {
        System.out.println("buscarPorNombre vacio");
        TipoExamenDAO cut = new TipoExamenDAO();
        
        IllegalArgumentException excepcion = assertThrows(IllegalArgumentException.class, () -> {
            cut.buscarPorNombre("   "); // Espacios en blanco
        });
        assertEquals("El nombre no puede ser nulo o vacío", excepcion.getMessage());
    }
}