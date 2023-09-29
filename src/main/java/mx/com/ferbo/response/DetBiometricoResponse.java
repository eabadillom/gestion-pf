package mx.com.ferbo.response;

import java.util.Date;

public class DetBiometricoResponse {
	
	private Integer idBiometrico;
	private Integer idEmpleado;
	private Date fechaCaptura;
	private Integer activo;
	private String huella;
	private String huella2;
	
	public Integer getIdBiometrico() {
		return idBiometrico;
	}
	public void setIdBiometrico(Integer idBiometrico) {
		this.idBiometrico = idBiometrico;
	}
	public Integer getIdEmpleado() {
		return idEmpleado;
	}
	public void setIdEmpleado(Integer idEmpleado) {
		this.idEmpleado = idEmpleado;
	}
	public Date getFechaCaptura() {
		return fechaCaptura;
	}
	public void setFechaCaptura(Date fechaCaptura) {
		this.fechaCaptura = fechaCaptura;
	}
	public Integer getActivo() {
		return activo;
	}
	public void setActivo(Integer activo) {
		this.activo = activo;
	}
	public String getHuella() {
		return huella;
	}
	public void setHuella(String huella) {
		this.huella = huella;
	}
	public String getHuella2() {
		return huella2;
	}
	public void setHuella2(String huella2) {
		this.huella2 = huella2;
	}
	
	@Override
	public String toString() {
		return "DetBiometricoResponse [idBiometrico=" + idBiometrico + ", idEmpleado=" + idEmpleado + ", fechaCaptura="
				+ fechaCaptura + ", activo=" + activo + ", huella=" + huella + ", huella2=" + huella2 + "]";
	}
	
	

}

