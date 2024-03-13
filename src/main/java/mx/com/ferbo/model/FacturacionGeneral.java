package mx.com.ferbo.model;

import java.math.BigDecimal;
import java.util.Date;

public class FacturacionGeneral {
	
	private BigDecimal total_facturacion;
	private BigDecimal total_por_cobrar_timbradas;
	private Date fecha;
	private BigDecimal acumulado;
	private BigDecimal porcentaje; 
	
	public BigDecimal getTotal_facturacion() {
		return total_facturacion;
	}
	public void setTotal_facturacion(BigDecimal total_facturacion) {
		this.total_facturacion = total_facturacion;
	}
	public BigDecimal getTotal_por_cobrar_timbradas() {
		return total_por_cobrar_timbradas;
	}
	public void setTotal_por_cobrar_timbradas(BigDecimal total_por_cobrar_timbradas) {
		this.total_por_cobrar_timbradas = total_por_cobrar_timbradas;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public BigDecimal getAcumulado() {
		return acumulado;
	}
	public void setAcumulado(BigDecimal acumulado) {
		this.acumulado = acumulado;
	}	
	public BigDecimal getPorcentaje() {
		return porcentaje;
	}
	public void setPorcentaje(BigDecimal porcentaje) {
		this.porcentaje = porcentaje;
	}
	@Override
	public String toString() {
		return "FacturacionGeneral [total_facturacion=" + total_facturacion + ", total_por_cobrar_timbradas="
				+ total_por_cobrar_timbradas + ", fecha=" + fecha + ", acumulado=" + acumulado + ", porcentaje="
				+ porcentaje + "]";
	}
	
	
	
}
