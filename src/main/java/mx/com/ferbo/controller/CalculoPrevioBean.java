package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import mx.com.ferbo.model.ConstanciaDeDeposito;
import mx.com.ferbo.model.ConstanciaFactura;

@Named
@ViewScoped
public class CalculoPrevioBean implements Serializable{

	private static final long serialVersionUID = -1785488265380235016L;
	
	@Inject
	private FacturacionConstanciasBean facturacionBean;
	
	private FacesContext context;
	private HttpServletRequest request;
	
	List<ConstanciaDeDeposito> listaEntradas;

	public CalculoPrevioBean() {
		
		
		
		try {
			context = FacesContext.getCurrentInstance();
			request = (HttpServletRequest) context.getExternalContext().getRequest();
			listaEntradas = (List<ConstanciaDeDeposito>) request.getSession(false).getAttribute("entradas");			
		} catch (Exception e) {
			// TODO: handle exception
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


	
	/* Metodo de prueba 
	public void verAtributos() {
		
		
		context = FacesContext.getCurrentInstance();
		request = (HttpServletRequest) context.getExternalContext().getRequest();
		listaEntradas = (List<ConstanciaDeDeposito>) request.getSession(false).getAttribute("lista");
		
	}*/
	
}
