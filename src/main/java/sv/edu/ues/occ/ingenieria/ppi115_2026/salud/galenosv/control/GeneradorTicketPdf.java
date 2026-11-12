package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Genera el ticket PDF de una cita SIN librerias externas: arma a mano las pocas partes
 * de un PDF (catalogo, paginas, fuente Helvetica estandar, contenido y tabla xref).
 *
 * Uso: byte[] pdf = new GeneradorTicketPdf().generar(ticket);
 *
 * El texto va en codificacion WinAnsi (ISO-8859-1), asi que se ven bien tildes y la enie.
 * Los caracteres fuera de ese conjunto se cambian por "?".
 */
public class GeneradorTicketPdf {

    static final ZoneId ZONA = ZoneId.of("America/El_Salvador");
    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.of("es", "SV"));
    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("hh:mm a", Locale.of("es", "SV"));
    private static final int ANCHO_LINEA = 78;      // caracteres por linea antes de cortar el texto

    /** Devuelve el PDF completo (una pagina carta) como arreglo de bytes. */
    public byte[] generar(TicketCita t) {
        if (t == null) {
            throw new IllegalArgumentException("El ticket no puede ser nulo");
        }
        String contenido = armarContenido(t);
        byte[] flujo = contenido.getBytes(StandardCharsets.ISO_8859_1);

        List<byte[]> objetos = new ArrayList<>();
        objetos.add(texto("<< /Type /Catalog /Pages 2 0 R >>"));
        objetos.add(texto("<< /Type /Pages /Kids [3 0 R] /Count 1 >>"));
        objetos.add(texto("<< /Type /Page /Parent 2 0 R /MediaBox [0 0 612 792] "
                + "/Resources << /Font << /F1 4 0 R /F2 5 0 R >> >> /Contents 6 0 R >>"));
        objetos.add(texto("<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica /Encoding /WinAnsiEncoding >>"));
        objetos.add(texto("<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica-Bold /Encoding /WinAnsiEncoding >>"));
        ByteArrayOutputStream flujoObjeto = new ByteArrayOutputStream();
        flujoObjeto.writeBytes(texto("<< /Length " + flujo.length + " >>\nstream\n"));
        flujoObjeto.writeBytes(flujo);
        flujoObjeto.writeBytes(texto("\nendstream"));
        objetos.add(flujoObjeto.toByteArray());

        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        salida.writeBytes(texto("%PDF-1.4\n"));
        int[] posiciones = new int[objetos.size()];
        for (int i = 0; i < objetos.size(); i++) {
            posiciones[i] = salida.size();
            salida.writeBytes(texto((i + 1) + " 0 obj\n"));
            salida.writeBytes(objetos.get(i));
            salida.writeBytes(texto("\nendobj\n"));
        }
        int inicioXref = salida.size();
        StringBuilder xref = new StringBuilder("xref\n0 " + (objetos.size() + 1) + "\n0000000000 65535 f \n");
        for (int p : posiciones) {
            xref.append(String.format(Locale.ROOT, "%010d 00000 n \n", p));
        }
        xref.append("trailer\n<< /Size ").append(objetos.size() + 1).append(" /Root 1 0 R >>\n")
                .append("startxref\n").append(inicioXref).append("\n%%EOF\n");
        salida.writeBytes(texto(xref.toString()));
        return salida.toByteArray();
    }

    /** Nombre sugerido para el archivo: ticket-cita-AAAA-MM-DD-xxxxxxxx.pdf */
    public String nombreArchivo(TicketCita t) {
        String dia = t.inicio() == null ? "sin-fecha"
                : t.inicio().toInstant().atZone(ZONA).toLocalDate().toString();
        String corto = t.codigo() == null ? "cita" : t.codigo().replace("-", "");
        return "ticket-cita-" + dia + "-" + corto.substring(0, Math.min(8, corto.length())) + ".pdf";
    }

    // ---------------------------------------------------------------- contenido de la pagina

    private String armarContenido(TicketCita t) {
        StringBuilder c = new StringBuilder();
        // franja azul con el titulo
        c.append("0.114 0.204 0.38 rg 0 712 612 80 re f\n");
        c.append("BT /F2 22 Tf 1 1 1 rg 50 750 Td (").append(esc("GalenoSV")).append(") Tj ET\n");
        c.append("BT /F1 12 Tf 1 1 1 rg 50 728 Td (").append(esc("Ticket de cita médica")).append(") Tj ET\n");

        int y = 680;
        y = campo(c, y, "Fecha", t.inicio() == null ? "—" : FECHA.format(t.inicio().toInstant().atZone(ZONA)));
        y = campo(c, y, "Hora", t.inicio() == null ? "—" : HORA.format(t.inicio().toInstant().atZone(ZONA)));
        y = campo(c, y, "Clínica", vacio(t.clinica()));
        y = campo(c, y, "Paciente", vacio(t.paciente()));
        y = campo(c, y, "Estado", t.fin() == null ? "En curso"
                : "Finalizada el " + FECHA.format(t.fin().toInstant().atZone(ZONA)));

        y -= 10;
        c.append("0.4 0.62 0.8 RG 1 w 50 ").append(y + 12).append(" m 562 ").append(y + 12).append(" l S\n");
        c.append("BT /F2 13 Tf 0.114 0.204 0.38 rg 50 ").append(y - 8).append(" Td (Procedimientos) Tj ET\n");
        y -= 28;
        List<String> procs = t.procedimientos() == null ? List.of() : t.procedimientos();
        if (procs.isEmpty()) {
            y = linea(c, y, "F1", "Sin procedimientos registrados", 50);
        }
        for (String p : procs) {
            for (String parte : cortar("- " + p, ANCHO_LINEA)) {
                y = linea(c, y, "F1", parte, 60);
            }
        }
        if (t.observaciones() != null && !t.observaciones().isBlank()) {
            y -= 14;
            c.append("BT /F2 13 Tf 0.114 0.204 0.38 rg 50 ").append(y).append(" Td (Observaciones) Tj ET\n");
            y -= 20;
            for (String parte : cortar(t.observaciones().trim(), ANCHO_LINEA)) {
                y = linea(c, y, "F1", parte, 50);
            }
        }
        // pie
        c.append("0.4 0.4 0.4 rg BT /F1 9 Tf 50 40 Td (")
                .append(esc("Código de la cita: " + vacio(t.codigo()))).append(") Tj ET\n");
        c.append("BT /F1 9 Tf 50 28 Td (").append(esc("Presente este ticket en recepción. Generado por GalenoSV."))
                .append(") Tj ET\n");
        return c.toString();
    }

    private int campo(StringBuilder c, int y, String etiqueta, String valor) {
        c.append("BT /F2 12 Tf 0.114 0.204 0.38 rg 50 ").append(y).append(" Td (").append(esc(etiqueta + ":")).append(") Tj ET\n");
        c.append("BT /F1 12 Tf 0 0 0 rg 140 ").append(y).append(" Td (").append(esc(valor)).append(") Tj ET\n");
        return y - 22;
    }

    private int linea(StringBuilder c, int y, String fuente, String valor, int x) {
        c.append("BT /").append(fuente).append(" 11 Tf 0 0 0 rg ").append(x).append(' ').append(y)
                .append(" Td (").append(esc(valor)).append(") Tj ET\n");
        return y - 16;
    }

    /** Corta un texto largo en lineas de a lo mas "ancho" caracteres, sin partir palabras. */
    static List<String> cortar(String texto, int ancho) {
        List<String> lineas = new ArrayList<>();
        StringBuilder actual = new StringBuilder();
        for (String palabra : texto.split("\\s+")) {
            while (palabra.length() > ancho) {                 // palabra mas larga que la linea
                if (actual.length() > 0) { lineas.add(actual.toString()); actual.setLength(0); }
                lineas.add(palabra.substring(0, ancho));
                palabra = palabra.substring(ancho);
            }
            if (actual.length() > 0 && actual.length() + 1 + palabra.length() > ancho) {
                lineas.add(actual.toString());
                actual.setLength(0);
            }
            if (actual.length() > 0) actual.append(' ');
            actual.append(palabra);
        }
        if (actual.length() > 0) lineas.add(actual.toString());
        return lineas;
    }

    /** Escapa los caracteres especiales de un texto PDF y cambia lo que WinAnsi no tiene. */
    static String esc(String s) {
        StringBuilder r = new StringBuilder();
        for (char ch : s.toCharArray()) {
            if (ch == '(' || ch == ')' || ch == '\\') {
                r.append('\\').append(ch);
            } else if (ch == '—' || ch == '–') {
                r.append('-');
            } else if (ch == '\n' || ch == '\r' || ch == '\t') {
                r.append(' ');
            } else if (ch > 255) {
                r.append('?');
            } else {
                r.append(ch);
            }
        }
        return r.toString();
    }

    private String vacio(String s) {
        return s == null || s.isBlank() ? "-" : s;
    }

    private byte[] texto(String s) {
        return s.getBytes(StandardCharsets.ISO_8859_1);
    }
}
