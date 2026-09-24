package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.TipoDocumento;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TipoDocumentoDAOTest {

    @Mock
    private EntityManager em;

    @Mock
    private TypedQuery<TipoDocumento> query;

    @InjectMocks
    private TipoDocumentoDAO dao;

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

        TipoDocumento tipoDocumento = new TipoDocumento();

        tipoDocumento.setNombre("DUI");

        when(em.createNamedQuery(
                "TipoDocumento.findByNombre",
                TipoDocumento.class
        )).thenReturn(query);

        when(query.getResultList())
                .thenReturn(List.of(tipoDocumento));

        List<TipoDocumento> resultado =
                dao.buscarPorNombre("DUI");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(
                "DUI",
                resultado.get(0).getNombre()
        );

        verify(query).setParameter(
                "nombre",
                "DUI"
        );

        verify(query).getResultList();
    }


    @Test
    void buscarPorIndicacionesNulo() {
        assertThrows(
                IllegalArgumentException.class,
                () -> dao.buscarPorIndicaciones(null)
        );
    }

    @Test
    void buscarPorIndicacionesVacio() {
        assertThrows(
                IllegalArgumentException.class,
                () -> dao.buscarPorIndicaciones("")
        );
    }

    @Test
    void buscarPorIndicacionesCorrectamente() {

        TipoDocumento tipoDocumento = new TipoDocumento();

        tipoDocumento.setNombre("DUI");
        tipoDocumento.setIndicaciones(
                "Documento de identidad"
        );

        when(em.createNamedQuery(
                "TipoDocumento.findByIndicaciones",
                TipoDocumento.class
        )).thenReturn(query);

        when(query.getResultList())
                .thenReturn(List.of(tipoDocumento));

        List<TipoDocumento> resultado =
                dao.buscarPorIndicaciones(
                        "Documento de identidad"
                );

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(
                "Documento de identidad",
                resultado.get(0).getIndicaciones()
        );

        verify(query).setParameter(
                "indicaciones",
                "Documento de identidad"
        );

        verify(query).getResultList();
    }


    @Test
    void buscarPorExpresionRegularNula() {
        assertThrows(
                IllegalArgumentException.class,
                () -> dao.buscarPorExpresionRegular(null)
        );
    }

    @Test
    void buscarPorExpresionRegularVacia() {
        assertThrows(
                IllegalArgumentException.class,
                () -> dao.buscarPorExpresionRegular("")
        );
    }

    @Test
    void buscarPorExpresionRegularCorrectamente() {

        TipoDocumento tipoDocumento = new TipoDocumento();

        tipoDocumento.setNombre("DUI");
        tipoDocumento.setExpresionRegular(
                "^[0-9]{8}-[0-9]$"
        );

        when(em.createNamedQuery(
                "TipoDocumento.findByExpresionRegular",
                TipoDocumento.class
        )).thenReturn(query);

        when(query.getResultList())
                .thenReturn(List.of(tipoDocumento));

        List<TipoDocumento> resultado =
                dao.buscarPorExpresionRegular(
                        "^[0-9]{8}-[0-9]$"
                );

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(
                "^[0-9]{8}-[0-9]$",
                resultado.get(0).getExpresionRegular()
        );

        verify(query).setParameter(
                "expresionRegular",
                "^[0-9]{8}-[0-9]$"
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

        TipoDocumento tipoDocumento = new TipoDocumento();

        tipoDocumento.setNombre("DUI");
        tipoDocumento.setActivo(true);

        when(em.createNamedQuery(
                "TipoDocumento.findByActivo",
                TipoDocumento.class
        )).thenReturn(query);

        when(query.getResultList())
                .thenReturn(List.of(tipoDocumento));

        List<TipoDocumento> resultado =
                dao.buscarPorActivo(true);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).getActivo());

        verify(query).setParameter(
                "activo",
                true
        );

        verify(query).getResultList();
    }
}
