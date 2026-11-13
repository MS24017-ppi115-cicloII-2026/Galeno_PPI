package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;
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
        validarNoNulo(idExamen, "El idExamen no puede ser nulo");
        return buscarConNamedQuery("ExamenTipoExamen.findByExamenId", ExamenTipoExamen.class,
                Map.of("id", idExamen));
    }

    public List<ExamenTipoExamen> buscarPorTipoExamen(UUID idTipoExamen) {
        validarNoNulo(idTipoExamen, "El idTipoExamen no puede ser nulo");
        return buscarConNamedQuery("ExamenTipoExamen.findByTipoExamenId", ExamenTipoExamen.class,
                Map.of("id", idTipoExamen));
    }
}
