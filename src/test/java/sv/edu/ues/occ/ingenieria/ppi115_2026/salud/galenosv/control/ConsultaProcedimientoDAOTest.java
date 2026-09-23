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
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ConsultaProcedimiento;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class ConsultaProcedimientoDAOTest {

    @Test
    public void testGetEntityManger() {
        ConsultaProcedimientoDAO cut = new ConsultaProcedimientoDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);

        cut.em = mockEM;

        assertEquals(mockEM, cut.getEntityManger());
    }

    @Test
    public void testBuscarPorConsultaExito() {
        UUID idConsulta = UUID.randomUUID();

        ConsultaProcedimientoDAO cut = new ConsultaProcedimientoDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        TypedQuery<ConsultaProcedimiento> mockQuery =
                Mockito.mock(TypedQuery.class);

        Mockito.when(mockEM.createQuery(
                any(String.class),
                eq(ConsultaProcedimiento.class)))
                .thenReturn(mockQuery);

        Mockito.when(mockQuery.getResultList())
                .thenReturn(
                        Collections.singletonList(
                                new ConsultaProcedimiento()
                        )
                );

        ConsultaProcedimientoDAO espia =
                Mockito.spy(cut);

        Mockito.doReturn(mockEM)
                .when(espia)
                .getEntityManger();

        List<ConsultaProcedimiento> resultado =
                espia.buscarPorConsulta(idConsulta);

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());

        Mockito.verify(mockQuery)
                .setParameter("id", idConsulta);
    }

    @Test
    public void testBuscarPorConsultaNulo() {
        ConsultaProcedimientoDAO cut =
                new ConsultaProcedimientoDAO();

        IllegalArgumentException excepcion =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> cut.buscarPorConsulta(null)
                );

        assertEquals(
                "El idConsulta no puede ser nulo",
                excepcion.getMessage()
        );
    }
}