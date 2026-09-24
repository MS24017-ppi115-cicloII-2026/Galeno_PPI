package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Clinica;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Persona;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.PersonaRol;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Rol;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonaRolDAOTest {

    @Mock
    private EntityManager em;

    @Mock
    private TypedQuery<PersonaRol> query;

    @InjectMocks
    private PersonaRolDAO dao;

    @Test
    void testBuscarPorPersona() {

        Persona persona = new Persona();

        List<PersonaRol> esperados = List.of(
                new PersonaRol(),
                new PersonaRol()
        );

        when(em.createQuery(
                "SELECT p FROM PersonaRol p "
                + "WHERE p.idPersona = :persona",
                PersonaRol.class
        )).thenReturn(query);

        when(query.setParameter("persona", persona))
                .thenReturn(query);

        when(query.getResultList())
                .thenReturn(esperados);

        List<PersonaRol> resultado = dao.buscarPorPersona(persona);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(esperados, resultado);

        verify(em).createQuery(
                "SELECT p FROM PersonaRol p "
                + "WHERE p.idPersona = :persona",
                PersonaRol.class
        );

        verify(query).setParameter("persona", persona);
        verify(query).getResultList();
    }

    @Test
    void testBuscarPorPersonaConNull() {

        assertThrows(
                IllegalArgumentException.class,
                () -> dao.buscarPorPersona(null)
        );

        verifyNoInteractions(em);
    }

    @Test
    void testBuscarPorRol() {

        Rol rol = new Rol();

        List<PersonaRol> esperados = List.of(
                new PersonaRol(),
                new PersonaRol()
        );

        when(em.createQuery(
                "SELECT p FROM PersonaRol p "
                + "WHERE p.idRol = :rol",
                PersonaRol.class
        )).thenReturn(query);

        when(query.setParameter("rol", rol))
                .thenReturn(query);

        when(query.getResultList())
                .thenReturn(esperados);

        List<PersonaRol> resultado = dao.buscarPorRol(rol);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(esperados, resultado);

        verify(em).createQuery(
                "SELECT p FROM PersonaRol p "
                + "WHERE p.idRol = :rol",
                PersonaRol.class
        );

        verify(query).setParameter("rol", rol);
        verify(query).getResultList();
    }

    @Test
    void testBuscarPorRolConNull() {

        assertThrows(
                IllegalArgumentException.class,
                () -> dao.buscarPorRol(null)
        );

        verifyNoInteractions(em);
    }

    @Test
    void testBuscarPorClinica() {

        Clinica clinica = new Clinica();

        List<PersonaRol> esperados = List.of(
                new PersonaRol(),
                new PersonaRol()
        );

        when(em.createQuery(
                "SELECT p FROM PersonaRol p "
                + "WHERE p.idClinica = :clinica",
                PersonaRol.class
        )).thenReturn(query);

        when(query.setParameter("clinica", clinica))
                .thenReturn(query);

        when(query.getResultList())
                .thenReturn(esperados);

        List<PersonaRol> resultado = dao.buscarPorClinica(clinica);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(esperados, resultado);

        verify(em).createQuery(
                "SELECT p FROM PersonaRol p "
                + "WHERE p.idClinica = :clinica",
                PersonaRol.class
        );

        verify(query).setParameter("clinica", clinica);
        verify(query).getResultList();
    }

    @Test
    void testBuscarPorClinicaConNull() {

        assertThrows(
                IllegalArgumentException.class,
                () -> dao.buscarPorClinica(null)
        );

        verifyNoInteractions(em);
    }
}
