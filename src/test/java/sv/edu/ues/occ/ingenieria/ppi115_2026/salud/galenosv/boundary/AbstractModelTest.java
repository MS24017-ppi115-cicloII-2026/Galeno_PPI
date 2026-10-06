package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DAOInterface;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Rol;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AbstractModelTest {

    /**
     * Implementación mínima de un CRUD para poder probar la base común.
     */
    static class ModeloRolPrueba extends AbstractModel<Rol> {

        private static final long serialVersionUID = 1L;

        private final DAOInterface<Rol> dao;

        public ModeloRolPrueba(DAOInterface<Rol> dao, FacesContext fc) {
            this.dao = dao;
            this.fc = fc;
        }

        @Override
        protected DAOInterface<Rol> getDAO() {
            return dao;
        }

        @Override
        protected Rol crearRegistroNuevo() {
            Rol rol = new Rol();
            rol.setIdRol(UUID.randomUUID());
            return rol;
        }

        @Override
        protected UUID obtenerId(Rol registro) {
            return registro.getIdRol();
        }
    }

    @Mock
    private DAOInterface<Rol> dao;

    @Mock
    private FacesContext fc;

    private ModeloRolPrueba model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        model = new ModeloRolPrueba(dao, fc);
    }

    private Rol crearRol() {
        Rol rol = new Rol();
        rol.setIdRol(UUID.randomUUID());
        rol.setNombre("ADMIN");
        return rol;
    }


    @Test
    void inicializarCargaLosRegistros() {

        Rol rol = crearRol();

        when(dao.findRange(0, 100)).thenReturn(List.of(rol));

        model.inicializar();

        assertNotNull(model.getregistros());
        assertEquals(1, model.getregistros().size());
        assertEquals(Estado_CRUD.NINGUNO, model.getEstado());
    }

    @Test
    void btnNuevoHandlerCreaElRegistroYEstadoCrear() {

        model.btnNuevoHandler(null);

        assertNotNull(model.getRegistro());
        assertNotNull(model.getRegistro().getIdRol());
        assertEquals(Estado_CRUD.CREAR, model.getEstado());
    }


    @Test
    void btnSeleccionarRegistroEncuentraElRegistroYEstadoModificar() {

        Rol primero = crearRol();
        Rol segundo = crearRol();
        model.setRegistros(List.of(primero, segundo));

        model.btnSeleccionarRegistro(segundo.getIdRol());

        assertSame(segundo, model.getRegistro());
        assertEquals(Estado_CRUD.MODIFICAR, model.getEstado());
    }

    @Test
    void btnModificarHandlerActualizaYRecarga() {

        Rol rol = crearRol();
        model.setRegistro(rol);
        when(dao.findRange(0, 100)).thenReturn(List.of(rol));

        model.btnModificarHandler();

        verify(dao).actualizar(rol);
        assertNull(model.getRegistro());
        assertEquals(Estado_CRUD.NINGUNO, model.getEstado());
        verify(fc).addMessage(isNull(), any(FacesMessage.class));
    }

    @Test
    void btnModificarHandlerConExcepcionMuestraError() {

        Rol rol = crearRol();
        model.setRegistro(rol);

        doThrow(new IllegalStateException("fallo"))
                .when(dao).actualizar(rol);

        model.btnModificarHandler();

        assertSame(rol, model.getRegistro());
        verify(fc).addMessage(isNull(), any(FacesMessage.class));
    }


    @Test
    void btnCrearhandlerCreaYRecarga() {

        Rol rol = crearRol();
        model.setRegistro(rol);
        when(dao.findRange(0, 100)).thenReturn(List.of(rol));

        model.btnCrearhandler(null);

        verify(dao).crear(rol);
        assertNull(model.getRegistro());
        assertEquals(Estado_CRUD.NINGUNO, model.getEstado());
        verify(fc).addMessage(isNull(), any(FacesMessage.class));
    }

    @Test
    void btnCrearhandlerConExcepcionMuestraError() {

        Rol rol = crearRol();
        model.setRegistro(rol);

        doThrow(new IllegalStateException("fallo"))
                .when(dao).crear(rol);

        model.btnCrearhandler(null);

        assertSame(rol, model.getRegistro());
        verify(fc).addMessage(isNull(), any(FacesMessage.class));
    }


    @Test
    void btnEliminarHandlerEliminaYRecarga() {

        Rol rol = crearRol();
        model.setRegistros(List.of(rol));
        when(dao.findRange(0, 100)).thenReturn(List.of());

        model.btnEliminarHandler(rol.getIdRol());

        verify(dao).eliminar(rol.getIdRol());
        assertNull(model.getRegistro());
        assertEquals(Estado_CRUD.NINGUNO, model.getEstado());
        verify(fc).addMessage(isNull(), any(FacesMessage.class));
    }

    @Test
    void btnEliminarHandlerSinArgumentosUsaElRegistroActual() {

        Rol rol = crearRol();
        model.setRegistro(rol);
        model.setRegistros(List.of(rol));
        when(dao.findRange(0, 100)).thenReturn(List.of());

        model.btnEliminarHandler();

        verify(dao).eliminar(rol.getIdRol());
    }

    @Test
    void btnCancelarLimpiaRegistroYEstado() {

        model.setRegistro(crearRol());
        model.btnNuevoHandler(null);

        model.btnCancelar();

        assertNull(model.getRegistro());
        assertEquals(Estado_CRUD.NINGUNO, model.getEstado());
    }


    @Test
    void btnEliminarHandlerConExcepcionMuestraError() {

        Rol rol = crearRol();
        UUID id = rol.getIdRol();

        model.setRegistros(List.of(rol));

        doThrow(new IllegalStateException("esta en uso"))
                .when(dao).eliminar(id);

        model.btnEliminarHandler(id);

        assertNotNull(model.getregistros());
        verify(fc).addMessage(isNull(), any(FacesMessage.class));
    }
}
