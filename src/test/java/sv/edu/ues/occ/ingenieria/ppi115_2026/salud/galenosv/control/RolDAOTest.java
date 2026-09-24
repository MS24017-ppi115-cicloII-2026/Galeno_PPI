package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Rol;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RolDAOTest {

    @Mock
    private EntityManager em;

    @Mock
    private TypedQuery<Rol> query;

    @InjectMocks
    private RolDAO dao;

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

        Rol rol = new Rol();

        rol.setNombre("Administrador");

        when(em.createQuery(
                anyString(),
                eq(Rol.class)
        )).thenReturn(query);

        when(query.getResultList())
                .thenReturn(List.of(rol));

        List<Rol> resultado =
                dao.buscarPorNombre("Admin");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(
                "Administrador",
                resultado.get(0).getNombre()
        );

        verify(query).setParameter(
                "nombre",
                "%Admin%"
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

        Rol rol = new Rol();

        rol.setNombre("Administrador");
        rol.setActivo(true);

        when(em.createNamedQuery(
                "Rol.findByActivo",
                Rol.class
        )).thenReturn(query);

        when(query.getResultList())
                .thenReturn(List.of(rol));

        List<Rol> resultado =
                dao.buscarPorActivo(true);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(
                "Administrador",
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
