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
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import mx.com.ferbo.dao.PrecioServicioDAO;
import mx.com.ferbo.model.Aviso;
import mx.com.ferbo.model.Camara;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ConstanciaDeDeposito;
import mx.com.ferbo.model.ConstanciaDepositoDetalle;
import mx.com.ferbo.model.ConstanciaFactura;
import mx.com.ferbo.model.ConstanciaFacturaDs;
import mx.com.ferbo.model.ConstanciaServicioDetalle;
import mx.com.ferbo.model.Factura;
import mx.com.ferbo.model.Partida;
import mx.com.ferbo.model.PartidaServicio;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.PrecioServicio;
import mx.com.ferbo.model.ProductoConstanciaDs;
import mx.com.ferbo.model.Servicio;
import mx.com.ferbo.model.ServicioConstancia;
import mx.com.ferbo.model.ServicioConstanciaDs;
import mx.com.ferbo.model.TipoCobro;

@Named
@ViewScoped
public class CalculoPrevioBean implements Serializable{

	private static final long serialVersionUID = -1785488265380235016L;
	
	@Inject
	private FacturacionConstanciasBean facturacionBean;
	
	private FacesContext context;
	private HttpServletRequest request;
	
	private List<ConstanciaFactura> listaEntradas;
	private List<ConstanciaFactura> listaVigencias;
	private List<ConstanciaFacturaDs> listaServicios;
	private List<ConstanciaFacturaDs> listaConstanciaFacturaDs;
	
	private PrecioServicioDAO precioServicioDAO;
	
	private Cliente clienteSelect;
	private Planta plantaSelect;
	private Factura factura;
	
	private Date fechaEmision;
	private BigDecimal cantidad;
	
