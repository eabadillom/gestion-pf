package mx.com.ferbo.controller;


import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.dao.ConstanciaTraspasoDAO;
import mx.com.ferbo.dao.EstadoConstanciaDAO;
import mx.com.ferbo.dao.TraspasoPartidaDAO;
import mx.com.ferbo.dao.TraspasoServicioDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ConstanciaDeDeposito;
import mx.com.ferbo.model.ConstanciaServicioDetalle;
import mx.com.ferbo.model.ConstanciaTraspaso;
import mx.com.ferbo.model.DetallePartida;
import mx.com.ferbo.model.EstadoConstancia;
import mx.com.ferbo.model.Inventario;
import mx.com.ferbo.model.InventarioDetalle;
import mx.com.ferbo.model.Partida;
import mx.com.ferbo.model.PartidaServicio;
import mx.com.ferbo.model.ProductoPorCliente;
import mx.com.ferbo.model.TraspasoPartida;
import mx.com.ferbo.model.TraspasoServicio;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.JasperReportUtil;
import mx.com.ferbo.util.conexion;
import net.sf.jasperreports.engine.JRException;

@Named
@ViewScoped
public class ConsultaTraspasosBean implements Serializable {

	private static final long serialVersionUID = -3109002730694247052L;
	private static Logger log = LogManager.getLogger(AltaConstanciaServicioBean.class);
	private List<Cliente> clientes;
	private List<PartidaServicio> alPartidas;
	private List<TraspasoServicio> alServiciosDetalle;
	private List<ProductoPorCliente> alProductosFiltered;
	private List<EstadoConstancia> estados = null;
	private List<ConstanciaDeDeposito> listaconstanciadepo;
	private List<Partida> partida;
	private List<DetallePartida> ldpartida;
	private List<InventarioDetalle> inventario;
	private List<InventarioDetalle> destino;
	private List<TraspasoPartida> listaTraspasoPartida;
	private List<ConstanciaTraspaso> listaTraspasos;
	private List<TraspasoServicio> listaServicios;
	
	private Date fecha_ini;
	private Date fecha_final;
	private Date maxDate;
	
	private String numero;
	private Integer cantidad;
	private Integer idUnidadManejo;
	private Integer idProducto;
	private BigDecimal peso;
	private String observaciones;
	private BigDecimal cantidadServicio;
	private Integer idCliente;
	
	private Cliente selCliente;
	private PartidaServicio selPartida;
	private ConstanciaServicioDetalle selServicio;
	private TraspasoPartida tp;
	private Inventario inv;
	private ConstanciaTraspaso selectedconstancia;
	
	
	private EstadoConstanciaDAO edoDAO;
	private ClienteDAO clienteDAO;
	private TraspasoPartidaDAO tpDAO;
	private ConstanciaTraspasoDAO constanciaTraspasoDAO;
	private TraspasoServicioDAO traspasoServicioDAO;
	
	private boolean habilitareporte = false;

	public ConsultaTraspasosBean() {
		log.info("Entrando al constructor del controller...");
		clienteDAO = new ClienteDAO();
		edoDAO = new EstadoConstanciaDAO();
		constanciaTraspasoDAO = new ConstanciaTraspasoDAO();
		tpDAO = new TraspasoPartidaDAO();
		traspasoServicioDAO = new TraspasoServicioDAO();
		clientes = new ArrayList<Cliente>();
		partida = new ArrayList<Partida>();
		ldpartida = new ArrayList<DetallePartida>();
		inventario = new ArrayList<InventarioDetalle>();
		alServiciosDetalle = new ArrayList<TraspasoServicio>();
		alProductosFiltered = new ArrayList<ProductoPorCliente>();
		listaconstanciadepo = new ArrayList<ConstanciaDeDeposito>();
		listaTraspasoPartida = new ArrayList<TraspasoPartida>();
		listaServicios  = new ArrayList<TraspasoServicio>();
		selCliente = new Cliente();
		selectedconstancia = new ConstanciaTraspaso();
		tp = new TraspasoPartida();
		clientes = clienteDAO.findall();
	}

