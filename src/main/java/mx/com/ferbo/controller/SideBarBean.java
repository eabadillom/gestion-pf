package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.salidas.SalidasBL;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.StatusSalida;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.util.DateUtil;

@Named
@SessionScoped
public class SideBarBean implements Serializable {

	private static final long serialVersionUID = 8802717839932668484L;
	private static Logger log = LogManager.getLogger(SideBarBean.class);
	
        @Inject
	private SalidasBL salidasBL;
        
	private List<Cliente> listaClientesActivos;
	private List<Cliente> listaClientesTodos;
	private Usuario usuario;
	private Integer idCliente = null;
	
	
	private FacesContext context;
    private HttpServletRequest request;
    private HttpSession session;
    private Integer numeroEntradas;
    private Integer numeroSalidas;
    
    private String fotografia;
    
    public String getFotografia() {
		return fotografia;
	}

	public void setFotografia(String fotografia) {
		this.fotografia = fotografia;
	}

	public SideBarBean() {
    	this.usuario = new Usuario();
    }
    
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		Date fecha = null;
		StatusSalida statusSalida = null;
		try {
			fecha = new Date();
			DateUtil.resetTime(fecha);
			context = FacesContext.getCurrentInstance();
			request = (HttpServletRequest) context.getExternalContext().getRequest();
			session = request.getSession(false);
			this.usuario = (Usuario) request.getSession(true).getAttribute("usuario");
			
			listaClientesActivos = (List<Cliente>) request.getSession(false).getAttribute("clientesActivosList");
			listaClientesTodos = (List<Cliente>) request.getSession(false).getAttribute("clientesTodosList");
			fotografia = (String) request.getSession(false).getAttribute("fotografia");
			
			if(this.usuario.getPerfil() == 1 || this.usuario.getPerfil() == 4) {
				statusSalida = salidasBL.obtenerStatusEnviado();
                                numeroSalidas = salidasBL.totalSalidasPorCliente(statusSalida.getClave(), fecha, this.usuario.getIdPlanta());
				log.info("Ordenes de salida pendientes: {}", numeroSalidas);
			}
			
			this.numeroEntradas = null;
			
		} catch(Exception ex) {
			log.error("Problema al iniciar la sesión del usuario.", ex);
		} finally {
			
		}
	}
	
	public void logout() {
		String contextPath = null;
		String fullPath = null;
		try {
			log.info("El usuario {} intenta finalizar su sesión...", this.usuario.getUsuario());
			
			contextPath = context.getExternalContext().getApplicationContextPath();
			fullPath = contextPath + "/login.xhtml";
			
    		this.usuario = (Usuario)session.getAttribute("usuario");
    		this.idCliente = (Integer) session.getAttribute("idCliente");
    		
    		if(this.usuario != null) {
    			session.removeAttribute("usuario");
    			log.info("Información del usuario eliminada de la sesión.");
    		}
    		if(this.idCliente != null) {
    			session.removeAttribute("idCliente");
    			log.info("Información del id de cliente eliminada de la sesión.");
    		}
    		
    		session.setAttribute("usuario", null);
    		session.setAttribute("idCliente", null);
    		
    		log.info("Invalidando sesión...");
    		session.invalidate();
    		
    		log.info("Obteniendo un nuevo objeto de sesión...");
    		session = request.getSession(true);
    		
    		log.info("Redirigiendo al usuario a {}", fullPath);
    		context.getExternalContext().redirect(fullPath);
    		
    		log.info("Fin del proceso de cierre de sesión.");
    	} catch(Exception ex) {
    		log.warn("Problema en el cierre de sesión del usuario...", ex);
    	}
	}
	
	public String redirectOrdenesSalida() {
            return "/inventarios/ordenSalidas/principal?faces-redirect=true";
        }
	
	public String redirectOrdenEntrada() {
            return "/inventarios/ordenEntrada?faces-redirect=true";
	}
	
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Integer getNumeroSalidas() {
		return numeroSalidas;
	}

	public void setNumeroSalidas(Integer numeroSalidas) {
		this.numeroSalidas = numeroSalidas;
	}

	public Integer getNumeroEntradas() {
		return numeroEntradas;
	}

	public void setNumeroEntradas(Integer numeroEntradas) {
		this.numeroEntradas = numeroEntradas;
	}

	public List<Cliente> getListaClientesActivos() {
		return listaClientesActivos;
	}

	public void setListaClientesActivos(List<Cliente> listaClientes) {
		this.listaClientesActivos = listaClientes;
	}

	public List<Cliente> getListaClientesTodos() {
		return listaClientesTodos;
	}

	public void setListaClientesTodos(List<Cliente> listaClientesTodos) {
		this.listaClientesTodos = listaClientesTodos;
	}
}