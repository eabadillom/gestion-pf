package mx.com.ferbo.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "NOTA_X_FACTURAS")
@NamedQueries({
	
	@NamedQuery(name = "NotaPorFactura.findByAll", query = "SELECT nf FROM NotaPorFactura nf"),
	@NamedQuery(name = "NotaPorFactura.findByNotaCredito", query = "SELECT nf FROM NotaPorFactura nf WHERE nf.nota = :nota"),
	@NamedQuery(name = "NotaPorFactura.findByFactura", query = "SELECT nf FROM NotaPorFactura nf WHERE nf.factura = :factura"),
	
})


public class NotaPorFactura implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@JoinColumn(name = "NOTA", referencedColumnName = "ID")
    @ManyToOne
	private NotaCredito nota;
	
	@JoinColumn(name = "FACTURA", referencedColumnName = "id")
    @ManyToOne
    private Factura factura;
	
	@Column(name = "CANTIDAD")
	private BigDecimal cantidad;
	
	
	
	public NotaPorFactura(){
		
	}

	public NotaCredito getNota() {
		return nota;
	}

	public void setNota(NotaCredito nota) {
		this.nota = nota;
	}

	public Factura getFactura() {
		return factura;
	}

	public void setFactura(Factura factura) {
		this.factura = factura;
	}

	public BigDecimal getCantidad() {
		return cantidad;
	}

	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}

	@Override
	public String toString() {
		return "NotaPorFactura [nota=" + nota + ", factura=" + factura + ", cantidad=" + cantidad + "]";
	}
	
	
	
	
}
