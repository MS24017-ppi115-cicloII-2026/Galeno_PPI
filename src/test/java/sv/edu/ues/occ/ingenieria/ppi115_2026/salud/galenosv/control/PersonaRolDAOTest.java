package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.PersistenceException;
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

    // ---------- buscarPorPersona / buscarPorRol / buscarPorClinica ----------

    @Test
    void testBuscarPorPersona() {

        Persona persona = new Persona();

        List<PersonaRol> esperados = List.of(
                new PersonaRol(),
                new PersonaRol()
        );

        when(em.createNamedQuery("PersonaRol.findByPersona", PersonaRol.class))
                .thenReturn(query);

        when(query.setParameter("persona", persona))
                .thenReturn(query);

        when(query.getResultList())
                .thenReturn(esperados);

        List<PersonaRol> resultado = dao.buscarPorPersona(persona);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(esperados, resultado);

        verify(em).createNamedQuery("PersonaRol.findByPersona", PersonaRol.class);

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

        when(em.createNamedQuery(
                "PersonaRol.findByRolConRelaciones", PersonaRol.class
        )).thenReturn(query);

        when(query.setParameter("rol", rol))
                .thenReturn(query);

        when(query.getResultList())
                .thenReturn(esperados);

        List<PersonaRol> resultado = dao.buscarPorRol(rol);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(esperados, resultado);

        verify(em).createNamedQuery(
                "PersonaRol.findByRolConRelaciones", PersonaRol.class
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

        when(em.createNamedQuery("PersonaRol.findByClinica", PersonaRol.class))
                .thenReturn(query);

        when(query.setParameter("clinica", clinica))
                .thenReturn(query);

        when(query.getResultList())
                .thenReturn(esperados);

        List<PersonaRol> resultado = dao.buscarPorClinica(clinica);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(esperados, resultado);

        verify(em).createNamedQuery("PersonaRol.findByClinica", PersonaRol.class);

        verify(query).setParameter("clinica", clinica);
        verify(query).getResultList();
    }

    @Test
    void testBuscarPorPersonaConPersonaNula() {

        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> dao.buscarPorPersona(null)
        );

        assertEquals("La persona no puede ser nula", e.getMessage());

        verifyNoInteractions(em);
    }

    @Test
    void testBuscarPorRolConRolNulo() {

        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> dao.buscarPorRol(null)
        );

        assertEquals("El rol no puede ser nulo", e.getMessage());

        verifyNoInteractions(em);
    }

    @Test
    void testBuscarPorClinicaConClinicaNula() {

        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> dao.buscarPorClinica(null)
        );

        assertEquals("La clínica no puede ser nula", e.getMessage());

        verifyNoInteractions(em);
    }

    @Test
    void testBuscarPorPersonaConResultadoVacioDevuelveListaVacia() {

        Persona persona = new Persona(UUID.randomUUID());

        when(em.createNamedQuery("PersonaRol.findByPersona", PersonaRol.class))
                .thenReturn(query);
        when(query.setParameter("persona", persona)).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of());

        List<PersonaRol> resultado = dao.buscarPorPersona(persona);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void testBuscarPorRolConErrorEnConsultaLanzaDAOException() {

        Rol rol = new Rol(UUID.randomUUID());
        PersistenceException causa = new PersistenceException("fallo de BD");

        when(em.createNamedQuery(
                "PersonaRol.findByRolConRelaciones", PersonaRol.class
        )).thenReturn(query);
        when(query.setParameter("rol", rol)).thenReturn(query);
        when(query.getResultList()).thenThrow(causa);

        DAOException e = assertThrows(
                DAOException.class,
                () -> dao.buscarPorRol(rol)
        );

        assertSame(causa, e.getCause());
    }

    // ---------- buscarConRelaciones ----------

    @Test
    void testBuscarConRelacionesDevuelveElPrimerRegistro() {

        UUID id = UUID.randomUUID();
        PersonaRol esperado = new PersonaRol(id);

        when(em.createNamedQuery(
                "PersonaRol.findPorIdConRelaciones", PersonaRol.class
        )).thenReturn(query);

        when(query.setParameter("id", id)).thenReturn(query);
        when(query.getSingleResult()).thenReturn(esperado);

        assertSame(esperado, dao.buscarConRelaciones(id));

        verify(em).createNamedQuery(
                "PersonaRol.findPorIdConRelaciones", PersonaRol.class
        );
        verify(query).setParameter("id", id);
        verify(query).getSingleResult();
    }

    @Test
    void testBuscarConRelacionesConIdNuloDevuelveNulo() {

        assertNull(dao.buscarConRelaciones(null));

        verifyNoInteractions(em);
    }

    @Test
    void testBuscarConRelacionesSinCoincidenciasDevuelveNulo() {

        UUID id = UUID.randomUUID();

        when(em.createNamedQuery(
                "PersonaRol.findPorIdConRelaciones", PersonaRol.class
        )).thenReturn(query);
        when(query.setParameter("id", id)).thenReturn(query);
        when(query.getSingleResult())
                .thenThrow(new NoResultException());

        assertNull(dao.buscarConRelaciones(id));
    }

    @Test
    void testBuscarConRelacionesConMultiplesResultadosLanzaDAOException() {

        UUID id = UUID.randomUUID();

        when(em.createNamedQuery(
                "PersonaRol.findPorIdConRelaciones", PersonaRol.class
        )).thenReturn(query);
        when(query.setParameter("id", id)).thenReturn(query);
        when(query.getSingleResult())
                .thenThrow(new NonUniqueResultException());

        assertThrows(
                DAOException.class,
                () -> dao.buscarConRelaciones(id)
        );
    }

    @Test
    void testBuscarConRelacionesConErrorDePersistenciaLanzaDAOException() {

        UUID id = UUID.randomUUID();
        PersistenceException causa = new PersistenceException("fallo de BD");

        when(em.createNamedQuery(
                "PersonaRol.findPorIdConRelaciones", PersonaRol.class
        )).thenReturn(query);
        when(query.setParameter("id", id)).thenReturn(query);
        when(query.getSingleResult()).thenThrow(causa);

        DAOException e = assertThrows(
                DAOException.class,
                () -> dao.buscarConRelaciones(id)
        );

        assertSame(causa, e.getCause());
    }

    // ---------- crear ----------

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
        when(em.createNamedQuery("PersonaRol.countDuplicados", Long.class))
                .thenReturn(countQuery);
        when(countQuery.setParameter(eq("persona"), any(Persona.class)))
                .thenReturn(countQuery);
        when(countQuery.setParameter(eq("rol"), any(Rol.class)))
                .thenReturn(countQuery);
        when(countQuery.setParameter(eq("clinica"), any(Clinica.class)))
                .thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(0L);

        dao.crear(registro);

        verify(em).persist(registro);

        assertSame(persona, registro.getIdPersona());
        assertSame(rol, registro.getIdRol());
        assertSame(clinica, registro.getIdClinica());

        verify(countQuery).setParameter("persona", persona);
        verify(countQuery).setParameter("rol", rol);
        verify(countQuery).setParameter("clinica", clinica);
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
        when(em.createNamedQuery("PersonaRol.countDuplicados", Long.class))
                .thenReturn(countQuery);
        when(countQuery.setParameter(eq("persona"), any(Persona.class)))
                .thenReturn(countQuery);
        when(countQuery.setParameter(eq("rol"), any(Rol.class)))
                .thenReturn(countQuery);
        when(countQuery.setParameter(eq("clinica"), any(Clinica.class)))
                .thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(0L);

        dao.crear(registro);

        verify(em).createNamedQuery("PersonaRol.countDuplicados", Long.class);
        verify(em).persist(registro);
    }

    @Test
    void testCrearConConsultaSinResultadoProcedeConElPersist() {

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
        when(em.createNamedQuery("PersonaRol.countDuplicados", Long.class))
                .thenReturn(countQuery);
        when(countQuery.setParameter(anyString(), any()))
                .thenReturn(countQuery);
        when(countQuery.getSingleResult())
                .thenThrow(new NoResultException());

        dao.crear(registro);

        verify(em).persist(registro);
    }

    @Test
    void testCrearConErrorEnConsultaLanzaDAOException() {

        PersonaRol registro = registroValido();

        Rol rol = new Rol(UUID.randomUUID());
        rol.setActivo(true);

        Clinica clinica = new Clinica(UUID.randomUUID());
        clinica.setActivo(true);

        PersistenceException causa = new PersistenceException("fallo de BD");

        when(em.find(Rol.class, registro.getIdRol().getIdRol()))
                .thenReturn(rol);
        when(em.find(Clinica.class, registro.getIdClinica().getIdClinica()))
                .thenReturn(clinica);
        when(em.find(Persona.class, registro.getIdPersona().getIdPersona()))
                .thenReturn(new Persona(UUID.randomUUID()));
        when(em.createNamedQuery("PersonaRol.countDuplicados", Long.class))
                .thenReturn(countQuery);
        when(countQuery.setParameter(anyString(), any()))
                .thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenThrow(causa);

        DAOException e = assertThrows(
                DAOException.class,
                () -> dao.crear(registro)
        );

        assertSame(causa, e.getCause());

        verify(em, never()).persist(any(PersonaRol.class));
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

        verifyNoInteractions(em);
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

        verifyNoInteractions(em);
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

        verifyNoInteractions(em);
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

        verifyNoInteractions(em);
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

        verifyNoInteractions(em);
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

        verifyNoInteractions(em);
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

        verify(em, never()).persist(any(PersonaRol.class));
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

        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> dao.crear(registro)
        );

        assertEquals("No se puede asignar un rol inactivo", e.getMessage());
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
        when(em.createNamedQuery("PersonaRol.countDuplicados", Long.class))
                .thenReturn(countQuery);
        when(countQuery.setParameter(eq("persona"), any(Persona.class)))
                .thenReturn(countQuery);
        when(countQuery.setParameter(eq("rol"), any(Rol.class)))
                .thenReturn(countQuery);
        when(countQuery.setParameter(eq("clinica"), any(Clinica.class)))
                .thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(2L);

        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> dao.crear(registro)
        );

        assertEquals(
                "La persona seleccionada ya tiene ese rol asignado en esa clínica",
                e.getMessage()
        );

        verify(em, never()).persist(any(PersonaRol.class));
    }

    @Test
    void testCrearSinEntityManagerLanzaIllegalState() {

        PersonaRolDAO daoSinEm = new PersonaRolDAO();

        IllegalStateException e = assertThrows(
                IllegalStateException.class,
                () -> daoSinEm.crear(registroValido())
        );

        assertTrue(
                e.getMessage().contains("EntityManager no está disponible")
        );
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

        Persona persona = new Persona(UUID.randomUUID());

        when(em.find(Rol.class, registro.getIdRol().getIdRol()))
                .thenReturn(rol);
        when(em.find(Clinica.class, registro.getIdClinica().getIdClinica()))
                .thenReturn(clinica);
        when(em.find(Persona.class, registro.getIdPersona().getIdPersona()))
                .thenReturn(persona);
        when(em.createNamedQuery(
                "PersonaRol.countDuplicadosExcluyendoRegistro", Long.class
        )).thenReturn(countQuery);
        when(countQuery.setParameter(eq("persona"), any(Persona.class)))
                .thenReturn(countQuery);
        when(countQuery.setParameter(
                eq("idPersonaRol"),
                eq(registro.getIdPersonaRol())
        )).thenReturn(countQuery);
        when(countQuery.setParameter(eq("rol"), any(Rol.class)))
                .thenReturn(countQuery);
        when(countQuery.setParameter(eq("clinica"), any(Clinica.class)))
                .thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(0L);

        dao.actualizar(registro);

        verify(em).createNamedQuery(
                "PersonaRol.countDuplicadosExcluyendoRegistro", Long.class
        );

        verify(countQuery).setParameter("idPersonaRol", registro.getIdPersonaRol());
        verify(countQuery).setParameter("persona", persona);
        verify(countQuery).setParameter("rol", rol);
        verify(countQuery).setParameter("clinica", clinica);

        verify(em).merge(registro);

        assertSame(persona, registro.getIdPersona());
        assertSame(rol, registro.getIdRol());
        assertSame(clinica, registro.getIdClinica());
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
        when(em.createNamedQuery(
                "PersonaRol.countDuplicadosExcluyendoRegistro", Long.class
        )).thenReturn(countQuery);
        when(countQuery.setParameter(eq("persona"), any(Persona.class)))
                .thenReturn(countQuery);
        when(countQuery.setParameter(
                eq("idPersonaRol"),
                eq(registro.getIdPersonaRol())
        )).thenReturn(countQuery);
        when(countQuery.setParameter(eq("rol"), any(Rol.class)))
                .thenReturn(countQuery);
        when(countQuery.setParameter(eq("clinica"), any(Clinica.class)))
                .thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(1L);

        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> dao.actualizar(registro)
        );

        assertEquals(
                "La persona seleccionada ya tiene ese rol asignado en esa clínica",
                e.getMessage()
        );

        verify(countQuery).setParameter("idPersonaRol", registro.getIdPersonaRol());
        verify(em, never()).merge(any(PersonaRol.class));
    }

    @Test
    void testActualizarConErrorEnConsultaLanzaDAOException() {

        PersonaRol registro = registroValido();
        registro.setIdPersonaRol(UUID.randomUUID());

        Rol rol = new Rol(UUID.randomUUID());
        rol.setActivo(true);

        Clinica clinica = new Clinica(UUID.randomUUID());
        clinica.setActivo(true);

        PersistenceException causa = new PersistenceException("fallo de BD");

        when(em.find(Rol.class, registro.getIdRol().getIdRol()))
                .thenReturn(rol);
        when(em.find(Clinica.class, registro.getIdClinica().getIdClinica()))
                .thenReturn(clinica);
        when(em.find(Persona.class, registro.getIdPersona().getIdPersona()))
                .thenReturn(new Persona(UUID.randomUUID()));
        when(em.createNamedQuery(
                "PersonaRol.countDuplicadosExcluyendoRegistro", Long.class
        )).thenReturn(countQuery);
        when(countQuery.setParameter(anyString(), any()))
                .thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenThrow(causa);

        DAOException e = assertThrows(
                DAOException.class,
                () -> dao.actualizar(registro)
        );

        assertSame(causa, e.getCause());

        verify(em, never()).merge(any(PersonaRol.class));
    }

    @Test
    void testActualizarSinEntityManagerLanzaIllegalState() {

        PersonaRolDAO daoSinEm = new PersonaRolDAO();

        PersonaRol registro = registroValido();
        registro.setIdPersonaRol(UUID.randomUUID());

        IllegalStateException e = assertThrows(
                IllegalStateException.class,
                () -> daoSinEm.actualizar(registro)
        );

        assertTrue(
                e.getMessage().contains("EntityManager no está disponible")
        );
    }

    // ---------- EntityManager ----------

    @Test
    void testGetEntityManger() {

        assertSame(em, dao.getEntityManger());
    }

    @Test
    void testBuscarPorPersonaSinEntityManagerLanzaIllegalState() {

        PersonaRolDAO daoSinEm = new PersonaRolDAO();

        IllegalStateException e = assertThrows(
                IllegalStateException.class,
                () -> daoSinEm.buscarPorPersona(new Persona(UUID.randomUUID()))
        );

        assertTrue(
                e.getMessage().contains("EntityManager no está disponible")
        );
    }

    @Test
    void testBuscarConRelacionesSinEntityManagerLanzaIllegalState() {

        PersonaRolDAO daoSinEm = new PersonaRolDAO();

        IllegalStateException e = assertThrows(
                IllegalStateException.class,
                () -> daoSinEm.buscarConRelaciones(UUID.randomUUID())
        );

        assertTrue(
                e.getMessage().contains("EntityManager no está disponible")
        );
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
