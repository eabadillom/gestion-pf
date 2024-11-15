package mx.com.ferbo.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import mx.com.ferbo.dao.AvisoDAO;
import mx.com.ferbo.dao.ConstanciaDeDepositoDAO;
import mx.com.ferbo.dao.ConstanciaDepositoDetalleDAO;
import mx.com.ferbo.dao.EstadoConstanciaDAO;
import mx.com.ferbo.dao.PrecioServicioDAO;
import mx.com.ferbo.dao.ProductoDAO;
import mx.com.ferbo.dao.UnidadDeManejoDAO;
import mx.com.ferbo.dao.UnidadDeProductoDAO;
import mx.com.ferbo.model.Aviso;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ConstanciaDeDeposito;
import mx.com.ferbo.model.ConstanciaDepositoDetalle;
import mx.com.ferbo.model.DetallePartida;
import mx.com.ferbo.model.EstadoConstancia;
import mx.com.ferbo.model.Partida;
import mx.com.ferbo.model.PrecioServicio;
import mx.com.ferbo.model.Producto;
import mx.com.ferbo.model.ProductoPorCliente;
import mx.com.ferbo.model.Servicio;
import mx.com.ferbo.model.Tarima;
import mx.com.ferbo.model.UnidadDeManejo;
import mx.com.ferbo.model.UnidadDeProducto;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.InventarioException;
import mx.com.ferbo.util.JasperReportUtil;

@Named
@ViewScoped
public class ConsultarConstanciaDeDepositoBean implements Serializable{
	
	private static final long serialVersionUID = -3109002730694247052L;
	private static Logger log = LogManager.getLogger(ConsultarConstanciaDeDepositoBean.class);
	
	@Inject
    private SideBarBean sideBar;
	
	private Date fechaInicial;
	private Date fechaFinal;
	private Date fechaIngreso;
	private Date maxDate;
	
	private String folio;
	
	private List<Cliente> listadoClientes;
	private Cliente cliente;
	
	private List<Producto> listadoProductoPorCliente;
	private Producto productoSelect;
	private UnidadDeManejo unidadSelect;
	
	private Partida partidaSelect;
	
	private ConstanciaDeDepositoDAO constanciaDeDepositoDAO;
	private List<ConstanciaDeDeposito> listadoConstanciaDeDepositos;
	private ConstanciaDeDeposito selectConstanciaDD;
	
	private UnidadDeProductoDAO unidadDeProductoDAO;
	private ProductoDAO productoDAO;
	
	private List<UnidadDeManejo> unidades;
	private UnidadDeManejoDAO unidadDAO;
	
	private List<PrecioServicio> listadoPrecioServicio;
	private PrecioServicioDAO precioServicioDAO;
	
	private List<ConstanciaDepositoDetalle> listadoConstanciaDepositoDetalle;
	private ConstanciaDepositoDetalleDAO constanciaDepositoDetalleDAO;
	private ConstanciaDepositoDetalle constanciaSelect;
	
	private Servicio servicioSelected;
	
	private BigDecimal servicioCantidad,cantidadServicio;
	
	private List<Aviso> listaAvisos;
	private AvisoDAO avisoDAO;
	
	private FacesContext faceContext;
	private HttpServletRequest httpServletRequest;
	private Usuario usuario;
	
	private EstadoConstancia statusCancelada;
	private EstadoConstanciaDAO statusDAO;
	
	private Integer cantidadTotal = null;
	private BigDecimal pesoTotal = null;
	private BigDecimal tarimasTotal = null;
	
	private DetallePartida detallePartida = null;
	
	private Tarima tarima = null;
	private Tarima nuevaTarima = null;
	private List<Tarima> tarimas = null;
	private Integer idTarima = null;
	
	private StreamedContent file;
	
	public ConsultarConstanciaDeDepositoBean() {
		
		constanciaDeDepositoDAO = new ConstanciaDeDepositoDAO();
		listadoConstanciaDeDepositos = new ArrayList<ConstanciaDeDeposito>();
		
		listadoClientes = new ArrayList<Cliente>();
		
		listadoProductoPorCliente = new ArrayList<>();
		productoDAO = new ProductoDAO();
		unidadDeProductoDAO = new UnidadDeProductoDAO();
		unidadDAO = new UnidadDeManejoDAO();
		
		listadoPrecioServicio = new ArrayList<PrecioServicio>();
		precioServicioDAO = new PrecioServicioDAO();
		
		listadoConstanciaDepositoDetalle = new ArrayList<ConstanciaDepositoDetalle>();
		this.constanciaDepositoDetalleDAO = new ConstanciaDepositoDetalleDAO();
		this.avisoDAO = new AvisoDAO();
		this.statusDAO = new EstadoConstanciaDAO();
	}

