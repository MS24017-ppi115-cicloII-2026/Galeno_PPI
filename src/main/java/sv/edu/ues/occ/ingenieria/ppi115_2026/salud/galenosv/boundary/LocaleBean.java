package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.Locale;

@Named
@SessionScoped
public class LocaleBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Locale idioma = new Locale("es");

    public Locale getIdioma() {
        return idioma;
    }

    public String getIdiomaCodigo() {
        return idioma.getLanguage();
    }

    public void cambiarIdioma(String idiomaNuevo) {
    String[] partes = idiomaNuevo.split("_");
    this.idioma = (partes.length == 2) ? new Locale(partes[0], partes[1]) : new Locale(idiomaNuevo);
    FacesContext fc = FacesContext.getCurrentInstance();
    fc.getViewRoot().setLocale(this.idioma);
    fc.getExternalContext().getSessionMap().put("localeSesion", this.idioma);
    fc.addMessage(null, new jakarta.faces.application.FacesMessage(
        jakarta.faces.application.FacesMessage.SEVERITY_INFO, "Idioma cambiado a:", idiomaNuevo));
    }
}