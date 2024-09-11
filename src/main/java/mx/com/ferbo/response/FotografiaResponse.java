package mx.com.ferbo.response;

import mx.com.ferbo.request.FotografiaRequest;

public class FotografiaResponse extends FotografiaRequest {
	
	protected Integer codigoError;
	
	protected String mensajeError;
	
	@Override
	public String toString() {
		return "FotografiaResponse [codigoError=" + codigoError + ", mensajeError=" + mensajeError + ", numero="
				+ numero + "]";
	}
	
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
