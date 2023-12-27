package mx.com.ferbo.controller;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.ConstanciaSalidaDAO;
import mx.com.ferbo.dao.ConstanciaServicioDAO;
import mx.com.ferbo.dao.DetallePartidaDAO;
import mx.com.ferbo.dao.EstadoInventarioDAO;
import mx.com.ferbo.dao.InventarioDAO;
import mx.com.ferbo.dao.PartidaDAO;
import mx.com.ferbo.dao.PlantaDAO;
import mx.com.ferbo.dao.PrecioServicioDAO;
import mx.com.ferbo.dao.SerieConstanciaDAO;
import mx.com.ferbo.dao.StatusConstanciaSalidaDAO;
import mx.com.ferbo.dao.TipoMovimientoDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ConstanciaDeDeposito;
import mx.com.ferbo.model.ConstanciaDeServicio;
import mx.com.ferbo.model.ConstanciaSalida;
import mx.com.ferbo.model.ConstanciaSalidaServicios;
import mx.com.ferbo.model.ConstanciaSalidaServiciosPK;
import mx.com.ferbo.model.ConstanciaServicioDetalle;
import mx.com.ferbo.model.DetalleConstanciaSalida;
import mx.com.ferbo.model.DetallePartida;
import mx.com.ferbo.model.DetallePartidaPK;
import mx.com.ferbo.model.EstadoConstancia;
import mx.com.ferbo.model.EstadoInventario;
import mx.com.ferbo.model.Inventario;
import mx.com.ferbo.model.Partida;
import mx.com.ferbo.model.PartidaServicio;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.PrecioServicio;
import mx.com.ferbo.model.SerieConstancia;
import mx.com.ferbo.model.SerieConstanciaPK;
import mx.com.ferbo.model.Servicio;
import mx.com.ferbo.model.StatusConstanciaSalida;
import mx.com.ferbo.model.TipoMovimiento;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.InventarioException;
import mx.com.ferbo.util.JasperReportUtil;
import mx.com.ferbo.util.conexion;

@Named
@ViewScoped

public class AltaConstanciaSalidaBean implements Serializable{
	
	private static final long serialVersionUID = -1785488265380235016L;
	
	private static Logger log = LogManager.getLogger(AltaConstanciaSalidaBean.class);
	
	private List<Cliente> listadoClientes;
	private Cliente clienteSelect;
	
	private List<Planta> listadoPlantas;
	private PlantaDAO plantaDAO;
	private Planta plantaSelect;
	
	private List<ConstanciaSalida> listadoConstanciasSalidas;
	private ConstanciaSalidaDAO constanciaSalidaDAO;
	
	private List<ConstanciaDeDeposito> listadoConstanciaDD;
	
	private List<PrecioServicio> listadoPrecioServicios;
	private PrecioServicioDAO preciosServicioDAO;
	private List<PrecioServicio> serviciosCliente;
	private PrecioServicio servicioClienteSelect;
	
	private List<ConstanciaSalidaServicios> listadoConstanciaSalidaServicios;//listadoConstanciaDepositoDetalle
	
	private Partida partidaSelect;
	private List<Partida> listadoPartida;
	
	private List<DetalleConstanciaSalida> listadoDetalleConstanciaSalida;
	private List<DetalleConstanciaSalida> listadoTemp;
	
	private DetallePartida detallePartida;
	private List<DetallePartida> detallePartidaLista;
	
	private DetallePartidaDAO detallePartidaDAO;
	
	private List<Inventario> listaInventario;
	private List<Inventario> listaInventarioCopy;
	private List<Inventario> filteredInventario;
	private List<String> listaEntradas;
	private List<Date> listaIngresos;

	private InventarioDAO inventarioDAO;
	private Inventario inventarioSelected;
	
	private ConstanciaDeServicio constanciaDeServicio;
	private ConstanciaServicioDAO constanciaServicioDAO;
	
	private String numFolio,nombreTransportista,placas,observaciones,temperatura;
	private BigDecimal cantidadServicio;
	private Date fechaSalida;
	private int cantidadTotal;
	private BigDecimal pesoTotal;
	
	private DetalleConstanciaSalida detalleSalida;
	private int tmpIdDetalleSalida = 0;
	private ConstanciaSalidaServicios constanciaSalidaServicio;
	private SerieConstanciaDAO serieConstanciaDAO;
	private SerieConstancia serie;
	
	private StatusConstanciaSalida status;
	private StatusConstanciaSalidaDAO statusDAO;
	private PartidaDAO partidaDAO;
	
	private TipoMovimientoDAO tipoMovimientoDAO;
	private TipoMovimiento tpMovimientoSalida;
	
	private EstadoInventarioDAO estadoInventarioDAO;
	private EstadoInventario edoInventarioActual;
	private EstadoInventario edoInventarioHistorico;
	private List<Partida> partidaList;
	
	private boolean saved = false;
	
