/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ferbo.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Gabriel Moreno <gabrielmos0309@gmail.com>
 */
@Entity
@Table(name = "CONSTANCIA_SERVICIO_DETALLE")
@NamedQueries({
    @NamedQuery(name = "ConstanciaServicioDetalle.findAll", query = "SELECT c FROM ConstanciaServicioDetalle c"),
    @NamedQuery(name = "ConstanciaServicioDetalle.findByConstanciaServicioDetalleCve", query = "SELECT c FROM ConstanciaServicioDetalle c WHERE c.constanciaServicioDetalleCve = :constanciaServicioDetalleCve"),
    @NamedQuery(name = "ConstanciaServicioDetalle.findByFolio", query = "SELECT c FROM ConstanciaServicioDetalle c WHERE c.folio = :folio"),
    @NamedQuery(name = "ConstanciaServicioDetalle.findByServicioCantidad", query = "SELECT c FROM ConstanciaServicioDetalle c WHERE c.servicioCantidad = :servicioCantidad")})
public class ConstanciaServicioDetalle implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "CONSTANCIA_SERVICIO_DETALLE_CVE")
    private Integer constanciaServicioDetalleCve;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "SERVICIO_CANTIDAD")
    private BigDecimal servicioCantidad;
    @JoinColumn(name = "FOLIO", referencedColumnName = "FOLIO")
    @ManyToOne(fetch = FetchType.LAZY)
    private ConstanciaDeServicio folio;
    @JoinColumn(name = "SERVICIO_CVE", referencedColumnName = "SERVICIO_CVE")
    @ManyToOne(optional = false)
    private Servicio servicioCve;

    public ConstanciaServicioDetalle() {
    }

    public ConstanciaServicioDetalle(Integer constanciaServicioDetalleCve) {
        this.constanciaServicioDetalleCve = constanciaServicioDetalleCve;
    }

    public Integer getConstanciaServicioDetalleCve() {
        return constanciaServicioDetalleCve;
    }

    public void setConstanciaServicioDetalleCve(Integer constanciaServicioDetalleCve) {
        this.constanciaServicioDetalleCve = constanciaServicioDetalleCve;
    }

    public BigDecimal getServicioCantidad() {
        return servicioCantidad;
    }

    public void setServicioCantidad(BigDecimal servicioCantidad) {
        this.servicioCantidad = servicioCantidad;
    }

    public ConstanciaDeServicio getFolio() {
        return folio;
    }

    public void setFolio(ConstanciaDeServicio folio) {
        this.folio = folio;
    }

    public Servicio getServicioCve() {
        return servicioCve;
    }

    public void setServicioCve(Servicio servicioCve) {
        this.servicioCve = servicioCve;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (constanciaServicioDetalleCve != null ? constanciaServicioDetalleCve.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ConstanciaServicioDetalle)) {
            return false;
        }
        ConstanciaServicioDetalle other = (ConstanciaServicioDetalle) object;
        if ((this.constanciaServicioDetalleCve == null && other.constanciaServicioDetalleCve != null) || (this.constanciaServicioDetalleCve != null && !this.constanciaServicioDetalleCve.equals(other.constanciaServicioDetalleCve))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.ConstanciaServicioDetalle[ constanciaServicioDetalleCve=" + constanciaServicioDetalleCve + " ]";
    }
    
}
