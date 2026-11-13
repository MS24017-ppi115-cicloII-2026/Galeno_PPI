package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;
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
        validarTexto(nombre, "El nombre no puede ser nulo o vacío");

        return buscarConNamedQuery("TipoMedioContacto.findByNombreLike", TipoMedioContacto.class,
                Map.of("nombre", "%" + nombre + "%"));
    }

    /**
     * Busca los tipos de medio de contacto según su estado.
     *
     * @param activo true para activos, false para inactivos
     * @return lista de tipos de medio de contacto
     */
    public List<TipoMedioContacto> buscarPorActivo(Boolean activo) {
        validarNoNulo(activo, "El estado activo no puede ser nulo");

        return buscarConNamedQuery("TipoMedioContacto.findByActivo", TipoMedioContacto.class,
                Map.of("activo", activo));
    }
}
