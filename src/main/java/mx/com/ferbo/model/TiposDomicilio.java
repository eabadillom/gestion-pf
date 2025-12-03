/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Gabriel Moreno <gabrielmos0309@gmail.com>
 */
@Entity
@Table(name = "tipos_domicilio")
@NamedQueries({
    @NamedQuery(name = "TiposDomicilio.findAll", query = "SELECT t FROM TiposDomicilio t"),
    @NamedQuery(name = "TiposDomicilio.findByDomicilioTipoCve", query = "SELECT t FROM TiposDomicilio t WHERE t.domicilioTipoCve = :domicilioTipoCve"),
    @NamedQuery(name = "TiposDomicilio.findByDomicilioTipoDesc", query = "SELECT t FROM TiposDomicilio t WHERE t.domicilioTipoDesc = :domicilioTipoDesc")
})
public class TiposDomicilio implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "domicilio_tipo_cve")
    private Short domicilioTipoCve;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "domicilio_tipo_desc")
    private String domicilioTipoDesc;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "domicilioTipoCve")
    private List<Domicilios> domiciliosList;

    public TiposDomicilio() {
    }

    public TiposDomicilio(Short domicilioTipoCve) {
        this.domicilioTipoCve = domicilioTipoCve;
    }

    public TiposDomicilio(Short domicilioTipoCve, String domicilioTipoDesc) {
        this.domicilioTipoCve = domicilioTipoCve;
        this.domicilioTipoDesc = domicilioTipoDesc;
    }

    public Short getDomicilioTipoCve() {
        return domicilioTipoCve;
    }

    public void setDomicilioTipoCve(Short domicilioTipoCve) {
        this.domicilioTipoCve = domicilioTipoCve;
    }

    public String getDomicilioTipoDesc() {
        return domicilioTipoDesc;
    }

    public void setDomicilioTipoDesc(String domicilioTipoDesc) {
        this.domicilioTipoDesc = domicilioTipoDesc;
    }

    public List<Domicilios> getDomiciliosList() {
        return domiciliosList;
    }

    public void setDomiciliosList(List<Domicilios> domiciliosList) {
        this.domiciliosList = domiciliosList;
    }

    @Override
    public int hashCode() {
        if (this.domicilioTipoCve == null) {
            return System.identityHashCode(this);
        }
        return Objects.hash(domicilioTipoCve);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object == null)
            return false;
        if (getClass() != object.getClass())
            return false;
        TiposDomicilio other = (TiposDomicilio) object;
        if(this.domicilioTipoCve == null || other.domicilioTipoCve == null)
            return Objects.equals(System.identityHashCode(this), System.identityHashCode(other));
        return Objects.equals(domicilioTipoCve, other.domicilioTipoCve);
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.TiposDomicilio[ domicilioTipoCve=" + domicilioTipoCve + " ]";
    }
    
}
