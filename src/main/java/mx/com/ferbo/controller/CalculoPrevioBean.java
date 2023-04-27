package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ConstanciaDeDeposito;
import mx.com.ferbo.model.ConstanciaFactura;
import mx.com.ferbo.model.ConstanciaFacturaDs;
import mx.com.ferbo.model.ConstanciaServicioDetalle;
import mx.com.ferbo.model.PrecioServicio;
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
	
	private Cliente clienteSelect;
	
	@SuppressWarnings("unchecked")
	public CalculoPrevioBean() {
		
		
		try {
			context = FacesContext.getCurrentInstance();
			request = (HttpServletRequest) context.getExternalContext().getRequest();
			listaEntradas = (List<ConstanciaDeDeposito>) request.getSession(false).getAttribute("entradas");
			listaVigencias = (List<ConstanciaFactura>) request.getSession(false).getAttribute("vigencias");	
			listaServicios = (List<ConstanciaFacturaDs>) request.getSession(false).getAttribute("servicios");
			clienteSelect = (Cliente) request.getSession(false).getAttribute("cliente");
			
			if(listaEntradas.isEmpty()) {
				listaEntradas = new ArrayList<>();
			}
			
			if(listaVigencias.isEmpty()) {
				listaVigencias = new ArrayList<>();
			}
			
			if(listaServicios.isEmpty()) {
				listaServicios = new ArrayList<>();
			}
			
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

	public Cliente getClienteSelect() {
		return clienteSelect;
	}

	public void setClienteSelect(Cliente clienteSelect) {
		this.clienteSelect = clienteSelect;
	}

	public void verServicios() {
		
		for(ConstanciaFacturaDs cfd: listaServicios) {
			List<ConstanciaServicioDetalle> listaConstanciaSD = cfd.getConstanciaDeServicio().getConstanciaServicioDetalleList();
			
			for(ConstanciaServicioDetalle csd :listaConstanciaSD) {
				System.out.println(csd.getServicioCantidad());//Cantidad de servicio
				
				List<PrecioServicio> listaPrecioS =  csd.getServicioCve().getPrecioServicioList();
				
				PrecioServicio precioServicio = getPrecioServicio(clienteSelect.getCteCve(),listaPrecioS);
				
				//System.out.println(clienteSelect);
				
			}
			
		}
		
	}
	
	private PrecioServicio getPrecioServicio(Integer idCliente, List<PrecioServicio> listaPrecioS) {
		
		List<PrecioServicio> listaPrecioServicioTemp = new ArrayList<>();
		
		for(PrecioServicio ps: listaPrecioS) {
			
			if(ps.getCliente().getCteCve().intValue()!=clienteSelect.getCteCve().intValue()){				
				listaPrecioServicioTemp.add(ps);			
			}			
		}
		
		listaPrecioS.removeAll(listaPrecioServicioTemp);
		
		return null;
	}
	

	
	/* Metodo de prueba 
	public void verAtributos() {
		
		
		context = FacesContext.getCurrentInstance();
		request = (HttpServletRequest) context.getExternalContext().getRequest();
		listaEntradas = (List<ConstanciaDeDeposito>) request.getSession(false).getAttribute("lista");
		
	}*/
	
}
