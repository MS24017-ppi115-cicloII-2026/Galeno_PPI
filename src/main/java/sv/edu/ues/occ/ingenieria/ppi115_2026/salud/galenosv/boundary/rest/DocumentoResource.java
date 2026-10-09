package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import java.io.Serializable;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DAOInterface;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DocumentoDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Documento;

@Path("documento")
public class DocumentoResource extends DefaultResource<Documento> implements Serializable {

    @Inject
    DocumentoDAO documentoDAO;

    @Override
    protected DAOInterface<Documento> getDAO() {
        return documentoDAO;
    }

    @Override
    protected UUID obtenerId(Documento registro) {
        return registro.getIdDocumento();
    }

    @Override
    protected void asignarId(Documento registro, UUID id) {
        registro.setIdDocumento(id);
    }
}
