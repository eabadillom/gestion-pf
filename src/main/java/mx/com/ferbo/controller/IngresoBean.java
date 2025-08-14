package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

import mx.com.ferbo.business.EntradaBL;
import mx.com.ferbo.business.UsuarioBL;
import mx.com.ferbo.dao.AvisoDAO;
import mx.com.ferbo.dao.CamaraDAO;
import mx.com.ferbo.dao.UnidadDeManejoDAO;
import mx.com.ferbo.model.Aviso;
import mx.com.ferbo.model.Camara;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ConstanciaDeDeposito;
import mx.com.ferbo.model.Partida;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.Producto;
import mx.com.ferbo.model.UnidadDeManejo;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.util.InventarioException;


@Named(value = "ingresoBean")
@ViewScoped
public class IngresoBean implements Serializable {

	private static final long serialVersionUID = 4358310646334303871L;
	private static Logger log = LogManager.getLogger(IngresoBean.class);
	
	private AvisoDAO          avisoDAO  = null;
	private CamaraDAO         camaraDAO = null;
	private UnidadDeManejoDAO unidadDAO = null;
	
	private List<Cliente>        clientes   = null;
	private List<Aviso>          avisos     = null;
	private List<Planta>         plantas    = null;
	private List<Camara>         camaras    = null;
	private List<Producto>       productos  = null;
	private List<UnidadDeManejo> unidades   = null;
	
	private Usuario              usuario = null;
	private ConstanciaDeDeposito entrada = null;
	private Planta               planta  = null;
	private Camara               camara  = null;
	private Partida              partida = null;
	
	private FacesContext context = null;
	private HttpServletRequest request = null;
	private boolean capturar = false;
	
	@SuppressWarnings("unchecked")
	public IngresoBean() {
		this.context = FacesContext.getCurrentInstance();
		this.request = (HttpServletRequest) context.getExternalContext().getRequest();
		
		
		try {
			avisoDAO  = new AvisoDAO();
			camaraDAO = new CamaraDAO();
			unidadDAO = new UnidadDeManejoDAO();
			
			clientes = (List<Cliente>) request.getSession(false).getAttribute("clientesActivosList");
			usuario = (Usuario) request.getSession(false).getAttribute("usuario");
			
			plantas = UsuarioBL.buscarPlantasAutorizadas(usuario);
			planta = UsuarioBL.buscarPlantaAsignada(usuario)
					.orElseThrow(() -> new InventarioException("El usuario no tiene una planta asignada"));
			unidades = unidadDAO.buscarTodos();
			
			this.entrada = EntradaBL.crear();
			
		} catch(Exception ex) {
			log.error("Ocurrió un problema al iniciar el módulo de registro de constancias de depósito...", ex);
		}
	}
	
	@PostConstruct
	public void init() {
		log.info("Entrando a Post Construct.");
	}
	
	public void crearEntrada() {
		FacesMessage message = null;
        Severity severity = null;
        String mensaje = null;
        String titulo = "Datos generales";
        
        String folio = null;
        
		try {
			if(this.entrada.getCteCve() == null)
				throw new InventarioException("Seleccione un cliente.");
			
			this.avisos = avisoDAO.buscarPorCliente(this.entrada.getCteCve().getCteCve());

			if(this.entrada.getAvisoCve() == null)
				throw new InventarioException("Seleccione un aviso.");
			
			if(this.planta == null)
				throw new InventarioException("Seleccione una planta.");
			
			this.camaras = this.camaraDAO.buscarPorPlanta(this.planta);
			
			if(this.camara == null)
				throw new InventarioException("Seleccione una camara.");
			
			folio = EntradaBL.crearFolio(this.entrada, this.planta);
			this.entrada.setFolioCliente(folio);
			
			this.productos = EntradaBL.productosPorCliente(this.entrada.getCteCve());
			
			this.partida = EntradaBL.crearPartida(this.entrada, this.camara);
			
			this.capturar = true;
			PrimeFaces.current().ajax().update("form:pnl-config-tarima");
			mensaje = "Configure sus tarimas";
			severity = FacesMessage.SEVERITY_INFO;
		} catch(InventarioException ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} catch(Exception ex) {
			log.error("Problema con la creación de la constancia de depósito...", ex);
			mensaje = "Ocurrió un problema con la creación de la constancia de depósito. Avise a su administrador de sistemas.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update("form:messages");
		}
	}
	
	public List<Cliente> getClientes() {
		return clientes;
	}

	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
	}

	public List<Aviso> getAvisos() {
		return avisos;
	}

	public void setAvisos(List<Aviso> avisos) {
		this.avisos = avisos;
	}

	public List<Planta> getPlantas() {
		return plantas;
	}

	public void setPlantas(List<Planta> plantas) {
		this.plantas = plantas;
	}

	public ConstanciaDeDeposito getEntrada() {
		return entrada;
	}

	public void setEntrada(ConstanciaDeDeposito entrada) {
		this.entrada = entrada;
	}

	public List<Camara> getCamaras() {
		return camaras;
	}

	public void setCamaras(List<Camara> camaras) {
		this.camaras = camaras;
	}

	public Planta getPlanta() {
		return planta;
	}

	public void setPlanta(Planta planta) {
		this.planta = planta;
	}

	public Camara getCamara() {
		return camara;
	}

	public void setCamara(Camara camara) {
		this.camara = camara;
	}

	public boolean isCapturar() {
		return capturar;
	}

	public void setCapturar(boolean capturar) {
		this.capturar = capturar;
	}

	public List<Producto> getProductos() {
		return productos;
	}

	public void setProductos(List<Producto> productos) {
		this.productos = productos;
	}

	public Partida getPartida() {
		return partida;
	}

	public void setPartida(Partida partida) {
		this.partida = partida;
	}

	public List<UnidadDeManejo> getUnidades() {
		return unidades;
	}

	public void setUnidades(List<UnidadDeManejo> unidades) {
		this.unidades = unidades;
	}
	

}
