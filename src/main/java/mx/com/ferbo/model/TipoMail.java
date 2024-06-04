/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ferbo.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Gabriel Moreno <gabrielmos0309@gmail.com>
 */
@Entity
@Table(name = "tipo_mail")
@NamedQueries({
    @NamedQuery(name = "TipoMail.findAll", query = "SELECT t FROM TipoMail t"),
    @NamedQuery(name = "TipoMail.findByTpMail", query = "SELECT t FROM TipoMail t WHERE t.tpMail = :tpMail"),
    @NamedQuery(name = "TipoMail.findByNbTipo", query = "SELECT t FROM TipoMail t WHERE t.nbTipo = :nbTipo")})
public class TipoMail implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "tp_mail")
    private Short tpMail;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nb_tipo")
    private String nbTipo;

    public TipoMail() {
    }

    public TipoMail(Short tpMail) {
        this.tpMail = tpMail;
    }

    public TipoMail(Short tpMail, String nbTipo) {
        this.tpMail = tpMail;
        this.nbTipo = nbTipo;
    }

    public Short getTpMail() {
        return tpMail;
    }

    public void setTpMail(Short tpMail) {
        this.tpMail = tpMail;
    }

    public String getNbTipo() {
        return nbTipo;
    }

    public void setNbTipo(String nbTipo) {
        this.nbTipo = nbTipo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tpMail != null ? tpMail.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoMail)) {
            return false;
        }
        TipoMail other = (TipoMail) object;
        if ((this.tpMail == null && other.tpMail != null) || (this.tpMail != null && !this.tpMail.equals(other.tpMail))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.TipoMail[ tpMail=" + tpMail + " ]";
    }
    
}
