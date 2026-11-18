package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.ToDoubleFunction;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.TicketCita.PasoTicket;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.TicketCita.ProcedimientoTicket;

/** Escribe un PDF carta con fuentes estándar y contenido vectorial. */
public class GeneradorTicketPdf {
    static final ZoneId ZONA = TicketCita.ZONA;
    // Anchos AFM de Helvetica en unidades de 1/1000, por código WinAnsi desde 32.
    private static final int[] HELVETICA = {
        278, 278, 355, 556, 556, 889, 667, 191, 333, 333, 389, 584, 278, 584, 278, 278,
        556, 556, 556, 556, 556, 556, 556, 556, 556, 556, 278, 278, 584, 584, 584, 556,
        1010, 667, 667, 722, 722, 667, 611, 778, 722, 278, 500, 667, 556, 833, 722, 778,
        667, 778, 722, 667, 611, 722, 667, 944, 667, 667, 611, 278, 278, 278, 469, 556,
        333, 556, 556, 500, 556, 556, 278, 556, 556, 222, 222, 500, 222, 833, 556, 556,
        556, 556, 333, 500, 278, 556, 500, 722, 500, 500, 500, 334, 260, 334, 584, 350,
        556, 350, 222, 556, 333, 1000, 556, 556, 333, 1000, 667, 333, 1000, 350, 611, 350,
        350, 222, 222, 333, 333, 350, 556, 1000, 333, 1000, 500, 333, 944, 350, 500, 667,
        278, 333, 556, 556, 556, 556, 260, 556, 333, 737, 370, 556, 584, 333, 737, 333,
        400, 584, 333, 333, 333, 556, 537, 278, 333, 333, 365, 556, 834, 834, 834, 611,
        667, 667, 667, 667, 667, 667, 1000, 722, 667, 667, 667, 667, 278, 278, 278, 278,
        722, 722, 778, 778, 778, 778, 778, 584, 778, 722, 722, 722, 722, 667, 667, 611,
        556, 556, 556, 556, 556, 556, 889, 500, 556, 556, 556, 556, 278, 278, 278, 278,
        556, 556, 556, 556, 556, 556, 556, 584, 611, 556, 556, 556, 556, 500, 556, 500,
    };
    private static final int[] HELVETICA_BOLD = {
        278, 333, 474, 556, 556, 889, 722, 238, 333, 333, 389, 584, 278, 584, 278, 278,
        556, 556, 556, 556, 556, 556, 556, 556, 556, 556, 333, 333, 584, 584, 584, 611,
        975, 722, 722, 722, 722, 667, 611, 778, 722, 278, 556, 722, 611, 833, 722, 778,
        667, 778, 722, 667, 611, 722, 667, 944, 667, 667, 611, 333, 278, 333, 584, 556,
        333, 556, 611, 556, 611, 556, 333, 611, 611, 278, 278, 556, 278, 889, 611, 611,
        611, 611, 389, 556, 333, 611, 556, 778, 556, 556, 500, 389, 280, 389, 584, 350,
        556, 350, 278, 556, 500, 1000, 556, 556, 333, 1000, 667, 333, 1000, 350, 611, 350,
        350, 278, 278, 500, 500, 350, 556, 1000, 333, 1000, 556, 333, 944, 350, 500, 667,
        278, 333, 556, 556, 556, 556, 280, 556, 333, 737, 370, 556, 584, 333, 737, 333,
        400, 584, 333, 333, 333, 611, 556, 278, 333, 333, 365, 556, 834, 834, 834, 611,
        722, 722, 722, 722, 722, 722, 1000, 722, 667, 667, 667, 667, 278, 278, 278, 278,
        722, 722, 778, 778, 778, 778, 778, 584, 778, 722, 722, 722, 722, 667, 667, 611,
        556, 556, 556, 556, 556, 556, 889, 556, 556, 556, 556, 556, 278, 278, 278, 278,
        611, 611, 611, 611, 611, 611, 611, 584, 611, 611, 611, 611, 611, 556, 611, 556,
    };

