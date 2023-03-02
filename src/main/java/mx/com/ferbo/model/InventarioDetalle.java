package mx.com.ferbo.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import mx.com.ferbo.dao.CamaraDAO;
import mx.com.ferbo.dao.PlantaDAO;
import mx.com.ferbo.dao.PosicionCamaraDAO;

public class InventarioDetalle extends Inventario {
	private List<Planta> listaplanta;
	private List<Camara> listacamara;
	private List<Posicion> listaposicion;
	private Planta plantaDestino;
	private Camara camaraDestino;
	private Posicion posicionDestino;

	public Posicion getPosicionDestino() {
		return posicionDestino;
	}


	public void setPosicionDestino(Posicion posicionDestino) {
		this.posicionDestino = posicionDestino;
	}


	public Camara getCamaraDestino() {
		return camaraDestino;
	}


	public void setCamaraDestino(Camara camaraDestino) {
		this.camaraDestino = camaraDestino;
	}


	public Planta getPlantaDestino() {
		return plantaDestino;
	}


	public void setPlantaDestino(Planta plantaDestino) {
		this.plantaDestino = plantaDestino;
	}


	public InventarioDetalle() {

	}
	

	public InventarioDetalle(
			Integer folio, Producto producto, Cliente cliente, Integer cantidad, UnidadDeManejo unidadManejo,
			Planta planta, Integer plantad, Date caducidad, String codigo, String lote, String sap, Camara camara,
			Integer camarad, Posicion posicion, Integer posiciond, Integer partidaCve, BigDecimal peso,
			String inventarioCve, String detalleAnt, Integer detallePartidaAnterior, Integer detallePadre,
			Integer detallePartidaPadre, String po, String mp, String pedimento, String tarimas, String folioCliente,
			String observaciones, String descripcion, List<Planta> listaplanta, List<Camara> listacamara, List<Posicion> listaposicion) {
		
		super(folio, producto, cliente, cantidad, unidadManejo, planta, plantad, caducidad, codigo, lote, sap, camara,
				camarad, posicion, posiciond, partidaCve, peso, inventarioCve, detalleAnt, detallePartidaAnterior, detallePadre,
				detallePartidaPadre, po, mp, pedimento, tarimas, folioCliente, observaciones, descripcion);
		this.listaplanta = listaplanta;
		this.listacamara = listacamara;
		this.listaposicion = listaposicion;
	}
	
	public InventarioDetalle(Inventario inventario) {
		this.folio = inventario.getFolio();
		this.producto = inventario.getProducto();
		this.cliente = inventario.getCliente();
		this.cantidad = inventario.getCantidad();
		this.unidadManejo = inventario.getUnidadManejo();
		this.planta = inventario.getPlanta();
		this.plantad = inventario.getPlantad();
		this.caducidad = inventario.getCaducidad();
		this.codigo = inventario.getCodigo();
		this.lote = inventario.getLote();
		this.sap = inventario.getSap();
		this.camara = inventario.getCamara();
		this.camarad = inventario.getCamarad();
		this.posicion = inventario.getPosicion();
		this.posiciond = inventario.getPosiciond();
		this.partidaCve = inventario.getPartidaCve();
		this.peso = inventario.getPeso();
		this.inventarioCve = inventario.getInventarioCve();
		this.detalleAnt = inventario.getDetalleAnt();
		this.detallePartidaAnterior = inventario.getDetallePartidaAnterior();
		this.detallePadre = inventario.getDetallePadre();
		this.detallePartidaPadre = inventario.getDetallePartidaPadre();
		this.po = inventario.getPo();
		this.mp = inventario.mp;
		this.pedimento = inventario.getPedimento();
		this.tarimas = inventario.getTarimas();
		this.folioCliente = inventario.getFolioCliente();
		this.Observaciones = inventario.getObservaciones();
		this.descripcion = inventario.getDescripcion();
	}


	public List<Planta> getListaplanta() {
		return listaplanta;
	}

	public void setListaplanta(List<Planta> listaplanta) {
		this.listaplanta = listaplanta;
	}

	public List<Camara> getListacamara() {
		return listacamara;
	}

	public void setListacamara(List<Camara> listacamara) {
		this.listacamara = listacamara;
	}

	public List<Posicion> getListaposicion() {
		return listaposicion;
	}

	public void setListaposicion(List<Posicion> listaposicion) {
		this.listaposicion = listaposicion;
	}

}