	@PostConstruct
	public void init() {
		byte bytes[] = {};
		faceContext = FacesContext.getCurrentInstance();
		httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
		usuario = (Usuario) httpServletRequest.getSession(false).getAttribute("usuario");
		
		listadoClientes = sideBar.getListaClientesActivos();
		unidades = unidadDAO.buscarTodos();
		
		if(listadoClientes.size() == 1)
			this.cliente = listadoClientes.get(0);
		
		folio = "";
		
		Date today = new Date();
		maxDate = new Date(today.getTime());
		
		this.selectConstanciaDD = new ConstanciaDeDeposito();
		this.selectConstanciaDD.setAvisoCve(new Aviso());
		this.statusCancelada = statusDAO.buscarPorId(2);
		
		this.file = DefaultStreamedContent.builder().contentType("application/pdf").contentLength(bytes.length)
				.name("ticket.pdf").stream(() -> new ByteArrayInputStream(bytes)).build();
		
		this.partidaSelect = new Partida();
		this.detallePartida = new DetallePartida();
		this.tarima = new Tarima();
		this.nuevaTarima = new Tarima();
	}
	
	@PreDestroy
	public void destroy() {
		log.info("Lanzando evento Pre-destroy...");
	}
	
	public void buscarConstancias() {
		Integer idCliente = null;
		
		try {
			if(listadoClientes.size() == 1)
				this.cliente = listadoClientes.get(0);
			
			if(cliente != null)
				idCliente = cliente.getCteCve();
			
			listadoConstanciaDeDepositos = constanciaDeDepositoDAO.buscarPor(folio, idCliente, fechaInicial, fechaFinal);
			
		} catch(Exception ex) {
			log.error("Problema en la búsqueda de constancias de depósito...", ex);
		}
	}
	
	public void cargaDetalle() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Carga de información...";		
		
