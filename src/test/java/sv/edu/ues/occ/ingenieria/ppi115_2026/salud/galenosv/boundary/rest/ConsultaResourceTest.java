package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary.rest;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.RuntimeDelegate;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ConsultaDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ConsultaProcedimientoDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DocumentoDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.MedioContactoDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Consulta;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Persona;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.PersonaRol;

class ConsultaResourceTest {
    private final UUID id = UUID.randomUUID();
    private final ConsultaResource recurso = new ConsultaResource();
    private final RuntimeDelegate delegate = mock(RuntimeDelegate.class);
    private final Response.ResponseBuilder builder = mock(Response.ResponseBuilder.class, RETURNS_SELF);
    private final Response respuesta = mock(Response.class);

    ConsultaResourceTest() {
        recurso.consultaDAO = mock(ConsultaDAO.class);
        recurso.consultaProcedimientoDAO = mock(ConsultaProcedimientoDAO.class);
        recurso.documentoDAO = mock(DocumentoDAO.class);
        recurso.medioContactoDAO = mock(MedioContactoDAO.class);
        when(delegate.createResponseBuilder()).thenReturn(builder);
        when(builder.build()).thenReturn(respuesta);
    }

    @Test
    void devuelve404SiLaConsultaNoExiste() {
        try (var runtime = mockStatic(RuntimeDelegate.class)) {
            runtime.when(RuntimeDelegate::getInstance).thenReturn(delegate);
            assertSame(respuesta, recurso.ticket(id));
            verify(builder).status((Response.StatusType) Response.Status.NOT_FOUND);
            verifyNoInteractions(recurso.consultaProcedimientoDAO);
        }
    }

    @Test
    void devuelvePdfAdjuntoInclusoSiFallaLaCargaDeUnDatoOpcional() {
        var persona = new Persona();
        persona.setIdPersona(UUID.randomUUID());
        var rol = new PersonaRol();
        rol.setIdPersona(persona);
        var consulta = new Consulta();
        consulta.setIdConsulta(id);
        consulta.setIdPersonaRol(rol);
        when(recurso.consultaDAO.buscarParaTicket(id)).thenReturn(consulta);
        when(recurso.documentoDAO.buscarPorPersona(persona.getIdPersona())).thenThrow(new IllegalStateException());
        when(recurso.medioContactoDAO.buscarPorPersona(persona)).thenReturn(null);
        when(recurso.consultaProcedimientoDAO.buscarPorConsulta(id)).thenReturn(List.of());
        try (var runtime = mockStatic(RuntimeDelegate.class)) {
            runtime.when(RuntimeDelegate::getInstance).thenReturn(delegate);
            assertSame(respuesta, recurso.ticket(id));
            verify(builder).status((Response.StatusType) Response.Status.OK);
            verify(builder).type("application/pdf");
            verify(builder).header(eq("Content-Disposition"), startsWith("attachment; filename=\"ticket-cita-"));
            verify(builder).entity(argThat(valor -> valor instanceof byte[] pdf && pdf.length > 8
                    && new String(pdf, 0, 8, java.nio.charset.StandardCharsets.US_ASCII).equals("%PDF-1.4")));
        }
    }

    @Test
    void armarTicketUsaGuionesSiNoHayDocumentoNiContacto() {
        var consulta = new Consulta();
        var persona = new Persona();
        persona.setNombres("Ana");
        var ticket = ConsultaResource.armarTicket(consulta, null, persona, null, null, List.of());
        assertEquals("Ana", ticket.paciente().nombre());
        assertEquals("-", ticket.paciente().documento());
        assertEquals("-", ticket.paciente().contacto());
    }
}
