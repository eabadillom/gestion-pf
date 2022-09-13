package mx.com.ferbo.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity

@Table(name="CONTROL_FACTURA_CONSTANCIA_DS")
@NamedQueries({
	@NamedQuery(name = "ControlFacturaConstanciaDS.findByConstancia", query = "SELECT c FROM ControlFacturaConstanciaDS c WHERE c.constancia = :constancia"),
	@NamedQuery(name = "ControlFacturaConstanciaDS.findByFactura", query = "SELECT c FROM ControlFacturaConstanciaDS c WHERE c.factura = :factura")
})
public class ControlFacturaConstanciaDS implements Serializable {

	private static final long serialVersionUID = 5847560075881311313L;
	
	@Id
	@Column(name="CONSTANCIA")
	private Integer constancia;
	
	@Id
	@Column(name="FACTURA")
	private Integer factura;
	
	@Column(name="STATUS")
	private Integer status;

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public ControlFacturaConstanciaDS() {
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((constancia == null) ? 0 : constancia.hashCode());
		result = prime * result + ((factura == null) ? 0 : factura.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
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
		ControlFacturaConstanciaDS other = (ControlFacturaConstanciaDS) obj;
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
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ControlFacturaConstanciaDS [constancia=" + constancia + ", factura=" + factura + ", status=" + status
				+ "]";
	}

	
	
	

}
