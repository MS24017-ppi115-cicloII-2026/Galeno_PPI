package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.UUID;
import java.util.Date;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DAOInterface;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.PersonaRolDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.PersonaRol;

@Named("personaRolModels")
@ViewScoped
public class PersonaRolModels extends AbstractModel<PersonaRol> {

    @Inject
    private PersonaRolDAO personaRolDAO;

    @Override
    protected DAOInterface<PersonaRol> getDAO() {
        return personaRolDAO;
    }

    @Override
    protected PersonaRol crearRegistroNuevo() {
        PersonaRol personaRol = new PersonaRol();
        personaRol.setIdPersonaRol(UUID.randomUUID());
        personaRol.setFechaCreacion(new Date());
        return personaRol;
    }

    @Override
    protected UUID obtenerId(PersonaRol registro) {
        return registro.getIdPersonaRol();
    }
}
