package mx.com.ferbo.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
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
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import mx.com.ferbo.dao.AvisoDAO;
import mx.com.ferbo.dao.CamaraDAO;
import mx.com.ferbo.dao.ConstanciaDeDepositoDAO;
import mx.com.ferbo.dao.EstadoConstanciaDAO;
import mx.com.ferbo.dao.EstadoInventarioDAO;
import mx.com.ferbo.dao.IngresoDAO;
import mx.com.ferbo.dao.IngresoProductoDAO;
import mx.com.ferbo.dao.IngresoServicioDAO;
import mx.com.ferbo.dao.PlantaDAO;
import mx.com.ferbo.dao.PrecioServicioDAO;
import mx.com.ferbo.dao.ProductoClienteDAO;
import mx.com.ferbo.dao.TipoMovimientoDAO;
import mx.com.ferbo.dao.UnidadDeManejoDAO;
import mx.com.ferbo.dao.UnidadDeProductoDAO;
import mx.com.ferbo.model.Aviso;
import mx.com.ferbo.model.Camara;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ConstanciaDeDeposito;
import mx.com.ferbo.model.ConstanciaDepositoDetalle;
import mx.com.ferbo.model.DetallePartida;
import mx.com.ferbo.model.DetallePartidaPK;
import mx.com.ferbo.model.EstadoConstancia;
import mx.com.ferbo.model.EstadoInventario;
import mx.com.ferbo.model.Ingreso;
import mx.com.ferbo.model.IngresoProducto;
import mx.com.ferbo.model.IngresoServicio;
import mx.com.ferbo.model.Partida;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.PrecioServicio;
import mx.com.ferbo.model.Producto;
import mx.com.ferbo.model.ProductoPorCliente;
import mx.com.ferbo.model.TipoMovimiento;
import mx.com.ferbo.model.UnidadDeManejo;
import mx.com.ferbo.model.UnidadDeProducto;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.JasperReportUtil;
import mx.com.ferbo.util.conexion;

