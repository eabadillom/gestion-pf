package mx.com.ferbo.controller;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

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

import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.dao.DetalleConstanciaSalidaDAO;
import mx.com.ferbo.dao.OrdenSalidaDAO;
import mx.com.ferbo.dao.PreSalidaServicioDAO;
import mx.com.ferbo.dao.PrecioServicioDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ConstanciaServicioDetalle;
import mx.com.ferbo.model.DetalleConstanciaSalida;
import mx.com.ferbo.model.DetallePartida;
import mx.com.ferbo.model.OrdenSalida;
import mx.com.ferbo.model.Partida;
import mx.com.ferbo.model.PreSalidaServicio;
import mx.com.ferbo.model.PrecioServicio;
import mx.com.ferbo.model.ServicioFactura;
import mx.com.ferbo.ui.OrdenDeSalidas;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.InventarioException;


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
	
	private ClienteDAO clienteDAO;
	private OrdenSalidaDAO ordenSalidaDAO;
	private PrecioServicioDAO precioServicioDAO;
	private PreSalidaServicioDAO presalidaservicioDAO;
	
	
	private boolean confirmacion;
	private String folioSelected;
	private Date fecha;
	private Time tmSalida;
	
	private Cliente clienteSelect;
	private OrdenSalida ordensalida;
	private ConstanciaServicioDetalle selServicio;
	private DetallePartida dp;
	private DetalleConstanciaSalida dcs;
	private OrdenDeSalidas ordenDeSalidas;
	private PrecioServicio idServicio;
	private Integer cantidadServicio;
	private PreSalidaServicio pss; 
	
	public OrdenSalidaBean() {
		clienteDAO = new ClienteDAO();
		ordenSalidaDAO = new OrdenSalidaDAO();
		presalidaservicioDAO = new PreSalidaServicioDAO();
		precioServicioDAO = new PrecioServicioDAO();
			
		listaOrdenSalida = new ArrayList<OrdenSalida>();
		listaClientes = new ArrayList<Cliente>();
	
		listaServicios = new ArrayList<PrecioServicio>();
		listaSalidasporPlantas = new ArrayList<OrdenDeSalidas>();
	}
	@PostConstruct
	public void init() {
		listaClientes = clienteDAO.buscarHabilitados(true);
		fecha= new Date();
		DateUtil.setTime(fecha, 0, 0, 0, 0);
	}
	
/*	public void buscarFoliosporFecha() {
		listaOrdenSalida = ordenSalidaDAO.buscarFolioPorCliente(clienteSelect, fecha);
	}*/
	
	public void reload() throws IOException {
	    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	    ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
	}
	
	public void filtroPorPlanta() {
		Integer folio = null;
		OrdenSalida os = null;
		log.debug("Orden salida seleccionada: {}", this.folioSelected);
	
		listaSalidasporPlantas = ordenSalidaDAO.buscarpoPlanta(folioSelected, fecha);
		listaSalidasporFolio = ordenSalidaDAO.buscaFolios(folioSelected);
		listaPreSalidaServicio = presalidaservicioDAO.buscarPorFolios(folioSelected);
		folio = listaSalidasporFolio.size();
		if(folio != 0) {
			ordensalida = listaSalidasporFolio.get(0);
		}
	}
	
	public void addMessage(AjaxBehaviorEvent event) {
        UIComponent component = event.getComponent();
        if (component instanceof UIInput) {
            String summary = confirmacion ? "Checked" : "Unchecked";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(summary));
            }
        }
	public void guardar() {
		String message = null;
		Severity severity = null;		
		List<OrdenSalida> listaSalidas = null;
		OrdenSalida os=null;
		try {			
			if(confirmacion != true) 
				throw new InventarioException("Favor de confirmar almenos una orden de salida.");
			listaSalidas = ordenSalidaDAO.buscarFolioPorCliente(clienteSelect, fecha);
			os = new OrdenSalida();
			os.setFolioSalida(folioSelected);
			os.setStEstado("C");
			os.setFechaSalida(fecha);
			listaSalidas.add(os);
			ordenSalidaDAO.actualizar(os);
		} catch (InventarioException ex) {
			log.error("Problema para obtener la información...", ex);
			message = ex.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
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
	
	@SuppressWarnings("unlikely-arg-type")
	public void deleteServicio(ConstanciaServicioDetalle servicio) {
		this.listaPreSalidaServicio.remove(servicio);
		this.listaServicios.remove(servicio);
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
			//buscarFoliosporFecha();
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
	
	public void agregaServicios() {
		String message = null;
		Severity severity = null;
		PreSalidaServicio ps = null;
		int count = 0;
		try {
			if(this.idServicio == null) 
					throw new InventarioException("Selecione almenos un servicio");
			if (this.clienteSelect == null )
				throw new InventarioException("Debe seleccionar el cliente");
			if (this.cantidadServicio == null )
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
					count = listaPreSalidaServicio.size();
					//if(count < 1) { }
					if(count == 1) {
						//ps = listaPreSalidaServicio.get(0);
					if (srv.getIdServicio() == idServicio.getServicio()) {
						
						coincidencias++;
						}
						}else {
						 //listaPreSalidaServicio.add(ps);
							diferentes++;			
					}
					
				}

				if (coincidencias == 1) {
					System.out.println("ya existe el servicio");
					message = "Ya existe el servicio.";
					severity = FacesMessage.SEVERITY_ERROR;
				} else if (diferentes > 0) {
					listaPreSalidaServicio.add(ps);
					message = "Producto agregado correctamente.";
					severity = FacesMessage.SEVERITY_INFO;
				}
			
			
		} catch (Exception e) {
			log.error("Problema para obtener el listado de servicios del cliente.", e);
			message = "Problema con la información de servicios.";
			severity = FacesMessage.SEVERITY_ERROR;
			
		}finally {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Servicio", message));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-servicios");
	
		}
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

}
