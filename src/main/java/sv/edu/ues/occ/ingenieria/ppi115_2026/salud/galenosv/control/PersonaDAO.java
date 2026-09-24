package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
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
        if (nombres == null || nombres.isBlank()) {
            throw new IllegalArgumentException(
                    "Los nombres no pueden ser nulos o vacíos"
            );
        }

        TypedQuery<Persona> q = getEntityManger().createQuery(
                "SELECT p FROM Persona p "
                + "WHERE LOWER(p.nombres) LIKE LOWER(:nombres)",
                Persona.class
        );

        q.setParameter("nombres", "%" + nombres + "%");

        return q.getResultList();
    }

    public List<Persona> buscarPorApellidos(String apellidos) {
        if (apellidos == null || apellidos.isBlank()) {
            throw new IllegalArgumentException(
                    "Los apellidos no pueden ser nulos o vacíos"
            );
        }

        TypedQuery<Persona> q = getEntityManger().createQuery(
                "SELECT p FROM Persona p "
                + "WHERE LOWER(p.apellidos) LIKE LOWER(:apellidos)",
                Persona.class
        );

        q.setParameter("apellidos", "%" + apellidos + "%");

        return q.getResultList();
    }
}