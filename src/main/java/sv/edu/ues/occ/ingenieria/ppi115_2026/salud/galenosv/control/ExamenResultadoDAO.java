/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ExamenResultado;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.OrdenExamen;

@Stateless
@LocalBean
public class ExamenResultadoDAO extends DefaultDAO<ExamenResultado>{
    private OrdenExamen idOrdenExamen;
@PersistenceContext(unitName = "GalenoSV")
EntityManager em;
    @Override
    public EntityManager getEntityManger() {
       return em;
    }
    public List<ExamenResultado> buscarPorOrdenExamen(UUID idOrdenExamen) {
    if (idOrdenExamen == null) {
        throw new IllegalArgumentException("El idOrdenExamen no puede ser nulo");
    }

    TypedQuery<ExamenResultado> q = getEntityManger().createQuery(
            "SELECT e FROM ExamenResultado e "
            + "WHERE e.idOrdenExamen.idOrdenExamen = :id",
            ExamenResultado.class
    );

    q.setParameter("id", idOrdenExamen);

    return q.getResultList();
}
}
