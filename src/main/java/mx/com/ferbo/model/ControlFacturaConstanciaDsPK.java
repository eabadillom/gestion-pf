package mx.com.ferbo.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ControlFacturaConstanciaDsPK implements Serializable {
	
	private static final long serialVersionUID = -7338582315367180517L;
	
	@Column(name="CONSTANCIA")
	protected Integer constancia;
	
	@Column(name="FACTURA")
	protected Integer factura;
	
	public ControlFacturaConstanciaDsPK() {
	}

	public ControlFacturaConstanciaDsPK(Integer constancia, Integer factura) {
		this.constancia = constancia;
		this.factura = factura;
	}

	public Integer getConstancia() {
		return constancia;
	}

	public void setConstancia(Integer constancia) {
		this.constancia = constancia;
	}

	public Integer getFactura() {
		return factura;
	}

	public void setFactura(Integer factura) {
		this.factura = factura;
	}

	@Override
	public String toString() {
		return "ControlFacturaConstanciaDsPK [constancia=" + constancia + ", factura=" + factura + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((constancia == null) ? 0 : constancia.hashCode());
		result = prime * result + ((factura == null) ? 0 : factura.hashCode());
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
		if (constancia == null) {
			if (other.constancia != null)
				return false;
		} else if (!constancia.equals(other.constancia))
			return false;
		if (factura == null) {
			if (other.factura != null)
				return false;
		} else if (!factura.equals(other.factura))
			return false;
		return true;
	}
}
