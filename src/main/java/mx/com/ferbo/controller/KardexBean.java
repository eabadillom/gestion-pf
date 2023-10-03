
package mx.com.ferbo.controller;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.model.StreamedContent;

import mx.com.ferbo.dao.CamaraDAO;
import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.dao.ConstanciaDeDepositoDAO;
import mx.com.ferbo.dao.ConstanciaTraspasoDAO;
import mx.com.ferbo.dao.DetalleConstanciaSalidaDAO;
import mx.com.ferbo.dao.PartidaDAO;
import mx.com.ferbo.dao.PlantaDAO;
import mx.com.ferbo.dao.ProductoDAO;
import mx.com.ferbo.dao.TraspasoPartidaDAO;
import mx.com.ferbo.dao.UnidadDeManejoDAO;
import mx.com.ferbo.dao.UnidadDeProductoDAO;
import mx.com.ferbo.model.Camara;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ConstanciaDeDeposito;
import mx.com.ferbo.model.ConstanciaDepositoDetalle;
import mx.com.ferbo.model.ConstanciaTraspaso;
import mx.com.ferbo.model.DetalleConstanciaSalida;
import mx.com.ferbo.model.Partida;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.Producto;
import mx.com.ferbo.model.TraspasoPartida;
import mx.com.ferbo.model.UnidadDeManejo;
import mx.com.ferbo.model.UnidadDeProducto;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.InventarioException;
import mx.com.ferbo.util.JasperReportUtil;
import mx.com.ferbo.util.KardexTotalsBean;
import mx.com.ferbo.util.conexion;

@Named
@ViewScoped
public class KardexBean implements Serializable {
	private static Logger log = LogManager.getLogger(KardexBean.class);
	private static final long serialVersionUID = -4400856556349650679L;

	/**
	 * Objetos para Cliente
	 */

	private List<Cliente> lstClientes;
	private Cliente clienteSelected;
	private ClienteDAO clienteDAO;

	/**
	 * Objetos para Productos
	 */
	private List<Producto> listProducto;
	private Producto productoSelected;
	private ProductoDAO productoDAO;

	/**
	 * Objetos para Plantas
	 */
	private List<Planta> listPlanta;
	private Planta plantaSelected;
	private PlantaDAO plantaDAO;

	/**
	 * Objetos para Camaras
	 */
	private List<Camara> listCamara;
	private Camara camaraSelected;
	private CamaraDAO camaraDAO;

	/**
	 * Objetos para Constancia de deposito
	 */
	private ConstanciaDeDepositoDAO constanciaDeDepositoDAO;
	private List<ConstanciaDeDeposito> listConstanciaDepositoFiltered;

	/**
	 * Objetos para Partida
	 */
	private List<Partida> listPartida;
	private Partida partidaSelected;
	private PartidaDAO partidaDAO;

	/**
	 * Objetos para unidad de manejo
	 */
	private List<UnidadDeManejo> listUnidadManejo;
	private UnidadDeManejo unidadManejoSelected;
	private UnidadDeManejoDAO unidadDeManejoDAO;

	/**
	 * Objetos para unidad de producto
	 */
	private List<UnidadDeProducto> listUnidadProducto;
	private UnidadDeProducto unidadProductoSelected;
	private UnidadDeProductoDAO unidadDeProductoDAO;

	/**
	 * Objetos para Detalle de Constancia de Salida
	 */
	private List<DetalleConstanciaSalida> listDetalleSalida;
	private DetalleConstanciaSalida detalleSalidaSelected;
	private DetalleConstanciaSalidaDAO detalleSalidaDAO;

	/**
	 * Objetos auxiliares para Totales de Salidas
	 */
	private List<KardexTotalsBean> totalSalidaKardex;

	/**
	 * Objetos para Constancia traspasos
	 */
	private List<ConstanciaTraspaso> listContanciaTraspaso;
	private ConstanciaTraspaso constanciaTraspasoSelected;
	private ConstanciaTraspasoDAO constanciaTraspasoDAO;

	/**
	 * Objetos para Traspaso Partida
	 */
	private List<TraspasoPartida> listTraspasoPartida;
	private TraspasoPartida traspasoPartidaSelected;
	private TraspasoPartidaDAO traspasoPartidaDAO;

