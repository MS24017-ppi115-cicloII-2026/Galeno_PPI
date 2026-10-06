package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIViewRoot;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocaleBeanTest {

    @Mock
    private FacesContext facesContext;

    @Mock
    private UIViewRoot viewRoot;

    @Mock
    private ExternalContext externalContext;

    private Map<String, Object> sessionMap;

    private MockedStatic<FacesContext> facesContextStatic;

    private LocaleBean bean;

    @BeforeEach
    void setUp() {
        facesContextStatic = mockStatic(FacesContext.class);
        sessionMap = new HashMap<>();
        bean = new LocaleBean();
    }

    /**
     * Solo lo llaman los tests que pasan por cambiarIdioma, para no
     * dejar stubs sin usar en el resto (Mockito es estricto).
     */
    private void prepararContexto() {
        facesContextStatic.when(FacesContext::getCurrentInstance)
                .thenReturn(facesContext);
        when(facesContext.getViewRoot()).thenReturn(viewRoot);
        when(facesContext.getExternalContext()).thenReturn(externalContext);
        when(externalContext.getSessionMap()).thenReturn(sessionMap);
    }

    @AfterEach
    void tearDown() {
        facesContextStatic.close();
    }

    @Test
    void elIdiomaPorDefectoEspanol() {

        assertEquals(new Locale("es"), bean.getIdioma());
        assertEquals("es", bean.getIdiomaCodigo());
    }

    @Test
    void cambiarIdiomaConPais() {

        prepararContexto();

        bean.cambiarIdioma("en_US");

        assertEquals(new Locale("en", "US"), bean.getIdioma());
        assertEquals("en", bean.getIdiomaCodigo());
        assertEquals("US", bean.getIdioma().getCountry());

        verify(viewRoot).setLocale(new Locale("en", "US"));
    }

    @Test
    void cambiarIdiomaSinPais() {

        prepararContexto();

        bean.cambiarIdioma("fr");

        assertEquals(new Locale("fr"), bean.getIdioma());
        assertEquals("fr", bean.getIdiomaCodigo());
        assertEquals("", bean.getIdioma().getCountry());

        verify(viewRoot).setLocale(new Locale("fr"));
    }

    @Test
    void cambiarIdiomaGuardaElLocaleEnLaSesion() {

        prepararContexto();

        bean.cambiarIdioma("en_US");

        assertEquals(new Locale("en", "US"), sessionMap.get("localeSesion"));
    }

    @Test
    void cambiarIdiomaMuestraUnMensajeInformativo() {

        prepararContexto();

        bean.cambiarIdioma("en_US");

        ArgumentCaptor<FacesMessage> captor =
                ArgumentCaptor.forClass(FacesMessage.class);

        verify(facesContext).addMessage(isNull(), captor.capture());

        FacesMessage mensaje = captor.getValue();

        assertEquals(FacesMessage.SEVERITY_INFO, mensaje.getSeverity());
        assertEquals("Idioma cambiado a:", mensaje.getSummary());
        assertEquals("en_US", mensaje.getDetail());
    }

    @Test
    void cambiarIdiomaSobrescribeElAnterior() {

        prepararContexto();

        bean.cambiarIdioma("en_US");
        bean.cambiarIdioma("zh_CN");

        assertEquals(new Locale("zh", "CN"), bean.getIdioma());
        assertEquals("zh", bean.getIdiomaCodigo());
        assertEquals(new Locale("zh", "CN"), sessionMap.get("localeSesion"));
    }

    /**
     * Con más de un "_" el bean no separa idioma/país y usa el
     * string completo como idioma (conducta actual).
     */
    @Test
    void cambiarIdiomaConFormatoDeVariasPartesUsaElStringCompleto() {

        prepararContexto();

        bean.cambiarIdioma("pt_BR_extra");

        assertEquals(new Locale("pt_BR_extra"), bean.getIdioma());
        assertEquals("pt_br_extra", bean.getIdiomaCodigo());
    }
}
