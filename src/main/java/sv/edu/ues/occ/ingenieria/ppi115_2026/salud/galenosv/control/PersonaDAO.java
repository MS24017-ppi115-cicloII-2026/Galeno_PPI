package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Persona;

@Stateless
@LocalBean
public class PersonaDAO extends DefaultDAO<Persona> {

    @PersistenceContext(unitName = "GalenoSV")
    private EntityManager em;

    @Override
    public EntityManager getEntityManger() {
        return em;
    }

    public List<Persona> buscarPorNombres(String nombres) {
        validarTexto(nombres, "Los nombres no pueden ser nulos o vacíos");

        return buscarConNamedQuery("Persona.findByNombresLike", Persona.class,
                Map.of("nombres", "%" + nombres + "%"));
    }

    public List<Persona> buscarPersonasSinRol() {
        return buscarConNamedQuery("Persona.findPersonasSinRol", Persona.class,
                Map.of());
    }

    public List<Persona> buscarPorApellidos(String apellidos) {
        validarTexto(apellidos, "Los apellidos no pueden ser nulos o vacíos");

        return buscarConNamedQuery("Persona.findByApellidosLike", Persona.class,
                Map.of("apellidos", "%" + apellidos + "%"));
    }
}
