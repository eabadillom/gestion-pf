package mx.com.ferbo.model;

import java.math.BigDecimal;
import java.util.Date;

public class FacturacionGeneral implements Comparable<Object> {
	
	private BigDecimal total_facturacion;
	private Date fecha;
	private BigDecimal acumulado;
	private BigDecimal porcentaje; 
	private BigDecimal total_por_efectivo;
	
	public BigDecimal getTotal_facturacion() {
		return total_facturacion;
	}
	public void setTotal_facturacion(BigDecimal total_facturacion) {
		this.total_facturacion = total_facturacion;
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
		return "FacturacionGeneral [total_facturacion=" + total_facturacion +  ", fecha=" + fecha + ", acumulado=" + acumulado + ", porcentaje="
				+ porcentaje + ", total_por_efectivo=" + total_por_efectivo + "]";
	}
	@Override
	public int compareTo(Object obj) {
		FacturacionGeneral f = (FacturacionGeneral) obj;
		return this.fecha.compareTo(f.fecha);
	}
	public BigDecimal getTotal_por_efectivo() {
		return total_por_efectivo;
	}
	public void setTotal_por_efectivo(BigDecimal total_por_efectivo) {
		this.total_por_efectivo = total_por_efectivo;
	}
	
	
	
}