		try {
			log.info("Constancia de deposito: {}",this.selectConstanciaDD);
			this.selectConstanciaDD = constanciaDeDepositoDAO.buscarPorFolioCliente(this.selectConstanciaDD.getFolioCliente(), true);
			
			listadoProductoPorCliente.clear();
			ProductoPorCliente productoPorCliente = new ProductoPorCliente();
			productoPorCliente.setCteCve(this.selectConstanciaDD.getCteCve());
			this.listadoProductoPorCliente =  productoDAO.buscarPorCliente(this.selectConstanciaDD.getCteCve().getCteCve());
			this.listadoConstanciaDepositoDetalle = this.selectConstanciaDD.getConstanciaDepositoDetalleList();
			log.info("Constancia de deposito: {}", this.selectConstanciaDD);
			log.info("Partida: {}", this.selectConstanciaDD.getPartidaList().size());
			this.productoSelect = null;
			
			if(selectConstanciaDD.getAvisoCve()!=null) {
				listadoPrecioServicio = precioServicioDAO.buscarPorAviso(selectConstanciaDD.getAvisoCve(), selectConstanciaDD.getCteCve());
			}
			
			this.tarimas = this.selectConstanciaDD.getPartidaList()
					.stream()
					.filter(partida -> partida.getTarima() != null)
					.map(partida -> partida.getTarima())
					.distinct()
					.collect(Collectors.toList())
					;
			
			for(Tarima t : this.tarimas) {
				List<Partida> partidas = this.selectConstanciaDD.getPartidaList()
						.stream()
						.filter(p -> t.equals(p.getTarima()))
						.collect(Collectors.toList())
						;
				t.setPartidas(partidas);
			}
			
			this.nuevaTarima();
			
			this.tarimasTotal = new BigDecimal("0.000").setScale(3, BigDecimal.ROUND_HALF_UP);
			
			this.tarimasTotal = this.totalTarimas(this.selectConstanciaDD.getPartidaList());
			
			log.debug("{} tarimas, {} unidades, {} kg", this.tarimasTotal, this.pesoTotal);
			
			listaAvisos = avisoDAO.buscarPorCliente(this.selectConstanciaDD.getCteCve().getCteCve());
			
			this.constanciaSelect = new ConstanciaDepositoDetalle();
			
		} catch (Exception ex) {
			log.error("Problema para cargar la información de la constancia...", ex);
			mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;
			
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
		} finally {
			PrimeFaces.current().ajax().update("form:messages","form:dlg-constancia", "form:dlg-partidas", "form:dlg-servicios", "form:constanciaD");
			
		}
	}
	
	public void cambiaProducto(Partida partida) {
		log.info("Cambiando producto de la partida {} - producto: {}", partida.getPartidaCve(), partida.getUnidadDeProductoCve().getProductoCve().getProductoCve());
	}
	
	public void nuevaTarima() {
		//Temporalmente se establece un ID negativo, sólo para permitir que los List<Tarima>
		//trabajen correctamente al compararar y/o eliminar elementos.
		if(this.idTarima == null)
			this.idTarima = new Integer(-1);
		
		this.nuevaTarima = new Tarima(String.format("%s-%d", this.selectConstanciaDD.getFolioCliente(), this.tarimas.size() + 1));
		this.nuevaTarima.setId(this.idTarima--);
		log.info("...Tarima: {}", this.tarima);
	}

	public void agregaTarima() {
		//Temporalmente se establece un ID negativo, sólo para permitir que los List<Tarima>
		//trabajen correctamente al compararar y/o eliminar elementos.
		if(this.idTarima == null)
			this.idTarima = new Integer(-1);
		
		log.info("Folio cliente: {}", this.selectConstanciaDD.getFolioCliente());
		this.nuevaTarima = new Tarima(String.format("%s-%d", this.selectConstanciaDD.getFolioCliente(), this.tarimas.size() + 1));
		this.nuevaTarima.setId(this.idTarima--);
		
		this.nuevaTarima.setPartidas(new ArrayList<Partida>());
		this.tarimas.add(this.nuevaTarima);
		this.nuevaTarima = new Tarima(String.format("%s-%d", this.selectConstanciaDD.getFolioCliente(), this.tarimas.size() + 1));
	}
	
	public void eliminarTarima(Tarima tarima) {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Carga de información...";	
		
		try {
			
			if(tarima.getPartidas() != null && tarima.getPartidas().size() > 0)
				throw new InventarioException("La tarima seleccionada tiene productos registrados.");
			
			log.info("--Tarima hashcode: {}", tarima.hashCode());
			
			for(Tarima t : this.tarimas) {
				log.info("Tarima hashcode: {}", t.hashCode());
			}
			
			if(this.tarimas.contains(tarima) == false)
				throw new InventarioException("La tarima seleccionada no está registrada en el listado de tarimas.");
			
			this.tarimas.remove(tarima);
			mensaje = "Tarima eliminada.";
			severity = FacesMessage.SEVERITY_INFO;
		} catch(InventarioException ex) {
			log.warn("Problema para eliminar la tarima...", ex);
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} catch(Exception ex) {
			log.error("Problema para eliminar la tarima...", ex);
			mensaje = "Hay un problema para eliminar la tarima.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages","form:datosGenerales");
		}
	}
	
	public BigDecimal totalTarimas(List<Partida> partidas) {
		BigDecimal totalTarimas = null;
		List<Tarima> tarimas = null;
		List<Partida> partidasConTarima = null;
		List<Partida> partidasSinTarima = null;
		
		try {
			partidasConTarima = partidas.stream().filter(p -> p.getTarima() != null)
					.collect(Collectors.toList())
					;
			
			partidasSinTarima = partidas.stream().filter(p -> p.getTarima() == null && p.getNoTarimas() != null)
					.collect(Collectors.toList())
					;
			
			if(partidasConTarima == null)
				throw new InventarioException("Las partidas de la constancia no están asociadas a una tarima.");
			
			tarimas = new ArrayList<Tarima>();
			
			for(Partida p : partidasConTarima) {
				if(tarimas.contains(p.getTarima()))
					continue;
				tarimas.add(p.getTarima());
			}
			
			totalTarimas = new BigDecimal(tarimas.size()).setScale(3, BigDecimal.ROUND_HALF_UP);
			
			totalTarimas = totalTarimas.add(
					partidasSinTarima.stream().map(item -> item.getNoTarimas())
					.reduce(BigDecimal.ZERO.setScale(3, BigDecimal.ROUND_HALF_UP), BigDecimal::add)
					);
		} catch(Exception ex) {
			totalTarimas = BigDecimal.ZERO.setScale(3, BigDecimal.ROUND_HALF_UP);
		}
		
		return totalTarimas;
	}
	
	public void updateConstanciaDD() {
		
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Guardar constancia";
		
		try {
			if( (usuario.getPerfil() == 1) || (usuario.getPerfil() == 4) )
				throw new Exception("No esta autorizado para modificar esta información.");
			
			//Cuando se crearon nuevas tarimas, se agregaron con un ID negativo temporal.
			//Este ID se debe eliminar, dejando el dato del ID en null, para permitir que Hibernate
			//guarde correctamente la nueva tarima.
			//En el caso en que el ID sea positivo (> 0) entonces se respeta el ID, ya que se sobreentiende
			//que la tarima ya estaba guardada en la Base de Datos.
			for(Tarima t : this.tarimas) {
				if(t.getId() == null)
					continue;
				
				if(t.getId() < 0)
					t.setId(null);
			}
			
			this.constanciaDeDepositoDAO.actualizar(this.selectConstanciaDD);
			
			mensaje = "Constancia actualizada correctamente.";
			severity = FacesMessage.SEVERITY_INFO;
			
		} catch (Exception ex) {
			log.error("Problema para actualizar el producto...", ex);
			mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages","form:datosGenerales:dt-partida");
		}
	}
	
	public void updateDetallePartida() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Detalle del producto";
		
		String resultado = null;
		
		UnidadDeProducto udp = null;
		
		try {
			if(this.productoSelect == null)
				throw new InventarioException("Debe seleccionar un producto.");
			
			if(this.unidadSelect == null)
				throw new InventarioException("Debe seleccionar una unidad.");
			
			udp = unidadDeProductoDAO.buscarPorProductoUnidad(this.productoSelect.getProductoCve(), this.unidadSelect.getUnidadDeManejoCve());
			
			if(udp == null) {
				udp = new UnidadDeProducto();
				udp.setProductoCve(this.productoSelect);
				udp.setUnidadDeManejoCve(this.unidadSelect);
				unidadDeProductoDAO.guardar(udp);
				log.info("Nueva Unidad de producto creada: {}", udp.getUnidadDeProductoCve());
			}
			
			this.partidaSelect.setUnidadDeProductoCve(udp);
			
			for(DetallePartida dp : partidaSelect.getDetallePartidaList()) {
				
				dp.setDtpCaducidad(this.detallePartida.getDtpCaducidad());
				dp.setDtpCodigo(this.detallePartida.getDtpCodigo());
				dp.setDtpLote(this.detallePartida.getDtpLote());
				dp.setDtpMP(this.detallePartida.getDtpMP());
				dp.setDtpPedimento(this.detallePartida.getDtpPedimento());
				dp.setDtpPO(this.detallePartida.getDtpPO());
				dp.setDtpSAP(this.detallePartida.getDtpSAP());
				dp.setDtpTarimas(this.detallePartida.getDtpTarimas());
				
				log.info("Actualizando detalle partida: {}", this.detallePartida);
			}
			
			mensaje = "Información del producto actualizada.";
			severity = FacesMessage.SEVERITY_INFO;
			
			this.partidaSelect = null;
			this.detallePartida = null;
		} catch (InventarioException ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} catch (Exception ex) {
			mensaje = "Existe un problema para actualizar el detalle del producto.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages");	
		}
	}
	
	public void verDetallePartida() {
		List<DetallePartida> listaDetalleP = partidaSelect.getDetallePartidaList();
		DetallePartida detalleP = listaDetalleP.get(0);
		this.detallePartida = detalleP;
		
		this.productoSelect = partidaSelect.getUnidadDeProductoCve().getProductoCve();
		this.unidadSelect = partidaSelect.getUnidadDeProductoCve().getUnidadDeManejoCve();
	}
	
	public List<PrecioServicio> serviciosDisponibles() {
		List<PrecioServicio> list = null;
		list = new ArrayList<PrecioServicio>();
		
		List<Servicio> servicios = this.selectConstanciaDD.getConstanciaDepositoDetalleList()
			.stream()
			.map(ConstanciaDepositoDetalle::getServicioCve)
			.collect(Collectors.toList())
			;
		
		for(PrecioServicio ps : this.listadoPrecioServicio) {
			
			if(servicios.contains(ps.getServicio()))
				continue;
			
			list.add(ps);
		}
		
		return list;
	}
	
	public void newServicio() {
		constanciaSelect = new ConstanciaDepositoDetalle();
	}
	
	public void updateServicio() {
		
		listadoConstanciaDepositoDetalle = constanciaDepositoDetalleDAO.buscarPorFolio(selectConstanciaDD);
		selectConstanciaDD.setConstanciaDepositoDetalleList(listadoConstanciaDepositoDetalle);
		
		for(ConstanciaDepositoDetalle c: selectConstanciaDD.getConstanciaDepositoDetalleList()) {
			
			if(c.getConstanciaDepositoDetalleCve().equals(constanciaSelect.getConstanciaDepositoDetalleCve())) {
				c.setServicioCantidad(servicioCantidad);
			}
			
		}
		
		if(constanciaDeDepositoDAO.actualizar(this.selectConstanciaDD) == null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Actualizacion","Servicio Actualizado"));
			listadoConstanciaDepositoDetalle = constanciaDepositoDetalleDAO.buscarPorFolio(selectConstanciaDD);
		}
		
		PrimeFaces.current().ajax().update("form:messages","form:dt-ConstanciaDepositoDetalle");
	}
	
	public void saveServicio() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		
		try {
			if(this.constanciaSelect == null)
				throw new InventarioException("No hay un servicio seleccionado.");
			
			if(this.constanciaSelect.getServicioCve() == null)
				throw new InventarioException("Debe seleccionar un servicio.");
			
			if(this.constanciaSelect.getServicioCantidad() == null)
				throw new InventarioException("Debe indicar una cantidad para el servicio.");
			
			if(this.constanciaSelect.getServicioCantidad().compareTo(BigDecimal.ZERO) <= 0)
				throw new InventarioException("Debe indicar una cantidad correcta para el servicio.");
			
			this.constanciaSelect.setFolio(this.selectConstanciaDD);
			this.selectConstanciaDD.getConstanciaDepositoDetalleList().add(this.constanciaSelect);
			
			if(constanciaDepositoDetalleDAO.guardar(this.constanciaSelect) != null)
				throw new InventarioException("Ocurrió un problema al guardar el servicio.");
			
			this.constanciaSelect = new ConstanciaDepositoDetalle();
			
			mensaje = "Servicio agregado correctamente.";
			severity = FacesMessage.SEVERITY_INFO;
		} catch(InventarioException ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} catch(Exception e) {
			mensaje = "Problema al agregar el servicio.";
			severity = FacesMessage.SEVERITY_WARN;
		} finally {
			message = new FacesMessage(severity,"Servicio" , mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages","form:datosGenerales:dt-ConstanciaDepositoDetalle", "form:precioServicio", "form:cantidadServicio");
		}
	}
	
	public void deleteServicio() {
		
		if(constanciaDepositoDetalleDAO.eliminar(this.constanciaSelect)== null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Eliminado","Servicio Eliminado"));
			listadoConstanciaDepositoDetalle = constanciaDepositoDetalleDAO.buscarPorFolio(selectConstanciaDD);
			selectConstanciaDD.getConstanciaDepositoDetalleList().remove(constanciaSelect);
			constanciaSelect = new ConstanciaDepositoDetalle();
		}
		
		PrimeFaces.current().ajax().update("form:messages","form:dt-ConstanciaDepositoDetalle");
		
	}
	
	public void cancelarConstancia() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Producto";
		
		try {
			
			if(this.selectConstanciaDD.getStatus().getEdoCve() == 2)
				throw new InventarioException("La constancia solicitada ya está cancelada.");
			
			if(constanciaDeDepositoDAO.tieneSalidas(this.selectConstanciaDD.getFolio()))
				throw new InventarioException("La entrada solicitada ya tiene movimientos de salida. No es posible cancelarla.");
			
			if(constanciaDeDepositoDAO.tieneFacturas(this.selectConstanciaDD.getFolio()))
				throw new InventarioException("La entrada solicitada ya tiene movimientos de facturación. No es posible cancelarla.");
			
			log.info("Cancelando constancia {}", this.selectConstanciaDD.getFolioCliente());
			this.selectConstanciaDD.setStatus(statusCancelada);
			this.selectConstanciaDD.setObservaciones("CONSTANCIA CANCELADA EL DIA " + DateUtil.getString(this.selectConstanciaDD.getFechaIngreso(), DateUtil.FORMATO_DD_MM_YYYY));
			constanciaDeDepositoDAO.actualizar(selectConstanciaDD);
			this.buscarConstancias();
			mensaje = "Constancia cancelada correctamente.";
			severity = FacesMessage.SEVERITY_INFO;
			PrimeFaces.current().executeScript("PF('cancelDialog').hide()");
		} catch (InventarioException ex) {
			log.error("Problema para cancelar la entrada...", ex);
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} catch (Exception ex) {
			log.error("Problema para cancelar la entrada...", ex);
			mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages","form:dt-constanciaDeDeposito");
		}
		
		
		
		
	}
	
	public void imprimir() {
		String jasperPath = null;
		String filename = null;
		String images = null;
		String message = null;
		Severity severity = null;
		File reportFile = null;
		File imgfile = null;
		JasperReportUtil jasperReportUtil = null;
		Map<String, Object> parameters = null;
		Connection conn = null;

		try {
			
			if(this.selectConstanciaDD.getFolio() == null)
				throw new InventarioException("La constancia de depósito no está registrada.");
			
			jasperPath = "/jasper/GestionReport.jrxml";
			filename = String.format("Entrada_%s.pdf", selectConstanciaDD.getFolioCliente());
			images = "/images/logo.jpeg";
			reportFile = new File(jasperPath);
			URL resource = getClass().getResource(jasperPath);
			URL resourceimg = getClass().getResource(images);
			String file = resource.getFile();
			String img = resourceimg.getFile();
			reportFile = new File(file);
			imgfile = new File(img);
			log.debug("Ruta del reporte: {}", reportFile.getPath());

			conn = EntityManagerUtil.getConnection();
			parameters = new HashMap<String, Object>();
			parameters.put("REPORT_CONNECTION", conn);
			parameters.put("FOLIO", this.selectConstanciaDD.getFolioCliente());
			parameters.put("LogoPath", imgfile.getPath());
			log.debug("Parametros: {}", parameters.toString());

			jasperReportUtil = new JasperReportUtil();
			byte[] bytes = jasperReportUtil.createPDF(parameters, reportFile.getPath());
			InputStream input = new ByteArrayInputStream(bytes);
			this.file = DefaultStreamedContent.builder().contentType("application/pdf").name(filename)
					.stream(() -> input).build();
			log.info("Ticket entrada generado {}...", filename);
		} catch (Exception ex) {
			log.error("Problema general...", ex);
			message = String.format("No se pudo imprimir el folio %s", this.selectConstanciaDD.getFolioCliente());
			severity = FacesMessage.SEVERITY_INFO;
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(severity, "Error en impresion", message));
			PrimeFaces.current().ajax().update("form:messages");
		} finally {
			EntityManagerUtil.close(conn);
		}
	}

	public Date getFechaInicial() {
		return fechaInicial;
	}

	public void setFechaInicial(Date fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	public Date getFechaFinal() {
		return fechaFinal;
	}

	public void setFechaFinal(Date fechaFinal) {
		this.fechaFinal = fechaFinal;
	}
	
	public ConstanciaDeDepositoDAO getConstanciaDeDepositoDAO() {
		return constanciaDeDepositoDAO;
	}

	public void setConstanciaDeDepositoDAO(ConstanciaDeDepositoDAO constanciaDeDepositoDAO) {
		this.constanciaDeDepositoDAO = constanciaDeDepositoDAO;
	}
	
	public String getFolio() {
		return folio;
	}

	public void setFolio(String folio) {
		this.folio = folio;
	}

	public List<ConstanciaDeDeposito> getListadoConstanciaDeDepositos() {
		return listadoConstanciaDeDepositos;
	}

	public void setListadoConstanciaDeDepositos(List<ConstanciaDeDeposito> listadoConstanciaDeDepositos) {
		this.listadoConstanciaDeDepositos = listadoConstanciaDeDepositos;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public List<Cliente> getListadoClientes() {
		return listadoClientes;
	}

	public void setListadoClientes(List<Cliente> listadoClientes) {
		this.listadoClientes = listadoClientes;
	}

	public ConstanciaDeDeposito getSelectConstanciaDD() {
		return selectConstanciaDD;
	}

	public void setSelectConstanciaDD(ConstanciaDeDeposito selectConstanciaDD) {
		this.selectConstanciaDD = selectConstanciaDD;
	}

	public List<Producto> getListadoProductoPorCliente() {
		return listadoProductoPorCliente;
	}

	public void setListadoProductoPorCliente(List<Producto> listadoProductoPorCliente) {
		this.listadoProductoPorCliente = listadoProductoPorCliente;
	}

	public Producto getProductoSelect() {
		return productoSelect;
	}

	public void setProductoSelect(Producto productoSelect) {
		this.productoSelect = productoSelect;
	}

	public Partida getPartidaSelect() {
		return partidaSelect;
	}

	public void setPartidaSelect(Partida partidaSelect) {
		this.partidaSelect = partidaSelect;
	}

	public List<PrecioServicio> getListadoPrecioServicio() {
		return listadoPrecioServicio;
	}

	public void setListadoPrecioServicio(List<PrecioServicio> listadoPrecioServicio) {
		this.listadoPrecioServicio = listadoPrecioServicio;
	}
	
	public List<ConstanciaDepositoDetalle> getListadoConstanciaDepositoDetalle() {
		return listadoConstanciaDepositoDetalle;
	}

	public void setListadoConstanciaDepositoDetalle(List<ConstanciaDepositoDetalle> listadoConstanciaDepositoDetalle) {
		this.listadoConstanciaDepositoDetalle = listadoConstanciaDepositoDetalle;
	}

	public Servicio getServicioSelected() {
		return servicioSelected;
	}

	public void setServicioSelected(Servicio servicioSelected) {
		this.servicioSelected = servicioSelected;
	}
	
	public BigDecimal getServicioCantidad() {
		return servicioCantidad;
	}

	public void setServicioCantidad(BigDecimal servicioCantidad) {
		this.servicioCantidad = servicioCantidad;
	}

	public ConstanciaDepositoDetalle getConstanciaSelect() {
		return constanciaSelect;
	}

	public void setConstanciaSelect(ConstanciaDepositoDetalle constanciaSelect) {
		this.constanciaSelect = constanciaSelect;
	}

	public BigDecimal getCantidadServicio() {
		return cantidadServicio;
	}

	public void setCantidadServicio(BigDecimal cantidadServicio) {
		this.cantidadServicio = cantidadServicio;
	}
	
	public Date getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Date getMaxDate() {
		return maxDate;
	}

	public void setMaxDate(Date maxDate) {
		this.maxDate = maxDate;
	}

	public List<Aviso> getListaAvisos() {
		return listaAvisos;
	}

	public void setListaAvisos(List<Aviso> listaAvisos) {
		this.listaAvisos = listaAvisos;
	}

	public Integer getCantidadTotal() {
		return cantidadTotal;
	}

	public void setCantidadTotal(Integer cantidadTotal) {
		this.cantidadTotal = cantidadTotal;
	}

	public BigDecimal getPesoTotal() {
		return pesoTotal;
	}

	public void setPesoTotal(BigDecimal pesoTotal) {
		this.pesoTotal = pesoTotal;
	}

	public BigDecimal getTarimasTotal() {
		return tarimasTotal;
	}

	public void setTarimasTotal(BigDecimal tarimasTotal) {
		this.tarimasTotal = tarimasTotal;
	}

	public DetallePartida getDetallePartida() {
		return detallePartida;
	}

	public void setDetallePartida(DetallePartida detallePartida) {
		this.detallePartida = detallePartida;
	}

	public StreamedContent getFile() {
		return file;
	}

	public void setFile(StreamedContent file) {
		this.file = file;
	}

	public Tarima getTarima() {
		return tarima;
	}

	public void setTarima(Tarima tarima) {
		this.tarima = tarima;
	}

	public List<Tarima> getTarimas() {
		return tarimas;
	}

	public void setTarimas(List<Tarima> tarimas) {
		this.tarimas = tarimas;
	}
	
	public Tarima getNuevaTarima() {
		return nuevaTarima;
	}

	public void setNuevaTarima(Tarima nuevaTarima) {
		this.nuevaTarima = nuevaTarima;
	}
	
	public UnidadDeManejo getUnidadSelect() {
		return unidadSelect;
	}

	public void setUnidadSelect(UnidadDeManejo unidadSelect) {
		this.unidadSelect = unidadSelect;
	}

	public List<UnidadDeManejo> getUnidades() {
		return unidades;
	}

	public void setUnidades(List<UnidadDeManejo> unidades) {
		this.unidades = unidades;
	}
}