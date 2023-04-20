package mx.com.ferbo.controller;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import mx.com.ferbo.model.Aviso;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ClienteDomicilios;
import mx.com.ferbo.model.Domicilios;
import mx.com.ferbo.model.MedioPago;
import mx.com.ferbo.model.MetodoPago;
import mx.com.ferbo.model.Parametro;
import mx.com.ferbo.model.Planta;

@Named
@ViewScoped
public class FacturaServiciosBean implements Serializable{

	private static final long serialVersionUID = 3500528042991256313L;
	private Cliente clienteSelect;
	private Domicilios domicilioSelect;
	private Planta plantaSelect;
	private MetodoPago metodoPagoSelect;
	private MedioPago medioPagoSelect;
	private Parametro iva,retencion;
	
	private Date fechaFactura;
	private Date fechaCorte;
	private String moneda = "MX$";
	private int plazoSelect;
	
	private List<Cliente> listaCliente;
	private List<ClienteDomicilios> listaClienteDom;//recupera datos de la tabla cliente-domicilio
	private List<ClienteDomicilios> listaClienteDomicilio;
	private List<Planta> listaPlanta;
	private List<Aviso> listaA;
	private List<Aviso> listaAviso;
	private List<MetodoPago> listaMetodoPago;
	private List<MedioPago> listaMedioPago;
	
	
	
	
	
	public FacturaServiciosBean() {
		
	}
	public Cliente getClienteSelect() {
		return clienteSelect;
	}
	public void setClienteSelect(Cliente clienteSelect) {
		this.clienteSelect = clienteSelect;
	}
	public Domicilios getDomicilioSelect() {
		return domicilioSelect;
	}
	public void setDomicilioSelect(Domicilios domicilioSelect) {
		this.domicilioSelect = domicilioSelect;
	}
	public Planta getPlantaSelect() {
		return plantaSelect;
	}
	public void setPlantaSelect(Planta plantaSelect) {
		this.plantaSelect = plantaSelect;
	}
	public MetodoPago getMetodoPagoSelect() {
		return metodoPagoSelect;
	}
	public void setMetodoPagoSelect(MetodoPago metodoPagoSelect) {
		this.metodoPagoSelect = metodoPagoSelect;
	}
	public MedioPago getMedioPagoSelect() {
		return medioPagoSelect;
	}
	public void setMedioPagoSelect(MedioPago medioPagoSelect) {
		this.medioPagoSelect = medioPagoSelect;
	}
	public Parametro getIva() {
		return iva;
	}
	public void setIva(Parametro iva) {
		this.iva = iva;
	}
	public Parametro getRetencion() {
		return retencion;
	}
	public void setRetencion(Parametro retencion) {
		this.retencion = retencion;
	}
	public Date getFechaFactura() {
		return fechaFactura;
	}
	public void setFechaFactura(Date fechaFactura) {
		this.fechaFactura = fechaFactura;
	}
	public Date getFechaCorte() {
		return fechaCorte;
	}
	public void setFechaCorte(Date fechaCorte) {
		this.fechaCorte = fechaCorte;
	}
	public String getMoneda() {
		return moneda;
	}
	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}
	public int getPlazoSelect() {
		return plazoSelect;
	}
	public void setPlazoSelect(int plazoSelect) {
		this.plazoSelect = plazoSelect;
	}
	public List<Cliente> getListaCliente() {
		return listaCliente;
	}
	public void setListaCliente(List<Cliente> listaCliente) {
		this.listaCliente = listaCliente;
	}
	public List<ClienteDomicilios> getListaClienteDom() {
		return listaClienteDom;
	}
	public void setListaClienteDom(List<ClienteDomicilios> listaClienteDom) {
		this.listaClienteDom = listaClienteDom;
	}
	public List<ClienteDomicilios> getListaClienteDomicilio() {
		return listaClienteDomicilio;
	}
	public void setListaClienteDomicilio(List<ClienteDomicilios> listaClienteDomicilio) {
		this.listaClienteDomicilio = listaClienteDomicilio;
	}
	public List<Planta> getListaPlanta() {
		return listaPlanta;
	}
	public void setListaPlanta(List<Planta> listaPlanta) {
		this.listaPlanta = listaPlanta;
	}
	public List<Aviso> getListaA() {
		return listaA;
	}
	public void setListaA(List<Aviso> listaA) {
		this.listaA = listaA;
	}
	public List<Aviso> getListaAviso() {
		return listaAviso;
	}
	public void setListaAviso(List<Aviso> listaAviso) {
		this.listaAviso = listaAviso;
	}
	public List<MetodoPago> getListaMetodoPago() {
		return listaMetodoPago;
	}
	public void setListaMetodoPago(List<MetodoPago> listaMetodoPago) {
		this.listaMetodoPago = listaMetodoPago;
	}
	public List<MedioPago> getListaMedioPago() {
		return listaMedioPago;
	}
	public void setListaMedioPago(List<MedioPago> listaMedioPago) {
		this.listaMedioPago = listaMedioPago;
	}
	
	
	
	
	
	
}
