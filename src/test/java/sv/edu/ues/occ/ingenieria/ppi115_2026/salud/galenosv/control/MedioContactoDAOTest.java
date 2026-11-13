package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import jakarta.persistence.PersistenceException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.MedioContacto;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Persona;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.TipoMedioContacto;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedioContactoDAOTest {

    @Mock
    private EntityManager em;

    @Mock
    private TypedQuery<MedioContacto> query;

    @InjectMocks
    private MedioContactoDAO dao;

    @BeforeEach
    void setUp() {
    }


    @Test
    void testBuscarPorValor() {

        String valor = "correo";

        List<MedioContacto> esperados = List.of(
                new MedioContacto(),
                new MedioContacto()
        );

        when(em.createNamedQuery(
                "MedioContacto.findByValorLike",
                MedioContacto.class
        )).thenReturn(query);

        when(query.setParameter("valor", "%correo%"))
                .thenReturn(query);

        when(query.getResultList())
                .thenReturn(esperados);

        List<MedioContacto> resultado = dao.buscarPorValor(valor);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(esperados, resultado);

        verify(em).createNamedQuery(
                "MedioContacto.findByValorLike",
                MedioContacto.class
        );

        verify(query).setParameter("valor", "%correo%");
        verify(query).getResultList();
    }

    @Test
    void testBuscarPorValorConNull() {

        IllegalArgumentException excepcion = assertThrows(
                IllegalArgumentException.class,
                () -> dao.buscarPorValor(null)
        );

        assertEquals("El valor no puede ser nulo o vacío",
                excepcion.getMessage());
        verifyNoInteractions(em);
    }

    @Test
    void testBuscarPorValorConValorVacio() {

        IllegalArgumentException excepcion = assertThrows(
                IllegalArgumentException.class,
                () -> dao.buscarPorValor("")
        );

        assertEquals("El valor no puede ser nulo o vacío",
                excepcion.getMessage());
        verifyNoInteractions(em);
    }

    @Test
    void testBuscarPorValorConEspacios() {

        IllegalArgumentException excepcion = assertThrows(
                IllegalArgumentException.class,
                () -> dao.buscarPorValor("   ")
        );

        assertEquals("El valor no puede ser nulo o vacío",
                excepcion.getMessage());
        verifyNoInteractions(em);
    }

    @Test
    void testBuscarPorValorListaVacia() {

        when(em.createNamedQuery(
                "MedioContacto.findByValorLike",
                MedioContacto.class
        )).thenReturn(query);

        when(query.setParameter("valor", "%correo%"))
                .thenReturn(query);

        when(query.getResultList())
                .thenReturn(List.of());

        List<MedioContacto> resultado = dao.buscarPorValor("correo");

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void testBuscarPorValorExcepcionServidor() {

        RuntimeException causa = new RuntimeException("Fallo simulado de BD");

        when(em.createNamedQuery(
                "MedioContacto.findByValorLike",
                MedioContacto.class
        )).thenReturn(query);

        when(query.setParameter("valor", "%correo%"))
                .thenReturn(query);

        when(query.getResultList())
                .thenThrow(causa);

        DAOException excepcion = assertThrows(
                DAOException.class,
                () -> dao.buscarPorValor("correo")
        );

        assertSame(causa, excepcion.getCause());
    }

    @Test
    void testBuscarPorValorExcepcionPersistencia() {

        PersistenceException causa
                = new PersistenceException("Fallo simulado de BD");

        when(em.createNamedQuery(
                "MedioContacto.findByValorLike",
                MedioContacto.class
        )).thenReturn(query);

        when(query.setParameter("valor", "%correo%"))
                .thenReturn(query);

        when(query.getResultList())
                .thenThrow(causa);

        DAOException excepcion = assertThrows(
                DAOException.class,
                () -> dao.buscarPorValor("correo")
        );

        assertSame(causa, excepcion.getCause());
    }

    @Test
    void testBuscarPorValorEntityManagerNulo() {

        MedioContactoDAO daoSinEm = new MedioContactoDAO();

        IllegalStateException excepcion = assertThrows(
                IllegalStateException.class,
                () -> daoSinEm.buscarPorValor("correo")
        );

        assertTrue(excepcion.getMessage()
                .contains("EntityManager no está disponible"));
    }


    @Test
    void testBuscarPorPersona() {

        Persona persona = new Persona();

        List<MedioContacto> esperados = List.of(
                new MedioContacto()
        );

        when(em.createNamedQuery(
                "MedioContacto.findByPersona",
                MedioContacto.class
        )).thenReturn(query);

        when(query.setParameter("persona", persona))
                .thenReturn(query);

        when(query.getResultList())
                .thenReturn(esperados);

        List<MedioContacto> resultado =
                dao.buscarPorPersona(persona);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(esperados, resultado);

        verify(em).createNamedQuery(
                "MedioContacto.findByPersona",
                MedioContacto.class
        );

        verify(query).setParameter("persona", persona);
        verify(query).getResultList();
    }

    @Test
    void testBuscarPorPersonaConNull() {

        IllegalArgumentException excepcion = assertThrows(
                IllegalArgumentException.class,
                () -> dao.buscarPorPersona(null)
        );

        assertEquals("La persona no puede ser nula",
                excepcion.getMessage());
        verifyNoInteractions(em);
    }

    @Test
    void testBuscarPorPersonaListaVacia() {

        Persona persona = new Persona();

        when(em.createNamedQuery(
                "MedioContacto.findByPersona",
                MedioContacto.class
        )).thenReturn(query);

        when(query.setParameter("persona", persona))
                .thenReturn(query);

        when(query.getResultList())
                .thenReturn(List.of());

        List<MedioContacto> resultado =
                dao.buscarPorPersona(persona);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void testBuscarPorPersonaExcepcionServidor() {

        Persona persona = new Persona();

        RuntimeException causa = new RuntimeException("Fallo simulado de BD");

        when(em.createNamedQuery(
                "MedioContacto.findByPersona",
                MedioContacto.class
        )).thenReturn(query);

        when(query.setParameter("persona", persona))
                .thenReturn(query);

        when(query.getResultList())
                .thenThrow(causa);

        DAOException excepcion = assertThrows(
                DAOException.class,
                () -> dao.buscarPorPersona(persona)
        );

        assertSame(causa, excepcion.getCause());
    }

    @Test
    void testBuscarPorPersonaEntityManagerNulo() {

        MedioContactoDAO daoSinEm = new MedioContactoDAO();

        IllegalStateException excepcion = assertThrows(
                IllegalStateException.class,
                () -> daoSinEm.buscarPorPersona(new Persona())
        );

        assertTrue(excepcion.getMessage()
                .contains("EntityManager no está disponible"));
    }


    @Test
    void testBuscarPorTipoMedioContacto() {

        TipoMedioContacto tipo = new TipoMedioContacto();

        List<MedioContacto> esperados = List.of(
                new MedioContacto(),
                new MedioContacto()
        );

        when(em.createNamedQuery(
                "MedioContacto.findByTipoMedioContacto",
                MedioContacto.class
        )).thenReturn(query);

        when(query.setParameter(
                "tipoMedioContacto",
                tipo
        )).thenReturn(query);

        when(query.getResultList())
                .thenReturn(esperados);

        List<MedioContacto> resultado =
                dao.buscarPorTipoMedioContacto(tipo);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(esperados, resultado);

        verify(em).createNamedQuery(
                "MedioContacto.findByTipoMedioContacto",
                MedioContacto.class
        );

        verify(query).setParameter(
                "tipoMedioContacto",
                tipo
        );

        verify(query).getResultList();
    }

    @Test
    void testBuscarPorTipoMedioContactoConNull() {

        IllegalArgumentException excepcion = assertThrows(
                IllegalArgumentException.class,
                () -> dao.buscarPorTipoMedioContacto(null)
        );

        assertEquals("El tipo de medio de contacto no puede ser nulo",
                excepcion.getMessage());
        verifyNoInteractions(em);
    }

    @Test
    void testBuscarPorTipoMedioContactoListaVacia() {

        TipoMedioContacto tipo = new TipoMedioContacto();

        when(em.createNamedQuery(
                "MedioContacto.findByTipoMedioContacto",
                MedioContacto.class
        )).thenReturn(query);

        when(query.setParameter(
                "tipoMedioContacto",
                tipo
        )).thenReturn(query);

        when(query.getResultList())
                .thenReturn(List.of());

        List<MedioContacto> resultado =
                dao.buscarPorTipoMedioContacto(tipo);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void testBuscarPorTipoMedioContactoExcepcionServidor() {

        TipoMedioContacto tipo = new TipoMedioContacto();

        RuntimeException causa = new RuntimeException("Fallo simulado de BD");

        when(em.createNamedQuery(
                "MedioContacto.findByTipoMedioContacto",
                MedioContacto.class
        )).thenReturn(query);

        when(query.setParameter(
                "tipoMedioContacto",
                tipo
        )).thenReturn(query);

        when(query.getResultList())
                .thenThrow(causa);

        DAOException excepcion = assertThrows(
                DAOException.class,
                () -> dao.buscarPorTipoMedioContacto(tipo)
        );

        assertSame(causa, excepcion.getCause());
    }

    @Test
    void testBuscarPorTipoMedioContactoEntityManagerNulo() {

        MedioContactoDAO daoSinEm = new MedioContactoDAO();

        IllegalStateException excepcion = assertThrows(
                IllegalStateException.class,
                () -> daoSinEm.buscarPorTipoMedioContacto(new TipoMedioContacto())
        );

        assertTrue(excepcion.getMessage()
                .contains("EntityManager no está disponible"));
    }
}
