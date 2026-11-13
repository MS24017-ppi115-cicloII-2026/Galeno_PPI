package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ExamenResultado;

@Stateless
@LocalBean
public class ExamenResultadoDAO extends DefaultDAO<ExamenResultado>{
    private sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.OrdenExamen idOrdenExamen;
@PersistenceContext(unitName = "GalenoSV")
EntityManager em;
    @Override
    public EntityManager getEntityManger() {
       return em;
    }
    public List<ExamenResultado> buscarPorOrdenExamen(UUID idOrdenExamen) {
        validarNoNulo(idOrdenExamen, "El idOrdenExamen no puede ser nulo");
        return buscarConNamedQuery("ExamenResultado.findByOrdenExamenId", ExamenResultado.class,
                Map.of("id", idOrdenExamen));
    }
}
