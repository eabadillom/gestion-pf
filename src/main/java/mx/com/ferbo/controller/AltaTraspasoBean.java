package mx.com.ferbo.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.CamaraDAO;
import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.dao.ConstanciaTraspasoDAO;
import mx.com.ferbo.dao.EstadoConstanciaDAO;
import mx.com.ferbo.dao.InventarioDAO;
import mx.com.ferbo.dao.PartidaDAO;
import mx.com.ferbo.dao.PlantaDAO;
import mx.com.ferbo.dao.PosicionCamaraDAO;
import mx.com.ferbo.dao.SerieConstanciaDAO;
import mx.com.ferbo.dao.UnidadDeManejoDAO;
import mx.com.ferbo.model.Camara;
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
import mx.com.ferbo.model.PartidasAfectadas;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.Posicion;
import mx.com.ferbo.model.PrecioServicio;
import mx.com.ferbo.model.Producto;
import mx.com.ferbo.model.ProductoPorCliente;
import mx.com.ferbo.model.SerieConstancia;
import mx.com.ferbo.model.SerieConstanciaPK;
import mx.com.ferbo.model.TraspasoPartida;
import mx.com.ferbo.model.TraspasoServicio;
import mx.com.ferbo.model.UnidadDeManejo;
import mx.com.ferbo.model.UnidadDeProducto;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.InventarioException;
import mx.com.ferbo.util.JasperReportUtil;
import mx.com.ferbo.util.conexion;
import net.sf.jasperreports.engine.JRException;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@Named
@ViewScoped
public class AltaTraspasoBean implements Serializable {

	private static final long serialVersionUID = -3109002730694247052L;
	private static Logger log = LogManager.getLogger(AltaTraspasoBean.class);
	
	@Inject
	private SideBarBean sideBar;

	private List<Cliente> clientes;
	private List<PartidaServicio> alPartidas;
	private List<TraspasoServicio> alServiciosDetalle;
	private List<PrecioServicio> alServicios;
	private List<ProductoPorCliente> alProductosFiltered;
	private List<UnidadDeManejo> alUnidades;
	private List<EstadoConstancia> estados = null;
	private List<ConstanciaDeDeposito> listaconstanciadepo;
	private List<Partida> partida;
	private List<DetallePartida> ldpartida;
	private List<InventarioDetalle> inventario;
	private List<String> listaEntradas;
	private List<InventarioDetalle> destino;
	private List<Planta> listaplanta;
	private List<Planta> listadoPlantas;
	private List<Camara> listacamara;
	private List<Posicion> listaposicion;
	
	private Date fecha;
	private String numero;
	private Integer cantidad;
	private Integer idUnidadManejo;
	private Integer idProducto;
	private BigDecimal peso;
	private BigDecimal valorDeclarado;
	private String observaciones;
	private String unidadcobro;
	private UnidadDeManejo selUnidadManejo;
	private Integer idPrecioServicio;
	private BigDecimal cantidadServicio;
	private Integer idCliente;
	private Integer idclavectedeposito;
	private InventarioDetalle selectedInventario;
	private Integer planta;
	private Integer camara;
	private Cliente selCliente;
	private ConstanciaDeDeposito ctecve;
	private PartidaServicio selPartida;
	private ConstanciaServicioDetalle selServicio;
	
	private UnidadDeManejoDAO udmDAO;
	private EstadoConstanciaDAO edoDAO;
	private ClienteDAO clienteDAO;
	private PartidaDAO partidaDAO;
	private InventarioDAO inventarioDAO;
	private PlantaDAO plantaDAO;
	private CamaraDAO camaraDAO;
	private PosicionCamaraDAO posicionDAO;
	private ConstanciaTraspasoDAO constanciaTDAO;
	private SerieConstanciaDAO serieConstanciaDAO;
	private Planta plantaSelect;
	
	private SerieConstancia serie;
	
	private Usuario usuario;
	private FacesContext context;
	private HttpServletRequest request;
	
