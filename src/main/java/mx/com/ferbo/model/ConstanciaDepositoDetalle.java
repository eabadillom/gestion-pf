/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ferbo.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Gabriel Moreno <gabrielmos0309@gmail.com>
 */
@Entity
@Table(name = "constancia_deposito_detalle")
@NamedQueries({
    @NamedQuery(name = "ConstanciaDepositoDetalle.findAll", query = "SELECT c FROM ConstanciaDepositoDetalle c"),
    @NamedQuery(name = "ConstanciaDepositoDetalle.findByConstanciaDepositoDetalleCve", query = "SELECT c FROM ConstanciaDepositoDetalle c WHERE c.constanciaDepositoDetalleCve = :constanciaDepositoDetalleCve"),
    @NamedQuery(name = "ConstanciaDepositoDetalle.findFolio", query = "SELECT c FROM ConstanciaDepositoDetalle c WHERE c.folio.folio = :folio"),
    @NamedQuery(name = "ConstanciaDepositoDetalle.findByServicioCantidad", query = "SELECT c FROM ConstanciaDepositoDetalle c WHERE c.servicioCantidad = :servicioCantidad")})
public class ConstanciaDepositoDetalle implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "CONSTANCIA_DEPOSITO_DETALLE_CVE")
    private Integer constanciaDepositoDetalleCve;
    
    @Column(name = "servicio_cantidad")
    private BigDecimal servicioCantidad;
    
    @JoinColumn(name = "FOLIO", referencedColumnName = "FOLIO")
    @ManyToOne(optional = false)
    private ConstanciaDeDeposito folio;
    
    @JoinColumn(name = "SERVICIO_CVE", referencedColumnName = "SERVICIO_CVE")
    @ManyToOne
    private Servicio servicioCve;

    public ConstanciaDepositoDetalle() {
    }

    public ConstanciaDepositoDetalle(Integer constanciaDepositoDetalleCve) {
        this.constanciaDepositoDetalleCve = constanciaDepositoDetalleCve;
    }

    public Integer getConstanciaDepositoDetalleCve() {
        return constanciaDepositoDetalleCve;
    }

    public void setConstanciaDepositoDetalleCve(Integer constanciaDepositoDetalleCve) {
        this.constanciaDepositoDetalleCve = constanciaDepositoDetalleCve;
    }

    public BigDecimal getServicioCantidad() {
        return servicioCantidad;
    }

    public void setServicioCantidad(BigDecimal servicioCantidad) {
        this.servicioCantidad = servicioCantidad;
    }

    public ConstanciaDeDeposito getFolio() {
        return folio;
    }

    public void setFolio(ConstanciaDeDeposito folio) {
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
    	if(this.constanciaDepositoDetalleCve == null)
    		return System.identityHashCode(this);
    	return Objects.hash(this.constanciaDepositoDetalleCve);
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ConstanciaDepositoDetalle)) {
            return false;
        }
        ConstanciaDepositoDetalle other = (ConstanciaDepositoDetalle) object;
        if ((this.constanciaDepositoDetalleCve == null && other.constanciaDepositoDetalleCve != null) || (this.constanciaDepositoDetalleCve != null && !this.constanciaDepositoDetalleCve.equals(other.constanciaDepositoDetalleCve))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.ConstanciaDepositoDetalle[ constanciaDepositoDetalleCve=" + constanciaDepositoDetalleCve + " ]";
    }
    
}