    private static final Locale ES = Locale.of("es", "SV");
    private static final DateTimeFormatter DIA = DateTimeFormatter.ofPattern("EEE dd/MM/yyyy", ES);
    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy", ES);
    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("hh:mm a", ES);
    private static final Charset WIN_ANSI = Charset.forName("windows-1252");
    private static final String PRIMARIO = "0.184 0.435 0.451";
    private static final String SUAVE = "0.914 0.945 0.937";
    private static final String BORDE_SUAVE = "0.682 0.812 0.776";
    private static final String BORDE = "0.863 0.914 0.941";
    private static final String ACENTO = "0.788 0.467 0.357";
    private static final String TINTA = "0.114 0.204 0.380";
    private static final String NORMAL = "0.149 0.208 0.306";
    private static final String ETIQUETA = "0.322 0.486 0.502";
    private static final String PIE = "0.478 0.541 0.600";
    private static final String BLANCO = "1 1 1";
    private static final int IZQUIERDA = 40;
    private static final int DERECHA = 572;
    private static final int TARJETA = DERECHA - IZQUIERDA;
    private static final int INTERIOR = IZQUIERDA + 17;
    private static final int UTIL = TARJETA - 34;
    private static final int LIMITE_INFERIOR = 62;

    private enum Fuente { F1, F2 }

    private enum Estado {
        PROGRAMADA("Programada", "0.969 0.941 0.890", "0.541 0.392 0.125"),
        EN_CURSO("En curso", "0.929 0.953 0.949", "0.247 0.471 0.659"),
        FINALIZADA("Finalizada", "0.914 0.953 0.929", "0.310 0.541 0.420");

        final String texto;
        final String fondo;
        final String tinta;

        Estado(String texto, String fondo, String tinta) {
            this.texto = texto;
            this.fondo = fondo;
            this.tinta = tinta;
        }
    }

    public byte[] generar(TicketCita ticket) {
        if (ticket == null) {
            throw new IllegalArgumentException("El ticket no puede ser nulo");
        }
        List<StringBuilder> paginas = new Diseno(ticket, Instant.now()).crear();
        List<byte[]> objetos = new ArrayList<>();
        objetos.add(texto("<< /Type /Catalog /Pages 2 0 R >>"));
        StringBuilder hijos = new StringBuilder();
        for (int i = 0; i < paginas.size(); i++) {
            hijos.append(5 + 2 * i).append(" 0 R ");
        }
        objetos.add(texto("<< /Type /Pages /Kids [" + hijos + "] /Count " + paginas.size() + " >>"));
        objetos.add(texto("<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica /Encoding /WinAnsiEncoding >>"));
        objetos.add(texto("<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica-Bold /Encoding /WinAnsiEncoding >>"));
        for (int i = 0; i < paginas.size(); i++) {
            objetos.add(texto("<< /Type /Page /Parent 2 0 R /MediaBox [0 0 612 792] "
                    + "/Resources << /Font << /F1 3 0 R /F2 4 0 R >> >> /Contents " + (6 + 2 * i) + " 0 R >>"));
            byte[] flujo = texto(paginas.get(i).toString());
            ByteArrayOutputStream objeto = new ByteArrayOutputStream();
            objeto.writeBytes(texto("<< /Length " + flujo.length + " >>\nstream\n"));
            objeto.writeBytes(flujo);
            objeto.writeBytes(texto("\nendstream"));
            objetos.add(objeto.toByteArray());
        }
        return serializar(objetos);
    }

    public String nombreArchivo(TicketCita ticket) {
        String dia = ticket.inicio() == null ? "sin-fecha"
                : ticket.inicio().toInstant().atZone(ZONA).toLocalDate().toString();
        String codigo = ticket.codigo() == null || ticket.codigo().isBlank() ? "cita" : codigoCorto(ticket);
        return "ticket-cita-" + dia + "-" + codigo.toLowerCase(Locale.ROOT) + ".pdf";
    }

