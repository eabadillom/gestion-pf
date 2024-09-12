package mx.com.ferbo.response;

import mx.com.ferbo.request.BiometricoRequest;

public class BiometricoResponse extends BiometricoRequest {
	private Integer codigoError;
	
	private String mensajeError;

	public Integer getCodigoError() {
		return codigoError;
	}

	public void setCodigoError(Integer codigoError) {
		this.codigoError = codigoError;
	}

	public String getMensajeError() {
		return mensajeError;
	}

	public void setMensajeError(String mensajeError) {
		this.mensajeError = mensajeError;
	}
}
