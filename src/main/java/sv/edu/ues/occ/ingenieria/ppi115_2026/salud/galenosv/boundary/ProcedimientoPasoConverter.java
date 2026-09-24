package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ProcedimientoPaso;

@FacesConverter("procedimientoPasoConverter")
public class ProcedimientoPasoConverter implements Converter<ProcedimientoPaso> {

    @Override
    public ProcedimientoPaso getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return new ProcedimientoPaso(UUID.fromString(value));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, ProcedimientoPaso value) {
        if (value == null || value.getIdProcedimientoPaso() == null) {
            return "";
        }
        return value.getIdProcedimientoPaso().toString();
    }
}