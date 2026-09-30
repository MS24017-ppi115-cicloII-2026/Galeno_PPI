package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DAOInterface;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DocumentoDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Documento;

@Named
@ViewScoped
public class DocumentoModels extends AbstractModel<Documento> {

    @Inject
    DocumentoDAO documentoDAO;

    @Override
    protected DAOInterface<Documento> getDAO() {
        return documentoDAO;
    }

    @Override
    protected Documento crearRegistroNuevo() {
        return new Documento(UUID.randomUUID());
    }

    @Override
    protected UUID obtenerId(Documento registro) {
        return registro.getIdDocumento();
    }

    private boolean validarRegistro() {
        if (registro == null) {
            return mostrarError(
                    "Registro inválido",
                    "El documento no puede ser nulo."
            );
        }

        if (registro.getIdPersona() == null) {
            return mostrarError(
                    "Persona requerida",
                    "Debe seleccionar una persona."
            );
        }

        if (registro.getIdTipoDocumento() == null) {
            return mostrarError(
                    "Tipo de documento requerido",
                    "Debe seleccionar un tipo de documento."
            );
        }

        if (registro.getValor() == null
                || registro.getValor().isBlank()) {
            return mostrarError(
                    "Valor requerido",
                    "El valor del documento no puede estar vacío."
            );
        }

        return true;
    }

    private boolean mostrarError(String resumen, String detalle) {
        fc.addMessage(
                null,
                new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        resumen,
                        detalle
                )
        );

        fc.validationFailed();
        return false;
    }

    @Override
    public void btnCrearhandler(ActionEvent ae) {
        if (validarRegistro()) {
            super.btnCrearhandler(ae);
        }
    }

    @Override
    public void btnModificarHandler() {
        if (validarRegistro()) {
            super.btnModificarHandler();
        }
    }
}