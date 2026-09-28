package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.PersonaRol;

@FacesConverter("personaRolConverter")
public class PersonaRolConverter implements Converter<PersonaRol> {

    @Override
    public PersonaRol getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return new PersonaRol(UUID.fromString(value));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, PersonaRol value) {
        if (value == null || value.getIdPersonaRol() == null) {
            return "";
        }
        return value.getIdPersonaRol().toString();
    }
}