    private static String codigoCorto(TicketCita ticket) {
        String codigo = vacio(ticket.codigo());
        return codigo.substring(0, Math.min(8, codigo.length())).toUpperCase(Locale.ROOT);
    }

    private static Estado estado(TicketCita ticket, Instant ahora) {
        if (ticket.fin() != null) {
            return Estado.FINALIZADA;
        }
        return ticket.inicio() != null && ticket.inicio().toInstant().isAfter(ahora)
                ? Estado.PROGRAMADA : Estado.EN_CURSO;
    }

    @FunctionalInterface
    private interface Dibujo {
        void pintar(StringBuilder pagina, int arriba);
    }

    private record Fila(int alto, Dibujo dibujo) {}
    private record Pastilla(String texto, double x, double ancho, boolean flecha) {}

    private static final class Diseno {
        private final TicketCita ticket;
        private final Instant emitido;
        private final List<StringBuilder> paginas = new ArrayList<>();
        private StringBuilder c;
        private int y;

        Diseno(TicketCita ticket, Instant emitido) {
            this.ticket = ticket;
            this.emitido = emitido;
        }

        List<StringBuilder> crear() {
            pagina(true);
            destacada();

            var paciente = ticket.paciente();
            var nombre = paciente == null ? "-" : vacio(paciente.nombre());
            var edad = paciente == null ? "-" : vacio(paciente.edad());
            var documento = paciente == null ? "-" : vacio(paciente.documento());
            var contacto = paciente == null ? "-" : vacio(paciente.contacto());
            List<Fila> pacienteFilas = new ArrayList<>();
            pacienteFilas.addAll(campos("Nombre", nombre, "Edad", edad));
            pacienteFilas.addAll(campos("Documento", documento, "Contacto", contacto));
            tarjeta("PACIENTE", pacienteFilas, 0);

            var procedimientos = ticket.procedimientos() == null ? List.<ProcedimientoTicket>of() : ticket.procedimientos();
            String titulo = procedimientos.size() > 1 ? "PROCEDIMIENTOS" : "PROCEDIMIENTO";
            if (procedimientos.isEmpty()) {
                tarjeta(titulo, List.of(filaTexto("-", 14, Fuente.F2, 12, TINTA)), 0);
            }
            for (int i = 0; i < procedimientos.size(); i++) {
                procedimiento(i == 0 ? titulo : null, procedimientos.get(i));
            }

            List<Fila> indicaciones = new ArrayList<>();
            String tipo = paciente == null ? "-" : vacio(paciente.tipoDocumento());
            vineta(indicaciones, "Preséntese 15 minutos antes con su " + tipo + ".");
            vineta(indicaciones, ticket.observaciones());
            for (var procedimiento : procedimientos) {
                if (procedimiento.observaciones() != null) {
                    procedimiento.observaciones().forEach(nota -> vineta(indicaciones, nota));
                }
            }
            if (ticket.referenciaExterna() != null && !ticket.referenciaExterna().isBlank()) {
                vineta(indicaciones, "Referencia: " + ticket.referenciaExterna());
            }
            tarjeta("INDICACIONES", indicaciones, 0);
            return paginas;
        }

        private void pagina(boolean primera) {
            c = new StringBuilder();
            paginas.add(c);
            if (primera) {
                rectangulo(c, 0, 696, 612, 96, PRIMARIO);
                redondeado(c, IZQUIERDA, 724, 40, 40, 10, BLANCO, null);
                redondeado(c, 57, 733, 6, 22, 2, PRIMARIO, null);
                redondeado(c, 49, 741, 22, 6, 2, PRIMARIO, null);
                escribir(c, 92, 746, Fuente.F2, 22, BLANCO, "GalenoSV");
                escribir(c, 92, 727, Fuente.F1, 11, BLANCO, "Comprobante de cita médica");
                derecha(c, DERECHA, 752, Fuente.F1, 9, BLANCO, "CÓDIGO DE CITA");
                derecha(c, DERECHA, 733, Fuente.F2, 20, BLANCO, codigoCorto(ticket));
                rectangulo(c, 0, 692, 612, 4, ACENTO);
                y = 670;
            } else {
                rectangulo(c, 0, 754, 612, 38, PRIMARIO);
                escribir(c, IZQUIERDA, 769, Fuente.F2, 12, BLANCO, "GalenoSV · Código " + codigoCorto(ticket));
                y = 735;
            }
            c.append("q [3 2] 0 d\n");
            linea(c, IZQUIERDA, 44, DERECHA, 44, BORDE_SUAVE);
            c.append("Q\n");
            escribir(c, IZQUIERDA, 28, Fuente.F1, 8.5, PIE, "Código completo: " + vacio(ticket.codigo()));
            var local = emitido.atZone(ZONA);
            derecha(c, DERECHA, 28, Fuente.F1, 8.5, PIE,
                    "Emitido: " + FECHA.format(local) + " " + HORA.format(local) + " · GalenoSV");
        }

