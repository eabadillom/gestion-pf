/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.bouncycastle.util.Objects;

/**
 *
 * @author Gabriel Moreno <gabrielmos0309@gmail.com>
 */
@Entity
@Table(name = "mail")
@NamedQueries({
    @NamedQuery(name = "Mail.findAll", query = "SELECT m FROM Mail m"),
    @NamedQuery(name = "Mail.findByIdMail", query = "SELECT m FROM Mail m WHERE m.idMail = :idMail"),
    @NamedQuery(name = "Mail.findByNbMail", query = "SELECT m FROM Mail m WHERE m.nbMail = :nbMail"),
    @NamedQuery(name = "Mail.findByStPrincipal", query = "SELECT m FROM Mail m WHERE m.stPrincipal = :stPrincipal")})
public class Mail implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_mail")
    private Integer idMail;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nb_mail")
    private String nbMail;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "st_principal")
    private boolean stPrincipal;
    
    @JoinColumn(name = "tp_mail", referencedColumnName = "tp_mail")
    @ManyToOne(optional = false)
    private TipoMail tpMail;
    
    @OneToMany(mappedBy = "idMail", cascade=CascadeType.ALL, orphanRemoval = true)
    private List<MedioCnt> medioCntList;
    
    @Override
    public int hashCode() {
    	if(this.idMail == null)
    		return System.identityHashCode(this);
    	return Objects.hashCode(this.idMail);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Mail)) {
            return false;
        }
        Mail other = (Mail) object;
        if ((this.idMail == null && other.idMail != null) || (this.idMail != null && !this.idMail.equals(other.idMail))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.Mail[ idMail=" + idMail + " ]";
    }

    public Mail() {
    }

    public Mail(Integer idMail) {
        this.idMail = idMail;
    }

    public Mail(Integer idMail, String nbMail, boolean stPrincipal) {
        this.idMail = idMail;
        this.nbMail = nbMail;
        this.stPrincipal = stPrincipal;
    }

    public Integer getIdMail() {
        return idMail;
    }

    public void setIdMail(Integer idMail) {
        this.idMail = idMail;
    }

    public String getNbMail() {
        return nbMail;
    }

    public void setNbMail(String nbMail) {
        this.nbMail = nbMail;
    }

    public boolean getStPrincipal() {
        return stPrincipal;
    }

    public void setStPrincipal(boolean stPrincipal) {
        this.stPrincipal = stPrincipal;
    }

    public TipoMail getTpMail() {
        return tpMail;
    }

    public void setTpMail(TipoMail tpMail) {
        this.tpMail = tpMail;
    }

    public List<MedioCnt> getMedioCntList() {
        return medioCntList;
    }

    public void setMedioCntList(List<MedioCnt> medioCntList) {
        this.medioCntList = medioCntList;
    }
}
