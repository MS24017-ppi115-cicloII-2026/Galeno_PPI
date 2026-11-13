package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Clinica;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Persona;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.PersonaRol;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Rol;

@Stateless
@LocalBean
public class PersonaRolDAO extends DefaultDAO<PersonaRol> {

@PersistenceContext(unitName = "GalenoSV")
private EntityManager em;

@Override
public EntityManager getEntityManger() {
    return em;
}

@Override
public void crear(PersonaRol registro) {
    validarPersonaRol(registro, false);
    super.crear(registro);
}

@Override
public void actualizar(PersonaRol registro) {
    validarPersonaRol(registro, true);
    super.actualizar(registro);
}

private void validarPersonaRol(PersonaRol registro, boolean esActualizacion) {

    if (registro == null) {
        throw new IllegalArgumentException(
                "El registro de persona y rol no puede ser nulo"
        );
    }

    if (registro.getIdPersona() == null) {
        throw new IllegalArgumentException(
                "Debe seleccionar una persona"
        );
    }

    if (registro.getIdRol() == null) {
        throw new IllegalArgumentException(
                "Debe seleccionar un rol"
        );
    }

    if (registro.getIdClinica() == null) {
        throw new IllegalArgumentException(
                "Debe seleccionar una clínica"
        );
    }

    if (registro.getIdRol().getIdRol() == null) {
        throw new IllegalArgumentException(
                "El rol seleccionado no es válido"
        );
    }

    if (registro.getIdClinica().getIdClinica() == null) {
        throw new IllegalArgumentException(
                "La clínica seleccionada no es válida"
        );
    }

    EntityManager emLocal = requireEntityManager();

    Rol rol = emLocal.find(Rol.class, registro.getIdRol().getIdRol());

    if (rol == null) {
        throw new IllegalArgumentException(
                "El rol seleccionado no existe"
        );
    }

    if (!Boolean.TRUE.equals(rol.getActivo())) {
        throw new IllegalArgumentException(
                "No se puede asignar un rol inactivo"
        );
    }

    Clinica clinica = emLocal.find(
            Clinica.class,
            registro.getIdClinica().getIdClinica()
    );

    if (clinica == null) {
        throw new IllegalArgumentException(
                "La clínica seleccionada no existe"
        );
    }

    if (!Boolean.TRUE.equals(clinica.getActivo())) {
        throw new IllegalArgumentException(
                "No se puede asignar una clínica inactiva"
        );
    }

    Persona persona = emLocal.find(
            Persona.class,
            registro.getIdPersona().getIdPersona()
    );

    if (persona == null) {
        throw new IllegalArgumentException(
                "La persona seleccionada no existe"
        );
    }

    if (esActualizacion && registro.getIdPersonaRol() == null) {
        throw new IllegalArgumentException(
                "El ID del registro es obligatorio para actualizar"
        );
    }

    Map<String, Object> parametros = new HashMap<>();
    parametros.put("persona", persona);
    parametros.put("rol", rol);
    parametros.put("clinica", clinica);

    String nombreConsulta = "PersonaRol.countDuplicados";
    if (esActualizacion) {
        nombreConsulta = "PersonaRol.countDuplicadosExcluyendoRegistro";
        parametros.put(
                "idPersonaRol",
                registro.getIdPersonaRol()
        );
    }

    Long cantidad = buscarUnicoConNamedQuery(nombreConsulta, Long.class, parametros);

    if (cantidad != null && cantidad > 0) {
        throw new IllegalArgumentException(
                "La persona seleccionada ya tiene ese rol asignado en esa clínica"
        );
    }

    registro.setIdPersona(persona);
    registro.setIdRol(rol);
    registro.setIdClinica(clinica);
}

public List<PersonaRol> buscarPorPersona(Persona persona) {

    validarNoNulo(persona, "La persona no puede ser nula");

    return buscarConNamedQuery("PersonaRol.findByPersona", PersonaRol.class,
            Map.of("persona", persona));
}

public List<PersonaRol> buscarPorRol(Rol rol) {

    validarNoNulo(rol, "El rol no puede ser nulo");

    return buscarConNamedQuery("PersonaRol.findByRolConRelaciones", PersonaRol.class,
            Map.of("rol", rol));
}

public List<PersonaRol> buscarPorClinica(Clinica clinica) {

    validarNoNulo(clinica, "La clínica no puede ser nula");

    return buscarConNamedQuery("PersonaRol.findByClinica", PersonaRol.class,
            Map.of("clinica", clinica));
}


    public PersonaRol buscarConRelaciones(UUID idPersonaRol) {

        if (idPersonaRol == null) {
            return null;
        }

        return buscarUnicoConNamedQuery("PersonaRol.findPorIdConRelaciones",
                PersonaRol.class, Map.of("id", idPersonaRol));

    }

}
