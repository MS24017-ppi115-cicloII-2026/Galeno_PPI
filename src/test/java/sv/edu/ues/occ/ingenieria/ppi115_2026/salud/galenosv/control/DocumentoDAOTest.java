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
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Documento;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class DocumentoDAOTest {

    @Test
    public void testGetEntityManger() {
        DocumentoDAO cut = new DocumentoDAO();

        EntityManager mockEM =
                Mockito.mock(EntityManager.class);

        cut.em = mockEM;

        assertEquals(
                mockEM,
                cut.getEntityManger()
        );
    }

    @Test
    public void testBuscarPorPersonaExito() {
        UUID idPersona = UUID.randomUUID();

        DocumentoDAO cut = new DocumentoDAO();

        EntityManager mockEM =
                Mockito.mock(EntityManager.class);

        TypedQuery<Documento> mockQuery =
                Mockito.mock(TypedQuery.class);

        Mockito.when(mockEM.createQuery(
                any(String.class),
                eq(Documento.class)))
                .thenReturn(mockQuery);

        Mockito.when(mockQuery.getResultList())
                .thenReturn(
                        Collections.singletonList(
                                new Documento()
                        )
                );

        DocumentoDAO espia =
                Mockito.spy(cut);

        Mockito.doReturn(mockEM)
                .when(espia)
                .getEntityManger();

        List<Documento> resultado =
                espia.buscarPorPersona(idPersona);

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());

        Mockito.verify(mockQuery)
                .setParameter("id", idPersona);
    }

    @Test
    public void testBuscarPorPersonaNulo() {
        DocumentoDAO cut =
                new DocumentoDAO();

        IllegalArgumentException excepcion =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> cut.buscarPorPersona(null)
                );

        assertEquals(
                "El idPersona no puede ser nulo",
                excepcion.getMessage()
        );
    }

    @Test
    public void testBuscarPorTipoDocumentoExito() {
        UUID idTipoDocumento =
                UUID.randomUUID();

        DocumentoDAO cut =
                new DocumentoDAO();

        EntityManager mockEM =
                Mockito.mock(EntityManager.class);

        TypedQuery<Documento> mockQuery =
                Mockito.mock(TypedQuery.class);

        Mockito.when(mockEM.createQuery(
                any(String.class),
                eq(Documento.class)))
                .thenReturn(mockQuery);

        Mockito.when(mockQuery.getResultList())
                .thenReturn(
                        Collections.singletonList(
                                new Documento()
                        )
                );

        DocumentoDAO espia =
                Mockito.spy(cut);

        Mockito.doReturn(mockEM)
                .when(espia)
                .getEntityManger();

        List<Documento> resultado =
                espia.buscarPorTipoDocumento(
                        idTipoDocumento
                );

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());

        Mockito.verify(mockQuery)
                .setParameter(
                        "id",
                        idTipoDocumento
                );
    }

    @Test
    public void testBuscarPorTipoDocumentoNulo() {
        DocumentoDAO cut =
                new DocumentoDAO();

        IllegalArgumentException excepcion =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> cut.buscarPorTipoDocumento(null)
                );

        assertEquals(
                "El idTipoDocumento no puede ser nulo",
                excepcion.getMessage()
        );
    }
}