	public CalculoPrevioBean() {
		
		listaConstanciaFacturaDs = new ArrayList<>();
		listaEntradas = new ArrayList<>();
		listaServicios = new ArrayList<>();
		listaVigencias = new ArrayList<>();
		
		clienteSelect = new Cliente();
		plantaSelect = new Planta();
		factura = new Factura();
		
		precioServicioDAO = new PrecioServicioDAO();
		
	}
	
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		
		try {
			context = FacesContext.getCurrentInstance();
			request = (HttpServletRequest) context.getExternalContext().getRequest();
			listaEntradas = (List<ConstanciaFactura>) request.getSession(false).getAttribute("entradas");
			listaVigencias = (List<ConstanciaFactura>) request.getSession(false).getAttribute("vigencias");	
			listaServicios = (List<ConstanciaFacturaDs>) request.getSession(false).getAttribute("servicios");
			clienteSelect = (Cliente) request.getSession(false).getAttribute("cliente");
			plantaSelect = (Planta) request.getSession(false).getAttribute("plantaSelect");
			factura = (Factura) request.getSession(false).getAttribute("factura");
			
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
			
			procesarServicios();//error
			procesarEntradas();
			procesarVigencias();
			//PrimeFaces.current().ajax().update("form:dt-constanciaFacturaDs");
			
		} catch (Exception e) {
			
			System.out.println("ERROR Aqui" + e.getMessage());
		}
		
	}

	public FacturacionConstanciasBean getFacturacionBean() {
		return facturacionBean;
	}

	public void setFacturacionBean(FacturacionConstanciasBean facturacionBean) {
		this.facturacionBean = facturacionBean;
	}

	public List<ConstanciaFactura> getListaEntradas() {
		return listaEntradas;
	}

	public void setListaEntradas(List<ConstanciaFactura> listaEntradas) {
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

	public Planta getPlantaSelect() {
		return plantaSelect;
	}

	public void setPlantaSelect(Planta plantaSelect) {
		this.plantaSelect = plantaSelect;
	}

	public Factura getFactura() {
		return factura;
	}

	public void setFactura(Factura factura) {
		this.factura = factura;
	}
	
	//SERVICIOS (CONSTANCIA DE SERVICIOS)

	public void procesarServicios() {
		
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
				
				ServicioConstanciaDs servicioConstanciaDs = new ServicioConstanciaDs();
				
				servicioConstanciaDs.setDescripcion(csd.getServicioCve().getServicioDs());
				servicioConstanciaDs.setConstancia(cfd);
				
				
				if(precioServicio.getPrecio() != null) {//modificado por error null al llamar al metodo getPrecioServicio
					cantidad = precioServicio.getPrecio().setScale(2);//variable agregada
					importe = csd.getServicioCantidad().multiply(cantidad);
					
					servicioConstanciaDs.setCosto(importe.setScale(2));
					servicioConstanciaDs.setTarifa(precioServicio.getPrecio().setScale(2));
				}
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
				productoConstanciaDs.setCantidadManejo(new BigDecimal(ps.getCantidadTotal()).setScale(2));
				productoConstanciaDs.setUnidadManejo(ps.getUnidadDeManejoCve().getUnidadDeManejoDs());
								
				constanciaFacturaDs.getProductoConstanciaDsList().add(productoConstanciaDs);
				
			}
			
			listaConstanciaFacturaDs.add(constanciaFacturaDs);
		}
			
	}
	
	
	private PrecioServicio getPrecioServicio(Integer idCliente, List<PrecioServicio> listaPrecioS) {
		
		List<PrecioServicio> listaPrecioSTemp = null;
		PrecioServicio precioServicio = new PrecioServicio();
		
		listaPrecioSTemp = listaPrecioS.stream().filter(ps -> ps.getCliente().getCteCve().intValue() == idCliente)
							.collect(Collectors.toList());
		
		if(!(listaPrecioSTemp.isEmpty())) { //modificado por error de retornar objeto precioServicio en null
			Optional<PrecioServicio> precioServicioMax = listaPrecioSTemp.stream().max((i,j) -> i.getAvisoCve().getAvisoCve().compareTo(j.getAvisoCve().getAvisoCve()));
			precioServicio = precioServicioMax.get();//colocar condicional, si y solo si precioServicioMax != null
		}
		
		return precioServicio;
	}
	
	//ENTRADAS (CONSTANCIA DE DEPOSITO)
	
	public void procesarEntradas() {
		
		//BigDecimal importe = new BigDecimal(0);
		List<ServicioConstancia> listaServiciosConstancias = null;
		
		for(ConstanciaFactura cf: listaEntradas) {
			
			ConstanciaDeDeposito cdd = cf.getFolio();
			listaServiciosConstancias = new ArrayList<>();
			Aviso aviso = cdd.getAvisoCve();
			String tipoFacturacion = aviso.getAvisoTpFacturacion();
			
			for(ConstanciaDepositoDetalle cs: cdd.getConstanciaDepositoDetalleList()) {
				
				//System.out.println(cs.getServicioCve().getCobro().getId());
				Servicio servicio = cs.getServicioCve();
				TipoCobro tipoCobro = servicio.getCobro();
				ServicioConstancia sc = new ServicioConstancia();
				
				BigDecimal importe = new BigDecimal(0);
				BigDecimal cantidad = null;
				
				PrecioServicio precioServicio = precioServicioDAO.busquedaServicio(cdd.getAvisoCve().getAvisoCve(), clienteSelect.getCteCve(), servicio.getServicioCve());
				
				switch(tipoCobro.getId()) {
				
				case 1:
				case 2:
					
					cantidad = cs.getServicioCantidad();
					
					importe = cantidad.multiply(precioServicio.getPrecio());
					sc.setCosto(importe);
					sc.setUnidadMedida("SRV");
					System.out.println("El tipo cobro es 1 o 2 y su importe es: "+ importe);
					break;
				
				case 3:
				case 4:
					
					cantidad = getCantidadPartidas(cdd.getPartidaList(),tipoFacturacion);
					
					importe = cantidad.multiply(precioServicio.getPrecio());
					sc.setCosto(importe);
					System.out.println("El tipo de cobro es 3 o 4 y su importe es: "+importe);
					
					if(aviso.getAvisoTpFacturacion().equals("T")) {
						sc.setUnidadMedida("TRM");
					}else {
						sc.setUnidadMedida("KG");
					}
					
					break;
				
				default:
					
					System.out.println("No existe el tipo de cobro");
					break;
				}
				
				sc.setConstancia(cf);
				sc.setTarifa(precioServicio.getPrecio());
				sc.setBaseCargo(cantidad);
				sc.setDescripcion(cs.getServicioCve().getServicioDs());
				sc.setPlantaCve(plantaSelect.getPlantaCve());
				sc.setPlantaDs(plantaSelect.getPlantaDs());
				sc.setPlantaAbrev(plantaSelect.getPlantaAbrev());
				sc.setPlantaCod(plantaSelect.getPlantaCod());
				
				Camara camara = cdd.getPartidaList().get(0).getCamaraCve();
				sc.setCamaraCve(camara.getCamaraCve());
				sc.setCamaraDs(camara.getCamaraDs());
				sc.setCamaraAbrev(camara.getCamaraAbrev());
				
				sc.setCodigo(null);
				
				
				/*listaServiciosConstancias.add(sc);				
				cf.setServicioConstanciaList(listaServiciosConstancias);*/
				
				
				
			}
		}
	}
	
	public BigDecimal getCantidadPartidas(List<Partida> listaPartidas, String tipoFacturacion) {
		
		BigDecimal cantidad = new BigDecimal(0);
		
		for(Partida p: listaPartidas){
			
			if(tipoFacturacion.equals("T")) {
				cantidad = cantidad.add(p.getNoTarimas());
			}else {
				cantidad = cantidad.add(p.getPesoTotal());
			}
			
		}
		
		return cantidad;
	}
	
	
	//VIGENCIAS (CONSTANCIA DE DEPOSITO)
	
	public void procesarVigencias() {
		
		BigDecimal importe = new BigDecimal(0);
		List<ServicioConstancia> listaServiciosConstancias = null;
		
		for(ConstanciaFactura cf: listaVigencias) {
			
			ConstanciaDeDeposito cdd = cf.getFolio();
			listaServiciosConstancias = new ArrayList<>();
			Aviso aviso = cdd.getAvisoCve();
			String tipoFacturacion = aviso.getAvisoTpFacturacion();
			
			for(ConstanciaDepositoDetalle cs: cdd.getConstanciaDepositoDetalleList()) {
				
				//System.out.println(cs.getServicioCve().getCobro().getId());
				Servicio servicio = cs.getServicioCve();
				TipoCobro tipoCobro = servicio.getCobro();
				ServicioConstancia sc = new ServicioConstancia();
				
				BigDecimal cantidad = null;
				
				PrecioServicio precioServicio = precioServicioDAO.busquedaServicio(cdd.getAvisoCve().getAvisoCve(), clienteSelect.getCteCve(), servicio.getServicioCve());
				
				switch(tipoCobro.getId()) {
				
				case 1:
				case 2:				
				case 3:
					break;
				case 4:
					
					cantidad = getCantidadPartidas(cdd.getPartidaList(),tipoFacturacion);
					
					importe = cantidad.multiply(precioServicio.getPrecio());
					sc.setCosto(importe);
					System.out.println("El tipo de cobro es 3 o 4 y su importe es: "+importe);
					
					if(aviso.getAvisoTpFacturacion().equals("T")) {
						sc.setUnidadMedida("TRM");
					}else {
						sc.setUnidadMedida("KG");
					}
					
					break;
				
				default:
					
					System.out.println("No existe el tipo de cobro");
					break;
				}
				
				sc.setConstancia(cf);
				sc.setTarifa(precioServicio.getPrecio());
				sc.setBaseCargo(cantidad);
				sc.setDescripcion(cs.getServicioCve().getServicioDs());
				sc.setPlantaCve(plantaSelect.getPlantaCve());
				sc.setPlantaDs(plantaSelect.getPlantaDs());
				sc.setPlantaAbrev(plantaSelect.getPlantaAbrev());
				sc.setPlantaCod(plantaSelect.getPlantaCod());
				
				Camara camara = cdd.getPartidaList().get(0).getCamaraCve();
				sc.setCamaraCve(camara.getCamaraCve());
				sc.setCamaraDs(camara.getCamaraDs());
				sc.setCamaraAbrev(camara.getCamaraAbrev());
				
				sc.setCodigo(null);
				
				
				/*listaServiciosConstancias.add(sc);				
				cf.setServicioConstanciaList(listaServiciosConstancias);*/
				
				
			}
		}
	}
	
	
}