	@PostConstruct
	public void init() {
		log.info("Entrando a Init...");
		fecha_ini = new Date();
		fecha_final = new Date();
		clientes = clienteDAO.buscarTodos();
		estados = edoDAO.buscarTodos();
		if (alProductosFiltered == null)
			alProductosFiltered = new ArrayList<ProductoPorCliente>();
		
		Date today = new Date();
		setMaxDate(new Date(today.getTime() ));
		
		this.selectedconstancia = new ConstanciaTraspaso();
		
	}
	public void buscarConstancia() {
		if(numero != null && "".equalsIgnoreCase(numero.trim()))
			this.numero = null;
		
		if("".equalsIgnoreCase(this.numero))
			this.numero = null;
		
		listaTraspasos = constanciaTraspasoDAO.buscar(fecha_ini, fecha_final, idCliente, numero);
		log.debug("Lista constancias de traspaso: {}", listaTraspasos.size());
	}
	
	public void cargaDetalle() {
		String message = null;
		Severity severity = null;
		EntityManager manager = null;
		try {
			log.info("Cargando constancia de traspaso: {}", this.selectedconstancia);
			this.selectedconstancia = constanciaTraspasoDAO.buscarPorId(this.selectedconstancia.getId(), true);
			
			log.debug("TraspasoPartida (size): {}", selectedconstancia.getTraspasoPartidaList().size());
			listaTraspasoPartida = selectedconstancia.getTraspasoPartidaList();
			listaServicios = selectedconstancia.getTraspasoServicioList();
		} catch (Exception ex) {
			log.error("Problema para recuperar los datos del cliente.", ex);
			message = ex.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Cliente", message));
			PrimeFaces.current().ajax().update("form:messages");
		} finally {
			EntityManagerUtil.close(manager);
		}
	}
	
