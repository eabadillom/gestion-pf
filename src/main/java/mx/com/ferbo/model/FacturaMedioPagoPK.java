/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Gabriel Moreno <gabrielmos0309@gmail.com>
 */
@Embeddable
public class FacturaMedioPagoPK implements Serializable {

    private static final long serialVersionUID = 1L;
    @ManyToOne(optional = false,cascade = CascadeType.ALL)//MODIFIQUE JUNIO 1
    @JoinColumn(name = "factura_id")
    private Factura facturaId;
	
	@Basic(optional = false)
    @NotNull
    @Column(name = "fmp_id")
    private int fmpId;

    public FacturaMedioPagoPK() {
    }

    public FacturaMedioPagoPK(Factura facturaId, int fmpId) {
        this.facturaId = facturaId;
        this.fmpId = fmpId;
    }

    public Factura getFacturaId() {
        return facturaId;
    }

    public void setFacturaId(Factura facturaId) {
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
		return Objects.hash(facturaId, fmpId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FacturaMedioPagoPK other = (FacturaMedioPagoPK) obj;
		return facturaId == other.facturaId && fmpId == other.fmpId;
	}

	@Override
	public String toString() {
		return "FacturaMedioPagoPK [facturaId=" + facturaId + ", fmpId=" + fmpId + "]";
	}

	
}
