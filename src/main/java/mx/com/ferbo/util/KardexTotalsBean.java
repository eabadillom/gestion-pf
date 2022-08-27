package mx.com.ferbo.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import mx.com.ferbo.model.ConstanciaDeDeposito;
import mx.com.ferbo.model.DetalleConstanciaSalida;
import mx.com.ferbo.model.Partida;

@Named
@ViewScoped
public class KardexTotalsBean implements Serializable {

	/**
	 * @author Juan_Cervantes
	 */
	private static final long serialVersionUID = 7564968189228115544L;
	
	/**
	 * Objetos para Detalle de Constancia de Salida
	 */
	private DetalleConstanciaSalida detalleSalidaSelected;
	private List<DetalleConstanciaSalida> listDetalleSalida;
	
	/**
	 * Objetos para salidas
	 */
	private List<Partida> listPartida;
	private Partida partSelected;
	
	/**
	 * Objetos para Constancia Deposito
	 */
	private ConstanciaDeDeposito consDepKardex;
	private List<ConstanciaDeDeposito> listConsDepKardex;
	
	/**
	 * Objetos para totales
	 */
	private Integer cantidadSalida;
	private BigDecimal pesoSalida;
	private Integer cantidadTotal;
	private BigDecimal pesoTotal;
	private String unidad;
	
	/**
	 * constructor
	 */
	public KardexTotalsBean() {
		detalleSalidaSelected = new DetalleConstanciaSalida();
		consDepKardex = new ConstanciaDeDeposito();
		listDetalleSalida = new ArrayList<>();
		listPartida = new ArrayList<>();
		listConsDepKardex = new ArrayList<>();
	}
	
	public DetalleConstanciaSalida getDetalleSalidaSelected() {
		return detalleSalidaSelected;
	}
	public void setDetalleSalidaSelected(DetalleConstanciaSalida detalleSalidaSelected) {
		this.detalleSalidaSelected = detalleSalidaSelected;
	}
	public Integer getCantidadSalida() {
		return cantidadSalida;
	}
	public void setCantidadSalida(Integer cantidadSalida) {
		this.cantidadSalida = cantidadSalida;
	}
	public BigDecimal getPesoSalida() {
		return pesoSalida;
	}
	public void setPesoSalida(BigDecimal pesoSalida) {
		this.pesoSalida = pesoSalida;
	}

	public ConstanciaDeDeposito getConsDepKardex() {
		return consDepKardex;
	}

	public void setConsDepKardex(ConstanciaDeDeposito consDepKardex) {
		this.consDepKardex = consDepKardex;
	}

	public List<DetalleConstanciaSalida> getListDetalleSalida() {
		return listDetalleSalida;
	}

	public void setListDetalleSalida(List<DetalleConstanciaSalida> listDetalleSalida) {
		this.listDetalleSalida = listDetalleSalida;
	}

	public List<Partida> getListPartida() {
		return listPartida;
	}

	public void setListPartida(List<Partida> listPartida) {
		this.listPartida = listPartida;
	}

	public List<ConstanciaDeDeposito> getListConsDepKardex() {
		return listConsDepKardex;
	}

	public void setListConsDepKardex(List<ConstanciaDeDeposito> listConsDepKardex) {
		this.listConsDepKardex = listConsDepKardex;
	}

	public Partida getPartSelected() {
		return partSelected;
	}

	public void setPartSelected(Partida partSelected) {
		this.partSelected = partSelected;
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

	public String getUnidad() {
		return unidad;
	}

	public void setUnidad(String unidad) {
		this.unidad = unidad;
	}		
	

}