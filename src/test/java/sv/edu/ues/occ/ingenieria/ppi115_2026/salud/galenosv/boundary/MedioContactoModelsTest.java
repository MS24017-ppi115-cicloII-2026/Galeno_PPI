package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.MedioContactoDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.TipoMedioContactoDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.MedioContacto;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Persona;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.TipoMedioContacto;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MedioContactoModelsTest {

    @Mock
    private MedioContactoDAO medioContactoDAO;

    @Mock
    private TipoMedioContactoDAO tipoMedioContactoDAO;

    @Mock
    private PersonaModels personaModel;

    @Mock
    private FacesContext fc;

    @InjectMocks
    private MedioContactoModels model;

    private Persona persona;
    private TipoMedioContacto tipo;
    private MedioContacto contacto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        persona = new Persona(UUID.randomUUID());
        persona.setNombres("Ana");
        persona.setApellidos("Lopez");

        tipo = new TipoMedioContacto(UUID.randomUUID());
        tipo.setNombre("Correo");
        tipo.setActivo(Boolean.TRUE);

        contacto = new MedioContacto(UUID.randomUUID());
        contacto.setIdPersona(persona);
        contacto.setIdTipoMedioContacto(tipo);
        contacto.setValor("ana@correo.com");

        when(personaModel.getPersonaSeleccionada()).thenReturn(persona);
    }


    // ---------- Lista filtrada por persona ----------

    @Test
    void getRegistrosDePersonaUsaBusquedaPorPersona() {

        when(medioContactoDAO.buscarPorPersona(persona))
                .thenReturn(List.of(contacto));

        List<MedioContacto> resultado = model.getRegistrosDePersona();

        assertEquals(1, resultado.size());
        verify(medioContactoDAO).buscarPorPersona(persona);
    }

    @Test
    void getRegistrosDePersonaNoRecargaSiLaPersonaNoCambio() {

        when(medioContactoDAO.buscarPorPersona(persona))
                .thenReturn(List.of(contacto));

        model.getRegistrosDePersona();
        model.getRegistrosDePersona();

        verify(medioContactoDAO, times(1)).buscarPorPersona(persona);
    }

    @Test
    void getRegistrosDePersonaRecargaCuandoCambiaLaPersona() {

        Persona otra = new Persona(UUID.randomUUID());
        otra.setNombres("Luis");
        otra.setApellidos("Perez");

        when(medioContactoDAO.buscarPorPersona(persona))
                .thenReturn(List.of(contacto));
        when(medioContactoDAO.buscarPorPersona(otra))
                .thenReturn(List.of());

        model.getRegistrosDePersona();

        when(personaModel.getPersonaSeleccionada()).thenReturn(otra);
        List<MedioContacto> resultado = model.getRegistrosDePersona();

        assertTrue(resultado.isEmpty());
        verify(medioContactoDAO, times(1)).buscarPorPersona(otra);
    }

    @Test
    void getRegistrosDePersonaLimpiaElRegistroEnEdicion() {

        when(medioContactoDAO.buscarPorPersona(persona))
                .thenReturn(List.of(contacto));

        model.setRegistro(contacto);
        model.btnNuevoHandler(null);

        model.getRegistrosDePersona();

        assertNull(model.getRegistro());
        assertEquals(Estado_CRUD.NINGUNO, model.getEstado());
    }


    @Test
    void getTiposActivos() {

        when(tipoMedioContactoDAO.buscarPorActivo(true))
                .thenReturn(List.of(tipo));

        assertEquals(1, model.getTiposActivos().size());
        verify(tipoMedioContactoDAO).buscarPorActivo(true);
    }


    @Test
    void crearSinPersonaMuestraError() {

        contacto.setIdPersona(null);
        model.setRegistro(contacto);

        model.btnCrearhandler(null);

        verify(medioContactoDAO, never()).crear(any());
        verify(fc).validationFailed();
    }

    @Test
    void crearConValorVacioMuestraError() {

        contacto.setValor("  ");
        model.setRegistro(contacto);

        model.btnCrearhandler(null);

        verify(medioContactoDAO, never()).crear(any());
        verify(fc).validationFailed();
    }

    @Test
    void crearConTipoInexistenteMuestraError() {

        when(tipoMedioContactoDAO.buscar(tipo.getIdTipoMedioContacto()))
                .thenReturn(null);
        model.setRegistro(contacto);

        model.btnCrearhandler(null);

        verify(medioContactoDAO, never()).crear(any());
        verify(fc).validationFailed();
    }

    @Test
    void crearConTipoInactivoMuestraError() {

        tipo.setActivo(Boolean.FALSE);
        when(tipoMedioContactoDAO.buscar(tipo.getIdTipoMedioContacto()))
                .thenReturn(tipo);
        model.setRegistro(contacto);

        model.btnCrearhandler(null);

        verify(medioContactoDAO, never()).crear(any());
        verify(fc).validationFailed();
    }

    @Test
    void crearConFormatoInvalidoMuestraError() {

        tipo.setExpresionRegular("^\\\\S+@\\\\S+$");
        when(tipoMedioContactoDAO.buscar(tipo.getIdTipoMedioContacto()))
                .thenReturn(tipo);
        model.setRegistro(contacto);

        model.btnCrearhandler(null);

        verify(medioContactoDAO, never()).crear(any());
        verify(fc).addMessage(isNull(), any(FacesMessage.class));
        verify(fc).validationFailed();
    }

    @Test
    void crearConFormatoInvalidoMuestraLasIndicacionesDelTipo() {

        tipo.setExpresionRegular("^\\\\S+@\\\\S+$");
        tipo.setIndicaciones("Revise el correo");
        when(tipoMedioContactoDAO.buscar(tipo.getIdTipoMedioContacto()))
                .thenReturn(tipo);
        model.setRegistro(contacto);

        model.btnCrearhandler(null);

        ArgumentCaptor<FacesMessage> captor =
                ArgumentCaptor.forClass(FacesMessage.class);

        verify(fc).addMessage(isNull(), captor.capture());

        assertEquals("Revise el correo", captor.getValue().getDetail());
    }

    @Test
    void crearConExpresionRegularMalConfiguradaMuestraError() {

        tipo.setExpresionRegular("[abc");
        when(tipoMedioContactoDAO.buscar(tipo.getIdTipoMedioContacto()))
                .thenReturn(tipo);
        model.setRegistro(contacto);

        model.btnCrearhandler(null);

        verify(medioContactoDAO, never()).crear(any());
        verify(fc).validationFailed();
    }

    @Test
    void crearConTodosLosDatosValidosGuarda() {

        when(tipoMedioContactoDAO.buscar(tipo.getIdTipoMedioContacto()))
                .thenReturn(tipo);
        when(medioContactoDAO.findRange(0, 100)).thenReturn(List.of());

        model.setRegistro(contacto);
        model.btnCrearhandler(null);

        verify(medioContactoDAO).crear(contacto);
        assertNull(model.getRegistro());
        assertEquals(Estado_CRUD.NINGUNO, model.getEstado());
        verify(fc, never()).validationFailed();
    }

    @Test
    void crearRecortaElValorEnLosEspacios() {

        contacto.setValor("  ana@correo.com  ");
        when(tipoMedioContactoDAO.buscar(tipo.getIdTipoMedioContacto()))
                .thenReturn(tipo);
        when(medioContactoDAO.findRange(0, 100)).thenReturn(List.of());

        model.setRegistro(contacto);
        model.btnCrearhandler(null);

        assertEquals("ana@correo.com", contacto.getValor());
    }

    @Test
    void crearAsignaFechaDeCreacionSiFalta() {

        contacto.setFechaCreacion(null);
        when(tipoMedioContactoDAO.buscar(tipo.getIdTipoMedioContacto()))
                .thenReturn(tipo);
        when(medioContactoDAO.findRange(0, 100)).thenReturn(List.of());

        model.setRegistro(contacto);
        model.btnCrearhandler(null);

        assertNotNull(contacto.getFechaCreacion());
        assertTrue(contacto.getFechaCreacion().getTime() <= new Date().getTime());
    }


    // ---------- Modificar y eliminar ----------

    @Test
    void modificarConDatosValidosActualiza() {

        when(tipoMedioContactoDAO.buscar(tipo.getIdTipoMedioContacto()))
                .thenReturn(tipo);
        when(medioContactoDAO.findRange(0, 100)).thenReturn(List.of());

        model.setRegistro(contacto);
        model.btnModificarHandler();

        verify(medioContactoDAO).actualizar(contacto);
        assertNull(model.getRegistro());
    }

    @Test
    void eliminarRecargaLaListaDeLaPersona() {

        when(medioContactoDAO.buscarPorPersona(persona))
                .thenReturn(List.of(contacto));

        model.setRegistros(List.of(contacto));

        model.btnEliminarHandler(contacto.getIdMedioContacto());

        verify(medioContactoDAO).eliminar(contacto.getIdMedioContacto());
        verify(medioContactoDAO).buscarPorPersona(persona);
    }
}
