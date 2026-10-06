package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv;

import jakarta.ws.rs.ApplicationPath;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JakartaRestConfigurationTest {

    @Test
    void laRutaDeLaAplicacionEsResources() {

        ApplicationPath ruta = JakartaRestConfiguration.class
                .getAnnotation(ApplicationPath.class);

        assertNotNull(ruta);
        assertEquals("resources", ruta.value());
    }

    @Test
    void laConfiguracionNoDeclaraSingletons() {

        JakartaRestConfiguration configuracion =
                new JakartaRestConfiguration();

        assertTrue(configuracion.getSingletons().isEmpty(),
                "la configuracion no debe registrar singletons a mano");
    }
}
