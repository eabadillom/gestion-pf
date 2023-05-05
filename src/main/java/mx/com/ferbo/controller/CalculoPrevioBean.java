package mx.com.ferbo.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.PrimeFaces;

import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ConstanciaDeDeposito;
import mx.com.ferbo.model.ConstanciaFactura;
import mx.com.ferbo.model.ConstanciaFacturaDs;
import mx.com.ferbo.model.ConstanciaServicioDetalle;
import mx.com.ferbo.model.PartidaServicio;
import mx.com.ferbo.model.PrecioServicio;
import mx.com.ferbo.model.ProductoConstanciaDs;
import mx.com.ferbo.model.ServicioConstanciaDs;
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
	private List<ConstanciaFacturaDs> listaConstanciaFacturaDs;
	
	private Cliente clienteSelect;
	
	private Date fechaEmision;
	private BigDecimal cantidad;
	
	@SuppressWarnings("unchecked")
	public CalculoPrevioBean() {
		
		listaConstanciaFacturaDs = new ArrayList<>();
		
		try {
			context = FacesContext.getCurrentInstance();
			request = (HttpServletRequest) context.getExternalContext().getRequest();
			listaEntradas = (List<ConstanciaDeDeposito>) request.getSession(false).getAttribute("entradas");
			listaVigencias = (List<ConstanciaFactura>) request.getSession(false).getAttribute("vigencias");	
			listaServicios = (List<ConstanciaFacturaDs>) request.getSession(false).getAttribute("servicios");
			clienteSelect = (Cliente) request.getSession(false).getAttribute("cliente");
			fechaEmision = (Date) request.getSession(false).getAttribute("fechaEmision");
			
			if(listaEntradas.isEmpty()) {
				listaEntradas = new ArrayList<>();
			}
			
			if(listaVigencias.isEmpty()) {
				listaVigencias = new ArrayList<>();
			}
			
			if(listaServicios.isEmpty()) {
				listaServicios = new ArrayList<>();
			}
			
			verServicios();
			PrimeFaces.current().ajax().update("form:dt-constanciaFacturaDs");
			
		} catch (Exception e) {
			
			System.out.println("ERROR" + e.getMessage());
		}
		
	}
	
	@PostConstruct
	public void init() {
		
		//fechaEmision = new Date();
		
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

	public List<ConstanciaFacturaDs> getListaConstanciaFacturaDs() {
		return listaConstanciaFacturaDs;
	}

	public void setListaConstanciaFacturaDs(List<ConstanciaFacturaDs> listaConstanciaFacturaDs) {
		this.listaConstanciaFacturaDs = listaConstanciaFacturaDs;
	}

	public Date getFechaEmision() {
		return fechaEmision;
	}

	public void setFechaEmision(Date fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	public BigDecimal getCantidad() {
		return cantidad;
	}

	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}

	public void verServicios() {
		
		BigDecimal importe = new BigDecimal(0); 		
		
		for(ConstanciaFacturaDs cfd: listaServicios) {
			
			List<ConstanciaServicioDetalle> listaConstanciaSD = cfd.getConstanciaDeServicio().getConstanciaServicioDetalleList();
			List<PartidaServicio> listaPartidaServicio = cfd.getConstanciaDeServicio().getPartidaServicioList();
			
			
			ConstanciaFacturaDs constanciaFacturaDs = new ConstanciaFacturaDs();			
			
			constanciaFacturaDs.setConstanciaDeServicio(cfd.getConstanciaDeServicio());
			
			constanciaFacturaDs.setServicioConstanciaDsList(new ArrayList<>());
			constanciaFacturaDs.setProductoConstanciaDsList(new ArrayList<>());
			
			for(ConstanciaServicioDetalle csd :listaConstanciaSD) {
				
				cantidad = new BigDecimal(0);
				
				List<PrecioServicio> listaPrecioS =  csd.getServicioCve().getPrecioServicioList();				
				PrecioServicio precioServicio = getPrecioServicio(clienteSelect.getCteCve(),listaPrecioS);				
				cantidad = precioServicio.getPrecio();//variable agregada
				
				importe = csd.getServicioCantidad().multiply(cantidad);
				
				ServicioConstanciaDs servicioConstanciaDs = new ServicioConstanciaDs();
				
				servicioConstanciaDs.setDescripcion(csd.getServicioCve().getServicioDs());
				servicioConstanciaDs.setConstancia(cfd);
				servicioConstanciaDs.setCosto(importe.setScale(2));
				servicioConstanciaDs.setTarifa(precioServicio.getPrecio().setScale(2));
				servicioConstanciaDs.setCodigo(csd.getServicioCve().getServicioCod());
				servicioConstanciaDs.setUdCobro("SRV");
				servicioConstanciaDs.setCdUnidad(csd.getServicioCve().getCdUnidad());
								
				constanciaFacturaDs.getServicioConstanciaDsList().add(servicioConstanciaDs);
			}
			
			for(PartidaServicio ps: listaPartidaServicio) {
				
				ProductoConstanciaDs productoConstanciaDs = new ProductoConstanciaDs();
				
				productoConstanciaDs.setDescripcion(ps.getProductoCve().getProductoDs());
				productoConstanciaDs.setConstancia(cfd);
				productoConstanciaDs.setCatidadCobro(ps.getCantidadDeCobro());
				productoConstanciaDs.setUnidadCobro(ps.getUnidadDeCobro().getUnidadDeManejoDs());
				productoConstanciaDs.setCantidadManejo(new BigDecimal(ps.getCantidadTotal()));
				productoConstanciaDs.setUnidadManejo(ps.getUnidadDeManejoCve().getUnidadDeManejoDs());
								
				constanciaFacturaDs.getProductoConstanciaDsList().add(productoConstanciaDs);
				
			}
			
			listaConstanciaFacturaDs.add(constanciaFacturaDs);
		}
			
	}
	
	
	
	private PrecioServicio getPrecioServicio(Integer idCliente, List<PrecioServicio> listaPrecioS) {
		
		List<PrecioServicio> listaPrecioSTemp = new ArrayList<>();
		
		listaPrecioSTemp = listaPrecioS.stream().filter(ps -> ps.getCliente().getCteCve().intValue() == idCliente)
							.collect(Collectors.toList());
		
		
		Optional<PrecioServicio> precioServicioMax = listaPrecioSTemp.stream().max((i,j) -> i.getAvisoCve().getAvisoCve().compareTo(j.getAvisoCve().getAvisoCve()));

		PrecioServicio precioServicio= precioServicioMax.get();
		
		return precioServicio;
	}
	

	
	/* Metodo de prueba 
	public void verAtributos() {
		
		
		context = FacesContext.getCurrentInstance();
		request = (HttpServletRequest) context.getExternalContext().getRequest();
		listaEntradas = (List<ConstanciaDeDeposito>) request.getSession(false).getAttribute("lista");
		
	}*/
	
}
