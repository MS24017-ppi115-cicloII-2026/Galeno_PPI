package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Consulta;

import static org.junit.jupiter.api.Assertions.*;

public class ConsultaConverterTest {

    private ConsultaConverter converter;

    @BeforeEach
    void setUp() {
        converter = new ConsultaConverter();
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

        Consulta resultado = converter.getAsObject(null, null, id.toString());

        assertNotNull(resultado);
        assertEquals(id, resultado.getIdConsulta());
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
                converter.getAsString(null, null, new Consulta())
        );
    }

    @Test
    void getAsStringCorrectamente() {

        UUID id = UUID.randomUUID();

        Consulta entidad = new Consulta();
        entidad.setIdConsulta(id);

        assertEquals(
                id.toString(),
                converter.getAsString(null, null, entidad)
        );
    }
}
