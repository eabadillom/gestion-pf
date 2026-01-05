package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class NotaPorFacturaPK implements Serializable, Cloneable{
	
	private static final long serialVersionUID = 1L;
	
	@JoinColumn(name = "NOTA")
    @ManyToOne(optional = false, cascade = {CascadeType.MERGE})
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
		return "NotaPorFacturaPK [nota=" + nota.getId() + ", factura=" + factura.getId() + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(factura, nota);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NotaPorFacturaPK other = (NotaPorFacturaPK) obj;
		return Objects.equals(factura, other.factura) && Objects.equals(nota, other.nota);
	}
	
	
	
}
