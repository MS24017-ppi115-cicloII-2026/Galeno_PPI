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
public class DocumentoDAO extends DefaultDAO<Documento> {

    private Persona idPersona;
    private TipoDocumento idTipoDocumento;
    @PersistenceContext(unitName = "GalenoSV")
    EntityManager em;

    @Override
    public EntityManager getEntityManger() {
        return em;
    }

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

    public boolean existeDuplicado(UUID idPersona, UUID idTipoDocumento,
            String valor, UUID idExcluir) {
        if (idPersona == null || idTipoDocumento == null
                || valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(
                    "Persona, tipo de documento y valor son obligatorios");
        }

        UUID excluir = (idExcluir != null) ? idExcluir : new UUID(0L, 0L);

        TypedQuery<Long> q = getEntityManger().createQuery(
                "SELECT COUNT(d) FROM Documento d "
                + "WHERE d.idPersona.idPersona = :persona "
                + "AND d.idTipoDocumento.idTipoDocumento = :tipo "
                + "AND LOWER(d.valor) = :valor "
                + "AND d.idDocumento <> :excluir",
                Long.class
        );

        q.setParameter("persona", idPersona);
        q.setParameter("tipo", idTipoDocumento);
        q.setParameter("valor", valor.trim().toLowerCase());
        q.setParameter("excluir", excluir);

        return q.getSingleResult() > 0;
    }

    public boolean existeValorEnOtraPersona(UUID idPersona,
            UUID idTipoDocumento, String valor, UUID idExcluir) {
        if (idPersona == null || idTipoDocumento == null
                || valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(
                    "Persona, tipo de documento y valor son obligatorios");
        }

        UUID excluir = (idExcluir != null) ? idExcluir : new UUID(0L, 0L);

        TypedQuery<Long> q = getEntityManger().createQuery(
                "SELECT COUNT(d) FROM Documento d "
                + "WHERE d.idTipoDocumento.idTipoDocumento = :tipo "
                + "AND LOWER(d.valor) = :valor "
                + "AND d.idPersona.idPersona <> :persona "
                + "AND d.idDocumento <> :excluir",
                Long.class
        );

        q.setParameter("tipo", idTipoDocumento);
        q.setParameter("valor", valor.trim().toLowerCase());
        q.setParameter("persona", idPersona);
        q.setParameter("excluir", excluir);

        return q.getSingleResult() > 0;
    }

    public boolean existeOtroDocumentoDelTipo(UUID idPersona,
            UUID idTipoDocumento, UUID idExcluir) {
        if (idPersona == null || idTipoDocumento == null) {
            throw new IllegalArgumentException(
                    "Persona y tipo de documento son obligatorios");
        }

        UUID excluir = (idExcluir != null) ? idExcluir : new UUID(0L, 0L);

        TypedQuery<Long> q = getEntityManger().createQuery(
                "SELECT COUNT(d) FROM Documento d "
                + "WHERE d.idPersona.idPersona = :persona "
                + "AND d.idTipoDocumento.idTipoDocumento = :tipo "
                + "AND d.idDocumento <> :excluir",
                Long.class
        );

        q.setParameter("persona", idPersona);
        q.setParameter("tipo", idTipoDocumento);
        q.setParameter("excluir", excluir);

        return q.getSingleResult() > 0;
    }
}