	public void jasper() throws JRException, IOException, SQLException {
		String jasperPath = "/jasper/ReporteTraspaso.jrxml";
		String filename = "Constancia_de_traspaso.pdf";
		String images = "/images/logo.jpeg";
		String message = null;
		Severity severity = null;
		File reportFile = new File(jasperPath);
		File imgfile = null;
		JasperReportUtil jasperReportUtil = new JasperReportUtil();
		Map<String, Object> parameters = new HashMap<String, Object>();
		Connection connection = null;
		try {
			URL resource = getClass().getResource(jasperPath);
			URL resourceimg = getClass().getResource(images);
			String file = resource.getFile();
			String img = resourceimg.getFile();
			reportFile = new File(file);
			imgfile = new File(img);
			log.info(reportFile.getPath());
			numero = String.valueOf(getNumero());
			connection = EntityManagerUtil.getConnection();
			parameters.put("REPORT_CONNECTION", connection);
			parameters.put("FOLIO", this.selectedconstancia.getNumero());
			parameters.put("LogoPath", imgfile.getPath());
			log.info("Parametros: " + parameters.toString());			
			jasperReportUtil.createPdf(filename, parameters, reportFile.getPath());
		} catch (Exception ex) {
			ex.fillInStackTrace();
			log.error("Problema general...", ex);
			message = String.format("No se pudo imprimir el folio %s", this.selectedconstancia.getNumero());
			severity = FacesMessage.SEVERITY_INFO;
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(severity, "Error en impresion", message));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-constanciaServicios");
		} finally {
			conexion.close((Connection) connection);
		}
	}

	public void deleteServicio(TraspasoServicio servicio) {
		this.alServiciosDetalle.remove(servicio);
	}

	public void deletePartida(InventarioDetalle partida) {
		this.destino.remove(partida);
	}

	public Cliente getSelCliente() {
		return selCliente;
	}

	public void setSelCliente(Cliente selCliente) {
		this.selCliente = selCliente;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}
	
	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public List<Cliente> getClientes() {
		return clientes;
	}

	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public Integer getIdUnidadManejo() {
		return idUnidadManejo;
	}

	public void setIdUnidadManejo(Integer idUnidadManejo) {
		this.idUnidadManejo = idUnidadManejo;
	}

	public Integer getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(Integer idProducto) {
		this.idProducto = idProducto;
	}

	public List<ProductoPorCliente> getAlProductosFiltered() {
		return alProductosFiltered;
	}

	public void setAlProductosFiltered(List<ProductoPorCliente> alProductosFiltered) {
		this.alProductosFiltered = alProductosFiltered;
	}

	public BigDecimal getPeso() {
		return peso;
	}

	public void setPeso(BigDecimal peso) {
		this.peso = peso;
	}

	public PartidaServicio getSelPartida() {
		return selPartida;
	}

	public void setSelPartida(PartidaServicio selPartida) {
		this.selPartida = selPartida;
	}

	public List<PartidaServicio> getAlPartidas() {
		return alPartidas;
	}

	public void setAlPartidas(List<PartidaServicio> alPartidas) {
		this.alPartidas = alPartidas;
	}

	public List<TraspasoServicio> getAlServiciosDetalle() {
		return alServiciosDetalle;
	}

	public void setAlServiciosDetalle(List<TraspasoServicio> alServiciosDetalle) {
		this.alServiciosDetalle = alServiciosDetalle;
	}


	public ConstanciaServicioDetalle getSelServicio() {
		return selServicio;
	}

	public void setSelServicio(ConstanciaServicioDetalle selServicio) {
		this.selServicio = selServicio;
	}

	public BigDecimal getCantidadServicio() {
		return cantidadServicio;
	}

	public void setCantidadServicio(BigDecimal cantidadServicio) {
		this.cantidadServicio = cantidadServicio;
	}

	public boolean isHabilitareporte() {
		return habilitareporte;
	}

	public void setHabilitareporte(boolean habilitareporte) {
		this.habilitareporte = habilitareporte;
	}

	public List<EstadoConstancia> getEstados() {
		return estados;
	}

	public void setEstados(List<EstadoConstancia> estados) {
		this.estados = estados;
	}

	public List<ConstanciaDeDeposito> getListaconstanciadepo() {
		return listaconstanciadepo;
	}

	public void setListaconstanciadepo(List<ConstanciaDeDeposito> listaconstanciadepo) {
		this.listaconstanciadepo = listaconstanciadepo;
	}



	public List<Partida> getPartida() {
		return partida;
	}

	public void setPartida(List<Partida> partida) {
		this.partida = partida;
	}

	public List<DetallePartida> getLdpartida() {
		return ldpartida;
	}

	public void setLdpartida(List<DetallePartida> ldpartida) {
		this.ldpartida = ldpartida;
	}

	public List<InventarioDetalle> getInventario() {
		return inventario;
	}

	public void setInventario(List<InventarioDetalle> inventario) {
		this.inventario = inventario;
	}



	public TraspasoPartida getTp() {
		return tp;
	}

	public void setTp(TraspasoPartida tp) {
		this.tp = tp;
	}

	public List<InventarioDetalle> getDestino() {
		return destino;
	}

	public void setDestino(List<InventarioDetalle> destino) {
		this.destino = destino;
	}

	public Date getFecha_ini() {
		return fecha_ini;
	}

	public void setFecha_ini(Date fecha_ini) {
		this.fecha_ini = fecha_ini;
	}

	public Date getFecha_final() {
		return fecha_final;
	}

	public void setFecha_final(Date fecha_final) {
		this.fecha_final = fecha_final;
	}

	public Inventario getInv() {
		return inv;
	}

	public void setInv(Inventario inv) {
		this.inv = inv;
	}


	public List<ConstanciaTraspaso> getListaTraspasos() {
		return listaTraspasos;
	}

	public void setListaTraspasos(List<ConstanciaTraspaso> listaTraspasos) {
		this.listaTraspasos = listaTraspasos;
	}

	public ConstanciaTraspaso getSelectedconstancia() {
		return selectedconstancia;
	}

	public void setSelectedconstancia(ConstanciaTraspaso selectedconstancia) {
		this.selectedconstancia = selectedconstancia;
	}

	public List<TraspasoPartida> getListaTraspasoPartida() {
		return listaTraspasoPartida;
	}

	public void setListaTraspasoPartida(List<TraspasoPartida> listaTraspasoPartida) {
		this.listaTraspasoPartida = listaTraspasoPartida;
	}

	public List<TraspasoServicio> getListaServicios() {
		return listaServicios;
	}

	public void setListaServicios(List<TraspasoServicio> listaServicios) {
		this.listaServicios = listaServicios;
	}

	public Integer getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Integer idCliente) {
		this.idCliente = idCliente;
	}

	public Date getMaxDate() {
		return maxDate;
	}

	public void setMaxDate(Date maxDate) {
		this.maxDate = maxDate;
	}



}
