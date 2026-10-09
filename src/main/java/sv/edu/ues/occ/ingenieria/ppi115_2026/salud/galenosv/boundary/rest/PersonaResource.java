package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import java.io.Serializable;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DAOInterface;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.PersonaDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Persona;

@Path("persona")
public class PersonaResource extends DefaultResource<Persona> implements Serializable {

    @Inject
    PersonaDAO personaDAO;

    @Override
    protected DAOInterface<Persona> getDAO() {
        return personaDAO;
    }

    @Override
    protected UUID obtenerId(Persona registro) {
        return registro.getIdPersona();
    }

    @Override
    protected void asignarId(Persona registro, UUID id) {
        registro.setIdPersona(id);
    }
}
