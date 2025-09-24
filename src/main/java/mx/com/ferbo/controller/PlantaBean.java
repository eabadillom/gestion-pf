package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

import mx.com.ferbo.dao.AsentamientoHumanoDAO;
import mx.com.ferbo.dao.CiudadesDAO;
import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.dao.EmisoresCFDISDAO;
import mx.com.ferbo.dao.EstadosDAO;
import mx.com.ferbo.dao.MunicipiosDAO;
import mx.com.ferbo.dao.PaisesDAO;
import mx.com.ferbo.dao.PlantaDAO;
import mx.com.ferbo.dao.SerieFacturaDAO;
import mx.com.ferbo.dao.TipoAsentamientoDAO;
import mx.com.ferbo.model.AsentamientoHumano;
import mx.com.ferbo.model.AsentamientoHumanoPK;
import mx.com.ferbo.model.Ciudades;
import mx.com.ferbo.model.CiudadesPK;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.EmisoresCFDIS;
import mx.com.ferbo.model.Estados;
import mx.com.ferbo.model.EstadosPK;
import mx.com.ferbo.model.Municipios;
import mx.com.ferbo.model.MunicipiosPK;
import mx.com.ferbo.model.Paises;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.SerieConstancia;
import mx.com.ferbo.model.SerieConstanciaPK;
import mx.com.ferbo.model.SerieFactura;
import mx.com.ferbo.model.TipoAsentamiento;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.util.InventarioException;

@Named
@ViewScoped
public class PlantaBean implements Serializable {
	private static Logger log = LogManager.getLogger(PlantaBean.class);
	private static final long serialVersionUID = 1L;
	private PlantaDAO daoPlanta;
	private PaisesDAO daoPaises;
	private EstadosDAO daoEstados;
	private MunicipiosDAO daoMunicipios;
	private CiudadesDAO ciudadesDao;
	private TipoAsentamiento tipoAsentamientoSelect; 
	private TipoAsentamientoDAO tipoAsentamientoDao;
	private AsentamientoHumanoDAO asentamientoHumanoDao;
	private ClienteDAO clienteDAO;
	private SerieFacturaDAO sfDAO;
	private EmisoresCFDISDAO emisorDAO;

	private List<Planta> list;
	private List<Usuario> usuarios;
	private List<Paises> listaPaises;
	private List<Estados> estadosList;
	private List<Municipios> listaMunicipios;
	private List<Ciudades> listaCiudades;
	private List<TipoAsentamiento> listaTipoAsentamiento;
	private List<AsentamientoHumano> asentamientoHumanoList;
	private List<EmisoresCFDIS> listaEmisores;
	private List<SerieFactura> listaSerieFactura;

	private Paises paisSelect;
	private EstadosPK estadoPkSelect;
	private Estados estadoSelect;
	private MunicipiosPK municipioPkSelect;
	private Municipios municipioSelect;
	private CiudadesPK ciudadPKSelect;
	private Ciudades ciudadSelect;
	private AsentamientoHumanoPK asentamientoHumanoPKSelect;
	private AsentamientoHumano asentamientoHumanoSelect;
	
	private EmisoresCFDIS idEmisoresCFDIS;
	private int idTipoAsentamiento;
	private String PlantaDs;
	private String PlantaAbrev;
	private String PlantaSufijo;
	private int IdUsuario;
	private String tipoAsentamientoSelected;
	private String codigopostalSelected;
	private Planta planta;
	private Planta seleccion;
	private String uuid;
	
	private Usuario usuario;
	
	private FacesContext faceContext;
    private HttpServletRequest httpServletRequest;

