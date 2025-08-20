package mx.com.ferbo.ui;

import java.util.List;

import mx.com.ferbo.model.ClaveUnidad;
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
	private ClaveUnidad claveUnit;
	
	private List<PrecioServicio> precioServicioList;
    
    
    public ServicioUI() {
    	
    }
    
	public ServicioUI(Servicio servicio) {
		this.servicioCve = servicio.getServicioCve();
		this.servicioDs = servicio.getServicioDs();
		this.servicioCod = servicio.getServicioCod();
		this.setCdUnidad(servicio.getCdUnidad());
		this.setUuId(servicio.getUuId());
		this.cobro = servicio.getCobro();
		this.claveUnit = servicio.getClaveUnit();
		this.precioServicioList = servicio.getPrecioServicioList();
		
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
	
	
	public ClaveUnidad getClaveUnit() {
		return claveUnit;
	}

	public void setClaveUnit(ClaveUnidad claveUnit) {
		this.claveUnit = claveUnit;
	}

	public List<PrecioServicio> getPrecioServicioList() {
		return precioServicioList;
	}
	public void setPrecioServicioList(List<PrecioServicio> precioServicioList) {
		this.precioServicioList = precioServicioList;
	}
	
	public boolean isValor() {
		return valor;
	}
	public void setValor(boolean valor) {
		this.valor = valor;
	}
}
