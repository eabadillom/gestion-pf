package mx.com.ferbo.model;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class ControlFacturaConstanciaDsPK implements Serializable {

	private static final long serialVersionUID = 1L;

	@JoinColumn(name = "CONSTANCIA")
	@ManyToOne(optional = false)
	private ConstanciaDeServicio constanciaDeServicio;

	@JoinColumn(name = "FACTURA")
	@ManyToOne(optional = false)
	private Factura factura;

	public ControlFacturaConstanciaDsPK() {
	}

	public ControlFacturaConstanciaDsPK(ConstanciaDeServicio constanciaDeServicio, Factura factura) {
		super();
		this.constanciaDeServicio = constanciaDeServicio;
		this.factura = factura;
	}

	public ConstanciaDeServicio getConstanciaDeServicio() {
		return constanciaDeServicio;
	}

	public void setConstanciaDeServicio(ConstanciaDeServicio constanciaDeServicio) {
		this.constanciaDeServicio = constanciaDeServicio;
	}

	public Factura getFactura() {
		return factura;
	}

	public void setFactura(Factura factura) {
		this.factura = factura;
	}

	@Override
	public String toString() {
		return "ControlFacturaConstanciaDsPK [constanciaDeServicio=" + constanciaDeServicio + ", factura=" + factura
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((constanciaDeServicio.getFolio() == null) ? 0 : constanciaDeServicio.getFolio().hashCode());
		result = prime * result + ((factura.getId() == null) ? 0 : factura.getId().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ControlFacturaConstanciaDsPK other = (ControlFacturaConstanciaDsPK) obj;
		if (constanciaDeServicio.getFolio() == null) {
			if (other.constanciaDeServicio.getFolio() != null)
				return false;
		} else if (!constanciaDeServicio.getFolio().equals(other.constanciaDeServicio.getFolio()))
			return false;
		if (factura.getId() == null) {
			if (other.factura.getId() != null)
				return false;
		} else if (!factura.getId().equals(other.factura.getId()))
			return false;
		return true;
	}
}
