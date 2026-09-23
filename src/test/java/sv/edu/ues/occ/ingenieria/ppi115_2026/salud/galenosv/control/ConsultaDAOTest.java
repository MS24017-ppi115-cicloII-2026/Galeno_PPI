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
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Consulta;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class ConsultaDAOTest {

    @Test
    public void testGetEntityManger() {
        ConsultaDAO cut = new ConsultaDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);

        cut.em = mockEM;

        assertEquals(mockEM, cut.getEntityManger());
    }

    @Test
    public void testBuscarPorPersonaRolExito() {
        UUID idPersonaRol = UUID.randomUUID();

        ConsultaDAO cut = new ConsultaDAO();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        TypedQuery<Consulta> mockQuery = Mockito.mock(TypedQuery.class);

        Mockito.when(mockEM.createQuery(
                any(String.class),
                eq(Consulta.class)))
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

        IllegalArgumentException excepcion = assertThrows(
                IllegalArgumentException.class,
                () -> cut.buscarPorPersonaRol(null)
        );

        assertEquals(
                "El idPersonaRol no puede ser nulo",
                excepcion.getMessage()
        );
    }
}