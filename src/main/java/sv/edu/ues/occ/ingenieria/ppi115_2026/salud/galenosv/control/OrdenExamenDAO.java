/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;
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
        validarNoNulo(idConsultaProcedimientoPaso,
                "El idConsultaProcedimientoPaso no puede ser nulo");

        return buscarConNamedQuery("OrdenExamen.findByConsultaProcedimientoPasoId",
                OrdenExamen.class, Map.of("id", idConsultaProcedimientoPaso));
    }
}
