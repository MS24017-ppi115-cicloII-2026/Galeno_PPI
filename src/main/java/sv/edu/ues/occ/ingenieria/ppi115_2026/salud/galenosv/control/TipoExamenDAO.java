package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.TipoExamen;

@Stateless
@LocalBean
public class TipoExamenDAO extends DefaultDAO<TipoExamen> {

    @PersistenceContext(unitName = "GalenoSV")
    EntityManager em;

    @Override
    public EntityManager getEntityManger() {
        return em;
    }
    public List<TipoExamen> buscarPorNombre(String nombre) {
        validarTexto(nombre, "El nombre no puede ser nulo o vacío");

        return buscarConNamedQuery("TipoExamen.findByNombreLike", TipoExamen.class,
                Map.of("nombre", "%" + nombre + "%"));
    }
}
