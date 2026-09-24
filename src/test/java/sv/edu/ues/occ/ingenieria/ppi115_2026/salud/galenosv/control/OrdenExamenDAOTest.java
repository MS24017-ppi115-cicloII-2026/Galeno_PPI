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
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.OrdenExamen;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class OrdenExamenDAOTest {

    @Test
    public void testGetEntityManger() {
        OrdenExamenDAO cut =
                new OrdenExamenDAO();

        EntityManager mockEM =
                Mockito.mock(EntityManager.class);

        cut.em = mockEM;

        assertEquals(
                mockEM,
                cut.getEntityManger()
        );
    }

    @Test
    public void testBuscarPorConsultaProcedimientoPasoExito() {
        UUID idConsultaProcedimientoPaso =
                UUID.randomUUID();

        OrdenExamenDAO cut =
                new OrdenExamenDAO();

        EntityManager mockEM =
                Mockito.mock(EntityManager.class);

        TypedQuery<OrdenExamen> mockQuery =
                Mockito.mock(TypedQuery.class);

        Mockito.when(mockEM.createQuery(
                any(String.class),
                eq(OrdenExamen.class)))
                .thenReturn(mockQuery);

        Mockito.when(mockQuery.getResultList())
                .thenReturn(
                        Collections.singletonList(
                                new OrdenExamen()
                        )
                );

        OrdenExamenDAO espia =
                Mockito.spy(cut);

        Mockito.doReturn(mockEM)
                .when(espia)
                .getEntityManger();

        List<OrdenExamen> resultado =
                espia.buscarPorConsultaProcedimientoPaso(
                        idConsultaProcedimientoPaso
                );

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());

        Mockito.verify(mockQuery)
                .setParameter(
                        "id",
                        idConsultaProcedimientoPaso
                );
    }

    @Test
    public void testBuscarPorConsultaProcedimientoPasoNulo() {
        OrdenExamenDAO cut =
                new OrdenExamenDAO();

        IllegalArgumentException excepcion =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> cut.buscarPorConsultaProcedimientoPaso(null)
                );

        assertEquals(
                "El idConsultaProcedimientoPaso no puede ser nulo",
                excepcion.getMessage()
        );
    }
}