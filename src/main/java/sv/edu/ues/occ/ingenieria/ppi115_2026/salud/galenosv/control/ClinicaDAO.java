package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;
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
        validarTexto(nombre, "El nombre no puede ser nulo o vacío");

        return buscarConNamedQuery("Clinica.findByNombreLike", Clinica.class,
                Map.of("nombre", "%" + nombre + "%"));
    }

    public List<Clinica> buscarPorActivo(Boolean activo) {
        validarNoNulo(activo, "El estado activo no puede ser nulo");

        return buscarConNamedQuery("Clinica.findByActivo", Clinica.class,
                Map.of("activo", activo));
    }

    public List<Clinica> buscarPorTipo(String tipo) {
        validarTexto(tipo, "El tipo no puede ser nulo o vacío");

        return buscarConNamedQuery("Clinica.findByTipoLike", Clinica.class,
                Map.of("tipo", "%" + tipo + "%"));
    }
}
