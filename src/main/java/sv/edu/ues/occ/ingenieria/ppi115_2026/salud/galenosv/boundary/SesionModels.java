package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.PersonaRolDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ClinicaDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.RolDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Clinica;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.PersonaRol;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Rol;

@Named("sesionModels")
@SessionScoped
public class SesionModels implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String ROL_PACIENTE = "paciente";

    @Inject
    PersonaRolDAO personaRolDAO;

    @Inject
    RolDAO rolDAO;

    @Inject
    ClinicaDAO clinicaDAO;

    private UUID idPersonaRolActual;
    private UUID idClinicaActual;
    private String nombrePersona;
    private String nombreRol;
    private String nombreClinica;

    private Clinica clinicaSeleccionada;
    private PersonaRol personaRolSeleccionado;

    
    public List<PersonaRol> getPersonasParaIniciarSesion() {

        List<PersonaRol> lista = new ArrayList<>();

        for (Rol rol : rolDAO.findRange(0, 1000)) {
            if (rol.getNombre() == null
                    || rol.getNombre().trim().equalsIgnoreCase(ROL_PACIENTE)) {
                continue;
            }
            lista.addAll(personaRolDAO.buscarPorRol(rol));
        }

        lista.sort(Comparator.comparing(this::nombreCompleto,
                Comparator.nullsFirst(String.CASE_INSENSITIVE_ORDER)));

        return lista;
    }

    private String nombreCompleto(PersonaRol personaRol) {
        if (personaRol == null || personaRol.getIdPersona() == null) {
            return null;
        }
        return personaRol.getIdPersona().getNombres()
                + " " + personaRol.getIdPersona().getApellidos();
    }

    public void iniciarSesion(PersonaRol seleccionado) {

        if (seleccionado == null || seleccionado.getIdPersonaRol() == null) {
            return;
        }

        PersonaRol pr = personaRolDAO.buscarConRelaciones(seleccionado.getIdPersonaRol());
        if (pr == null) {
            return;
        }

        this.idPersonaRolActual = pr.getIdPersonaRol();
        this.nombrePersona = pr.getIdPersona().getNombres() + " " + pr.getIdPersona().getApellidos();
        this.nombreRol = (pr.getIdRol() != null) ? pr.getIdRol().getNombre() : null;
        this.nombreClinica = (pr.getIdClinica() != null) ? pr.getIdClinica().getNombre() : null;
        this.idClinicaActual = (pr.getIdClinica() != null) ? pr.getIdClinica().getIdClinica() : null;
    }

    public void cerrarSesion() {
        this.idPersonaRolActual = null;
        this.idClinicaActual = null;
        this.nombrePersona = null;
        this.nombreRol = null;
        this.nombreClinica = null;
        this.clinicaSeleccionada = null;
        this.personaRolSeleccionado = null;
    }

    public boolean isSesionIniciada() {
        return idPersonaRolActual != null;
    }

    
    public List<Clinica> getClinicas() {
        return clinicaDAO.buscarPorActivo(Boolean.TRUE);
    }

    
    public List<PersonaRol> getPersonasRolesPorClinica() {
        if (clinicaSeleccionada == null || clinicaSeleccionada.getIdClinica() == null) {
            return List.of();
        }
        Clinica clinica = clinicaDAO.buscar(clinicaSeleccionada.getIdClinica());
        if (clinica == null) {
            return List.of();
        }
        List<PersonaRol> lista = new ArrayList<>();
        for (PersonaRol pr : personaRolDAO.buscarPorClinica(clinica)) {
            if (pr.getIdRol() == null || pr.getIdRol().getNombre() == null
                    || pr.getIdRol().getNombre().trim().equalsIgnoreCase(ROL_PACIENTE)) {
                continue;
            }
            lista.add(pr);
        }
        lista.sort(Comparator.comparing(this::nombreCompleto,
                Comparator.nullsFirst(String.CASE_INSENSITIVE_ORDER)));
        return lista;
    }

    
    public void aplicarRol() {
        iniciarSesion(personaRolSeleccionado);
    }

    public Clinica getClinicaSeleccionada() {
        return clinicaSeleccionada;
    }

    public void setClinicaSeleccionada(Clinica clinicaSeleccionada) {
        this.clinicaSeleccionada = clinicaSeleccionada;
        this.personaRolSeleccionado = null;
    }

    public PersonaRol getPersonaRolSeleccionado() {
        return personaRolSeleccionado;
    }

    public void setPersonaRolSeleccionado(PersonaRol personaRolSeleccionado) {
        this.personaRolSeleccionado = personaRolSeleccionado;
    }

    public UUID getIdPersonaRolActual() {
        return idPersonaRolActual;
    }

    public UUID getIdClinicaActual() {
        return idClinicaActual;
    }

    public String getNombrePersona() {
        return nombrePersona;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public String getNombreClinica() {
        return nombreClinica;
    }
}