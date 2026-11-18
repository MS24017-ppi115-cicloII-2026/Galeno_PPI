package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import java.util.Date;
import java.util.List;

/**
 * Datos que lleva el ticket PDF de una cita (consulta). Es un objeto simple, sin JPA,
 * para que el generador del PDF no dependa de la base de datos.
 *
 * @param codigo         identificador de la consulta (UUID en texto)
 * @param inicio         fecha y hora de inicio de la consulta
 * @param fin            fecha y hora de fin (null si todavia esta en curso)
 * @param clinica        nombre de la clinica donde se atiende
 * @param paciente       nombre completo del paciente
 * @param procedimientos detalle de los procedimientos que se siguen en la cita
 * @param observaciones  observaciones de la consulta (puede ser null)
 * @param examenes       órdenes de examen vinculadas a los procedimientos de la cita
 */
public record TicketCita(String codigo, Date inicio, Date fin, String clinica, String paciente,
                         List<String> procedimientos, String observaciones, List<String> examenes) {

    public TicketCita(String codigo, Date inicio, Date fin, String clinica, String paciente,
                      List<String> procedimientos, String observaciones) {
        this(codigo, inicio, fin, clinica, paciente, procedimientos, observaciones, List.of());
    }
}
