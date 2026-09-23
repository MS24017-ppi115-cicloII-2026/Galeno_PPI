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
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Documento;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Persona;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.TipoDocumento;

@Stateless
@LocalBean
public class DocumentoDAO extends DefaultDAO<Documento>{
    private Persona idPersona;
private TipoDocumento idTipoDocumento;
@PersistenceContext(unitName = "GalenoSV")
EntityManager em;
    @Override
    public EntityManager getEntityManger() {
return em;}
    
    public List<Documento> buscarPorPersona(UUID idPersona) {
    if (idPersona == null) {
        throw new IllegalArgumentException("El idPersona no puede ser nulo");
    }

    TypedQuery<Documento> q = getEntityManger().createQuery(
            "SELECT d FROM Documento d WHERE d.idPersona.idPersona = :id",
            Documento.class
    );

    
    q.setParameter("id", idPersona);

    return q.getResultList();
}
    public List<Documento> buscarPorTipoDocumento(UUID idTipoDocumento) {
    if (idTipoDocumento == null) {
        throw new IllegalArgumentException("El idTipoDocumento no puede ser nulo");
    }

    TypedQuery<Documento> q = getEntityManger().createQuery(
            "SELECT d FROM Documento d WHERE d.idTipoDocumento.idTipoDocumento = :id",
            Documento.class
    );

    q.setParameter("id", idTipoDocumento);

    return q.getResultList();
}
}
