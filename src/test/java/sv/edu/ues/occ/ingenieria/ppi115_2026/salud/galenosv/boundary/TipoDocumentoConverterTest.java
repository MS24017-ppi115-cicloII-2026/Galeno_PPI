package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.TipoDocumento;

import static org.junit.jupiter.api.Assertions.*;

public class TipoDocumentoConverterTest {

    private TipoDocumentoConverter converter;

    @BeforeEach
    void setUp() {
        converter = new TipoDocumentoConverter();
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

        TipoDocumento resultado = converter.getAsObject(null, null, id.toString());

        assertNotNull(resultado);
        assertEquals(id, resultado.getIdTipoDocumento());
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
                converter.getAsString(null, null, new TipoDocumento())
        );
    }

    @Test
    void getAsStringCorrectamente() {

        UUID id = UUID.randomUUID();

        TipoDocumento entidad = new TipoDocumento();
        entidad.setIdTipoDocumento(id);

        assertEquals(
                id.toString(),
                converter.getAsString(null, null, entidad)
        );
    }
}