	private Usuario usuario;
	private FacesContext faceContext;
	private HttpServletRequest httpServletRequest;
	
	public String formatDate(Date date) {
	     DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");   
	     return dateFormat.format(date);
	}
	
	public AltaConstanciaSalidaBean() {
		constanciaDeServicio = new ConstanciaDeServicio();
		constanciaServicioDAO = new ConstanciaServicioDAO();
		
		listadoClientes = new ArrayList<>();
		
		plantaDAO = new PlantaDAO();
		listadoPlantas = new ArrayList<>();
		
		constanciaSalidaDAO = new ConstanciaSalidaDAO();
		listadoConstanciasSalidas = new ArrayList<>();
		
		listadoConstanciaDD = new ArrayList<>();
		listadoPrecioServicios = new ArrayList<>();
		serviciosCliente = new ArrayList<>();
		preciosServicioDAO = new PrecioServicioDAO();
		
		inventarioDAO = new InventarioDAO();
		listaInventario = new ArrayList<Inventario>();
		listaInventarioCopy = new ArrayList<>();
		
		detallePartidaDAO = new DetallePartidaDAO();
		serieConstanciaDAO = new SerieConstanciaDAO();
		statusDAO = new StatusConstanciaSalidaDAO();
		partidaDAO = new PartidaDAO();
		tipoMovimientoDAO = new TipoMovimientoDAO();
		estadoInventarioDAO = new EstadoInventarioDAO();
		
		listadoConstanciaSalidaServicios = new ArrayList<>();

		listadoPartida = new ArrayList<Partida>();
		listadoDetalleConstanciaSalida = new ArrayList<>();
		listadoTemp = new ArrayList<>();
		detallePartidaLista = new ArrayList<>();
	}
	
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		
		faceContext = FacesContext.getCurrentInstance();
		httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
		usuario = (Usuario) httpServletRequest.getSession(false).getAttribute("usuario");
		
//		listadoClientes = clienteDAO.buscarHabilitados(true, false);
		listadoClientes = (List<Cliente>) httpServletRequest.getSession(false).getAttribute("clientesActivosList");
		status = statusDAO.buscarPorId(1);//Por defecto, el catálogo de status constancia salida tiene el valor 1 para una constancia vigente.
		tpMovimientoSalida = tipoMovimientoDAO.buscarPorId(2);
		edoInventarioActual = estadoInventarioDAO.buscarPorId(1);
		edoInventarioHistorico = estadoInventarioDAO.buscarPorId(2);
		
		if((usuario.getPerfil() == 1)||(usuario.getPerfil() == 4)) {
			listadoPlantas.add(plantaDAO.buscarPorId(usuario.getIdPlanta()));
		}else {
			listadoPlantas = plantaDAO.findall(false);
		}
		plantaSelect = listadoPlantas.get(0);
		fechaSalida = new Date();
		
