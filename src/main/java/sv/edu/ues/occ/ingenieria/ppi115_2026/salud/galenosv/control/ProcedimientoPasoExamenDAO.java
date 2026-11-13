package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;
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
        validarNoNulo(idExamen, "El idExamen no puede ser nulo");

        return buscarConNamedQuery("ProcedimientoPasoExamen.findByExamenId", ProcedimientoPasoExamen.class,
                Map.of("id", idExamen));
    }

    public List<ProcedimientoPasoExamen> buscarPorProcedimientoPaso(UUID idProcedimientoPaso) {
        validarNoNulo(idProcedimientoPaso, "El idProcedimientoPaso no puede ser nulo");

        return buscarConNamedQuery("ProcedimientoPasoExamen.findByProcedimientoPasoId", ProcedimientoPasoExamen.class,
                Map.of("id", idProcedimientoPaso));
    }
}