	private boolean isSaved = false;
	private boolean habilitareporte = false;
        
        private StreamedContent file;

	public AltaTraspasoBean() {
		log.debug("Entrando al constructor del controller...");
		clienteDAO = new ClienteDAO();
		udmDAO = new UnidadDeManejoDAO();
		edoDAO = new EstadoConstanciaDAO();
		partidaDAO = new PartidaDAO();
		inventarioDAO = new InventarioDAO();
		plantaDAO = new PlantaDAO();
		camaraDAO = new CamaraDAO();
		constanciaTDAO = new ConstanciaTraspasoDAO();
		posicionDAO = new PosicionCamaraDAO();
		serieConstanciaDAO = new SerieConstanciaDAO();
		
		partida = new ArrayList<Partida>();
		ldpartida = new ArrayList<DetallePartida>();
		inventario = new ArrayList<InventarioDetalle>();
		alServiciosDetalle = new ArrayList<TraspasoServicio>();
		alServicios = new ArrayList<PrecioServicio>();
		alUnidades = new ArrayList<UnidadDeManejo>();
		alProductosFiltered = new ArrayList<ProductoPorCliente>();
		listaconstanciadepo = new ArrayList<ConstanciaDeDeposito>();
		destino = new ArrayList<InventarioDetalle>();
		selCliente = new Cliente();
		
		listaplanta = plantaDAO.findall();
		listacamara = camaraDAO.findall();
		listaposicion = posicionDAO.findAll();
	}

	@PostConstruct
	public void init() {
		log.debug("Entrando a Init de alta Traspaso...");
		
		context = FacesContext.getCurrentInstance();
		request = (HttpServletRequest) context.getExternalContext().getRequest();
		usuario = (Usuario) request.getSession(false).getAttribute("usuario");
		log.info("El usuario {} ingresa al alta constancia de traspaso.", this.usuario.getUsuario());
		
		log.debug("Buscando lista de clientes...");
		clientes = sideBar.getListaClientesActivos();
		fecha = new Date();
		
		log.debug("Buscando lista de unidades de medida");
		alUnidades = udmDAO.buscarTodos();
		if (alProductosFiltered == null)
			alProductosFiltered = new ArrayList<ProductoPorCliente>();
		estados = edoDAO.buscarTodos();
		log.debug("Usuario con perfil: {}", usuario.getPerfil());
		if ((usuario.getPerfil() == 1) || (usuario.getPerfil() == 4)) {
			listadoPlantas = new ArrayList<Planta>();
			listadoPlantas.add(plantaDAO.buscarPorId(usuario.getIdPlanta()));
		} else {
			listadoPlantas = plantaDAO.findall(false);
		}
		plantaSelect = listadoPlantas.get(0);

	}

