package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import org.primefaces.event.SelectEvent;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ConsultaDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DAOInterface;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DocumentoDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Consulta;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Documento;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.MedioContacto;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.PersonaRol;

@Named
@ViewScoped
public class ConsultaModels extends AbstractModel<Consulta> {

    @Inject
    ConsultaDAO consultaDAO;

    @Inject
    SesionModels sesionModels;

    @Inject
    DocumentoDAO documentoDAO;

    private Date fechaDesde;
    private Date fechaHasta;

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    @Override
    protected DAOInterface<Consulta> getDAO() {
        return consultaDAO;
    }

    private static final String ROL_PACIENTE = "paciente";

    private List<PersonaRol> pacientes;

    public List<PersonaRol> getPacientes() {
        if (pacientes == null) {
            pacientes = consultaDAO.buscarPersonasPorNombreRol(ROL_PACIENTE);
        }
        UUID idClinica = (sesionModels != null) ? sesionModels.getIdClinicaActual() : null;
        if (idClinica == null) {
            return pacientes;
        }
        return pacientes.stream()
                .filter(p -> p.getIdClinica() != null
                        && idClinica.equals(p.getIdClinica().getIdClinica()))
                .toList();
    }

    @Override
    public List<Consulta> getregistros() {

        List<Consulta> base = super.getregistros();
        if (base == null) {
            return List.of();
        }

        UUID idClinica = (sesionModels != null) ? sesionModels.getIdClinicaActual() : null;
        Date hasta = (fechaHasta != null) ? finDelDia(fechaHasta) : null;

        return base.stream()
                .filter(c -> idClinica == null
                        || (c.getIdPersonaRol() != null
                        && c.getIdPersonaRol().getIdClinica() != null
                        && idClinica.equals(c.getIdPersonaRol().getIdClinica().getIdClinica())))
                .filter(c -> fechaDesde == null
                        || (c.getFechaInicio() != null && !c.getFechaInicio().before(fechaDesde)))
                .filter(c -> hasta == null
                        || (c.getFechaInicio() != null && !c.getFechaInicio().after(hasta)))
                .toList();
    }

    private static Date finDelDia(Date fecha) {
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(fecha);
        calendario.set(Calendar.HOUR_OF_DAY, 23);
        calendario.set(Calendar.MINUTE, 59);
        calendario.set(Calendar.SECOND, 59);
        calendario.set(Calendar.MILLISECOND, 999);
        return calendario.getTime();
    }

    @Override
    protected Consulta crearRegistroNuevo() {
        Consulta consulta = new Consulta(UUID.randomUUID());
        consulta.setFechaInicio(new Date());
        return consulta;
    }

    @Override
    protected UUID obtenerId(Consulta registro) {
        return registro.getIdConsulta();
    }

    private boolean validarRegistro() {
        if (registro == null) {
            return mostrarError(
                    "Registro inválido",
                    "La consulta no puede ser nula."
            );
        }

        if (registro.getIdPersonaRol() == null) {
            return mostrarError(
                    "Persona requerida",
                    "Debe seleccionar la persona de la consulta."
            );
        }

        UUID idSeleccionado = registro.getIdPersonaRol().getIdPersonaRol();
        boolean esPaciente = getPacientes().stream()
                .anyMatch(p -> Objects.equals(p.getIdPersonaRol(), idSeleccionado));

        if (!esPaciente) {
            return mostrarError(
                    "Paciente inválido",
                    "La persona seleccionada no tiene el rol de paciente."
            );
        }

        if (registro.getFechaInicio() == null) {
            return mostrarError(
                    "Fecha requerida",
                    "La consulta debe tener una fecha de inicio."
            );
        }
        if (registro.getFechaFin() != null
                && registro.getFechaFin().before(registro.getFechaInicio())) {
            return mostrarError(
                    "Fechas inválidas",
                    "La fecha de fin no puede ser anterior a la fecha de inicio."
            );
        }

        return true;
    }

    private boolean mostrarError(String resumen, String detalle) {
        fc.addMessage(
                null,
                new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        resumen,
                        detalle
                )
        );

