/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.UUID;

/**
 *
 * @author santy
 */
@Entity
@Table(name = "documento")
@NamedQueries({
    @NamedQuery(name = "Documento.findAll", query = "SELECT d FROM Documento d"),
    @NamedQuery(name = "Documento.findByValor", query = "SELECT d FROM Documento d WHERE d.valor = :valor"),
    @NamedQuery(name = "Documento.findByRutaFisica", query = "SELECT d FROM Documento d WHERE d.rutaFisica = :rutaFisica"),
    @NamedQuery(name = "Documento.findByPersonaId", query = "SELECT d FROM Documento d WHERE d.idPersona.idPersona = :id"),
    @NamedQuery(name = "Documento.findByTipoDocumentoId", query = "SELECT d FROM Documento d WHERE d.idTipoDocumento.idTipoDocumento = :id"),
    @NamedQuery(name = "Documento.countDuplicados", query = "SELECT COUNT(d) FROM Documento d WHERE d.idPersona.idPersona = :persona AND d.idTipoDocumento.idTipoDocumento = :tipo AND LOWER(d.valor) = :valor AND d.idDocumento <> :excluir"),
    @NamedQuery(name = "Documento.countDuplicadosEnOtraPersona", query = "SELECT COUNT(d) FROM Documento d WHERE d.idTipoDocumento.idTipoDocumento = :tipo AND LOWER(d.valor) = :valor AND d.idPersona.idPersona <> :persona AND d.idDocumento <> :excluir"),
    @NamedQuery(name = "Documento.countOtrosDelTipo", query = "SELECT COUNT(d) FROM Documento d WHERE d.idPersona.idPersona = :persona AND d.idTipoDocumento.idTipoDocumento = :tipo AND d.idDocumento <> :excluir")})
public class Documento implements Serializable {

    private static final long serialVersionUID = 1L;
       

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_documento")
    private UUID idDocumento;

    @NotBlank(message = "El valor del documento es obligatorio")
    @Column(name = "valor")
    private String valor;

    @Size(max = 2147483647)
    @Column(name = "ruta_fisica")
    private String rutaFisica;

    @NotNull(message = "La persona es obligatoria")
    @JoinColumn(name = "id_persona", referencedColumnName = "id_persona")
    @ManyToOne(fetch = FetchType.LAZY)
    private Persona idPersona;

    @NotNull(message = "El tipo de documento es obligatorio")
    @JoinColumn(name = "id_tipo_documento", referencedColumnName = "id_tipo_documento")
    @ManyToOne(fetch = FetchType.LAZY)
    private TipoDocumento idTipoDocumento;

    public Documento() {
    }

    public Documento(UUID idDocumento) {
        this.idDocumento = idDocumento;
    }

    public UUID getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(UUID idDocumento) {
        this.idDocumento = idDocumento;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getRutaFisica() {
        return rutaFisica;
    }

    public void setRutaFisica(String rutaFisica) {
        this.rutaFisica = rutaFisica;
    }

    public Persona getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(Persona idPersona) {
        this.idPersona = idPersona;
    }

    public TipoDocumento getIdTipoDocumento() {
        return idTipoDocumento;
    }

    public void setIdTipoDocumento(TipoDocumento idTipoDocumento) {
        this.idTipoDocumento = idTipoDocumento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDocumento != null ? idDocumento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Documento)) {
            return false;
        }
        Documento other = (Documento) object;
        if ((this.idDocumento == null && other.idDocumento != null) || (this.idDocumento != null && !this.idDocumento.equals(other.idDocumento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Documento[ idDocumento=" + idDocumento + " ]";
    }
    
}
