package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.TipoMedioContactoDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.TipoMedioContacto;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TipoMedioContactoModelsTest {

    @Mock
    private TipoMedioContactoDAO tipoMedioContactoDAO;

    @InjectMocks
    private TipoMedioContactoModels model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(tipoMedioContactoDAO.findRange(0, 100)).thenReturn(List.of());
    }


    @Test
    void btnNuevoHandlerUsaElRegistroNuevo() {

        model.btnNuevoHandler(null);

        assertEquals(Estado_CRUD.CREAR, model.getEstado());
        assertNotNull(model.getRegistro().getIdTipoMedioContacto());
    }

    @Test
    void btnSeleccionarRegistroPoneEnModificar() {

        TipoMedioContacto tipo = new TipoMedioContacto(UUID.randomUUID());
        model.setRegistros(List.of(tipo));

        model.btnSeleccionarRegistro(tipo.getIdTipoMedioContacto());

        assertSame(tipo, model.getRegistro());
        assertEquals(Estado_CRUD.MODIFICAR, model.getEstado());
    }

    @Test
    void getDAO() {

        assertSame(tipoMedioContactoDAO, model.getDAO());
    }
}
