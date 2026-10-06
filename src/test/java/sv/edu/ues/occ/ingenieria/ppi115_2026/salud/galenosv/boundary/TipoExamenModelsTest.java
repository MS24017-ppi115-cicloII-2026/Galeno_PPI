package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.TipoExamenDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.TipoExamen;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TipoExamenModelsTest {

    @Mock
    private TipoExamenDAO tipoExamenDAO;

    @Mock
    private FacesContext fc;

    @InjectMocks
    private TipoExamenModels model;

    private TipoExamen tipo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        tipo = new TipoExamen(UUID.randomUUID());
        tipo.setNombre("Hemograma");
        tipo.setActivo(Boolean.TRUE);

        when(tipoExamenDAO.findRange(0, 100)).thenReturn(List.of());
        when(tipoExamenDAO.buscarPorNombre(any())).thenReturn(List.of());
    }


    @Test
    void crearSinNombreMuestraError() {

        tipo.setNombre("   ");
        model.setRegistro(tipo);

        model.btnCrearhandler(null);

        verify(tipoExamenDAO, never()).crear(any());
        verify(fc).addMessage(isNull(), any(FacesMessage.class));
        verify(fc).validationFailed();
    }

    @Test
    void crearConNombreDuplicadoMuestraError() {

        TipoExamen otro = new TipoExamen(UUID.randomUUID());
        otro.setNombre("Hemograma");

        when(tipoExamenDAO.buscarPorNombre("Hemograma"))
                .thenReturn(List.of(otro));

        model.setRegistro(tipo);
        model.btnCrearhandler(null);

        verify(tipoExamenDAO, never()).crear(any());
        verify(fc).validationFailed();
    }

    @Test
    void crearConNombreValidoGuarda() {

        model.setRegistro(tipo);
        model.btnCrearhandler(null);

        verify(tipoExamenDAO).crear(tipo);
        assertNull(model.getRegistro());
    }
}