	/**
	 * Auxiliares
	 */
	private String folioClienteSelected;
	private Integer cantidadSalida;
	private BigDecimal pesoSalida;
	private Integer cantidadTotal;
	private BigDecimal pesoTotal;
	private boolean pintaTraspaso;
	
	private ConstanciaDeDeposito entrada;
	
	private StreamedContent scKardexPDF = null;
	private StreamedContent scKardexExcel = null;

	/**
	 * Constructores
	 */
	public KardexBean() {
		this.clienteDAO = new ClienteDAO();
		this.constanciaDeDepositoDAO = new ConstanciaDeDepositoDAO();
		this.partidaDAO = new PartidaDAO();
		this.detalleSalidaDAO = new DetalleConstanciaSalidaDAO();
		this.constanciaTraspasoDAO = new ConstanciaTraspasoDAO();
		this.traspasoPartidaDAO = new TraspasoPartidaDAO();
		this.cantidadSalida = 0;
		this.cantidadTotal = 0;
		this.pesoSalida = new BigDecimal(0);
		this.pesoTotal = new BigDecimal(0);
	}

	@PostConstruct
	public void init() {
		//lstClientes = clienteDAO.buscarTodos();
		listConstanciaDepositoFiltered = new ArrayList<>();
		listDetalleSalida = new ArrayList<>();
		listPartida = new ArrayList<>();
		totalSalidaKardex = new ArrayList<>();
		listContanciaTraspaso = new ArrayList<>();
		listTraspasoPartida = new ArrayList<>();
		pintaTraspaso = false;
	}

	public void buscaDatos() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Folio";
		
