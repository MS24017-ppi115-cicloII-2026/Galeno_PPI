package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Documento;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class DocumentoDAOTest {

    @Test
    public void testGetEntityManger() {
        DocumentoDAO cut = new DocumentoDAO();

        EntityManager mockEM
                = Mockito.mock(EntityManager.class);

        cut.em = mockEM;

        assertEquals(
                mockEM,
                cut.getEntityManger()
        );
    }

    // ---------- buscarPorPersona ----------

    @Test
    public void testBuscarPorPersonaExito() {
        UUID idPersona = UUID.randomUUID();

        DocumentoDAO cut = new DocumentoDAO();

        EntityManager mockEM
                = Mockito.mock(EntityManager.class);

        @SuppressWarnings("unchecked")
        TypedQuery<Documento> mockQuery
                = Mockito.mock(TypedQuery.class);

        Mockito.when(mockEM.createNamedQuery(
                "Documento.findByPersonaId",
                Documento.class))
                .thenReturn(mockQuery);

        Mockito.when(mockQuery.getResultList())
                .thenReturn(
                        Collections.singletonList(
                                new Documento()
                        )
                );

        DocumentoDAO espia
                = Mockito.spy(cut);

        Mockito.doReturn(mockEM)
                .when(espia)
                .getEntityManger();

        List<Documento> resultado
                = espia.buscarPorPersona(idPersona);

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());

        Mockito.verify(mockEM)
                .createNamedQuery("Documento.findByPersonaId", Documento.class);
        Mockito.verify(mockQuery)
                .setParameter("id", idPersona);
    }

    @Test
    public void testBuscarPorPersonaNulo() {
        DocumentoDAO cut = new DocumentoDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        cut.em = mockEM;

        IllegalArgumentException excepcion
                = assertThrows(
                        IllegalArgumentException.class,
                        () -> cut.buscarPorPersona(null)
                );

        assertEquals(
                "El idPersona no puede ser nulo",
                excepcion.getMessage()
        );
        Mockito.verifyNoInteractions(mockEM);
    }

    @Test
    public void testBuscarPorPersonaListaVacia() {
        UUID idPersona = UUID.randomUUID();

        DocumentoDAO cut = new DocumentoDAO();

        EntityManager mockEM
                = Mockito.mock(EntityManager.class);

        @SuppressWarnings("unchecked")
        TypedQuery<Documento> mockQuery
                = Mockito.mock(TypedQuery.class);

        Mockito.when(mockEM.createNamedQuery(
                "Documento.findByPersonaId",
                Documento.class))
                .thenReturn(mockQuery);

        Mockito.when(mockQuery.getResultList())
                .thenReturn(Collections.emptyList());

        DocumentoDAO espia
                = Mockito.spy(cut);

        Mockito.doReturn(mockEM)
                .when(espia)
                .getEntityManger();

        List<Documento> resultado
                = espia.buscarPorPersona(idPersona);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    public void testBuscarPorPersonaExcepcionServidor() {
        UUID idPersona = UUID.randomUUID();

        DocumentoDAO cut = new DocumentoDAO();

        EntityManager mockEM
                = Mockito.mock(EntityManager.class);

        @SuppressWarnings("unchecked")
        TypedQuery<Documento> mockQuery
                = Mockito.mock(TypedQuery.class);

        Mockito.when(mockEM.createNamedQuery(
                "Documento.findByPersonaId",
                Documento.class))
                .thenReturn(mockQuery);

        RuntimeException causa
                = new RuntimeException("Fallo simulado de BD");

        Mockito.when(mockQuery.getResultList())
                .thenThrow(causa);

        DocumentoDAO espia
                = Mockito.spy(cut);

        Mockito.doReturn(mockEM)
                .when(espia)
                .getEntityManger();

        DAOException excepcion
                = assertThrows(
                        DAOException.class,
                        () -> espia.buscarPorPersona(idPersona)
                );

        assertSame(causa, excepcion.getCause());
    }

    @Test
    public void testBuscarPorPersonaEntityManagerNulo() {
        DocumentoDAO cut = new DocumentoDAO();
        cut.em = null;

        IllegalStateException excepcion
                = assertThrows(
                        IllegalStateException.class,
                        () -> cut.buscarPorPersona(UUID.randomUUID())
                );

        assertTrue(excepcion.getMessage()
                .contains("EntityManager no está disponible"));
    }

    // ---------- buscarPorTipoDocumento ----------

    @Test
    public void testBuscarPorTipoDocumentoExito() {
        UUID idTipoDocumento
                = UUID.randomUUID();

        DocumentoDAO cut
                = new DocumentoDAO();

        EntityManager mockEM
                = Mockito.mock(EntityManager.class);

        @SuppressWarnings("unchecked")
        TypedQuery<Documento> mockQuery
                = Mockito.mock(TypedQuery.class);

        Mockito.when(mockEM.createNamedQuery(
                "Documento.findByTipoDocumentoId",
                Documento.class))
                .thenReturn(mockQuery);

        Mockito.when(mockQuery.getResultList())
                .thenReturn(
                        Collections.singletonList(
                                new Documento()
                        )
                );

        DocumentoDAO espia
                = Mockito.spy(cut);

        Mockito.doReturn(mockEM)
                .when(espia)
                .getEntityManger();

        List<Documento> resultado
                = espia.buscarPorTipoDocumento(
                        idTipoDocumento
                );

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());

        Mockito.verify(mockEM)
                .createNamedQuery("Documento.findByTipoDocumentoId", Documento.class);
        Mockito.verify(mockQuery)
                .setParameter(
                        "id",
                        idTipoDocumento
                );
    }

    @Test
    public void testBuscarPorTipoDocumentoNulo() {
        DocumentoDAO cut = new DocumentoDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        cut.em = mockEM;

        IllegalArgumentException excepcion
                = assertThrows(
                        IllegalArgumentException.class,
                        () -> cut.buscarPorTipoDocumento(null)
                );

        assertEquals(
                "El idTipoDocumento no puede ser nulo",
                excepcion.getMessage()
        );
        Mockito.verifyNoInteractions(mockEM);
    }

    @Test
    public void testBuscarPorTipoDocumentoListaVacia() {
        UUID idTipoDocumento
                = UUID.randomUUID();

        DocumentoDAO cut
                = new DocumentoDAO();

        EntityManager mockEM
                = Mockito.mock(EntityManager.class);

        @SuppressWarnings("unchecked")
        TypedQuery<Documento> mockQuery
                = Mockito.mock(TypedQuery.class);

        Mockito.when(mockEM.createNamedQuery(
                "Documento.findByTipoDocumentoId",
                Documento.class))
                .thenReturn(mockQuery);

        Mockito.when(mockQuery.getResultList())
                .thenReturn(Collections.emptyList());

        DocumentoDAO espia
                = Mockito.spy(cut);

        Mockito.doReturn(mockEM)
                .when(espia)
                .getEntityManger();

        List<Documento> resultado
                = espia.buscarPorTipoDocumento(idTipoDocumento);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    public void testBuscarPorTipoDocumentoExcepcionServidor() {
        UUID idTipoDocumento
                = UUID.randomUUID();

        DocumentoDAO cut
                = new DocumentoDAO();

        EntityManager mockEM
                = Mockito.mock(EntityManager.class);

        @SuppressWarnings("unchecked")
        TypedQuery<Documento> mockQuery
                = Mockito.mock(TypedQuery.class);

        Mockito.when(mockEM.createNamedQuery(
                "Documento.findByTipoDocumentoId",
                Documento.class))
                .thenReturn(mockQuery);

        RuntimeException causa
                = new RuntimeException("Fallo simulado de BD");

        Mockito.when(mockQuery.getResultList())
                .thenThrow(causa);

        DocumentoDAO espia
                = Mockito.spy(cut);

        Mockito.doReturn(mockEM)
                .when(espia)
                .getEntityManger();

        DAOException excepcion
                = assertThrows(
                        DAOException.class,
                        () -> espia.buscarPorTipoDocumento(idTipoDocumento)
                );

        assertSame(causa, excepcion.getCause());
    }

    @Test
    public void testBuscarPorTipoDocumentoEntityManagerNulo() {
        DocumentoDAO cut = new DocumentoDAO();
        cut.em = null;

        IllegalStateException excepcion
                = assertThrows(
                        IllegalStateException.class,
                        () -> cut.buscarPorTipoDocumento(UUID.randomUUID())
                );

        assertTrue(excepcion.getMessage()
                .contains("EntityManager no está disponible"));
    }

    // ---------- existeDuplicado ----------

    @Test
    public void testExisteDuplicadoVerdadero() {
        DocumentoDAO cut = new DocumentoDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);

        @SuppressWarnings("unchecked")
        TypedQuery<Long> mockQuery = Mockito.mock(TypedQuery.class);

        UUID idPersona = UUID.randomUUID();
        UUID idTipo = UUID.randomUUID();
        UUID idExcluir = UUID.randomUUID();

        Mockito.when(mockEM.createNamedQuery(
                "Documento.countDuplicados", Long.class))
                .thenReturn(mockQuery);
        Mockito.when(mockQuery.getSingleResult()).thenReturn(1L);

        DocumentoDAO espia = Mockito.spy(cut);
        Mockito.doReturn(mockEM).when(espia).getEntityManger();

        boolean resultado = espia.existeDuplicado(
                idPersona, idTipo, "A01234567", idExcluir);

        assertTrue(resultado);
        Mockito.verify(mockEM)
                .createNamedQuery("Documento.countDuplicados", Long.class);
        Mockito.verify(mockQuery).setParameter("persona", idPersona);
        Mockito.verify(mockQuery).setParameter("tipo", idTipo);
        Mockito.verify(mockQuery).setParameter("valor", "a01234567");
        Mockito.verify(mockQuery).setParameter("excluir", idExcluir);
    }

    @Test
    public void testExisteDuplicadoFalso() {
        DocumentoDAO cut = new DocumentoDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);

        @SuppressWarnings("unchecked")
        TypedQuery<Long> mockQuery = Mockito.mock(TypedQuery.class);

        Mockito.when(mockEM.createNamedQuery(
                "Documento.countDuplicados", Long.class))
                .thenReturn(mockQuery);
        Mockito.when(mockQuery.getSingleResult()).thenReturn(0L);

        DocumentoDAO espia = Mockito.spy(cut);
        Mockito.doReturn(mockEM).when(espia).getEntityManger();

        assertFalse(espia.existeDuplicado(
                UUID.randomUUID(), UUID.randomUUID(), "01234567-8", null));
        Mockito.verify(mockQuery).setParameter("excluir", new UUID(0L, 0L));
    }

    @Test
    public void testExisteDuplicadoNoResultFalso() {
        DocumentoDAO cut = new DocumentoDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);

        @SuppressWarnings("unchecked")
        TypedQuery<Long> mockQuery = Mockito.mock(TypedQuery.class);

        Mockito.when(mockEM.createNamedQuery(
                "Documento.countDuplicados", Long.class))
                .thenReturn(mockQuery);
        Mockito.when(mockQuery.getSingleResult())
                .thenThrow(new NoResultException());

        DocumentoDAO espia = Mockito.spy(cut);
        Mockito.doReturn(mockEM).when(espia).getEntityManger();

        assertFalse(espia.existeDuplicado(
                UUID.randomUUID(), UUID.randomUUID(), "01234567-8", null));
    }

    @Test
    public void testExisteDuplicadoNonUniqueDAOException() {
        DocumentoDAO cut = new DocumentoDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);

        @SuppressWarnings("unchecked")
        TypedQuery<Long> mockQuery = Mockito.mock(TypedQuery.class);

        Mockito.when(mockEM.createNamedQuery(
                "Documento.countDuplicados", Long.class))
                .thenReturn(mockQuery);

        NonUniqueResultException causa = new NonUniqueResultException();

        Mockito.when(mockQuery.getSingleResult()).thenThrow(causa);

        DocumentoDAO espia = Mockito.spy(cut);
        Mockito.doReturn(mockEM).when(espia).getEntityManger();

        DAOException excepcion = assertThrows(
                DAOException.class,
                () -> espia.existeDuplicado(
                        UUID.randomUUID(), UUID.randomUUID(),
                        "01234567-8", null));

        assertSame(causa, excepcion.getCause());
    }

    @Test
    public void testExisteDuplicadoExcepcionServidor() {
        DocumentoDAO cut = new DocumentoDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);

        @SuppressWarnings("unchecked")
        TypedQuery<Long> mockQuery = Mockito.mock(TypedQuery.class);

        Mockito.when(mockEM.createNamedQuery(
                "Documento.countDuplicados", Long.class))
                .thenReturn(mockQuery);

        PersistenceException causa = new PersistenceException("Fallo simulado de BD");

        Mockito.when(mockQuery.getSingleResult()).thenThrow(causa);

        DocumentoDAO espia = Mockito.spy(cut);
        Mockito.doReturn(mockEM).when(espia).getEntityManger();

        DAOException excepcion = assertThrows(
                DAOException.class,
                () -> espia.existeDuplicado(
                        UUID.randomUUID(), UUID.randomUUID(),
                        "01234567-8", null));

        assertSame(causa, excepcion.getCause());
    }

    @Test
    public void testExisteDuplicadoEntityManagerNulo() {
        DocumentoDAO cut = new DocumentoDAO();
        cut.em = null;

        IllegalStateException excepcion = assertThrows(
                IllegalStateException.class,
                () -> cut.existeDuplicado(
                        UUID.randomUUID(), UUID.randomUUID(),
                        "01234567-8", null));

        assertTrue(excepcion.getMessage()
                .contains("EntityManager no está disponible"));
    }

    @Test
    public void testExisteDuplicadoParametrosInvalidos() {
        DocumentoDAO cut = new DocumentoDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        cut.em = mockEM;

        IllegalArgumentException excepcion = assertThrows(
                IllegalArgumentException.class,
                () -> cut.existeDuplicado(null, UUID.randomUUID(), "x", null));
        assertEquals(
                "Persona, tipo de documento y valor son obligatorios",
                excepcion.getMessage());

        excepcion = assertThrows(
                IllegalArgumentException.class,
                () -> cut.existeDuplicado(UUID.randomUUID(), null, "x", null));
        assertEquals(
                "Persona, tipo de documento y valor son obligatorios",
                excepcion.getMessage());

        excepcion = assertThrows(
                IllegalArgumentException.class,
                () -> cut.existeDuplicado(
                        UUID.randomUUID(), UUID.randomUUID(), "   ", null));
        assertEquals(
                "Persona, tipo de documento y valor son obligatorios",
                excepcion.getMessage());

        excepcion = assertThrows(
                IllegalArgumentException.class,
                () -> cut.existeDuplicado(
                        UUID.randomUUID(), UUID.randomUUID(), null, null));
        assertEquals(
                "Persona, tipo de documento y valor son obligatorios",
                excepcion.getMessage());

        Mockito.verifyNoInteractions(mockEM);
    }

    // ---------- existeValorEnOtraPersona ----------

    @Test
    public void testExisteValorEnOtraPersonaVerdadero() {
        DocumentoDAO cut = new DocumentoDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);

        @SuppressWarnings("unchecked")
        TypedQuery<Long> mockQuery = Mockito.mock(TypedQuery.class);

        UUID idPersona = UUID.randomUUID();
        UUID idTipo = UUID.randomUUID();
        UUID idExcluir = UUID.randomUUID();

        Mockito.when(mockEM.createNamedQuery(
                "Documento.countDuplicadosEnOtraPersona", Long.class))
                .thenReturn(mockQuery);
        Mockito.when(mockQuery.getSingleResult()).thenReturn(1L);

        DocumentoDAO espia = Mockito.spy(cut);
        Mockito.doReturn(mockEM).when(espia).getEntityManger();

        boolean resultado = espia.existeValorEnOtraPersona(
                idPersona, idTipo, "01234567-8", idExcluir);

        assertTrue(resultado);
        Mockito.verify(mockEM)
                .createNamedQuery("Documento.countDuplicadosEnOtraPersona", Long.class);
        Mockito.verify(mockQuery).setParameter("tipo", idTipo);
        Mockito.verify(mockQuery).setParameter("valor", "01234567-8");
        Mockito.verify(mockQuery).setParameter("persona", idPersona);
        Mockito.verify(mockQuery).setParameter("excluir", idExcluir);
    }

    @Test
    public void testExisteValorEnOtraPersonaFalso() {
        DocumentoDAO cut = new DocumentoDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);

        @SuppressWarnings("unchecked")
        TypedQuery<Long> mockQuery = Mockito.mock(TypedQuery.class);

        Mockito.when(mockEM.createNamedQuery(
                "Documento.countDuplicadosEnOtraPersona", Long.class))
                .thenReturn(mockQuery);
        Mockito.when(mockQuery.getSingleResult()).thenReturn(0L);

        DocumentoDAO espia = Mockito.spy(cut);
        Mockito.doReturn(mockEM).when(espia).getEntityManger();

        assertFalse(espia.existeValorEnOtraPersona(
                UUID.randomUUID(), UUID.randomUUID(), "01234567-8", null));
        Mockito.verify(mockQuery).setParameter("excluir", new UUID(0L, 0L));
    }

    @Test
    public void testExisteValorEnOtraPersonaNoResultFalso() {
        DocumentoDAO cut = new DocumentoDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);

        @SuppressWarnings("unchecked")
        TypedQuery<Long> mockQuery = Mockito.mock(TypedQuery.class);

        Mockito.when(mockEM.createNamedQuery(
                "Documento.countDuplicadosEnOtraPersona", Long.class))
                .thenReturn(mockQuery);
        Mockito.when(mockQuery.getSingleResult())
                .thenThrow(new NoResultException());

        DocumentoDAO espia = Mockito.spy(cut);
        Mockito.doReturn(mockEM).when(espia).getEntityManger();

        assertFalse(espia.existeValorEnOtraPersona(
                UUID.randomUUID(), UUID.randomUUID(), "01234567-8", null));
    }

    @Test
    public void testExisteValorEnOtraPersonaNonUniqueDAOException() {
        DocumentoDAO cut = new DocumentoDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);

        @SuppressWarnings("unchecked")
        TypedQuery<Long> mockQuery = Mockito.mock(TypedQuery.class);

        Mockito.when(mockEM.createNamedQuery(
                "Documento.countDuplicadosEnOtraPersona", Long.class))
                .thenReturn(mockQuery);

        NonUniqueResultException causa = new NonUniqueResultException();

        Mockito.when(mockQuery.getSingleResult()).thenThrow(causa);

        DocumentoDAO espia = Mockito.spy(cut);
        Mockito.doReturn(mockEM).when(espia).getEntityManger();

        DAOException excepcion = assertThrows(
                DAOException.class,
                () -> espia.existeValorEnOtraPersona(
                        UUID.randomUUID(), UUID.randomUUID(),
                        "01234567-8", null));

        assertSame(causa, excepcion.getCause());
    }

    @Test
    public void testExisteValorEnOtraPersonaExcepcionServidor() {
        DocumentoDAO cut = new DocumentoDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);

        @SuppressWarnings("unchecked")
        TypedQuery<Long> mockQuery = Mockito.mock(TypedQuery.class);

        Mockito.when(mockEM.createNamedQuery(
                "Documento.countDuplicadosEnOtraPersona", Long.class))
                .thenReturn(mockQuery);

        PersistenceException causa = new PersistenceException("Fallo simulado de BD");

        Mockito.when(mockQuery.getSingleResult()).thenThrow(causa);

        DocumentoDAO espia = Mockito.spy(cut);
        Mockito.doReturn(mockEM).when(espia).getEntityManger();

        DAOException excepcion = assertThrows(
                DAOException.class,
                () -> espia.existeValorEnOtraPersona(
                        UUID.randomUUID(), UUID.randomUUID(),
                        "01234567-8", null));

        assertSame(causa, excepcion.getCause());
    }

    @Test
    public void testExisteValorEnOtraPersonaEntityManagerNulo() {
        DocumentoDAO cut = new DocumentoDAO();
        cut.em = null;

        IllegalStateException excepcion = assertThrows(
                IllegalStateException.class,
                () -> cut.existeValorEnOtraPersona(
                        UUID.randomUUID(), UUID.randomUUID(),
                        "01234567-8", null));

        assertTrue(excepcion.getMessage()
                .contains("EntityManager no está disponible"));
    }

    @Test
    public void testExisteValorEnOtraPersonaParametrosInvalidos() {
        DocumentoDAO cut = new DocumentoDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        cut.em = mockEM;

        IllegalArgumentException excepcion = assertThrows(
                IllegalArgumentException.class,
                () -> cut.existeValorEnOtraPersona(
                        null, UUID.randomUUID(), "x", null));
        assertEquals(
                "Persona, tipo de documento y valor son obligatorios",
                excepcion.getMessage());

        excepcion = assertThrows(
                IllegalArgumentException.class,
                () -> cut.existeValorEnOtraPersona(
                        UUID.randomUUID(), null, "x", null));
        assertEquals(
                "Persona, tipo de documento y valor son obligatorios",
                excepcion.getMessage());

        excepcion = assertThrows(
                IllegalArgumentException.class,
                () -> cut.existeValorEnOtraPersona(
                        UUID.randomUUID(), UUID.randomUUID(), "   ", null));
        assertEquals(
                "Persona, tipo de documento y valor son obligatorios",
                excepcion.getMessage());

        excepcion = assertThrows(
                IllegalArgumentException.class,
                () -> cut.existeValorEnOtraPersona(
                        UUID.randomUUID(), UUID.randomUUID(), null, null));
        assertEquals(
                "Persona, tipo de documento y valor son obligatorios",
                excepcion.getMessage());

        Mockito.verifyNoInteractions(mockEM);
    }

    // ---------- existeOtroDocumentoDelTipo ----------

    @Test
    public void testExisteOtroDocumentoDelTipoVerdadero() {
        DocumentoDAO cut = new DocumentoDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);

        @SuppressWarnings("unchecked")
        TypedQuery<Long> mockQuery = Mockito.mock(TypedQuery.class);

        UUID idPersona = UUID.randomUUID();
        UUID idTipo = UUID.randomUUID();
        UUID idExcluir = UUID.randomUUID();

        Mockito.when(mockEM.createNamedQuery(
                "Documento.countOtrosDelTipo", Long.class))
                .thenReturn(mockQuery);
        Mockito.when(mockQuery.getSingleResult()).thenReturn(1L);

        DocumentoDAO espia = Mockito.spy(cut);
        Mockito.doReturn(mockEM).when(espia).getEntityManger();

        assertTrue(espia.existeOtroDocumentoDelTipo(
                idPersona, idTipo, idExcluir));
        Mockito.verify(mockEM)
                .createNamedQuery("Documento.countOtrosDelTipo", Long.class);
        Mockito.verify(mockQuery).setParameter("persona", idPersona);
        Mockito.verify(mockQuery).setParameter("tipo", idTipo);
        Mockito.verify(mockQuery).setParameter("excluir", idExcluir);
    }

    @Test
    public void testExisteOtroDocumentoDelTipoFalso() {
        DocumentoDAO cut = new DocumentoDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);

        @SuppressWarnings("unchecked")
        TypedQuery<Long> mockQuery = Mockito.mock(TypedQuery.class);

        Mockito.when(mockEM.createNamedQuery(
                "Documento.countOtrosDelTipo", Long.class))
                .thenReturn(mockQuery);
        Mockito.when(mockQuery.getSingleResult()).thenReturn(0L);

        DocumentoDAO espia = Mockito.spy(cut);
        Mockito.doReturn(mockEM).when(espia).getEntityManger();

        assertFalse(espia.existeOtroDocumentoDelTipo(
                UUID.randomUUID(), UUID.randomUUID(), null));
        Mockito.verify(mockQuery).setParameter("excluir", new UUID(0L, 0L));
    }

    @Test
    public void testExisteOtroDocumentoDelTipoNoResultFalso() {
        DocumentoDAO cut = new DocumentoDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);

        @SuppressWarnings("unchecked")
        TypedQuery<Long> mockQuery = Mockito.mock(TypedQuery.class);

        Mockito.when(mockEM.createNamedQuery(
                "Documento.countOtrosDelTipo", Long.class))
                .thenReturn(mockQuery);
        Mockito.when(mockQuery.getSingleResult())
                .thenThrow(new NoResultException());

        DocumentoDAO espia = Mockito.spy(cut);
        Mockito.doReturn(mockEM).when(espia).getEntityManger();

        assertFalse(espia.existeOtroDocumentoDelTipo(
                UUID.randomUUID(), UUID.randomUUID(), null));
    }

    @Test
    public void testExisteOtroDocumentoDelTipoNonUniqueDAOException() {
        DocumentoDAO cut = new DocumentoDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);

        @SuppressWarnings("unchecked")
        TypedQuery<Long> mockQuery = Mockito.mock(TypedQuery.class);

        Mockito.when(mockEM.createNamedQuery(
                "Documento.countOtrosDelTipo", Long.class))
                .thenReturn(mockQuery);

        NonUniqueResultException causa = new NonUniqueResultException();

        Mockito.when(mockQuery.getSingleResult()).thenThrow(causa);

        DocumentoDAO espia = Mockito.spy(cut);
        Mockito.doReturn(mockEM).when(espia).getEntityManger();

        DAOException excepcion = assertThrows(
                DAOException.class,
                () -> espia.existeOtroDocumentoDelTipo(
                        UUID.randomUUID(), UUID.randomUUID(), null));

        assertSame(causa, excepcion.getCause());
    }

    @Test
    public void testExisteOtroDocumentoDelTipoExcepcionServidor() {
        DocumentoDAO cut = new DocumentoDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);

        @SuppressWarnings("unchecked")
        TypedQuery<Long> mockQuery = Mockito.mock(TypedQuery.class);

        Mockito.when(mockEM.createNamedQuery(
                "Documento.countOtrosDelTipo", Long.class))
                .thenReturn(mockQuery);

        PersistenceException causa = new PersistenceException("Fallo simulado de BD");

        Mockito.when(mockQuery.getSingleResult()).thenThrow(causa);

        DocumentoDAO espia = Mockito.spy(cut);
        Mockito.doReturn(mockEM).when(espia).getEntityManger();

        DAOException excepcion = assertThrows(
                DAOException.class,
                () -> espia.existeOtroDocumentoDelTipo(
                        UUID.randomUUID(), UUID.randomUUID(), null));

        assertSame(causa, excepcion.getCause());
    }

    @Test
    public void testExisteOtroDocumentoDelTipoEntityManagerNulo() {
        DocumentoDAO cut = new DocumentoDAO();
        cut.em = null;

        IllegalStateException excepcion = assertThrows(
                IllegalStateException.class,
                () -> cut.existeOtroDocumentoDelTipo(
                        UUID.randomUUID(), UUID.randomUUID(), null));

        assertTrue(excepcion.getMessage()
                .contains("EntityManager no está disponible"));
    }

    @Test
    public void testExisteOtroDocumentoDelTipoParametrosInvalidos() {
        DocumentoDAO cut = new DocumentoDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        cut.em = mockEM;

        IllegalArgumentException excepcion = assertThrows(
                IllegalArgumentException.class,
                () -> cut.existeOtroDocumentoDelTipo(null, UUID.randomUUID(), null));
        assertEquals(
                "Persona y tipo de documento son obligatorios",
                excepcion.getMessage());

        excepcion = assertThrows(
                IllegalArgumentException.class,
                () -> cut.existeOtroDocumentoDelTipo(UUID.randomUUID(), null, null));
        assertEquals(
                "Persona y tipo de documento son obligatorios",
                excepcion.getMessage());

        Mockito.verifyNoInteractions(mockEM);
    }
}
