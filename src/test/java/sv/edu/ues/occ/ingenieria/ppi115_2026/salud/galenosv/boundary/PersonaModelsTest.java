package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.PersonaDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Persona;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PersonaModelsTest {

    @Mock
    private PersonaDAO personaDAO;

    @InjectMocks
    private PersonaModels model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(personaDAO.findRange(0, 100)).thenReturn(List.of());
    }


    @Test
    void getPersonasDisponibles() {

        Persona persona = new Persona(UUID.randomUUID());
        when(personaDAO.buscarPersonasSinRol())
                .thenReturn(List.of(persona));

        assertEquals(1, model.getPersonasDisponibles().size());
        verify(personaDAO).buscarPersonasSinRol();
    }


    @Test
    void getPersonaSeleccionadaEnEstadoModificarDevuelveLaPersona() {

        Persona persona = new Persona(UUID.randomUUID());
        model.setRegistros(List.of(persona));

        model.btnSeleccionarRegistro(persona.getIdPersona());

        assertSame(persona, model.getPersonaSeleccionada());
        assertTrue(model.isHayPersonaSeleccionada());
    }

    @Test
    void getPersonaSeleccionadaTrasCancelarDevuelveNulo() {

        Persona persona = new Persona(UUID.randomUUID());
        model.setRegistros(List.of(persona));
        model.btnSeleccionarRegistro(persona.getIdPersona());

        model.btnCancelar();

        assertNull(model.getPersonaSeleccionada());
    }

    @Test
    void getDAO() {

        assertSame(personaDAO, model.getDAO());
    }
}
