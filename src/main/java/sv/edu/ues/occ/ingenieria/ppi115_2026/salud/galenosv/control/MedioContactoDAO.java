package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;
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
        validarTexto(valor, "El valor no puede ser nulo o vacío");

        return buscarConNamedQuery("MedioContacto.findByValorLike", MedioContacto.class,
                Map.of("valor", "%" + valor + "%"));
    }

    public List<MedioContacto> buscarPorPersona(Persona persona) {
        validarNoNulo(persona, "La persona no puede ser nula");

        return buscarConNamedQuery("MedioContacto.findByPersona", MedioContacto.class,
                Map.of("persona", persona));
    }

    public List<MedioContacto> buscarPorTipoMedioContacto(
            TipoMedioContacto tipoMedioContacto) {

        validarNoNulo(tipoMedioContacto,
                "El tipo de medio de contacto no puede ser nulo");

        return buscarConNamedQuery("MedioContacto.findByTipoMedioContacto", MedioContacto.class,
                Map.of("tipoMedioContacto", tipoMedioContacto));
    }
}
