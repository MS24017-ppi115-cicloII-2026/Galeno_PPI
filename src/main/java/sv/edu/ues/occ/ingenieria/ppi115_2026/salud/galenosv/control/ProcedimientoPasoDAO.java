package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ProcedimientoPaso;

@Stateless
@LocalBean
public class ProcedimientoPasoDAO extends DefaultDAO<ProcedimientoPaso> {

    @PersistenceContext(unitName = "GalenoSV")
    EntityManager em;

    @Override
    public EntityManager getEntityManger() {
        return em;
    }

    public List<ProcedimientoPaso> buscarPorProcedimiento(UUID idProcedimiento) {
        if (idProcedimiento == null) {
            throw new IllegalArgumentException("El idProcedimiento no puede ser nulo");
        }
        
        TypedQuery<ProcedimientoPaso> q = getEntityManger().createQuery(
                "SELECT p FROM ProcedimientoPaso p WHERE p.idProcedimiento.idProcedimiento = :id", ProcedimientoPaso.class);
        q.setParameter("id", idProcedimiento);
        return q.getResultList();
    }

    public List<ProcedimientoPaso> buscarPorRol(UUID idRol) {
        if (idRol == null) {
            throw new IllegalArgumentException("El idRol no puede ser nulo");
        }
        
        TypedQuery<ProcedimientoPaso> q = getEntityManger().createQuery(
                "SELECT p FROM ProcedimientoPaso p WHERE p.idRol.idRol = :id", ProcedimientoPaso.class);
        q.setParameter("id", idRol);
        return q.getResultList();
    }
}