package mx.com.ferbo.model;

import java.math.BigDecimal;
import java.util.Date;

public class Inventario {
	Integer folio;
	Producto Producto;
	Integer Cantidad;
	UnidadDeManejo Unidad_Manejo;
	Integer Planta;
	Date Caducidad;
	String codigo;
	String lote;
	String SAP;
	Integer Camara;
	String posicion;
	Integer partidaCve;
	BigDecimal peso;
	String inventarioCve;
	String detalle_ant;
	Integer detallePartidaAnterior;
	Integer detallePadre;
	Integer detallePartidaPadre;
	String PO;
	String MP;
	String pedimento;
	String tarimas;
	
	
	public Inventario() {
		
	}
	
	public Inventario(Integer folio, Producto producto, Integer cantidad, UnidadDeManejo unidad_Manejo, Integer planta_Origen,
			Date caducidad, String codigo, String lote, String sAP, String planta,
			Integer camara, String posicion, Integer partidaCve, BigDecimal peso, String inventarioCve,
			String detalle_ant, Integer detallePartidaAnterior, Integer detallePadre, Integer detallePartidaPadre,
			String pO, String mP, String pedimento, String tarimas) {
		super();
		this.folio = folio;
		this.Producto = producto;
		this.Cantidad = cantidad;
		this.Unidad_Manejo = unidad_Manejo;
		this.Planta = planta_Origen;
		this.Caducidad = caducidad;
		this.codigo = codigo;
		this.lote = lote;
		this.SAP = sAP;
		this.Camara = camara;
		this.posicion = posicion;
		this.partidaCve = partidaCve;
		this.peso = peso;
		this.inventarioCve = inventarioCve;
		this.detalle_ant = detalle_ant;
		this.detallePartidaAnterior = detallePartidaAnterior;
		this.detallePadre = detallePadre;
		this.detallePartidaPadre = detallePartidaPadre;
		this.PO = pO;
		this.MP = mP;
		this.pedimento = pedimento;
		this.tarimas = tarimas;
	}
	public Integer getPartidaCve() {
		return partidaCve;
	}
	public void setPartidaCve(Integer partidaCve) {
		this.partidaCve = partidaCve;
	}
	public Integer getFolio() {
		return folio;
	}
	public void setFolio(Integer folio) {
		this.folio = folio;
	}
	public Producto getProducto() {
		return Producto;
	}
	public void setProducto(Producto producto) {
		Producto = producto;
	}
	public Integer getCantidad() {
		return Cantidad;
	}
	public void setCantidad(Integer cantidad) {
		Cantidad = cantidad;
	}
	public UnidadDeManejo getUnidad_Manejo() {
		return Unidad_Manejo;
	}
	public void setUnidad_Manejo(UnidadDeManejo unidad_Manejo) {
		Unidad_Manejo = unidad_Manejo;
	}
	public Integer getPlanta_Origen() {
		return Planta;
	}
	public void setPlanta(Integer planta_Origen) {
		Planta = planta_Origen;
	}
	public Date getCaducidad() {
		return Caducidad;
	}
	public void setCaducidad(Date caducidad) {
		Caducidad = caducidad;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getLote() {
		return lote;
	}
	public void setLote(String lote) {
		this.lote = lote;
	}
	public String getSAP() {
		return SAP;
	}
	public void setSAP(String sAP) {
		SAP = sAP;
	}
	public Integer getCamara() {
		return Camara;
	}
	public void setCamara(Integer camara) {
		Camara = camara;
	}
	public String getPosicion() {
		return posicion;
	}
	public void setPosicion(String posicion) {
		this.posicion = posicion;
	}
	public BigDecimal getPeso() {
		return peso;
	}
	public void setPeso(BigDecimal peso) {
		this.peso = peso;
	}
	public String getInventarioCve() {
		return inventarioCve;
	}
	public void setInventarioCve(String inventarioCve) {
		this.inventarioCve = inventarioCve;
	}
	public String getDetalle_ant() {
		return detalle_ant;
	}
	public void setDetalle_ant(String detalle_ant) {
		this.detalle_ant = detalle_ant;
	}
	public Integer getDetallePartidaAnterior() {
		return detallePartidaAnterior;
	}
	public void setDetallePartidaAnterior(Integer detallePartidaAnterior) {
		this.detallePartidaAnterior = detallePartidaAnterior;
	}
	public Integer getDetallePadre() {
		return detallePadre;
	}
	public void setDetallePadre(Integer detallePadre) {
		this.detallePadre = detallePadre;
	}
	public Integer getDetallePartidaPadre() {
		return detallePartidaPadre;
	}
	public void setDetallePartidaPadre(Integer detallePartidaPadre) {
		this.detallePartidaPadre = detallePartidaPadre;
	}
	public String getPO() {
		return PO;
	}
	public void setPO(String pO) {
		PO = pO;
	}
	public String getMP() {
		return MP;
	}
	public void setMP(String mP) {
		MP = mP;
	}
	public String getPedimento() {
		return pedimento;
	}
	public void setPedimento(String pedimento) {
		this.pedimento = pedimento;
	}
	public String getTarimas() {
		return tarimas;
	}
	public void setTarimas(String tarimas) {
		this.tarimas = tarimas;
	}
	


}
