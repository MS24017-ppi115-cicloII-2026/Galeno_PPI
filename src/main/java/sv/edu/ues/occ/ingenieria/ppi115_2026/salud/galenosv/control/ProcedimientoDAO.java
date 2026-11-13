package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Procedimiento;

@Stateless
@LocalBean
public class ProcedimientoDAO extends DefaultDAO<Procedimiento>{

    @PersistenceContext(unitName = "GalenoSV")
    EntityManager em;

    @Override
    public EntityManager getEntityManger() {
        return em;
    }

    public List<Procedimiento> buscarPorNombre(String nombre) {
        validarTexto(nombre, "El nombre no puede ser nulo o vacío");

        return buscarConNamedQuery("Procedimiento.findByNombreLike", Procedimiento.class,
                Map.of("nombre", "%" + nombre + "%"));
    }

    public List<Procedimiento> buscarPorActivo(Boolean activo) {
        validarNoNulo(activo, "El estado activo no puede ser nulo");

        return buscarConNamedQuery("Procedimiento.findByActivoOrdenadoPorNombre", Procedimiento.class,
                Map.of("activo", activo));
    }
}
