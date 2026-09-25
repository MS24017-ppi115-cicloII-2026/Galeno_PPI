package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;
 
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Consulta;
 
@FacesConverter("consultaConverter")
public class ConsultaConverter implements Converter<Consulta> {
 
    @Override
    public Consulta getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return new Consulta(UUID.fromString(value));
    }
 
    @Override
    public String getAsString(FacesContext context, UIComponent component, Consulta value) {
        if (value == null || value.getIdConsulta() == null) {
            return "";
        }
        return value.getIdConsulta().toString();
    }
}