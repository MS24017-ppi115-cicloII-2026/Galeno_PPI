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
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ConsultaProcedimientoPaso;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.PersonaRol;

import static org.junit.jupiter.api.Assertions.*;

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

    // ---------- buscarPorConsultaProcedimiento ----------

    @Test
    public void testBuscarPorConsultaProcedimiento() {
        UUID idConsultaProcedimiento =
                UUID.randomUUID();

        ConsultaProcedimientoPasoDAO cut =
                new ConsultaProcedimientoPasoDAO();

        EntityManager mockEM =
                Mockito.mock(EntityManager.class);

        @SuppressWarnings("unchecked")
        TypedQuery<ConsultaProcedimientoPaso> mockQuery =
                Mockito.mock(TypedQuery.class);

        Mockito.when(mockEM.createNamedQuery(
                "ConsultaProcedimientoPaso.findByConsultaProcedimientoId",
                ConsultaProcedimientoPaso.class))
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
    public void testBuscarPorConsultaProcedimientoNulo() {

        ConsultaProcedimientoPasoDAO cut =
                new ConsultaProcedimientoPasoDAO();
        EntityManager mockEM =
                Mockito.mock(EntityManager.class);

        cut.em = mockEM;

        IllegalArgumentException excepcion = assertThrows(
                IllegalArgumentException.class,
                () -> cut.buscarPorConsultaProcedimiento(null)
        );

        assertEquals(
                "El idConsultaProcedimiento no puede ser nulo",
                excepcion.getMessage()
        );

        Mockito.verifyNoInteractions(mockEM);
    }

    @Test
    public void testBuscarPorConsultaProcedimientoResultadoVacio() {

        ConsultaProcedimientoPasoDAO cut =
                new ConsultaProcedimientoPasoDAO();
        EntityManager mockEM =
                Mockito.mock(EntityManager.class);

        @SuppressWarnings("unchecked")
        TypedQuery<ConsultaProcedimientoPaso> mockQuery =
                Mockito.mock(TypedQuery.class);

        Mockito.when(mockEM.createNamedQuery(
                "ConsultaProcedimientoPaso.findByConsultaProcedimientoId",
                ConsultaProcedimientoPaso.class))
                .thenReturn(mockQuery);

        Mockito.when(mockQuery.getResultList())
                .thenReturn(Collections.emptyList());

        cut.em = mockEM;

        List<ConsultaProcedimientoPaso> resultado =
                cut.buscarPorConsultaProcedimiento(UUID.randomUUID());

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    public void testBuscarPorConsultaProcedimientoExcepcionPersistencia() {

        ConsultaProcedimientoPasoDAO cut =
                new ConsultaProcedimientoPasoDAO();
        EntityManager mockEM =
                Mockito.mock(EntityManager.class);

        @SuppressWarnings("unchecked")
        TypedQuery<ConsultaProcedimientoPaso> mockQuery =
                Mockito.mock(TypedQuery.class);

        PersistenceException causa =
                new PersistenceException("fallo de base de datos");

        Mockito.when(mockEM.createNamedQuery(
                "ConsultaProcedimientoPaso.findByConsultaProcedimientoId",
                ConsultaProcedimientoPaso.class))
                .thenReturn(mockQuery);

        Mockito.when(mockQuery.getResultList())
                .thenThrow(causa);

        cut.em = mockEM;

        DAOException excepcion = assertThrows(
                DAOException.class,
                () -> cut.buscarPorConsultaProcedimiento(UUID.randomUUID())
        );

        assertSame(causa, excepcion.getCause());
    }

    @Test
    public void testBuscarPorConsultaProcedimientoEntityManagerNulo() {

        ConsultaProcedimientoPasoDAO cut =
                new ConsultaProcedimientoPasoDAO();

        IllegalStateException excepcion = assertThrows(
                IllegalStateException.class,
                () -> cut.buscarPorConsultaProcedimiento(UUID.randomUUID())
        );

        assertTrue(
                excepcion.getMessage().contains("EntityManager no está disponible")
        );
    }

    // ---------- buscarPorPersonaRol ----------

    @Test
    public void testBuscarPorPersonaRol() {
        UUID idPersonaRol =
                UUID.randomUUID();

        ConsultaProcedimientoPasoDAO cut =
                new ConsultaProcedimientoPasoDAO();

        EntityManager mockEM =
                Mockito.mock(EntityManager.class);

        @SuppressWarnings("unchecked")
        TypedQuery<ConsultaProcedimientoPaso> mockQuery =
                Mockito.mock(TypedQuery.class);

        Mockito.when(mockEM.createNamedQuery(
                "ConsultaProcedimientoPaso.findByPersonaRolId",
                ConsultaProcedimientoPaso.class))
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

    @Test
    public void testBuscarPorPersonaRolNulo() {

        ConsultaProcedimientoPasoDAO cut =
                new ConsultaProcedimientoPasoDAO();
        EntityManager mockEM =
                Mockito.mock(EntityManager.class);

        cut.em = mockEM;

        IllegalArgumentException excepcion = assertThrows(
                IllegalArgumentException.class,
                () -> cut.buscarPorPersonaRol(null)
        );

        assertEquals(
                "El idPersonaRol no puede ser nulo",
                excepcion.getMessage()
        );

        Mockito.verifyNoInteractions(mockEM);
    }

    @Test
    public void testBuscarPorPersonaRolResultadoVacio() {

        ConsultaProcedimientoPasoDAO cut =
                new ConsultaProcedimientoPasoDAO();
        EntityManager mockEM =
                Mockito.mock(EntityManager.class);

        @SuppressWarnings("unchecked")
        TypedQuery<ConsultaProcedimientoPaso> mockQuery =
                Mockito.mock(TypedQuery.class);

        Mockito.when(mockEM.createNamedQuery(
                "ConsultaProcedimientoPaso.findByPersonaRolId",
                ConsultaProcedimientoPaso.class))
                .thenReturn(mockQuery);

        Mockito.when(mockQuery.getResultList())
                .thenReturn(Collections.emptyList());

        cut.em = mockEM;

        List<ConsultaProcedimientoPaso> resultado =
                cut.buscarPorPersonaRol(UUID.randomUUID());

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    public void testBuscarPorPersonaRolExcepcionPersistencia() {

        ConsultaProcedimientoPasoDAO cut =
                new ConsultaProcedimientoPasoDAO();
        EntityManager mockEM =
                Mockito.mock(EntityManager.class);

        @SuppressWarnings("unchecked")
        TypedQuery<ConsultaProcedimientoPaso> mockQuery =
                Mockito.mock(TypedQuery.class);

        PersistenceException causa =
                new PersistenceException("fallo de base de datos");

        Mockito.when(mockEM.createNamedQuery(
                "ConsultaProcedimientoPaso.findByPersonaRolId",
                ConsultaProcedimientoPaso.class))
                .thenReturn(mockQuery);

        Mockito.when(mockQuery.getResultList())
                .thenThrow(causa);

        cut.em = mockEM;

        DAOException excepcion = assertThrows(
                DAOException.class,
                () -> cut.buscarPorPersonaRol(UUID.randomUUID())
        );

        assertSame(causa, excepcion.getCause());
    }

    @Test
    public void testBuscarPorPersonaRolEntityManagerNulo() {

        ConsultaProcedimientoPasoDAO cut =
                new ConsultaProcedimientoPasoDAO();

        IllegalStateException excepcion = assertThrows(
                IllegalStateException.class,
                () -> cut.buscarPorPersonaRol(UUID.randomUUID())
        );

        assertTrue(
                excepcion.getMessage().contains("EntityManager no está disponible")
        );
    }

    // ---------- buscarPersonasExcluyendoRol ----------

    @Test
    public void testBuscarPersonasExcluyendoRolExito() {

        ConsultaProcedimientoPasoDAO cut =
                new ConsultaProcedimientoPasoDAO();

        EntityManager mockEM = Mockito.mock(EntityManager.class);
        @SuppressWarnings("unchecked")
        TypedQuery<PersonaRol> mockQuery =
                Mockito.mock(TypedQuery.class);

        cut.em = mockEM;

        List<PersonaRol> esperados =
                Collections.singletonList(new PersonaRol());

        Mockito.when(mockEM.createNamedQuery(
                "PersonaRol.findByNombreRolSinEl",
                PersonaRol.class
        )).thenReturn(mockQuery);

        Mockito.when(mockQuery.getResultList())
                .thenReturn(esperados);

        List<PersonaRol> resultado =
                cut.buscarPersonasExcluyendoRol("Doctor");

        assertEquals(esperados, resultado);

        Mockito.verify(mockQuery)
                .setParameter("nombre", "doctor");

        Mockito.verify(mockQuery).getResultList();
    }

    @Test
    public void testBuscarPersonasExcluyendoRolConvierteAMinusculas() {

        ConsultaProcedimientoPasoDAO cut =
                new ConsultaProcedimientoPasoDAO();

        EntityManager mockEM = Mockito.mock(EntityManager.class);
        @SuppressWarnings("unchecked")
        TypedQuery<PersonaRol> mockQuery =
                Mockito.mock(TypedQuery.class);

        cut.em = mockEM;

        Mockito.when(mockEM.createNamedQuery(
                "PersonaRol.findByNombreRolSinEl",
                PersonaRol.class
        )).thenReturn(mockQuery);

        Mockito.when(mockQuery.getResultList())
                .thenReturn(Collections.emptyList());

        cut.buscarPersonasExcluyendoRol("ENFERMERA");

        Mockito.verify(mockQuery)
                .setParameter("nombre", "enfermera");
    }

    @Test
    public void testBuscarPersonasExcluyendoRolNulo() {

        ConsultaProcedimientoPasoDAO cut =
                new ConsultaProcedimientoPasoDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);

        cut.em = mockEM;

        IllegalArgumentException excepcion = assertThrows(
                IllegalArgumentException.class,
                () -> cut.buscarPersonasExcluyendoRol(null)
        );

        assertEquals(
                "El nombre del rol no puede estar vacío",
                excepcion.getMessage()
        );

        Mockito.verifyNoInteractions(mockEM);
    }

    @Test
    public void testBuscarPersonasExcluyendoRolVacio() {

        ConsultaProcedimientoPasoDAO cut =
                new ConsultaProcedimientoPasoDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);

        cut.em = mockEM;

        IllegalArgumentException excepcion = assertThrows(
                IllegalArgumentException.class,
                () -> cut.buscarPersonasExcluyendoRol("  ")
        );

        assertEquals(
                "El nombre del rol no puede estar vacío",
                excepcion.getMessage()
        );

        Mockito.verifyNoInteractions(mockEM);
    }

    @Test
    public void testBuscarPersonasExcluyendoRolResultadoVacio() {

        ConsultaProcedimientoPasoDAO cut =
                new ConsultaProcedimientoPasoDAO();

        EntityManager mockEM = Mockito.mock(EntityManager.class);
        @SuppressWarnings("unchecked")
        TypedQuery<PersonaRol> mockQuery =
                Mockito.mock(TypedQuery.class);

        cut.em = mockEM;

        Mockito.when(mockEM.createNamedQuery(
                "PersonaRol.findByNombreRolSinEl",
                PersonaRol.class
        )).thenReturn(mockQuery);

        Mockito.when(mockQuery.getResultList())
                .thenReturn(Collections.emptyList());

        List<PersonaRol> resultado =
                cut.buscarPersonasExcluyendoRol("doctor");

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    public void testBuscarPersonasExcluyendoRolExcepcionPersistencia() {

        ConsultaProcedimientoPasoDAO cut =
                new ConsultaProcedimientoPasoDAO();

        EntityManager mockEM = Mockito.mock(EntityManager.class);
        @SuppressWarnings("unchecked")
        TypedQuery<PersonaRol> mockQuery =
                Mockito.mock(TypedQuery.class);

        PersistenceException causa =
                new PersistenceException("fallo de base de datos");

        cut.em = mockEM;

        Mockito.when(mockEM.createNamedQuery(
                "PersonaRol.findByNombreRolSinEl",
                PersonaRol.class
        )).thenReturn(mockQuery);

        Mockito.when(mockQuery.getResultList())
                .thenThrow(causa);

        DAOException excepcion = assertThrows(
                DAOException.class,
                () -> cut.buscarPersonasExcluyendoRol("doctor")
        );

        assertSame(causa, excepcion.getCause());
    }

    @Test
    public void testBuscarPersonasExcluyendoRolEntityManagerNulo() {

        ConsultaProcedimientoPasoDAO cut =
                new ConsultaProcedimientoPasoDAO();

        IllegalStateException excepcion = assertThrows(
                IllegalStateException.class,
                () -> cut.buscarPersonasExcluyendoRol("doctor")
        );

        assertTrue(
                excepcion.getMessage().contains("EntityManager no está disponible")
        );
    }
}
