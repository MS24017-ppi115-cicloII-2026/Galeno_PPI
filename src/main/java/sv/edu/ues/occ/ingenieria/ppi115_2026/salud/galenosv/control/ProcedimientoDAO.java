package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Procedimiento;

@Stateless
@LocalBean
public class ProcedimientoDAO extends DefaultDAO<Procedimiento>{

    @PersistenceContext(unitName = "GalenoSV")
    EntityManager em;

    @Override
    public EntityManager getEntityManger() {
        return em;
    }

    public List<Procedimiento> buscarPorNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo o vacío");
        }
        
        TypedQuery<Procedimiento> q = getEntityManger().createQuery(
                "SELECT p FROM Procedimiento p WHERE LOWER(p.nombre) LIKE LOWER(:nombre)", Procedimiento.class);
        q.setParameter("nombre", "%" + nombre + "%");
        return q.getResultList();
    }

    public List<Procedimiento> buscarPorActivo(Boolean activo) {
        if (activo == null) {
            throw new IllegalArgumentException(
                    "El estado activo no puede ser nulo"
            );
        }

        TypedQuery<Procedimiento> q = getEntityManger().createQuery(
                "SELECT p FROM Procedimiento p WHERE p.activo = :activo ORDER BY p.nombre",
                Procedimiento.class);
        q.setParameter("activo", activo);
        return q.getResultList();
    }
}