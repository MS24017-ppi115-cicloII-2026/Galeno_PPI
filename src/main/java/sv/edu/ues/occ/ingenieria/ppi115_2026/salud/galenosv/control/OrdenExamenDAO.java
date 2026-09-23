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
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ConsultaProcedimientoPaso;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.OrdenExamen;

@Stateless
@LocalBean
public class OrdenExamenDAO extends DefaultDAO<OrdenExamen>{
    private ConsultaProcedimientoPaso idConsultaProcedimientoPaso;
    @PersistenceContext(unitName = "GalenoSV") 
    EntityManager em;

    @Override
    public EntityManager getEntityManger() {
return em ;
    }
    public List<OrdenExamen> buscarPorConsultaProcedimientoPaso(UUID idConsultaProcedimientoPaso) {
    if (idConsultaProcedimientoPaso == null) {
        throw new IllegalArgumentException(
                "El idConsultaProcedimientoPaso no puede ser nulo"
        );
    }

    TypedQuery<OrdenExamen> q = getEntityManger().createQuery(
            "SELECT o FROM OrdenExamen o "
            + "WHERE o.idConsultaProcedimientoPaso.idConsultaProcedimientoPaso = :id",
            OrdenExamen.class
    );

    q.setParameter("id", idConsultaProcedimientoPaso);

    return q.getResultList();
}
}
