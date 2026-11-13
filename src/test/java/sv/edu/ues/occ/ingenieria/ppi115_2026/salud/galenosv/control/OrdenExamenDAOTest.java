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
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.OrdenExamen;

import static org.junit.jupiter.api.Assertions.*;

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

        @SuppressWarnings("unchecked")
        TypedQuery<OrdenExamen> mockQuery =
                Mockito.mock(TypedQuery.class);

        Mockito.when(mockEM.createNamedQuery(
                "OrdenExamen.findByConsultaProcedimientoPasoId",
                OrdenExamen.class))
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

        Mockito.verify(mockEM)
                .createNamedQuery(
                        "OrdenExamen.findByConsultaProcedimientoPasoId",
                        OrdenExamen.class);
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
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        cut.em = mockEM;

        IllegalArgumentException excepcion =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> cut.buscarPorConsultaProcedimientoPaso(null)
                );

        assertEquals(
                "El idConsultaProcedimientoPaso no puede ser nulo",
                excepcion.getMessage()
        );
        Mockito.verifyNoInteractions(mockEM);
    }

    @Test
    public void testBuscarPorConsultaProcedimientoPasoListaVacia() {
        UUID idConsultaProcedimientoPaso =
                UUID.randomUUID();

        OrdenExamenDAO cut =
                new OrdenExamenDAO();

        EntityManager mockEM =
                Mockito.mock(EntityManager.class);

        @SuppressWarnings("unchecked")
        TypedQuery<OrdenExamen> mockQuery =
                Mockito.mock(TypedQuery.class);

        Mockito.when(mockEM.createNamedQuery(
                "OrdenExamen.findByConsultaProcedimientoPasoId",
                OrdenExamen.class))
                .thenReturn(mockQuery);

        Mockito.when(mockQuery.getResultList())
                .thenReturn(Collections.emptyList());

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
        assertTrue(resultado.isEmpty());
    }

    @Test
    public void testBuscarPorConsultaProcedimientoPasoExcepcionServidor() {
        UUID idConsultaProcedimientoPaso =
                UUID.randomUUID();

        OrdenExamenDAO cut =
                new OrdenExamenDAO();

        EntityManager mockEM =
                Mockito.mock(EntityManager.class);

        @SuppressWarnings("unchecked")
        TypedQuery<OrdenExamen> mockQuery =
                Mockito.mock(TypedQuery.class);

        Mockito.when(mockEM.createNamedQuery(
                "OrdenExamen.findByConsultaProcedimientoPasoId",
                OrdenExamen.class))
                .thenReturn(mockQuery);

        PersistenceException causa =
                new PersistenceException("Fallo simulado de BD");

        Mockito.when(mockQuery.getResultList())
                .thenThrow(causa);

        OrdenExamenDAO espia =
                Mockito.spy(cut);

        Mockito.doReturn(mockEM)
                .when(espia)
                .getEntityManger();

        DAOException excepcion =
                assertThrows(
                        DAOException.class,
                        () -> espia.buscarPorConsultaProcedimientoPaso(
                                idConsultaProcedimientoPaso)
                );

        assertSame(causa, excepcion.getCause());
    }

    @Test
    public void testBuscarPorConsultaProcedimientoPasoEntityManagerNulo() {
        OrdenExamenDAO cut = new OrdenExamenDAO();
        cut.em = null;

        IllegalStateException excepcion =
                assertThrows(
                        IllegalStateException.class,
                        () -> cut.buscarPorConsultaProcedimientoPaso(
                                UUID.randomUUID())
                );

        assertTrue(excepcion.getMessage()
                .contains("EntityManager no está disponible"));
    }
}
