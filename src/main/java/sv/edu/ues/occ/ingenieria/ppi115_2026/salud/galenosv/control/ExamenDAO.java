package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Examen;

@Stateless
@LocalBean
public class ExamenDAO extends DefaultDAO<Examen>{

    @PersistenceContext(unitName = "GalenoSV")
    EntityManager em;

    @Override
    public EntityManager getEntityManger() {
        return em;
    }

    public List<Examen> buscarPorNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo o vacío");
        }
        
        TypedQuery<Examen> q = getEntityManger().createQuery(
                "SELECT e FROM Examen e WHERE LOWER(e.nombre) LIKE LOWER(:nombre)", Examen.class);
        q.setParameter("nombre", "%" + nombre + "%");
        return q.getResultList();
    }
}