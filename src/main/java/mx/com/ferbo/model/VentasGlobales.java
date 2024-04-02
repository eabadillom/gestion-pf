package mx.com.ferbo.model;

import java.math.BigDecimal;

public class VentasGlobales {
	
	private BigDecimal ventasTotales;
	private BigDecimal ganancias;
	private BigDecimal porcentajeGanancias;
	
	
	@Override
	public String toString() {
		return "VentasGlobales [ventasTotales=" + ventasTotales + ", ganancias=" + ganancias + ", porcentajeGanancias="
				+ porcentajeGanancias + "]";
	}
	public BigDecimal getVentasTotales() {
		return ventasTotales;
	}
	public void setVentasTotales(BigDecimal ventasTotales) {
		this.ventasTotales = ventasTotales;
	}
	public BigDecimal getGanancias() {
		return ganancias;
	}
	public void setGanancias(BigDecimal ganancias) {
		this.ganancias = ganancias;
	}
	public BigDecimal getPorcentajeGanancias() {
		return porcentajeGanancias;
	}
	public void setPorcentajeGanancias(BigDecimal porcentajeGanancias) {
		this.porcentajeGanancias = porcentajeGanancias;
	}
	
	

}
