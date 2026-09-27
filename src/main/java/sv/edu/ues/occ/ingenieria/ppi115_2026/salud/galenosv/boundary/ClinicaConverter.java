package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Clinica;

@FacesConverter("clinicaConverter")
public class ClinicaConverter implements Converter<Clinica> {

    @Override
    public Clinica getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return new Clinica(UUID.fromString(value));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Clinica value) {
        if (value == null || value.getIdClinica() == null) {
            return "";
        }
        return value.getIdClinica().toString();
    }
}
