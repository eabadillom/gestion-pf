package mx.com.ferbo.ui;

import java.math.BigDecimal;
import java.util.Date;

public class RepSalidas {

	private Date fecha = null;
	private String folioCliente = null;
	private String numero = null;
	private String producto = null;
	private String unidad = null;
	private Integer cantidad = null;
	private BigDecimal peso = null;
	private String numeroCliente = null;
	private String nombreCliente = null;
	private Date fechaIngreso = null;
	private Integer idPartida = null;
	private Integer cantidadTotal = null;
	private BigDecimal pesoTotal = null;
	private Integer idCamara = null;
	private String camara = null;
	private Integer idPlanta = null;
	private String planta = null;

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getFolioCliente() {
		return folioCliente;
	}

	public void setFolioCliente(String folioCliente) {
		this.folioCliente = folioCliente;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getProducto() {
		return producto;
	}

	public void setProducto(String producto) {
		this.producto = producto;
	}

	public String getUnidad() {
		return unidad;
	}

	public void setUnidad(String unidad) {
		this.unidad = unidad;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public BigDecimal getPeso() {
		return peso;
	}

	public void setPeso(BigDecimal peso) {
		this.peso = peso;
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

	public Date getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public Integer getIdPartida() {
		return idPartida;
	}

	public void setIdPartida(Integer idPartida) {
		this.idPartida = idPartida;
	}

	public Integer getCantidadTotal() {
		return cantidadTotal;
	}

	public void setCantidadTotal(Integer cantidadTotal) {
		this.cantidadTotal = cantidadTotal;
	}

	public BigDecimal getPesoTotal() {
		return pesoTotal;
	}

	public void setPesoTotal(BigDecimal pesoTotal) {
		this.pesoTotal = pesoTotal;
	}

	public Integer getIdCamara() {
		return idCamara;
	}

	public void setIdCamara(Integer idCamara) {
		this.idCamara = idCamara;
	}

	public String getCamara() {
		return camara;
	}

	public void setCamara(String camara) {
		this.camara = camara;
	}

	public Integer getIdPlanta() {
		return idPlanta;
	}

	public void setIdPlanta(Integer idPlanta) {
		this.idPlanta = idPlanta;
	}

	public String getPlanta() {
		return planta;
	}

	public void setPlanta(String planta) {
		this.planta = planta;
	}

	@Override
	public String toString() {
		return "{\"fecha\":\"" + fecha + "\", \"folioCliente\":\"" + folioCliente + "\", \"numero\":\"" + numero
				+ "\", \"producto\":\"" + producto + "\", \"unidad\":\"" + unidad + "\", \"cantidad\":\"" + cantidad
				+ "\", \"peso\":\"" + peso + "\", \"numeroCliente\":\"" + numeroCliente + "\", \"nombreCliente\":\""
				+ nombreCliente + "\", \"fechaIngreso\":\"" + fechaIngreso + "\", \"idPartida\":\"" + idPartida
				+ "\", \"cantidadTotal\":\"" + cantidadTotal + "\", \"pesoTotal\":\"" + pesoTotal
				+ "\", \"idCamara\":\"" + idCamara + "\", \"camara\":\"" + camara + "\", \"idPlanta\":\"" + idPlanta
				+ "\", \"planta\":\"" + planta + "\"}";
	}
}
