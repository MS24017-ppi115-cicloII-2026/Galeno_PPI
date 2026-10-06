package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.RolDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Rol;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RolModelsTest {

    @Mock
    private RolDAO rolDAO;

    @InjectMocks
    private RolModels model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(rolDAO.findRange(0, 100)).thenReturn(List.of());
    }


    @Test
    void getRolesActivos() {

        Rol rol = new Rol();
        rol.setNombre("DOCTOR");

        when(rolDAO.buscarRolesActivos()).thenReturn(List.of(rol));

        assertEquals(1, model.getRolesActivos().size());
        verify(rolDAO).buscarRolesActivos();
    }

    @Test
    void btnNuevoHandlerUsaElRegistroNuevo() {

        model.btnNuevoHandler(null);

        assertEquals(Estado_CRUD.CREAR, model.getEstado());
        assertNotNull(model.getRegistro().getIdRol());
    }

    @Test
    void getDAO() {

        assertSame(rolDAO, model.getDAO());
    }
}
