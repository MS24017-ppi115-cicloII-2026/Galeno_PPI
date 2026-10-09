package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import java.io.Serializable;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DAOInterface;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.TipoDocumentoDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.TipoDocumento;

@Path("tipo_documento")   // la URL: .../resources/tipo_documento
public class TipoDocumentoResource extends DefaultResource<TipoDocumento> implements Serializable {

    @Inject
    TipoDocumentoDAO tipoDocumentoDAO;   // el equivalente a su "tdService"

    @Override
    protected DAOInterface<TipoDocumento> getDAO() {
        return tipoDocumentoDAO;
    }

    @Override
    protected UUID obtenerId(TipoDocumento registro) {
        return registro.getIdTipoDocumento();
    }

    @Override
    protected void asignarId(TipoDocumento registro, UUID id) {
        registro.setIdTipoDocumento(id);
    }

    // Igual que el ingeniero: todo tipo nuevo nace activo
    @Override
    protected void antesDeCrear(TipoDocumento nuevo) {
        nuevo.setActivo(Boolean.TRUE);
    }
}
