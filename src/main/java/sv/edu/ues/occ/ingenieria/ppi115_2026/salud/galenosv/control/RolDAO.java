package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Rol;

@Stateless
@LocalBean
public class RolDAO extends DefaultDAO<Rol> {

    @PersistenceContext(unitName = "GalenoSV")
    private EntityManager em;

    @Override
    public EntityManager getEntityManger() {
        return em;
    }

    public List<Rol> buscarPorNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre no puede ser nulo o vacío"
            );
        }

        TypedQuery<Rol> q = getEntityManger().createQuery(
                "SELECT r FROM Rol r "
                + "WHERE LOWER(r.nombre) LIKE LOWER(:nombre)",
                Rol.class
        );

        q.setParameter("nombre", "%" + nombre + "%");

        return q.getResultList();
    }

    public List<Rol> buscarPorActivo(Boolean activo) {
        if (activo == null) {
            throw new IllegalArgumentException(
                    "El estado activo no puede ser nulo"
            );
        }

        TypedQuery<Rol> q = getEntityManger().createNamedQuery(
                "Rol.findByActivo",
                Rol.class
        );

        q.setParameter("activo", activo);

        return q.getResultList();
    }
}