        private void destacada() {
            var inicio = ticket.inicio() == null ? null : ticket.inicio().toInstant().atZone(ZONA);
            String fecha = inicio == null ? "-" : DIA.format(inicio).replaceFirst("\\.", "");
            fecha = fecha.substring(0, 1).toUpperCase(ES) + fecha.substring(1);
            String hora = inicio == null ? "-" : HORA.format(inicio).replace('\u00a0', ' ');
            int xClinica = 401;
            List<String> clinica = envolver(vacio(ticket.clinica()), Fuente.F2, 16, DERECHA - xClinica - 14);
            // Deja espacio para el resto del comprobante aun con nombres de clínica excepcionalmente largos.
            if (clinica.size() > 3) {
                clinica = new ArrayList<>(clinica.subList(0, 3));
                String ultima = clinica.get(2);
                while (ancho(ultima + "…", Fuente.F2, 16) > DERECHA - xClinica - 14) {
                    ultima = ultima.substring(0, ultima.length() - 1);
                }
                clinica.set(2, ultima.stripTrailing() + "…");
            }
            int alto = 88 + 18 * (clinica.size() - 1);
            redondeado(c, IZQUIERDA, y - alto, TARJETA, alto, 10, SUAVE, BORDE_SUAVE);
            linea(c, 217, y - 15, 217, y - alto + 15, BORDE_SUAVE);
            linea(c, 386, y - 15, 386, y - alto + 15, BORDE_SUAVE);
            dato(63, "FECHA", fecha);
            dato(232, "HORA", hora);
            dato(xClinica, "CLÍNICA", clinica.getFirst());
            for (int i = 1; i < clinica.size(); i++) {
                escribir(c, xClinica, y - 49 - i * 18, Fuente.F2, 16, TINTA, clinica.get(i));
            }
            Estado estado = estado(ticket, emitido);
            pastilla(c, xClinica, y - alto + 15, ancho(estado.texto, Fuente.F2, 10) + 18,
                    estado.texto, estado.fondo, estado.tinta, Fuente.F2);
            y -= alto + 13;
        }

        private void dato(int x, String etiqueta, String valor) {
            escribir(c, x, y - 28, Fuente.F1, 9, ETIQUETA, etiqueta);
            escribir(c, x, y - 49, Fuente.F2, 16, TINTA, valor);
        }

        private List<Fila> campos(String etiqueta1, String valor1, String etiqueta2, String valor2) {
            int mitad = UTIL / 2;
            int sangria = 110;
            var izquierda = envolver(valor1, Fuente.F1, 11, mitad - sangria - 8);
            var derecha = envolver(valor2, Fuente.F1, 11, mitad - sangria);
            var filas = new ArrayList<Fila>();
            for (int i = 0; i < Math.max(izquierda.size(), derecha.size()); i++) {
                boolean inicial = i == 0;
                String a = i < izquierda.size() ? izquierda.get(i) : "";
                String b = i < derecha.size() ? derecha.get(i) : "";
                filas.add(new Fila(19, (p, arriba) -> {
                    if (inicial) {
                        escribir(p, INTERIOR, arriba - 12, Fuente.F2, 11, ETIQUETA, etiqueta1);
                        escribir(p, INTERIOR + mitad, arriba - 12, Fuente.F2, 11, ETIQUETA, etiqueta2);
                    }
                    int base = arriba - 12;
                    escribir(p, INTERIOR + sangria, base, Fuente.F1, 11, NORMAL, a);
                    escribir(p, INTERIOR + mitad + sangria, base, Fuente.F1, 11, NORMAL, b);
                }));
            }
            return filas;
        }

