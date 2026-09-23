
package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Consulta;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ConsultaProcedimiento;

@Stateless
@LocalBean
public class ConsultaProcedimientoDAO extends DefaultDAO<ConsultaProcedimiento>{
private Consulta idConsulta;
 @PersistenceContext(unitName = "GalenoSV")
EntityManager em;
    @Override
    public EntityManager getEntityManger() {
       return em;
    }
    public List<ConsultaProcedimiento> buscarPorConsulta(UUID idConsulta) {
    if (idConsulta == null) {
        throw new IllegalArgumentException("El idConsulta no puede ser nulo");
    }

    TypedQuery<ConsultaProcedimiento> q = getEntityManger().createQuery(
            "SELECT c FROM ConsultaProcedimiento c WHERE c.idConsulta.idConsulta = :id",
            ConsultaProcedimiento.class
    );

    q.setParameter("id", idConsulta);

    return q.getResultList();
}
}
