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
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DocumentoDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.TipoDocumentoDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Documento;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Persona;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.TipoDocumento;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DocumentoModelsTest {

    @Mock
    private DocumentoDAO documentoDAO;

    @Mock
    private TipoDocumentoDAO tipoDocumentoDAO;

    @Mock
    private PersonaModels personaModel;

    @Mock
    private FacesContext fc;

    @InjectMocks
    private DocumentoModels model;

    private Persona persona;
    private TipoDocumento tipo;
    private Documento documento;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        persona = new Persona(UUID.randomUUID());
        persona.setNombres("Ana");
        persona.setApellidos("Lopez");

        tipo = new TipoDocumento(UUID.randomUUID());
        tipo.setNombre("Pasaporte");
        tipo.setActivo(Boolean.TRUE);

        documento = new Documento(UUID.randomUUID());
        documento.setIdPersona(persona);
        documento.setIdTipoDocumento(tipo);
        documento.setValor("P-123");

        when(personaModel.getPersonaSeleccionada()).thenReturn(persona);
    }


    // ---------- Lista filtrada por persona ----------

    @Test
    void getRegistrosDePersonaUsaBusquedaPorPersona() {

        when(documentoDAO.buscarPorPersona(persona.getIdPersona()))
                .thenReturn(List.of(documento));

        List<Documento> resultado = model.getRegistrosDePersona();

        assertEquals(1, resultado.size());
        verify(documentoDAO).buscarPorPersona(persona.getIdPersona());
        verify(documentoDAO, never()).findRange(anyInt(), anyInt());
    }

    @Test
    void getRegistrosDePersonaNoRecargaSiLaPersonaNoCambio() {

        when(documentoDAO.buscarPorPersona(persona.getIdPersona()))
                .thenReturn(List.of(documento));

        model.getRegistrosDePersona();
        model.getRegistrosDePersona();
        model.getRegistrosDePersona();

        verify(documentoDAO, times(1)).buscarPorPersona(persona.getIdPersona());
    }

    @Test
    void getRegistrosDePersonaRecargaCuandoCambiaLaPersona() {

        Persona otra = new Persona(UUID.randomUUID());
        otra.setNombres("Luis");
        otra.setApellidos("Perez");

        when(documentoDAO.buscarPorPersona(persona.getIdPersona()))
                .thenReturn(List.of(documento));
        when(documentoDAO.buscarPorPersona(otra.getIdPersona()))
                .thenReturn(List.of());

        model.getRegistrosDePersona();

        when(personaModel.getPersonaSeleccionada()).thenReturn(otra);
        List<Documento> resultado = model.getRegistrosDePersona();

        assertTrue(resultado.isEmpty());
        verify(documentoDAO, times(1)).buscarPorPersona(otra.getIdPersona());
    }

    @Test
    void getRegistrosDePersonaLimpiaElRegistroEnEdicion() {

        when(documentoDAO.buscarPorPersona(persona.getIdPersona()))
                .thenReturn(List.of(documento));

        model.setRegistro(documento);
        model.btnNuevoHandler(null);

        model.getRegistrosDePersona();

        assertNull(model.getRegistro());
        assertEquals(Estado_CRUD.NINGUNO, model.getEstado());
    }


    @Test
    void getTiposActivos() {

        when(tipoDocumentoDAO.buscarPorActivo(true))
                .thenReturn(List.of(tipo));

        assertEquals(1, model.getTiposActivos().size());
        verify(tipoDocumentoDAO).buscarPorActivo(true);
    }


    @Test
    void crearSinPersonaMuestraError() {

        documento.setIdPersona(null);
        model.setRegistro(documento);

        model.btnCrearhandler(null);

        verify(documentoDAO, never()).crear(any());
        verify(fc).addMessage(isNull(), any(FacesMessage.class));
        verify(fc).validationFailed();
    }

    @Test
    void crearSinTipoDocumentoMuestraError() {

        documento.setIdTipoDocumento(null);
        model.setRegistro(documento);

        model.btnCrearhandler(null);

        verify(documentoDAO, never()).crear(any());
        verify(fc).validationFailed();
    }

    @Test
    void crearConTipoInexistenteMuestraError() {

        when(tipoDocumentoDAO.buscar(tipo.getIdTipoDocumento()))
                .thenReturn(null);
        model.setRegistro(documento);

        model.btnCrearhandler(null);

        verify(documentoDAO, never()).crear(any());
        verify(fc).validationFailed();
    }

    @Test
    void crearConTipoInactivoMuestraError() {

        tipo.setActivo(Boolean.FALSE);
        when(tipoDocumentoDAO.buscar(tipo.getIdTipoDocumento()))
                .thenReturn(tipo);
        model.setRegistro(documento);

        model.btnCrearhandler(null);

        verify(documentoDAO, never()).crear(any());
        verify(fc).validationFailed();
    }

    @Test
    void crearConFormatoInvalidoMuestraError() {

        tipo.setExpresionRegular("^[0-9]{8}-[0-9]$");
        when(tipoDocumentoDAO.buscar(tipo.getIdTipoDocumento()))
                .thenReturn(tipo);
        model.setRegistro(documento);

        model.btnCrearhandler(null);

        verify(documentoDAO, never()).crear(any());
        verify(fc).addMessage(isNull(), any(FacesMessage.class));
        verify(fc).validationFailed();
    }

    @Test
    void crearConFormatoInvalidoMuestraLasIndicacionesDelTipo() {

        tipo.setExpresionRegular("^[0-9]{8}$");
        tipo.setIndicaciones("Use solo los numeros");
        when(tipoDocumentoDAO.buscar(tipo.getIdTipoDocumento()))
                .thenReturn(tipo);
        model.setRegistro(documento);

        model.btnCrearhandler(null);

        org.mockito.ArgumentCaptor<FacesMessage> captor =
                org.mockito.ArgumentCaptor.forClass(FacesMessage.class);

        verify(fc).addMessage(isNull(), captor.capture());

        assertEquals(
                "Use solo los numeros",
                captor.getValue().getDetail()
        );
    }

    @Test
    void crearConExpresionRegularMalConfiguradaMuestraError() {

        tipo.setExpresionRegular("[abc");
        when(tipoDocumentoDAO.buscar(tipo.getIdTipoDocumento()))
                .thenReturn(tipo);
        model.setRegistro(documento);

        model.btnCrearhandler(null);

        verify(documentoDAO, never()).crear(any());
        verify(fc).validationFailed();
    }

    @Test
    void crearConDocumentoUnicoDuplicadoMuestraError() {

        tipo.setNombre("DUI");
        when(tipoDocumentoDAO.buscar(tipo.getIdTipoDocumento()))
                .thenReturn(tipo);
        when(documentoDAO.existeDuplicado(
                any(), any(), any(), any()
        )).thenReturn(false);
        when(documentoDAO.existeOtroDocumentoDelTipo(
                persona.getIdPersona(),
                tipo.getIdTipoDocumento(),
                documento.getIdDocumento()
        )).thenReturn(true);
        model.setRegistro(documento);

        model.btnCrearhandler(null);

        verify(documentoDAO, never()).crear(any());
        verify(fc).validationFailed();
    }

    @Test
    void crearConTodosLosDatosValidosGuarda() {

        when(tipoDocumentoDAO.buscar(tipo.getIdTipoDocumento()))
                .thenReturn(tipo);
        when(documentoDAO.existeDuplicado(any(), any(), any(), any()))
                .thenReturn(false);
        when(documentoDAO.findRange(0, 100)).thenReturn(List.of());

        model.setRegistro(documento);
        model.btnCrearhandler(null);

        verify(documentoDAO).crear(documento);
        assertNull(model.getRegistro());
        assertEquals(Estado_CRUD.NINGUNO, model.getEstado());
        verify(fc, never()).validationFailed();
    }

    @Test
    void crearRecortaElValorEnLosEspacios() {

        documento.setValor("  P-123  ");
        when(tipoDocumentoDAO.buscar(tipo.getIdTipoDocumento()))
                .thenReturn(tipo);
        when(documentoDAO.existeDuplicado(any(), any(), any(), any()))
                .thenReturn(false);
        when(documentoDAO.findRange(0, 100)).thenReturn(List.of());

        model.setRegistro(documento);
        model.btnCrearhandler(null);

        assertEquals("P-123", documento.getValor());
    }


    // ---------- Modificar ----------

    @Test
    void modificarConDatosValidosActualiza() {

        when(tipoDocumentoDAO.buscar(tipo.getIdTipoDocumento()))
                .thenReturn(tipo);
        when(documentoDAO.existeDuplicado(any(), any(), any(), any()))
                .thenReturn(false);
        when(documentoDAO.findRange(0, 100)).thenReturn(List.of());

        model.setRegistro(documento);
        model.btnModificarHandler();

        verify(documentoDAO).actualizar(documento);
        assertNull(model.getRegistro());
    }

    @Test
    void modificarConDuplicadoNoActualiza() {

        when(tipoDocumentoDAO.buscar(tipo.getIdTipoDocumento()))
                .thenReturn(tipo);
        when(documentoDAO.existeDuplicado(any(), any(), any(), any()))
                .thenReturn(true);

        model.setRegistro(documento);
        model.btnModificarHandler();

        verify(documentoDAO, never()).actualizar(any());
        verify(fc).validationFailed();
    }


    // ---------- Eliminar ----------

    @Test
    void eliminarRecargaLaListaDeLaPersona() {

        when(documentoDAO.buscarPorPersona(persona.getIdPersona()))
                .thenReturn(List.of(documento));

        model.setRegistros(List.of(documento));

        model.btnEliminarHandler(documento.getIdDocumento());

        verify(documentoDAO).eliminar(documento.getIdDocumento());
        verify(documentoDAO).buscarPorPersona(persona.getIdPersona());
    }
}
