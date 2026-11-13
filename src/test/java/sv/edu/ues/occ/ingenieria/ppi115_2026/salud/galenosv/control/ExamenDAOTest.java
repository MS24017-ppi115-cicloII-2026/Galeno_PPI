package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Examen;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
        @SuppressWarnings("unchecked")
        TypedQuery<Examen> mockQuery = Mockito.mock(TypedQuery.class);
        
        Mockito.when(mockEM.createNamedQuery("Examen.findByNombreLike", Examen.class)).thenReturn(mockQuery);
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
        EntityManager mockEM = Mockito.mock(EntityManager.class);

        cut.em = mockEM;

        IllegalArgumentException excepcion = assertThrows(IllegalArgumentException.class, () -> {
            cut.buscarPorNombre(null);
        });
        assertEquals("El nombre no puede ser nulo o vacío", excepcion.getMessage());

        Mockito.verifyNoInteractions(mockEM);
    }

    @Test
    public void testBuscarPorNombreVacio() {
        System.out.println("buscarPorNombre vacio");
        ExamenDAO cut = new ExamenDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);

        cut.em = mockEM;

        IllegalArgumentException excepcion = assertThrows(IllegalArgumentException.class, () -> {
            cut.buscarPorNombre("   ");
        });
        assertEquals("El nombre no puede ser nulo o vacío", excepcion.getMessage());

        Mockito.verifyNoInteractions(mockEM);
    }

    @Test
    public void testBuscarPorNombreResultadoVacio() {
        System.out.println("buscarPorNombre resultado vacío");
        ExamenDAO cut = new ExamenDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        @SuppressWarnings("unchecked")
        TypedQuery<Examen> mockQuery = Mockito.mock(TypedQuery.class);

        Mockito.when(mockEM.createNamedQuery("Examen.findByNombreLike", Examen.class)).thenReturn(mockQuery);
        Mockito.when(mockQuery.getResultList()).thenReturn(Collections.emptyList());

        cut.em = mockEM;

        List<Examen> resultado = cut.buscarPorNombre("inexistente");
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        Mockito.verify(mockQuery).setParameter("nombre", "%inexistente%");
    }

    @Test
    public void testBuscarPorNombreExcepcionPersistencia() {
        System.out.println("buscarPorNombre excepción de persistencia");
        ExamenDAO cut = new ExamenDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        @SuppressWarnings("unchecked")
        TypedQuery<Examen> mockQuery = Mockito.mock(TypedQuery.class);

        PersistenceException causa = new PersistenceException("fallo de base de datos");

        Mockito.when(mockEM.createNamedQuery("Examen.findByNombreLike", Examen.class)).thenReturn(mockQuery);
        Mockito.when(mockQuery.getResultList()).thenThrow(causa);

        cut.em = mockEM;

        DAOException excepcion = assertThrows(DAOException.class, () -> {
            cut.buscarPorNombre("Hemograma");
        });
        assertSame(causa, excepcion.getCause());
    }

    @Test
    public void testBuscarPorNombreEntityManagerNulo() {
        System.out.println("buscarPorNombre EntityManager nulo");
        ExamenDAO cut = new ExamenDAO();

        IllegalStateException excepcion = assertThrows(IllegalStateException.class, () -> {
            cut.buscarPorNombre("Hemograma");
        });
        assertTrue(excepcion.getMessage().contains("EntityManager no está disponible"));
    }
}
