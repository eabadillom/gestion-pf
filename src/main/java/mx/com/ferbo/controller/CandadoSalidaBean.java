package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

import mx.com.ferbo.business.salidas.SalidasBL;
import mx.com.ferbo.dao.CandadoSalidaDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.Salida;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.util.InventarioException;

@Named
@ViewScoped
public class CandadoSalidaBean implements Serializable {

	private static final long serialVersionUID = 1;
	private static Logger log = LogManager.getLogger(CandadoSalidaBean.class);

	private CandadoSalidaDAO dao;
	
	private Usuario usuario;
	private FacesContext context;
	private HttpServletRequest request;
	private List<Cliente> clientes = null;
	private List<Cliente> busquedaClientes = null;
	private Cliente cliente = null;
	private List<Salida> ordenesSalida = null;
	private String query = null;
	
	@Inject
	private SalidasBL salidaBO;

	@SuppressWarnings("unchecked")
	public CandadoSalidaBean() {
		context = FacesContext.getCurrentInstance();
		request = (HttpServletRequest) context.getExternalContext().getRequest();
		usuario = (Usuario) request.getSession(false).getAttribute("usuario");
		clientes = (List<Cliente>) request.getSession(false).getAttribute("clientesActivosList");;
		log.info("El usuario {} ingresa al mantenimiento de candados de salida...", this.usuario.getUsuario());
		
		dao = new CandadoSalidaDAO();
	}
	
	public void precarga() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
	    
	    if (facesContext.isPostback()) {
	        return;
	    }
	    
	    this.busquedaClientes = new ArrayList<Cliente>(this.clientes);
	}
	
	public void buscar() {
		log.info("Query: {}", query);
		if(query == null || query.trim().isEmpty()) {
			this.busquedaClientes = new ArrayList<Cliente>(this.clientes);
			return;
		}
		
		String texto = this.query.toLowerCase();
		
		this.busquedaClientes = this.clientes.stream()
				.filter(cliente -> cliente.getNombre().toLowerCase().contains(texto))
				.collect(Collectors.toList())
				;
	}
	
	public void cargar(Cliente cliente) {
		log.info("Cliente: {}", cliente);
		this.ordenesSalida = salidaBO.buscarEnviadas(cliente);
	}
	
	public void update() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = null;
		String result = null;
		
		try {
			result = dao.update(this.cliente.getCandadoSalida());
			if(result == null) {
				titulo = "Candado actualizado";
				mensaje = "Informe a almacén que puede continuar con el registro de salida de la mercancía.";
				severity = FacesMessage.SEVERITY_INFO;
				PrimeFaces.current().executeScript("PF('dg-modifica').hide()");
			} else {
				throw new InventarioException("Error al actualizar el candado");
			}
				
		} catch(Exception ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
			message = new FacesMessage(severity, titulo, mensaje);
		} finally {
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages", "form:dtCandado");
		}
	}

	public List<Cliente> getClientes() {
		return clientes;
	}

	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public List<Salida> getOrdenesSalida() {
		return ordenesSalida;
	}

	public void setOrdenesSalida(List<Salida> ordenesSalida) {
		this.ordenesSalida = ordenesSalida;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public List<Cliente> getBusquedaClientes() {
		return busquedaClientes;
	}

	public void setBusquedaClientes(List<Cliente> busquedaClientes) {
		this.busquedaClientes = busquedaClientes;
	}

}