        private void procedimiento(String titulo, ProcedimientoTicket procedimiento) {
            List<Fila> filas = new ArrayList<>();
            envolver(vacio(procedimiento.nombre()), Fuente.F2, 12, UTIL).forEach(
                    parte -> filas.add(filaTexto(parte, 18, Fuente.F2, 12, TINTA)));
            var pasos = procedimiento.pasos();
            if (pasos == null || pasos.isEmpty()) {
                filas.add(filaTexto("-", 20, Fuente.F1, 10, NORMAL));
            } else {
                filas.addAll(filasPasos(pasos));
            }
            var examenes = procedimiento.examenes();
            String previstos = examenes == null || examenes.isEmpty() ? "-" : String.join(", ", examenes);
            String etiqueta = "Exámenes previstos:";
            int sangria = (int) Math.ceil(ancho(etiqueta + " ", Fuente.F2, 10.5));
            var lineas = envolver(previstos, Fuente.F1, 10.5, UTIL - sangria);
            filas.add(new Fila(18, (p, arriba) -> {
                escribir(p, INTERIOR, arriba - 10, Fuente.F2, 10.5, ETIQUETA, etiqueta);
                escribir(p, INTERIOR + sangria, arriba - 10, Fuente.F1, 10.5, NORMAL, lineas.getFirst());
            }));
            lineas.stream().skip(1).forEach(parte -> filas.add(filaTexto(parte, 14, Fuente.F1, 10.5, NORMAL)));
            tarjeta(titulo, filas, 88);
        }

        private List<Fila> filasPasos(List<PasoTicket> pasos) {
            List<Fila> filas = new ArrayList<>();
            List<Pastilla> fila = new ArrayList<>();
            double x = INTERIOR;
            for (int i = 0; i < pasos.size(); i++) {
                PasoTicket paso = pasos.get(i);
                String texto = (i + 1) + ". " + vacio(paso.nombre()) + " · " + vacio(paso.rol());
                int j = 0;
                for (String parte : envolver(texto, Fuente.F1, 10, UTIL - 35)) {
                    boolean flecha = i > 0 && j == 0;
                    double ancho = ancho(parte, Fuente.F1, 10) + 16;
                    if (x + (flecha ? 19 : 0) + ancho > INTERIOR + UTIL) {
                        filas.add(filaPastillas(fila));
                        fila = new ArrayList<>();
                        x = INTERIOR;
                    }
                    if (flecha) {
                        x += 19;
                    }
                    fila.add(new Pastilla(parte, x, ancho, flecha));
                    x += ancho + 2;
                    j++;
                }
            }
            filas.add(filaPastillas(fila));
            return filas;
        }

        private Fila filaPastillas(List<Pastilla> fila) {
            return new Fila(24, (p, arriba) -> {
                for (Pastilla paso : fila) {
                    double x = paso.x();
                    if (paso.flecha()) {
                        flecha(p, x - 16, arriba - 11);
                    }
                    pastilla(p, x, arriba - 20, paso.ancho(), paso.texto(), SUAVE, NORMAL, Fuente.F1);
                }
            });
        }

        private void vineta(List<Fila> filas, String texto) {
            if (texto == null || texto.isBlank()) {
                return;
            }
            boolean primera = true;
            for (String parte : envolver(texto, Fuente.F1, 11, UTIL - 12)) {
                boolean punto = primera;
                filas.add(new Fila(18, (p, arriba) -> {
                    if (punto) {
                        escribir(p, INTERIOR + 2, arriba - 13, Fuente.F1, 11, NORMAL, "•");
                    }
                    escribir(p, INTERIOR + 16, arriba - 13, Fuente.F1, 11, NORMAL, parte);
                }));
                primera = false;
            }
        }