	public PlantaBean() {
		
		daoPlanta = new PlantaDAO();
		daoPaises = new PaisesDAO();
		daoEstados = new EstadosDAO();
		daoMunicipios = new MunicipiosDAO();
		ciudadesDao = new CiudadesDAO();
		tipoAsentamientoDao = new TipoAsentamientoDAO();
		asentamientoHumanoDao = new AsentamientoHumanoDAO();
		clienteDAO = new ClienteDAO();
		sfDAO = new SerieFacturaDAO();
		emisorDAO = new EmisoresCFDISDAO();
		
		listaEmisores = new ArrayList<EmisoresCFDIS>();
		listaMunicipios = new ArrayList<>();
		listaCiudades = new ArrayList<>();
		asentamientoHumanoList = new ArrayList<>();
		
		planta = new Planta();
		seleccion = new Planta();
	}
	
	@PostConstruct
	public synchronized void init() {
		this.faceContext = FacesContext.getCurrentInstance();
        this.httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        this.usuario = (Usuario) httpServletRequest.getSession(true).getAttribute("usuario");
        
        log.info("El usuario {} ingresa al catálogo de plantas (sucursales).", this.usuario.getUsuario());
		
		list = daoPlanta.findall(false);
		listaEmisores = emisorDAO.buscarTodos(true);
		usuarios = daoPlanta.getUsuarios();
		listaTipoAsentamiento = tipoAsentamientoDao.buscarTodos();
		asentamientoHumanoList = new ArrayList<>();
		this.paisSelect = new Paises();
		this.estadoSelect = new Estados();
		this.estadoPkSelect = new EstadosPK();
		this.municipioSelect = new Municipios();
		this.municipioPkSelect = new MunicipiosPK();
		this.ciudadPKSelect = new CiudadesPK();
		this.ciudadSelect = new Ciudades();
		this.asentamientoHumanoPKSelect = new AsentamientoHumanoPK();
		this.asentamientoHumanoSelect = new AsentamientoHumano();
		this.tipoAsentamientoSelect = new TipoAsentamiento();
		this.idEmisoresCFDIS = new EmisoresCFDIS();
	}
	
	public void openNew() {
		this.planta = new Planta();
		
		this.listaPaises = daoPaises.buscarTodos();
		if(this.planta.getPlantaCve() == null)
			paisSelect = daoPaises.buscarPorId(150);
		this.estadoSelect = new Estados();
		this.filtroEstado();
		this.municipioSelect = new Municipios();
		this.listaMunicipios = null;
		this.ciudadSelect = new Ciudades();
		this.listaCiudades = null;
		this.asentamientoHumanoSelect = new AsentamientoHumano();
		this.asentamientoHumanoList = null;
		this.tipoAsentamientoSelected = "";
		this.codigopostalSelected = "";
	}
	
	public void datosPlanta(Planta planta) {
		this.planta = daoPlanta.buscarPorId(planta.getPlantaCve(), true);
		cargaSeries();
		filtroPais();
		cargaPais();
		//Carga de datos de estado planta
		filtroEstado();
		cargaEstado();
		//Carga de datos de municipio planta
		filtroMunicipio();
		cargaMunicipio();
		//Carga de datos de ciudades planta
		filtroCiudad();
		cargaCiudad();
		//Carga de datos de asentamiento planta
		filtroAsentamiento();
		cargaAsentamiento();
		//Carga tipo de asentamiento
		tipoAsentamiento();
		//Carga domicilio de la planta
		cargaDireccion();
		
		PrimeFaces.current().ajax().update("form:state","form:messages","form:Municipality","form:city","form:asentamiento","form:codigopostal-new","form:Tasn-new");
	}
	
	public void cargaSeries() {
		if(this.planta == null) {
			log.warn("No hay una planta seleccionada");
			return;
		}
		if(this.idEmisoresCFDIS == null) {
			log.warn("No hay un emisor seleccionado");
			return;
		}
		
		log.info("Emisor seleccionado: {}", this.planta.getIdEmisoresCFDIS());
		
		this.listaSerieFactura = sfDAO.buscarPorEmisor(this.planta.getIdEmisoresCFDIS());
		log.info("Emisor: {}, Total de series de facturas: {}", this.planta.getIdEmisoresCFDIS(), this.listaSerieFactura.size());
	}
	
