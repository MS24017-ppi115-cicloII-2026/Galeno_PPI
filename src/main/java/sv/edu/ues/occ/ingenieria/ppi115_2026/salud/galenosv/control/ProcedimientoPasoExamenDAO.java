package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ProcedimientoPasoExamen;

@Stateless
@LocalBean
public class ProcedimientoPasoExamenDAO extends DefaultDAO<ProcedimientoPasoExamen> {

    @PersistenceContext(unitName = "GalenoSV")
    EntityManager em;

    @Override
    public EntityManager getEntityManger() {
        return em;
    }

    public List<ProcedimientoPasoExamen> buscarPorExamen(UUID idExamen) {
        if (idExamen == null) throw new IllegalArgumentException("El idExamen no puede ser nulo");
        TypedQuery<ProcedimientoPasoExamen> q = getEntityManger().createQuery(
                "SELECT p FROM ProcedimientoPasoExamen p WHERE p.idExamen.idExamen = :id", ProcedimientoPasoExamen.class);
        q.setParameter("id", idExamen);
        return q.getResultList();
    }

    public List<ProcedimientoPasoExamen> buscarPorProcedimientoPaso(UUID idProcedimientoPaso) {
        if (idProcedimientoPaso == null) throw new IllegalArgumentException("El idProcedimientoPaso no puede ser nulo");
        TypedQuery<ProcedimientoPasoExamen> q = getEntityManger().createQuery(
                "SELECT p FROM ProcedimientoPasoExamen p WHERE p.idProcedimientoPaso.idProcedimientoPaso = :id", ProcedimientoPasoExamen.class);
        q.setParameter("id", idProcedimientoPaso);
        return q.getResultList();
    }
}