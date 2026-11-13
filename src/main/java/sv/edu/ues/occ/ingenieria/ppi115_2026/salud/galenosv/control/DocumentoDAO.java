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
        validarNoNulo(idPersona, "El idPersona no puede ser nulo");

        return buscarConNamedQuery("Documento.findByPersonaId", Documento.class,
                Map.of("id", idPersona));
    }

    public List<Documento> buscarPorTipoDocumento(UUID idTipoDocumento) {
        validarNoNulo(idTipoDocumento, "El idTipoDocumento no puede ser nulo");

        return buscarConNamedQuery("Documento.findByTipoDocumentoId", Documento.class,
                Map.of("id", idTipoDocumento));
    }

    public boolean existeDuplicado(UUID idPersona, UUID idTipoDocumento,
            String valor, UUID idExcluir) {
        if (idPersona == null || idTipoDocumento == null
                || valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(
                    "Persona, tipo de documento y valor son obligatorios");
        }

        UUID excluir = (idExcluir != null) ? idExcluir : new UUID(0L, 0L);

        Long cantidad = buscarUnicoConNamedQuery("Documento.countDuplicados", Long.class,
                Map.of(
                        "persona", idPersona,
                        "tipo", idTipoDocumento,
                        "valor", valor.trim().toLowerCase(),
                        "excluir", excluir));

        return cantidad != null && cantidad > 0;
    }

    public boolean existeValorEnOtraPersona(UUID idPersona,
            UUID idTipoDocumento, String valor, UUID idExcluir) {
        if (idPersona == null || idTipoDocumento == null
                || valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(
                    "Persona, tipo de documento y valor son obligatorios");
        }

        UUID excluir = (idExcluir != null) ? idExcluir : new UUID(0L, 0L);

        Long cantidad = buscarUnicoConNamedQuery("Documento.countDuplicadosEnOtraPersona", Long.class,
                Map.of(
                        "tipo", idTipoDocumento,
                        "valor", valor.trim().toLowerCase(),
                        "persona", idPersona,
                        "excluir", excluir));

        return cantidad != null && cantidad > 0;
    }

    public boolean existeOtroDocumentoDelTipo(UUID idPersona,
            UUID idTipoDocumento, UUID idExcluir) {
        if (idPersona == null || idTipoDocumento == null) {
            throw new IllegalArgumentException(
                    "Persona y tipo de documento son obligatorios");
        }

        UUID excluir = (idExcluir != null) ? idExcluir : new UUID(0L, 0L);

        Long cantidad = buscarUnicoConNamedQuery("Documento.countOtrosDelTipo", Long.class,
                Map.of(
                        "persona", idPersona,
                        "tipo", idTipoDocumento,
                        "excluir", excluir));

        return cantidad != null && cantidad > 0;
    }
}
