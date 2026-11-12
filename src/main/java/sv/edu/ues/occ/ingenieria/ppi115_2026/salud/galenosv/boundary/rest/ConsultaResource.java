package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ConsultaDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ConsultaProcedimientoDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DAOInterface;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.GeneradorTicketPdf;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ProcedimientoDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.TicketCita;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Consulta;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ConsultaProcedimiento;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Procedimiento;

@Path("consulta")
public class ConsultaResource extends DefaultResource<Consulta> implements Serializable {

    @Inject
    ConsultaDAO consultaDAO;

    @Inject
    ConsultaProcedimientoDAO consultaProcedimientoDAO;

    @Inject
    ProcedimientoDAO procedimientoDAO;

    @Override
    protected DAOInterface<Consulta> getDAO() {
        return consultaDAO;
    }

    @Override
    protected UUID obtenerId(Consulta registro) {
        return registro.getIdConsulta();
    }

    @Override
    protected void asignarId(Consulta registro, UUID id) {
        registro.setIdConsulta(id);
    }

    // GET .../consulta/{id}/ticket  -> PDF con el resumen de la cita (HU1: "ticket generado desde el backend").
    // 404 si la consulta no existe. El nombre sugerido del archivo viaja en Content-Disposition.
    @GET
    @Path("{id}/ticket")
    @Produces("application/pdf")
    public Response ticket(@PathParam("id") UUID id) {
        Consulta consulta = consultaDAO.buscar(id);
        if (consulta == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        List<ConsultaProcedimiento> seguidos = consultaProcedimientoDAO.buscarPorConsulta(id);
        TicketCita ticket = armarTicket(consulta, seguidos, this::nombreProcedimiento);
        GeneradorTicketPdf generador = new GeneradorTicketPdf();
        return Response.ok(generador.generar(ticket))
                .header("Content-Disposition", "attachment; filename=\"" + generador.nombreArchivo(ticket) + "\"")
                .build();
    }

    // Convierte las entidades en el objeto simple que usa el generador del PDF.
    // nombreDe resuelve el UUID de un procedimiento a su nombre (ConsultaProcedimiento guarda solo el UUID).
    static TicketCita armarTicket(Consulta consulta, List<ConsultaProcedimiento> seguidos,
                                  Function<UUID, String> nombreDe) {
        String clinica = null;
        String paciente = null;
        if (consulta.getIdPersonaRol() != null) {
            if (consulta.getIdPersonaRol().getIdClinica() != null) {
                clinica = consulta.getIdPersonaRol().getIdClinica().getNombre();
            }
            if (consulta.getIdPersonaRol().getIdPersona() != null) {
                paciente = (nulo(consulta.getIdPersonaRol().getIdPersona().getNombres()) + " "
                        + nulo(consulta.getIdPersonaRol().getIdPersona().getApellidos())).trim();
            }
        }
        List<String> procedimientos = new ArrayList<>();
        for (ConsultaProcedimiento cp : seguidos) {
            String nombre = cp.getIdProcedimiento() == null ? null : nombreDe.apply(cp.getIdProcedimiento());
            procedimientos.add(nombre == null ? "Procedimiento sin nombre" : nombre);
        }
        return new TicketCita(consulta.getIdConsulta() == null ? null : consulta.getIdConsulta().toString(),
                consulta.getFechaInicio(), consulta.getFechaFin(), clinica, paciente, procedimientos,
                consulta.getObservaciones());
    }

    private String nombreProcedimiento(UUID idProcedimiento) {
        Procedimiento p = procedimientoDAO.buscar(idProcedimiento);
        return p == null ? null : p.getNombre();
    }

    private static String nulo(String s) {
        return s == null ? "" : s;
    }
}