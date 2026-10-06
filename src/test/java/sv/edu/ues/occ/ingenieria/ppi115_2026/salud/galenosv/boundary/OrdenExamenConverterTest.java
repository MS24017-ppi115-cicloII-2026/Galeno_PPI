package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.OrdenExamen;

import static org.junit.jupiter.api.Assertions.*;

public class OrdenExamenConverterTest {

    private OrdenExamenConverter converter;

    @BeforeEach
    void setUp() {
        converter = new OrdenExamenConverter();
    }

    @Test
    void getAsObjectNulo() {
        assertNull(converter.getAsObject(null, null, null));
    }

    @Test
    void getAsObjectVacio() {
        assertNull(converter.getAsObject(null, null, ""));
    }

    @Test
    void getAsObjectBlanco() {
        assertNull(converter.getAsObject(null, null, "   "));
    }

    @Test
    void getAsObjectCorrectamente() {

        UUID id = UUID.randomUUID();

        OrdenExamen resultado = converter.getAsObject(null, null, id.toString());

        assertNotNull(resultado);
        assertEquals(id, resultado.getIdOrdenExamen());
    }

    @Test
    void getAsObjectUuidInvalido() {
        assertThrows(
                IllegalArgumentException.class,
                () -> converter.getAsObject(null, null, "no-es-uuid")
        );
    }

    @Test
    void getAsStringNulo() {
        assertEquals("", converter.getAsString(null, null, null));
    }

    @Test
    void getAsStringConIdNulo() {
        assertEquals(
                "",
                converter.getAsString(null, null, new OrdenExamen())
        );
    }

    @Test
    void getAsStringCorrectamente() {

        UUID id = UUID.randomUUID();

        OrdenExamen entidad = new OrdenExamen();
        entidad.setIdOrdenExamen(id);

        assertEquals(
                id.toString(),
                converter.getAsString(null, null, entidad)
        );
    }
}
