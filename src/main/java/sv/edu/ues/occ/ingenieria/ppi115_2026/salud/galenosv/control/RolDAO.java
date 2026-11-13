package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Rol;

@Stateless
@LocalBean
public class RolDAO extends DefaultDAO<Rol> {

    @PersistenceContext(unitName = "GalenoSV")
    private EntityManager em;

    @Override
    public EntityManager getEntityManger() {
        return em;
    }

    public List<Rol> buscarPorNombre(String nombre) {
        validarTexto(nombre, "El nombre no puede ser nulo o vacío");

        return buscarConNamedQuery("Rol.findByNombreLike", Rol.class,
                Map.of("nombre", "%" + nombre + "%"));
    }

    public List<Rol> buscarPorActivo(Boolean activo) {
        validarNoNulo(activo, "El estado activo no puede ser nulo");

        return buscarConNamedQuery("Rol.findByActivo", Rol.class,
                Map.of("activo", activo));
    }

    public List<Rol> buscarRolesActivos() {
        return buscarConNamedQuery("Rol.findActivosOrdenadosPorNombre", Rol.class,
                Map.of());
    }

}
