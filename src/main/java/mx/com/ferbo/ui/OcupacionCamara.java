package mx.com.ferbo.ui;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class OcupacionCamara {

	@Id
	private Integer camara_cve;
	private String camara_abrev;
	private String camara_ds;
	private String planta_ds;
	private Integer tarima;
	
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

	public Integer getTarima() {
		return tarima;
	}

	public void setTarima(Integer tarima) {
		this.tarima = tarima;
	}

	@Override
	public String toString() {
		return "OcupacionCamara [camara_cve=" + camara_cve + ", camara_abrev=" + camara_abrev + ", camara_ds="
				+ camara_ds + ", planta_ds=" + planta_ds + ", tarima=" + tarima + "]";
	}

	
	
}