		try {
			if(this.folioClienteSelected == null)
				throw new InventarioException("Debe indicar un folio de entrada.");
			
			if("".equalsIgnoreCase(this.folioClienteSelected.trim()))
				throw new InventarioException("Debe indicar un folio de entrada.");
			
			this.entrada = constanciaDeDepositoDAO.buscarPorFolioCliente(folioClienteSelected, true);
			
			if(this.entrada == null)
				throw new InventarioException("El folio indicado no existe.");
			
			this.imprimeConstancia(entrada);
//			this.exportToPDF();
//			this.exportToExcel();
			
		} catch (InventarioException ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
			
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
		} catch (Exception ex) {
			log.error("Problema para obtener el folio de entrada...", ex);
			mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;
			
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
		} finally {
			PrimeFaces.current().ajax().update(":form:messages", "form:dt-entradasKardex", "form:dt-salidasKardex",
					"form:dt-traspasos", "form:button-traspasos", "form:cmd-pdf", "form:cmd-xlsx");
		}
		
		
	}
	
	public void getSaldo(Partida partida) {
		log.info("Obteniendo información de partida... {}", partida);
	}

	private void imprimeConstancia(ConstanciaDeDeposito constancia) {
		List<ConstanciaDepositoDetalle> constanciaDepositoDetalleList = constancia.getConstanciaDepositoDetalleList();
		for(ConstanciaDepositoDetalle cdet : constanciaDepositoDetalleList) {
			log.debug("Servicio: {}", cdet.getServicioCve().getServicioCod());
		}
		
		List<Partida> partidaList = constancia.getPartidaList();
		for(Partida partida : partidaList) {
			log.debug("Partida: {}",  partida.getPartidaCve());
			log.debug("Planta: {}", partida.getCamaraCve().getPlantaCve().getPlantaCve());
			log.debug("Producto: {}",partida.getUnidadDeProductoCve().getProductoCve().getProductoCve());
			log.debug("Unidad de Manejo: {}",partida.getUnidadDeProductoCve().getUnidadDeManejoCve().getUnidadDeManejoCve());
			log.debug("Unidad de cobro: {}",  partida.getUnidadDeCobro().getUnidadDeManejoCve());
			List<DetalleConstanciaSalida> detalleConstanciaSalidaList = partida.getDetalleConstanciaSalidaList();
			for(DetalleConstanciaSalida dcs : detalleConstanciaSalidaList) {
				log.debug("Detalle constancia salida: {}",dcs.getId());
				log.debug("Constancia salida: {}", dcs.getConstanciaCve().getId());
			}
		}
		
	}
	
	public void exportToPDF() {
		String jasperPath = null;
		String filename = null;
		String images = null;
		String message = null;
		Severity severity = null;
		File reportFile = null;
		File imgfile = null;
		JasperReportUtil jasperReportUtil = new JasperReportUtil();
		Map<String, Object> parameters = new HashMap<String, Object>();
		Connection connection = null;
		parameters = new HashMap<String, Object>();
		
		try {
			jasperPath = "/jasper/kardex.jrxml";
			images = "/images/logoF.png";
			
			URL resource = getClass().getResource(jasperPath);
			URL resourceimg = getClass().getResource(images);
			String file = resource.getFile();
			String img = resourceimg.getFile();
			
			reportFile = new File(file);
			imgfile = new File(img);
			log.info(reportFile.getPath());
			filename = String.format("kardex_%s.pdf", this.entrada.getFolioCliente());
			
			connection = EntityManagerUtil.getConnection();
			parameters.put("REPORT_CONNECTION", connection);
			parameters.put("folio", this.entrada.getFolioCliente() );
			parameters.put("imagen", imgfile.getPath());
			log.info("Parametros: " + parameters.toString());
			scKardexPDF = jasperReportUtil.getPdf(filename, parameters, reportFile.getPath());
			log.info("Kardex exportado.");
		} catch (Exception ex) {
			log.error("Problema general...", ex);
			message = String.format("No se pudo imprimir el reporte");
			severity = FacesMessage.SEVERITY_INFO;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Error en impresion", message));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-inventarioEntradas");
		} finally {
			conexion.close((Connection) connection);
		}
	}
	
	public void exportToExcel() {
		String jasperPath = null;
		String filename = null;
		String images = null;
		String message = null;
		Severity severity = null;
		File reportFile = null;
		File imgfile = null;
		JasperReportUtil jasperReportUtil = new JasperReportUtil();
		Map<String, Object> parameters = new HashMap<String, Object>();
		Connection connection = null;
		parameters = new HashMap<String, Object>();
		
		try {
			jasperPath = "/jasper/kardex.jrxml";
			images = "/images/logoF.png";
			
			URL resource = getClass().getResource(jasperPath);
			URL resourceimg = getClass().getResource(images);
			String file = resource.getFile();
			String img = resourceimg.getFile();
			
			reportFile = new File(file);
			imgfile = new File(img);
			log.info(reportFile.getPath());
			filename = String.format("kardex_%s.xlsx", this.entrada.getFolioCliente());
			
			connection = EntityManagerUtil.getConnection();
			parameters.put("REPORT_CONNECTION", connection);
			parameters.put("folio", this.entrada.getFolioCliente() );
			parameters.put("imagen", imgfile.getPath());
			log.info("Parametros: " + parameters.toString());
			scKardexExcel = jasperReportUtil.getXls(filename, parameters, reportFile.getPath());
			log.info("Kardex exportado.");
		} catch (Exception ex) {
			log.error("Problema general...", ex);
			message = String.format("No se pudo imprimir el reporte");
			severity = FacesMessage.SEVERITY_INFO;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Error en impresion", message));
		} finally {
			conexion.close((Connection) connection);
			PrimeFaces.current().ajax().update("form:messages", "form:cmd-xlsx");
		}
	}

	/**
	 * Getters & Setters
	 */

	public List<Cliente> getLstClientes() {
		return lstClientes;
	}

	public void setLstClientes(List<Cliente> lstClientes) {
		this.lstClientes = lstClientes;
	}

	public Cliente getClienteSelected() {
		return clienteSelected;
	}

	public void setClienteSelected(Cliente clienteSelected) {
		this.clienteSelected = clienteSelected;
	}

	public ClienteDAO getClienteDAO() {
		return clienteDAO;
	}

	public void setClienteDAO(ClienteDAO clienteDAO) {
		this.clienteDAO = clienteDAO;
	}

	public List<Producto> getListProducto() {
		return listProducto;
	}

	public void setListProducto(List<Producto> listProducto) {
		this.listProducto = listProducto;
	}

	public Producto getProductoSelected() {
		return productoSelected;
	}

	public void setProductoSelected(Producto productoSelected) {
		this.productoSelected = productoSelected;
	}

	public ProductoDAO getProductoDAO() {
		return productoDAO;
	}

	public void setProductoDAO(ProductoDAO productoDAO) {
		this.productoDAO = productoDAO;
	}

	public List<Planta> getListPlanta() {
		return listPlanta;
	}

	public void setListPlanta(List<Planta> listPlanta) {
		this.listPlanta = listPlanta;
	}

	public Planta getPlantaSelected() {
		return plantaSelected;
	}

	public void setPlantaSelected(Planta plantaSelected) {
		this.plantaSelected = plantaSelected;
	}

	public PlantaDAO getPlantaDAO() {
		return plantaDAO;
	}

	public void setPlantaDAO(PlantaDAO plantaDAO) {
		this.plantaDAO = plantaDAO;
	}

	public List<Camara> getListCamara() {
		return listCamara;
	}

	public void setListCamara(List<Camara> listCamara) {
		this.listCamara = listCamara;
	}

	public Camara getCamaraSelected() {
		return camaraSelected;
	}

	public void setCamaraSelected(Camara camaraSelected) {
		this.camaraSelected = camaraSelected;
	}

	public CamaraDAO getCamaraDAO() {
		return camaraDAO;
	}

	public void setCamaraDAO(CamaraDAO camaraDAO) {
		this.camaraDAO = camaraDAO;
	}

	public ConstanciaDeDepositoDAO getConstanciaDeDepositoDAO() {
		return constanciaDeDepositoDAO;
	}

	public void setConstanciaDeDepositoDAO(ConstanciaDeDepositoDAO constanciaDeDepositoDAO) {
		this.constanciaDeDepositoDAO = constanciaDeDepositoDAO;
	}

	public List<Partida> getListPartida() {
		return listPartida;
	}

	public void setListPartida(List<Partida> listPartida) {
		this.listPartida = listPartida;
	}

	public Partida getPartidaSelected() {
		return partidaSelected;
	}

	public void setPartidaSelected(Partida partidaSelected) {
		this.partidaSelected = partidaSelected;
	}

	public PartidaDAO getPartidaDAO() {
		return partidaDAO;
	}

	public void setPartidaDAO(PartidaDAO partidaDAO) {
		this.partidaDAO = partidaDAO;
	}

	public List<UnidadDeManejo> getListUnidadManejo() {
		return listUnidadManejo;
	}

	public void setListUnidadManejo(List<UnidadDeManejo> listUnidadManejo) {
		this.listUnidadManejo = listUnidadManejo;
	}

	public UnidadDeManejo getUnidadManejoSelected() {
		return unidadManejoSelected;
	}

	public void setUnidadManejoSelected(UnidadDeManejo unidadManejoSelected) {
		this.unidadManejoSelected = unidadManejoSelected;
	}

	public UnidadDeManejoDAO getUnidadDeManejoDAO() {
		return unidadDeManejoDAO;
	}

	public void setUnidadDeManejoDAO(UnidadDeManejoDAO unidadDeManejoDAO) {
		this.unidadDeManejoDAO = unidadDeManejoDAO;
	}

	public List<UnidadDeProducto> getListUnidadProducto() {
		return listUnidadProducto;
	}

	public void setListUnidadProducto(List<UnidadDeProducto> listUnidadProducto) {
		this.listUnidadProducto = listUnidadProducto;
	}

	public UnidadDeProducto getUnidadProductoSelected() {
		return unidadProductoSelected;
	}

	public void setUnidadProductoSelected(UnidadDeProducto unidadProductoSelected) {
		this.unidadProductoSelected = unidadProductoSelected;
	}

	public UnidadDeProductoDAO getUnidadDeProductoDAO() {
		return unidadDeProductoDAO;
	}

	public void setUnidadDeProductoDAO(UnidadDeProductoDAO unidadDeProductoDAO) {
		this.unidadDeProductoDAO = unidadDeProductoDAO;
	}

	public String getFolioClienteSelected() {
		return folioClienteSelected;
	}

	public void setFolioClienteSelected(String folioSelected) {
		this.folioClienteSelected = folioSelected;
	}

	public List<ConstanciaDeDeposito> getListConstanciaDepositoFiltered() {
		return listConstanciaDepositoFiltered;
	}

	public void setListConstanciaDepositoFiltered(List<ConstanciaDeDeposito> listConstanciaDepositoFiltered) {
		this.listConstanciaDepositoFiltered = listConstanciaDepositoFiltered;
	}

	public List<DetalleConstanciaSalida> getListDetalleSalida() {
		return listDetalleSalida;
	}

	public void setListDetalleSalida(List<DetalleConstanciaSalida> listDetalleSalida) {
		this.listDetalleSalida = listDetalleSalida;
	}

	public DetalleConstanciaSalida getDetalleSalidaSelected() {
		return detalleSalidaSelected;
	}

	public void setDetalleSalidaSelected(DetalleConstanciaSalida detalleSalidaSelected) {
		this.detalleSalidaSelected = detalleSalidaSelected;
	}

	public DetalleConstanciaSalidaDAO getDetalleSalidaDAO() {
		return detalleSalidaDAO;
	}

	public void setDetalleSalidaDAO(DetalleConstanciaSalidaDAO detalleSalidaDAO) {
		this.detalleSalidaDAO = detalleSalidaDAO;
	}

	public Integer getCantidadSalida() {
		return cantidadSalida;
	}

	public void setCantidadSalida(Integer cantidadSalida) {
		this.cantidadSalida = cantidadSalida;
	}

	public BigDecimal getPesoSalida() {
		return pesoSalida;
	}

	public void setPesoSalida(BigDecimal pesoSalida) {
		this.pesoSalida = pesoSalida;
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

	public List<KardexTotalsBean> getTotalSalidaKardex() {
		return totalSalidaKardex;
	}

	public void setTotalSalidaKardex(List<KardexTotalsBean> totalSalidaKardex) {
		this.totalSalidaKardex = totalSalidaKardex;
	}

	public List<ConstanciaTraspaso> getListContanciaTraspaso() {
		return listContanciaTraspaso;
	}

	public void setListContanciaTraspaso(List<ConstanciaTraspaso> listContanciaTraspaso) {
		this.listContanciaTraspaso = listContanciaTraspaso;
	}

	public ConstanciaTraspaso getConstanciaTraspasoSelected() {
		return constanciaTraspasoSelected;
	}

	public void setConstanciaTraspasoSelected(ConstanciaTraspaso constanciaTraspasoSelected) {
		this.constanciaTraspasoSelected = constanciaTraspasoSelected;
	}

	public List<TraspasoPartida> getListTraspasoPartida() {
		return listTraspasoPartida;
	}

	public void setListTraspasoPartida(List<TraspasoPartida> listTraspasoPartida) {
		this.listTraspasoPartida = listTraspasoPartida;
	}

	public ConstanciaTraspasoDAO getConstanciaTraspasoDAO() {
		return constanciaTraspasoDAO;
	}

	public void setConstanciaTraspasoDAO(ConstanciaTraspasoDAO constanciaTraspasoDAO) {
		this.constanciaTraspasoDAO = constanciaTraspasoDAO;
	}

	public TraspasoPartida getTraspasoPartidaSelected() {
		return traspasoPartidaSelected;
	}

	public void setTraspasoPartidaSelected(TraspasoPartida traspasoPartidaSelected) {
		this.traspasoPartidaSelected = traspasoPartidaSelected;
	}

	public TraspasoPartidaDAO getTraspasoPartidaDAO() {
		return traspasoPartidaDAO;
	}

	public void setTraspasoPartidaDAO(TraspasoPartidaDAO traspasoPartidaDAO) {
		this.traspasoPartidaDAO = traspasoPartidaDAO;
	}

	public boolean isPintaTraspaso() {
		return pintaTraspaso;
	}

	public void setPintaTraspaso(boolean pintaTraspaso) {
		this.pintaTraspaso = pintaTraspaso;
	}

	public ConstanciaDeDeposito getEntrada() {
		return entrada;
	}

	public void setEntrada(ConstanciaDeDeposito entrada) {
		this.entrada = entrada;
	}

	public StreamedContent getScKardexPDF() {
		return scKardexPDF;
	}

	public void setScKardexPDF(StreamedContent scKardexPDF) {
		this.scKardexPDF = scKardexPDF;
	}

	public StreamedContent getScKardexExcel() {
		return scKardexExcel;
	}

	public void setScKardexExcel(StreamedContent scKardexExcel) {
		this.scKardexExcel = scKardexExcel;
	}

	

	

}
