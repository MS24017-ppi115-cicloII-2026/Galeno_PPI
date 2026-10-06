package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ConsultaProcedimientoPaso;

import static org.junit.jupiter.api.Assertions.*;

public class ConsultaProcedimientoPasoConverterTest {

    private ConsultaProcedimientoPasoConverter converter;

    @BeforeEach
    void setUp() {
        converter = new ConsultaProcedimientoPasoConverter();
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

        ConsultaProcedimientoPaso resultado = converter.getAsObject(null, null, id.toString());

        assertNotNull(resultado);
        assertEquals(id, resultado.getIdConsultaProcedimientoPaso());
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
                converter.getAsString(null, null, new ConsultaProcedimientoPaso())
        );
    }

    @Test
    void getAsStringCorrectamente() {

        UUID id = UUID.randomUUID();

        ConsultaProcedimientoPaso entidad = new ConsultaProcedimientoPaso();
        entidad.setIdConsultaProcedimientoPaso(id);

        assertEquals(
                id.toString(),
                converter.getAsString(null, null, entidad)
        );
    }
}
