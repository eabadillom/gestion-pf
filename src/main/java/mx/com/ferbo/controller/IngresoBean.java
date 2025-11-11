package mx.com.ferbo.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import mx.com.ferbo.business.ClienteBL;
import mx.com.ferbo.business.EntradaBL;
import mx.com.ferbo.business.UsuarioBL;
import mx.com.ferbo.dao.AvisoDAO;
import mx.com.ferbo.dao.CamaraDAO;
import mx.com.ferbo.dao.ServicioDAO;
import mx.com.ferbo.dao.UnidadDeManejoDAO;
import mx.com.ferbo.model.Aviso;
import mx.com.ferbo.model.Camara;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ConstanciaDeDeposito;
import mx.com.ferbo.model.ConstanciaDepositoDetalle;
import mx.com.ferbo.model.Partida;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.PrecioServicio;
import mx.com.ferbo.model.Producto;
import mx.com.ferbo.model.Servicio;
import mx.com.ferbo.model.Tarima;
import mx.com.ferbo.model.UnidadDeManejo;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.reports.TicketEntradaReport;
import mx.com.ferbo.util.InventarioException;


@Named(value = "ingresoBean")
@ViewScoped
public class IngresoBean implements Serializable {

	private static final long serialVersionUID = 4358310646334303871L;
	private static Logger log = LogManager.getLogger(IngresoBean.class);
	
	private AvisoDAO          avisoDAO    = null;
	private CamaraDAO         camaraDAO   = null;
	private UnidadDeManejoDAO unidadDAO   = null;
	private ServicioDAO       servicioDAO = null;
	
	private List<Cliente>        clientes         = null;
	private List<Aviso>          avisos           = null;
	private List<Planta>         plantas          = null;
	private List<Camara>         camaras          = null;
	private List<Producto>       productos        = null;
	private List<UnidadDeManejo> unidades         = null;
        private List<Partida>        partidas         = null;
	private List<Tarima>         tarimas          = null;
	private List<PrecioServicio> serviciosCliente = null;
	
	private Usuario                   usuario            = null;
	private ConstanciaDeDeposito      entrada            = null;
	private Planta                    planta             = null;
	private Camara                    camara             = null;
	private Partida                   partida            = null;
	private Partida                   partidaEdit        = null;
	private Tarima                    tarima             = null;
	private PrecioServicio            servicio           = null;
	private BigDecimal                cantidadServicio   = null;
	private Integer                   numTarimas         = null;
        private Integer                   totalPartidasTarima= 0;
        private BigDecimal                noTarimas          = null;
	private Boolean                   isCongelacion      = Boolean.FALSE;
	private Boolean                   isConservacion     = Boolean.FALSE;
	private Boolean                   isRefrigeracion    = Boolean.FALSE;
	private Boolean                   isManiobras        = Boolean.FALSE;
	
	private boolean capturar = false;
	private boolean editar   = true;
        private boolean cargaTarimas = false;
        private int activeTabIndex;
	
	private FacesContext       context = null;
	private HttpServletRequest request = null;
	private StreamedContent    file    = null;
	private byte[]             bytes   = {};
	
	private static final Integer SRV_CONGELACION   = 619;
	private static final Integer SRV_CONSERVACION  = 620;
	private static final Integer SRV_REFRIGERACION = 621;
	private static final Integer SRV_MANIOBRAS     = 622;
	
	private Servicio congelacion   = null;
	private Servicio conservacion  = null;
	private Servicio refrigeracion = null;
	private Servicio maniobras     = null;
	
