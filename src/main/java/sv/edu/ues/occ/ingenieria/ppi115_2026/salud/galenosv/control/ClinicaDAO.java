package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Clinica;

@Stateless
@LocalBean
public class ClinicaDAO extends DefaultDAO<Clinica> {

    @PersistenceContext(unitName = "GalenoSV")
    private EntityManager em;

    @Override
    public EntityManager getEntityManger() {
        return em;
    }

    public List<Clinica> buscarPorNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre no puede ser nulo o vacío"
            );
        }

        TypedQuery<Clinica> q = getEntityManger().createQuery(
                "SELECT c FROM Clinica c "
                + "WHERE LOWER(c.nombre) LIKE LOWER(:nombre)",
                Clinica.class
        );

        q.setParameter("nombre", "%" + nombre + "%");

        return q.getResultList();
    }

    public List<Clinica> buscarPorActivo(Boolean activo) {
        if (activo == null) {
            throw new IllegalArgumentException(
                    "El estado activo no puede ser nulo"
            );
        }

        TypedQuery<Clinica> q = getEntityManger().createNamedQuery(
                "Clinica.findByActivo",
                Clinica.class
        );

        q.setParameter("activo", activo);

        return q.getResultList();
    }

    public List<Clinica> buscarPorTipo(String tipo) {
        if (tipo == null || tipo.isBlank()) {
            throw new IllegalArgumentException(
                    "El tipo no puede ser nulo o vacío"
            );
        }

        TypedQuery<Clinica> q = getEntityManger().createQuery(
                "SELECT c FROM Clinica c "
                + "WHERE LOWER(c.tipo) LIKE LOWER(:tipo)",
                Clinica.class
        );

        q.setParameter("tipo", "%" + tipo + "%");

        return q.getResultList();
    }
}
