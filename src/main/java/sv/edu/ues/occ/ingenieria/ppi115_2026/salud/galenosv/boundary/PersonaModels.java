package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DAOInterface;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.PersonaDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Persona;

@Named("personaModel")
@ViewScoped
public class PersonaModels extends AbstractModel<Persona> {

    @Inject
    private PersonaDAO personaDAO;

    @Override
    protected DAOInterface<Persona> getDAO() {
        return personaDAO;
    }

    @Override
    protected Persona crearRegistroNuevo() {
        Persona persona = new Persona();
        persona.setIdPersona(UUID.randomUUID());
        return persona;
    }

    @Override
    protected UUID obtenerId(Persona registro) {
        return registro.getIdPersona();
    }
}
