package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Consulta;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.PersonaRol;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ConsultaDAOTest {

    @Test
    public void testGetEntityManger() {
        ConsultaDAO cut = new ConsultaDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);

        cut.em = mockEM;

        assertEquals(mockEM, cut.getEntityManger());
    }

    // ---------- buscarPorPersonaRol ----------

    @Test
    public void testBuscarPorPersonaRolExito() {
        UUID idPersonaRol = UUID.randomUUID();

        ConsultaDAO cut = new ConsultaDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        @SuppressWarnings("unchecked")
        TypedQuery<Consulta> mockQuery = Mockito.mock(TypedQuery.class);

        Mockito.when(mockEM.createNamedQuery(
                "Consulta.findByPersonaRolId",
                Consulta.class))
                .thenReturn(mockQuery);

        Mockito.when(mockQuery.getResultList())
                .thenReturn(Collections.singletonList(new Consulta()));

        ConsultaDAO espia = Mockito.spy(cut);
        Mockito.doReturn(mockEM)
                .when(espia)
                .getEntityManger();

        List<Consulta> resultado =
                espia.buscarPorPersonaRol(idPersonaRol);

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());

        Mockito.verify(mockQuery)
                .setParameter("id", idPersonaRol);
    }

    @Test
    public void testBuscarPorPersonaRolNulo() {
        ConsultaDAO cut = new ConsultaDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);

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
        ConsultaDAO cut = new ConsultaDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        @SuppressWarnings("unchecked")
        TypedQuery<Consulta> mockQuery = Mockito.mock(TypedQuery.class);

        Mockito.when(mockEM.createNamedQuery(
                "Consulta.findByPersonaRolId",
                Consulta.class))
                .thenReturn(mockQuery);

        Mockito.when(mockQuery.getResultList())
                .thenReturn(Collections.emptyList());

        cut.em = mockEM;

        List<Consulta> resultado =
                cut.buscarPorPersonaRol(UUID.randomUUID());

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    public void testBuscarPorPersonaRolExcepcionPersistencia() {
        ConsultaDAO cut = new ConsultaDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        @SuppressWarnings("unchecked")
        TypedQuery<Consulta> mockQuery = Mockito.mock(TypedQuery.class);

        PersistenceException causa =
                new PersistenceException("fallo de base de datos");

        Mockito.when(mockEM.createNamedQuery(
                "Consulta.findByPersonaRolId",
                Consulta.class))
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
        ConsultaDAO cut = new ConsultaDAO();

        IllegalStateException excepcion = assertThrows(
                IllegalStateException.class,
                () -> cut.buscarPorPersonaRol(UUID.randomUUID())
        );

        assertTrue(
                excepcion.getMessage().contains("EntityManager no está disponible")
        );
    }

    // ---------- buscarPersonasPorNombreRol ----------

    @Test
    public void testBuscarPersonasPorNombreRolExito() {

        ConsultaDAO cut = new ConsultaDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        @SuppressWarnings("unchecked")
        TypedQuery<PersonaRol> mockQuery =
                Mockito.mock(TypedQuery.class);

        cut.em = mockEM;

        List<PersonaRol> esperados =
                Collections.singletonList(new PersonaRol());

        Mockito.when(mockEM.createNamedQuery(
                "PersonaRol.findByNombreRolConRelaciones",
                PersonaRol.class
        )).thenReturn(mockQuery);

        Mockito.when(mockQuery.getResultList())
                .thenReturn(esperados);

        List<PersonaRol> resultado =
                cut.buscarPersonasPorNombreRol("Doctor");

        assertEquals(esperados, resultado);

        Mockito.verify(mockQuery)
                .setParameter("nombre", "doctor");

        Mockito.verify(mockQuery).getResultList();
    }

    @Test
    public void testBuscarPersonasPorNombreRolConvierteAMinusculas() {

        ConsultaDAO cut = new ConsultaDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        @SuppressWarnings("unchecked")
        TypedQuery<PersonaRol> mockQuery =
                Mockito.mock(TypedQuery.class);

        cut.em = mockEM;

        Mockito.when(mockEM.createNamedQuery(
                "PersonaRol.findByNombreRolConRelaciones",
                PersonaRol.class
        )).thenReturn(mockQuery);

        Mockito.when(mockQuery.getResultList())
                .thenReturn(Collections.emptyList());

        cut.buscarPersonasPorNombreRol("ADMINISTRADOR");

        Mockito.verify(mockQuery)
                .setParameter("nombre", "administrador");
    }

    @Test
    public void testBuscarPersonasPorNombreRolNulo() {

        ConsultaDAO cut = new ConsultaDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);

        cut.em = mockEM;

        IllegalArgumentException excepcion = assertThrows(
                IllegalArgumentException.class,
                () -> cut.buscarPersonasPorNombreRol(null)
        );

        assertEquals(
                "El nombre del rol no puede estar vacío",
                excepcion.getMessage()
        );

        Mockito.verifyNoInteractions(mockEM);
    }

    @Test
    public void testBuscarPersonasPorNombreRolVacio() {

        ConsultaDAO cut = new ConsultaDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);

        cut.em = mockEM;

        IllegalArgumentException excepcion = assertThrows(
                IllegalArgumentException.class,
                () -> cut.buscarPersonasPorNombreRol("   ")
        );

        assertEquals(
                "El nombre del rol no puede estar vacío",
                excepcion.getMessage()
        );

        Mockito.verifyNoInteractions(mockEM);
    }

    @Test
    public void testBuscarPersonasPorNombreRolResultadoVacio() {

        ConsultaDAO cut = new ConsultaDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        @SuppressWarnings("unchecked")
        TypedQuery<PersonaRol> mockQuery =
                Mockito.mock(TypedQuery.class);

        cut.em = mockEM;

        Mockito.when(mockEM.createNamedQuery(
                "PersonaRol.findByNombreRolConRelaciones",
                PersonaRol.class
        )).thenReturn(mockQuery);

        Mockito.when(mockQuery.getResultList())
                .thenReturn(Collections.emptyList());

        List<PersonaRol> resultado =
                cut.buscarPersonasPorNombreRol("doctor");

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    public void testBuscarPersonasPorNombreRolExcepcionPersistencia() {

        ConsultaDAO cut = new ConsultaDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        @SuppressWarnings("unchecked")
        TypedQuery<PersonaRol> mockQuery =
                Mockito.mock(TypedQuery.class);

        PersistenceException causa =
                new PersistenceException("fallo de base de datos");

        cut.em = mockEM;

        Mockito.when(mockEM.createNamedQuery(
                "PersonaRol.findByNombreRolConRelaciones",
                PersonaRol.class
        )).thenReturn(mockQuery);

        Mockito.when(mockQuery.getResultList())
                .thenThrow(causa);

        DAOException excepcion = assertThrows(
                DAOException.class,
                () -> cut.buscarPersonasPorNombreRol("doctor")
        );

        assertSame(causa, excepcion.getCause());
    }

    @Test
    public void testBuscarPersonasPorNombreRolEntityManagerNulo() {

        ConsultaDAO cut = new ConsultaDAO();

        IllegalStateException excepcion = assertThrows(
                IllegalStateException.class,
                () -> cut.buscarPersonasPorNombreRol("doctor")
        );

        assertTrue(
                excepcion.getMessage().contains("EntityManager no está disponible")
        );
    }

    // ---------- buscarPacientePorCriterio ----------

    @Test
    public void testBuscarPacientePorCriterioNulo() {

        ConsultaDAO cut = new ConsultaDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);

        cut.em = mockEM;

        List<PersonaRol> resultado =
                cut.buscarPacientePorCriterio(null, null);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        Mockito.verifyNoInteractions(mockEM);
    }

    @Test
    public void testBuscarPacientePorCriterioBlanco() {

        ConsultaDAO cut = new ConsultaDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);

        cut.em = mockEM;

        List<PersonaRol> resultado =
                cut.buscarPacientePorCriterio("   ", UUID.randomUUID());

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        Mockito.verifyNoInteractions(mockEM);
    }

    @Test
    public void testBuscarPacientePorCriterioExitoSinClinica() {

        ConsultaDAO cut = new ConsultaDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        @SuppressWarnings("unchecked")
        TypedQuery<PersonaRol> mockQuery =
                Mockito.mock(TypedQuery.class);

        cut.em = mockEM;

        UUID idEsperado = UUID.randomUUID();
        PersonaRol esperado = new PersonaRol();
        esperado.setIdPersonaRol(idEsperado);

        Mockito.when(mockEM.createNamedQuery(
                "PersonaRol.buscarPacientesPorCriterio",
                PersonaRol.class
        )).thenReturn(mockQuery);

        Mockito.when(mockQuery.getResultList())
                .thenReturn(Collections.singletonList(esperado));

        List<PersonaRol> resultado =
                cut.buscarPacientePorCriterio("  Juan ", null);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertSame(esperado, resultado.get(0));

        Mockito.verify(mockQuery)
                .setParameter("nombreRol", "paciente");
        Mockito.verify(mockQuery)
                .setParameter("c", "%juan%");
        Mockito.verify(mockQuery).getResultList();
    }

    @Test
    public void testBuscarPacientePorCriterioExitoConClinica() {

        ConsultaDAO cut = new ConsultaDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        @SuppressWarnings("unchecked")
        TypedQuery<PersonaRol> mockQuery =
                Mockito.mock(TypedQuery.class);

        cut.em = mockEM;

        UUID idClinica = UUID.randomUUID();
        UUID idEsperado = UUID.randomUUID();
        PersonaRol esperado = new PersonaRol();
        esperado.setIdPersonaRol(idEsperado);

        Mockito.when(mockEM.createNamedQuery(
                "PersonaRol.buscarPacientesPorCriterioEnClinica",
                PersonaRol.class
        )).thenReturn(mockQuery);

        Mockito.when(mockQuery.getResultList())
                .thenReturn(Collections.singletonList(esperado));

        List<PersonaRol> resultado =
                cut.buscarPacientePorCriterio("Maria", idClinica);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertSame(esperado, resultado.get(0));

        Mockito.verify(mockQuery)
                .setParameter("nombreRol", "paciente");
        Mockito.verify(mockQuery)
                .setParameter("c", "%maria%");
        Mockito.verify(mockQuery)
                .setParameter("idClinica", idClinica);
        Mockito.verify(mockQuery).getResultList();
    }

    @Test
    public void testBuscarPacientePorCriterioResultadoVacio() {

        ConsultaDAO cut = new ConsultaDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        @SuppressWarnings("unchecked")
        TypedQuery<PersonaRol> mockQuery =
                Mockito.mock(TypedQuery.class);

        cut.em = mockEM;

        Mockito.when(mockEM.createNamedQuery(
                "PersonaRol.buscarPacientesPorCriterio",
                PersonaRol.class
        )).thenReturn(mockQuery);

        Mockito.when(mockQuery.getResultList())
                .thenReturn(Collections.emptyList());

        List<PersonaRol> resultado =
                cut.buscarPacientePorCriterio("inexistente", null);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    public void testBuscarPacientePorCriterioDeduplica() {

        ConsultaDAO cut = new ConsultaDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        @SuppressWarnings("unchecked")
        TypedQuery<PersonaRol> mockQuery =
                Mockito.mock(TypedQuery.class);

        cut.em = mockEM;

        UUID idRepetido = UUID.randomUUID();
        UUID idOtro = UUID.randomUUID();

        PersonaRol primero = new PersonaRol();
        primero.setIdPersonaRol(idRepetido);

        PersonaRol duplicado = new PersonaRol();
        duplicado.setIdPersonaRol(idRepetido);

        PersonaRol otro = new PersonaRol();
        otro.setIdPersonaRol(idOtro);

        PersonaRol sinId = new PersonaRol();

        Mockito.when(mockEM.createNamedQuery(
                "PersonaRol.buscarPacientesPorCriterio",
                PersonaRol.class
        )).thenReturn(mockQuery);

        Mockito.when(mockQuery.getResultList())
                .thenReturn(Arrays.asList(
                        primero, duplicado, otro, sinId, null
                ));

        List<PersonaRol> resultado =
                cut.buscarPacientePorCriterio("juan", null);

        assertNotNull(resultado);
        assertInstanceOf(ArrayList.class, resultado);
        assertEquals(2, resultado.size());
        assertSame(primero, resultado.get(0));
        assertSame(otro, resultado.get(1));
    }

    @Test
    public void testBuscarPacientePorCriterioExcepcionPersistencia() {

        ConsultaDAO cut = new ConsultaDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        @SuppressWarnings("unchecked")
        TypedQuery<PersonaRol> mockQuery =
                Mockito.mock(TypedQuery.class);

        PersistenceException causa =
                new PersistenceException("fallo de base de datos");

        cut.em = mockEM;

        Mockito.when(mockEM.createNamedQuery(
                "PersonaRol.buscarPacientesPorCriterio",
                PersonaRol.class
        )).thenReturn(mockQuery);

        Mockito.when(mockQuery.getResultList())
                .thenThrow(causa);

        DAOException excepcion = assertThrows(
                DAOException.class,
                () -> cut.buscarPacientePorCriterio("juan", null)
        );

        assertSame(causa, excepcion.getCause());
    }

    @Test
    public void testBuscarPacientePorCriterioEntityManagerNulo() {

        ConsultaDAO cut = new ConsultaDAO();

        IllegalStateException excepcion = assertThrows(
                IllegalStateException.class,
                () -> cut.buscarPacientePorCriterio("juan", null)
        );

        assertTrue(
                excepcion.getMessage().contains("EntityManager no está disponible")
        );
    }
}