		this.cantidadTotal = 0;
		this.pesoTotal = new BigDecimal("0.000").setScale(3, BigDecimal.ROUND_HALF_UP);
	}
	
	public void validar() throws InventarioException {
		
		int contador = 0;
		SerieConstancia serie = new SerieConstancia();
		listadoConstanciasSalidas = constanciaSalidaDAO.buscarTodos();
		for(ConstanciaSalida cs: listadoConstanciasSalidas) {
			if(cs.getNumero().equals(numFolio)){
				FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,"ERROR FOLIO","El folio no esta disponible"));
				this.numFolio = null;
				PrimeFaces.current().ajax().update("form:folio");
				break;
			}
			contador = contador + 1;
			if(contador == listadoConstanciasSalidas.size()) {
				SerieConstanciaPK seriePK = new SerieConstanciaPK();
				
				seriePK.setCliente(this.clienteSelect);
				seriePK.setTpSerie("O");
				serie = serieConstanciaDAO.buscarPorId(seriePK);

				if (serie == null) {
					this.numFolio = "";
					throw new InventarioException("No se encontró información de los folios del cliente. Debe indicar manualmente un folio de constancia.");
				}
				this.serie = serie;
				FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,"FOLIO DISPONIBLE","El folio esta disponible"));
			}
		}
		PrimeFaces.current().ajax().update("form:messages");
		
	}
	
	public void cargaInfoCliente() {
		try {
			this.generaFolioSalida();//si el folio existe no ejecutar lo demas y mandar a llamar a validar 
			serviciosCliente = preciosServicioDAO.buscarPorCliente(clienteSelect.getCteCve(), true);
			listaInventario = inventarioDAO.buscar(clienteSelect, plantaSelect);
			listadoTemp = new ArrayList<DetalleConstanciaSalida>();
			listadoConstanciaSalidaServicios = new ArrayList<ConstanciaSalidaServicios>();
			partidaList = new ArrayList<Partida>();
			listaInventarioCopy = inventarioDAO.buscar(clienteSelect, plantaSelect);
			
			listaEntradas = new ArrayList<String>();
			for(Inventario i : listaInventario) {
				if(listaEntradas.contains(i.getFolioCliente()))
					continue;
				listaEntradas.add(i.getFolioCliente());
			}
			log.debug("Lista entradas: {}",listaEntradas);
			
			listaIngresos = new ArrayList<Date>();
			for(Inventario i : listaInventario) {
				if(listaIngresos.contains(i.getFechaIngreso()))
					continue;
				listaIngresos.add(i.getFechaIngreso());
			}
			log.debug("Lista fechas de ingreso: {}", listaIngresos);
			
		} catch(Exception ex) {
			log.error("Problema para cargar la información del cliente...", ex);
		}
	}
	
	public void generaFolioSalida() {
		SerieConstanciaPK seriePK = null;
		SerieConstancia serie = null;

		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Folio";

		try {
			if (this.clienteSelect == null)
				throw new InventarioException("Debe seleccionar un cliente");

			if (this.plantaSelect == null)
				throw new InventarioException("Debe seleccionar una planta");
			
			seriePK = new SerieConstanciaPK();
			seriePK.setCliente(this.clienteSelect);
			seriePK.setTpSerie("O");
			serie = serieConstanciaDAO.buscarPorId(seriePK);

			if (serie == null) {
				this.numFolio = "";
				throw new InventarioException(
						"No se encontró información de los folios del cliente. Debe indicar manualmente un folio de constancia.");
			}

			this.numFolio = String.format("%s%s%s%d", seriePK.getTpSerie(), plantaSelect.getPlantaSufijo(),
					clienteSelect.getCodUnico(), serie.getNuSerie());
			
			String folio = constanciaSalidaDAO.buscarPorNumero(numFolio);
			
			if(numFolio.equalsIgnoreCase(folio)) {
								
				throw new InventarioException("El folio ya existe");
			}
			
			this.serie = serie;

		} catch (InventarioException ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
			
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
		} catch (Exception ex) {
			log.error("Problema para generar el folio de entrada...", ex);
			mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;
			
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
		} finally {
			PrimeFaces.current().ajax().update(":form:messages", ":form:folio");
		}
	}
	
	public void agregaServicio() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Servicio";
		
		ConstanciaSalidaServicios constanciaSalidaServicios = null;
		ConstanciaSalidaServiciosPK constanciaSalidaServiciosPK = null;
		
		try {
			constanciaSalidaServicios = new ConstanciaSalidaServicios();
			constanciaSalidaServiciosPK = new ConstanciaSalidaServiciosPK();
			
			if(servicioClienteSelect == null)
				throw new InventarioException("Debe indicar un servicio.");
			
			if(cantidadServicio == null)
				throw new InventarioException("Debe indicar la cantidad del servicio solicitado.");
			
			if(cantidadServicio.compareTo(BigDecimal.ZERO) <= 0)
				throw new InventarioException("La cantidad del servicio indicada es incorrecta.");
			
			constanciaSalidaServiciosPK.setServicioCve(servicioClienteSelect.getServicio());
			constanciaSalidaServicios.setConstanciaSalidaServiciosPK(constanciaSalidaServiciosPK);
			constanciaSalidaServicios.setNumCantidad(cantidadServicio);
			
			listadoConstanciaSalidaServicios.add(constanciaSalidaServicios);
			log.debug(listadoConstanciaSalidaServicios);
			
		} catch (InventarioException ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
			
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
		} catch (Exception ex) {
			log.error("Problema para generar el folio de entrada...", ex);
			mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;
			
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
		} finally {
			PrimeFaces.current().ajax().update(":form:messages");
		}
		
	}
	
	public void newDetalleSalida(Inventario inventario){
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Agregar producto";
		
		Partida partida = null;
		List<DetallePartida> detallePartidaList = null;
		DetallePartida detallePartidaAnterior = null;
		DetallePartidaPK dpPK = null;
		DetallePartida detallePartida = null;
		Integer detPartCve = null;
		
		try {
			this.inventarioSelected = inventario;
			List<DetalleConstanciaSalida> lstDcsRegistrada = this.listadoTemp.stream()
					.filter(d -> d.getPartidaCve().getPartidaCve() == inventario.getPartidaCve())
					.collect(Collectors.toList())
					;
			DetalleConstanciaSalida dcsRegistrada = null;
			if(lstDcsRegistrada != null && lstDcsRegistrada.size() > 0)
				dcsRegistrada = lstDcsRegistrada.get(0);
			
			if(dcsRegistrada != null)
				throw new InventarioException("El producto seleccionado ya está registrado.");
			
			partida = partidaDAO.buscarPorId(inventario.getPartidaCve(), true);
			detallePartidaList = partida.getDetallePartidaList();
			detallePartidaAnterior = detallePartidaList.get(detallePartidaList.size() - 1);
			detPartCve = detallePartidaAnterior.getDetallePartidaPK().getDetPartCve() + 1;

			detallePartida = detallePartidaAnterior.clone();
			detallePartidaAnterior.setEdoInvCve(edoInventarioHistorico);
			
			dpPK = detallePartidaAnterior.getDetallePartidaPK().clone();
			dpPK.setDetPartCve(detPartCve);
			detallePartida.setDetallePartidaPK(dpPK);
			detallePartida.getDetallePartidaPK().setDetPartCve(detPartCve);
			detallePartida.setDetallePartida(detallePartidaAnterior);
			detallePartida.setTipoMovCve(tpMovimientoSalida);
			detallePartida.setEdoInvCve(edoInventarioActual);
			
			partida.add(detallePartida);
			
			this.detalleSalida = new DetalleConstanciaSalida();                                                                                 
			this.detalleSalida.setPartidaCve(partida);
			this.detalleSalida.setDetPartCve(detallePartida.getDetallePartidaPK().getDetPartCve());
			//this.detalleSalida.setDetallePartida(detallePartida);
			this.detalleSalida.setCantidad(inventario.getCantidad());
			this.detalleSalida.setPeso(inventario.getPeso());
			this.detalleSalida.setUnidad(inventario.getUnidadManejo().getUnidadDeManejoDs());
			this.detalleSalida.setCamaraCve(inventario.getCamara().getCamaraCve());
			this.detalleSalida.setProducto(inventario.getProducto().getProductoDs());
			this.detalleSalida.setFolioEntrada(inventario.getFolioCliente());
			this.detalleSalida.setCamaraCadena(inventario.getCamara().getCamaraDs());
			this.detalleSalida.setId(this.tmpIdDetalleSalida++);
			partida.add(detalleSalida);
			
			this.partidaList.add(partida);
			mensaje = "Indique la cantidad y temperatura.";
			severity = FacesMessage.SEVERITY_INFO;
			
		} catch (InventarioException ex) {
			
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
			
			PrimeFaces.current().executeScript("PF('dg-cantidad-producto').hide()");
		} catch (Exception ex) {
			log.error("Problema para generar el folio de entrada...", ex);
			
			mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;
			
			PrimeFaces.current().executeScript("PF('dg-cantidad-producto').hide()");
		} finally {
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			
			PrimeFaces.current().ajax().update("form:messages", "form:pnl-cantidad-producto", "form:det-cantidad", "form:det-peso", "form:-det-temperatura");
		}
	}
	
	public void cancelaDetalleSalida() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Producto";
		
		try {
			log.debug("Cancela la captura del detalle de salida.");
			this.detalleSalida = new DetalleConstanciaSalida();
			this.detalleSalida.setCantidad(0);
			this.detalleSalida.setPeso(BigDecimal.ZERO);
			
			PrimeFaces.current().executeScript("PF('dg-cantidad-producto').hide()");
		} catch (Exception ex) {
			log.error("Problema para generar el folio de entrada...", ex);
			mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.";
			
			severity = FacesMessage.SEVERITY_ERROR;
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
		} finally {
			PrimeFaces.current().ajax().update("form:messages");
		}
		
	}
	
	public void addDetalleSalida() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Producto";
		List<DetalleConstanciaSalida> lista = null;
		
		
		try {
			if(this.detalleSalida.getTemperatura() == null)
				throw new InventarioException("Debe indicar una temperatura.");
			
			if("".equalsIgnoreCase(this.detalleSalida.getTemperatura()))
				throw new InventarioException("Debe indicar una temperatura.");
			
			if(this.detalleSalida.getCantidad() <= 0)
				throw new InventarioException("Debe indicar una cantidad correcta.");
			
			if(this.detalleSalida.getCantidad() > this.inventarioSelected.getCantidad())
				throw new InventarioException("La cantidad indicada es mayor al inventario disponible.");
			
			if(listadoTemp == null)
				this.listadoTemp = new ArrayList<DetalleConstanciaSalida>();
			
			lista = listadoTemp.stream().filter(d -> d.getPartidaCve().getPartidaCve() == detalleSalida.getPartidaCve().getPartidaCve())
					.collect(Collectors.toList());
			
			if(lista.size() > 0)
				throw new InventarioException("El producto seleccionado ya está registrado");
			
			listadoTemp.add(this.detalleSalida);
			
			
			this.cantidadTotal = 0;
			this.pesoTotal = new BigDecimal("0.000").setScale(3, BigDecimal.ROUND_HALF_UP);
			for(DetalleConstanciaSalida dcs : listadoTemp ) {
				this.cantidadTotal += dcs.getCantidad();
				this.pesoTotal = this.pesoTotal.add(dcs.getPeso());
			}
			
			PrimeFaces.current().executeScript("PF('dg-cantidad-producto').hide()");
			mensaje = "El producto se registró correctamente.";
			severity = FacesMessage.SEVERITY_INFO;
			
			listaInventario.remove(inventarioSelected);
			
		} catch (InventarioException ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} catch (Exception ex) {
			log.error("Problema para generar el folio de entrada...", ex);
			mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages","form:dt-salidas");
		}
	}
	
	
	public void calculoPesoSalida() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		
		BigDecimal peso = null;
		BigDecimal cantidad = null;
		BigDecimal pesoUnitario = null;
		BigDecimal pesoSalida = null;
		
		try {
			if(this.detalleSalida.getCantidad() > this.inventarioSelected.getCantidad())
				throw new InventarioException("La cantidad indicada es mayor a la disponible en inventario.");
			
			log.debug("Calculando peso...");
			peso = this.inventarioSelected.getPeso();
			
			cantidad = new BigDecimal(this.inventarioSelected.getCantidad()).setScale(3, BigDecimal.ROUND_HALF_UP);
			pesoUnitario = peso.divide(cantidad, BigDecimal.ROUND_HALF_UP);
			pesoSalida = pesoUnitario
					.multiply(new BigDecimal(this.detalleSalida.getCantidad()).setScale(3, BigDecimal.ROUND_HALF_UP))
					.setScale(3, BigDecimal.ROUND_HALF_UP);
		} catch (InventarioException ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
			
			message = new FacesMessage(severity, "Aviso", mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
		} catch(Exception ex) {
			pesoSalida = new BigDecimal("0.000").setScale(3, BigDecimal.ROUND_HALF_UP);
		} finally {
			log.debug("Peso calculado: {}", pesoSalida);
			if(this.detalleSalida != null)
				this.detalleSalida.setPeso(pesoSalida);
			PrimeFaces.current().ajax().update("form:det-peso", ":form:messages");
		}
	}
	
	public void deleteDetalleConstanciaSalida(DetalleConstanciaSalida detalle) {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		
		try {
			if(this.detalleSalida == null)
				throw new InventarioException("Debe indicar un producto.");
			
			this.listadoTemp.remove(detalleSalida);
			
			this.cantidadTotal = 0;
			this.pesoTotal = new BigDecimal("0.000").setScale(3, BigDecimal.ROUND_HALF_UP);
			for(DetalleConstanciaSalida dcs : listadoTemp ) {
				this.cantidadTotal += dcs.getCantidad();
				this.pesoTotal = this.pesoTotal.add(dcs.getPeso());
			}
		
			detalleSalida.getProducto();
			detalleSalida.getFolioEntrada();
			
			Inventario inventarioTemp = new Inventario();
			
			for(Inventario i: listaInventarioCopy) {				
				if((i.getProducto().getProductoDs().equals(detalleSalida.getProducto())) && (i.getFolioCliente().equals(detalleSalida.getFolioEntrada()))   ) {
					inventarioTemp = i;
					break;
				}				
			}
			
			listaInventario.add(inventarioTemp);
			
		} catch (InventarioException ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
			
			message = new FacesMessage(severity, "Productos", mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
		} catch (Exception ex) {
			log.error("Problema para eliminar el producto de la salida...", ex);
			mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;
			
			message = new FacesMessage(severity, "Productos", mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
		} finally {
			PrimeFaces.current().ajax().update("form:messages","form:dt-salidas");
		}
	}
	
	public void deleteConstanciaSalidaServicio(ConstanciaSalidaServicios csServicio) {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		
		try {
			if(this.detalleSalida == null)
				throw new InventarioException("Debe indicar un servicio.");
			
			this.listadoConstanciaSalidaServicios.remove(csServicio);
		} catch (InventarioException ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
			
			message = new FacesMessage(severity, "Servicio", mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
		} catch (Exception ex) {
			log.error("Problema para eliminar el servicio de la salida...", ex);
			mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;
			
			message = new FacesMessage(severity, "Productos", mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
		} finally {
			PrimeFaces.current().ajax().update("form:messages","form:dt-salidas");
		}
	}
	
	public void resetCantidadServicio() {
		this.cantidadServicio = new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP);
		log.debug("Reiniciando el valor de cantidad total (servicio)");
	}
	
	public void saveConstanciaSalida() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Guardar constancia";
		
		ConstanciaSalida constancia = new ConstanciaSalida();
		
		try {
			
			if(saved)
				throw new InventarioException("La constancia ya está registrada.");
			constancia.setFecha(fechaSalida);
			constancia.setNumero(numFolio);
			constancia.setClienteCve(clienteSelect);
			constancia.setNombreCte(clienteSelect.getCteNombre());
			constancia.setStatus(status);
			constancia.setObservaciones(observaciones);
			constancia.setNombreTransportista(nombreTransportista);
			constancia.setPlacasTransporte(placas);
			constancia.setConstanciaSalidaServiciosList(listadoConstanciaSalidaServicios);
			constancia.setDetalleConstanciaSalidaList(listadoTemp);
			
	 		for(ConstanciaSalidaServicios c: listadoConstanciaSalidaServicios) {
	 			Servicio servicioCve = c.getConstanciaSalidaServiciosPK().getServicioCve();
	 			c.getConstanciaSalidaServiciosPK().setConstanciaSalidaCve(constancia);
	 			c.setIdConstancia(constancia);
	 			c.setServicioCve(servicioCve);
	 			log.debug("ConstanciaSalidaServicio: {}", c);
			}
	 		
	 		for(DetalleConstanciaSalida d: listadoTemp) {
	 			log.debug("Partida: {}", d.getPartidaCve());
	 		}
	 		
	 		for(DetalleConstanciaSalida d: listadoTemp) {
	 			d.setId(null);
	 			d.setConstanciaCve(constancia);
	 			List<DetallePartida> detallePartidaList = d.getPartidaCve().getDetallePartidaList();
	 			DetallePartida detallePartida = detallePartidaList.get(detallePartidaList.size() - 1);
	 			detallePartidaDAO.guardar(detallePartida);
	 		}
	 		constanciaSalidaDAO.guardar(constancia); //REGISTRO LA CONTSANCIA SALIDA
	 		Integer numeroSerie = this.serie.getNuSerie() + 1;
			this.serie.setNuSerie(numeroSerie);
			serieConstanciaDAO.actualizar(this.serie);
	 		
	 		if(!(constancia.getConstanciaSalidaServiciosList().isEmpty())) {
	 			
	 			EstadoConstancia estadoConstancia = new EstadoConstancia();
	 			estadoConstancia.setEdoCve(1);
	 			estadoConstancia.setDescripcion("NUEVA");
	 			
	 			constanciaDeServicio.setCteCve(clienteSelect);
	 			constanciaDeServicio.setFecha(fechaSalida);
	 			constanciaDeServicio.setNombreTransportista(nombreTransportista);
	 			constanciaDeServicio.setPlacasTransporte(placas);
	 			constanciaDeServicio.setObservaciones(String.format("Salida %s - %s",this.numFolio , observaciones));
	 			constanciaDeServicio.setFolioCliente("S"+constancia.getNumero());
	 			constanciaDeServicio.setValorDeclarado(new BigDecimal(1));
	 			constanciaDeServicio.setStatus(estadoConstancia);
	 			
	 			List<ConstanciaServicioDetalle> constanciaServicioDetalles = new ArrayList<ConstanciaServicioDetalle>(); 
	 			List<PartidaServicio> partidaServicios = new ArrayList<PartidaServicio>(); 
	 			
	 			for(ConstanciaSalidaServicios css:listadoConstanciaSalidaServicios) {
	 				ConstanciaServicioDetalle constanciaServicioDetalle = new ConstanciaServicioDetalle();
	 				constanciaServicioDetalle.setServicioCve(css.getConstanciaSalidaServiciosPK().getServicioCve());
	 				constanciaServicioDetalle.setFolio(constanciaDeServicio);
	 				constanciaServicioDetalle.setServicioCantidad(css.getNumCantidad());
	 				constanciaServicioDetalles.add(constanciaServicioDetalle);
	 			}
	 			constanciaDeServicio.setConstanciaServicioDetalleList(new ArrayList<>());
	 			constanciaDeServicio.setConstanciaServicioDetalleList(constanciaServicioDetalles);
	 			
	 			for(DetalleConstanciaSalida dcs: listadoTemp) {
	 				
	 				PartidaServicio partidaS = new PartidaServicio();
	 				partidaS.setFolio(constanciaDeServicio);
	 				partidaS.setCantidadDeCobro(dcs.getPeso());
	 				partidaS.setCantidadTotal(dcs.getCantidad());
	 				partidaS.setProductoCve(dcs.getPartidaCve().getUnidadDeProductoCve().getProductoCve());
	 				partidaS.setUnidadDeManejoCve(dcs.getPartidaCve().getUnidadDeProductoCve().getUnidadDeManejoCve());
	 				partidaS.setUnidadDeCobro(dcs.getPartidaCve().getUnidadDeProductoCve().getUnidadDeManejoCve());
	 				partidaServicios.add(partidaS);
	 			}
	 			
	 			constanciaDeServicio.setPartidaServicioList(new ArrayList<>());
	 			constanciaDeServicio.setPartidaServicioList(partidaServicios);
	 			
	 			if(constanciaServicioDAO.guardar(constanciaDeServicio)==null) {
	 				System.out.println("se guardo correctamente la constancia de servicio");
	 			}
	 			
	 		}
	 		saved = true;
	 		
	 		mensaje = "La constancia de salida se guardó correctamente.";
			severity = FacesMessage.SEVERITY_INFO;
		} catch (InventarioException ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} catch (Exception ex) {
			log.error("Problema para eliminar el servicio de la salida...", ex);
			mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			
			PrimeFaces.current().ajax().update("form:messages");
		}
	}
	
	public void nuevoRegistro() {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	    try {
			ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
		} catch (IOException e) {
			log.error("Problema para crear una nueva constancia de salida...",  e);
		}
		
	}
	
	public void imprimirTicket() throws Exception{
		
		String jasperPath = "/jasper/ConstanciaSalida.jrxml";
		String filename = "ticket.pdf";
		String images = "/images/logoF.png";
		String message = null;
		Severity severity = null;
		ConstanciaSalida constancia = null;
		File reportFile = new File(jasperPath);
		File imgFile = null;
		JasperReportUtil jasperReportUtil = new JasperReportUtil();
		Map<String, Object> parameters = new HashMap<String, Object>();
		Connection connection = null;
		parameters = new HashMap<String, Object>();
		try {
			
			URL resource = getClass().getResource(jasperPath);//verifica si el recurso esta disponible 
			URL resourceimg = getClass().getResource(images); 
			String file = resource.getFile();//retorna la ubicacion del archivo
			String img = resourceimg.getFile();
			reportFile = new File(file);//crea un archivo
			imgFile = new File(img);
			constancia = new ConstanciaSalida();
			constancia.setNumero(this.numFolio);
			numFolio = String.valueOf(getNumFolio());
			connection = EntityManagerUtil.getConnection();
			parameters.put("REPORT_CONNECTION", connection);
			parameters.put("NUMERO", numFolio);
			parameters.put("LogoPath", imgFile.getPath());
			jasperReportUtil.createPdf(filename, parameters, reportFile.getPath());
			   
		} catch (Exception e) {
			e.printStackTrace();
			message = String.format("No se pudo imprimir el folio %s", this.numFolio);
			severity = FacesMessage.SEVERITY_INFO;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity,"Error en impresion",message));
			PrimeFaces.current().ajax().update("form:messages");
			
		}finally {
			conexion.close((Connection) connection);
		}
		
		
	}

	public List<Cliente> getListadoClientes() {
		return listadoClientes;
	}

	public void setListadoClientes(List<Cliente> listadoClientes) {
		this.listadoClientes = listadoClientes;
	}

	public Cliente getClienteSelect() {
		return clienteSelect;
	}

	public void setClienteSelect(Cliente clienteSelect) {
		this.clienteSelect = clienteSelect;
	}

	public List<Planta> getListadoPlantas() {
		return listadoPlantas;
	}

	public void setListadoPlantas(List<Planta> listadoPlantas) {
		this.listadoPlantas = listadoPlantas;
	}

	public PlantaDAO getPlantaDAO() {
		return plantaDAO;
	}

	public void setPlantaDAO(PlantaDAO plantaDAO) {
		this.plantaDAO = plantaDAO;
	}

	public Planta getPlantaSelect() {
		return plantaSelect;
	}

	public void setPlantaSelect(Planta plantaSelect) {
		this.plantaSelect = plantaSelect;
	}

	public Date getFechaSalida() {
		return fechaSalida;
	}

	public void setFechaSalida(Date fechaSalida) {
		this.fechaSalida = fechaSalida;
	}

	public String getNumFolio() {
		return numFolio;
	}

	public void setNumFolio(String numFolio) {
		this.numFolio = numFolio;
	}
	
	public List<ConstanciaSalida> getListadoConstanciasSalidas() {
		return listadoConstanciasSalidas;
	}

	public void setListadoConstanciasSalidas(List<ConstanciaSalida> listadoConstanciasSalidas) {
		this.listadoConstanciasSalidas = listadoConstanciasSalidas;
	}

	public List<ConstanciaDeDeposito> getListadoConstanciaDD() {
		return listadoConstanciaDD;
	}

	public void setListadoConstanciaDD(List<ConstanciaDeDeposito> listadoConstanciaDD) {
		this.listadoConstanciaDD = listadoConstanciaDD;
	}

	public List<PrecioServicio> getListadoPrecioServicios() {
		return listadoPrecioServicios;
	}

	public void setListadoPrecioServicios(List<PrecioServicio> listadoPrecioServicios) {
		this.listadoPrecioServicios = listadoPrecioServicios;
	}

	public PrecioServicioDAO getPreciosServicioDAO() {
		return preciosServicioDAO;
	}

	public void setPreciosServicioDAO(PrecioServicioDAO preciosServicioDAO) {
		this.preciosServicioDAO = preciosServicioDAO;
	}

	public List<PrecioServicio> getServiciosCliente() {
		return serviciosCliente;
	}

	public void setServiciosCliente(List<PrecioServicio> serviciosCliente) {
		this.serviciosCliente = serviciosCliente;
	}

	public PrecioServicio getServicioClienteSelect() {
		return servicioClienteSelect;
	}

	public void setServicioClienteSelect(PrecioServicio servicioClienteSelect) {
		this.servicioClienteSelect = servicioClienteSelect;
	}

	public List<ConstanciaSalidaServicios> getListadoConstanciaSalidaServicios() {
		return listadoConstanciaSalidaServicios;
	}

	public void setListadoConstanciaSalidaServicios(List<ConstanciaSalidaServicios> listadoConstanciaSalidaServicios) {
		this.listadoConstanciaSalidaServicios = listadoConstanciaSalidaServicios;
	}

	public BigDecimal getCantidadServicio() {
		return cantidadServicio;
	}

	public void setCantidadServicio(BigDecimal cantidadServicio) {
		this.cantidadServicio = cantidadServicio;
	}

	public Partida getPartidaSelect() {
		return partidaSelect;
	}

	public void setPartidaSelect(Partida partidaSelect) {
		this.partidaSelect = partidaSelect;
	}

	public List<Partida> getListadoPartida() {
		return listadoPartida;
	}

	public void setListadoPartida(List<Partida> listadoPartida) {
		this.listadoPartida = listadoPartida;
	}

	public String getNombreTransportista() {
		return nombreTransportista;
	}

	public void setNombreTransportista(String nombreTransportista) {
		this.nombreTransportista = nombreTransportista;
	}

	public String getPlacas() {
		return placas;
	}

	public void setPlacas(String placas) {
		this.placas = placas;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getTemperatura() {
		return temperatura;
	}

	public void setTemperatura(String temperatura) {
		this.temperatura = temperatura;
	}

	public List<DetalleConstanciaSalida> getListadoDetalleConstanciaSalida() {
		return listadoDetalleConstanciaSalida;
	}

	public void setListadoDetalleConstanciaSalida(List<DetalleConstanciaSalida> listadoDetalleConstanciaSalida) {
		this.listadoDetalleConstanciaSalida = listadoDetalleConstanciaSalida;
	}
	
	public int getCantidadTotal() {
		return cantidadTotal;
	}

	public void setCantidadTotal(int cantidadTotal) {
		this.cantidadTotal = cantidadTotal;
	}

	public BigDecimal getPesoTotal() {
		return pesoTotal;
	}

	public void setPesoTotal(BigDecimal pesoTotal) {
		this.pesoTotal = pesoTotal;
	}

	public List<DetalleConstanciaSalida> getListadoTemp() {
		return listadoTemp;
	}

	public void setListadoTemp(List<DetalleConstanciaSalida> listadoTemp) {
		this.listadoTemp = listadoTemp;
	}
	
	public DetallePartida getDetallePartida() {
		return detallePartida;
	}

	public void setDetallePartida(DetallePartida detallePartida) {
		this.detallePartida = detallePartida;
	}

	public List<DetallePartida> getDetallePartidaLista() {
		return detallePartidaLista;
	}

	public void setDetallePartidaLista(List<DetallePartida> detallePartidaLista) {
		this.detallePartidaLista = detallePartidaLista;
	}
	
	public List<Inventario> getListaInventario() {
		return listaInventario;
	}

	public void setListaInventario(List<Inventario> listaInventario) {
		this.listaInventario = listaInventario;
	}

	public InventarioDAO getInventarioDAO() {
		return inventarioDAO;
	}

	public void setInventarioDAO(InventarioDAO inventarioDAO) {
		this.inventarioDAO = inventarioDAO;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public DetalleConstanciaSalida getDetalleSalida() {
		return detalleSalida;
	}

	public void setDetalleSalida(DetalleConstanciaSalida detalleSalida) {
		this.detalleSalida = detalleSalida;
	}

	public Inventario getInventarioSelected() {
		return inventarioSelected;
	}

	public void setInventarioSelected(Inventario inventarioSelected) {
		this.inventarioSelected = inventarioSelected;
	}

	public ConstanciaSalidaServicios getConstanciaSalidaServicio() {
		return constanciaSalidaServicio;
	}

	public void setConstanciaSalidaServicio(ConstanciaSalidaServicios constanciaSalidaServicio) {
		this.constanciaSalidaServicio = constanciaSalidaServicio;
	}

	public boolean isSaved() {
		return saved;
	}

	public void setSaved(boolean saved) {
		this.saved = saved;
	}

	public List<Inventario> getFilteredInventario() {
		return filteredInventario;
	}

	public void setFilteredInventario(List<Inventario> filteredInventario) {
		this.filteredInventario = filteredInventario;
	}

	public List<String> getListaEntradas() {
		return listaEntradas;
	}

	public void setListaEntradas(List<String> listaEntradas) {
		this.listaEntradas = listaEntradas;
	}

	public List<Date> getListaIngresos() {
		return listaIngresos;
	}

	public void setListaIngresos(List<Date> listaIngresos) {
		this.listaIngresos = listaIngresos;
	}

	public List<Inventario> getListaInventarioCopy() {
		return listaInventarioCopy;
	}

	public void setListaInventarioCopy(List<Inventario> listaInventarioCopy) {
		this.listaInventarioCopy = listaInventarioCopy;
	}
	
}
