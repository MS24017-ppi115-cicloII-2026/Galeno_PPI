
package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.ArrayList;
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
    if (idPersonaRol == null) {
        throw new IllegalArgumentException("El idPersonaRol no puede ser nulo");
    }

    TypedQuery<Consulta> q = getEntityManger().createQuery(
            "SELECT c FROM Consulta c WHERE c.idPersonaRol.idPersonaRol = :id",
            Consulta.class
    );

    q.setParameter("id", idPersonaRol);

    return q.getResultList();
}
        public List<PersonaRol> buscarPersonasPorNombreRol(String nombreRol) {
        if (nombreRol == null || nombreRol.isBlank()) {
            throw new IllegalArgumentException("El nombre del rol no puede estar vacío");
        }

        TypedQuery<PersonaRol> q = getEntityManger().createQuery(
                "SELECT p FROM PersonaRol p "
                + "JOIN FETCH p.idPersona "
                + "JOIN FETCH p.idRol "
                + "WHERE LOWER(p.idRol.nombre) = :nombre",
                PersonaRol.class
        );

        q.setParameter("nombre", nombreRol.toLowerCase());

        return q.getResultList();
    }

    public List<PersonaRol> buscarPacientePorCriterio(String criterio, UUID idClinica) {

        if (criterio == null || criterio.isBlank()) {
            return List.of();
        }

        String like = "%" + criterio.trim().toLowerCase() + "%";

        StringBuilder jpql = new StringBuilder(
                "SELECT DISTINCT p FROM PersonaRol p "
                + "JOIN FETCH p.idPersona per "
                + "JOIN FETCH p.idRol r "
                + "LEFT JOIN FETCH p.idClinica cl "
                + "LEFT JOIN FETCH per.documentoCollection "
                + "LEFT JOIN FETCH per.medioContactoCollection "
                + "WHERE LOWER(r.nombre) = :nombreRol "
                + "AND (LOWER(per.nombres) LIKE :c "
                + "OR LOWER(per.apellidos) LIKE :c "
                + "OR EXISTS (SELECT d FROM Documento d "
                + "WHERE d.idPersona = per AND LOWER(d.valor) LIKE :c) "
                + "OR EXISTS (SELECT m FROM MedioContacto m "
                + "WHERE m.idPersona = per AND LOWER(m.valor) LIKE :c))");

        if (idClinica != null) {
            jpql.append(" AND cl.idClinica = :idClinica");
        }

        TypedQuery<PersonaRol> q = getEntityManger()
                .createQuery(jpql.toString(), PersonaRol.class);

        q.setParameter("nombreRol", "paciente");
        q.setParameter("c", like);

        if (idClinica != null) {
            q.setParameter("idClinica", idClinica);
        }

        Map<UUID, PersonaRol> unicos = new LinkedHashMap<>();

        for (PersonaRol pr : q.getResultList()) {
            if (pr != null && pr.getIdPersonaRol() != null) {
                unicos.putIfAbsent(pr.getIdPersonaRol(), pr);
            }
        }

        return new ArrayList<>(unicos.values());
    }
}
