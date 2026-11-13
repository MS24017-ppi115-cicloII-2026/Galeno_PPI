/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

/**
 *
 * @author santy
 */
@Entity
@Table(name = "persona_rol")
@NamedQueries({
    @NamedQuery(name = "PersonaRol.findAll", query = "SELECT p FROM PersonaRol p"),
    @NamedQuery(name = "PersonaRol.findByFechaCreacion", query = "SELECT p FROM PersonaRol p WHERE p.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "PersonaRol.findByPersona", query = "SELECT p FROM PersonaRol p WHERE p.idPersona = :persona"),
    @NamedQuery(name = "PersonaRol.findByClinica", query = "SELECT p FROM PersonaRol p WHERE p.idClinica = :clinica"),
    @NamedQuery(name = "PersonaRol.findByRolConRelaciones", query = "SELECT p FROM PersonaRol p JOIN FETCH p.idPersona JOIN FETCH p.idRol LEFT JOIN FETCH p.idClinica WHERE p.idRol = :rol"),
    @NamedQuery(name = "PersonaRol.findByNombreRolConRelaciones", query = "SELECT p FROM PersonaRol p JOIN FETCH p.idPersona JOIN FETCH p.idRol WHERE LOWER(p.idRol.nombre) = :nombre"),
    @NamedQuery(name = "PersonaRol.findByNombreRolSinEl", query = "SELECT p FROM PersonaRol p JOIN FETCH p.idPersona JOIN FETCH p.idRol WHERE LOWER(p.idRol.nombre) <> :nombre"),
    @NamedQuery(name = "PersonaRol.findPorIdConRelaciones", query = "SELECT pr FROM PersonaRol pr JOIN FETCH pr.idPersona JOIN FETCH pr.idRol LEFT JOIN FETCH pr.idClinica WHERE pr.idPersonaRol = :id"),
    @NamedQuery(name = "PersonaRol.buscarPacientesPorCriterio", query = "SELECT DISTINCT p FROM PersonaRol p JOIN FETCH p.idPersona per JOIN FETCH p.idRol r LEFT JOIN FETCH p.idClinica cl LEFT JOIN FETCH per.documentoCollection LEFT JOIN FETCH per.medioContactoCollection WHERE LOWER(r.nombre) = :nombreRol AND (LOWER(per.nombres) LIKE :c OR LOWER(per.apellidos) LIKE :c OR EXISTS (SELECT d FROM Documento d WHERE d.idPersona = per AND LOWER(d.valor) LIKE :c) OR EXISTS (SELECT m FROM MedioContacto m WHERE m.idPersona = per AND LOWER(m.valor) LIKE :c))"),
    @NamedQuery(name = "PersonaRol.buscarPacientesPorCriterioEnClinica", query = "SELECT DISTINCT p FROM PersonaRol p JOIN FETCH p.idPersona per JOIN FETCH p.idRol r LEFT JOIN FETCH p.idClinica cl LEFT JOIN FETCH per.documentoCollection LEFT JOIN FETCH per.medioContactoCollection WHERE LOWER(r.nombre) = :nombreRol AND (LOWER(per.nombres) LIKE :c OR LOWER(per.apellidos) LIKE :c OR EXISTS (SELECT d FROM Documento d WHERE d.idPersona = per AND LOWER(d.valor) LIKE :c) OR EXISTS (SELECT m FROM MedioContacto m WHERE m.idPersona = per AND LOWER(m.valor) LIKE :c)) AND cl.idClinica = :idClinica"),
    @NamedQuery(name = "PersonaRol.countDuplicados", query = "SELECT COUNT(pr) FROM PersonaRol pr WHERE pr.idPersona = :persona AND pr.idRol = :rol AND pr.idClinica = :clinica"),
    @NamedQuery(name = "PersonaRol.countDuplicadosExcluyendoRegistro", query = "SELECT COUNT(pr) FROM PersonaRol pr WHERE pr.idPersona = :persona AND pr.idRol = :rol AND pr.idClinica = :clinica AND pr.idPersonaRol <> :idPersonaRol")})
public class PersonaRol implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_persona_rol")
    private UUID idPersonaRol;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @OneToMany(mappedBy = "idPersonaRol", fetch = FetchType.LAZY)
    private Collection<ConsultaProcedimientoPaso> consultaProcedimientoPasoCollection;
    @JoinColumn(name = "id_clinica", referencedColumnName = "id_clinica")
    @ManyToOne(fetch = FetchType.LAZY)
    private Clinica idClinica;
    @JoinColumn(name = "id_persona", referencedColumnName = "id_persona")
    @ManyToOne(fetch = FetchType.LAZY)
    private Persona idPersona;
    @JoinColumn(name = "id_rol", referencedColumnName = "id_rol")
    @ManyToOne(fetch = FetchType.LAZY)
    private Rol idRol;
    @OneToMany(mappedBy = "idPersonaRol", fetch = FetchType.LAZY)
    private Collection<Consulta> consultaCollection;

    public PersonaRol() {
    }

    public PersonaRol(UUID idPersonaRol) {
        this.idPersonaRol = idPersonaRol;
    }

    public UUID getIdPersonaRol() {
        return idPersonaRol;
    }

    public void setIdPersonaRol(UUID idPersonaRol) {
        this.idPersonaRol = idPersonaRol;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
@JsonbTransient
    public Collection<ConsultaProcedimientoPaso> getConsultaProcedimientoPasoCollection() {
        return consultaProcedimientoPasoCollection;
    }

    public void setConsultaProcedimientoPasoCollection(Collection<ConsultaProcedimientoPaso> consultaProcedimientoPasoCollection) {
        this.consultaProcedimientoPasoCollection = consultaProcedimientoPasoCollection;
    }

    public Clinica getIdClinica() {
        return idClinica;
    }

    public void setIdClinica(Clinica idClinica) {
        this.idClinica = idClinica;
    }

    public Persona getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(Persona idPersona) {
        this.idPersona = idPersona;
    }

    public Rol getIdRol() {
        return idRol;
    }

    public void setIdRol(Rol idRol) {
        this.idRol = idRol;
    }
@JsonbTransient
    public Collection<Consulta> getConsultaCollection() {
        return consultaCollection;
    }

    public void setConsultaCollection(Collection<Consulta> consultaCollection) {
        this.consultaCollection = consultaCollection;
    }

    @PrePersist
    protected void inicializarFechaCreacion() {
        if (fechaCreacion == null) {
            fechaCreacion = new Date();
        }
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPersonaRol != null ? idPersonaRol.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PersonaRol)) {
            return false;
        }
        PersonaRol other = (PersonaRol) object;
        if ((this.idPersonaRol == null && other.idPersonaRol != null) || (this.idPersonaRol != null && !this.idPersonaRol.equals(other.idPersonaRol))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.PersonaRol[ idPersonaRol=" + idPersonaRol + " ]";
    }

}
