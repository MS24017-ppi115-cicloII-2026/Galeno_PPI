package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;
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
        validarNoNulo(idProcedimientoPaso, "El idProcedimientoPaso no puede ser nulo");

        return buscarConNamedQuery("ProcedimientoPasoSecuencia.findByProcedimientoPasoId", ProcedimientoPasoSecuencia.class,
                Map.of("id", idProcedimientoPaso));
    }

    public List<ProcedimientoPasoSecuencia> buscarPorPasoReferencia(UUID idPasoReferencia) {
        validarNoNulo(idPasoReferencia, "El idPasoReferencia no puede ser nulo");
        // Consulta directa porque no es una relación mapeada
        return buscarConNamedQuery("ProcedimientoPasoSecuencia.findByPasoReferenciaId", ProcedimientoPasoSecuencia.class,
                Map.of("id", idPasoReferencia));
    }
}
