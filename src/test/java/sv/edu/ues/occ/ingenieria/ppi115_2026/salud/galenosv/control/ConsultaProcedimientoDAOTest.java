package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
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
        @SuppressWarnings("unchecked")
        TypedQuery<ConsultaProcedimiento> mockQuery =
                Mockito.mock(TypedQuery.class);

        Mockito.when(mockEM.createNamedQuery(
                "ConsultaProcedimiento.findByConsultaId",
                ConsultaProcedimiento.class))
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
        EntityManager mockEM = Mockito.mock(EntityManager.class);

        cut.em = mockEM;

        IllegalArgumentException excepcion =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> cut.buscarPorConsulta(null)
                );

        assertEquals(
                "El idConsulta no puede ser nulo",
                excepcion.getMessage()
        );

        Mockito.verifyNoInteractions(mockEM);
    }

    @Test
    public void testBuscarPorConsultaResultadoVacio() {
        ConsultaProcedimientoDAO cut =
                new ConsultaProcedimientoDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        @SuppressWarnings("unchecked")
        TypedQuery<ConsultaProcedimiento> mockQuery =
                Mockito.mock(TypedQuery.class);

        Mockito.when(mockEM.createNamedQuery(
                "ConsultaProcedimiento.findByConsultaId",
                ConsultaProcedimiento.class))
                .thenReturn(mockQuery);

        Mockito.when(mockQuery.getResultList())
                .thenReturn(Collections.emptyList());

        cut.em = mockEM;

        List<ConsultaProcedimiento> resultado =
                cut.buscarPorConsulta(UUID.randomUUID());

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    public void testBuscarPorConsultaExcepcionPersistencia() {
        ConsultaProcedimientoDAO cut =
                new ConsultaProcedimientoDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        @SuppressWarnings("unchecked")
        TypedQuery<ConsultaProcedimiento> mockQuery =
                Mockito.mock(TypedQuery.class);

        PersistenceException causa =
                new PersistenceException("fallo de base de datos");

        Mockito.when(mockEM.createNamedQuery(
                "ConsultaProcedimiento.findByConsultaId",
                ConsultaProcedimiento.class))
                .thenReturn(mockQuery);

        Mockito.when(mockQuery.getResultList())
                .thenThrow(causa);

        cut.em = mockEM;

        DAOException excepcion = assertThrows(
                DAOException.class,
                () -> cut.buscarPorConsulta(UUID.randomUUID())
        );

        assertSame(causa, excepcion.getCause());
    }

    @Test
    public void testBuscarPorConsultaEntityManagerNulo() {
        ConsultaProcedimientoDAO cut =
                new ConsultaProcedimientoDAO();

        IllegalStateException excepcion = assertThrows(
                IllegalStateException.class,
                () -> cut.buscarPorConsulta(UUID.randomUUID())
        );

        assertTrue(
                excepcion.getMessage().contains("EntityManager no está disponible")
        );
    }
}
