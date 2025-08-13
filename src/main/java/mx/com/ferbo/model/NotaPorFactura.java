package mx.com.ferbo.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "nota_x_facturas")
@NamedQueries({
	
	@NamedQuery(name = "NotaPorFactura.findByAll", query = "SELECT nf FROM NotaPorFactura nf")
	
})


public class NotaPorFactura implements Serializable, Cloneable{

	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private NotaPorFacturaPK notaPorFacturaPK;
	
	@Column(name = "CANTIDAD")
	private BigDecimal cantidad;
	
	public NotaPorFactura(){
		
	}
	
	public NotaPorFacturaPK getNotaPorFacturaPK() {
		return notaPorFacturaPK;
	}

	public void setNotaPorFacturaPK(NotaPorFacturaPK notaPorFacturaPK) {
		this.notaPorFacturaPK = notaPorFacturaPK;
	}

	public BigDecimal getCantidad() {
		return cantidad;
	}

	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}

	@Override
	public String toString() {
		return "NotaPorFactura [notaPorFacturaPK=" + notaPorFacturaPK + ", cantidad=" + cantidad + "]";
	}
}