        fc.validationFailed();
        return false;
    }

    @Override
    public void btnCrearhandler(ActionEvent ae) {
        if (validarRegistro()) {
            super.btnCrearhandler(ae);
        }
    }

    @Override
    public void btnModificarHandler() {
        if (validarRegistro()) {
            super.btnModificarHandler();
        }
    }

    // ===================== Sesión requerida y buscador =====================

    private String criterioBusqueda;
    private List<PersonaRol> resultadosBusqueda = List.of();
    private PersonaRol personaBusqueda;
    private boolean pacienteBloqueado;

    public String getCriterioBusqueda() {
        return criterioBusqueda;
    }

    public void setCriterioBusqueda(String criterioBusqueda) {
        this.criterioBusqueda = criterioBusqueda;
    }

    public List<PersonaRol> getResultadosBusqueda() {
        return resultadosBusqueda;
    }

    public PersonaRol getPersonaBusqueda() {
        return personaBusqueda;
    }

    public void setPersonaBusqueda(PersonaRol personaBusqueda) {
        this.personaBusqueda = personaBusqueda;
    }

    public boolean isPacienteBloqueado() {
        return pacienteBloqueado;
    }

    private String texto(String clave) {
        return (String) fc.getApplication()
                .evaluateExpressionGet(fc, "#{msg['" + clave + "']}", String.class);
    }

    private boolean exigirSesion() {
        if (sesionModels != null && sesionModels.isSesionIniciada()) {
            return true;
        }
        fc.addMessage(null, new FacesMessage(
                FacesMessage.SEVERITY_WARN,
                texto("consulta.sesion_titulo"),
                texto("consulta.msg_sesion_no_iniciada")
        ));
        fc.validationFailed();
        return false;
    }

    public void nuevaConsulta(ActionEvent ae) {
        if (!exigirSesion()) {
            return;
        }
        this.pacienteBloqueado = false;
        super.btnNuevoHandler(ae);
    }

    @Override
    public void btnCancelar() {
        super.btnCancelar();
        this.pacienteBloqueado = false;
    }

    public void abrirBuscador(ActionEvent ae) {
        if (!exigirSesion()) {
            return;
        }
        this.criterioBusqueda = null;
        this.resultadosBusqueda = List.of();
        this.personaBusqueda = null;
    }

    public void buscarPaciente() {
        if (!exigirSesion()) {
            return;
        }
        UUID idClinica = (sesionModels != null) ? sesionModels.getIdClinicaActual() : null;

        this.personaBusqueda = null;
        this.resultadosBusqueda = consultaDAO.buscarPacientePorCriterio(
                criterioBusqueda,
                idClinica
        );

        if (resultadosBusqueda.isEmpty()) {
            fc.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_INFO,
                    texto("consulta.buscador_titulo"),
                    texto("consulta.buscador_vacio")
            ));
        }
    }

    public void seleccionarPaciente(PersonaRol pr) {
        if (!exigirSesion()) {
            return;
        }
        if (pr == null) {
            return;
        }
        super.btnNuevoHandler(null);
        if (this.registro != null) {
            this.registro.setIdPersonaRol(pr);
        }
        this.personaBusqueda = pr;
        this.pacienteBloqueado = true;
    }

    public void seleccionarDesdeBusqueda(AjaxBehaviorEvent event) {
        if (!(event instanceof SelectEvent)) {
            return;
        }
        Object fila = ((SelectEvent<?>) event).getObject();
        if (fila instanceof PersonaRol) {
            seleccionarPaciente((PersonaRol) fila);
        }
    }

    public String getDocumentoDe(PersonaRol pr) {
        if (pr == null || pr.getIdPersona() == null) {
            return "";
        }
        Collection<Documento> documentos = pr.getIdPersona().getDocumentoCollection();
        if (documentos == null || documentos.isEmpty()) {
            return "";
        }
        return documentos.stream()
                .map(Documento::getValor)
                .filter(v -> v != null && !v.isBlank())
                .collect(Collectors.joining(", "));
    }

    public String getTelefonoDe(PersonaRol pr) {
        if (pr == null || pr.getIdPersona() == null) {
            return "";
        }
        Collection<MedioContacto> medios = pr.getIdPersona().getMedioContactoCollection();
        if (medios == null || medios.isEmpty()) {
            return "";
        }
        List<String> telefonos = medios.stream()
                .filter(this::esTelefono)
                .map(MedioContacto::getValor)
                .filter(v -> v != null && !v.isBlank())
                .toList();

        List<String> aMostrar = telefonos.isEmpty()
                ? medios.stream()
                        .map(MedioContacto::getValor)
                        .filter(v -> v != null && !v.isBlank())
                        .toList()
                : telefonos;

        return String.join(", ", aMostrar);
    }

    private boolean esTelefono(MedioContacto medio) {
        if (medio == null || medio.getIdTipoMedioContacto() == null
                || medio.getIdTipoMedioContacto().getNombre() == null) {
            return false;
        }
        String tipo = medio.getIdTipoMedioContacto().getNombre().toLowerCase();
        return tipo.contains("tel")
                || tipo.contains("cel")
                || tipo.contains("fono")
                || tipo.contains("phone")
                || tipo.contains("vil")
                || tipo.contains("fij");
    }

    // ===================== Información extra =====================
    // ===================== Documentos del paciente =====================

    private List<Documento> documentosPaciente = List.of();
    private UUID idPersonaDocumentos;

    public Consulta getConsultaSeleccionada() {
        if (registro == null || registro.getIdConsulta() == null) {
            return null;
        }
        if (estado != Estado_CRUD.MODIFICAR) {
            return null;
        }
        return registro;
    }

    public boolean isHayConsultaSeleccionada() {
        return getConsultaSeleccionada() != null;
    }

    private UUID idPacienteDeLaConsulta() {
        Consulta consulta = getConsultaSeleccionada();
        if (consulta == null
                || consulta.getIdPersonaRol() == null
                || consulta.getIdPersonaRol().getIdPersona() == null) {
            return null;
        }
        return consulta.getIdPersonaRol().getIdPersona().getIdPersona();
    }

    public List<Documento> getDocumentosDelPaciente() {
        UUID idPaciente = idPacienteDeLaConsulta();
        if (!Objects.equals(idPaciente, idPersonaDocumentos)) {
            idPersonaDocumentos = idPaciente;
            documentosPaciente = (idPaciente == null)
                    ? List.of()
                    : documentoDAO.buscarPorPersona(idPaciente);
        }
        return documentosPaciente;
    }
}
