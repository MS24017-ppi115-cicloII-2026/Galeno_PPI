package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Procedimiento;

@FacesConverter("procedimientoConverter")
public class ProcedimientoConverter implements Converter<Procedimiento> {

    @Override
    public Procedimiento getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return new Procedimiento(UUID.fromString(value));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Procedimiento value) {
        if (value == null || value.getIdProcedimiento() == null) {
            return "";
        }
        return value.getIdProcedimiento().toString();
    }
}