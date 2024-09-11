package mx.com.ferbo.request;
import java.util.Objects;

public class FotografiaRequest {
	
	protected String fotografia;
	
	protected String numero;

	@Override
	public int hashCode() {
		return Objects.hash(numero);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FotografiaRequest other = (FotografiaRequest) obj;
		return Objects.equals(numero, other.numero);
	}

	@Override
	public String toString() {
		return "FotografiaRequest [numero=" + numero + "]";
	}

	public String getFotografia() {
		return fotografia;
	}

	public void setFotografia(String fotografia) {
		this.fotografia = fotografia;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}
	
	

}
