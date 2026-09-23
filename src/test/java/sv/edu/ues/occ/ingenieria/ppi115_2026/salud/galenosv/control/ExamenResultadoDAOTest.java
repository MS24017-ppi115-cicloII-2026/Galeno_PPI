package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ExamenResultado;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class ExamenResultadoDAOTest {

    @Test
    public void testGetEntityManger() {
        ExamenResultadoDAO cut =
                new ExamenResultadoDAO();

        EntityManager mockEM =
                Mockito.mock(EntityManager.class);

        cut.em = mockEM;

        assertEquals(
                mockEM,
                cut.getEntityManger()
        );
    }

    @Test
    public void testBuscarPorOrdenExamenExito() {
        UUID idOrdenExamen =
                UUID.randomUUID();

        ExamenResultadoDAO cut =
                new ExamenResultadoDAO();

        EntityManager mockEM =
                Mockito.mock(EntityManager.class);

        TypedQuery<ExamenResultado> mockQuery =
                Mockito.mock(TypedQuery.class);

        Mockito.when(mockEM.createQuery(
                any(String.class),
                eq(ExamenResultado.class)))
                .thenReturn(mockQuery);

        Mockito.when(mockQuery.getResultList())
                .thenReturn(
                        Collections.singletonList(
                                new ExamenResultado()
                        )
                );

        ExamenResultadoDAO espia =
                Mockito.spy(cut);

        Mockito.doReturn(mockEM)
                .when(espia)
                .getEntityManger();

        List<ExamenResultado> resultado =
                espia.buscarPorOrdenExamen(
                        idOrdenExamen
                );

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());

        Mockito.verify(mockQuery)
                .setParameter(
                        "id",
                        idOrdenExamen
                );
    }

    @Test
    public void testBuscarPorOrdenExamenNulo() {
        ExamenResultadoDAO cut =
                new ExamenResultadoDAO();

        IllegalArgumentException excepcion =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> cut.buscarPorOrdenExamen(null)
                );

        assertEquals(
                "El idOrdenExamen no puede ser nulo",
                excepcion.getMessage()
        );
    }
}