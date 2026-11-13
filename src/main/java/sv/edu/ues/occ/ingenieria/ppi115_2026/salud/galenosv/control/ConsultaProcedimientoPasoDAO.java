package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ConsultaProcedimiento;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ConsultaProcedimientoPaso;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.PersonaRol;

@Stateless
@LocalBean
public class ConsultaProcedimientoPasoDAO extends DefaultDAO<ConsultaProcedimientoPaso> {

    private ConsultaProcedimiento idConsultaProcedimiento;
    private PersonaRol idPersonaRol;
    @PersistenceContext(unitName = "GalenoSV")
    EntityManager em;

    @Override
    public EntityManager getEntityManger() {
        return em;
    }

    public List<ConsultaProcedimientoPaso> buscarPorConsultaProcedimiento(UUID idConsultaProcedimiento) {
        validarNoNulo(idConsultaProcedimiento, "El idConsultaProcedimiento no puede ser nulo");

        return buscarConNamedQuery("ConsultaProcedimientoPaso.findByConsultaProcedimientoId",
                ConsultaProcedimientoPaso.class, Map.of("id", idConsultaProcedimiento));
    }

    public List<ConsultaProcedimientoPaso> buscarPorPersonaRol(UUID idPersonaRol) {
        validarNoNulo(idPersonaRol, "El idPersonaRol no puede ser nulo");

        return buscarConNamedQuery("ConsultaProcedimientoPaso.findByPersonaRolId",
                ConsultaProcedimientoPaso.class, Map.of("id", idPersonaRol));
    }

    public List<PersonaRol> buscarPersonasExcluyendoRol(String nombreRol) {
        validarTexto(nombreRol, "El nombre del rol no puede estar vacío");

        return buscarConNamedQuery("PersonaRol.findByNombreRolSinEl", PersonaRol.class,
                Map.of("nombre", nombreRol.toLowerCase()));
    }
}