	@SuppressWarnings("unchecked")
	public IngresoBean() {
		this.context = FacesContext.getCurrentInstance();
		this.request = (HttpServletRequest) context.getExternalContext().getRequest();
		
		try {
			
			this.avisoDAO    = new AvisoDAO();
			this.camaraDAO   = new CamaraDAO();
			this.unidadDAO   = new UnidadDeManejoDAO();
			this.servicioDAO = new ServicioDAO();
			
			this.clientes = (List<Cliente>) this.request.getSession(false).getAttribute("clientesActivosList");
			this.usuario = (Usuario) this.request.getSession(false).getAttribute("usuario");
			
			this.plantas = UsuarioBL.buscarPlantasAutorizadas(this.usuario);
			this.planta = UsuarioBL.buscarPlantaAsignada(this.usuario)
					.orElseThrow(() -> new InventarioException("El usuario no tiene una planta asignada"));
			this.unidades = this.unidadDAO.buscarTodos();
			
			this.entrada = EntradaBL.crear();
			
			//Se crea una partida vacía, solo para que no provoque problemas con el renderizado
			//de los objetos en la pantalla.
			this.partida = EntradaBL.crearPartida(new Camara());
                        this.partidas = new ArrayList();
			
			this.congelacion   = this.servicioDAO.buscarPorId(SRV_CONGELACION);
			this.conservacion  = this.servicioDAO.buscarPorId(SRV_CONSERVACION);
			this.refrigeracion = this.servicioDAO.buscarPorId(SRV_REFRIGERACION);
			this.maniobras     = this.servicioDAO.buscarPorId(SRV_MANIOBRAS);
			
			this.file = DefaultStreamedContent.builder().contentType("application/pdf").contentLength(this.bytes.length)
					.name("ticket.pdf").stream(() -> new ByteArrayInputStream(this.bytes)).build();
			log.info("El usuario {} entra a la captura de constancias de depósito", this.usuario.getUsuario());
                        this.activeTabIndex = this.cargaTarimas ? 2 : 0;
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
			log.info("Cliente: {}", this.entrada.getCteCve());
			
			this.avisos = avisoDAO.buscarPorCliente(this.entrada.getCteCve().getCteCve());

			if(this.entrada.getAvisoCve() == null)
				throw new InventarioException("Seleccione un aviso.");
			log.info("Aviso: {}", this.entrada.getAvisoCve());
			
			if(this.planta == null)
				throw new InventarioException("Seleccione una planta.");
			log.info("Planta: {}", this.planta);
			
			this.camaras = this.camaraDAO.buscarPorPlanta(this.planta);
			
			if(this.camara == null)
				throw new InventarioException("Seleccione una camara.");
			log.info("Camara: {}", this.camara);
			
			folio = EntradaBL.crearFolio(this.entrada, this.planta);
			log.info("Folio: {}", folio);
			
			this.entrada.setFolioCliente(folio);
			
			this.productos        = ClienteBL.productosPorCliente(this.entrada.getCteCve());
			this.serviciosCliente = ClienteBL.getServicios(this.entrada.getCteCve(), this.entrada.getAvisoCve());
			
			this.isCongelacion   = ClienteBL.isServicio(this.serviciosCliente, SRV_CONGELACION);
			this.isConservacion  = ClienteBL.isServicio(this.serviciosCliente, SRV_CONSERVACION);
			this.isRefrigeracion = ClienteBL.isServicio(this.serviciosCliente, SRV_REFRIGERACION);
			this.isManiobras     = ClienteBL.isServicio(this.serviciosCliente, SRV_MANIOBRAS);
			
			this.partida = EntradaBL.crearPartida(this.camara);
			
			this.capturar = true;
			PrimeFaces.current().ajax().update("form:pnl-config-tarima", "form:pnl-add-producto", "form:pnl-servicios");
			mensaje = "Configure sus tarimas";
			severity = FacesMessage.SEVERITY_INFO;
			log.info("Creación de constancia de depósito terminada, procediendo con captura de productos.");
		} catch(InventarioException ex) {
			log.warn(ex.getMessage());
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
	
	public void agregarTarimas() {
		FacesMessage message = null;
        Severity severity = null;
        String mensaje = null;
        String titulo = "Configuración de la tarima";
		
		try {
                        if(this.numTarimas == null)
				throw new InventarioException("Debe indicar el número de tarimas.");
			
			if(this.numTarimas <= 0)
				throw new InventarioException("El número de tarimas indicado es incorrecto");
			
			log.info("Agregando {} tarima(s)", this.numTarimas);
			
			this.tarimas = EntradaBL.crearTarimas(this.entrada, this.numTarimas, this.partida, this.tarimas);
			this.totalPartidasTarima = this.totalPartidasTarima + this.numTarimas;
                        
                        this.partida = EntradaBL.crearPartida(this.camara);
			
			log.info("{} tarimas agregadas.", this.numTarimas);
			
			mensaje = String.format("Se agregaron %d tarima(s)", this.numTarimas);
			severity = FacesMessage.SEVERITY_INFO;
			
			this.numTarimas = null;
                        this.partida = EntradaBL.crearPartida(camara);
		} catch(InventarioException ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
			log.warn(ex.getMessage());
		} catch(Exception ex) {
			log.error("Problema para generar las tarimas...", ex);
			mensaje = "Ocurrió un problema al generar la(s) tarima(s).";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update("form:messages");
		}
	}
	
	public void eliminarTarima(Tarima tarima) {
		FacesMessage message = null;
        Severity severity = null;
        String mensaje = null;
        String titulo = "Eliminar tarima";
        
        try {
                this.totalPartidasTarima = this.totalPartidasTarima - EntradaBL.totalPartidasEliminadas(this.tarimas, tarima);
        	EntradaBL.eliminarTarima(this.entrada, this.tarimas, tarima);
                log.info("Tarima eliminada: {}", tarima);
        	
        	mensaje= "Tarima eliminada";
        	severity = FacesMessage.SEVERITY_INFO;
        } catch(InventarioException ex ) {
        	log.warn(ex.getMessage());
        	mensaje = ex.getMessage();
        	severity = FacesMessage.SEVERITY_WARN;
        } catch(Exception ex) {
        	log.error("Problema para eliminar la tarima...");
        	mensaje = "Ocurrió un problema al eliminar la tarima.";
        	severity = FacesMessage.SEVERITY_ERROR;
        } finally {
        	message = new FacesMessage(severity, titulo, mensaje);
        	FacesContext.getCurrentInstance().addMessage(null, message);
        	PrimeFaces.current().ajax().update("form:messages");
        }
	}
        
        public void agregarPartidaCompleta(){
            FacesMessage message = null;
            Severity severity = null;
            String mensaje = null;
            String titulo = "Carga completa";
            try {
                log.info("Agregando una partida con carga completa");
                EntradaBL.agregarPartidaCompleta(this.entrada, this.noTarimas, this.partida);
                log.info("Carga completa configurada");
                this.cargaPartidas();
                this.partida = EntradaBL.crearPartida(camara);
                this.noTarimas = null;
                
                mensaje = "Se termino de configurar la partida con carga completa";
        	severity = FacesMessage.SEVERITY_INFO;
            } catch(InventarioException ex ) {
                log.warn(ex.getMessage());
        	mensaje = ex.getMessage();
        	severity = FacesMessage.SEVERITY_WARN;
            } catch(Exception ex) {
                log.error("Problema para configurar la carga completa...");
        	mensaje = "Ocurrió un problema para configurar la carga completa.";
        	severity = FacesMessage.SEVERITY_ERROR;
            } finally {
                message = new FacesMessage(severity, titulo, mensaje);
        	FacesContext.getCurrentInstance().addMessage(null, message);
                PrimeFaces.current().ajax().update("form:messages");
            }
        }
        
        public void editarPartida(){
            FacesMessage message = null;
            Severity severity = null;
            String mensaje = null;
            String titulo = "Carga completa";
            try {
                log.info("Editando una partida con carga completa");
                EntradaBL.editarPartidaCompleta(this.entrada, partidaEdit);
                log.info("Se edito la partida con carga completa");
                this.cargaPartidas();
                
                mensaje = "Se termino de editar una partida con carga completa";
        	severity = FacesMessage.SEVERITY_INFO;
            } catch(InventarioException ex ) {
                log.warn(ex.getMessage());
        	mensaje = ex.getMessage();
        	severity = FacesMessage.SEVERITY_WARN;
            } catch(Exception ex) {
                log.error("Problema para editar la carga completa...", ex);
        	mensaje = "Ocurrió un problema para configurar la carga completa.";
        	severity = FacesMessage.SEVERITY_ERROR;
            } finally {
                message = new FacesMessage(severity, titulo, mensaje);
        	FacesContext.getCurrentInstance().addMessage(null, message);
                PrimeFaces.current().ajax().update("form:messages");
            }
        }
        
        public void eliminarPartida(Partida partida){
            FacesMessage message = null;
            Severity severity = null;
            String mensaje = null;
            String titulo = "Carga completa";
            try {
                log.info("Eliminando una partida con carga completa");
                EntradaBL.eliminarPartidaCompleta(this.entrada, partida);
                log.info("Se elimino la partida con carga completa");
                this.cargaPartidas();
                
                mensaje= "Se termino de eliminar una partida con carga completa";
        	severity = FacesMessage.SEVERITY_INFO;
            } catch(InventarioException ex ) {
                log.warn(ex.getMessage());
        	mensaje = ex.getMessage();
        	severity = FacesMessage.SEVERITY_WARN;
            } catch(Exception ex) {
                log.error("Problema para eliminar la carga completa...", ex);
        	mensaje = "Ocurrió un problema para eliminar una carga completa.";
        	severity = FacesMessage.SEVERITY_ERROR;
            } finally {
                message = new FacesMessage(severity, titulo, mensaje);
        	FacesContext.getCurrentInstance().addMessage(null, message);
                PrimeFaces.current().ajax().update("form:messages");
            }
        }
        
        public void cargaPartidas(){
                try {
                    this.partidas = this.entrada.getPartidaList().stream()
                        .filter(p -> (p.getNoTarimas() != null && p.getNoTarimas().compareTo(BigDecimal.ONE) > 0) && (p.getTarima() == null))
                        .collect(Collectors.toList());
                } catch(Exception ex ) {
			log.error("Problema para cargar la lista de partidas...", ex);
		}
        }
	
	public void cargarDetalle(ActionEvent e) {
		try {
			log.info("Cargando partida: {}", this.partida);
			log.info("Producto: {}", this.partida.getUnidadDeProductoCve().getProductoCve().getProductoDs());
		} catch(Exception ex ) {
			log.error("Problema para cargar el detalle de la partida...", ex);
		}
	}
	
	public void prepararPartida() {
		try {
			log.info("Preparando partida...");
			this.partida = EntradaBL.crearPartida(camara);
			log.info("Partida: {}", partida);
		} catch(Exception ex) {
			log.warn("Problema para crear la tarima...", ex);
		}
	}
	
	public void addToTarima() {
		try {
			log.info("Agregando producto a la tarima {}", this.tarima);
			EntradaBL.agregarATarima(this.entrada, this.tarima, this.partida);
                        this.totalPartidasTarima++;
			log.info("Producto agregado a la tarima.");
                        this.partida = EntradaBL.crearPartida(camara);
		} catch (InventarioException ex) {
			log.error("Problema para agregar la partida a la tarima...", ex);
		}
		
	}
	
	public void agregarProducto(Tarima tarima) {
		log.info("Agregando producto a tarima {}...", tarima);
	}
	
	public void eliminarProducto(Partida partida) {
		try {
			log.info("Eliminando partida: {}", partida);
			EntradaBL.eliminarProducto(entrada, tarima, partida);
                        this.totalPartidasTarima--;
			log.info("Partida eliminada.");
		} catch(Exception ex) {
			log.error("Problema para eliminar la partida...", ex);
		}
	}
	
	public Integer cantidadTarima(Tarima tarima) {
		Integer cantidad = tarima.getPartidas().stream()
				.mapToInt(Partida::getCantidadTotal)
				.sum();
		
		return cantidad;
	}
	
	public BigDecimal pesoTarima(Tarima tarima) {
		BigDecimal peso = tarima.getPartidas().stream()
				.map(Partida::getPesoTotal)
				.reduce(BigDecimal.ZERO, BigDecimal::add)
				;
		return peso;
	}
	
	public Integer totalTarimas() {
		if(tarimas == null)
			return 0;
		
		return tarimas.size();
	}
	
	public Integer totalCantidad() {
		Integer totalCantidad;
		
		try {
			totalCantidad = tarimas.stream()
					.mapToInt(tarima -> tarima.getPartidas().stream().mapToInt(Partida::getCantidadTotal).sum())
					.sum()
					;
		} catch(Exception ex) {
			totalCantidad = 0;
		}
		
		return totalCantidad;
	}
	
	public BigDecimal totalPeso() {
		BigDecimal totalPeso = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
		
		try {
			for(Tarima t : this.tarimas) {
				BigDecimal subtotal = t.getPartidas()
						.stream()
						.map(Partida::getPesoTotal)
						.reduce(BigDecimal.ZERO, BigDecimal::add)
						;
				
				totalPeso = totalPeso.add(subtotal);
			}
		} catch(Exception ex) {
			totalPeso = BigDecimal.ZERO.setScale(3, RoundingMode.HALF_EVEN);
		}
		
		return totalPeso;
	}
	
	public List<PrecioServicio> serviciosElegibles() {
		List<PrecioServicio> servicios = null;
		
		if(this.serviciosCliente == null)
			return new ArrayList<PrecioServicio>();
		
		servicios = this.serviciosCliente.stream()
				.filter(item -> (item.getServicio().getServicioCve().equals(SRV_CONGELACION)   == false) )
				.filter(item -> (item.getServicio().getServicioCve().equals(SRV_CONSERVACION)  == false) )
				.filter(item -> (item.getServicio().getServicioCve().equals(SRV_REFRIGERACION) == false) )
				.filter(item -> (item.getServicio().getServicioCve().equals(SRV_MANIOBRAS)     == false) )
				.collect(Collectors.toList())
				;
		
		return servicios;
	}
	
	public void agregarServicio() {
		FacesMessage message = null;
        Severity severity = null;
        String mensaje = null;
        String titulo = "Agregar servicio";
		
		try {
			log.info("Agregando servicio a la entrada: {}", servicio);
			EntradaBL.agregarServicio(entrada, servicio, cantidadServicio);
			this.servicio = null;
			this.cantidadServicio = null;
			log.info("Precio servicio agregado: {}", this.servicio);
		} catch(InventarioException ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
			
			message = new FacesMessage(severity, titulo, mensaje);
        	FacesContext.getCurrentInstance().addMessage(null, message);
        	PrimeFaces.current().ajax().update("form:messages");
		} catch (Exception ex) {
			log.error("Problema para agregar el servicio...", ex);
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
			
			message = new FacesMessage(severity, titulo, mensaje);
        	FacesContext.getCurrentInstance().addMessage(null, message);
        	PrimeFaces.current().ajax().update("form:messages");
			
		}
	}
	
	public void eliminarServicio(ConstanciaDepositoDetalle constanciaServicio) {
		try {
			log.info("Eliminando servicio de la entrada: {}", constanciaServicio);
			EntradaBL.eliminarServicio(entrada, constanciaServicio);
			log.info("Servicio eliminado.");
		} catch(Exception ex) {
			log.error("Problema para eliminar el servicio...", ex);
		}
	}
	
	public void guardar() {
		FacesMessage message = null;
        Severity severity = null;
        String mensaje = null;
        String titulo = "Guardar tarima";
        
        try {
        	log.info("El usuario {} se prepara para guardar la constancia de depósito {}...",
        			this.usuario.getUsuario(), this.entrada.getFolioCliente());
        	
        	if(this.isCongelacion) {
        		EntradaBL.agregarServicio(this.entrada, this.congelacion, BigDecimal.ONE.setScale(2, RoundingMode.HALF_UP));
        		log.info("Servicio de congelación agregado.");
        	}
        	
        	if(this.isConservacion) {
        		EntradaBL.agregarServicio(this.entrada, this.conservacion, BigDecimal.ONE.setScale(2, RoundingMode.HALF_UP));
        		log.info("Servicio de conservación agregado.");
        	}
        	
        	if(this.isRefrigeracion) {
        		EntradaBL.agregarServicio(this.entrada, this.refrigeracion, BigDecimal.ONE.setScale(2, RoundingMode.HALF_UP));
        		log.info("Servicio de refrigeración agregado.");
        	}
        	
        	if(this.isManiobras) {
        		EntradaBL.agregarServicio(this.entrada, this.maniobras, BigDecimal.ONE.setScale(2, RoundingMode.HALF_UP));
        		log.info("Servicio de maniobras agregado.");
        	}
        	
        	EntradaBL.guardar(this.entrada);
        	EntradaBL.actualizarFolio(this.entrada.getCteCve(), this.planta);
        	
        	this.editar = false;
        	
        	mensaje = "La entrada se registró correctamente";
        	severity = FacesMessage.SEVERITY_INFO;
        } catch(InventarioException ex ) {
        	log.warn(ex.getMessage());
        	mensaje = ex.getMessage();
        	severity = FacesMessage.SEVERITY_WARN;
        } catch(Exception ex) {
        	log.error("Problema para eliminar la tarima...");
        	mensaje = "Ocurrió un problema al eliminar la tarima.";
        	severity = FacesMessage.SEVERITY_ERROR;
        } finally {
        	message = new FacesMessage(severity, titulo, mensaje);
        	FacesContext.getCurrentInstance().addMessage(null, message);
        	PrimeFaces.current().ajax().update("form:messages");
        }
	}
	
	public void imprimir() {
		InputStream input;
		byte[] bytes = null;
		String fileName = null;
		
		try {
			fileName = String.format("Entrada_%s.pdf", entrada.getFolioCliente());
			
			bytes = TicketEntradaReport.getPDF(this.entrada.getFolioCliente());
			input = new ByteArrayInputStream(bytes);
			this.file = DefaultStreamedContent.builder()
					.contentType("application/pdf")
					.name(fileName)
					.stream(() -> input)
					.contentLength(bytes.length)
					.build()
					;
			
		} catch(Exception ex) {
			log.error("Problema para obtener el ticket de entrada...", ex);
		}
	}
	
	public void nueva()
	throws IOException {
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		String requestURI = ((HttpServletRequest) context.getRequest()).getRequestURI();
		context.redirect(requestURI);
	}
        
        public void onToggleCargaTarimas() {
            // Si cargaTarimas = false → pestaña 0, 1 (Tarima completa y Agregar a tarima)
            // Si cargaTarimas = true → pestaña 2 (Carga Complete)
            this.activeTabIndex = this.cargaTarimas ? 2 : 0;
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

        public List<Partida> getPartidas() {
            return partidas;
        }

        public void setPartidas(List<Partida> partidas) {
            this.partidas = partidas;
        }

	public List<UnidadDeManejo> getUnidades() {
		return unidades;
	}

	public void setUnidades(List<UnidadDeManejo> unidades) {
		this.unidades = unidades;
	}

        public Integer getTotalPartidasTarima() {
            return totalPartidasTarima;
        }

        public void setTotalPartidasTarima(Integer totalPartidasTarima) {
            this.totalPartidasTarima = totalPartidasTarima;
        }

	public Integer getNumTarimas() {
		return numTarimas;
	}

	public void setNumTarimas(Integer numTarimas) {
		this.numTarimas = numTarimas;
	}

        public BigDecimal getNoTarimas() {
            return noTarimas;
        }

        public void setNoTarimas(BigDecimal noTarimas) {
            this.noTarimas = noTarimas;
        }

	public List<Tarima> getTarimas() {
		return tarimas;
	}

	public void setTarimas(List<Tarima> tarimas) {
		this.tarimas = tarimas;
	}

	public Tarima getTarima() {
		return tarima;
	}

	public void setTarima(Tarima tarima) {
		this.tarima = tarima;
	}

	public Partida getPartidaEdit() {
		return partidaEdit;
	}

	public void setPartidaEdit(Partida partidaEdit) {
		this.partidaEdit = partidaEdit;
	}

	public List<PrecioServicio> getServiciosCliente() {
		return serviciosCliente;
	}

	public void setServiciosCliente(List<PrecioServicio> serviciosCliente) {
		this.serviciosCliente = serviciosCliente;
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
        
        public boolean isCargaTarimas() {
            return cargaTarimas;
        }

        public void setCargaTarimas(boolean cargaTarimas) {
            this.cargaTarimas = cargaTarimas;
        }

        public int getActiveTabIndex() {
            return activeTabIndex;
        }

        public void setActiveTabIndex(int activeTabIndex) {
            this.activeTabIndex = activeTabIndex;
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

	public PrecioServicio getServicio() {
		return servicio;
	}

	public void setServicio(PrecioServicio servicio) {
		this.servicio = servicio;
	}

	public BigDecimal getCantidadServicio() {
		return cantidadServicio;
	}

	public void setCantidadServicio(BigDecimal cantidadServicio) {
		this.cantidadServicio = cantidadServicio;
	}

	public StreamedContent getFile() {
		return file;
	}

	public void setFile(StreamedContent file) {
		this.file = file;
	}

	public boolean isEditar() {
		return editar;
	}

	public void setEditar(boolean editar) {
		this.editar = editar;
	}
}
