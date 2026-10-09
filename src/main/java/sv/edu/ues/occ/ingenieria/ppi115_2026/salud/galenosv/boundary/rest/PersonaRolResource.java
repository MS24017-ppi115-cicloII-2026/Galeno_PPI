package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import java.io.Serializable;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DAOInterface;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.PersonaRolDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.PersonaRol;

@Path("persona_rol")
public class PersonaRolResource extends DefaultResource<PersonaRol> implements Serializable {

    @Inject
    PersonaRolDAO personaRolDAO;

    @Override
    protected DAOInterface<PersonaRol> getDAO() {
        return personaRolDAO;
    }

    @Override
    protected UUID obtenerId(PersonaRol registro) {
        return registro.getIdPersonaRol();
    }

    @Override
    protected void asignarId(PersonaRol registro, UUID id) {
        registro.setIdPersonaRol(id);
    }
}
