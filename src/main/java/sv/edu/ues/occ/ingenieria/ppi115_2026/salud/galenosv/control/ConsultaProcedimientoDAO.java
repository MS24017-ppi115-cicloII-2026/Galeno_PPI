
package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;
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
        validarNoNulo(idConsulta, "El idConsulta no puede ser nulo");

        return buscarConNamedQuery("ConsultaProcedimiento.findByConsultaId",
                ConsultaProcedimiento.class, Map.of("id", idConsulta));
    }
}
