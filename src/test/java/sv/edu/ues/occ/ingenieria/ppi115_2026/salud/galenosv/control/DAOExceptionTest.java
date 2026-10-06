package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DAOExceptionTest {

    @Test
    void constructorConMensaje() {

        DAOException e = new DAOException("fallo al guardar");

        assertEquals("fallo al guardar", e.getMessage());
        assertNull(e.getCause());
    }

    @Test
    void constructorConMensajeYCausa() {

        RuntimeException causa = new RuntimeException("fk violation");

        DAOException e = new DAOException("no se puede eliminar", causa);

        assertEquals("no se puede eliminar", e.getMessage());
        assertSame(causa, e.getCause());
    }

    @Test
    void esUnaIllegalStateException() {

        assertInstanceOf(
                IllegalStateException.class,
                new DAOException("x")
        );
    }
}
