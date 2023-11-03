package mx.com.ferbo.controller;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.CamaraDAO;
import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.dao.ConstanciaSalidaDAO;
import mx.com.ferbo.dao.DetalleConstanciaSalidaDAO;
import mx.com.ferbo.dao.DetallePartidaDAO;
import mx.com.ferbo.dao.EstadoConstanciaDAO;
import mx.com.ferbo.dao.EstadoInventarioDAO;
import mx.com.ferbo.dao.OrdenSalidaDAO;
import mx.com.ferbo.dao.PartidaDAO;
import mx.com.ferbo.dao.PreSalidaServicioDAO;
import mx.com.ferbo.dao.PrecioServicioDAO;
import mx.com.ferbo.dao.ProductoDAO;
import mx.com.ferbo.dao.UnidadDeManejoDAO;
import mx.com.ferbo.model.Camara;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ConstanciaDeServicio;
import mx.com.ferbo.model.ConstanciaSalida;
import mx.com.ferbo.model.ConstanciaSalidaServicios;
import mx.com.ferbo.model.ConstanciaServicioDetalle;
import mx.com.ferbo.model.DetalleConstanciaSalida;
import mx.com.ferbo.model.DetallePartida;
import mx.com.ferbo.model.EstadoConstancia;
import mx.com.ferbo.model.EstadoInventario;
import mx.com.ferbo.model.OrdenSalida;
import mx.com.ferbo.model.Partida;
import mx.com.ferbo.model.PartidaServicio;
import mx.com.ferbo.model.PreSalida;
import mx.com.ferbo.model.PreSalidaServicio;
import mx.com.ferbo.model.PrecioServicio;
import mx.com.ferbo.model.Producto;
import mx.com.ferbo.model.Servicio;
import mx.com.ferbo.model.ServicioFactura;
import mx.com.ferbo.model.UnidadDeManejo;
import mx.com.ferbo.ui.OrdenDeSalidas;
import mx.com.ferbo.ui.PreSalidaUI;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.InventarioException;
import mx.com.ferbo.util.JasperReportUtil;
import mx.com.ferbo.util.conexion;
import net.sf.jasperreports.engine.JRException;