        private void tarjeta(String titulo, List<Fila> filas, int minimo) {
            int indice = 0;
            while (indice < filas.size()) {
                int cabecera = titulo == null ? 0 : 22;
                int altoTotal = 22 + filas.subList(indice, filas.size()).stream().mapToInt(Fila::alto).sum();
                int espacio = y - LIMITE_INFERIOR - cabecera;
                if (espacio < Math.max(minimo, Math.min(altoTotal, 22 + filas.get(indice).alto() * 2))) {
                    pagina(false);
                    espacio = y - LIMITE_INFERIOR - cabecera;
                }
                if (titulo != null) {
                    escribir(c, IZQUIERDA, y - 11, Fuente.F2, 11, PRIMARIO, titulo);
                    y -= cabecera;
                    titulo = null;
                }

                int fin = indice;
                int alto = 22;
                while (fin < filas.size() && alto + filas.get(fin).alto() <= espacio) {
                    alto += filas.get(fin++).alto();
                }
                // Las filas individuales se ajustan a la página antes de llegar a este punto.
                alto = Math.max(alto, minimo);
                redondeado(c, IZQUIERDA, y - alto, TARJETA, alto, 9, BLANCO, BORDE);
                int arriba = y - 12;
                while (indice < fin) {
                    Fila fila = filas.get(indice++);
                    fila.dibujo().pintar(c, arriba);
                    arriba -= fila.alto();
                }
                y -= alto + 12;
                if (indice < filas.size()) {
                    pagina(false);
                }
            }
        }
    }

