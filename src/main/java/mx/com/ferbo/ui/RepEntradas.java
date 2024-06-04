package mx.com.ferbo.ui;

import java.math.BigDecimal;
import java.util.Date;

public class RepEntradas {
	String folioCliente = null;
	Date fechaIngreso = null;
	String numeroCliente = null;
	String nombreCliente = null;
	String productoDescripcion = null;
	BigDecimal pesoTotal = null;
	Integer cantidadTotal = null;
	BigDecimal valorMercancia = null;
	String dtpCodigo = null;
	String dtpSap = null;
	String dtpLote = null;
	String dtpPo = null;
	Date dtpCaducidad = null;
	String dtpTarimas = null;
	Integer cantidadUnidadManejo = null;
	String unidadManejoDescripcion = null;
	Integer idPlanta = null;
	String nombrePlanta = null;
	Integer idCamara = null;
	String nombreCamara = null;
	
	public String getFolioCliente() {
		return folioCliente;
	}
	public void setFolioCliente(String folioCliente) {
		this.folioCliente = folioCliente;
	}
	public Date getFechaIngreso() {
		return fechaIngreso;
	}
	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
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
	public BigDecimal getPesoTotal() {
		return pesoTotal;
	}
	public void setPesoTotal(BigDecimal pesoTotal) {
		this.pesoTotal = pesoTotal;
	}
	public Integer getCantidadTotal() {
		return cantidadTotal;
	}
	public void setCantidadTotal(Integer cantidadTotal) {
		this.cantidadTotal = cantidadTotal;
	}
	public BigDecimal getValorMercancia() {
		return valorMercancia;
	}
	public void setValorMercancia(BigDecimal valorMercancia) {
		this.valorMercancia = valorMercancia;
	}
	public String getDtpCodigo() {
		return dtpCodigo;
	}
	public void setDtpCodigo(String dtpCodigo) {
		this.dtpCodigo = dtpCodigo;
	}
	public String getDtpSap() {
		return dtpSap;
	}
	public void setDtpSap(String dtpSap) {
		this.dtpSap = dtpSap;
	}
	public String getDtpLote() {
		return dtpLote;
	}
	public void setDtpLote(String dtpLote) {
		this.dtpLote = dtpLote;
	}
	public String getDtpPo() {
		return dtpPo;
	}
	public void setDtpPo(String dtpPo) {
		this.dtpPo = dtpPo;
	}
	public Date getDtpCaducidad() {
		return dtpCaducidad;
	}
	public void setDtpCaducidad(Date dtpCaducidad) {
		this.dtpCaducidad = dtpCaducidad;
	}
	public String getDtpTarimas() {
		return dtpTarimas;
	}
	public void setDtpTarimas(String dtpTarimas) {
		this.dtpTarimas = dtpTarimas;
	}
	public Integer getCantidadUnidadManejo() {
		return cantidadUnidadManejo;
	}
	public void setCantidadUnidadManejo(Integer cantidadUnidadManejo) {
		this.cantidadUnidadManejo = cantidadUnidadManejo;
	}
	public String getUnidadManejoDescripcion() {
		return unidadManejoDescripcion;
	}
	public void setUnidadManejoDescripcion(String unidadManejoDescripcion) {
		this.unidadManejoDescripcion = unidadManejoDescripcion;
	}
	public Integer getIdPlanta() {
		return idPlanta;
	}
	public void setIdPlanta(Integer idPlanta) {
		this.idPlanta = idPlanta;
	}
	public String getNombrePlanta() {
		return nombrePlanta;
	}
	public void setNombrePlanta(String nombrePlanta) {
		this.nombrePlanta = nombrePlanta;
	}
	public Integer getIdCamara() {
		return idCamara;
	}
	public void setIdCamara(Integer idCamara) {
		this.idCamara = idCamara;
	}
	public String getNombreCamara() {
		return nombreCamara;
	}
	public void setNombreCamara(String nombreCamara) {
		this.nombreCamara = nombreCamara;
	}
	@Override
	public String toString() {
		return "{\"folioCliente\":\"" + folioCliente + "\", \"fechaIngreso\":\"" + fechaIngreso
				+ "\", \"numeroCliente\":\"" + numeroCliente + "\", \"nombreCliente\":\"" + nombreCliente
				+ "\", \"productoDescripcion\":\"" + productoDescripcion + "\", \"pesoTotal\":\"" + pesoTotal
				+ "\", \"cantidadTotal\":\"" + cantidadTotal + "\", \"valorMercancia\":\"" + valorMercancia
				+ "\", \"dtpCodigo\":\"" + dtpCodigo + "\", \"dtpSap\":\"" + dtpSap + "\", \"dtpLote\":\"" + dtpLote
				+ "\", \"dtpPo\":\"" + dtpPo + "\", \"dtpCaducidad\":\"" + dtpCaducidad + "\", \"dtpTarimas\":\""
				+ dtpTarimas + "\", \"cantidadUnidadManejo\":\"" + cantidadUnidadManejo
				+ "\", \"unidadManejoDescripcion\":\"" + unidadManejoDescripcion + "\", \"idPlanta\":\"" + idPlanta
				+ "\", \"nombrePlanta\":\"" + nombrePlanta + "\", \"idCamara\":\"" + idCamara
				+ "\", \"nombreCamara\":\"" + nombreCamara + "\"}";
	}
}
