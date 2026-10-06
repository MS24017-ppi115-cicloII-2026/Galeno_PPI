package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.resources;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.RuntimeDelegate;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class JakartaEE11ResourceTest {

    /**
     * Requiere una implementación de JAX-RS en el classpath para poder
     * construir el Response. El proyecto solo tiene la API, asi que
     * el test se omite en vez de fallar: queda como anotacion de lo que
     * se verifica cuando corra dentro del contenedor.
     */
    @Test
    void pingDevuelvePongConEstadoOk() {

        assumeTrue(hayRuntimeDelegate(),
                "No hay implementacion de JAX-RS en el classpath de pruebas");

        JakartaEE11Resource recurso = new JakartaEE11Resource();

        Response respuesta = recurso.ping();

        assertNotNull(respuesta);
        assertEquals(Response.Status.OK.getStatusCode(),
                respuesta.getStatus());
        assertEquals("ping Jakarta EE", respuesta.getEntity());
    }

    @Test
    void laRutaEsJakartaee11() {

        jakarta.ws.rs.Path ruta =
                JakartaEE11Resource.class.getAnnotation(
                        jakarta.ws.rs.Path.class);

        assertNotNull(ruta);
        assertEquals("jakartaee11", ruta.value());
    }

    private boolean hayRuntimeDelegate() {
        try {
            return RuntimeDelegate.getInstance() != null;
        } catch (RuntimeException ex) {
            return false;
        }
    }
}
