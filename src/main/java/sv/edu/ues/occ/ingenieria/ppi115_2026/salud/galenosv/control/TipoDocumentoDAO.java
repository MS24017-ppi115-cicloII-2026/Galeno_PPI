package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.TipoDocumento;

@Stateless
@LocalBean
public class TipoDocumentoDAO extends DefaultDAO<TipoDocumento> {

    @PersistenceContext(unitName = "GalenoSV")
    private EntityManager em;

    @Override
    public EntityManager getEntityManger() {
        return em;
    }

    public List<TipoDocumento> buscarPorNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre no puede ser nulo o vacío"
            );
        }

        TypedQuery<TipoDocumento> query = getEntityManger().createNamedQuery(
                "TipoDocumento.findByNombre",
                TipoDocumento.class
        );

        query.setParameter("nombre", nombre);

        return query.getResultList();
    }

    public List<TipoDocumento> buscarPorIndicaciones(String indicaciones) {
        if (indicaciones == null || indicaciones.isBlank()) {
            throw new IllegalArgumentException(
                    "Las indicaciones no pueden ser nulas o vacías"
            );
        }

        TypedQuery<TipoDocumento> query = getEntityManger().createNamedQuery(
                "TipoDocumento.findByIndicaciones",
                TipoDocumento.class
        );

        query.setParameter("indicaciones", indicaciones);

        return query.getResultList();
    }

    public List<TipoDocumento> buscarPorExpresionRegular(
            String expresionRegular) {

        if (expresionRegular == null || expresionRegular.isBlank()) {
            throw new IllegalArgumentException(
                    "La expresión regular no puede ser nula o vacía"
            );
        }

        TypedQuery<TipoDocumento> query = getEntityManger().createNamedQuery(
                "TipoDocumento.findByExpresionRegular",
                TipoDocumento.class
        );

        query.setParameter("expresionRegular", expresionRegular);

        return query.getResultList();
    }

    public List<TipoDocumento> buscarPorActivo(Boolean activo) {
        if (activo == null) {
            throw new IllegalArgumentException(
                    "El estado activo no puede ser nulo"
            );
        }

        TypedQuery<TipoDocumento> query = getEntityManger().createNamedQuery(
                "TipoDocumento.findByActivo",
                TipoDocumento.class
        );

        query.setParameter("activo", activo);

        return query.getResultList();
    }
}