package mx.com.ferbo.ui;

import java.math.BigDecimal;
import java.util.Date;

public class RepTraspasos {
	private String numeroCliente = null;
	private String nombreCliente = null;
	private String numero = null;
	private Date fecha = null;
	private String observacion = null;
	private String constancia = null;
	private String origen = null;
	private String destino = null;
	private BigDecimal cantidad = null;
	private String descripcion = null;
	private Integer folio = null;
	private String productoDescripcion = null;
	private String unidadDeManejo = null;

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

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public String getConstancia() {
		return constancia;
	}

	public void setConstancia(String constancia) {
		this.constancia = constancia;
	}

	public String getOrigen() {
		return origen;
	}

	public void setOrigen(String origen) {
		this.origen = origen;
	}

	public String getDestino() {
		return destino;
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}

	public BigDecimal getCantidad() {
		return cantidad;
	}

	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Integer getFolio() {
		return folio;
	}

	public void setFolio(Integer folio) {
		this.folio = folio;
	}

	public String getProductoDescripcion() {
		return productoDescripcion;
	}

	public void setProductoDescripcion(String productoDescripcion) {
		this.productoDescripcion = productoDescripcion;
	}

	public String getUnidadDeManejo() {
		return unidadDeManejo;
	}

	public void setUnidadDeManejo(String unidadDeManejo) {
		this.unidadDeManejo = unidadDeManejo;
	}

	@Override
	public String toString() {
		return "{\"numeroCliente\":\"" + numeroCliente + "\", \"nombreCliente\":\"" + nombreCliente
				+ "\", \"numero\":\"" + numero + "\", \"fecha\":\"" + fecha + "\", \"observacion\":\"" + observacion
				+ "\", \"constancia\":\"" + constancia + "\", \"origen\":\"" + origen + "\", \"destino\":\"" + destino
				+ "\", \"cantidad\":\"" + cantidad + "\", \"descripcion\":\"" + descripcion + "\", \"folio\":\"" + folio
				+ "\", \"productoDescripcion\":\"" + productoDescripcion + "\", \"unidadDeManejo\":\"" + unidadDeManejo
				+ "\"}";
	}
}
