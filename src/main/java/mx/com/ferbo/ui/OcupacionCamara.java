package mx.com.ferbo.ui;

import java.math.BigDecimal;

public class OcupacionCamara {

	private Integer camara_cve;
	private String camara_abrev;
	private String camara_ds;
	private String planta_ds;
	private BigDecimal tarima;
	private Integer total_pos;
	private BigDecimal posiciones_Generales_Ocupadas;
	private BigDecimal posiciones_Disponibles;
	
	public OcupacionCamara() {
		
	}

	public Integer getCamara_cve() {
		return camara_cve;
	}

	public void setCamara_cve(Integer camara_cve) {
		this.camara_cve = camara_cve;
	}

	public String getCamara_abrev() {
		return camara_abrev;
	}

	public void setCamara_abrev(String camara_abrev) {
		this.camara_abrev = camara_abrev;
	}

	public String getCamara_ds() {
		return camara_ds;
	}

	public void setCamara_ds(String camara_ds) {
		this.camara_ds = camara_ds;
	}

	public String getPlanta_ds() {
		return planta_ds;
	}

	public void setPlanta_ds(String planta_ds) {
		this.planta_ds = planta_ds;
	}

	public BigDecimal getTarima() {
		return tarima;
	}

	public void setTarima(BigDecimal tarima) {
		this.tarima = tarima;
	}	

	public Integer getTotal_pos() {
		return total_pos;
	}

	public void setTotal_pos(Integer total_pos) {
		this.total_pos = total_pos;
	}

	public BigDecimal getPosiciones_Disponibles() {
		return posiciones_Disponibles;
	}

	public void setPosiciones_Disponibles(BigDecimal posiciones_Disponibles) {
		this.posiciones_Disponibles = posiciones_Disponibles;
	}

	public BigDecimal getPosiciones_Generales_Ocupadas() {
		return posiciones_Generales_Ocupadas;
	}

	public void setPosiciones_Generales_Ocupadas(BigDecimal posiciones_Generales_Ocupadas) {
		this.posiciones_Generales_Ocupadas = posiciones_Generales_Ocupadas;
	}

	@Override
	public String toString() {
		return "OcupacionCamara [camara_cve=" + camara_cve + ", camara_abrev=" + camara_abrev + ", camara_ds="
				+ camara_ds + ", planta_ds=" + planta_ds + ", tarima=" + tarima + ", total_pos=" + total_pos
				+ ", posiciones_Generales_Ocupadas=" + posiciones_Generales_Ocupadas + ", posiciones_Disponibles="
				+ posiciones_Disponibles + "]";
	}

	

	

	
	
}
