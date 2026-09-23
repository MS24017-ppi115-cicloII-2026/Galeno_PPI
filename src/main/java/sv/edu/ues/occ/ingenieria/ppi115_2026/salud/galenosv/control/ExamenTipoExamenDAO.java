package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ExamenTipoExamen;

@Stateless
@LocalBean
public class ExamenTipoExamenDAO extends DefaultDAO<ExamenTipoExamen> {

    @PersistenceContext(unitName = "GalenoSV")
    EntityManager em;

    @Override
    public EntityManager getEntityManger() {
        return em;
    }

    public List<ExamenTipoExamen> buscarPorExamen(UUID idExamen) {
        if (idExamen == null) throw new IllegalArgumentException("El idExamen no puede ser nulo");
        TypedQuery<ExamenTipoExamen> q = getEntityManger().createQuery(
                "SELECT e FROM ExamenTipoExamen e WHERE e.idExamen.idExamen = :id", ExamenTipoExamen.class);
        q.setParameter("id", idExamen);
        return q.getResultList();
    }

    public List<ExamenTipoExamen> buscarPorTipoExamen(UUID idTipoExamen) {
        if (idTipoExamen == null) throw new IllegalArgumentException("El idTipoExamen no puede ser nulo");
        TypedQuery<ExamenTipoExamen> q = getEntityManger().createQuery(
                "SELECT e FROM ExamenTipoExamen e WHERE e.idTipoExamen.idTipoExamen = :id", ExamenTipoExamen.class);
        q.setParameter("id", idTipoExamen);
        return q.getResultList();
    }
}