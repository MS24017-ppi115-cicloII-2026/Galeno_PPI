package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DAOInterface;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.TipoDocumentoDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.TipoDocumento;

@Named("tipoDocumentoModel")
@ViewScoped
public class TipoDocumentoModels extends AbstractModel<TipoDocumento> {

    @Inject
    private TipoDocumentoDAO tipoDocumentoDAO;

    @Override
    protected DAOInterface<TipoDocumento> getDAO() {
        return tipoDocumentoDAO;
    }

    @Override
    protected TipoDocumento crearRegistroNuevo() {
        TipoDocumento tipoDocumento = new TipoDocumento();
        tipoDocumento.setIdTipoDocumento(UUID.randomUUID());
        tipoDocumento.setActivo(true);
        return tipoDocumento;
    }

    @Override
    protected UUID obtenerId(TipoDocumento registro) {
        return registro.getIdTipoDocumento();
    }
}
