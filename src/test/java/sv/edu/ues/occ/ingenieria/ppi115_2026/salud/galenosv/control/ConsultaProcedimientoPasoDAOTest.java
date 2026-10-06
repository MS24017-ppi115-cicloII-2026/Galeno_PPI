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
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.PersonaRol;

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

    @Test
    public void testBuscarPorConsultaProcedimientoNulo() {

        ConsultaProcedimientoPasoDAO cut =
                new ConsultaProcedimientoPasoDAO();

        IllegalArgumentException excepcion = assertThrows(
                IllegalArgumentException.class,
                () -> cut.buscarPorConsultaProcedimiento(null)
        );

        assertEquals(
                "El idConsultaProcedimiento no puede ser nulo",
                excepcion.getMessage()
        );
    }

    @Test
    public void testBuscarPorPersonaRolNulo() {

        ConsultaProcedimientoPasoDAO cut =
                new ConsultaProcedimientoPasoDAO();

        IllegalArgumentException excepcion = assertThrows(
                IllegalArgumentException.class,
                () -> cut.buscarPorPersonaRol(null)
        );

        assertEquals(
                "El idPersonaRol no puede ser nulo",
                excepcion.getMessage()
        );
    }

    @Test
    public void testBuscarPersonasExcluyendoRolExito() {

        ConsultaProcedimientoPasoDAO cut =
                new ConsultaProcedimientoPasoDAO();

        EntityManager mockEM = Mockito.mock(EntityManager.class);
        TypedQuery<PersonaRol> mockQuery =
                Mockito.mock(TypedQuery.class);

        cut.em = mockEM;

        List<PersonaRol> esperados =
                Collections.singletonList(new PersonaRol());

        Mockito.when(mockEM.createQuery(
                any(String.class),
                eq(PersonaRol.class)
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
    public void testBuscarPersonasExcluyendoRolNulo() {

        ConsultaProcedimientoPasoDAO cut =
                new ConsultaProcedimientoPasoDAO();

        IllegalArgumentException excepcion = assertThrows(
                IllegalArgumentException.class,
                () -> cut.buscarPersonasExcluyendoRol(null)
        );

        assertEquals(
                "El nombre del rol no puede estar vacío",
                excepcion.getMessage()
        );
    }

    @Test
    public void testBuscarPersonasExcluyendoRolVacio() {

        ConsultaProcedimientoPasoDAO cut =
                new ConsultaProcedimientoPasoDAO();

        assertThrows(
                IllegalArgumentException.class,
                () -> cut.buscarPersonasExcluyendoRol("  ")
        );
    }
}
