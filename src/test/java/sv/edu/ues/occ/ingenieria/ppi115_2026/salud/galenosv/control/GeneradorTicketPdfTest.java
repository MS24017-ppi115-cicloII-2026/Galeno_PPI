package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import java.nio.charset.Charset;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.TicketCita.PacienteTicket;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.TicketCita.PasoTicket;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.TicketCita.ProcedimientoTicket;

class GeneradorTicketPdfTest {
    private static final Date PASADO = Date.from(Instant.EPOCH);
    private static final Date FUTURO = Date.from(Instant.parse("2200-11-18T14:30:00Z"));
    private static final ProcedimientoTicket PROCEDIMIENTO = new ProcedimientoTicket("Consulta general",
            List.of(new PasoTicket("Recepción", "Secretaría"), new PasoTicket("Atención", "Medicina")),
            List.of("Hemograma"), List.of("Traiga sus resultados."));

    @Test
    void generaPdfConLosDatosDeLaCita() {
        String pdf = pdf(FUTURO, null, List.of(PROCEDIMIENTO));
        assertTrue(pdf.startsWith("%PDF-1.4"));
        assertTrue(pdf.stripTrailing().endsWith("%%EOF"));
        for (String texto : List.of("Ana López", "Clínica Central", "B8948B7F", "DUI 123",
                "1. Recepción · Secretaría", "2. Atención · Medicina", "Exámenes previstos:", "Hemograma",
                "Preséntese 15 minutos antes con su DUI.", "Traiga sus resultados.", "Referencia: HU1")) {
            assertTrue(pdf.contains(texto), texto);
        }
    }

    @Test
    void distingueLosTresEstadosYDaPrioridadALaFechaFin() {
        assertTrue(pdf(FUTURO, null, List.of()).contains("(Programada)"));
        assertTrue(pdf(PASADO, null, List.of()).contains("(En curso)"));
        assertTrue(pdf(FUTURO, PASADO, List.of()).contains("(Finalizada)"));
    }

    @Test
    void paginaOchoProcedimientosYConservaElContenido() {
        String pdf = pdf(FUTURO, null, Collections.nCopies(8, PROCEDIMIENTO));
        assertTrue(pdf.contains("/Kids [5 0 R 7 0 R ] /Count 2"));
        assertTrue(pdf.contains("GalenoSV · Código B8948B7F"));
        assertEquals(8, pdf.split("Consulta general", -1).length - 1);
    }

    @Test
    void escapaLosDelimitadoresDelPdf() {
        assertEquals("\\(A\\)\\\\B", GeneradorTicketPdf.esc("(A)\\B"));
    }

    @Test
    void cortaEntrePalabras() {
        assertEquals(List.of("Una cita", "médica"), GeneradorTicketPdf.cortar("Una cita médica", 8));
    }

    @Test
    void rechazaUnTicketNulo() {
        assertThrows(IllegalArgumentException.class, () -> new GeneradorTicketPdf().generar(null));
    }

    private String pdf(Date inicio, Date fin, List<ProcedimientoTicket> procedimientos) {
        var paciente = new PacienteTicket("Ana López", "34 años", "DUI", "DUI 123", "7777-0000");
        var ticket = new TicketCita("b8948b7f-4703-4711-a3b2-01f67d0ac845", inicio, fin, "Clínica Central",
                paciente, procedimientos, "Lleve sus documentos.", "HU1");
        return new String(new GeneradorTicketPdf().generar(ticket), Charset.forName("windows-1252"));
    }
}