	public void filtroPais() {
		if(this.planta ==null) {
			return;
		}
		
		this.listaPaises = daoPaises.buscarTodos();
		if(this.planta.getPlantaCve() == null)
			paisSelect = daoPaises.buscarPorId(150);
	}
	
	public void cargaPais() {
		if(this.planta == null)
			return;
		
		if(this.planta.getIdPais() == null)
			return;
		
		this.paisSelect = daoPaises.buscarPorId(this.planta.getIdPais());
	}
	
	public void filtroEstado() {
		if(this.planta == null) {
			log.warn("No hay una planta seleccionada.");
			return;
		}
		
		if(this.paisSelect == null) {
			log.warn("No hay un pais seleccionado.");
			return;
		}
		
		log.info("Pais seleccionado: {}", this.paisSelect);
		
		estadosList = daoEstados.buscarPorPais(paisSelect);
	}
	
	public void cargaEstado() {
		if(this.planta == null)
			return;
		
		if(this.planta.getPlantaCve() == null)
			return;
		
		if(this.paisSelect == null)
			return;
		
		this.estadoSelect = daoEstados.buscarPorId(this.planta.getIdPais(), this.planta.getIdEstado());
	}
	
	public void filtroMunicipio() {
		EstadosPK estadosPK = null;
		
		if(this.planta == null) {
			log.warn("No hay un pais seleccionado.");
			return;
		}
		
		if(this.paisSelect == null) {
			log.warn("No hay un pais seleccionado.");
			return;
		}
		
		if(this.estadoSelect == null) {
			log.warn("No hay un estado seleccionado.");
			return;
		}
		
		estadosPK = this.estadoSelect.getEstadosPK();
		
		log.info("Buscando información de municipios: idPais {}, idEstado {}",
				estadosPK.getPaisCve() , estadosPK.getEstadoCve());
		listaMunicipios = daoMunicipios.buscarPorPaisEstado(estadosPK.getPaisCve() , estadosPK.getEstadoCve());
	}
	
	public void cargaMunicipio() {
		if(this.planta == null)
			return;
		if(this.planta.getPlantaCve() == null)
			return;
		if(this.paisSelect == null)
			return;
		if(this.estadoSelect == null)
			return;
		
		this.municipioSelect = daoMunicipios.buscarPorId(
				new MunicipiosPK(this.planta.getIdPais(), this.planta.getIdEstado(), this.planta.getIdMunicipio()));
	}
	
	
	public void filtroCiudad() {
		MunicipiosPK municipioPK = this.municipioSelect.getMunicipiosPK();
		
		if(this.planta == null)
			return;
		
		if(this.paisSelect == null)
			return;
		
		if(this.estadoSelect == null)
			return;
		
		if(this.municipioSelect == null)
			return;
		
		listaCiudades = ciudadesDao.buscarPorPaisEstadoMunicipio(municipioPK.getPaisCve(), municipioPK.getEstadoCve(), municipioPK.getMunicipioCve());
	}
	
	public void cargaCiudad() {
		if(this.planta == null)
			return;
		if(this.planta.getPlantaCve() == null)
			return;
		if(this.paisSelect == null)
			return;
		if(this.estadoSelect == null)
			return;
		if(this.municipioSelect == null)
			return;
		
		this.ciudadSelect = ciudadesDao.buscarPorId(
				new CiudadesPK(this.planta.getIdPais(), this.planta.getIdEstado(), this.planta.getIdMunicipio(), this.planta.getIdCiudad()));
	}
	
	public void filtroAsentamiento() {
		CiudadesPK ciudadPK = this.ciudadSelect.getCiudadesPK();
		if(this.planta == null)
			return;
		
		if(this.paisSelect == null)
			return;
		
		if(this.estadoSelect == null)
			return;
		
		if(this.municipioSelect == null)
			return;
		
		if(this.ciudadSelect == null)
			return;
		
		this.asentamientoHumanoList = asentamientoHumanoDao.buscarPorPaisEstadoMunicipioCiudad(ciudadPK.getPaisCve(), ciudadPK.getEstadoCve(), ciudadPK.getMunicipioCve(), ciudadPK.getCiudadCve());
	}
	