    private static byte[] serializar(List<byte[]> objetos) {
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

    private static Fila filaTexto(String texto, int alto, Fuente fuente, double puntos, String color) {
        return new Fila(alto, (p, arriba) -> escribir(p, INTERIOR, arriba - alto + 4, fuente, puntos, color, texto));
    }

    private static void pastilla(StringBuilder c, double x, int y, double ancho, String texto,
                                 String fondo, String tinta, Fuente fuente) {
        boolean estado = fuente == Fuente.F2;
        redondeado(c, x, y, ancho, estado ? 15 : 18, estado ? 7 : 9, fondo, estado ? null : BORDE_SUAVE);
        escribir(c, x + (ancho - ancho(texto, fuente, 10)) / 2, y + (estado ? 4 : 5.5), fuente, 10, tinta, texto);
    }

    // WinAnsi no contiene la flecha. Se dibuja con el mismo trazo que los separadores.
    private static void flecha(StringBuilder c, double x, double y) {
        c.append(String.format(Locale.ROOT, "%s RG 0.8 w %.3f %.3f m %.3f %.3f l "
                + "%.3f %.3f m %.3f %.3f l %.3f %.3f l S%n",
                ETIQUETA, x, y, x + 11, y, x + 7, y + 3, x + 11, y, x + 7, y - 3));
    }

    private static void escribir(StringBuilder c, double x, double y, Fuente fuente, double puntos,
                                 String color, String valor) {
        c.append(String.format(Locale.ROOT, "BT /%s %s Tf %s rg %.3f %.3f Td (%s) Tj ET%n",
                fuente, puntos, color, x, y, esc(valor)));
    }

    private static void derecha(StringBuilder c, double x, double y, Fuente fuente, double puntos,
                                String color, String valor) {
        escribir(c, x - ancho(valor, fuente, puntos), y, fuente, puntos, color, valor);
    }

    private static void rectangulo(StringBuilder c, int x, int y, int ancho, int alto, String color) {
        c.append(color).append(" rg ").append(x).append(' ').append(y).append(' ')
                .append(ancho).append(' ').append(alto).append(" re f\n");
    }

    private static void linea(StringBuilder c, int x1, int y1, int x2, int y2, String color) {
        c.append(color).append(" RG 0.8 w ").append(x1).append(' ').append(y1).append(" m ")
                .append(x2).append(' ').append(y2).append(" l S\n");
    }

    private static void redondeado(StringBuilder c, double x, double y, double ancho, double alto,
                                   double radio, String fondo, String borde) {
        double derecha = x + ancho;
        double arriba = y + alto;
        double k = radio * (1 - 0.5522847498);
        c.append(fondo).append(" rg\n");
        if (borde != null) {
            c.append(borde).append(" RG 0.8 w\n");
        }
        c.append(String.format(Locale.ROOT,
                "%.3f %.3f m %.3f %.3f l %.3f %.3f %.3f %.3f %.3f %.3f c%n"
                + "%.3f %.3f l %.3f %.3f %.3f %.3f %.3f %.3f c%n"
                + "%.3f %.3f l %.3f %.3f %.3f %.3f %.3f %.3f c%n"
                + "%.3f %.3f l %.3f %.3f %.3f %.3f %.3f %.3f c h %s%n",
                x + radio, y, derecha - radio, y, derecha - k, y, derecha, y + k, derecha, y + radio,
                derecha, arriba - radio, derecha, arriba - k, derecha - k, arriba, derecha - radio, arriba,
                x + radio, arriba, x + k, arriba, x, arriba - k, x, arriba - radio,
                x, y + radio, x, y + k, x + k, y, x + radio, y, borde == null ? "f" : "B"));
    }

    /** Parte entre palabras. Fracciona un término solo si por sí mismo supera el límite. */
    static List<String> cortar(String texto, int ancho) {
        return cortar(texto, ancho, String::length);
    }

    private static List<String> envolver(String texto, Fuente fuente, double puntos, int ancho) {
        var lineas = cortar(texto, ancho, linea -> ancho(linea, fuente, puntos));
        return lineas.isEmpty() ? List.of("-") : lineas;
    }

    private static List<String> cortar(String texto, double limite, ToDoubleFunction<String> medir) {
        if (limite <= 0) {
            throw new IllegalArgumentException("El ancho debe ser positivo");
        }
        List<String> lineas = new ArrayList<>();
        String actual = "";
        for (String palabra : texto.strip().split("(?U)\\s+")) {
            String tentativa = actual.isEmpty() ? palabra : actual + " " + palabra;
            if (medir.applyAsDouble(tentativa) <= limite) {
                actual = tentativa;
                continue;
            }
            if (!actual.isEmpty()) {
                lineas.add(actual);
            }
            int inicio = 0;
            int fin = 0;
            while (fin < palabra.length()) {
                int siguiente = fin + Character.charCount(palabra.codePointAt(fin));
                if (fin > inicio && medir.applyAsDouble(palabra.substring(inicio, siguiente)) > limite) {
                    lineas.add(palabra.substring(inicio, fin));
                    inicio = fin;
                }
                fin = siguiente;
            }
            actual = palabra.substring(inicio);
        }
        if (!actual.isEmpty()) {
            lineas.add(actual);
        }
        return lineas;
    }

    private static String normalizar(String valor) {
        return valor.replaceAll("[\\p{Cc}]", " ");
    }

    /** Escapa los delimitadores de literales PDF y conserva los caracteres de WinAnsi. */
    static String esc(String valor) {
        return normalizar(valor).replace("\\", "\\\\").replace("(", "\\(").replace(")", "\\)");
    }

    private static double ancho(String texto, Fuente fuente, double puntos) {
        int[] anchos = fuente == Fuente.F2 ? HELVETICA_BOLD : HELVETICA;
        int total = 0;
        for (byte caracter : normalizar(texto).getBytes(WIN_ANSI)) {
            int codigo = Byte.toUnsignedInt(caracter);
            total += anchos[codigo - 32];
        }
        return total * puntos / 1000;
    }

    private static String vacio(String valor) {
        return valor == null || valor.isBlank() ? "-" : valor.strip();
    }

    private static byte[] texto(String valor) {
        return valor.getBytes(WIN_ANSI);
    }
}
