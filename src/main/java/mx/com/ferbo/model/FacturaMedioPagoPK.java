/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ferbo.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Gabriel Moreno <gabrielmos0309@gmail.com>
 */
@Embeddable
public class FacturaMedioPagoPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "factura_id")
    private int facturaId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fmp_id")
    private int fmpId;

    public FacturaMedioPagoPK() {
    }

    public FacturaMedioPagoPK(int facturaId, int fmpId) {
        this.facturaId = facturaId;
        this.fmpId = fmpId;
    }

    public int getFacturaId() {
        return facturaId;
    }

    public void setFacturaId(int facturaId) {
        this.facturaId = facturaId;
    }

    public int getFmpId() {
        return fmpId;
    }

    public void setFmpId(int fmpId) {
        this.fmpId = fmpId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) facturaId;
        hash += (int) fmpId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacturaMedioPagoPK)) {
            return false;
        }
        FacturaMedioPagoPK other = (FacturaMedioPagoPK) object;
        if (this.facturaId != other.facturaId) {
            return false;
        }
        if (this.fmpId != other.fmpId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.FacturaMedioPagoPK[ facturaId=" + facturaId + ", fmpId=" + fmpId + " ]";
    }
    
}