	public void cargaAsentamiento() {
		if(this.planta == null)
			return;
		if(this.planta.getPlantaCve() == null)
			return;
		if(this.paisSelect == null)
			return;
		if(this.estadoSelect == null)
			return;
		if(this.municipioSelect == null)
			return;
		if(this.ciudadSelect == null)
			return;
		this.asentamientoHumanoSelect = this.asentamientoHumanoList.stream()
				.filter(a -> a.getAsentamientoHumanoPK().getAsentamientoCve() == this.planta.getIdAsentamiento())
				.collect(Collectors.toList())
				.get(0)
				;
	}
	
	
	public void tipoAsentamiento() {
		
		tipoAsentamientoSelected = "";
		idTipoAsentamiento = 0;
		codigopostalSelected = null;
		AsentamientoHumano ah = new AsentamientoHumano();
		if(planta.getIdAsentamiento()!=null||paisSelect!=null) {
			this.asentamientoHumanoPKSelect.getCiudades().getMunicipios().getEstados().getPaises().setPaisCve(paisSelect.getPaisCve());
			this.asentamientoHumanoPKSelect.getCiudades().getMunicipios().getEstados().getEstadosPK().setEstadoCve(estadoSelect.getEstadosPK().getEstadoCve());
			this.asentamientoHumanoPKSelect.getCiudades().getMunicipios().getMunicipiosPK().setMunicipioCve(municipioSelect.getMunicipiosPK().getMunicipioCve());
			this.asentamientoHumanoPKSelect.getCiudades().getCiudadesPK().setCiudadCve(ciudadSelect.getCiudadesPK().getCiudadCve());
			this.asentamientoHumanoPKSelect.setAsentamientoCve(asentamientoHumanoSelect.getAsentamientoHumanoPK().getAsentamientoCve());
			//asentamientoHumanoSelect = new AsentamientoHumano();
			this.asentamientoHumanoSelect.setAsentamientoHumanoPK(asentamientoHumanoPKSelect);
			asentamientoHumanoPKSelect = new AsentamientoHumanoPK();
		  ah = asentamientoHumanoDao.buscar(asentamientoHumanoSelect);
		  if(ah!=null) {
				this.tipoAsentamientoSelected = ah.getTipoAsentamiento().getTipoasntmntoDs();
				this.idTipoAsentamiento = ah.getTipoAsentamiento().getTipoasntmntoCve();
				this.codigopostalSelected = ah.getCp();
		  } else {
			  FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "No tiene asentamiento humano existente"));
		  }
		
