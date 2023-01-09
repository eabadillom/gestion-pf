package mx.com.ferbo.ui;

import java.util.List;

import mx.com.ferbo.model.ConstanciaDepositoDetalle;
import mx.com.ferbo.model.ConstanciaServicioDetalle;
import mx.com.ferbo.model.CuotaMensualServicio;
import mx.com.ferbo.model.DetalleConstanciaServicios;
import mx.com.ferbo.model.PrecioServicio;
import mx.com.ferbo.model.Servicio;
import mx.com.ferbo.model.TipoCobro;

public class ServicioUI {
	
	private Integer servicioCve;
	private String servicioDs;
	private String servicioCod;
	private String cdUnidad;
	private String uuId;
	private boolean valor;
	
	private List<DetalleConstanciaServicios> detalleConstanciaServiciosList;
	private TipoCobro cobro;
	
	private List<CuotaMensualServicio> cuotaMensualServicioList;
	private List<PrecioServicio> precioServicioList;
	private List<ConstanciaDepositoDetalle> constanciaDepositoDetalleList;
	private List<ConstanciaServicioDetalle> constanciaServicioDetalleList;
    
    
    public ServicioUI() {
    	
    }
    
	public ServicioUI(Servicio servicio) {
		this.servicioCve = servicio.getServicioCve();
		this.servicioDs = servicio.getServicioDs();
		this.servicioCod = servicio.getServicioCod();
		this.setCdUnidad(servicio.getCdUnidad());
		this.setUuId(servicio.getUuId());
		this.cobro = servicio.getCobro();
		this.cuotaMensualServicioList = servicio.getCuotaMensualServicioList();
		this.precioServicioList = servicio.getPrecioServicioList();
		this.constanciaDepositoDetalleList = servicio.getConstanciaDepositoDetalleList();
		this.constanciaServicioDetalleList = servicio.getConstanciaServicioDetalleList();
		
		if(this.uuId == null || this.uuId.equalsIgnoreCase("")) {
			valor = false;
			
		}else {
			valor = true;
		}
		
	}
	
	
	public Integer getServicioCve() {
		return servicioCve;
	}
	public void setServicioCve(Integer servicioCve) {
		this.servicioCve = servicioCve;
	}
	
	
	public String getServicioDs() {
		return servicioDs;
	}
	public void setServicioDs(String servicioDs) {
		this.servicioDs = servicioDs;
	}
	
	
	public String getServicioCod() {
		return servicioCod;
	}
	public void setServicioCod(String servicioCod) {
		this.servicioCod = servicioCod;
	}
	
	
	public String getCdUnidad() {
		return cdUnidad;
	}
	public void setCdUnidad(String cdUnidad) {
		this.cdUnidad = cdUnidad;
	}
	
	
	public String getUuId() {
		return uuId;
	}
	public void setUuId(String uuId) {
		this.uuId = uuId;
	}
	
	
	public List<DetalleConstanciaServicios> getDetalleConstanciaServiciosList() {
		return detalleConstanciaServiciosList;
	}
	public void setDetalleConstanciaServiciosList(List<DetalleConstanciaServicios> detalleConstanciaServiciosList) {
		this.detalleConstanciaServiciosList = detalleConstanciaServiciosList;
	}
	
	
	public TipoCobro getCobro() {
		return cobro;
	}
	public void setCobro(TipoCobro cobro) {
		this.cobro = cobro;
	}
	
	
	public List<CuotaMensualServicio> getCuotaMensualServicioList() {
		return cuotaMensualServicioList;
	}
	public void setCuotaMensualServicioList(List<CuotaMensualServicio> cuotaMensualServicioList) {
		this.cuotaMensualServicioList = cuotaMensualServicioList;
	}
	
	
	public List<PrecioServicio> getPrecioServicioList() {
		return precioServicioList;
	}
	public void setPrecioServicioList(List<PrecioServicio> precioServicioList) {
		this.precioServicioList = precioServicioList;
	}
	
	
	public List<ConstanciaDepositoDetalle> getConstanciaDepositoDetalleList() {
		return constanciaDepositoDetalleList;
	}
	public void setConstanciaDepositoDetalleList(List<ConstanciaDepositoDetalle> constanciaDepositoDetalleList) {
		this.constanciaDepositoDetalleList = constanciaDepositoDetalleList;
	}
	
	
	public List<ConstanciaServicioDetalle> getConstanciaServicioDetalleList() {
		return constanciaServicioDetalleList;
	}
	public void setConstanciaServicioDetalleList(List<ConstanciaServicioDetalle> constanciaServicioDetalleList) {
		this.constanciaServicioDetalleList = constanciaServicioDetalleList;
	}
	
	public boolean isValor() {
		return valor;
	}
	public void setValor(boolean valor) {
		this.valor = valor;
	}
    
    

}
