package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.TipoMedioContacto;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TipoMedioContactoDAOTest {

    @Mock
    private EntityManager em;

    @Mock
    private TypedQuery<TipoMedioContacto> query;

    @InjectMocks
    private TipoMedioContactoDAO dao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void buscarPorNombreNulo() {
        assertThrows(
                IllegalArgumentException.class,
                () -> dao.buscarPorNombre(null)
        );
    }

    @Test
    void buscarPorNombreVacio() {
        assertThrows(
                IllegalArgumentException.class,
                () -> dao.buscarPorNombre("")
        );
    }

    @Test
    void buscarPorNombreCorrectamente() {

        TipoMedioContacto tipoMedioContacto =
                new TipoMedioContacto();

        tipoMedioContacto.setNombre("Correo electrónico");

        when(em.createQuery(
                anyString(),
                eq(TipoMedioContacto.class)
        )).thenReturn(query);

        when(query.getResultList())
                .thenReturn(List.of(tipoMedioContacto));

        List<TipoMedioContacto> resultado =
                dao.buscarPorNombre("Correo");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(
                "Correo electrónico",
                resultado.get(0).getNombre()
        );

        verify(query).setParameter(
                "nombre",
                "%Correo%"
        );

        verify(query).getResultList();
    }

    @Test
    void buscarPorActivoNulo() {
        assertThrows(
                IllegalArgumentException.class,
                () -> dao.buscarPorActivo(null)
        );
    }

    @Test
    void buscarPorActivoCorrectamente() {

        TipoMedioContacto tipoMedioContacto =
                new TipoMedioContacto();

        tipoMedioContacto.setNombre("Correo electrónico");
        tipoMedioContacto.setActivo(true);

        when(em.createNamedQuery(
                "TipoMedioContacto.findByActivo",
                TipoMedioContacto.class
        )).thenReturn(query);

        when(query.getResultList())
                .thenReturn(List.of(tipoMedioContacto));

        List<TipoMedioContacto> resultado =
                dao.buscarPorActivo(true);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(
                "Correo electrónico",
                resultado.get(0).getNombre()
        );
        assertTrue(resultado.get(0).getActivo());

        verify(query).setParameter(
                "activo",
                true
        );

        verify(query).getResultList();
    }
}