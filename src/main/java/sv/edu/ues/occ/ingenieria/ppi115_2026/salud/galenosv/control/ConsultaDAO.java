
package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
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
}