		 PrimeFaces.current().ajax().update("form:messages");
			
		}
	}
	
	public void cargaDireccion() {
		AsentamientoHumanoPK aPK = null;
		
		if(this.planta == null)
			return;
		
		if(this.planta.getPlantaCve() == null)
			return;
		
		aPK = new AsentamientoHumanoPK();
		aPK.getCiudades().getMunicipios().getEstados().getPaises().setPaisCve(this.planta.getIdPais());
		aPK.getCiudades().getMunicipios().getEstados().getEstadosPK().setEstadoCve(this.planta.getIdEstado());
		aPK.getCiudades().getMunicipios().getMunicipiosPK().setMunicipioCve(this.planta.getIdMunicipio());
		aPK.getCiudades().getCiudadesPK().setCiudadCve(this.planta.getIdCiudad());
		aPK.setAsentamientoCve(this.planta.getIdAsentamiento());
		
		paisSelect = daoPaises.buscarPorId(aPK.getCiudades().getMunicipios().getEstados().getPaises().getPaisCve());
		log.info("Pais: {}", this.paisSelect.getPaisCve());
		
		estadoSelect = daoEstados.buscarPorId(aPK.getCiudades().getMunicipios().getEstados().getPaises().getPaisCve(), aPK.getCiudades().getMunicipios().getEstados().getEstadosPK().getEstadoCve());
		log.info("Estado: {}", this.estadoSelect.getEstadosPK());
		
		
		MunicipiosPK municipioPK = new MunicipiosPK(aPK.getCiudades().getMunicipios().getEstados().getPaises().getPaisCve(), aPK.getCiudades().getMunicipios().getEstados().getEstadosPK().getEstadoCve(), aPK.getCiudades().getMunicipios().getMunicipiosPK().getMunicipioCve());
		this.municipioSelect = daoMunicipios.buscarPorId(municipioPK);
		log.info("Municipio: {}", this.municipioSelect.getMunicipiosPK());
		
	}
	
	public void handleUsers () {
		
		if(this.IdUsuario != 1)
		this.setPlantaDs(PlantaDs);
		this.setPlantaAbrev(PlantaAbrev);
		this.setPlantaSufijo(PlantaSufijo);		
		this.setIdUsuario(IdUsuario);
		this.paisSelect = new Paises();
		this.estadoSelect = new Estados();
		this.estadoPkSelect = new EstadosPK();
		this.municipioSelect = new Municipios();
		this.municipioPkSelect = new MunicipiosPK();
		this.ciudadPKSelect = new CiudadesPK();
		this.ciudadSelect = new Ciudades();
		this.asentamientoHumanoPKSelect = new AsentamientoHumanoPK();
		this.asentamientoHumanoSelect = new AsentamientoHumano();
		this.tipoAsentamientoSelect = new TipoAsentamiento();
	}
	
	public void update() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Guardar planta";
		
		String result = null;
		Integer idPais = null;
		Integer idEstado = null;
		Integer idMunicipio = null;
		Integer idCiudad = null;
		Integer idAsentamiento = null;
		
		try {
			if(this.planta.getPlantaDs() == null || "".equalsIgnoreCase(this.planta.getPlantaDs().trim()))
				throw new InventarioException("Debe indicar el nombre de la planta");
			
			if(this.planta.getPlantaAbrev() == null || "".equalsIgnoreCase(this.planta.getPlantaAbrev().trim()))
				throw new InventarioException("Debe indicar el número de la planta");
			
			if(this.planta.getPlantaSufijo() == null || "".equalsIgnoreCase(this.planta.getPlantaSufijo().trim()))
				throw new InventarioException("Debe indicar el sufijo (código) de la planta");
			
			if(this.planta.getIdUsuario() == null)
				throw new InventarioException("Debe indicar un usuario responsable de la planta");
			
			if(this.planta.getIdEmisoresCFDIS() == null)
				throw new InventarioException("Debe indicar un emisor de CFDI para la planta");
			
			if(this.paisSelect == null)
				throw new InventarioException("Debe seleccionar un país");
			
			if(this.estadoSelect == null)
				throw new InventarioException("Debe seleccionar un estado");
			
			if(this.municipioSelect == null)
				throw new InventarioException("Debe seleccionar un municipio");
			
			if(this.ciudadSelect == null)
				throw new InventarioException("Debe seleccionar una ciudad");
			
			if(this.asentamientoHumanoSelect == null)
				throw new InventarioException("Debe seleccionar un asentamiento (colonia)");
			
			if(this.planta.getCalle() == null || "".equalsIgnoreCase(this.planta.getCalle().trim()))
				throw new InventarioException("Debe indicar la calle del domicilio de la planta");
			
			if(this.planta.getNumexterior() == null || "".equalsIgnoreCase(this.planta.getNumexterior().trim()))
				throw new InventarioException("Debe indicar el número exterior del domicilio de la planta.");
			
			idPais = paisSelect.getPaisCve();
			idEstado = estadoSelect.getEstadosPK().getEstadoCve();
			idMunicipio = municipioSelect.getMunicipiosPK().getMunicipioCve();
			idCiudad = ciudadSelect.getCiudadesPK().getCiudadCve();
			idAsentamiento = asentamientoHumanoSelect.getAsentamientoHumanoPK().getAsentamientoCve();
			
			planta.setIdPais(idPais);
			planta.setIdEstado(idEstado);
			planta.setIdMunicipio(idMunicipio);
			planta.setIdCiudad(idCiudad);
			planta.setIdAsentamiento(idAsentamiento);
			planta.setTipoasentamiento(idTipoAsentamiento);
			planta.setCodigopostal(codigopostalSelected);
			planta.setIdEmisoresCFDIS(planta.getIdEmisoresCFDIS());
			
			if(planta.getPlantaCve() == null) {
				log.info("Guardando información de la planta");
				this.processSerieConstancia(planta);
				result = daoPlanta.save(this.planta);
				if(result != null)
					throw new InventarioException("Ocurrió un problema al guardar la planta.");
				list = daoPlanta.findall(false);
				log.info("El usuario {} ha registrado la planta {}.", this.usuario.getUsuario(), this.planta);
				mensaje = "La planta se registró correctamente";
				
			} else {
				result = daoPlanta.update(planta);
				if(result != null)
					throw new InventarioException("Ocurrió un problema al guardar la planta.");
				log.info("El usuario {} ha actualizado la planta {}.", this.usuario.getUsuario(), this.planta);
				list = daoPlanta.findall(false);
				mensaje = "La planta se actualizó correctamente";
			}
			
			this.planta = new Planta();
			this.paisSelect = new Paises();
			this.estadoSelect = new Estados();
			this.municipioSelect = new Municipios();
			this.ciudadSelect = new Ciudades();
			this.tipoAsentamientoSelected = "";
			this.codigopostalSelected = "";
			
			PrimeFaces.current().executeScript("PF('dg-agrega').hide()");
			severity = FacesMessage.SEVERITY_INFO;
		} catch (InventarioException ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
		} catch (Exception ex) {
			log.error("Problema para registrar información de la planta...", ex);
		} finally {
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages", "form:dt-planta");
		}
	}
	
	public void processSerieConstancia(Planta planta) {
		List<Cliente> clienteList = null;
		
		try {
			clienteList = clienteDAO.buscarTodos(true);
			
			for(Cliente cliente : clienteList) {
				SerieConstanciaPK serieConstanciaPK_I = new SerieConstanciaPK();
				serieConstanciaPK_I.setCliente(cliente);
				serieConstanciaPK_I.setPlanta(planta);
				serieConstanciaPK_I.setTpSerie("I");
				SerieConstancia serieConstanciaI = new SerieConstancia();
				serieConstanciaI.setSerieConstanciaPK(serieConstanciaPK_I);
				serieConstanciaI.setNuSerie(1);
				cliente.addSerieConstancia(serieConstanciaI);
				planta.add(serieConstanciaI);
				
				SerieConstanciaPK serieConstanciaPK_O = new SerieConstanciaPK();
				serieConstanciaPK_O.setCliente(cliente);
				serieConstanciaPK_O.setPlanta(planta);
				serieConstanciaPK_O.setTpSerie("O");
				SerieConstancia serieConstanciaO = new SerieConstancia();
				serieConstanciaO.setSerieConstanciaPK(serieConstanciaPK_O);
				serieConstanciaO.setNuSerie(1);
				cliente.addSerieConstancia(serieConstanciaO);
				planta.add(serieConstanciaO);
				
				SerieConstanciaPK serieConstanciaPK_T = new SerieConstanciaPK();
				serieConstanciaPK_T.setCliente(cliente);
				serieConstanciaPK_T.setPlanta(planta);
				serieConstanciaPK_T.setTpSerie("T");
				SerieConstancia serieConstanciaT = new SerieConstancia();
				serieConstanciaT.setSerieConstanciaPK(serieConstanciaPK_T);
				serieConstanciaT.setNuSerie(1);
				cliente.addSerieConstancia(serieConstanciaT);
				planta.add(serieConstanciaT);
				
				SerieConstanciaPK serieConstanciaPK_S = new SerieConstanciaPK();
				serieConstanciaPK_S.setCliente(cliente);
				serieConstanciaPK_S.setPlanta(planta);
				serieConstanciaPK_S.setTpSerie("S");
				SerieConstancia serieConstanciaS = new SerieConstancia();
				serieConstanciaS.setSerieConstanciaPK(serieConstanciaPK_S);
				serieConstanciaS.setNuSerie(1);
				cliente.addSerieConstancia(serieConstanciaS);
				planta.add(serieConstanciaS);
			}
			
			
		} catch(Exception ex) {
			
		} finally {
			
		}
	}

	public void delete() {
		PrimeFaces.current().executeScript("PF('dg-delete').hide()");
		
		String message = daoPlanta.delete(planta);
	
			if (message == null) {
				
				list.remove(this.planta);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Planta Eliminado " + planta.getPlantaDs(), null));
				PrimeFaces.current().ajax().update("form:messages", "form:dt-planta");
			}else{
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al eliminar " + planta.getPlantaDs(), message));
				PrimeFaces.current().ajax().update("form:messages");
			}
	}
	
	public void muestraplanta(){
		System.out.println("Planta" + planta);
		System.out.println("Planta" + seleccion);
		
		
	}
	public void limpiaPlanta() {
		this.planta = new Planta();
	}
	
	
	
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getCodigopostalSelected() {
		return codigopostalSelected;
	}

	public void setCodigopostalSelected(String codigopostalSelected) {
		this.codigopostalSelected = codigopostalSelected;
	}

	public String getTipoAsentamientoSelected() {
		return tipoAsentamientoSelected;
	}

	public void setTipoAsentamientoSelected(String tipoAsentamientoSelected) {
		this.tipoAsentamientoSelected = tipoAsentamientoSelected;
	}

	public List<AsentamientoHumano> getAsentamientoHumanoList() {
		return asentamientoHumanoList;
	}

	public void setAsentamientoHumanoList(List<AsentamientoHumano> asentamientoHumanoList) {
		this.asentamientoHumanoList = asentamientoHumanoList;
	}

	public String getPlantaDs() {
		return PlantaDs;
	}

	public void setPlantaDs(String plantaDs) {
		PlantaDs = plantaDs;
	}

	public String getPlantaAbrev() {
		return PlantaAbrev;
	}

	public void setPlantaAbrev(String plantaAbrev) {
		PlantaAbrev = plantaAbrev;
	}

	public String getPlantaSufijo() {
		return PlantaSufijo;
	}

	public void setPlantaSufijo(String plantaSufijo) {
		PlantaSufijo = plantaSufijo;
	}

	public int getIdUsuario() {
		return IdUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		IdUsuario = idUsuario;
	}

	public TipoAsentamiento getTipoAsentamientoSelect() {
		return tipoAsentamientoSelect;
	}

	public void setTipoAsentamientoSelect(TipoAsentamiento tipoAsentamientoSelect) {
		this.tipoAsentamientoSelect = tipoAsentamientoSelect;
	}

	public List<TipoAsentamiento> getListaTipoAsentamiento() {
		return listaTipoAsentamiento;
	}

	public void setListaTipoAsentamiento(List<TipoAsentamiento> listaTipoAsentamiento) {
		this.listaTipoAsentamiento = listaTipoAsentamiento;
	}

	public MunicipiosPK getMunicipioPkSelect() {
		return municipioPkSelect;
	}

	public void setMunicipioPkSelect(MunicipiosPK municipioPkSelect) {
		this.municipioPkSelect = municipioPkSelect;
	}

	public Municipios getMunicipioSelect() {
		return municipioSelect;
	}

	public void setMunicipioSelect(Municipios municipioSelect) {
		this.municipioSelect = municipioSelect;
	}

	public Paises getPaisSelect() {
		return paisSelect;
	}

	public void setPaisSelect(Paises paisSelect) {
		this.paisSelect = paisSelect;
	}

	public EstadosPK getEstadoPkSelect() {
		return estadoPkSelect;
	}

	public void setEstadoPkSelect(EstadosPK estadoPkSelect) {
		this.estadoPkSelect = estadoPkSelect;
	}

	public Estados getEstadoSelect() {
		return estadoSelect;
	}

	public void setEstadoSelect(Estados estadoSelect) {
		this.estadoSelect = estadoSelect;
	}


	public AsentamientoHumanoPK getAsentamientoHumanoPKSelect() {
		return asentamientoHumanoPKSelect;
	}

	public void setAsentamientoHumanoPKSelect(AsentamientoHumanoPK asentamientoHumanoPKSelect) {
		this.asentamientoHumanoPKSelect = asentamientoHumanoPKSelect;
	}

	public AsentamientoHumano getAsentamientoHumanoSelect() {
		return asentamientoHumanoSelect;
	}

	public void setAsentamientoHumanoSelect(AsentamientoHumano asentamientoHumanoSelect) {
		this.asentamientoHumanoSelect = asentamientoHumanoSelect;
	}

	public int getIdTipoAsentamiento() {
		return idTipoAsentamiento;
	}

	public void setIdTipoAsentamiento(int idTipoAsentamiento) {
		this.idTipoAsentamiento = idTipoAsentamiento;
	}

	public List<Municipios> getListaMunicipios() {
		return listaMunicipios;
	}

	public void setListaMunicipios(List<Municipios> listaMunicipios) {
		this.listaMunicipios = listaMunicipios;
	}

	public Ciudades getCiudadSelect() {
		return ciudadSelect;
	}
	public void setCiudadSelect(Ciudades ciudadSelect) {
		this.ciudadSelect = ciudadSelect;
	}
	public CiudadesPK getCiudadPKSelect() {
		return ciudadPKSelect;
	}
	public void setCiudadPKSelect(CiudadesPK ciudadPKSelect) {
		this.ciudadPKSelect = ciudadPKSelect;
	}
	public List<Ciudades> getListaCiudades() {
		return listaCiudades;
	}
	public void setListaCiudades(List<Ciudades> listaCiudades) {
		this.listaCiudades = listaCiudades;
	}

	public List<Estados> getEstadosList() {
		return estadosList;
	}

	public void setEstadosList(List<Estados> estadosList) {
		this.estadosList = estadosList;
	}

	public List<Paises> getListaPaises() {
		return listaPaises;
	}

	public void setListaPaises(List<Paises> listaPaises) {
		this.listaPaises = listaPaises;
	}

	public List<Planta> getList() {
		return list;
	}

	public void setList(List<Planta> list) {
		this.list = list;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public Planta getPlanta() {
		return planta;
	}

	public void setPlanta(Planta planta) {
		this.planta = planta;
	}

	public Planta getSeleccion() {
		return seleccion;
	}

	public void setSeleccion(Planta seleccion) {
		this.seleccion = seleccion;
	}

	public List<EmisoresCFDIS> getListaEmisores() {
		return listaEmisores;
	}

	public void setListaEmisores(List<EmisoresCFDIS> listaEmisores) {
		this.listaEmisores = listaEmisores;
	}

	public EmisoresCFDIS getIdEmisoresCFDIS() {
		return idEmisoresCFDIS;
	}

	public void setIdEmisoresCFDIS(EmisoresCFDIS idEmisoresCFDIS) {
		this.idEmisoresCFDIS = idEmisoresCFDIS;
	}

	public List<SerieFactura> getListaSerieFactura() {
		return listaSerieFactura;
	}

	public void setListaSerieFactura(List<SerieFactura> listaSerieFactura) {
		this.listaSerieFactura = listaSerieFactura;
	}


}
