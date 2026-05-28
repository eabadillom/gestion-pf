package mx.com.ferbo.ui;

import java.math.BigDecimal;

public class OcupacionCamara 
{
    private String cte_nombre;
    private String planta_ds;
    private BigDecimal tarima;//agregar posiciones que permite la camara  y posiciones disponibles

	private Integer camara_cve;
	private String camara_abrev;
	private String camara_ds;
        private String planta_abrev;
	private Integer total_pos;
	private BigDecimal posiciones_Disponibles;
	
	public OcupacionCamara() {
		
	}

    public String getCte_nombre() {
        return cte_nombre;
    }

    public void setCte_nombre(String cte_nombre) {
        this.cte_nombre = cte_nombre;
    }

	public String getPlanta_ds() {
		return planta_ds;
	}

	public void setPlanta_ds(String planta_ds) {
		this.planta_ds = planta_ds;
	}
        
        public String getPlanta_abrev() {
		return planta_abrev;
	}

	public void setPlanta_abrev(String planta_abrev) {
		this.planta_abrev = planta_abrev;
	}

	public BigDecimal getTarima() {
		return tarima;
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

	@Override
	public String toString() {
		return "OcupacionCamara [camara_cve=" + camara_cve + ", camara_abrev=" + camara_abrev + ", camara_ds="
				+ camara_ds + ", planta_ds=" + planta_ds + ", tarima=" + tarima + ", total_pos=" + total_pos
				+ ", posiciones_Disponibles=" + posiciones_Disponibles + "]";
	}
	
    public void setTarima(BigDecimal tarima) {
        this.tarima = tarima;
    }
        
}