@Named
@ViewScoped
public class OrdenSalidaBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Logger log = LogManager.getLogger(OrdenSalidaBean.class);

	private List<Cliente> listaClientes;
	private List<OrdenSalida> listaOrdenSalida;
	private List<OrdenDeSalidas> listaSalidasporPlantas;
	private List<PrecioServicio> listaServicios;
	private List<PreSalidaServicio> listaPreSalidaServicio;
	private List<String> listaFolios;
	private List<OrdenSalida> listaSalidasporFolio;
	private List<PreSalidaUI> listaPreSalidaUI;
	
	private ClienteDAO clienteDAO;
	private OrdenSalidaDAO ordenSalidaDAO;
	private PrecioServicioDAO precioServicioDAO;
	private PreSalidaServicioDAO presalidaservicioDAO;
	private PartidaDAO partidaDAO;
	private CamaraDAO camaraDAO;
	private ConstanciaSalidaDAO constanciaDAO;
	private DetallePartidaDAO dpDAO;
	private EstadoInventarioDAO estadoInventarioDAO;
	private EstadoConstanciaDAO estadoConstanciaDAO;
	private ProductoDAO productoDAO;
	private	UnidadDeManejoDAO unidadDAO ;

	private boolean confirmacion;
	private boolean pdf;
	private boolean excel;
	private String folioSelected;
	private Date fecha;
	private Time tmSalida;
	private String detalleAnterior;
	private String detalleActual;
	private Integer cantidad;
	private BigDecimal peso;
	private String observaciones;
	private Integer cantidadServicio;
	
	private Cliente clienteSelect;
	private OrdenSalida ordensalida;
	private ConstanciaServicioDetalle selServicio;
	private DetallePartida dp;
	private DetalleConstanciaSalida dcs;
	private OrdenDeSalidas ordenDeSalidas;
	private PrecioServicio idServicio;
	private PreSalidaServicio pss;
	private PreSalidaUI psu;
	private EstadoInventario estadoInventarioActual;
	private EstadoInventario estadoInventarioHistorico;
	private EstadoConstancia estadoConstancia;


	public OrdenSalidaBean() {
		clienteDAO = new ClienteDAO();
		ordenSalidaDAO = new OrdenSalidaDAO();
		presalidaservicioDAO = new PreSalidaServicioDAO();
		precioServicioDAO = new PrecioServicioDAO();
		estadoConstanciaDAO = new EstadoConstanciaDAO();
		partidaDAO = new PartidaDAO();
		camaraDAO = new CamaraDAO();
		productoDAO = new ProductoDAO();
		constanciaDAO = new ConstanciaSalidaDAO();
		unidadDAO = new UnidadDeManejoDAO();
		dpDAO = new DetallePartidaDAO();
		estadoInventarioDAO = new EstadoInventarioDAO();
		listaOrdenSalida = new ArrayList<OrdenSalida>();
		listaClientes = new ArrayList<Cliente>();
		listaPreSalidaUI = new ArrayList<>();
		listaServicios = new ArrayList<PrecioServicio>();
		listaSalidasporPlantas = new ArrayList<OrdenDeSalidas>();
		
	}

	@PostConstruct
	public void init() {
		listaClientes = clienteDAO.buscarHabilitados(true);
		fecha = new Date();
		DateUtil.setTime(fecha, 0, 0, 0, 0);
		estadoInventarioActual = estadoInventarioDAO.buscarPorId(1);
		estadoInventarioHistorico = estadoInventarioDAO.buscarPorId(2);
	    estadoConstancia = estadoConstanciaDAO.buscarPorId(1);
	}

	public void filtrarCliente() {
		String message = null;
		Severity severity = null;
		EntityManager manager = null;

		try {
			log.info("Entrando a filtrar cliente...");

			manager = EntityManagerUtil.getEntityManager();
			listaFolios = ordenSalidaDAO.buscaFolios(clienteSelect, fecha);
			listaServicios = precioServicioDAO.buscarPorCliente(clienteSelect.getCteCve(), true);
			message = "Seleccione el folio.";
			severity = FacesMessage.SEVERITY_INFO;
		} catch (Exception ex) {
			log.error("Problema para recuperar los datos del cliente.", ex);
			message = ex.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			if (manager != null)
				manager.close();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Cliente", message));
			PrimeFaces.current().ajax().update("form:messages", "form:selServicio", "form:folio-som");
		}
		log.info("Informacion exitosamente filtrada.");
	}

	public void reload() throws IOException {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
	}

	public void filtroPorPlanta() {
		Integer folio = null;
		OrdenSalida os = null;
		System.out.println("Probando agregar producto...");
		listaSalidasporPlantas = ordenSalidaDAO.buscarpoPlanta(folioSelected, fecha);
		listaPreSalidaUI = new ArrayList<PreSalidaUI>();
		try {
			for (OrdenDeSalidas orden : listaSalidasporPlantas) {
				PreSalidaUI preUI = new PreSalidaUI(orden.getFolioSalida(), orden.getStatus(), orden.getFechaSalida(),
						orden.getHoraSalida(), orden.getPartidaCve(), orden.getCantidad(), orden.getPeso(),
						orden.getCodigo(), orden.getLote(), orden.getFechaCaducidad(), orden.getSAP(),
						orden.getPedimento(), orden.getTemperatura(), orden.getUnidadManejo(),
						orden.getCodigoProducto(), orden.getNombreProducto(), orden.getNombrePlanta(),
						orden.getNombreCamara(), orden.getFolioOrdenSalida(), orden.getProductoClave(), orden.getUnidadManejoCve());
						preUI.setSalidaSelected(false);
						Partida partida = partidaDAO.buscarPorId(preUI.getPartidaCve());
						
						Integer cantidadInicial = partida.getCantidadTotal();
						BigDecimal CantidadInicial = new BigDecimal(cantidadInicial);
						BigDecimal pesoInicial = partida.getPesoTotal();
						BigDecimal pesoPorUnidad = pesoInicial.divide(CantidadInicial);
						BigDecimal cantidadOrden = new BigDecimal(orden.getCantidad());
						preUI.setPeso(pesoPorUnidad.multiply(cantidadOrden));

				listaPreSalidaUI.add(preUI);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		listaSalidasporFolio = ordenSalidaDAO.buscaFolios(folioSelected);
		listaPreSalidaServicio = presalidaservicioDAO.buscarPorFolios(folioSelected);
		folio = listaSalidasporFolio.size();
		if (folio != 0) {
			ordensalida = listaSalidasporFolio.get(0);
		}
	}


	public void addMessage(AjaxBehaviorEvent event) {
		UIComponent component = event.getComponent();
		if (component instanceof UIInput) {
			UIInput inputComponent = (UIInput) component;
			boolean value = (boolean) inputComponent.getValue();
			String summary = value ? "Checked" : "Unchecked";
			value = psu.salidaSelected;
			System.out.println(psu.salidaSelected);
			log.info(psu.salidaSelected);

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(summary));
		}
	}

	public void validarProducto() {
		String message = null;
		Severity severity = null;
		EntityManager manager = null;
		List<PreSalidaUI> listapsU = null;
		OrdenDeSalidas ods = null;

		try {
			log.info("Filtrando Producto...");
			manager = EntityManagerUtil.getEntityManager();
			for (PreSalidaUI ps : listaPreSalidaUI) {
				if(ps.salidaSelected == true) {
					listapsU = new ArrayList<>();
					ps.getPartidaCve();
					ps.getNombreProducto();
					ps.getCantidad();
					ps.getCodigoProducto();
					ps.getCodigo();
					listapsU.add(ps);
					message = "Agregado con exito";
					severity = FacesMessage.SEVERITY_INFO;
					
					}
				
				}
			
		} catch (Exception ex) {
			log.error("Problema para recuperar los datos del cliente.", ex);
			message = ex.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			if (manager != null)
				manager.close();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Producto", message));
			PrimeFaces.current().ajax().update("form:messages", "form:selServicio", "form:folio-som");
		}
		log.info("Informacion filtrada con éxito.");
	}
	
	public void imprimirTicketPDF() throws JRException, IOException, SQLException {
		
		System.out.println("Exportando a pdf.....");
		String jasperPath = "/jasper/OrdenSalida.jrxml";
		String filename = "OrdenSalida"+fecha+".pdf";
		String images = "/images/logo.jpeg";
		String message = null;
		Severity severity = null;
		File reportFile = new File(jasperPath);
		File imgfile = null;
		JasperReportUtil jasperReportUtil = new JasperReportUtil();
		Map<String, Object> parameters = new HashMap<String, Object>();
		Connection connection = null;
		parameters = new HashMap<String, Object>();
		
		try {
			
			URL resource = getClass().getResource(jasperPath);
			URL resourceimg = getClass().getResource(images);
			String file = resource.getFile();
			String img = resourceimg.getFile();
			reportFile = new File(file);
			imgfile = new File(img);
			log.info(reportFile.getPath());
			
			Integer clienteCve = null;
			if(clienteSelect == null) {
				clienteCve = null; 
			}else {
				clienteCve = clienteSelect.getCteCve();
			}
			
			connection = EntityManagerUtil.getConnection();
			parameters.put("REPORT_CONNECTION", connection);
			parameters.put("idCliente",clienteCve);
			parameters.put("fechaInicio", fecha);
			parameters.put("fechaFin", fecha);
			parameters.put("imagen", imgfile.getPath());
			log.info("Parametros: " + parameters.toString());
			jasperReportUtil.createPdf(filename, parameters, reportFile.getPath());
			
		} catch (Exception ex) {
			log.error("Problema general...", ex);
			message = String.format("No se pudo imprimir el reporte");
			severity = FacesMessage.SEVERITY_INFO;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Error en impresion", message));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-inventarioSalidas");
		} finally {
			conexion.close((Connection) connection);
		}
		
	}

	public void agregaServicios() {
		String message = null;
		Severity severity = null;
		PreSalidaServicio ps = null;
		int count = 0;
		try {
			if (this.idServicio == null)
				throw new InventarioException("Selecione almenos un servicio");
			if (this.clienteSelect == null)
				throw new InventarioException("Debe seleccionar el cliente");
			if (this.cantidadServicio == null)
				throw new InventarioException("Debe indicar la cantidad de servicios.");

			if (listaPreSalidaServicio == null)
				listaPreSalidaServicio = new ArrayList<>();
			ps = new PreSalidaServicio();
			ps.setCantidad(cantidadServicio);
			ps.setIdServicio(idServicio.getServicio());
			ps.setIdUnidadManejo(idServicio.getUnidad());
			ps.setObservacion(ps.getObservacion());

			int coincidencias = 0, diferentes = 0;
			for (PreSalidaServicio srv : listaPreSalidaServicio) {
				if (srv.getIdServicio().getServicioCve().equals(ps.getIdServicio().getServicioCve())) {
					coincidencias++;
				} else {
					diferentes++;
				}
			}

			if (coincidencias == 1) {
				System.out.println("Servicio duplicado");
				message = "Servicio duplicado, favor de modificar la cantidad y/o la unidad.";
				severity = FacesMessage.SEVERITY_ERROR;
			} else if (diferentes > 0) {
				listaPreSalidaServicio.add(ps);
				message = "Servicio agregado correctamente.";
				severity = FacesMessage.SEVERITY_INFO;
			}

		} catch (Exception e) {
			log.error("Problema para obtener el listado de servicios del cliente.", e);
			message = "Problema con la información de servicios.";
			severity = FacesMessage.SEVERITY_ERROR;

		} finally {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Servicios", message));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-servicios");

		}
	}
	
	public void validaTemperatura(PreSalidaUI p) {
		log.debug("PreSalida {}",p);
	}
	
	public void guardar() throws InventarioException {
		String message = null;
		Severity severity = null;
		ConstanciaSalida constancia = new ConstanciaSalida();
		ConstanciaSalidaServicios css = new ConstanciaSalidaServicios();
		DetallePartida detAnterior = null;
		DetallePartida detNUevo= null;
		Integer cantidadManejo = null;
		BigDecimal peso = null;
		try {	
			
			for (PreSalidaUI preS : listaPreSalidaUI) {
				if (preS.salidaSelected == true) {
					DetalleConstanciaSalida dcs = new DetalleConstanciaSalida();
					Partida p = partidaDAO.buscarPorId(preS.getPartidaCve());
					constancia.setFecha(fecha);
					constancia.setPlacasTransporte(ordensalida.getNombrePlacas());
					constancia.setNombreTransportista(ordensalida.getNombreOperador());
					constancia.setNumero(ordensalida.getFolioSalida());
					constancia.setClienteCve(clienteSelect);
					
					List<DetalleConstanciaSalida> dcsList = new ArrayList<>();
					constancia.setDetalleConstanciaSalidaList(dcsList);
					
					List<DetallePartida> listadp = new ArrayList<>();
					listadp = dpDAO.buscarPorPartida(p.getPartidaCve());
					p.setDetallePartidaList(listadp);
					for(DetallePartida d : listadp) {
						detAnterior = d;
						detalleAnterior = detAnterior.toString();							
					}
					detNUevo = detAnterior.clone();
					detNUevo.setDetallePartidaPK(detNUevo.getDetallePartidaPK().clone());
					detAnterior.setEdoInvCve(estadoInventarioHistorico);
					detalleActual= detNUevo.toString();
					int i = detNUevo.getDetallePartidaPK().getDetPartCve() + 1;
					detNUevo.getDetallePartidaPK().setDetPartCve(i);
					detNUevo.setDetallePartida(detAnterior);
					detNUevo.setEdoInvCve(estadoInventarioActual);
					cantidadManejo = detNUevo.getCantidadUManejo();
					peso = detNUevo.getCantidadUMedida();
					cantidadManejo= cantidadManejo - preS.getCantidad();
					peso = peso.subtract(preS.getPeso());
					detNUevo.setCantidadUManejo(cantidadManejo);
					detNUevo.setCantidadUMedida(peso);
					listadp.add(detNUevo);
					dpDAO.actualizar(detAnterior);
					dpDAO.guardar(detNUevo);
					dcs.setPartidaCve(p);
					
					Camara c = camaraDAO.buscarPorId(p.getCamaraCve().getCamaraCve());
					dcs.setCamaraCve(c.getCamaraCve());
					dcs.setCantidad(preS.getCantidad());
					dcs.setPeso(preS.getPeso());
					dcs.setUnidad(preS.getUnidadManejo());
					dcs.setProducto(preS.getNombreProducto());
					dcs.setTemperatura(preS.getTemperatura());
					dcs.setConstanciaCve(constancia);
					dcs.setDetPartCve(detNUevo.getDetallePartidaPK().getDetPartCve());
					dcsList.add(dcs);
				}
			}
				List<ConstanciaSalidaServicios> listaConstanciaSalidaServicios = new ArrayList<>();
				for (PreSalidaServicio pss : listaPreSalidaServicio) {
					ConstanciaSalidaServicios constanciaSaldaServicios = new ConstanciaSalidaServicios();
					constanciaSaldaServicios.setNumCantidad(BigDecimal.valueOf(pss.getCantidad()));
					constanciaSaldaServicios.setServicioCve(pss.getIdServicio());
					constanciaSaldaServicios.setIdConstancia(constancia);
					listaConstanciaSalidaServicios.add(constanciaSaldaServicios);
				}
				
				constanciaDAO.guardar(constancia);
				
				List<ConstanciaDeServicio> listaConstanciaDeServicios = new ArrayList<>();
				if(listaPreSalidaServicio.size() > 0) {
					ConstanciaDeServicio cds = new ConstanciaDeServicio();
					cds.setCteCve(clienteSelect);
					cds.setFecha(fecha);
					cds.setNombreTransportista(ordensalida.getNombreOperador());
					cds.setPlacasTransporte(ordensalida.getNombrePlacas());
					cds.setObservaciones(observaciones);
					BigDecimal valor = new BigDecimal(1);
					cds.setValorDeclarado(valor);
					cds.setFolioCliente("S"+ordensalida.getFolioSalida());
					cds.setStatus(estadoConstancia);
					listaConstanciaDeServicios.add(cds);
					
					List<PartidaServicio> listaPartidaServicio = new ArrayList<>();
					for(OrdenDeSalidas ordenDeSalida : listaSalidasporPlantas) {
						PartidaServicio ps = new PartidaServicio();
						Producto pr = new Producto();
						UnidadDeManejo udm = new UnidadDeManejo();
						Integer cantidad = ordenDeSalida.getCantidad();
						BigDecimal Cantidad = new BigDecimal(cantidad);
						BigDecimal pso = ordenDeSalida.getPeso();
						BigDecimal psoPorProducto = pso.divide(Cantidad);
						BigDecimal cantidadOrdenSalida = new BigDecimal(ordenDeSalida.getCantidad());

						pr = productoDAO.buscarPorId(ordenDeSalida.getProductoClave());
						udm = unidadDAO.buscarPorId(ordenDeSalida.getUnidadManejoCve());
						ps.setCantidadDeCobro(psoPorProducto.multiply(cantidadOrdenSalida));
						ps.setCantidadTotal(ordenDeSalida.getCantidad());
						ps.setFolio(cds);
						ps.setPartidaCve(ordenDeSalida.getPartidaCve());
						ps.setProductoCve(pr);
						ps.setUnidadDeCobro(udm);
						ps.setUnidadDeManejoCve(udm);
						listaPartidaServicio.add(ps);
					}
					cds.setPartidaServicioList(listaPartidaServicio);
				
					List<ConstanciaServicioDetalle> listaConstanciaSrv = new ArrayList<>();
					for(PreSalidaServicio preServ : listaPreSalidaServicio) {
						ConstanciaServicioDetalle consdetalle = new ConstanciaServicioDetalle();
						consdetalle.setFolio(null);
						consdetalle.setServicioCantidad(BigDecimal.valueOf(preServ.getCantidad()));
						consdetalle.setServicioCve(preServ.getIdServicio());
						listaConstanciaSrv.add(consdetalle);
					}
				}	
				listaSalidasporPlantas = ordenSalidaDAO.buscarpoPlanta(folioSelected, fecha);
				for(OrdenDeSalidas orden : listaSalidasporPlantas) {
					orden.setStatus("C");
					ordenSalidaDAO.actualizar(orden);
				}
				message = "Constancia guardada correctamente.";
				severity = FacesMessage.SEVERITY_INFO;
			
		} catch (Exception ex) {
			log.error("Problema para obtener el listado de la orden.", ex);
			ex.printStackTrace();
			message = "Problema con la información de servicios.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Orden en proceso", message));
			PrimeFaces.current().ajax().update("form:messages");
		}
	}

