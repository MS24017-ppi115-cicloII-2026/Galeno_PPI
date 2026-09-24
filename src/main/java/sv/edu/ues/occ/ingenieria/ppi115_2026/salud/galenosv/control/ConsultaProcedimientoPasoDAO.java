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
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ConsultaProcedimiento;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ConsultaProcedimientoPaso;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.PersonaRol;

@Stateless
@LocalBean
public class ConsultaProcedimientoPasoDAO extends DefaultDAO<ConsultaProcedimientoPaso>{
    private ConsultaProcedimiento idConsultaProcedimiento;
    private PersonaRol idPersonaRol;
     @PersistenceContext(unitName = "GalenoSV")
EntityManager em;
    @Override
    public EntityManager getEntityManger() {
       return em;
    }
    public List<ConsultaProcedimientoPaso> buscarPorConsultaProcedimiento(UUID idConsultaProcedimiento) {
    if (idConsultaProcedimiento == null) {
        throw new IllegalArgumentException("El idConsultaProcedimiento no puede ser nulo");
    }

    TypedQuery<ConsultaProcedimientoPaso> q = getEntityManger().createQuery(
            "SELECT c FROM ConsultaProcedimientoPaso c "
            + "WHERE c.idConsultaProcedimiento.idConsultaProcedimiento = :id",
            ConsultaProcedimientoPaso.class
    );

    q.setParameter("id", idConsultaProcedimiento);

    return q.getResultList();
}
    public List<ConsultaProcedimientoPaso> buscarPorPersonaRol(UUID idPersonaRol) {
    if (idPersonaRol == null) {
        throw new IllegalArgumentException("El idPersonaRol no puede ser nulo");
    }

    TypedQuery<ConsultaProcedimientoPaso> q = getEntityManger().createQuery(
            "SELECT c FROM ConsultaProcedimientoPaso c "
            + "WHERE c.idPersonaRol.idPersonaRol = :id",
            ConsultaProcedimientoPaso.class
    );

    q.setParameter("id", idPersonaRol);

    return q.getResultList();
}
}