	public void filtrarCliente() {
		String message = null;
		Severity severity = null;
		
		Cliente cliente = null;
		List<Inventario> inventarioList = null;
		Map<Integer, List<PrecioServicio>> mpPrecioServicio = new HashMap<Integer, List<PrecioServicio>>();
		List<PrecioServicio> precioServicioList = null;
		
		try {
			log.debug("Entrando a filtrar cliente...");
			
			this.selCliente = clienteDAO.buscarPorId(idCliente, false);
			inventarioList = inventarioDAO.buscar(selCliente, plantaSelect);
			this.inventario.clear();
			this.generaFolioTraspaso();
			
			for (Inventario i : inventarioList) {
				InventarioDetalle invdet = new InventarioDetalle(i);
				invdet.setListaplanta(listadoPlantas);
				invdet.setListacamara(listacamara);
				invdet.setListaposicion(listaposicion);
				inventario.add(invdet);
			}
			
			listaEntradas = new ArrayList<String>();
			for (InventarioDetalle i : inventario) {
				if (listaEntradas.contains(i.getFolioCliente())) {
					continue;
				}
				listaEntradas.add(i.getFolioCliente());
			}
			log.debug("Lista entradas: {}", listaEntradas);
			
			cliente = clienteDAO.buscarProductosServicios(this.idCliente);
			alProductosFiltered = cliente.getProductoPorClienteList();
			
			precioServicioList = cliente.getPrecioServicioList();
			Integer idAviso = new Integer(-1);
			for (PrecioServicio ps : precioServicioList) {
				/*Código que no consideraba nulos en precios servicio
				Integer avisoCve = ps.getAvisoCve().getAvisoCve();
				if (avisoCve > idAviso)
					idAviso = new Integer(avisoCve);
				
				List<PrecioServicio> list = mpPrecioServicio.get(avisoCve);
				if (list == null) {
					list = new ArrayList<PrecioServicio>();
					mpPrecioServicio.put(avisoCve, list);
				}
				list.add(ps);*/

				// Obtener el aviso (puede ser null por cambios en BD)
				Integer avisoCve = (ps.getAvisoCve() != null) ? ps.getAvisoCve().getAvisoCve() : null;

				// Usamos -1 para agrupar los que no tienen aviso
				Integer avisoKey = (avisoCve != null) ? avisoCve : -1;

				// Solo actualizar idAviso cuando NO es null
				if (avisoCve != null && avisoCve > idAviso) {
					idAviso = avisoCve;
				}

				// Agrupar por aviso
				mpPrecioServicio.computeIfAbsent(avisoKey, k -> new ArrayList<>())
						.add(ps);

			}
			mpPrecioServicio.get(idAviso);
			alServicios.clear();
			alServicios = mpPrecioServicio.get(idAviso);
			for (PrecioServicio ps : alServicios) {
				log.debug(ps.getServicio().getServicioDs());
				log.debug(ps.getUnidad().getUnidadDeManejoDs());
			}
			message = "Agregue los servicios requeridos.";
			severity = FacesMessage.SEVERITY_INFO;
		} catch (Exception ex) {
			log.error("Problema para recuperar los datos del cliente.", ex);
			message = ex.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Cliente", message));
			PrimeFaces.current().ajax().update("form:messages", "form:destino", "form:txtFolio", ":form:traspaso", "form:selServicio");
		}
		
		log.debug("Servicios del cliente filtrados.");
	}
	
