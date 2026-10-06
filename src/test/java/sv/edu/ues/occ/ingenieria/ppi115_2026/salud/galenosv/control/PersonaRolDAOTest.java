package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.UUID;
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

    @Mock
    private TypedQuery<Long> countQuery;

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
    void testBuscarPorRol() {

        Rol rol = new Rol();

        List<PersonaRol> esperados = List.of(
                new PersonaRol(),
                new PersonaRol()
        );

        when(em.createQuery(
                "SELECT p FROM PersonaRol p "
                + "JOIN FETCH p.idPersona "
                + "JOIN FETCH p.idRol "
                + "LEFT JOIN FETCH p.idClinica "
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
                + "JOIN FETCH p.idPersona "
                + "JOIN FETCH p.idRol "
                + "LEFT JOIN FETCH p.idClinica "
                + "WHERE p.idRol = :rol",
                PersonaRol.class
        );

        verify(query).setParameter("rol", rol);
        verify(query).getResultList();
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
    void testBuscarConRelacionesDevuelveElPrimerRegistro() {

        UUID id = UUID.randomUUID();
        PersonaRol esperado = new PersonaRol(id);

        when(em.createQuery(
                "SELECT pr FROM PersonaRol pr "
                + "JOIN FETCH pr.idPersona "
                + "JOIN FETCH pr.idRol "
                + "LEFT JOIN FETCH pr.idClinica "
                + "WHERE pr.idPersonaRol = :id",
                PersonaRol.class
        )).thenReturn(query);

        when(query.setParameter("id", id)).thenReturn(query);
        when(query.setMaxResults(1)).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(esperado));

        assertSame(esperado, dao.buscarConRelaciones(id));

        verify(query).setMaxResults(1);
    }

    @Test
    void testBuscarConRelacionesConIdNuloDevuelveNulo() {

        assertNull(dao.buscarConRelaciones(null));

        verifyNoInteractions(em);
    }

    @Test
    void testBuscarConRelacionesSinCoincidenciasDevuelveNulo() {

        UUID id = UUID.randomUUID();

        when(em.createQuery(anyString(), eq(PersonaRol.class)))
                .thenReturn(query);
        when(query.setParameter("id", id)).thenReturn(query);
        when(query.setMaxResults(1)).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of());

        assertNull(dao.buscarConRelaciones(id));
    }

    @Test
    void testCrearReemplazaLasRelacionesPorLasResueltas() {

        PersonaRol registro = registroValido();

        Rol rol = new Rol(UUID.randomUUID());
        rol.setActivo(true);

        Clinica clinica = new Clinica(UUID.randomUUID());
        clinica.setActivo(true);

        Persona persona = new Persona(UUID.randomUUID());

        when(em.find(Rol.class, registro.getIdRol().getIdRol()))
                .thenReturn(rol);
        when(em.find(Clinica.class, registro.getIdClinica().getIdClinica()))
                .thenReturn(clinica);
        when(em.find(Persona.class, registro.getIdPersona().getIdPersona()))
                .thenReturn(persona);
        when(em.createQuery(anyString(), eq(Long.class)))
                .thenReturn(countQuery);
        when(countQuery.setParameter(eq("persona"), any(Persona.class)))
                .thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(0L);

        dao.crear(registro);

        verify(em).persist(registro);

        assertSame(persona, registro.getIdPersona());
        assertSame(rol, registro.getIdRol());
        assertSame(clinica, registro.getIdClinica());
    }

    @Test
    void testCrearUsaLaConsultaDeConteoSinExcepcion() {

        PersonaRol registro = registroValido();

        Rol rol = new Rol(UUID.randomUUID());
        rol.setActivo(true);

        Clinica clinica = new Clinica(UUID.randomUUID());
        clinica.setActivo(true);

        when(em.find(Rol.class, registro.getIdRol().getIdRol()))
                .thenReturn(rol);
        when(em.find(Clinica.class, registro.getIdClinica().getIdClinica()))
                .thenReturn(clinica);
        when(em.find(Persona.class, registro.getIdPersona().getIdPersona()))
                .thenReturn(new Persona(UUID.randomUUID()));
        when(em.createQuery(anyString(), eq(Long.class)))
                .thenReturn(countQuery);
        when(countQuery.setParameter(eq("persona"), any(Persona.class)))
                .thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(0L);

        dao.crear(registro);

        verify(em).createQuery(
                "SELECT COUNT(pr) FROM PersonaRol pr "
                + "WHERE pr.idPersona = :persona ",
                Long.class
        );
        verify(em).persist(registro);
    }

    @Test
    void testCrearConRegistroNulo() {

        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> dao.crear(null)
        );

        assertEquals(
                "El registro de persona y rol no puede ser nulo",
                e.getMessage()
        );
    }

    @Test
    void testCrearSinPersona() {

        PersonaRol registro = registroValido();
        registro.setIdPersona(null);

        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> dao.crear(registro)
        );

        assertEquals("Debe seleccionar una persona", e.getMessage());
    }

    @Test
    void testCrearSinRol() {

        PersonaRol registro = registroValido();
        registro.setIdRol(null);

        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> dao.crear(registro)
        );

        assertEquals("Debe seleccionar un rol", e.getMessage());
    }

    @Test
    void testCrearSinClinica() {

        PersonaRol registro = registroValido();
        registro.setIdClinica(null);

        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> dao.crear(registro)
        );

        assertEquals("Debe seleccionar una clínica", e.getMessage());
    }

    @Test
    void testCrearConRolSinId() {

        PersonaRol registro = registroValido();
        registro.setIdRol(new Rol());

        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> dao.crear(registro)
        );

        assertEquals("El rol seleccionado no es válido", e.getMessage());
    }

    @Test
    void testCrearConClinicaSinId() {

        PersonaRol registro = registroValido();
        registro.setIdClinica(new Clinica());

        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> dao.crear(registro)
        );

        assertEquals("La clínica seleccionada no es válida", e.getMessage());
    }

    @Test
    void testCrearConRolInexistente() {

        PersonaRol registro = registroValido();

        when(em.find(Rol.class, registro.getIdRol().getIdRol()))
                .thenReturn(null);

        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> dao.crear(registro)
        );

        assertEquals("El rol seleccionado no existe", e.getMessage());
    }

    @Test
    void testCrearConRolInactivo() {

        PersonaRol registro = registroValido();

        Rol rol = new Rol(UUID.randomUUID());
        rol.setActivo(false);

        when(em.find(Rol.class, registro.getIdRol().getIdRol()))
                .thenReturn(rol);

        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> dao.crear(registro)
        );

        assertEquals("No se puede asignar un rol inactivo", e.getMessage());
    }

    @Test
    void testCrearConRolActivoNulo() {

        PersonaRol registro = registroValido();

        Rol rol = new Rol(UUID.randomUUID());
        rol.setActivo(null);

        when(em.find(Rol.class, registro.getIdRol().getIdRol()))
                .thenReturn(rol);

        assertThrows(IllegalArgumentException.class, () -> dao.crear(registro));
    }

    @Test
    void testCrearConClinicaInexistente() {

        PersonaRol registro = registroValido();

        Rol rol = new Rol(UUID.randomUUID());
        rol.setActivo(true);

        when(em.find(Rol.class, registro.getIdRol().getIdRol()))
                .thenReturn(rol);
        when(em.find(
                Clinica.class,
                registro.getIdClinica().getIdClinica()
        )).thenReturn(null);

        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> dao.crear(registro)
        );

        assertEquals("La clínica seleccionada no existe", e.getMessage());
    }

    @Test
    void testCrearConClinicaInactiva() {

        PersonaRol registro = registroValido();

        Rol rol = new Rol(UUID.randomUUID());
        rol.setActivo(true);

        Clinica clinica = new Clinica(UUID.randomUUID());
        clinica.setActivo(false);

        when(em.find(Rol.class, registro.getIdRol().getIdRol()))
                .thenReturn(rol);
        when(em.find(Clinica.class, registro.getIdClinica().getIdClinica()))
                .thenReturn(clinica);

        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> dao.crear(registro)
        );

        assertEquals(
                "No se puede asignar una clínica inactiva",
                e.getMessage()
        );
    }

    @Test
    void testCrearConPersonaInexistente() {

        PersonaRol registro = registroValido();

        Rol rol = new Rol(UUID.randomUUID());
        rol.setActivo(true);

        Clinica clinica = new Clinica(UUID.randomUUID());
        clinica.setActivo(true);

        when(em.find(Rol.class, registro.getIdRol().getIdRol()))
                .thenReturn(rol);
        when(em.find(Clinica.class, registro.getIdClinica().getIdClinica()))
                .thenReturn(clinica);
        when(em.find(Persona.class, registro.getIdPersona().getIdPersona()))
                .thenReturn(null);

        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> dao.crear(registro)
        );

        assertEquals("La persona seleccionada no existe", e.getMessage());
    }

    @Test
    void testCrearConPersonaQueYaTieneRol() {

        PersonaRol registro = registroValido();

        Rol rol = new Rol(UUID.randomUUID());
        rol.setActivo(true);

        Clinica clinica = new Clinica(UUID.randomUUID());
        clinica.setActivo(true);

        Persona persona = new Persona(UUID.randomUUID());

        when(em.find(Rol.class, registro.getIdRol().getIdRol()))
                .thenReturn(rol);
        when(em.find(Clinica.class, registro.getIdClinica().getIdClinica()))
                .thenReturn(clinica);
        when(em.find(Persona.class, registro.getIdPersona().getIdPersona()))
                .thenReturn(persona);
        when(em.createQuery(anyString(), eq(Long.class)))
                .thenReturn(countQuery);
        when(countQuery.setParameter(eq("persona"), any(Persona.class)))
                .thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(2L);

        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> dao.crear(registro)
        );

        assertEquals(
                "La persona seleccionada ya tiene un rol asignado",
                e.getMessage()
        );

        verify(em, never()).persist(any(PersonaRol.class));
    }

    // ---------- actualizar ----------

    @Test
    void testActualizarSinIdDeRegistro() {

        PersonaRol registro = registroValido();

        Rol rol = new Rol(UUID.randomUUID());
        rol.setActivo(true);

        Clinica clinica = new Clinica(UUID.randomUUID());
        clinica.setActivo(true);

        when(em.find(Rol.class, registro.getIdRol().getIdRol()))
                .thenReturn(rol);
        when(em.find(Clinica.class, registro.getIdClinica().getIdClinica()))
                .thenReturn(clinica);
        when(em.find(Persona.class, registro.getIdPersona().getIdPersona()))
                .thenReturn(new Persona(UUID.randomUUID()));

        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> dao.actualizar(registro)
        );

        assertEquals(
                "El ID del registro es obligatorio para actualizar",
                e.getMessage()
        );

        verify(em, never()).merge(any(PersonaRol.class));
    }

    @Test
    void testActualizarExcluyeElPropioRegistroDelConteo() {

        PersonaRol registro = registroValido();
        registro.setIdPersonaRol(UUID.randomUUID());

        Rol rol = new Rol(UUID.randomUUID());
        rol.setActivo(true);

        Clinica clinica = new Clinica(UUID.randomUUID());
        clinica.setActivo(true);

        when(em.find(Rol.class, registro.getIdRol().getIdRol()))
                .thenReturn(rol);
        when(em.find(Clinica.class, registro.getIdClinica().getIdClinica()))
                .thenReturn(clinica);
        when(em.find(Persona.class, registro.getIdPersona().getIdPersona()))
                .thenReturn(new Persona(UUID.randomUUID()));
        when(em.createQuery(anyString(), eq(Long.class)))
                .thenReturn(countQuery);
        when(countQuery.setParameter(eq("persona"), any(Persona.class)))
                .thenReturn(countQuery);
        when(countQuery.setParameter(
                eq("idPersonaRol"),
                eq(registro.getIdPersonaRol())
        )).thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(0L);

        dao.actualizar(registro);

        verify(em).createQuery(
                "SELECT COUNT(pr) FROM PersonaRol pr "
                + "WHERE pr.idPersona = :persona "
                + "AND pr.idPersonaRol <> :idPersonaRol ",
                Long.class
        );

        verify(em).merge(registro);
    }

    @Test
    void testActualizarConRolDuplicado() {

        PersonaRol registro = registroValido();
        registro.setIdPersonaRol(UUID.randomUUID());

        Rol rol = new Rol(UUID.randomUUID());
        rol.setActivo(true);

        Clinica clinica = new Clinica(UUID.randomUUID());
        clinica.setActivo(true);

        when(em.find(Rol.class, registro.getIdRol().getIdRol()))
                .thenReturn(rol);
        when(em.find(Clinica.class, registro.getIdClinica().getIdClinica()))
                .thenReturn(clinica);
        when(em.find(Persona.class, registro.getIdPersona().getIdPersona()))
                .thenReturn(new Persona(UUID.randomUUID()));
        when(em.createQuery(anyString(), eq(Long.class)))
                .thenReturn(countQuery);
        when(countQuery.setParameter(eq("persona"), any(Persona.class)))
                .thenReturn(countQuery);
        when(countQuery.setParameter(
                eq("idPersonaRol"),
                eq(registro.getIdPersonaRol())
        )).thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(1L);

        assertThrows(
                IllegalArgumentException.class,
                () -> dao.actualizar(registro)
        );

        verify(em, never()).merge(any(PersonaRol.class));
    }

    // ---------- helpers ----------

    private PersonaRol registroValido() {

        PersonaRol registro = new PersonaRol();
        registro.setIdPersona(new Persona(UUID.randomUUID()));
        registro.setIdRol(new Rol(UUID.randomUUID()));
        registro.setIdClinica(new Clinica(UUID.randomUUID()));

        return registro;
    }
}
