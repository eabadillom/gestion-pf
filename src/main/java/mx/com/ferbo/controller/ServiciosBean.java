package mx.com.ferbo.controller;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.jfree.util.Log;
import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.ClaveUnidadDAO;
import mx.com.ferbo.dao.ServicioDAO;
import mx.com.ferbo.dao.TipoCobroDAO;
import mx.com.ferbo.facturacion.facturama.FacturamaBL;
import mx.com.ferbo.facturacion.facturama.Product;
import mx.com.ferbo.facturacion.facturama.ProductTax;
import mx.com.ferbo.facturacion.facturama.response.ProductRsp;
import mx.com.ferbo.model.ClaveUnidad;
import mx.com.ferbo.model.Servicio;
import mx.com.ferbo.model.TipoCobro;
import mx.com.ferbo.ui.ServicioUI;


@Named
@ViewScoped
public class ServiciosBean implements Serializable{

	private static final long serialVersionUID = -4777843305394525276L;

	private List<ServicioUI> servicios;

	private List<ServicioUI> selectedServicios;
	
	private List<TipoCobro> listadoTipoCobro;
	private List<ClaveUnidad> listadoUnidades;

	private ServicioUI selectedServicio;
	
	private ServicioDAO servicioDAO;
	private TipoCobroDAO tipoCobroDAO;
	private ClaveUnidadDAO claveUnidadDAO;
	
	String unitCode,identificador,name,cdProducto;
	
	
	public ServiciosBean() {
		servicioDAO = new ServicioDAO();
		tipoCobroDAO = new TipoCobroDAO();
		claveUnidadDAO = new ClaveUnidadDAO();
		selectedServicios = new ArrayList<>();
	}
	
	@PostConstruct
	public void init() {
		List<Servicio> servicios = servicioDAO.buscarTodos();
		listadoTipoCobro = tipoCobroDAO.buscarTodos();
		listadoUnidades = claveUnidadDAO.buscarTodos();
		
		if(this.servicios == null) {
			this.servicios = new ArrayList<>();
		}
		for(Servicio s:servicios){
			ServicioUI servicioui = new ServicioUI(s);
			this.servicios.add(servicioui);
		}
	}
	
	public void openNew() {
		this.selectedServicio = new ServicioUI();
	}

	public void saveServicio(){
		Servicio servicio = new Servicio();
		
		servicio.setCdUnidad(selectedServicio.getCdUnidad());
		servicio.setServicioCod(selectedServicio.getServicioCod());//colocar en codigo producto directamente en el dialogo el dato: 26121661 para permitir el ingreso del json a facturama
		servicio.setServicioCve(selectedServicio.getServicioCve());
		servicio.setServicioDs(selectedServicio.getServicioDs());
		servicio.setUuId(selectedServicio.getUuId());
		servicio.setCobro(selectedServicio.getCobro());
		
		for(ClaveUnidad cn: listadoUnidades) {
			if(cn.getcdUnidad().equals(selectedServicio.getCdUnidad())) {
				servicio.setClaveUnit(cn);
			}
		}
		
		
		if (this.selectedServicio.getServicioCve() == null) {
			
			if (servicioDAO.guardar(servicio) == null) {
				
				//INICIA FACTURAMA 
				
				ProductTax Ptaxe = new ProductTax();//Producto facturama
				ProductRsp productRsp = new ProductRsp();//Producto registrado
				Product producto = new Product();
				
				producto.setUnit(servicio.getClaveUnit().getNbUnidad());//UNIT (clave unidad-nombre pendiente)
				producto.setUnitCode(servicio.getCdUnidad());//uni_code (cdUnidad)
				producto.setIdentificationNumber("");//identificador
				producto.setName(servicio.getServicioDs());//Nombre (servicioDS)
				producto.setDescription(servicio.getServicioDs());//Descripcion (servicioDS)
				producto.setPrice(new BigDecimal("1").setScale(2));//(pendiente)
				producto.setCodeProdServ(servicio.getServicioCod());//Codigo Producto (servicioCod)
				
				List<ProductTax> listaTaxes = new ArrayList<>();//Taxes
				Ptaxe.setName("IVA");//name
				Ptaxe.setRate(new BigDecimal("0.16").setScale(2));//rate
				Ptaxe.setIsRetention(false);//Isretentin
				Ptaxe.setIsFederalTax(true);//IsfederalTax
				listaTaxes.add(Ptaxe);
				
				producto.setTaxes(listaTaxes);
				
				FacturamaBL facturama = new FacturamaBL();
				productRsp = facturama.registra(producto);//ERROR
				Log.debug("El producto registrado es:" + productRsp);
				
				//TERMINA FACTURAMA
				
				selectedServicio = new ServicioUI(servicio);
				this.servicios.add(this.selectedServicio);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Servicio Agregado"));
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error" ,"Ocurrió un error al intentar guardar el Servicio"));
			}

		} else {
			if (servicioDAO.actualizar(servicio) == null) {
				
				
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Servicio Actualizado"));
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error" ,"Ocurrió un error al intentar actualizar el Servicio"));
			}
		}

		PrimeFaces.current().executeScript("PF('manageServicioDialog').hide()");
		PrimeFaces.current().ajax().update("form:messages", "form:dt-servicios");
	}

