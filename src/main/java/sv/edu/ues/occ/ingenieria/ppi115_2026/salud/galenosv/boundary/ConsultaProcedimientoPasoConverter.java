package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ConsultaProcedimientoPaso;

@FacesConverter("consultaProcedimientoPasoConverter")
public class ConsultaProcedimientoPasoConverter implements Converter<ConsultaProcedimientoPaso> {

    @Override
    public ConsultaProcedimientoPaso getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return new ConsultaProcedimientoPaso(UUID.fromString(value));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, ConsultaProcedimientoPaso value) {
        if (value == null || value.getIdConsultaProcedimientoPaso() == null) {
            return "";
        }
        return value.getIdConsultaProcedimientoPaso().toString();
    }
}