public void imprimirTicketSalida() throws Exception{
		
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
			constancia.setNumero(ordensalida.getFolioSalida());
			connection = EntityManagerUtil.getConnection();
			parameters.put("REPORT_CONNECTION", connection);
			parameters.put("NUMERO", ordensalida.getFolioSalida());
			parameters.put("LogoPath", imgFile.getPath());
			jasperReportUtil.createPdf(filename, parameters, reportFile.getPath());
			   
		} catch (Exception e) {
			e.printStackTrace();
			message = String.format("No se pudo imprimir el folio %s", ordensalida.getFolioSalida());
			severity = FacesMessage.SEVERITY_INFO;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity,"Error en impresion",message));
			PrimeFaces.current().ajax().update("form:messages");
			
		}finally {
			conexion.close((Connection) connection);
		}

	}
	
	@SuppressWarnings("unlikely-arg-type")
	public void deleteServicio(PreSalidaServicio servicio) {
		this.listaPreSalidaServicio.remove(servicio);
		this.listaServicios.remove(servicio);
	}

	public Cliente getClienteSelect() {
		return clienteSelect;
	}

	public void setClienteSelect(Cliente clienteSelect) {
		this.clienteSelect = clienteSelect;
	}

	public List<Cliente> getListaClientes() {
		return listaClientes;
	}

	public void setListaClientes(List<Cliente> listaClientes) {
		this.listaClientes = listaClientes;
	}

	public OrdenSalida getOrdensalida() {
		return ordensalida;
	}

	public void setOrdensalida(OrdenSalida ordensalida) {
		this.ordensalida = ordensalida;
	}

	public List<OrdenSalida> getListaOrdenSalida() {
		return listaOrdenSalida;
	}

	public void setListaOrdenSalida(List<OrdenSalida> listaOrdenSalida) {
		this.listaOrdenSalida = listaOrdenSalida;
	}

	public boolean isConfirmacion() {
		return confirmacion;
	}

	public void setConfirmacion(boolean confirmacion) {
		this.confirmacion = confirmacion;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public List<PrecioServicio> getListaServicios() {
		return listaServicios;
	}

	public void setListaServicios(List<PrecioServicio> listaServicios) {
		this.listaServicios = listaServicios;
	}

	public ConstanciaServicioDetalle getSelServicio() {
		return selServicio;
	}

	public void setSelServicio(ConstanciaServicioDetalle selServicio) {
		this.selServicio = selServicio;
	}

	public PrecioServicio getIdServicio() {
		return idServicio;
	}

	public void setIdServicio(PrecioServicio idServicio) {
		this.idServicio = idServicio;
	}

	public Integer getCantidadServicio() {
		return cantidadServicio;
	}

	public void setCantidadServicio(Integer cantidadServicio) {
		this.cantidadServicio = cantidadServicio;
	}

	public List<OrdenDeSalidas> getListaSalidasporPlantas() {
		return listaSalidasporPlantas;
	}

	public void setListaSalidasporPlantas(List<OrdenDeSalidas> listaSalidasporPlantas) {
		this.listaSalidasporPlantas = listaSalidasporPlantas;
	}

	public DetallePartida getDp() {
		return dp;
	}

	public void setDp(DetallePartida dp) {
		this.dp = dp;
	}

	public DetalleConstanciaSalida getDcs() {
		return dcs;
	}

	public void setDcs(DetalleConstanciaSalida dcs) {
		this.dcs = dcs;
	}

	public OrdenDeSalidas getOrdenDeSalidas() {
		return ordenDeSalidas;
	}

	public void setOrdenDeSalidas(OrdenDeSalidas ordenDeSalidas) {
		this.ordenDeSalidas = ordenDeSalidas;
	}

	public List<String> getListaFolios() {
		return listaFolios;
	}

	public void setListaFolios(List<String> listaFolios) {
		this.listaFolios = listaFolios;
	}

	public String getFolioSelected() {
		return folioSelected;
	}

	public void setFolioSelected(String folioSelected) {
		this.folioSelected = folioSelected;
	}

	public Time getTmSalida() {
		return tmSalida;
	}

	public void setTmSalida(Time tmSalida) {
		this.tmSalida = tmSalida;
	}

	public List<OrdenSalida> getListaSalidasporFolio() {
		return listaSalidasporFolio;
	}

	public void setListaSalidasporFolio(List<OrdenSalida> listaSalidasporFolio) {
		this.listaSalidasporFolio = listaSalidasporFolio;
	}

	public PreSalidaServicio getPss() {
		return pss;
	}

	public void setPss(PreSalidaServicio pss) {
		this.pss = pss;
	}

	public List<PreSalidaServicio> getListaPreSalidaServicio() {
		return listaPreSalidaServicio;
	}

	public void setListaPreSalidaServicio(List<PreSalidaServicio> listaPreSalidaServicio) {
		this.listaPreSalidaServicio = listaPreSalidaServicio;
	}

	public List<PreSalidaUI> getListaPreSalidaUI() {
		return listaPreSalidaUI;
	}

	public void setListaPreSalidaUI(List<PreSalidaUI> listaPreSalidaUI) {
		this.listaPreSalidaUI = listaPreSalidaUI;
	}

	public PreSalidaUI getPsu() {
		return psu;
	}

	public void setPsu(PreSalidaUI psu) {
		this.psu = psu;
	}

	public String getDetalleAnterior() {
		return detalleAnterior;
	}

	public void setDetalleAnterior(String detalleAnterior) {
		this.detalleAnterior = detalleAnterior;
	}

	public String getDetalleActual() {
		return detalleActual;
	}

	public void setDetalleActual(String detalleActual) {
		this.detalleActual = detalleActual;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public BigDecimal getPeso() {
		return peso;
	}

	public void setPeso(BigDecimal peso) {
		this.peso = peso;
	}

	public EstadoInventario getEstadoInventarioActual() {
		return estadoInventarioActual;
	}

	public void setEstadoInventarioActual(EstadoInventario estadoInventarioActual) {
		this.estadoInventarioActual = estadoInventarioActual;
	}

	public EstadoConstancia getEstadoConstancia() {
		return estadoConstancia;
	}

	public void setEstadoConstancia(EstadoConstancia estadoConstancia) {
		this.estadoConstancia = estadoConstancia;
	}

	public EstadoInventario getEstadoInventarioHistorico() {
		return estadoInventarioHistorico;
	}

	public void setEstadoInventarioHistorico(EstadoInventario estadoInventarioHistorico) {
		this.estadoInventarioHistorico = estadoInventarioHistorico;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public boolean isPdf() {
		return pdf;
	}

	public void setPdf(boolean pdf) {
		this.pdf = pdf;
	}

	public boolean isExcel() {
		return excel;
	}

	public void setExcel(boolean excel) {
		this.excel = excel;
	}

}
