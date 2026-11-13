package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;
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
        validarNoNulo(idProcedimiento, "El idProcedimiento no puede ser nulo");

        return buscarConNamedQuery("ProcedimientoPaso.findByProcedimientoId", ProcedimientoPaso.class,
                Map.of("id", idProcedimiento));
    }

    public List<ProcedimientoPaso> buscarPorRol(UUID idRol) {
        validarNoNulo(idRol, "El idRol no puede ser nulo");

        return buscarConNamedQuery("ProcedimientoPaso.findByRolId", ProcedimientoPaso.class,
                Map.of("id", idRol));
    }
}
