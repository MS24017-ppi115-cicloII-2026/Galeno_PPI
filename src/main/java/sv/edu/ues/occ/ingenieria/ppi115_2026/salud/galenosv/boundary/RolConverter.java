package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Rol;

@FacesConverter("rolConverter")
public class RolConverter implements Converter<Rol> {

    @Override
    public Rol getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return new Rol(UUID.fromString(value));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Rol value) {
        if (value == null || value.getIdRol() == null) {
            return "";
        }
        return value.getIdRol().toString();
    }
}