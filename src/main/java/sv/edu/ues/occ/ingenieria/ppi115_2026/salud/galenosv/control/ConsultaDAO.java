
package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Consulta;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.PersonaRol;
@Stateless
@LocalBean
public class ConsultaDAO extends DefaultDAO<Consulta>{
    private PersonaRol idPersonaRol;
@PersistenceContext(unitName = "GalenoSV")
EntityManager em;
    @Override
    public EntityManager getEntityManger() {
        return  em;
    }
    public List<Consulta> buscarPorPersonaRol(UUID idPersonaRol) {
        validarNoNulo(idPersonaRol, "El idPersonaRol no puede ser nulo");

        return buscarConNamedQuery("Consulta.findByPersonaRolId", Consulta.class,
                Map.of("id", idPersonaRol));
    }
        public List<PersonaRol> buscarPersonasPorNombreRol(String nombreRol) {
        validarTexto(nombreRol, "El nombre del rol no puede estar vacío");

        return buscarConNamedQuery("PersonaRol.findByNombreRolConRelaciones", PersonaRol.class,
                Map.of("nombre", nombreRol.toLowerCase()));
    }

    public List<PersonaRol> buscarPacientePorCriterio(String criterio, UUID idClinica) {

        if (criterio == null || criterio.isBlank()) {
            return List.of();
        }

        String like = "%" + criterio.trim().toLowerCase() + "%";

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("nombreRol", "paciente");
        parametros.put("c", like);

        List<PersonaRol> resultados;
        if (idClinica != null) {
            parametros.put("idClinica", idClinica);
            resultados = buscarConNamedQuery(
                    "PersonaRol.buscarPacientesPorCriterioEnClinica",
                    PersonaRol.class, parametros);
        } else {
            resultados = buscarConNamedQuery(
                    "PersonaRol.buscarPacientesPorCriterio",
                    PersonaRol.class, parametros);
        }

        Map<UUID, PersonaRol> unicos = new LinkedHashMap<>();

        for (PersonaRol pr : resultados) {
            if (pr != null && pr.getIdPersonaRol() != null) {
                unicos.putIfAbsent(pr.getIdPersonaRol(), pr);
            }
        }

        return new ArrayList<>(unicos.values());
    }
}