@Named
@ViewScoped
public class OrdenEntradaBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Logger log = LogManager.getLogger(OrdenEntradaBean.class);

	private List<Cliente> listaClientes;
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

	private StreamedContent file;

	private Boolean save;
	private Date fechaActual;
	private BigDecimal temperatura;
	private BigDecimal sumaTarimas;
	private BigDecimal sumaCantidad;
	private BigDecimal sumaPeso;
	private Boolean isCongelacion, isConservacion, isRefrigeracion, isManiobras;
	private int congelacion = 619, conservacion = 620, refrigeracion = 621, maniobras = 622;
	private BigDecimal cantidadServicio;

	FacesContext faceContext = null;
	HttpServletRequest request = null;
	HttpSession session = null;
	Usuario usuario = null;

	public OrdenEntradaBean() {

		listaClientes = new ArrayList<Cliente>();
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

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {

		faceContext = FacesContext.getCurrentInstance();
		request = (HttpServletRequest) faceContext.getExternalContext().getRequest();
		session = request.getSession(false);

		usuario = (Usuario) session.getAttribute("usuario");

//		listaClientes = clienteDAO.buscarTodos();
		listaClientes = (List<Cliente>) request.getSession(false).getAttribute("clientesActivosList");
		listaPlantas = plantaDAO.buscarTodos();
		listaUnidadDeManejo = unidadDeManejoDAO.buscarTodos();

		save = false;

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
			listaIngreso.clear();
			ingreso = new Ingreso();

			Date fechaActualIni = new Date(fechaActual.getTime());
			DateUtil.setTime(fechaActualIni, 0, 0, 0, 0);

			Date fechaActualFin = new Date(fechaActual.getTime());
			DateUtil.setTime(fechaActualFin, 23, 59, 59, 99);

			listaIngreso = ingresoDAO.buscarPorFechaCtePlanta(fechaActualIni, fechaActualFin, cliente.getCteCve(),
					usuario.getIdPlanta());

			severity = FacesMessage.SEVERITY_INFO;
			mensaje = "Selecciona Folio de Orden de Entrada";
		} catch (Exception e) {
			log.info("Error ...." + e.getMessage());

			severity = FacesMessage.SEVERITY_INFO;
			mensaje = "Error al encontrar ordenes de entrada";

		} finally {
			message = new FacesMessage(severity, "Orden Entrada", mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages");
		}

	}

	public void productoCte() {

		listaProductos.clear();
		listaProductoPorCte = productoClienteDAO.buscarPorCliente(cliente.getCteCve(), false);

		for (ProductoPorCliente pc : listaProductoPorCte) {
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

			for (int i = 0; i < tarima; i++) {
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
		PrimeFaces.current().ajax().update("form:messages", "form:aviso");

	}

	public void productoIngresoOrden() {

		listaIngresoProducto.clear();
		sumaTarimas = new BigDecimal(0);
		sumaCantidad = new BigDecimal(0);
		sumaPeso = new BigDecimal(0);

		List<IngresoProducto> listaTmpIngresoP = ingresoProductoDAO.buscarIngresosProductoPorId(ingreso.getIdIngreso());

		for (IngresoProducto ip : listaTmpIngresoP) {

			sumaTarimas = sumaTarimas.add(ip.getNoTarimas());
			sumaCantidad = sumaCantidad.add(new BigDecimal(ip.getCantidad()));
			sumaPeso = sumaPeso.add(ip.getPeso());

			listaIngresoProducto.add(ip);

		}

	}

	public void servicioIngresoOrden() {

		listaIngresoServicio.clear();
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
		precioServicioTemp = listaServicioUnidad.stream().filter(p -> (p.getServicio().getServicioCve() == congelacion
				|| p.getServicio().getServicioCve() == conservacion || p.getServicio().getServicioCve() == refrigeracion
				|| p.getServicio().getServicioCve() == maniobras)).collect(Collectors.toList());

		listaTmpServicios = precioServicioTemp;

		listaServicioUnidad.removeAll(precioServicioTemp);

		l = precioServicioTemp.stream().filter(ps -> ps.getServicio().getServicioCve() == 619)
				.collect(Collectors.toList());
		if (l.size() > 0) {
			this.isCongelacion = true;
		}

		l = precioServicioTemp.stream().filter(ps -> ps.getServicio().getServicioCve() == 620)
				.collect(Collectors.toList());
		if (l.size() > 0) {
			this.isConservacion = true;

			ingresoServicio.setServicio(l.get(0).getServicio());
			ingresoServicio.setCantidad(new BigDecimal(1));
			ingresoServicio.setUnidadDeManejo(l.get(0).getUnidad());
			ingresoServicio.setIngreso(ingreso);

			listaIngresoServicio.add(ingresoServicio);
		}

		l = precioServicioTemp.stream().filter(ps -> ps.getServicio().getServicioCve() == 621)
				.collect(Collectors.toList());
		if (l.size() > 0) {
			this.isRefrigeracion = true;
		}

		l = precioServicioTemp.stream().filter(ps -> ps.getServicio().getServicioCve() == 622)
				.collect(Collectors.toList());
		if (l.size() > 0) {
			this.isManiobras = true;
		}

	}

	public void saveIngresoServicio() {

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

		if (isCongelacion) {

			precioServicioTmp = listaTmpServicios.stream().filter(p -> p.getServicio().getServicioCve() == 619)
					.collect(Collectors.toList());

			if (precioServicioTmp.size() > 0) {

				pServicio = precioServicioTmp.get(0);

				ingresoServicio.setServicio(pServicio.getServicio());
				ingresoServicio.setCantidad(new BigDecimal(1));
				ingresoServicio.setUnidadDeManejo(pServicio.getUnidad());
				ingresoServicio.setIngreso(ingreso);

				listaIngresoServicio.add(ingresoServicio);
			} else {
				isCongelacion = false;

				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
						"Servicio", "Servicio no encontrado - contactar a contaduria "));
				PrimeFaces.current().ajax().update("form:messages");

			}
		} else {
			listaTmp = listaIngresoServicio.stream().filter(is -> is.getServicio().getServicioCve() == 619)
					.collect(Collectors.toList());
			if (listaTmp.size() > 0) {
				listaIngresoServicio.remove(listaTmp.get(0));
			}
		}
	}

	public void addConservacion() {

		IngresoServicio ingresoServicio = new IngresoServicio();
		List<PrecioServicio> precioServicioTmp = null;
		PrecioServicio pServicio = null;
		List<IngresoServicio> listaTmp = new ArrayList<IngresoServicio>();

		if (isConservacion) {

			precioServicioTmp = listaTmpServicios.stream().filter(p -> p.getServicio().getServicioCve() == 620)
					.collect(Collectors.toList());

			if (precioServicioTmp.size() > 0) {

				pServicio = precioServicioTmp.get(0);

				ingresoServicio.setServicio(pServicio.getServicio());
				ingresoServicio.setCantidad(new BigDecimal(1));
				ingresoServicio.setUnidadDeManejo(pServicio.getUnidad());
				ingresoServicio.setIngreso(ingreso);

				listaIngresoServicio.add(ingresoServicio);
			} else {
				isConservacion = false;

				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
						"Servicio", "Servicio no encontrado - contactar a contaduria "));
				PrimeFaces.current().ajax().update("form:messages");
			}
		} else {
			listaTmp = listaIngresoServicio.stream().filter(is -> is.getServicio().getServicioCve() == 620)
					.collect(Collectors.toList());
			if (listaTmp.size() > 0) {
				listaIngresoServicio.remove(listaTmp.get(0));
			}
		}

	}

	public void addRefrigeracion() {

		IngresoServicio ingresoServicio = new IngresoServicio();
		List<PrecioServicio> precioServicioTmp = null;
		List<IngresoServicio> listaTmp = new ArrayList<IngresoServicio>();
		PrecioServicio pServicio = null;

		if (isRefrigeracion) {

			precioServicioTmp = listaTmpServicios.stream().filter(p -> p.getServicio().getServicioCve() == 621)
					.collect(Collectors.toList());

			if (precioServicioTmp.size() > 0) {

				pServicio = precioServicioTmp.get(0);

				ingresoServicio.setServicio(pServicio.getServicio());
				ingresoServicio.setCantidad(new BigDecimal(1));
				ingresoServicio.setUnidadDeManejo(pServicio.getUnidad());
				ingresoServicio.setIngreso(ingreso);

				listaIngresoServicio.add(ingresoServicio);
			} else {
				isRefrigeracion = false;

				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
						"Servicio", "Servicio no encontrado - contactar a contaduria "));
				PrimeFaces.current().ajax().update("form:messages");
			}
		} else {
			listaTmp = listaIngresoServicio.stream().filter(is -> is.getServicio().getServicioCve() == 621)
					.collect(Collectors.toList());
			if (listaTmp.size() > 0) {
				listaIngresoServicio.remove(listaTmp.get(0));
			}
		}

	}

	public void addManiobras() {

		IngresoServicio ingresoServicio = new IngresoServicio();
		List<PrecioServicio> precioServicioTmp = null;
		List<IngresoServicio> listaTmp = new ArrayList<IngresoServicio>();
		PrecioServicio pServicio = null;

		if (isManiobras) {

			precioServicioTmp = listaTmpServicios.stream().filter(p -> p.getServicio().getServicioCve() == 622)
					.collect(Collectors.toList());

			if (precioServicioTmp.size() > 0) {

				pServicio = precioServicioTmp.get(0);

				ingresoServicio.setServicio(pServicio.getServicio());
				ingresoServicio.setCantidad(new BigDecimal(1));
				ingresoServicio.setUnidadDeManejo(pServicio.getUnidad());
				ingresoServicio.setIngreso(ingreso);

				listaIngresoServicio.add(ingresoServicio);
			} else {
				isManiobras = false;

				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
						"Servicio", "Servicio no encontrado - contactar a contaduria "));
				PrimeFaces.current().ajax().update("form:messages");
			}
		} else {
			listaTmp = listaIngresoServicio.stream().filter(is -> is.getServicio().getServicioCve() == 622)
					.collect(Collectors.toList());
			if (listaTmp.size() > 0) {
				listaIngresoServicio.remove(listaTmp.get(0));
			}
		}

	}

	public void eliminarIngresoServicio() {
		listaIngresoServicio.remove(selectIngresoServicio);

		if (selectIngresoServicio.getServicio().getServicioCve() == 619) {
			isCongelacion = false;
		}

		if (selectIngresoServicio.getServicio().getServicioCve() == 620) {
			isConservacion = false;
		}

		if (selectIngresoServicio.getServicio().getServicioCve() == 621) {
			isRefrigeracion = false;
		}

		if (selectIngresoServicio.getServicio().getServicioCve() == 622) {
			isManiobras = false;
		}

	}

	public void save() {

		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;

		try {

			// OBJETOS CONSTANCIA DE DEPOSITO
			ConstanciaDeDeposito constanciaDeDeposito = new ConstanciaDeDeposito();
			ConstanciaDeDepositoDAO constanciaDeDepositoDAO = new ConstanciaDeDepositoDAO();
			EstadoConstancia status = new EstadoConstancia();
			EstadoConstanciaDAO estadoConstanciaDAO = new EstadoConstanciaDAO();

			// OBJETOS UNIDAD DE PRODUCTO
			UnidadDeProducto unidadDeProducto = new UnidadDeProducto();
			UnidadDeProductoDAO unidadDeProductoDAO = new UnidadDeProductoDAO();

			constanciaDeDeposito.setCteCve(ingreso.getIdCliente());
			constanciaDeDeposito.setFechaIngreso(ingreso.getFechaHora());
			constanciaDeDeposito.setNombreTransportista(ingreso.getTransportista());
			constanciaDeDeposito.setPlacasTransporte(ingreso.getPlacas());
			constanciaDeDeposito.setObservaciones(ingreso.getObservaciones());
			constanciaDeDeposito.setFolioCliente("I" + ingreso.getFolio());
			constanciaDeDeposito.setValorDeclarado(new BigDecimal(0));

			status = estadoConstanciaDAO.buscarPorId(0);

			constanciaDeDeposito.setStatus(status);
			constanciaDeDeposito.setAvisoCve(avisoSelect);
			constanciaDeDeposito.setTemperatura(temperatura.toString());
			constanciaDeDeposito.setPartidaList(new ArrayList<Partida>());
			constanciaDeDeposito.setConstanciaDepositoDetalleList(new ArrayList<ConstanciaDepositoDetalle>());

			for (IngresoProducto ip : listaIngresoProducto) {

				Partida partida = new Partida();

				partida.setCamaraCve(camara);
				partida.setFolio(constanciaDeDeposito);
				partida.setPesoTotal(ip.getPeso());
				partida.setCantidadTotal(ip.getCantidad());

				unidadDeProducto = unidadDeProductoDAO.buscarPorProductoUnidad(ip.getProducto().getProductoCve(),
						ip.getUnidadDeManejo().getUnidadDeManejoCve());

				if (unidadDeProducto == null) {

					unidadDeProducto = new UnidadDeProducto();
					unidadDeProducto.setProductoCve(ip.getProducto());
					unidadDeProducto.setUnidadDeManejoCve(ip.getUnidadDeManejo());

					unidadDeProductoDAO.guardar(unidadDeProducto);

				} else {
					partida.setUnidadDeProductoCve(unidadDeProducto);// NO APARECE UNA UNIDAD DE PRODUCTO- QUE HACER EN
																		// ESE CASO? PORNER CONDICIONAL ¿?
				}

				partida.setCantidadDeCobro(ip.getPeso());
				partida.setUnidadDeCobro(ip.getUnidadDeManejo());
				partida.setPartidaSeq(0); // DUDA
				partida.setValorMercancia(new BigDecimal(1));
				// FALTA RENDIMIENTO
				partida.setNoTarimas(ip.getNoTarimas());

				partida.setDetallePartidaList(new ArrayList<DetallePartida>());

				DetallePartida detallePartida = new DetallePartida();
				DetallePartidaPK detallePartidaPK = new DetallePartidaPK();

				TipoMovimiento tipoMovimiento = new TipoMovimiento();
				TipoMovimientoDAO tipoMovimientoDAO = new TipoMovimientoDAO();
				EstadoInventario estadoInventario = new EstadoInventario();
				EstadoInventarioDAO estadoInventarioDAO = new EstadoInventarioDAO();

				tipoMovimiento = tipoMovimientoDAO.buscarPorId(1);
				estadoInventario = estadoInventarioDAO.buscarPorId(1);

				detallePartidaPK.setPartidaCve(partida);

				detallePartida.setDetallePartidaPK(detallePartidaPK);
				detallePartida.setTipoMovCve(tipoMovimiento);
				detallePartida.setEdoInvCve(estadoInventario);
				detallePartida.setCantidadUManejo(partida.getCantidadTotal());
				detallePartida.setUMedidaCve(partida.getUnidadDeProductoCve().getUnidadDeManejoCve());
				detallePartida.setCantidadUMedida(partida.getPesoTotal());
				detallePartida.setDtpLote(ip.getLote());
				detallePartida.setDtpCaducidad(ip.getFechaCaducidad());
				detallePartida.setDtpPO(ip.getOtro());
				detallePartida.setDtpPedimento(ip.getPedimento());
				detallePartida.setDtpSAP(ip.getContenedor());

				partida.getDetallePartidaList().add(detallePartida);
				constanciaDeDeposito.getPartidaList().add(partida);

			}

			for (IngresoServicio is : listaIngresoServicio) {

				ConstanciaDepositoDetalle constanciaDepositoDetalle = new ConstanciaDepositoDetalle();

				constanciaDepositoDetalle.setServicioCve(is.getServicio());
				constanciaDepositoDetalle.setFolio(constanciaDeDeposito);
				constanciaDepositoDetalle.setServicioCantidad(is.getCantidad());

				constanciaDeDeposito.getConstanciaDepositoDetalleList().add(constanciaDepositoDetalle);
			}

			constanciaDeDepositoDAO.guardar(constanciaDeDeposito);

			save = true;

			severity = FacesMessage.SEVERITY_INFO;
			mensaje = "La orden de entrada se registro correctamente";

		} catch (Exception e) {

			severity = FacesMessage.SEVERITY_ERROR;
			mensaje = "La orden de entrada no se pudo registrar";

			log.info("Error al guardar la Constancia de deposito " + e.getMessage());
		} finally {
			message = new FacesMessage(severity, "Orden de entrada", mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);

			PrimeFaces.current().ajax().update("form:messages");
		}

	}

	public void imprimirEntrada() throws Exception {

		String jasperPath = "/jasper/GestionReport.jrxml";
		String filename = "ticketEntrada.pdf";
		String images = "/images/logoF.png";
		String message = null;
		Severity severity = null;

		File reportFile = new File(jasperPath);
		File imgFile = null;
		JasperReportUtil jasperReportUtil = new JasperReportUtil();

		Map<String, Object> parameters = new HashMap<String, Object>();
		Connection connection = null;
		parameters = new HashMap<String, Object>();
		try {

			if (save == false) {
				message = "Debe guardar la orden de entrada";
				throw new Exception("Debe guardar la orden de entrada");
			}

			URL resource = getClass().getResource(jasperPath);// verifica si el recurso esta disponible
			URL resourceimg = getClass().getResource(images);
			String file = resource.getFile();// retorna la ubicacion del archivo
			String img = resourceimg.getFile();
			reportFile = new File(file);// crea un archivo
			imgFile = new File(img);
			log.info(reportFile.getPath());
			connection = EntityManagerUtil.getConnection();
			parameters.put("REPORT_CONNECTION", connection);
			parameters.put("FOLIO", "I" + this.ingreso.getFolio());
			parameters.put("LogoPath", imgFile.getPath());
			// jasperReportUtil.createPdf(filename, parameters, reportFile.getPath());
			byte[] bytes = jasperReportUtil.createPDF(parameters, reportFile.getPath());
			InputStream input = new ByteArrayInputStream(bytes);
			this.file = DefaultStreamedContent.builder().contentType("application/pdf").name(filename)
					.stream(() -> input).build();
			log.info("Orden de salida generada {}...", filename);

		} catch (Exception e) {
			e.printStackTrace();
			severity = FacesMessage.SEVERITY_INFO;
		} finally {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(severity, "Error en impresion", message));
			PrimeFaces.current().ajax().update("form:messages");
			conexion.close((Connection) connection);
		}

	}

	public void reload() throws IOException {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
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

	public BigDecimal getTemperatura() {
		return temperatura;
	}

	public void setTemperatura(BigDecimal temperatura) {
		this.temperatura = temperatura;
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

	public StreamedContent getFile() {
		return file;
	}

	public void setFile(StreamedContent file) {
		this.file = file;
	}

}
