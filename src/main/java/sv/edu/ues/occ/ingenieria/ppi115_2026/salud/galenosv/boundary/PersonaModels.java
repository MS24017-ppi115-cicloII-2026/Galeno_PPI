package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.Date;
import java.util.List;
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
        persona.setFechaCreacion(new Date());
        return persona;
    }

    @Override
    protected UUID obtenerId(Persona registro) {
        return registro.getIdPersona();
    }

    public List<Persona> getPersonasDisponibles() {
        return personaDAO.buscarPersonasSinRol();
    }

    /**
     * Persona sobre la que trabajan las pestañas de documentos, medios de
     * contacto y roles. Solo cuenta si ya está guardada en la base.
     */
    public Persona getPersonaSeleccionada() {
        if (registro == null || registro.getIdPersona() == null) {
            return null;
        }
        if (estado != Estado_CRUD.MODIFICAR) {
            return null;
        }
        return registro;
    }

    public boolean isHayPersonaSeleccionada() {
        return getPersonaSeleccionada() != null;
    }
}
