package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ConsultaProcedimiento;

@FacesConverter("consultaProcedimientoConverter")
public class ConsultaProcedimientoConverter implements Converter<ConsultaProcedimiento> {

    @Override
    public ConsultaProcedimiento getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return new ConsultaProcedimiento(UUID.fromString(value));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, ConsultaProcedimiento value) {
        if (value == null || value.getIdConsultaProcedimiento() == null) {
            return "";
        }
        return value.getIdConsultaProcedimiento().toString();
    }
}