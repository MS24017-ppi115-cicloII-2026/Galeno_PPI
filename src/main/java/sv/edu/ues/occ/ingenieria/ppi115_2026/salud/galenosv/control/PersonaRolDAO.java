package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import java.util.List;
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

    Rol rol = em.find(Rol.class, registro.getIdRol().getIdRol());

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

    Clinica clinica = em.find(
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

    Persona persona = em.find(
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

    TypedQuery<Long> q = em.createQuery(
            "SELECT COUNT(pr) FROM PersonaRol pr "
            + "WHERE pr.idPersona = :persona "
            + (esActualizacion
                    ? "AND pr.idPersonaRol <> :idPersonaRol "
                    : ""),
            Long.class
    );

    q.setParameter("persona", persona);

    if (esActualizacion) {
        q.setParameter(
                "idPersonaRol",
                registro.getIdPersonaRol()
        );
    }

    Long cantidad = q.getSingleResult();

    if (cantidad > 0) {
        throw new IllegalArgumentException(
                "La persona seleccionada ya tiene un rol asignado"
        );
    }

    registro.setIdPersona(persona);
    registro.setIdRol(rol);
    registro.setIdClinica(clinica);
}

public List<PersonaRol> buscarPorPersona(Persona persona) {

    if (persona == null) {
        throw new IllegalArgumentException(
                "La persona no puede ser nula"
        );
    }

    TypedQuery<PersonaRol> q = getEntityManger().createQuery(
            "SELECT p FROM PersonaRol p "
            + "WHERE p.idPersona = :persona",
            PersonaRol.class
    );

    q.setParameter("persona", persona);

    return q.getResultList();
}

public List<PersonaRol> buscarPorRol(Rol rol) {

    if (rol == null) {
        throw new IllegalArgumentException(
                "El rol no puede ser nulo"
        );
    }

    TypedQuery<PersonaRol> q = getEntityManger().createQuery(
            "SELECT p FROM PersonaRol p "
            + "JOIN FETCH p.idPersona "
            + "JOIN FETCH p.idRol "
            + "LEFT JOIN FETCH p.idClinica "
            + "WHERE p.idRol = :rol",
            PersonaRol.class
    );

    q.setParameter("rol", rol);

    return q.getResultList();
}

public List<PersonaRol> buscarPorClinica(Clinica clinica) {

    if (clinica == null) {
        throw new IllegalArgumentException(
                "La clínica no puede ser nula"
        );
    }

    TypedQuery<PersonaRol> q = getEntityManger().createQuery(
            "SELECT p FROM PersonaRol p "
            + "WHERE p.idClinica = :clinica",
            PersonaRol.class
    );

    q.setParameter("clinica", clinica);

    return q.getResultList();
}

    
    public PersonaRol buscarConRelaciones(UUID idPersonaRol) {

        if (idPersonaRol == null) {
            return null;
        }

        TypedQuery<PersonaRol> q = getEntityManger().createQuery(
                "SELECT pr FROM PersonaRol pr "
                + "JOIN FETCH pr.idPersona "
                + "JOIN FETCH pr.idRol "
                + "LEFT JOIN FETCH pr.idClinica "
                + "WHERE pr.idPersonaRol = :id",
                PersonaRol.class
        );

        q.setParameter("id", idPersonaRol);
        q.setMaxResults(1);

        List<PersonaRol> resultado = q.getResultList();

        return resultado.isEmpty() ? null : resultado.getFirst();
    }

}
