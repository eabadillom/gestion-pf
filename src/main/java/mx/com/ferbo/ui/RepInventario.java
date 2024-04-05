package mx.com.ferbo.ui;

import java.math.BigDecimal;
import java.util.Date;

public class RepInventario {
	private Integer folio = null;
	private String nombreCliente = null;
	private String numeroCliente = null;
	private String folioCliente = null;
	private Date ingreso = null;
	private Integer idPartida = null;
	private BigDecimal cantidad = null;
	private String unidadCobro = null;
	private BigDecimal tarima = null;
	private BigDecimal peso = null;
	private Integer idProducto = null;
	private String productoDescripcion = null;
	private Date caducidad = null;
	private String codigo = null;
	private String sap = null;
	private String po = null;
	private Integer idCamara = null;
	private String camaraDescripcion = null;
	private Integer idPlanta = null;
	private String plantaDescripcion = null;
	private String lote = null;
	private BigDecimal valor = null;
	private BigDecimal baseCargo = null;
	private BigDecimal resultado = null;
	private String posicionCodigo = null;

	public Integer getFolio() {
		return folio;
	}

	public void setFolio(Integer folio) {
		this.folio = folio;
	}

	public String getNombreCliente() {
		return nombreCliente;
	}

	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}

	public String getNumeroCliente() {
		return numeroCliente;
	}

	public void setNumeroCliente(String numeroCliente) {
		this.numeroCliente = numeroCliente;
	}

	public String getFolioCliente() {
		return folioCliente;
	}

	public void setFolioCliente(String folioCliente) {
		this.folioCliente = folioCliente;
	}

	public Date getIngreso() {
		return ingreso;
	}

	public void setIngreso(Date ingreso) {
		this.ingreso = ingreso;
	}

	public Integer getIdPartida() {
		return idPartida;
	}

	public void setIdPartida(Integer idPartida) {
		this.idPartida = idPartida;
	}

	public BigDecimal getCantidad() {
		return cantidad;
	}

	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}

	public String getUnidadCobro() {
		return unidadCobro;
	}

	public void setUnidadCobro(String unidadCobro) {
		this.unidadCobro = unidadCobro;
	}

	public BigDecimal getTarima() {
		return tarima;
	}

	public void setTarima(BigDecimal tarima) {
		this.tarima = tarima;
	}

	public BigDecimal getPeso() {
		return peso;
	}

	public void setPeso(BigDecimal peso) {
		this.peso = peso;
	}

	public Integer getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(Integer idProducto) {
		this.idProducto = idProducto;
	}

	public String getProductoDescripcion() {
		return productoDescripcion;
	}

	public void setProductoDescripcion(String productoDescripcion) {
		this.productoDescripcion = productoDescripcion;
	}

	public Date getCaducidad() {
		return caducidad;
	}

	public void setCaducidad(Date caducidad) {
		this.caducidad = caducidad;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getSap() {
		return sap;
	}

	public void setSap(String sap) {
		this.sap = sap;
	}

	public String getPo() {
		return po;
	}

	public void setPo(String po) {
		this.po = po;
	}

	public Integer getIdCamara() {
		return idCamara;
	}

	public void setIdCamara(Integer idCamara) {
		this.idCamara = idCamara;
	}

	public String getCamaraDescripcion() {
		return camaraDescripcion;
	}

	public void setCamaraDescripcion(String camaraDescripcion) {
		this.camaraDescripcion = camaraDescripcion;
	}

	public Integer getIdPlanta() {
		return idPlanta;
	}

	public void setIdPlanta(Integer idPlanta) {
		this.idPlanta = idPlanta;
	}

	public String getPlantaDescripcion() {
		return plantaDescripcion;
	}

	public void setPlantaDescripcion(String plantaDescripcion) {
		this.plantaDescripcion = plantaDescripcion;
	}

	public String getLote() {
		return lote;
	}

	public void setLote(String lote) {
		this.lote = lote;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public BigDecimal getBaseCargo() {
		return baseCargo;
	}

	public void setBaseCargo(BigDecimal baseCargo) {
		this.baseCargo = baseCargo;
	}

	public BigDecimal getResultado() {
		return resultado;
	}

	public void setResultado(BigDecimal resultado) {
		this.resultado = resultado;
	}

	public String getPosicionCodigo() {
		return posicionCodigo;
	}

	public void setPosicionCodigo(String posicionCodigo) {
		this.posicionCodigo = posicionCodigo;
	}

	@Override
	public String toString() {
		return "{\"folio\":\"" + folio + "\", \"nombreCliente\":\"" + nombreCliente + "\", \"numeroCliente\":\""
				+ numeroCliente + "\", \"folioCliente\":\"" + folioCliente + "\", \"ingreso\":\"" + ingreso
				+ "\", \"idPartida\":\"" + idPartida + "\", \"cantidad\":\"" + cantidad + "\", \"unidadCobro\":\""
				+ unidadCobro + "\", \"tarima\":\"" + tarima + "\", \"peso\":\"" + peso + "\", \"idProducto\":\""
				+ idProducto + "\", \"productoDescripcion\":\"" + productoDescripcion + "\", \"caducidad\":\""
				+ caducidad + "\", \"codigo\":\"" + codigo + "\", \"sap\":\"" + sap + "\", \"po\":\"" + po
				+ "\", \"idCamara\":\"" + idCamara + "\", \"camaraDescripcion\":\"" + camaraDescripcion
				+ "\", \"idPlanta\":\"" + idPlanta + "\", \"plantaDescripcion\":\"" + plantaDescripcion
				+ "\", \"lote\":\"" + lote + "\", \"valor\":\"" + valor + "\", \"baseCargo\":\"" + baseCargo
				+ "\", \"resultado\":\"" + resultado + "\", \"posicionCodigo\":\"" + posicionCodigo + "\"}";
	}
}
