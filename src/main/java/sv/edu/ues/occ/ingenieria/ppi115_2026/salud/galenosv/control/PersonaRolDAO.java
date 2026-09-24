package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Clinica;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Persona;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.PersonaRol;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Rol;

@Stateless
@LocalBean
public class PersonaRolDAO extends DefaultDAO<PersonaRol> {

    @PersistenceContext(unitName = "GalenoSV")
    private EntityManager em;

    @Override
    public EntityManager getEntityManger() {
        return em;
    }

    public List<PersonaRol> buscarPorPersona(Persona persona) {
        if (persona == null) {
            throw new IllegalArgumentException(
                    "La persona no puede ser nula"
            );
        }

        TypedQuery<PersonaRol> q = getEntityManger().createQuery(
                "SELECT p FROM PersonaRol p "
                + "WHERE p.idPersona = :persona",
                PersonaRol.class
        );

        q.setParameter("persona", persona);

        return q.getResultList();
    }

    public List<PersonaRol> buscarPorRol(Rol rol) {
        if (rol == null) {
            throw new IllegalArgumentException(
                    "El rol no puede ser nulo"
            );
        }

        TypedQuery<PersonaRol> q = getEntityManger().createQuery(
                "SELECT p FROM PersonaRol p "
                + "WHERE p.idRol = :rol",
                PersonaRol.class
        );

        q.setParameter("rol", rol);

        return q.getResultList();
    }

    public List<PersonaRol> buscarPorClinica(Clinica clinica) {
        if (clinica == null) {
            throw new IllegalArgumentException(
                    "La clínica no puede ser nula"
            );
        }

        TypedQuery<PersonaRol> q = getEntityManger().createQuery(
                "SELECT p FROM PersonaRol p "
                + "WHERE p.idClinica = :clinica",
                PersonaRol.class
        );

        q.setParameter("clinica", clinica);

        return q.getResultList();
    }
}