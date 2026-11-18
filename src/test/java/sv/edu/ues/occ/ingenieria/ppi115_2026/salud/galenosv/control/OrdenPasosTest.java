package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ProcedimientoPaso;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ProcedimientoPasoSecuencia;

class OrdenPasosTest {
    private final ProcedimientoPaso primero = paso();
    private final ProcedimientoPaso segundo = paso();

    @Test
    void interpretaAfterYVariantesEnEspanol() {
        for (String tipo : List.of("AFTER", "Después", "posterior")) {
            assertEquals(List.of(primero, segundo), OrdenPasos.ordenar(List.of(segundo, primero),
                    List.of(secuencia(segundo, primero, tipo))));
        }
    }

    @Test
    void interpretaBefore() {
        assertEquals(List.of(primero, segundo), OrdenPasos.ordenar(List.of(segundo, primero),
                List.of(secuencia(primero, segundo, "BEFORE"))));
    }

    @Test
    void conservaElOrdenSinSecuencias() {
        var pasos = List.of(segundo, primero);
        assertEquals(pasos, OrdenPasos.ordenar(pasos, List.of()));
    }

    @Test
    void unCicloConservaElOrdenDeLosPasosPendientes() {
        var pasos = List.of(segundo, primero);
        var secuencias = List.of(secuencia(primero, segundo, "AFTER"), secuencia(segundo, primero, "AFTER"));
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> assertEquals(pasos, OrdenPasos.ordenar(pasos, secuencias)));
    }

    private ProcedimientoPaso paso() {
        var paso = new ProcedimientoPaso();
        paso.setIdProcedimientoPaso(UUID.randomUUID());
        return paso;
    }

    private ProcedimientoPasoSecuencia secuencia(ProcedimientoPaso paso, ProcedimientoPaso referencia, String tipo) {
        var secuencia = new ProcedimientoPasoSecuencia();
        secuencia.setIdProcedimientoPaso(paso);
        secuencia.setIdProcedimientoPasoReferencia(referencia.getIdProcedimientoPaso());
        secuencia.setTipoSecuencia(tipo);
        return secuencia;
    }
}
