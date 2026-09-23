package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ProcedimientoPasoSecuencia;

@Stateless
@LocalBean
public class ProcedimientoPasoSecuenciaDAO extends DefaultDAO<ProcedimientoPasoSecuencia> {

    @PersistenceContext(unitName = "GalenoSV")
    EntityManager em;

    @Override
    public EntityManager getEntityManger() {
        return em;
    }

    public List<ProcedimientoPasoSecuencia> buscarPorProcedimientoPaso(UUID idProcedimientoPaso) {
        if (idProcedimientoPaso == null) throw new IllegalArgumentException("El idProcedimientoPaso no puede ser nulo");
        TypedQuery<ProcedimientoPasoSecuencia> q = getEntityManger().createQuery(
                "SELECT p FROM ProcedimientoPasoSecuencia p WHERE p.idProcedimientoPaso.idProcedimientoPaso = :id", ProcedimientoPasoSecuencia.class);
        q.setParameter("id", idProcedimientoPaso);
        return q.getResultList();
    }

    public List<ProcedimientoPasoSecuencia> buscarPorPasoReferencia(UUID idPasoReferencia) {
        if (idPasoReferencia == null) throw new IllegalArgumentException("El idPasoReferencia no puede ser nulo");
        // Consulta directa porque no es una relación mapeada
        TypedQuery<ProcedimientoPasoSecuencia> q = getEntityManger().createQuery(
                "SELECT p FROM ProcedimientoPasoSecuencia p WHERE p.idProcedimientoPasoReferencia = :id", ProcedimientoPasoSecuencia.class);
        q.setParameter("id", idPasoReferencia);
        return q.getResultList();
    }
}