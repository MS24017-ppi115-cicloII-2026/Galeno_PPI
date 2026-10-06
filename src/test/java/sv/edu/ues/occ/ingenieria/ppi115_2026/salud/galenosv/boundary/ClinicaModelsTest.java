package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ClinicaDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Clinica;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ClinicaModelsTest {

    @Mock
    private ClinicaDAO clinicaDAO;

    @InjectMocks
    private ClinicaModels model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(clinicaDAO.findRange(0, 100)).thenReturn(List.of());
    }


    @Test
    void getClinicasActivas() {

        Clinica clinica = new Clinica(UUID.randomUUID());
        when(clinicaDAO.buscarPorActivo(Boolean.TRUE))
                .thenReturn(List.of(clinica));

        assertEquals(1, model.getClinicasActivas().size());
        verify(clinicaDAO).buscarPorActivo(Boolean.TRUE);
    }

    @Test
    void btnNuevoHandlerUsaElRegistroNuevo() {

        model.btnNuevoHandler(null);

        assertEquals(Estado_CRUD.CREAR, model.getEstado());
        assertNotNull(model.getRegistro().getIdClinica());
    }

    @Test
    void getDAO() {

        assertSame(clinicaDAO, model.getDAO());
    }
}
