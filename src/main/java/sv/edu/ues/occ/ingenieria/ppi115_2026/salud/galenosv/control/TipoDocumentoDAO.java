package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;
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
        validarTexto(nombre, "El nombre no puede ser nulo o vacío");

        return buscarConNamedQuery("TipoDocumento.findByNombre", TipoDocumento.class,
                Map.of("nombre", nombre));
    }

    public List<TipoDocumento> buscarPorIndicaciones(String indicaciones) {
        validarTexto(indicaciones, "Las indicaciones no pueden ser nulas o vacías");

        return buscarConNamedQuery("TipoDocumento.findByIndicaciones", TipoDocumento.class,
                Map.of("indicaciones", indicaciones));
    }

    public List<TipoDocumento> buscarPorExpresionRegular(
            String expresionRegular) {

        validarTexto(expresionRegular,
                "La expresión regular no puede ser nula o vacía");

        return buscarConNamedQuery("TipoDocumento.findByExpresionRegular", TipoDocumento.class,
                Map.of("expresionRegular", expresionRegular));
    }

    public List<TipoDocumento> buscarPorActivo(Boolean activo) {
        validarNoNulo(activo, "El estado activo no puede ser nulo");

        return buscarConNamedQuery("TipoDocumento.findByActivo", TipoDocumento.class,
                Map.of("activo", activo));
    }
}
