package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.OrdenExamen;

@FacesConverter("ordenExamenConverter")
public class OrdenExamenConverter implements Converter<OrdenExamen> {

    @Override
    public OrdenExamen getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return new OrdenExamen(UUID.fromString(value));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, OrdenExamen value) {
        if (value == null || value.getIdOrdenExamen() == null) {
            return "";
        }
        return value.getIdOrdenExamen().toString();
    }
}