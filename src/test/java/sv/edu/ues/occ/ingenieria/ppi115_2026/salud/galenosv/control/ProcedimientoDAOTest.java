package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Procedimiento;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class ProcedimientoDAOTest {

    @Test
    public void testGetEntityManger() {
        System.out.println("getEntityManger");
        ProcedimientoDAO cut = new ProcedimientoDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        
        cut.em = mockEM; 
        assertEquals(mockEM, cut.getEntityManger());
    }

    @Test
    public void testBuscarPorNombreExito() {
        System.out.println("buscarPorNombre éxito");
        String nombre = "Cirugía";
        ProcedimientoDAO cut = new ProcedimientoDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        TypedQuery mockQuery = Mockito.mock(TypedQuery.class);
        
        Mockito.when(mockEM.createQuery(any(String.class), eq(Procedimiento.class))).thenReturn(mockQuery);
        Mockito.when(mockQuery.getResultList()).thenReturn(Collections.singletonList(new Procedimiento()));
        
        ProcedimientoDAO espia = Mockito.spy(cut);
        Mockito.doReturn(mockEM).when(espia).getEntityManger();
        
        List<Procedimiento> resultado = espia.buscarPorNombre(nombre);
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        Mockito.verify(mockQuery).setParameter("nombre", "%" + nombre + "%");
    }

    @Test
    public void testBuscarPorNombreNulo() {
        System.out.println("buscarPorNombre nulo");
        ProcedimientoDAO cut = new ProcedimientoDAO();
        
        IllegalArgumentException excepcion = assertThrows(IllegalArgumentException.class, () -> {
            cut.buscarPorNombre(null);
        });
        assertEquals("El nombre no puede ser nulo o vacío", excepcion.getMessage());
    }

    @Test
    public void testBuscarPorNombreVacio() {
        System.out.println("buscarPorNombre vacio");
        ProcedimientoDAO cut = new ProcedimientoDAO();
        
        IllegalArgumentException excepcion = assertThrows(IllegalArgumentException.class, () -> {
            cut.buscarPorNombre("   ");
        });
        assertEquals("El nombre no puede ser nulo o vacío", excepcion.getMessage());
    }
    @Test
    public void testCrearNulo() {
        System.out.println("crear nulo");
        ProcedimientoDAO cut = new ProcedimientoDAO();
        assertThrows(IllegalArgumentException.class, () -> cut.crear(null));
    }

    @Test
    public void testActualizarNulo() {
        System.out.println("actualizar nulo");
        ProcedimientoDAO cut = new ProcedimientoDAO();
        assertThrows(IllegalArgumentException.class, () -> cut.actualizar(null));
    }

    @Test
    public void testBuscarNulo() {
        System.out.println("buscar nulo");
        ProcedimientoDAO cut = new ProcedimientoDAO();
        assertThrows(IllegalArgumentException.class, () -> cut.buscar(null));
    }

    @Test
    public void testEliminarNulo() {
        System.out.println("eliminar nulo");
        ProcedimientoDAO cut = new ProcedimientoDAO();
        assertThrows(IllegalArgumentException.class, () -> cut.eliminar(null));
    }

    @Test
    public void testFindRangeParametrosInvalidos() {
        System.out.println("findRange inválido");
        ProcedimientoDAO cut = new ProcedimientoDAO();
        assertThrows(IllegalArgumentException.class, () -> cut.findRange(-1, -5));
    }

    // --- PRUEBAS DE EXCEPCIONES DEL SERVIDOR (CATCH BLOCKS) ---

    @Test
    public void testCrearExcepcionServidor() {
        System.out.println("crear lanza IllegalStateException");
        Procedimiento registro = new Procedimiento(java.util.UUID.randomUUID());
        ProcedimientoDAO cut = new ProcedimientoDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        ProcedimientoDAO espia = Mockito.spy(cut);
        
        Mockito.doReturn(mockEM).when(espia).getEntityManger();
        // Simulamos que al hacer persist, el EntityManager falla y lanza un error
        Mockito.doThrow(new RuntimeException("Fallo simulado de BD")).when(mockEM).persist(registro);
        
        assertThrows(IllegalStateException.class, () -> espia.crear(registro));
    }

    @Test
    public void testBuscarExcepcionServidor() {
        System.out.println("buscar lanza IllegalStateException");
        java.util.UUID id = java.util.UUID.randomUUID();
        ProcedimientoDAO cut = new ProcedimientoDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        ProcedimientoDAO espia = Mockito.spy(cut);
        
        Mockito.doReturn(mockEM).when(espia).getEntityManger();
        // Simulamos que al hacer find, falla
        Mockito.doThrow(new RuntimeException("Fallo simulado de BD")).when(mockEM).find(Procedimiento.class, id);
        
        assertThrows(IllegalStateException.class, () -> espia.buscar(id));
    }
}