package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import java.io.Serializable;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DAOInterface;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.MedioContactoDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.MedioContacto;

@Path("medio_contacto")
public class MedioContactoResource extends DefaultResource<MedioContacto> implements Serializable {

    @Inject
    MedioContactoDAO medioContactoDAO;

    @Override
    protected DAOInterface<MedioContacto> getDAO() {
        return medioContactoDAO;
    }

    @Override
    protected UUID obtenerId(MedioContacto registro) {
        return registro.getIdMedioContacto();
    }

    @Override
    protected void asignarId(MedioContacto registro, UUID id) {
        registro.setIdMedioContacto(id);
    }
}
