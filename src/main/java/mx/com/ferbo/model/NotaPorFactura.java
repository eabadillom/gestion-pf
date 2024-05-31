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
	
//	@JoinColumn(name = "NOTA", referencedColumnName = "ID",insertable = false, updatable = false)
//    @ManyToOne(optional = false, cascade = CascadeType.ALL)
//	private NotaCredito nota;
//	
//	@JoinColumn(name = "FACTURA", referencedColumnName = "id", insertable = false, updatable = false)
//    @ManyToOne(optional = false, cascade = {CascadeType.MERGE})
//    private Factura factura;
	
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

//	public NotaCredito getNota() {
//		return nota;
//	}
//
//	public void setNota(NotaCredito nota) {
//		this.nota = nota;
//	}
//
//	public Factura getFactura() {
//		return factura;
//	}
//
//	public void setFactura(Factura factura) {
//		this.factura = factura;
//	}

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
