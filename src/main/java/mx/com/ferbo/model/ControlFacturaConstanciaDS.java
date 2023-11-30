package mx.com.ferbo.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity

@Table(name="control_factura_constancia_ds")
@NamedQueries({
	@NamedQuery(name = "ControlFacturaConstanciaDS.findByConstancia", query = "SELECT c FROM ControlFacturaConstanciaDS c WHERE c.constanciaDeServicio.folio = :constancia"),
	@NamedQuery(name = "ControlFacturaConstanciaDS.findByFactura", query = "SELECT c FROM ControlFacturaConstanciaDS c WHERE c.factura = :factura")
})
public class ControlFacturaConstanciaDS implements Serializable {

	private static final long serialVersionUID = 5847560075881311313L;
	
	@EmbeddedId
	private ControlFacturaConstanciaDsPK controlFactConstDsPK;
	
	@JoinColumn(name = "CONSTANCIA", referencedColumnName = "FOLIO",insertable = false, updatable = false)
    @ManyToOne(optional = false)
	private ConstanciaDeServicio constanciaDeServicio; 
	
	@JoinColumn(name = "FACTURA", referencedColumnName = "id",insertable = false, updatable = false)
    @ManyToOne(optional = false)
	private Factura factura;
	
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

	public ControlFacturaConstanciaDsPK getControlFactConstDsPK() {
		return controlFactConstDsPK;
	}

	public void setControlFactConstDsPK(ControlFacturaConstanciaDsPK controlFactConstDsPK) {
		this.controlFactConstDsPK = controlFactConstDsPK;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((constanciaDeServicio.getFolio() == null) ? 0 : constanciaDeServicio.getFolio().hashCode());
		result = prime * result + ((factura.getId() == null) ? 0 : factura.getId().hashCode());
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
		if (constanciaDeServicio.getFolio() == null) {
			if (other.constanciaDeServicio.getFolio() != null)
				return false;
		} else if (!constanciaDeServicio.getFolio().equals(other.constanciaDeServicio.getFolio()))
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
		return "ControlFacturaConstanciaDS [controlFactConstDsPK=" + controlFactConstDsPK + ", constanciaDeServicio="
				+ constanciaDeServicio + ", factura=" + factura + ", status=" + status + "]";
	}

	

	
	
	

}
