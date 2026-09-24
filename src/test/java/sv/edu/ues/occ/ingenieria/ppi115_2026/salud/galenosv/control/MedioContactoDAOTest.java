package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

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

        when(em.createQuery(
                "SELECT m FROM MedioContacto m "
                + "WHERE LOWER(m.valor) LIKE LOWER(:valor)",
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

        verify(em).createQuery(
                "SELECT m FROM MedioContacto m "
                + "WHERE LOWER(m.valor) LIKE LOWER(:valor)",
                MedioContacto.class
        );

        verify(query).setParameter("valor", "%correo%");
        verify(query).getResultList();
    }

    @Test
    void testBuscarPorValorConNull() {

        assertThrows(
                IllegalArgumentException.class,
                () -> dao.buscarPorValor(null)
        );

        verifyNoInteractions(em);
    }

    @Test
    void testBuscarPorValorConValorVacio() {

        assertThrows(
                IllegalArgumentException.class,
                () -> dao.buscarPorValor("")
        );

        verifyNoInteractions(em);
    }

    @Test
    void testBuscarPorValorConEspacios() {

        assertThrows(
                IllegalArgumentException.class,
                () -> dao.buscarPorValor("   ")
        );

        verifyNoInteractions(em);
    }


    @Test
    void testBuscarPorPersona() {

        Persona persona = new Persona();

        List<MedioContacto> esperados = List.of(
                new MedioContacto()
        );

        when(em.createQuery(
                "SELECT m FROM MedioContacto m "
                + "WHERE m.idPersona = :persona",
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

        verify(em).createQuery(
                "SELECT m FROM MedioContacto m "
                + "WHERE m.idPersona = :persona",
                MedioContacto.class
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
    void testBuscarPorTipoMedioContacto() {

        TipoMedioContacto tipo = new TipoMedioContacto();

        List<MedioContacto> esperados = List.of(
                new MedioContacto(),
                new MedioContacto()
        );

        when(em.createQuery(
                "SELECT m FROM MedioContacto m "
                + "WHERE m.idTipoMedioContacto = :tipoMedioContacto",
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

        verify(em).createQuery(
                "SELECT m FROM MedioContacto m "
                + "WHERE m.idTipoMedioContacto = :tipoMedioContacto",
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

        assertThrows(
                IllegalArgumentException.class,
                () -> dao.buscarPorTipoMedioContacto(null)
        );

        verifyNoInteractions(em);
    }
}
