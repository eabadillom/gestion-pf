package mx.com.ferbo.ui;

import java.math.BigDecimal;
import java.util.Date;

public class RepServicios {
	
	private Integer folio = null;
	private String folioCliente = null;
	private Date fecha = null;
	private String observaciones = null;
	private String numeroCliente = null;
	private String nombreCliente = null;
	private String productoDescripcion = null;
	private BigDecimal cantidadCobro = null; //Peso
	private Integer cantidadTotal = null; //Cantidad
	private String unidadManejo = null;
	private String servicio = null;
	private String tipoCobro = null;
	private BigDecimal servicioCantidad = null;
	
	public Integer getFolio() {
		return folio;
	}
	public void setFolio(Integer folio) {
		this.folio = folio;
	}
	public String getFolioCliente() {
		return folioCliente;
	}
	public void setFolioCliente(String folioCliente) {
		this.folioCliente = folioCliente;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	public String getNumeroCliente() {
		return numeroCliente;
	}
	public void setNumeroCliente(String numeroCliente) {
		this.numeroCliente = numeroCliente;
	}
	public String getNombreCliente() {
		return nombreCliente;
	}
	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}
	public String getProductoDescripcion() {
		return productoDescripcion;
	}
	public void setProductoDescripcion(String productoDescripcion) {
		this.productoDescripcion = productoDescripcion;
	}
	public BigDecimal getCantidadCobro() {
		return cantidadCobro;
	}
	public void setCantidadCobro(BigDecimal cantidadCobro) {
		this.cantidadCobro = cantidadCobro;
	}
	public Integer getCantidadTotal() {
		return cantidadTotal;
	}
	public void setCantidadTotal(Integer cantidadTotal) {
		this.cantidadTotal = cantidadTotal;
	}
	public String getUnidadManejo() {
		return unidadManejo;
	}
	public void setUnidadManejo(String unidadManejo) {
		this.unidadManejo = unidadManejo;
	}
	public String getServicio() {
		return servicio;
	}
	public void setServicio(String servicio) {
		this.servicio = servicio;
	}
	public String getTipoCobro() {
		return tipoCobro;
	}
	public void setTipoCobro(String tipoCobro) {
		this.tipoCobro = tipoCobro;
	}
	public BigDecimal getServicioCantidad() {
		return servicioCantidad;
	}
	public void setServicioCantidad(BigDecimal servicioCantidad) {
		this.servicioCantidad = servicioCantidad;
	}
	@Override
	public String toString() {
		return "{\"folio\":\"" + folio + "\", \"folioCliente\":\"" + folioCliente + "\", \"fecha\":\"" + fecha
				+ "\", \"observaciones\":\"" + observaciones + "\", \"numeroCliente\":\"" + numeroCliente
				+ "\", \"nombreCliente\":\"" + nombreCliente + "\", \"productoDescripcion\":\"" + productoDescripcion
				+ "\", \"cantidadCobro\":\"" + cantidadCobro + "\", \"cantidadTotal\":\"" + cantidadTotal
				+ "\", \"unidadManejo\":\"" + unidadManejo + "\", \"servicio\":\"" + servicio + "\", \"tipoCobro\":\""
				+ tipoCobro + "\", \"servicioCantidad\":\"" + servicioCantidad + "\"}";
	}
}
