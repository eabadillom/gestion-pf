package mx.com.ferbo.model;

import java.io.Serializable;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class NotaPorFacturaPK implements Serializable, Cloneable{
	
	private static final long serialVersionUID = 1L;
	
	@JoinColumn(name = "NOTA")
    @ManyToOne(optional = false)
	private NotaCredito nota;
	
	@JoinColumn(name = "FACTURA")
    @ManyToOne(optional = false)
    private Factura factura;

	public NotaPorFacturaPK() {
		
	}

	public NotaPorFacturaPK(NotaCredito nota, Factura factura) {
		super();
		this.nota = nota;
		this.factura = factura;
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

	@Override
	public String toString() {
		return "NotaPorFacturaPK [nota=" + nota + ", factura=" + factura + "]";
	}
	
	
	
}