	public void deleteServicio() {
		Servicio servicio = new Servicio();
		
		servicio.setCdUnidad(selectedServicio.getCdUnidad());
		servicio.setServicioCod(selectedServicio.getServicioCod());
		servicio.setServicioCve(selectedServicio.getServicioCve());
		servicio.setServicioDs(selectedServicio.getServicioDs());
		servicio.setUuId(selectedServicio.getUuId());
		servicio.setCobro(selectedServicio.getCobro());
		//servicio.setClaveUnit(selectedServicio.getClaveUnit());
		
		if (servicioDAO.eliminar(servicio) == null) {
			this.servicios.remove(this.selectedServicio);
			this.selectedServicio = null;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Servicio Eliminado"));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-servicios");
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error" ,"Ocurrió un error al intentar eliminar el Servicio"));
			PrimeFaces.current().ajax().update("form:messages");
		}
	}

	public String getDeleteButtonMessage() {
		if (hasSelectedServicios()) {
			int size = this.selectedServicios.size();
			return size > 1 ? size + " servicios seleccionados" : "1 servicio seleccionado";
		}

		return "Eliminar";
	}

	public boolean hasSelectedServicios() {
		return this.selectedServicios != null && !this.selectedServicios.isEmpty();
	}

	public void deleteSelectedServicios() {
		List<Servicio> listaservicio = new ArrayList<>();
		for(ServicioUI s:selectedServicios) {
			Servicio servicio = new Servicio();
			servicio.setCdUnidad(s.getCdUnidad());
			servicio.setServicioCod(s.getServicioCod());
			servicio.setServicioCve(s.getServicioCve());
			servicio.setServicioDs(s.getServicioDs());
			servicio.setUuId(s.getUuId());
			servicio.setCobro(s.getCobro());
			//servicio.setClaveUnit(s.getClaveUnit());
			listaservicio.add(servicio);
		}
		
		if (servicioDAO.eliminarListado(listaservicio) == null) {
			this.servicios.removeAll(this.selectedServicios);
			this.selectedServicios = null;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Servicios Eliminados"));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-servicios");
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error" ,"Ocurrió un error al intentar eliminar los Servicios"));
			PrimeFaces.current().ajax().update("form:messages");
		}
	}
	

	public List<ServicioUI> getServicios() {
		return servicios;
	}

	public void setServicios(List<ServicioUI> servicios) {
		this.servicios = servicios;
	}

	public ServicioUI getSelectedServicio() {
		return selectedServicio;
	}

	public void setSelectedServicio(ServicioUI selectedServicio) {
		this.selectedServicio = selectedServicio;
	}

	public List<ServicioUI> getSelectedServicios() {
		return selectedServicios;
	}

	public void setSelectedServicios(List<ServicioUI> selectedServicios) {
		this.selectedServicios = selectedServicios;
	}

	public List<TipoCobro> getListadoTipoCobro() {
		return listadoTipoCobro;
	}

	public void setListadoTipoCobro(List<TipoCobro> listadoTipoCobro) {
		this.listadoTipoCobro = listadoTipoCobro;
	}
	
	

}
