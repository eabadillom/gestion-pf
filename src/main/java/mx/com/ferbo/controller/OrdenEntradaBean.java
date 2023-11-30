package mx.com.ferbo.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.AvisoDAO;
import mx.com.ferbo.dao.CamaraDAO;
import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.dao.IngresoDAO;
import mx.com.ferbo.dao.IngresoProductoDAO;
import mx.com.ferbo.dao.IngresoServicioDAO;
import mx.com.ferbo.dao.PlantaDAO;
import mx.com.ferbo.dao.PrecioServicioDAO;
import mx.com.ferbo.dao.ProductoClienteDAO;
import mx.com.ferbo.dao.UnidadDeManejoDAO;
import mx.com.ferbo.model.Aviso;
import mx.com.ferbo.model.Camara;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.Ingreso;
import mx.com.ferbo.model.IngresoProducto;
import mx.com.ferbo.model.IngresoServicio;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.PrecioServicio;
import mx.com.ferbo.model.Producto;
import mx.com.ferbo.model.ProductoPorCliente;
import mx.com.ferbo.model.UnidadDeManejo;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.util.DateUtil;

@Named
@ViewScoped
public class OrdenEntradaBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private static Logger log = LogManager.getLogger(OrdenEntradaBean.class);
	
	private List<Cliente> listaClientes;
	private ClienteDAO clienteDAO;
	private Cliente cliente;
	
	private List<Ingreso> listaIngreso;
	private IngresoDAO ingresoDAO;
	private Ingreso ingreso;
	
	private List<ProductoPorCliente> listaProductoPorCte;
	private ProductoClienteDAO productoClienteDAO;
	
	private List<Producto> listaProductos;
	private Producto producto;
	
	private List<Planta> listaPlantas;
	private PlantaDAO plantaDAO;
	private Planta planta;
	
	private List<Camara> listaCamara;
	private CamaraDAO camaraDAO;
	private Camara camara;
	
	private List<UnidadDeManejo> listaUnidadDeManejo;
	private UnidadDeManejoDAO unidadDeManejoDAO;
	private UnidadDeManejo unidadDeManejo;
	
	private List<IngresoProducto> listaIngresoProducto;
	private IngresoProducto ingresoProducto;
	private IngresoProductoDAO ingresoProductoDAO;
	private IngresoProducto selectIngresoProducto;
	
	private List<IngresoServicio> listaIngresoServicio;
	private IngresoServicioDAO ingresoServicioDAO;
	private IngresoServicio ingresoServicio;
	private IngresoServicio selectIngresoServicio;
	
	private List<PrecioServicio> listaServicioUnidad;
	private List<PrecioServicio> listaTmpServicios;
	private PrecioServicioDAO precioServicioDAO;
	private PrecioServicio precioServicioSelect;
	
	private List<Aviso> avisoPorCliente;
	private AvisoDAO avisoDAO;
	private Aviso avisoSelect;
	
	private Date fechaActual;
	private Integer tarima;
	private BigDecimal sumaTarimas;
	private BigDecimal sumaCantidad;
	private BigDecimal sumaPeso;
	private Boolean isCongelacion, isConservacion, isRefrigeracion, isManiobras;
	private int congelacion = 619, conservacion = 620, refrigeracion = 621, maniobras = 622 ;
	private BigDecimal cantidadServicio;
	
	FacesContext faceContext = null;
	HttpServletRequest request = null;
	HttpSession session = null;
	Usuario usuario = null;
	
	public OrdenEntradaBean(){
		
		listaClientes = new ArrayList<Cliente>();
		clienteDAO = new ClienteDAO();
		cliente = new Cliente();
		
		listaIngreso = new ArrayList<Ingreso>();
		ingresoDAO = new IngresoDAO();
		ingreso = new Ingreso();
		
		listaProductoPorCte = new ArrayList<ProductoPorCliente>();
		productoClienteDAO = new ProductoClienteDAO();
		
		listaProductos = new ArrayList<Producto>();
		producto = new Producto();
		
		listaPlantas = new ArrayList<Planta>();
		plantaDAO = new PlantaDAO();
		planta = new Planta();
		
		listaCamara = new ArrayList<Camara>();
		camaraDAO = new CamaraDAO();
		camara = new Camara();
		
		listaServicioUnidad = new ArrayList<PrecioServicio>();
		listaTmpServicios = new ArrayList<PrecioServicio>();
		precioServicioDAO = new PrecioServicioDAO();
		precioServicioSelect = new PrecioServicio();
		
		listaUnidadDeManejo = new ArrayList<UnidadDeManejo>();
		unidadDeManejoDAO = new UnidadDeManejoDAO();
		unidadDeManejo = new UnidadDeManejo();
		
		listaIngresoProducto = new ArrayList<IngresoProducto>();
		ingresoProductoDAO = new IngresoProductoDAO();
		ingresoProducto = new IngresoProducto();
		selectIngresoProducto = new IngresoProducto();
		
		listaIngresoServicio = new ArrayList<IngresoServicio>();
		ingresoServicioDAO = new IngresoServicioDAO();
		ingresoServicio = new IngresoServicio();
		selectIngresoServicio = new IngresoServicio();
		
		avisoPorCliente = new ArrayList<Aviso>();
		avisoDAO = new AvisoDAO();
		avisoSelect = new Aviso();
		
				
		fechaActual = new Date();
		ingresoProducto.setNoTarimas(new BigDecimal(1));
		sumaTarimas = new BigDecimal(0);
		sumaCantidad = new BigDecimal(0);
		sumaPeso = new BigDecimal(0);
		
	}
	
	@PostConstruct
	public void init() {
		
		faceContext = FacesContext.getCurrentInstance();
        request = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        session = request.getSession(false);
        
        usuario = (Usuario) session.getAttribute("usuario");
		
		listaClientes = clienteDAO.buscarTodos();
		listaPlantas = plantaDAO.buscarTodos();
		listaUnidadDeManejo = unidadDeManejoDAO.buscarTodos();
		
		
		isCongelacion = false;
		isConservacion = false;
		isRefrigeracion = false;
		isManiobras = false;
		
	}	
	
	public void cargaInfoCliente() {		
			
		
		ordenesEntrada();
		productoCte();		
	}
	
	public void ordenesEntrada() {		
		
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		
		
		try {
			
			Date fechaActualIni = new Date(fechaActual.getTime());
			DateUtil.setTime(fechaActualIni, 0, 0, 0, 0);
			
			Date fechaActualFin = new Date(fechaActual.getTime());
			DateUtil.setTime(fechaActualFin, 23, 59, 59, 99);	        	       
	        
	        listaIngreso = ingresoDAO.buscarPorFechaCtePlanta(fechaActualIni, fechaActualFin, cliente.getCteCve(),usuario.getIdPlanta());
			
	        severity = FacesMessage.SEVERITY_INFO;
	        mensaje = "Selecciona Folio de Orden de Entrada";
		} catch (Exception e) {
			log.info("Error ...." + e.getMessage());
			
			severity = FacesMessage.SEVERITY_INFO;
	        mensaje = "Error al encontrar ordenes de entrada";
			
		}finally {
			message = new FacesMessage(severity, "Orden Entrada", mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages");
		}
		
	}
	
	public void productoCte() {
		
		listaProductos.clear();
		listaProductoPorCte = productoClienteDAO.buscarPorCliente(cliente.getCteCve(), false);
		
		for(ProductoPorCliente pc: listaProductoPorCte) {			
			Producto prod = pc.getProductoCve();
			listaProductos.add(prod);
		}
		
	}
	
	public void camaraDisponible() {
		
		listaCamara = camaraDAO.buscarPorPlanta(planta);
		
	}
	
	public void addIngresoProducto() {
				
		IngresoProducto ingresoProducto = null;
		int tarima = this.ingresoProducto.getNoTarimas().intValue();
		
		
		try {
			
			for(int i=0;i<tarima;i++) {
				this.ingresoProducto.setNoTarimas(new BigDecimal(1));
				sumaTarimas = sumaTarimas.add(this.ingresoProducto.getNoTarimas());
				sumaCantidad = sumaCantidad.add(new BigDecimal(this.ingresoProducto.getCantidad()));
				sumaPeso = sumaPeso.add(this.ingresoProducto.getPeso());
				ingresoProducto = (IngresoProducto) this.ingresoProducto.clone();
				listaIngresoProducto.add(ingresoProducto);				
			}
			
			this.ingresoProducto = new IngresoProducto();
			this.ingresoProducto.setNoTarimas(new BigDecimal(1));
			
		} catch (Exception e) {
			log.info("Error al agregar ingreso Producto" + e.getMessage());
		}
		
	}
	
	public void eliminarIngresoProducto() {
		
		sumaTarimas = sumaTarimas.subtract(selectIngresoProducto.getNoTarimas());
		sumaCantidad = sumaCantidad.subtract(new BigDecimal(selectIngresoProducto.getCantidad()));
		sumaPeso = sumaPeso.subtract(selectIngresoProducto.getPeso());
		
		listaIngresoProducto.remove(selectIngresoProducto);		
	}
	
	public void cargaIngresoOrden() {
		
		productoIngresoOrden();
		servicioIngresoOrden();
		
		avisoPorCliente.clear();
		avisoPorCliente = avisoDAO.buscarPorCliente(cliente.getCteCve());
		
		Severity severity = FacesMessage.SEVERITY_WARN;
		String mensaje = "Selecciona un aviso";
		FacesMessage message = new FacesMessage(severity, "Aviso", mensaje);
		
		FacesContext.getCurrentInstance().addMessage(null, message);
		PrimeFaces.current().ajax().update("form:messages","form:aviso");
		
	}
	
	public void productoIngresoOrden() {
		
		listaIngresoProducto.clear();
		sumaTarimas = new BigDecimal(0);
		sumaCantidad = new BigDecimal(0);
		sumaPeso = new BigDecimal(0);
		
		List<IngresoProducto> listaTmpIngresoP = ingresoProductoDAO.buscarIngresosProductoPorId(ingreso.getIdIngreso());
		
		for(IngresoProducto ip: listaTmpIngresoP){
			
			sumaTarimas = sumaTarimas.add(ip.getNoTarimas());
			sumaCantidad = sumaCantidad.add(new BigDecimal(ip.getCantidad()));
			sumaPeso = sumaPeso.add(ip.getPeso());
			
			listaIngresoProducto.add(ip);
			
		}
		
	}
	
	public void servicioIngresoOrden(){
		
		List<IngresoServicio> listaTmp = ingresoServicioDAO.buscarPorIngreso(ingreso.getIdIngreso());
		listaIngresoServicio.addAll(listaTmp);		
	}
	
	public void renderServicio() {
		
		List<PrecioServicio> l = null;
		
		
		log.info("AvisoSelect: " + avisoSelect);

		
		listaServicioUnidad = precioServicioDAO.buscarPorAviso(avisoSelect, cliente);

		isCongelacion = false;
		isRefrigeracion = false;
		isConservacion = false;
		isManiobras = false;
		
		IngresoServicio ingresoServicio = new IngresoServicio();
		
		List<PrecioServicio> precioServicioTemp = new ArrayList<PrecioServicio>();
		precioServicioTemp.clear();
		precioServicioTemp = listaServicioUnidad.stream()
				.filter(p -> (
						p.getServicio().getServicioCve() == congelacion ||
						p.getServicio().getServicioCve() == conservacion ||
						p.getServicio().getServicioCve() == refrigeracion ||
						p.getServicio().getServicioCve() == maniobras
						))
				.collect(Collectors.toList());
		
		listaTmpServicios = precioServicioTemp;
		
		listaServicioUnidad.removeAll(precioServicioTemp);
		
		l = precioServicioTemp.stream().filter(ps -> ps.getServicio().getServicioCve() == 619)
		.collect(Collectors.toList());
		if(l.size() > 0) {
			this.isCongelacion = true;
		}
			
		
		l = precioServicioTemp.stream().filter(ps -> ps.getServicio().getServicioCve() == 620)
				.collect(Collectors.toList());
		if(l.size() > 0) {
			this.isConservacion = true;
			
			ingresoServicio.setServicio(l.get(0).getServicio());
			ingresoServicio.setCantidad(new BigDecimal(1));
			ingresoServicio.setUnidadDeManejo(l.get(0).getUnidad());
			ingresoServicio.setIngreso(ingreso);
			
			listaIngresoServicio.add(ingresoServicio);
		}
		
		l = precioServicioTemp.stream().filter(ps -> ps.getServicio().getServicioCve() == 621)
				.collect(Collectors.toList());
		if(l.size() > 0) {
			this.isRefrigeracion = true;
		}
		
		l = precioServicioTemp.stream().filter(ps -> ps.getServicio().getServicioCve() == 622)
				.collect(Collectors.toList());
		if(l.size() > 0) {
			this.isManiobras = true;
		}
		
	}
	
	public void saveIngresoServicio(){
				
		
		ingresoServicio.setServicio(precioServicioSelect.getServicio());
		ingresoServicio.setCantidad(cantidadServicio);
		ingresoServicio.setUnidadDeManejo(precioServicioSelect.getUnidad());
		ingresoServicio.setIngreso(ingreso);
		
		listaIngresoServicio.add(ingresoServicio);
		
		ingresoServicio = new IngresoServicio();
		precioServicioSelect = new PrecioServicio();
		cantidadServicio = new BigDecimal(1);
		
	}
	
	public void addCongelacion() {
		
		IngresoServicio ingresoServicio = new IngresoServicio();
		List<PrecioServicio> precioServicioTmp = null;
		List<IngresoServicio> listaTmp = new ArrayList<IngresoServicio>();
		PrecioServicio pServicio = null;
		
		if(isCongelacion){
			
			precioServicioTmp = listaTmpServicios.stream()
											.filter(p -> p.getServicio().getServicioCve() == 619).collect(Collectors.toList());
			
			if(precioServicioTmp.size() > 0) {
				
				pServicio = precioServicioTmp.get(0);
				
				ingresoServicio.setServicio(pServicio.getServicio());
				ingresoServicio.setCantidad(new BigDecimal(1));
				ingresoServicio.setUnidadDeManejo(pServicio.getUnidad());
				ingresoServicio.setIngreso(ingreso);
				
				listaIngresoServicio.add(ingresoServicio);
			}else {
				isCongelacion = false;
				
				FacesContext.getCurrentInstance().addMessage( null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Servicio" , "Servicio no encontrado - contactar a contaduria "));
				PrimeFaces.current().ajax().update("form:messages");
				
			}				
		}else {
			listaTmp = listaIngresoServicio.stream().filter(is -> is.getServicio().getServicioCve() == 619).collect(Collectors.toList());
			if(listaTmp.size() > 0) {
				listaIngresoServicio.remove(listaTmp.get(0));
			}
		}			
	}
	
	public void addConservacion() {
		
		IngresoServicio ingresoServicio = new IngresoServicio();
		List<PrecioServicio> precioServicioTmp = null;
		PrecioServicio pServicio = null;
		List<IngresoServicio> listaTmp = new ArrayList<IngresoServicio>(); 
		
		if(isConservacion){
			
			precioServicioTmp = listaTmpServicios.stream()
											.filter(p -> p.getServicio().getServicioCve() == 620).collect(Collectors.toList());
			
			if(precioServicioTmp.size() > 0) {
				
				pServicio = precioServicioTmp.get(0);
				
				ingresoServicio.setServicio(pServicio.getServicio());
				ingresoServicio.setCantidad(new BigDecimal(1));
				ingresoServicio.setUnidadDeManejo(pServicio.getUnidad());
				ingresoServicio.setIngreso(ingreso);
				
				listaIngresoServicio.add(ingresoServicio);
			}else {
				isConservacion = false;
				
				FacesContext.getCurrentInstance().addMessage( null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Servicio" , "Servicio no encontrado - contactar a contaduria "));
				PrimeFaces.current().ajax().update("form:messages");
			}				
		}else {
			listaTmp = listaIngresoServicio.stream().filter(is -> is.getServicio().getServicioCve() == 620).collect(Collectors.toList());
			if(listaTmp.size() > 0) {
				listaIngresoServicio.remove(listaTmp.get(0));
			}
		}
		
	}
	
	public void addRefrigeracion() {
		
		IngresoServicio ingresoServicio = new IngresoServicio();
		List<PrecioServicio> precioServicioTmp = null;
		List<IngresoServicio> listaTmp = new ArrayList<IngresoServicio>();
		PrecioServicio pServicio = null;
		
		if(isRefrigeracion){
			
			precioServicioTmp = listaTmpServicios.stream()
											.filter(p -> p.getServicio().getServicioCve() == 621).collect(Collectors.toList());
			
			if(precioServicioTmp.size() > 0) {
				
				pServicio = precioServicioTmp.get(0);
				
				ingresoServicio.setServicio(pServicio.getServicio());
				ingresoServicio.setCantidad(new BigDecimal(1));
				ingresoServicio.setUnidadDeManejo(pServicio.getUnidad());
				ingresoServicio.setIngreso(ingreso);
				
				listaIngresoServicio.add(ingresoServicio);
			}else {
				isRefrigeracion = false;
				
				FacesContext.getCurrentInstance().addMessage( null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Servicio" , "Servicio no encontrado - contactar a contaduria "));
				PrimeFaces.current().ajax().update("form:messages");
			}				
		}else {
			listaTmp = listaIngresoServicio.stream().filter(is -> is.getServicio().getServicioCve() == 621).collect(Collectors.toList());
			if(listaTmp.size() > 0) {
				listaIngresoServicio.remove(listaTmp.get(0));
			}
		}
		
	}
	
	public void addManiobras() {
		
		IngresoServicio ingresoServicio = new IngresoServicio();
		List<PrecioServicio> precioServicioTmp = null;
		List<IngresoServicio> listaTmp = new ArrayList<IngresoServicio>();
		PrecioServicio pServicio = null;
		
		if(isManiobras){
			
			precioServicioTmp = listaTmpServicios.stream()
											.filter(p -> p.getServicio().getServicioCve() == 622).collect(Collectors.toList());
			
			if(precioServicioTmp.size() > 0) {
				
				pServicio = precioServicioTmp.get(0);
				
				ingresoServicio.setServicio(pServicio.getServicio());
				ingresoServicio.setCantidad(new BigDecimal(1));
				ingresoServicio.setUnidadDeManejo(pServicio.getUnidad());
				ingresoServicio.setIngreso(ingreso);
				
				listaIngresoServicio.add(ingresoServicio);
			}else {
				isManiobras = false;
				
				FacesContext.getCurrentInstance().addMessage( null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Servicio" , "Servicio no encontrado - contactar a contaduria "));
				PrimeFaces.current().ajax().update("form:messages");
			}				
		}else {
			listaTmp = listaIngresoServicio.stream().filter(is -> is.getServicio().getServicioCve() == 622).collect(Collectors.toList());
			if(listaTmp.size() > 0) {
				listaIngresoServicio.remove(listaTmp.get(0));
			}
		}
		
	}

	public void eliminarIngresoServicio() {		
		listaIngresoServicio.remove(selectIngresoServicio);
		
		if(selectIngresoServicio.getServicio().getServicioCve() == 619 ) {
			isCongelacion = false;
		}
		
		if(selectIngresoServicio.getServicio().getServicioCve() == 620 ) {
			isConservacion = false;
		}
		
		if(selectIngresoServicio.getServicio().getServicioCve() == 621 ) {
			isRefrigeracion = false;
		}
		
		if(selectIngresoServicio.getServicio().getServicioCve() == 622 ) {
			isManiobras = false;
		}
		
	}
	
	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public List<Cliente> getListaClientes() {
		return listaClientes;
	}

	public void setListaClientes(List<Cliente> listaClientes) {
		this.listaClientes = listaClientes;
	}

	public List<Ingreso> getListaIngreso() {
		return listaIngreso;
	}

	public void setListaIngreso(List<Ingreso> listaIngreso) {
		this.listaIngreso = listaIngreso;
	}

	public Ingreso getIngreso() {
		return ingreso;
	}

	public void setIngreso(Ingreso ingreso) {
		this.ingreso = ingreso;
	}

	public List<ProductoPorCliente> getListaProductoPorCte() {
		return listaProductoPorCte;
	}

	public void setListaProductoPorCte(List<ProductoPorCliente> listaProductoPorCte) {
		this.listaProductoPorCte = listaProductoPorCte;
	}

	public List<Producto> getListaProductos() {
		return listaProductos;
	}

	public void setListaProductos(List<Producto> listaProductos) {
		this.listaProductos = listaProductos;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public Planta getPlanta() {
		return planta;
	}

	public void setPlanta(Planta planta) {
		this.planta = planta;
	}

	public List<Planta> getListaPlantas() {
		return listaPlantas;
	}

	public void setListaPlantas(List<Planta> listaPlantas) {
		this.listaPlantas = listaPlantas;
	}

	public List<Camara> getListaCamara() {
		return listaCamara;
	}

	public void setListaCamara(List<Camara> listaCamara) {
		this.listaCamara = listaCamara;
	}

	public Camara getCamara() {
		return camara;
	}

	public void setCamara(Camara camara) {
		this.camara = camara;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public IngresoProducto getIngresoProducto() {
		return ingresoProducto;
	}

	public void setIngresoProducto(IngresoProducto ingresoProducto) {
		this.ingresoProducto = ingresoProducto;
	}

	public List<UnidadDeManejo> getListaUnidadDeManejo() {
		return listaUnidadDeManejo;
	}

	public void setListaUnidadDeManejo(List<UnidadDeManejo> listaUnidadDeManejo) {
		this.listaUnidadDeManejo = listaUnidadDeManejo;
	}

	public UnidadDeManejo getUnidadDeManejo() {
		return unidadDeManejo;
	}

	public void setUnidadDeManejo(UnidadDeManejo unidadDeManejo) {
		this.unidadDeManejo = unidadDeManejo;
	}

	public Integer getTarima() {
		return tarima;
	}

	public void setTarima(Integer tarima) {
		this.tarima = tarima;
	}

	public List<IngresoProducto> getListaIngresoProducto() {
		return listaIngresoProducto;
	}

	public void setListaIngresoProducto(List<IngresoProducto> listaIngresoProducto) {
		this.listaIngresoProducto = listaIngresoProducto;
	}

	public BigDecimal getSumaTarimas() {
		return sumaTarimas;
	}

	public void setSumaTarimas(BigDecimal sumaTarimas) {
		this.sumaTarimas = sumaTarimas;
	}

	public BigDecimal getSumaCantidad() {
		return sumaCantidad;
	}

	public void setSumaCantidad(BigDecimal sumaCantidad) {
		this.sumaCantidad = sumaCantidad;
	}

	public BigDecimal getSumaPeso() {
		return sumaPeso;
	}

	public void setSumaPeso(BigDecimal sumaPeso) {
		this.sumaPeso = sumaPeso;
	}

	public Boolean getIsCongelacion() {
		return isCongelacion;
	}

	public void setIsCongelacion(Boolean isCongelacion) {
		this.isCongelacion = isCongelacion;
	}

	public Boolean getIsConservacion() {
		return isConservacion;
	}

	public void setIsConservacion(Boolean isConservacion) {
		this.isConservacion = isConservacion;
	}

	public Boolean getIsRefrigeracion() {
		return isRefrigeracion;
	}

	public void setIsRefrigeracion(Boolean isRefrigeracion) {
		this.isRefrigeracion = isRefrigeracion;
	}

	public Boolean getIsManiobras() {
		return isManiobras;
	}

	public void setIsManiobras(Boolean isManiobras) {
		this.isManiobras = isManiobras;
	}

	public PrecioServicio getPrecioServicioSelect() {
		return precioServicioSelect;
	}

	public void setPrecioServicioSelect(PrecioServicio precioServicioSelect) {
		this.precioServicioSelect = precioServicioSelect;
	}

	public List<PrecioServicio> getListaServicioUnidad() {
		return listaServicioUnidad;
	}

	public void setListaServicioUnidad(List<PrecioServicio> listaServicioUnidad) {
		this.listaServicioUnidad = listaServicioUnidad;
	}

	public BigDecimal getCantidadServicio() {
		return cantidadServicio;
	}

	public void setCantidadServicio(BigDecimal cantidadServicio) {
		this.cantidadServicio = cantidadServicio;
	}

	public List<IngresoServicio> getListaIngresoServicio() {
		return listaIngresoServicio;
	}

	public void setListaIngresoServicio(List<IngresoServicio> listaIngresoServicio) {
		this.listaIngresoServicio = listaIngresoServicio;
	}

	public Aviso getAvisoSelect() {
		return avisoSelect;
	}

	public void setAvisoSelect(Aviso avisoSelect) {
		this.avisoSelect = avisoSelect;
	}

	public List<Aviso> getAvisoPorCliente() {
		return avisoPorCliente;
	}

	public void setAvisoPorCliente(List<Aviso> avisoPorCliente) {
		this.avisoPorCliente = avisoPorCliente;
	}

	public IngresoServicio getIngresoServicio() {
		return ingresoServicio;
	}

	public void setIngresoServicio(IngresoServicio ingresoServicio) {
		this.ingresoServicio = ingresoServicio;
	}

	public IngresoProducto getSelectIngresoProducto() {
		return selectIngresoProducto;
	}

	public void setSelectIngresoProducto(IngresoProducto selectIngresoProducto) {
		this.selectIngresoProducto = selectIngresoProducto;
	}

	public IngresoServicio getSelectIngresoServicio() {
		return selectIngresoServicio;
	}

	public void setSelectIngresoServicio(IngresoServicio selectIngresoServicio) {
		this.selectIngresoServicio = selectIngresoServicio;
	}
	

}
