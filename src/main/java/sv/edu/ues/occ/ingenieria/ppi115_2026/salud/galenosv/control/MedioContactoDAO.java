package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.MedioContacto;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Persona;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.TipoMedioContacto;

@Stateless
@LocalBean
public class MedioContactoDAO extends DefaultDAO<MedioContacto> {

    @PersistenceContext(unitName = "GalenoSV")
    private EntityManager em;

    @Override
    public EntityManager getEntityManger() {
        return em;
    }

    public List<MedioContacto> buscarPorValor(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(
                    "El valor no puede ser nulo o vacío"
            );
        }

        TypedQuery<MedioContacto> q = getEntityManger().createQuery(
                "SELECT m FROM MedioContacto m "
                + "WHERE LOWER(m.valor) LIKE LOWER(:valor)",
                MedioContacto.class
        );

        q.setParameter("valor", "%" + valor + "%");

        return q.getResultList();
    }

    public List<MedioContacto> buscarPorPersona(Persona persona) {
        if (persona == null) {
            throw new IllegalArgumentException(
                    "La persona no puede ser nula"
            );
        }

        TypedQuery<MedioContacto> q = getEntityManger().createQuery(
                "SELECT m FROM MedioContacto m "
                + "WHERE m.idPersona = :persona",
                MedioContacto.class
        );

        q.setParameter("persona", persona);

        return q.getResultList();
    }

    public List<MedioContacto> buscarPorTipoMedioContacto(
            TipoMedioContacto tipoMedioContacto) {

        if (tipoMedioContacto == null) {
            throw new IllegalArgumentException(
                    "El tipo de medio de contacto no puede ser nulo"
            );
        }

        TypedQuery<MedioContacto> q = getEntityManger().createQuery(
                "SELECT m FROM MedioContacto m "
                + "WHERE m.idTipoMedioContacto = :tipoMedioContacto",
                MedioContacto.class
        );

        q.setParameter("tipoMedioContacto", tipoMedioContacto);

        return q.getResultList();
    }
}
