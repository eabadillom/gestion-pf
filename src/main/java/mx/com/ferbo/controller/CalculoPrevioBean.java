package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import mx.com.ferbo.model.ConstanciaDeDeposito;
import mx.com.ferbo.model.ConstanciaFactura;
import mx.com.ferbo.model.ConstanciaFacturaDs;
@Named

public class CalculoPrevioBean implements Serializable{

	private static final long serialVersionUID = -1785488265380235016L;
	
	@Inject
	private FacturacionConstanciasBean facturacionBean;
	
	private FacesContext context;
	private HttpServletRequest request;
	
	private List<ConstanciaDeDeposito> listaEntradas;
	private List<ConstanciaFactura> listaVigencias;
	private List<ConstanciaFacturaDs> listaServicios;
	
	@SuppressWarnings("unchecked")
	public CalculoPrevioBean() {
		
		
		try {
			context = FacesContext.getCurrentInstance();
			request = (HttpServletRequest) context.getExternalContext().getRequest();
			listaEntradas = (List<ConstanciaDeDeposito>) request.getSession(false).getAttribute("entradas");
			listaVigencias = (List<ConstanciaFactura>) request.getSession(false).getAttribute("vigencias");	
			listaServicios = (List<ConstanciaFacturaDs>) request.getSession(false).getAttribute("servicios");
			
		} catch (Exception e) {
			
			System.out.println("ERROR" + e.getMessage());
		}
		
	}
	
	@PostConstruct
	public void init() {
		
	}

	public FacturacionConstanciasBean getFacturacionBean() {
		return facturacionBean;
	}

	public void setFacturacionBean(FacturacionConstanciasBean facturacionBean) {
		this.facturacionBean = facturacionBean;
	}

	public List<ConstanciaDeDeposito> getListaEntradas() {
		return listaEntradas;
	}

	public void setListaEntradas(List<ConstanciaDeDeposito> listaEntradas) {
		this.listaEntradas = listaEntradas;
	}

	public List<ConstanciaFactura> getListaVigencias() {
		return listaVigencias;
	}

	public void setListaVigencias(List<ConstanciaFactura> listaVigencias) {
		this.listaVigencias = listaVigencias;
	}

	public List<ConstanciaFacturaDs> getListaServicios() {
		return listaServicios;
	}

	public void setListaServicios(List<ConstanciaFacturaDs> listaServicios) {
		this.listaServicios = listaServicios;
	}

	public void verServicios() {
		
		System.out.println("jhb");	
	}
	

	
	/* Metodo de prueba 
	public void verAtributos() {
		
		
		context = FacesContext.getCurrentInstance();
		request = (HttpServletRequest) context.getExternalContext().getRequest();
		listaEntradas = (List<ConstanciaDeDeposito>) request.getSession(false).getAttribute("lista");
		
	}*/
	
}
