package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Examen;

@FacesConverter("examenConverter")
public class ExamenConverter implements Converter<Examen> {


@Override
public Examen getAsObject(FacesContext context, UIComponent component, String value) {

    if (value == null || value.isBlank()) {
        return null;
    }

    return new Examen(UUID.fromString(value));
}

@Override
public String getAsString(FacesContext context, UIComponent component, Examen value) {

    if (value == null || value.getIdExamen() == null) {
        return "";
    }

    return value.getIdExamen().toString();
}

}
