package mx.com.ferbo.request;

import java.util.Objects;

public class BiometricoRequest {
	protected String numero;
	
	protected String biometrico1;
	
	protected String biometrico2;
	
	protected String token;

	@Override
	public String toString() {
		return "BiometricoRequest [numero=" + numero + "]";
	}

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
		BiometricoRequest other = (BiometricoRequest) obj;
		return Objects.equals(numero, other.numero);
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getBiometrico1() {
		return biometrico1;
	}

	public void setBiometrico1(String biometrico1) {
		this.biometrico1 = biometrico1;
	}

	public String getBiometrico2() {
		return biometrico2;
	}

	public void setBiometrico2(String biometrico2) {
		this.biometrico2 = biometrico2;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
