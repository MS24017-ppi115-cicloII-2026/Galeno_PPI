package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/** Datos del comprobante, independientes de JPA. */
public record TicketCita(String codigo, Date inicio, Date fin, String clinica, PacienteTicket paciente,
                         List<ProcedimientoTicket> procedimientos, String observaciones, String referenciaExterna) {
    public static final ZoneId ZONA = ZoneId.of("America/El_Salvador");

    public record PacienteTicket(String nombre, String edad, String tipoDocumento, String documento, String contacto) {}
    public record ProcedimientoTicket(String nombre, List<PasoTicket> pasos, List<String> examenes,
                                      List<String> observaciones) {}
    public record PasoTicket(String nombre, String rol) {}
}
