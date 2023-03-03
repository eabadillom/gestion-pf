package mx.com.ferbo.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.faces.application.FacesMessage.Severity;

public class Inventario {
	protected Integer folio;
	protected Producto producto;
	protected Cliente cliente;
	protected Integer cantidad;
	protected UnidadDeManejo unidadManejo;
	protected Planta planta;
	protected Integer plantad;
	protected Date caducidad;
	protected String codigo;
	protected String lote;
	protected String sap;
	protected Camara camara;
	protected Integer camarad;
	protected Posicion posicion;
	protected Integer partidaCve;
	protected BigDecimal peso;
	protected String inventarioCve;
	protected String detalleAnt;
	protected Integer detallePartidaAnterior;
	protected Integer detallePadre;
	protected Integer detallePartidaPadre;
	protected String po;
	protected String mp;
	protected String pedimento;
	protected String tarimas;
	protected String folioCliente;
	protected String Observaciones;
	protected String descripcion;

	public Inventario() {

	}

	public void listas() {
	}

	public Inventario(Integer folio, Producto producto, Cliente cliente, Integer cantidad, UnidadDeManejo unidadManejo,
			Planta planta, Integer plantad, Date caducidad, String codigo, String lote, String sap, Camara camara,
			Integer camarad, Posicion posicion, Integer posiciond, Integer partidaCve, BigDecimal peso,
			String inventarioCve, String detalleAnt, Integer detallePartidaAnterior, Integer detallePadre,
			Integer detallePartidaPadre, String po, String mp, String pedimento, String tarimas, String folioCliente,
			String observaciones, String descripcion) {
		this.folio = folio;
		this.producto = producto;
		this.cliente = cliente;
		this.cantidad = cantidad;
		this.unidadManejo = unidadManejo;
		this.planta = planta;
		this.plantad = plantad;
		this.caducidad = caducidad;
		this.codigo = codigo;
		this.lote = lote;
		this.sap = sap;
		this.camara = camara;
		this.camarad = camarad;
		this.posicion = posicion;
		this.partidaCve = partidaCve;
		this.peso = peso;
		this.inventarioCve = inventarioCve;
		this.detalleAnt = detalleAnt;
		this.detallePartidaAnterior = detallePartidaAnterior;
		this.detallePadre = detallePadre;
		this.detallePartidaPadre = detallePartidaPadre;
		this.po = po;
		this.mp = mp;
		this.pedimento = pedimento;
		this.tarimas = tarimas;
		this.folioCliente = folioCliente;
		this.Observaciones = observaciones;
		this.descripcion = descripcion;
	}

	public Integer getFolio() {
		return folio;
	}

	public void setFolio(Integer folio) {
		this.folio = folio;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public UnidadDeManejo getUnidadManejo() {
		return unidadManejo;
	}

	public void setUnidadManejo(UnidadDeManejo unidadManejo) {
		this.unidadManejo = unidadManejo;
	}

	public Planta getPlanta() {
		return planta;
	}

	public void setPlanta(Planta planta) {
		this.planta = planta;
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

	public String getLote() {
		return lote;
	}

	public void setLote(String lote) {
		this.lote = lote;
	}

	public String getSap() {
		return sap;
	}

	public void setSap(String sap) {
		this.sap = sap;
	}

	public Camara getCamara() {
		return camara;
	}

	public void setCamara(Camara camara) {
		this.camara = camara;
	}

	public Posicion getPosicion() {
		return posicion;
	}

	public void setPosicion(Posicion posicion) {
		this.posicion = posicion;
	}

	public Integer getPartidaCve() {
		return partidaCve;
	}

	public void setPartidaCve(Integer partidaCve) {
		this.partidaCve = partidaCve;
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

	public String getDetalleAnt() {
		return detalleAnt;
	}

	public void setDetalleAnt(String detalleAnt) {
		this.detalleAnt = detalleAnt;
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

	public String getPo() {
		return po;
	}

	public void setPo(String po) {
		this.po = po;
	}

	public String getMp() {
		return mp;
	}

	public void setMp(String mp) {
		this.mp = mp;
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

	public String getFolioCliente() {
		return folioCliente;
	}

	public void setFolioCliente(String folioCliente) {
		this.folioCliente = folioCliente;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}


	public Integer getPlantad() {
		return plantad;
	}

	public void setPlantad(Integer plantad) {
		this.plantad = plantad;
	}

	public Integer getCamarad() {
		return camarad;
	}

	public void setCamarad(Integer camarad) {
		this.camarad = camarad;
	}

	public String getObservaciones() {
		return Observaciones;
	}

	public void setObservaciones(String observaciones) {
		Observaciones = observaciones;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Override
	public String toString() {
		return "Inventario [folio=" + folio + ", producto=" + producto + ", cliente=" + cliente + ", cantidad="
				+ cantidad + ", unidadManejo=" + unidadManejo + ", planta=" + planta + ", plantad=" + plantad
				+ ", caducidad=" + caducidad + ", codigo=" + codigo + ", lote=" + lote + ", sap=" + sap + ", camara="
				+ camara + ", camarad=" + camarad + ", posicion=" + posicion
				+ ", partidaCve=" + partidaCve + ", peso=" + peso + ", inventarioCve=" + inventarioCve + ", detalleAnt="
				+ detalleAnt + ", detallePartidaAnterior=" + detallePartidaAnterior + ", detallePadre=" + detallePadre
				+ ", detallePartidaPadre=" + detallePartidaPadre + ", po=" + po + ", mp=" + mp + ", pedimento="
				+ pedimento + ", tarimas=" + tarimas + ", folioCliente=" + folioCliente + "]";
	}

}
