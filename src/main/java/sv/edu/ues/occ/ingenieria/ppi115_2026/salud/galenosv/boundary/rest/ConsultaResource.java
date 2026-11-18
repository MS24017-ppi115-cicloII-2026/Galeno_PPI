package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import java.time.LocalDate;
import java.time.Period;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ConsultaDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ConsultaProcedimientoDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DAOInterface;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.GeneradorTicketPdf;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ProcedimientoDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.TicketCita;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Consulta;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ConsultaProcedimiento;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Procedimiento;

import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DocumentoDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.MedioContactoDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.OrdenPasos;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ProcedimientoPasoDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ProcedimientoPasoSecuenciaDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ProcedimientoPasoExamenDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.TicketCita.PacienteTicket;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.TicketCita.PasoTicket;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.TicketCita.ProcedimientoTicket;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Documento;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.MedioContacto;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Persona;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.PersonaRol;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ProcedimientoPaso;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ProcedimientoPasoSecuencia;

@Path("consulta")
public class ConsultaResource extends DefaultResource<Consulta> implements Serializable {

    private static final Logger LOG = Logger.getLogger(ConsultaResource.class.getName());

    @Inject
    ConsultaDAO consultaDAO;

    @Inject
    ConsultaProcedimientoDAO consultaProcedimientoDAO;

    @Inject
    ProcedimientoDAO procedimientoDAO;

    @Inject
    DocumentoDAO documentoDAO;

    @Inject
    MedioContactoDAO medioContactoDAO;

    @Inject
    ProcedimientoPasoDAO procedimientoPasoDAO;

    @Inject
    ProcedimientoPasoSecuenciaDAO procedimientoPasoSecuenciaDAO;

    @Inject
    ProcedimientoPasoExamenDAO procedimientoPasoExamenDAO;

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
        Consulta consulta;
        try {
            consulta = consultaDAO.buscarParaTicket(id);
        } catch (RuntimeException ex) {
            LOG.log(Level.FINE, "No se pudo cargar la persona y la clínica del ticket", ex);
            consulta = consultaDAO.buscar(id);
        }
        if (consulta == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        PersonaRol personaRol = opcional(consulta::getIdPersonaRol);
        Persona persona = personaRol == null ? null : opcional(personaRol::getIdPersona);
        UUID idPersona = persona == null ? null : opcional(persona::getIdPersona);
        Documento documento = idPersona == null ? null
                : primero(opcional(() -> documentoDAO.buscarPorPersona(idPersona)));
        MedioContacto contacto = idPersona == null ? null
                : primero(opcional(() -> medioContactoDAO.buscarPorPersona(persona)));
        List<ProcedimientoTicket> procedimientos = lista(opcional(() -> consultaProcedimientoDAO.buscarPorConsulta(id)))
                .stream().map(this::cargarProcedimiento).toList();
        TicketCita ticket = armarTicket(consulta, personaRol, persona, documento, contacto, procedimientos);
        GeneradorTicketPdf generador = new GeneradorTicketPdf();
        return Response.ok(generador.generar(ticket), "application/pdf")
                .header("Content-Disposition", "attachment; filename=\"" + generador.nombreArchivo(ticket) + "\"")
                .build();
    }

