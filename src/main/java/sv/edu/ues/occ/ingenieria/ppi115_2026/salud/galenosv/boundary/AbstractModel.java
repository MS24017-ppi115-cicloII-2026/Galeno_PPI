package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DAOInterface;

public abstract class AbstractModel<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    protected FacesContext fc;

    protected Estado_CRUD estado = Estado_CRUD.NINGUNO;

    private List<T> registros;

    protected T registro;

    // ---- Métodos que cada subclase debe implementar ----
    protected abstract DAOInterface<T> getDAO();

    protected abstract T crearRegistroNuevo();

    protected abstract UUID obtenerId(T registro);

    public T getRegistro() {
        return registro;
    }

    public void setRegistro(T registro) {
        this.registro = registro;
    }

    public void btnNuevoHandler(ActionEvent ae) {
        this.registro = crearRegistroNuevo();
        this.estado = Estado_CRUD.CREAR;
    }

    public void btnSeleccionarRegistro(UUID id) {
        if (this.registros != null && !this.registros.isEmpty() && id != null) {
            this.registro = this.registros.stream()
                    .filter(r -> obtenerId(r).equals(id))
                    .collect(Collectors.toList()).getFirst();
            this.estado = Estado_CRUD.MODIFICAR;
        }
    }

    public void btnModificarHandler() {
        FacesMessage mensaje;
        if (this.registro != null) {
            try {
                getDAO().actualizar(registro);
                mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Registro actualizado con exito", "Registro guardado");
                this.estado = Estado_CRUD.NINGUNO;
                this.registro = null;
                this.registros = getDAO().findRange(0, 100);
            } catch (Exception ex) {
                mensaje = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se puede actualizar el registro", ex.getMessage());
            }
        } else {
            mensaje = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Registro no puede ser nulo", "Seleccione algun registro");
        }
        fc.addMessage(null, mensaje);
    }

    public void btnEliminarHandler(UUID id) {
        FacesMessage mensaje;
        if (this.registros != null && !this.registros.isEmpty() && id != null) {
            try {
                getDAO().eliminar(id);
                mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Registro eliminado con exito", "Registro borrado");
                this.estado = Estado_CRUD.NINGUNO;
                this.registro = null;
                this.registros = getDAO().findRange(0, 100);
            } catch (Exception ex) {
                mensaje = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se puede eliminar el registro", ex.getMessage());
            }
        } else {
            mensaje = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Registro no puede ser nulo", "Seleccione algun registro");
        }
        fc.addMessage(null, mensaje);
    }

    public Estado_CRUD getEstado() {
        return estado;
    }

    public void btnCancelar() {
        this.registro = null;
        this.estado = Estado_CRUD.NINGUNO;
    }

    public void btnCrearhandler(ActionEvent ae) {
        FacesMessage mensaje;
        if (this.registro != null) {
            try {
                getDAO().crear(registro);
                mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Registro creado con exito", "Registro guardado");
                this.estado = Estado_CRUD.NINGUNO;
                this.registro = null;
                this.registros = getDAO().findRange(0, 100);
            } catch (Exception ex) {
                mensaje = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se puede guardar el registro", ex.getMessage());
            }
        } else {
            mensaje = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Registro no puede ser nulo", "Ingrese algun registro");
        }
        fc.addMessage(null, mensaje);
    }

    @PostConstruct
    public void inicializar() {
        this.registros = getDAO().findRange(0, 100);
    }

    public List<T> getregistros() {
        return registros;
    }

    public void setRegistros(List<T> registros) {
        this.registros = registros;
    }
}