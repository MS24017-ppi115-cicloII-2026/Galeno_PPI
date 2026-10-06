package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.TipoDocumentoDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.TipoDocumento;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TipoDocumentoModelsTest {

    @Mock
    private TipoDocumentoDAO tipoDocumentoDAO;

    @InjectMocks
    private TipoDocumentoModels model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(tipoDocumentoDAO.findRange(0, 100)).thenReturn(List.of());
    }


    @Test
    void btnNuevoHandlerUsaElRegistroNuevo() {

        model.btnNuevoHandler(null);

        assertEquals(Estado_CRUD.CREAR, model.getEstado());
        assertNotNull(model.getRegistro().getIdTipoDocumento());
    }

    @Test
    void btnSeleccionarRegistroPoneEnModificar() {

        TipoDocumento tipo = new TipoDocumento(UUID.randomUUID());
        model.setRegistros(List.of(tipo));

        model.btnSeleccionarRegistro(tipo.getIdTipoDocumento());

        assertSame(tipo, model.getRegistro());
        assertEquals(Estado_CRUD.MODIFICAR, model.getEstado());
    }

    @Test
    void getDAO() {

        assertSame(tipoDocumentoDAO, model.getDAO());
    }
}
