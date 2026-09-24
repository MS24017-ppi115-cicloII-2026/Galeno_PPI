package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.TipoMedioContacto;

@Stateless
@LocalBean
public class TipoMedioContactoDAO extends DefaultDAO<TipoMedioContacto> {

    @PersistenceContext(unitName = "GalenoSV")
    private EntityManager em;

    @Override
    public EntityManager getEntityManger() {
        return em;
    }

    /**
     * Busca tipos de medio de contacto cuyo nombre
     * contenga el texto recibido.
     *
     * @param nombre texto a buscar
     * @return lista de tipos de medio de contacto encontrados
     */
    public List<TipoMedioContacto> buscarPorNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre no puede ser nulo o vacío"
            );
        }

        TypedQuery<TipoMedioContacto> q = getEntityManger().createQuery(
                "SELECT t FROM TipoMedioContacto t "
                + "WHERE LOWER(t.nombre) LIKE LOWER(:nombre)",
                TipoMedioContacto.class
        );

        q.setParameter("nombre", "%" + nombre + "%");

        return q.getResultList();
    }

    /**
     * Busca los tipos de medio de contacto según su estado.
     *
     * @param activo true para activos, false para inactivos
     * @return lista de tipos de medio de contacto
     */
    public List<TipoMedioContacto> buscarPorActivo(Boolean activo) {
        if (activo == null) {
            throw new IllegalArgumentException(
                    "El estado activo no puede ser nulo"
            );
        }

        TypedQuery<TipoMedioContacto> q = getEntityManger().createNamedQuery(
                "TipoMedioContacto.findByActivo",
                TipoMedioContacto.class
        );

        q.setParameter("activo", activo);

        return q.getResultList();
    }
}