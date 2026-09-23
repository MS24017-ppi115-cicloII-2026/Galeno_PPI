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
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ConsultaProcedimientoPaso;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class ConsultaProcedimientoPasoDAOTest {

    @Test
    public void testGetEntityManger() {
        ConsultaProcedimientoPasoDAO cut =
                new ConsultaProcedimientoPasoDAO();

        EntityManager mockEM =
                Mockito.mock(EntityManager.class);

        cut.em = mockEM;

        assertEquals(mockEM, cut.getEntityManger());
    }

    @Test
    public void testBuscarPorConsultaProcedimiento() {
        UUID idConsultaProcedimiento =
                UUID.randomUUID();

        ConsultaProcedimientoPasoDAO cut =
                new ConsultaProcedimientoPasoDAO();

        EntityManager mockEM =
                Mockito.mock(EntityManager.class);

        TypedQuery<ConsultaProcedimientoPaso> mockQuery =
                Mockito.mock(TypedQuery.class);

        Mockito.when(mockEM.createQuery(
                any(String.class),
                eq(ConsultaProcedimientoPaso.class)))
                .thenReturn(mockQuery);

        Mockito.when(mockQuery.getResultList())
                .thenReturn(
                        Collections.singletonList(
                                new ConsultaProcedimientoPaso()
                        )
                );

        ConsultaProcedimientoPasoDAO espia =
                Mockito.spy(cut);

        Mockito.doReturn(mockEM)
                .when(espia)
                .getEntityManger();

        List<ConsultaProcedimientoPaso> resultado =
                espia.buscarPorConsultaProcedimiento(
                        idConsultaProcedimiento
                );

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());

        Mockito.verify(mockQuery)
                .setParameter(
                        "id",
                        idConsultaProcedimiento
                );
    }

    @Test
    public void testBuscarPorPersonaRol() {
        UUID idPersonaRol =
                UUID.randomUUID();

        ConsultaProcedimientoPasoDAO cut =
                new ConsultaProcedimientoPasoDAO();

        EntityManager mockEM =
                Mockito.mock(EntityManager.class);

        TypedQuery<ConsultaProcedimientoPaso> mockQuery =
                Mockito.mock(TypedQuery.class);

        Mockito.when(mockEM.createQuery(
                any(String.class),
                eq(ConsultaProcedimientoPaso.class)))
                .thenReturn(mockQuery);

        Mockito.when(mockQuery.getResultList())
                .thenReturn(
                        Collections.singletonList(
                                new ConsultaProcedimientoPaso()
                        )
                );

        ConsultaProcedimientoPasoDAO espia =
                Mockito.spy(cut);

        Mockito.doReturn(mockEM)
                .when(espia)
                .getEntityManger();

        List<ConsultaProcedimientoPaso> resultado =
                espia.buscarPorPersonaRol(idPersonaRol);

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());

        Mockito.verify(mockQuery)
                .setParameter(
                        "id",
                        idPersonaRol
                );
    }
}