    static TicketCita armarTicket(Consulta consulta, PersonaRol personaRol, Persona persona, Documento documento,
                                  MedioContacto contacto, List<ProcedimientoTicket> procedimientos) {
        String clinica = personaRol == null ? null : opcional(() -> {
            var entidad = personaRol.getIdClinica();
            return entidad == null ? null : entidad.getNombre();
        });
        String nombre = persona == null ? "" : String.join(" ",
                Objects.toString(opcional(persona::getNombres), ""),
                Objects.toString(opcional(persona::getApellidos), "")).trim();
        String edad = persona == null ? null : opcional(() -> {
            var nacimiento = persona.getFechaNacimiento();
            if (nacimiento == null) {
                return null;
            }
            var fecha = nacimiento.toInstant().atZone(TicketCita.ZONA).toLocalDate();
            var hoy = LocalDate.now(TicketCita.ZONA);
            return fecha.isAfter(hoy) ? null : Period.between(fecha, hoy).getYears() + " años";
        });
        String tipo = documento == null ? null : opcional(() -> {
            var entidad = documento.getIdTipoDocumento();
            return entidad == null ? null : entidad.getNombre();
        });
        String valor = documento == null ? null : opcional(documento::getValor);
        String identificacion = valor == null || valor.isBlank() ? "-"
                : tipo == null || tipo.isBlank() ? valor : tipo + " " + valor;
        var paciente = new PacienteTicket(texto(nombre), texto(edad), texto(tipo), identificacion,
                texto(contacto == null ? null : opcional(contacto::getValor)));
        UUID codigo = consulta.getIdConsulta();
        return new TicketCita(codigo == null ? null : codigo.toString(), consulta.getFechaInicio(),
                consulta.getFechaFin(), texto(clinica), paciente, lista(procedimientos),
                opcional(consulta::getObservaciones), opcional(consulta::getReferenciaExterna));
    }

    private ProcedimientoTicket cargarProcedimiento(ConsultaProcedimiento seguido) {
        UUID id = opcional(seguido::getIdProcedimiento);
        Procedimiento procedimiento = id == null ? null : opcional(() -> procedimientoDAO.buscar(id));
        var pasos = id == null ? List.<ProcedimientoPaso>of()
                : lista(opcional(() -> procedimientoPasoDAO.buscarPorProcedimiento(id)));
        var secuencias = new ArrayList<ProcedimientoPasoSecuencia>();
        for (var paso : pasos) {
            UUID idPaso = opcional(paso::getIdProcedimientoPaso);
            if (idPaso != null) {
                secuencias.addAll(lista(opcional(() -> procedimientoPasoSecuenciaDAO.buscarPorProcedimientoPaso(idPaso))));
            }
        }
        var orden = opcional(() -> OrdenPasos.ordenar(pasos, secuencias));
        var pasosTicket = new ArrayList<PasoTicket>();
        var examenes = new LinkedHashSet<String>();
        for (var paso : orden == null ? pasos : orden) {
            String rol = opcional(() -> {
                var entidad = paso.getIdRol();
                return entidad == null ? null : entidad.getNombre();
            });
            pasosTicket.add(new PasoTicket(texto(opcional(paso::getNombre)), texto(rol)));
            UUID idPaso = opcional(paso::getIdProcedimientoPaso);
            if (idPaso != null) {
                for (var previsto : lista(opcional(() -> procedimientoPasoExamenDAO.buscarPorProcedimientoPaso(idPaso)))) {
                    String nombre = opcional(() -> {
                        if (!Boolean.TRUE.equals(previsto.getActivo())) {
                            return null;
                        }
                        var examen = previsto.getIdExamen();
                        return texto(examen == null ? null : examen.getNombre());
                    });
                    if (nombre != null) {
                        examenes.add(nombre);
                    }
                }
            }
        }
        var observaciones = new LinkedHashSet<String>();
        String notas = opcional(seguido::getObservaciones);
        if (notas != null && !notas.isBlank()) {
            observaciones.add(notas);
        }
        notas = procedimiento == null ? null : opcional(procedimiento::getObservaciones);
        if (notas != null && !notas.isBlank()) {
            observaciones.add(notas);
        }
        return new ProcedimientoTicket(texto(procedimiento == null ? null : opcional(procedimiento::getNombre)),
                pasosTicket, List.copyOf(examenes), List.copyOf(observaciones));
    }

    private static <T> T opcional(Supplier<T> carga) {
        try {
            return carga.get();
        } catch (RuntimeException ex) {
            LOG.log(Level.FINE, "No se pudo leer un dato opcional del ticket", ex);
            return null;
        }
    }

    private static <T> List<T> lista(List<T> datos) {
        return datos == null ? List.of() : datos.stream().filter(Objects::nonNull).toList();
    }

    private static <T> T primero(List<T> datos) {
        return lista(datos).stream().findFirst().orElse(null);
    }

    private static String texto(String valor) {
        return valor == null || valor.isBlank() ? "-" : valor;
    }
}