	public void generaFolioTraspaso() {
		SerieConstanciaPK seriePK = null;
		SerieConstancia serie = null;
		SerieConstancia serieConstancia = null;
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Folio";

		try {
			if (this.selCliente == null) {
				throw new InventarioException("Debe seleccionar un cliente");
			}

			if (this.plantaSelect == null) {
				throw new InventarioException("Debe seleccionar una planta");
			}
			serieConstancia = new SerieConstancia();
			seriePK = new SerieConstanciaPK();
			seriePK.setCliente(this.selCliente);
			seriePK.setPlanta(plantaSelect);
			seriePK.setTpSerie("T");
			serieConstancia.setSerieConstanciaPK(seriePK);
			serie = serieConstanciaDAO.buscarPorClienteAndPlanta(serieConstancia);

			if (serie == null) {
				this.numero = "";
				throw new InventarioException(
						"No se encontró información de los folios del cliente. Debe indicar manualmente un folio de constancia.");
			}

			this.numero = String.format("%s%s%s%d", serieConstancia.getSerieConstanciaPK().getTpSerie(), plantaSelect.getPlantaSufijo(),
					selCliente.getCodUnico(), serie.getNuSerie());
			
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
		
	public void agrega() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Producto";
		
		try {
			log.debug(this.selectedInventario);
			
			if (this.selectedInventario == null)
				throw new InventarioException("Debe indicar un producto.");

			List<InventarioDetalle> list = destino.stream()
					.filter(t -> t.getPartidaCve() == this.selectedInventario.getPartidaCve())
					.collect(Collectors.toList());

			if (list.size() > 0) {
				throw new InventarioException("El producto ya se encuentra en la lista de traspaso.");
			}

			destino.add(selectedInventario);
			PrimeFaces.current().executeScript("PF('dialogCliente').hide()");
			
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
	
	public void agregarServicio() {
		String message = null;
		Severity severity = null;
		PrecioServicio precioServicio = null;
		TraspasoServicio servicio = null;

		try {
			if (this.idCliente == null || this.idCliente == 0) {
				throw new InventarioException("Debe seleccionar el cliente");
			}

			if (this.cantidadServicio == null || this.cantidadServicio.compareTo(BigDecimal.ZERO) <= 0) {
				throw new InventarioException("Debe indicar la cantidad de servicios.");
			}

			if (this.idPrecioServicio == null) {
				throw new InventarioException("Debe seleccionar un servicio.");
			}

			precioServicio = this.alServicios.stream().filter(ps -> this.idPrecioServicio.equals(ps.getId()))
					.collect(Collectors.toList()).get(0);
			if (alServiciosDetalle == null) {
				alServiciosDetalle = new ArrayList<TraspasoServicio>();
			}

			servicio = new TraspasoServicio();
			servicio.setCantidad(this.cantidadServicio);
			servicio.setServicio(precioServicio.getServicio().getServicioDs());
			servicio.setPrecio(precioServicio.getPrecio());
			servicio.setSubtotal(this.cantidadServicio.multiply(precioServicio.getPrecio()));
			alServiciosDetalle.add(servicio);
			message = "Servicio agregado correctamente.";
			severity = FacesMessage.SEVERITY_INFO;
		} catch (InventarioException ex) {
			log.error("Problema para obtener la información de los servicios...", ex);
			message = ex.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
		} catch (Exception ex) {
			log.error("Problema para obtener el listado de servicios del cliente.", ex);
			message = "Problema con la información de servicios.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Agregar servicio", message));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-altaTraspaso");
		}
	}
	
	public void plantaselect() {
		Planta plantaDestino = this.selectedInventario.getPlantaDestino();
		List<Camara> listaCamarasDestino = camaraDAO.buscarPorPlanta(plantaDestino);
		selectedInventario.setListacamara(listaCamarasDestino);
	}

	public void camaraselect() {
		Camara camD = this.selectedInventario.getCamaraDestino();
		List<Posicion> listaposicionDestino = posicionDAO.buscarPorCamara(camD);
		selectedInventario.setListaposicion(listaposicionDestino);
	}

	public synchronized void guardar() {
		String message = null;
		Severity severity = null;
		ConstanciaTraspaso constancia = null;
		List<ConstanciaTraspaso> alConstancias = null;
		
		try {
			if (this.isSaved) {
				throw new InventarioException("La constancia ya se encuentra registrada.");
			}

			if (this.numero == null || "".equalsIgnoreCase(this.numero.trim())) {
				throw new InventarioException("Debe indicar el folio de la constancia.");
			}

			if (this.destino == null || this.destino.size() == 0) {
				throw new InventarioException("Debe traspasar almenos un producto");
			}

			alConstancias = constanciaTDAO.buscarporNumero(this.numero);

			if (alConstancias != null && alConstancias.size() > 0) {
				throw new InventarioException(String.format("El folio %s ya se encuentra registrado.", this.numero));
			}

			constancia = new ConstanciaTraspaso();
			constancia.setFecha(this.fecha);
			
			constancia.setNumero(this.numero);
			constancia.setCliente(this.selCliente);
			constancia.setObservacion(this.observaciones);
			constancia.setNombreCliente(this.selCliente.getNombre());
			constancia.setFechaCadena(DateUtil.getString(this.fecha, DateUtil.FORMATO_FECHA_CADENA));
			List<TraspasoPartida> listaTraspasoPartida = new ArrayList<TraspasoPartida>();
			
			for(InventarioDetalle i : destino ) {
				log.debug("InventarioDetalle: {}", i);
				
				Partida partida = partidaDAO.buscarPorId(i.getPartidaCve());
				TraspasoPartida traspasoPartida = new TraspasoPartida();
				UnidadDeProducto udp = partida.getUnidadDeProductoCve();
				Producto pr = udp.getProductoCve();
				
				partida.setCamaraCve(i.getCamaraDestino());
				
				traspasoPartida.setTraspaso(constancia);
				traspasoPartida.setConstancia(i.getFolioCliente());
				traspasoPartida.setPartida(partida);
				traspasoPartida.setDescripcion(pr.getProductoDs());
				traspasoPartida.setCantidad(i.getCantidad());
				String sOrigen = String.format("%s - %s", i.getPlanta().getPlantaDs(), i.getCamara().getCamaraDs());
				traspasoPartida.setOrigen(sOrigen);
				String sDestino = String.format("%s - %s", i.getPlantaDestino().getPlantaDs(), i.getCamaraDestino().getCamaraDs());
				traspasoPartida.setDestino(sDestino);
				
				PartidasAfectadas pa = new PartidasAfectadas();
				pa.setPartidatraspaso(traspasoPartida);
				pa.setPartida(partida);
				
				traspasoPartida.setPartidasAfectadas(pa);
				
				listaTraspasoPartida.add(traspasoPartida);
				
				partidaDAO.actualizar(partida);
			}
			for (TraspasoServicio servicio : alServiciosDetalle) {
				servicio.setTraspaso(constancia);
			}
			constancia.setTraspasoServicioList(alServiciosDetalle);
			constancia.setTraspasoPartidaList(listaTraspasoPartida);
			constanciaTDAO.guardar(constancia);
			
			Integer numeroSerie = this.serie.getNuSerie() + 1;
			this.serie.setNuSerie(numeroSerie);
			serieConstanciaDAO.actualizar(this.serie);

			this.isSaved = true;
			this.habilitareporte = true;
			message = String.format("Constancia guardada correctamente con el folio %s", this.numero);
			severity = FacesMessage.SEVERITY_INFO;

		} catch (InventarioException ex) {
			log.error("Problema para obtener la información de los productos...", ex);
			message = ex.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
		} catch (Exception ex) {
			log.error("Problema para obtener el listado de servicios del cliente.", ex);
			ex.printStackTrace();
			message = "Problema con la información de servicios.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Agregar servicio", message));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-altaTraspaso");
		}
	}

	public void jasper() throws JRException, IOException, SQLException {
		String message = null;
		Severity severity = null;
		
		String jasperPath = "/jasper/ReporteTraspaso.jrxml";
		String filename = "Constancia_de_traspaso.pdf";
		String images = "/images/logo.jpeg";
		File reportFile = new File(jasperPath);
		File imgfile = null;
		JasperReportUtil jasperReportUtil = new JasperReportUtil();
		Map<String, Object> parameters = null;
		Connection connection = null;
		try {
			if (habilitareporte == false) {
				throw new InventarioException("Favor de guardar la constancia");
			}

			URL resource = getClass().getResource(jasperPath);
			URL resourceimg = getClass().getResource(images);
			String file = resource.getFile();
			String img = resourceimg.getFile();
			reportFile = new File(file);
			imgfile = new File(img);
			log.debug(reportFile.getPath());
			
			connection = EntityManagerUtil.getConnection();
			parameters = new HashMap<String, Object>();
			
			parameters.put("REPORT_CONNECTION", connection);
			parameters.put("FOLIO", this.numero);
			parameters.put("LogoPath", imgfile.getPath());
			log.debug("Parametros: " + parameters.toString());
			
			byte[] bytes = jasperReportUtil.createPDF(parameters, reportFile.getPath());
			InputStream input = new ByteArrayInputStream(bytes);
			this.file = DefaultStreamedContent.builder().contentType("application/pdf").name(filename).stream(() -> input).build();
			log.info("Ticket de traspaso generado {}...", filename);
		} catch(InventarioException ex) {
			message = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Error en impresion", message));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-constanciaServicios");
		} catch (Exception ex) {
			log.error("Problema general...", ex);
			message = String.format("No se pudo imprimir el folio %s", this.numero);
			severity = FacesMessage.SEVERITY_ERROR;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Error en impresion", message));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-constanciaServicios");
		} finally {
			conexion.close((Connection) connection);
		}
	}

	public void reload() throws IOException {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getUnidadcobro() {
		return unidadcobro;
	}

	public void setUnidadcobro(String unidadcobro) {
		this.unidadcobro = unidadcobro;
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

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public BigDecimal getValorDeclarado() {
		return valorDeclarado;
	}

	public void setValorDeclarado(BigDecimal valorDeclarado) {
		this.valorDeclarado = valorDeclarado;
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

	public List<UnidadDeManejo> getAlUnidades() {
		return alUnidades;
	}

	public void setAlUnidades(List<UnidadDeManejo> alUnidades) {
		this.alUnidades = alUnidades;
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

	public UnidadDeManejo getSelUnidadManejo() {
		return selUnidadManejo;
	}

	public void setSelUnidadManejo(UnidadDeManejo selUnidadManejo) {
		this.selUnidadManejo = selUnidadManejo;
	}

	public Integer getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Integer idCliente) {
		this.idCliente = idCliente;
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

	public List<PrecioServicio> getAlServicios() {
		return alServicios;
	}

	public void setAlServicios(List<PrecioServicio> alServicios) {
		this.alServicios = alServicios;
	}

	public Integer getIdPrecioServicio() {
		return idPrecioServicio;
	}

	public void setIdPrecioServicio(Integer idPrecioServicio) {
		this.idPrecioServicio = idPrecioServicio;
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

	public ConstanciaDeDeposito getCtecve() {
		return ctecve;
	}

	public void setCtecve(ConstanciaDeDeposito ctecve) {
		this.ctecve = ctecve;
	}

	public Integer getIdclavectedeposito() {
		return idclavectedeposito;
	}

	public void setIdclavectedeposito(Integer idclavectedeposito) {
		this.idclavectedeposito = idclavectedeposito;
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

	public List<Planta> getListaplanta() {
		return listaplanta;
	}

	public void setListaplanta(List<Planta> listaplanta) {
		this.listaplanta = listaplanta;
	}

	public List<Camara> getListacamara() {
		return listacamara;
	}

	public void setListacamara(List<Camara> listacamara) {
		this.listacamara = listacamara;
	}

	public List<Posicion> getListaposicion() {
		return listaposicion;
	}

	public void setListaposicion(List<Posicion> listaposicion) {
		this.listaposicion = listaposicion;
	}

	public InventarioDetalle getSelectedInventario() {
		return selectedInventario;
	}

	public void setSelectedInventario(InventarioDetalle selectedInventario) {
		this.selectedInventario = selectedInventario;
	}

	public Integer getPlanta() {
		return planta;
	}

	public void setPlanta(Integer planta) {
		this.planta = planta;
	}

	public Integer getCamara() {
		return camara;
	}

	public void setCamara(Integer camara) {
		this.camara = camara;
	}

	public List<InventarioDetalle> getDestino() {
		return destino;
	}

	public void setDestino(List<InventarioDetalle> destino) {
		this.destino = destino;
	}

	public Planta getPlantaSelect() {
		return plantaSelect;
	}

	public void setPlantaSelect(Planta plantaSelect) {
		this.plantaSelect = plantaSelect;
	}

	public List<Planta> getListadoPlantas() {
		return listadoPlantas;
	}

	public void setListadoPlantas(List<Planta> listadoPlantas) {
		this.listadoPlantas = listadoPlantas;
	}

	public List<String> getListaEntradas() {
		return listaEntradas;
	}

	public void setListaEntradas(List<String> listaEntradas) {
		this.listaEntradas = listaEntradas;
	}

        public StreamedContent getFile() {
            return file;
        }

        public void setFile(StreamedContent file) {
            this.file = file;
        }

}