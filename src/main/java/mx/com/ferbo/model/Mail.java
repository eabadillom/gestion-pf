/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "mail")
@NamedQuery(name = "Mail.findAll", query = "SELECT m FROM Mail m")
@NamedQuery(name = "Mail.findByIdMail", query = "SELECT m FROM Mail m WHERE m.id = :idMail")
@NamedQuery(name = "Mail.findByNbMail", query = "SELECT m FROM Mail m WHERE m.descripcion = :nbMail")
@NamedQuery(name = "Mail.findByStPrincipal", query = "SELECT m FROM Mail m WHERE m.principal = :stPrincipal")
public class Mail implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_mail")
    private Integer id;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nb_mail")
    private String descripcion;

    @Basic(optional = false)
    @NotNull
    @Column(name = "st_principal")
    private boolean principal;

    @JoinColumn(name = "tp_mail", referencedColumnName = "tp_mail")
    @ManyToOne(optional = false)
    private TipoMail tipoMail;

    //@OneToOne(mappedBy = "mail", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<MedioContacto> mediosContacto;
//    @OneToOne(optional = true)
//    private MedioContacto medioContacto;
    
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Mail))
            return false;
        Mail that = (Mail) o;

        if (this.id != null && that.id != null) {
            return Objects.equals(this.id, that.id);
        } else {
            return this == that;
        }
    }

    @Override
    public int hashCode() {
        return (id != null) ? id.hashCode() : System.identityHashCode(this);
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.Mail[ idMail=" + id + " ]";
    }

    public Mail() {
    }

    public Mail(Integer id) {
        this.id = id;
    }

    public Mail(Integer id, String descripcion, boolean principal) {
        this.id = id;
        this.descripcion = descripcion;
        this.principal = principal;
    }

    public Integer getIdMail() {
        return id;
    }

    public void setIdMail(Integer idMail) {
        this.id = idMail;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean getPrincipal() {
        return principal;
    }

    public void setPrincipal(boolean principal) {
        this.principal = principal;
    }

    public TipoMail getTipoMail() {
        return tipoMail;
    }

    public void setTipoMail(TipoMail tipoMail) {
        this.tipoMail = tipoMail;
    }

//	public MedioContacto getMedioContacto() {
//		return medioContacto;
//	}

//	public void setMedioContacto(MedioContacto medioContacto) {
//		this.medioContacto = medioContacto;
//	}

//    public List<MedioContacto> getMedioCntList() {
//        return mediosContacto;
//    }
//
//    public void setMedioCntList(List<MedioContacto> medioCntList) {
//        this.mediosContacto = medioCntList;
//    }
}
