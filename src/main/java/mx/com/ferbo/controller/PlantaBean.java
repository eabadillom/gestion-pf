package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.AsentamientoHumandoDAO;
import mx.com.ferbo.dao.CiudadesDAO;
import mx.com.ferbo.dao.EstadosDAO;
import mx.com.ferbo.dao.MunicipiosDAO;
import mx.com.ferbo.dao.PaisesDAO;
import mx.com.ferbo.dao.PlantaDAO;
import mx.com.ferbo.dao.TipoAsentamientoDAO;
import mx.com.ferbo.model.AsentamientoHumano;
import mx.com.ferbo.model.AsentamientoHumanoPK;
import mx.com.ferbo.model.Ciudades;
import mx.com.ferbo.model.CiudadesPK;
import mx.com.ferbo.model.EmisoresCFDIS;
import mx.com.ferbo.model.Estados;
import mx.com.ferbo.model.EstadosPK;
import mx.com.ferbo.model.Municipios;
import mx.com.ferbo.model.MunicipiosPK;
import mx.com.ferbo.model.Paises;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.TipoAsentamiento;
import mx.com.ferbo.model.Usuario;

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
	private AsentamientoHumandoDAO asentamientoHumandoDao;

	private List<Planta> list;
	private List<Usuario> usuarios;
	private List<Paises> listaPaises;
	private List<Estados> estadosList;
	private List<Municipios> listaMunicipios;
	private List<Ciudades> listaCiudades;
	private List<TipoAsentamiento> listaTipoAsentamiento;
	private List<AsentamientoHumano> asentamientoHumanoList;
	private List<EmisoresCFDIS> listaEmisores;

	private Paises paisSelect;
	private EstadosPK estadoPkSelect;
	private Estados estadoSelect;
	private MunicipiosPK municipioPkSelect;
	private Municipios municipioSelect;
	private CiudadesPK ciudadPKSelect;
	private Ciudades ciudadSelect;
	private AsentamientoHumanoPK asentamientoHumanoPKSelect;
	private AsentamientoHumano asentamientoHumanoSelect;
	
	String cadena;

	/*private int idMunicipio;
	private int idPais;
	private int idEstado;
	private int idCiudad;
	private int idAsentamiento;*/
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

	public PlantaBean() {
		
		daoPlanta = new PlantaDAO();
		daoPaises = new PaisesDAO();
		daoEstados = new EstadosDAO();
		daoMunicipios = new MunicipiosDAO();
		ciudadesDao = new CiudadesDAO();
		tipoAsentamientoDao = new TipoAsentamientoDAO();
		asentamientoHumandoDao = new AsentamientoHumandoDAO();
		
		listaEmisores = new ArrayList<EmisoresCFDIS>();
		listaMunicipios = new ArrayList<>();
		listaCiudades = new ArrayList<>();
		asentamientoHumanoList = new ArrayList<>();
		
		planta = new Planta();
		seleccion = new Planta();
	};
	
	@PostConstruct
	public void init() {
		list = daoPlanta.findall(true);
		usuarios = daoPlanta.getUsuarios();
		listaPaises = daoPaises.findall();
		listaEmisores = daoPlanta.getEmisor();
		
		listaPaises = daoPaises.buscarTodos();
		listaTipoAsentamiento = tipoAsentamientoDao.buscarTodos();
		//listaEmisores = emisorDAO.findall();
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
	
	public void datosPlanta() {
		filtroPais();
		//Cargar de datos de estado planta
		filtroEstado();
		
		List<Estados> tmpEstadosList = estadosList.stream().filter(e -> e.getEstadosPK().getEstadoCve() == planta.getIdEstado())
				.collect(Collectors.toList());
		if((tmpEstadosList != null ) && (tmpEstadosList.size()>0)) {
			Estados estado = tmpEstadosList.get(0);
			estadoSelect = estado;
		}
		
		//Cargar de datos de municipio planta
		filtroMunicipio();
		
		List<Municipios> tmpMunicipios = listaMunicipios.stream().filter(m -> m.getMunicipiosPK().getMunicipioCve() == planta.getIdMunicipio())
				.collect(Collectors.toList());
		
		if((tmpMunicipios != null)&&(tmpMunicipios.size()>0)) {
			Municipios municipio = tmpMunicipios.get(0);
			municipioSelect = municipio;//EVITAR QUE SE HAGA ESTA ASIGNACION CUANDO SE MODIFICA EL ESTADO (QUE NO ESTE SELECCIONADO)
		}
		
		//Cargar de datos de ciudades planta
		filtroCiudad();
		
		List<Ciudades> tmpCiudades = listaCiudades.stream().filter(c -> c.getCiudadesPK().getCiudadCve()==planta.getIdCiudad())
				.collect(Collectors.toList());
		
		if((tmpCiudades != null)&&(tmpCiudades.size()>0)) {
			Ciudades ciudad = tmpCiudades.get(0);
			ciudadSelect = ciudad;
		}
		
		//Cargar de datos de asentamiento planta
		filtroAsentamiento();
		
		List<AsentamientoHumano> tmpAsentamientoHumano = asentamientoHumanoList.stream().filter(ah -> ah.getAsentamientoHumanoPK().getAsentamientoCve()==planta.getIdAsentamiento())
				.collect(Collectors.toList());
		
		if((tmpAsentamientoHumano != null)&&(tmpAsentamientoHumano.size()>0)) { 
			AsentamientoHumano asentamientoT = tmpAsentamientoHumano.get(0);
			asentamientoHumanoSelect = asentamientoT;
		}
		
		tipoAsentamiento();
		
		PrimeFaces.current().ajax().update("form:state","form:messages","form:Municipality","form:city","form:asentamiento","form:codigopostal-new","form:Tasn-new");
		
	}
	
	
	public int filtroPais() {
		
		paisSelect = null;
		if(planta.getPlantaCve()!=null) {
			paisSelect = daoPaises.buscarPorId(planta.getIdPais());
		}else {
			listaPaises = daoPaises.buscarTodos();
		}
		
		return 0;
		
	}
	
	public int filtroEstado() {
		
		estadoSelect = new Estados();
		
		if(planta.getIdPais()!=null) {
			estadosList = daoEstados.buscarPorPais(paisSelect);
			if(estadosList.isEmpty()) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "No hay estados para el pais seleccionado"));
			}else {
				listaMunicipios.clear();
				listaCiudades.clear();
				asentamientoHumanoList.clear();
				tipoAsentamientoSelected = "";
				codigopostalSelected = "";
			}
			
			PrimeFaces.current().ajax().update("form:state","form:messages","form:Municipality","form:city","form:asentamiento","form:codigopostal-new","form:Tasn-new");
			
			return 0;
		}
		else if(paisSelect != null) {
			estadosList = daoEstados.buscarPorPais(paisSelect);
			if(estadosList.isEmpty()) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "No hay estados para el pais seleccionado"));
			}else {
				listaMunicipios.clear();
				listaCiudades.clear();
				asentamientoHumanoList.clear();
				tipoAsentamientoSelected = "";
				codigopostalSelected = "";
			}
		}
		
		PrimeFaces.current().ajax().update("form:state","form:messages","form:Municipality","form:city","form:asentamiento","form:codigopostal-new","form:Tasn-new");
		
		return 0;
		
	}
	
	public int filtroMunicipio() {
		
		municipioSelect = new Municipios();
		
		if(planta.getIdMunicipio()!=null) {
			
			municipioPkSelect.setPaisCve(paisSelect.getPaisCve());
			municipioPkSelect.setEstadoCve(estadoSelect.getEstadosPK().getEstadoCve());
			municipioSelect.setMunicipiosPK(municipioPkSelect);
			
			listaMunicipios = daoMunicipios.buscarPorCriteriosMunicipios(municipioSelect);
			
			if(listaMunicipios.isEmpty()) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "No hay municipios para el estado seleccionado"));
			}
			
			listaCiudades.clear();
			asentamientoHumanoList.clear();
			tipoAsentamientoSelected = "";
			codigopostalSelected = "";
			
			
			PrimeFaces.current().ajax().update("form:messages");
			return 0;
		}else if(estadoSelect!=null && paisSelect!=null) {
			//Buscar por pais y estado
			
			municipioPkSelect.setPaisCve(paisSelect.getPaisCve());
			municipioPkSelect.setEstadoCve(estadoSelect.getEstadosPK().getEstadoCve());
			municipioSelect.setMunicipiosPK(municipioPkSelect);
			
			listaMunicipios = daoMunicipios.buscarPorCriteriosMunicipios(municipioSelect);
			
			
			if(listaMunicipios.isEmpty()) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "No hay municipios para el estado seleccionado"));
			}
			listaCiudades.clear();
			asentamientoHumanoList.clear();
			
		}
		
		PrimeFaces.current().ajax().update("form:messages","form:city","form:asentamiento","form:codigopostal-new","form:Tasn-new");
		
		return 0;
		
	}
	
	public int filtroCiudad() {
		
		ciudadSelect = new Ciudades();
	
		if(planta.getIdCiudad()!=null) {
			
			this.ciudadPKSelect.setPaisCve(paisSelect.getPaisCve());
			this.ciudadPKSelect.setEstadoCve(estadoSelect.getEstadosPK().getEstadoCve());
			this.ciudadPKSelect.setMunicipioCve(municipioSelect.getMunicipiosPK().getMunicipioCve());
			this.ciudadSelect.setCiudadesPK(ciudadPKSelect);
						
			listaCiudades = ciudadesDao.buscarPorCriteriosCiudades(ciudadSelect);
			
			ciudadSelect = new Ciudades();
			
			if(listaCiudades.isEmpty()) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "No hay ciudades para el municipio seleccionado"));
				
			}
			
			asentamientoHumanoList.clear();
			tipoAsentamientoSelected = "";
			codigopostalSelected = "";
			
			
			PrimeFaces.current().ajax().update("form:messages","form:codigopostal-new","form:Tasn-new","form:city");
			return 0;			
		}else if(paisSelect!=null&&estadoSelect!=null&&municipioSelect!=null) {
			
			this.ciudadPKSelect.setPaisCve(paisSelect.getPaisCve());
			this.ciudadPKSelect.setEstadoCve(estadoSelect.getEstadosPK().getEstadoCve());
			this.ciudadPKSelect.setMunicipioCve(municipioSelect.getMunicipiosPK().getMunicipioCve());
			this.ciudadSelect.setCiudadesPK(ciudadPKSelect);
						
			listaCiudades = ciudadesDao.buscarPorCriteriosCiudades(ciudadSelect);
			
			if((listaCiudades.isEmpty())) {
				ciudadSelect = new Ciudades();
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "No hay ciudades para el municipio seleccionado"));
				
			}else {
				asentamientoHumanoList.clear(); //Limpiar asentamientos MODIFICAR
				ciudadSelect = new Ciudades();
			}
		}
		
		PrimeFaces.current().ajax().update("form:messages","form:city");
		
		return 0;
		
	}
	
	public int filtroAsentamiento() {
		
		asentamientoHumanoSelect = new AsentamientoHumano();
		
		if(planta.getIdAsentamiento()!=null) {
			
			this.asentamientoHumanoPKSelect.setPaisCve(paisSelect.getPaisCve());
			this.asentamientoHumanoPKSelect.setEstadoCve(estadoSelect.getEstadosPK().getEstadoCve());
			this.asentamientoHumanoPKSelect.setMunicipioCve(municipioSelect.getMunicipiosPK().getMunicipioCve());
			this.asentamientoHumanoPKSelect.setCiudadCve(ciudadSelect.getCiudadesPK().getCiudadCve());
			this.asentamientoHumanoSelect.setAsentamientoHumanoPK(asentamientoHumanoPKSelect);			
			
			asentamientoHumanoList = asentamientoHumandoDao.buscarPorCriterios(asentamientoHumanoSelect);
			
			if(asentamientoHumanoList.isEmpty()) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "No hay asentamientos para la ciudad seleccionada"));				
				asentamientoHumanoList.clear();
				tipoAsentamientoSelected = "";
				codigopostalSelected = "";
			}
			
			
			PrimeFaces.current().ajax().update("form:messages","form:codigopostal-new","form:Tasn-new");
			return 0;			
		}else if(paisSelect!=null&&estadoSelect!=null&&municipioSelect!=null&&ciudadSelect!=null) {
			
			this.asentamientoHumanoPKSelect.setPaisCve(paisSelect.getPaisCve());
			this.asentamientoHumanoPKSelect.setEstadoCve(estadoSelect.getEstadosPK().getEstadoCve());
			this.asentamientoHumanoPKSelect.setMunicipioCve(municipioSelect.getMunicipiosPK().getMunicipioCve());
			this.asentamientoHumanoPKSelect.setCiudadCve(ciudadSelect.getCiudadesPK().getCiudadCve());
			this.asentamientoHumanoSelect.setAsentamientoHumanoPK(asentamientoHumanoPKSelect);			
			
			asentamientoHumanoList = asentamientoHumandoDao.buscarPorCriterios(asentamientoHumanoSelect);
			
			if(asentamientoHumanoList.isEmpty()) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "No hay asentamientos para la ciudad seleccionada"));
			}
			
		}
		
		PrimeFaces.current().ajax().update("form:messages");
		
		return 0;
		
	}
	
	public void tipoAsentamiento() {
		
		tipoAsentamientoSelected = "";
		idTipoAsentamiento = 0;
		codigopostalSelected = null;
		AsentamientoHumano ah = new AsentamientoHumano();
		if(planta.getIdAsentamiento()!=null||paisSelect!=null) {
			this.asentamientoHumanoPKSelect.setPaisCve(paisSelect.getPaisCve());
			this.asentamientoHumanoPKSelect.setEstadoCve(estadoSelect.getEstadosPK().getEstadoCve());
			this.asentamientoHumanoPKSelect.setMunicipioCve(municipioSelect.getMunicipiosPK().getMunicipioCve());
			this.asentamientoHumanoPKSelect.setCiudadCve(ciudadSelect.getCiudadesPK().getCiudadCve());
			this.asentamientoHumanoPKSelect.setAsentamientoCve(asentamientoHumanoSelect.getAsentamientoHumanoPK().getAsentamientoCve());
			//asentamientoHumanoSelect = new AsentamientoHumano();
			this.asentamientoHumanoSelect.setAsentamientoHumanoPK(asentamientoHumanoPKSelect);
			asentamientoHumanoPKSelect = new AsentamientoHumanoPK();
		  ah = asentamientoHumandoDao.buscar(asentamientoHumanoSelect);
		  if(ah!=null) {
				this.tipoAsentamientoSelected = ah.getTipoAsentamiento().getTipoasntmntoDs();
				this.idTipoAsentamiento = ah.getTipoAsentamiento().getTipoasntmntoCve();
				this.codigopostalSelected = ah.getCp();
		  }else {
			  FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "No tiene asentamiento humano existente"));
		  }
			//nombreEmisor();
		
		 PrimeFaces.current().ajax().update("form:messages");
			
		}
		
		
	}
	
		/*public void handleContrySelect() {
			
		if (this.idPais != -1) {
			this.estadoPkSelect.setPaisCve(idPais);
			this.estadoSelect.setEstadosPK(estadoPkSelect);
			estadosList= daoEstados.buscarPorCriteriosEstados(estadoSelect);
		}
	}
	public void handleStateSelect() {
		if (this.idEstado != -1) {
			this.municipioPkSelect.setPaisCve(idPais);
			this.municipioPkSelect.setEstadoCve(idEstado);
			this.municipioSelect.setMunicipiosPK(municipioPkSelect);
			listaMunicipios = daoMunicipios.buscarPorCriteriosMunicipios(municipioSelect);
			Log.info("Lista Municipios" + listaMunicipios);
		}
	}
	public void handleMunicipalitySelect() {
		if (this.idMunicipio != -1) {
			this.ciudadPKSelect.setPaisCve(idPais);
			this.ciudadPKSelect.setEstadoCve(idEstado);
			this.ciudadPKSelect.setMunicipioCve(idMunicipio);
			this.ciudadSelect.setCiudadesPK(ciudadPKSelect);
			listaCiudades = ciudadesDao.buscarPorCriteriosCiudades(ciudadSelect);
		}
	}
	public void handleCitySelect() {
		if (this.idCiudad != -1) {
			this.asentamientoHumanoPKSelect.setPaisCve(idPais);
			this.asentamientoHumanoPKSelect.setEstadoCve(idEstado);
			this.asentamientoHumanoPKSelect.setMunicipioCve(idMunicipio);
			this.asentamientoHumanoPKSelect.setCiudadCve(idCiudad);
			this.asentamientoHumanoSelect.setAsentamientoHumanoPK(asentamientoHumanoPKSelect);
			asentamientoHumanoList = asentamientoHumandoDao.buscarPorCriterios(asentamientoHumanoSelect);
		}
	}	
	public void handleAsentH() {
		if (this.idAsentamiento != -1) {
			this.asentamientoHumanoPKSelect.setPaisCve(idPais);
			this.asentamientoHumanoPKSelect.setEstadoCve(idEstado);
			this.asentamientoHumanoPKSelect.setMunicipioCve(idMunicipio);
			this.asentamientoHumanoPKSelect.setCiudadCve(idCiudad);
			this.asentamientoHumanoPKSelect.setAsentamientoCve(idAsentamiento);
			this.asentamientoHumanoSelect.setAsentamientoHumanoPK(asentamientoHumanoPKSelect);
			AsentamientoHumano ah = asentamientoHumandoDao.buscar(asentamientoHumanoSelect);
			this.tipoAsentamientoSelected = ah.getTipoAsentamiento().getTipoasntmntoDs();
			this.idTipoAsentamiento = ah.getTipoAsentamiento().getTipoasntmntoCve();
			this.codigopostalSelected = ah.getCp();
		}
	}	*/
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

	public void openNew() {
		this.planta = new Planta();
		this.paisSelect = new Paises();
		this.estadoSelect = new Estados();
		this.municipioSelect = new Municipios();
		this.ciudadSelect = new Ciudades();
		this.asentamientoHumanoSelect = new AsentamientoHumano();
		this.tipoAsentamientoSelected = "";
		this.codigopostalSelected = "";
	};
	/*public void save() {
		PrimeFaces.current().executeScript("PF('dg-agrega').hide()");
		System.out.println(planta);
		planta.setIdPais(this.idPais);
		planta.setIdEstado(this.idEstado);
		planta.setIdMunicipio(this.idMunicipio);
		planta.setIdCiudad(this.idCiudad);
		planta.setIdAsentamiento(this.idAsentamiento);
		planta.setTipoasentamiento(this.idTipoAsentamiento);
		String message = daoPlanta.save(planta);

		if (message == null) {
			list.clear();
			list = daoPlanta.findall();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Planta Modificada" + planta.getPlantaDs(), null));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-planta");
		} else {
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR, "Planta Modificada" + planta.getPlantaDs(), message));
			PrimeFaces.current().ajax().update("form:messages");
		}
		this.planta = new Planta();

};*/

	public void update() {
		PrimeFaces.current().executeScript("PF('dg-agrega').hide()");
		
		int idPais = paisSelect.getPaisCve();
		int idEstado = estadoSelect.getEstadosPK().getEstadoCve();
		int idMunicipio = municipioSelect.getMunicipiosPK().getMunicipioCve();
		int idCiudad = ciudadSelect.getCiudadesPK().getCiudadCve();
		int idAsentamiento = asentamientoHumanoSelect.getAsentamientoHumanoPK().getAsentamientoCve();
		
		//System.out.println(planta + "" +planta.getIdEmisoresCFDIS());
		planta.setIdPais(idPais);
		planta.setIdEstado(idEstado);
		planta.setIdMunicipio(idMunicipio);
		planta.setIdCiudad(idCiudad);
		planta.setIdAsentamiento(idAsentamiento);
		planta.setTipoasentamiento(idTipoAsentamiento);
		planta.setCodigopostal(codigopostalSelected);
		planta.setIdEmisoresCFDIS(planta.getIdEmisoresCFDIS());
		
		//String message = daoPlanta.update(planta);
		String message = null;
		//boolean bandera = false;
		if(planta.getPlantaCve() == null) {//No guardaba
				
			//MODIFICACION	
			daoPlanta.save(this.planta);
			
			//Empieza el llenado de facturama
		/*	Paises buscaPais = daoPaises.buscarPorId(idPais);
			Estados estado = new Estados();
			estado.setPaises(buscaPais);
			List<Estados> buscarPorCriterios = daoEstados.buscarPorCriterios(estado);
			Estados estadoB = null;
			for(Estados e : buscarPorCriterios) {
				EstadosPK pkestado= e.getEstadosPK();
				if(pkestado.getEstadoCve() == idEstado) {
					estadoB = e;
					break;
				}
			}
			log.info("Estado" + estadoB);
			
			Municipios buscaMunicipio = new Municipios();
			MunicipiosPK buscaMunicipiopk = new MunicipiosPK();
			buscaMunicipiopk.setEstadoCve(idEstado);
			buscaMunicipiopk.setPaisCve(idPais);
			buscaMunicipio.setMunicipiosPK(buscaMunicipiopk);
			List<Municipios> buscarMunicipios = daoMunicipios.buscarPorCriteriosMunicipios(buscaMunicipio);
			Municipios buscaMunicipiofor = null;
			for(Municipios e: buscarMunicipios ) {
				MunicipiosPK buscaMunicipioPK = e.getMunicipiosPK();
				if(buscaMunicipioPK.getMunicipioCve() == idMunicipio) {
					buscaMunicipiofor = e;
					break;
				}
			}
			log.info("Municipio" + buscaMunicipiofor);
			Ciudades buscaCiudad = new Ciudades();
			CiudadesPK buscaCiudadPK = new CiudadesPK();
			buscaCiudadPK.setEstadoCve(idEstado);
			buscaCiudadPK.setPaisCve(idPais);
			buscaCiudadPK.setMunicipioCve(idMunicipio);
			buscaCiudad.setCiudadesPK(buscaCiudadPK);
			List<Ciudades> buscarPorCiudades = ciudadesDao.buscarPorCriteriosCiudades(buscaCiudad);
			Ciudades buscaCiudadfor = null;
			for(Ciudades e :buscarPorCiudades ) {
				CiudadesPK buscaCiudadesPK = e.getCiudadesPK();
				if(buscaCiudadesPK.getCiudadCve() == idCiudad) {
					buscaCiudadfor = e;
					break;
				}
			}
			log.info("Ciudades" + buscaCiudadfor);
			AsentamientoHumano buscaAsentamiento = new AsentamientoHumano();
			AsentamientoHumanoPK buscaAsentamientoHumanoPK = new AsentamientoHumanoPK();
			buscaAsentamientoHumanoPK.setEstadoCve(idEstado);
			buscaAsentamientoHumanoPK.setPaisCve(idPais);
			buscaAsentamientoHumanoPK.setMunicipioCve(idMunicipio);
			buscaAsentamientoHumanoPK.setCiudadCve(idCiudad); 
			buscaAsentamiento.setAsentamientoHumanoPK(buscaAsentamientoHumanoPK);
			List<AsentamientoHumano> buscarAsent= asentamientoHumandoDao.buscarPorCriterios(buscaAsentamiento);
			AsentamientoHumano buscaAsentamientofor = null;
			for(AsentamientoHumano e: buscarAsent ) {
				AsentamientoHumanoPK buscaAsnPK = e.getAsentamientoHumanoPK();
				if(buscaAsnPK.getAsentamientoCve() == idAsentamiento){
					buscaAsentamientofor = e;
				}
			}
			log.info("Asentamiento: "+ buscaAsentamientofor);
			TipoAsentamiento buscatipoAsn = new TipoAsentamiento();
			AsentamientoHumanoPK buscaTipoAsentamientoHumanoPK = new AsentamientoHumanoPK();
			buscaTipoAsentamientoHumanoPK.setEstadoCve(idEstado);
			buscaTipoAsentamientoHumanoPK.setPaisCve(idPais);
			buscaTipoAsentamientoHumanoPK.setMunicipioCve(idMunicipio);
			buscaTipoAsentamientoHumanoPK.setCiudadCve(idCiudad); 
			buscaTipoAsentamientoHumanoPK.setAsentamientoCve(idAsentamiento);
			buscatipoAsn.setAsentamientoHumanoList(buscarAsent);
			TipoAsentamiento buscarIdT = tipoAsentamientoDao.buscarPorId(idTipoAsentamiento);
			
			BranchOffice sucursal = new BranchOffice();
			TaxAddress direccion = new TaxAddress();
			direccion.setCountry(buscaPais.getPaisDesc());
			direccion.setState(estadoB.getEstadoDesc());
			//direccion.setState(estado.getEstadoDesc());
			direccion.setMunicipality(buscaMunicipiofor.getMunicipioDs());
			//direccion.setMunicipality(buscaMunicipio.getMunicipioDs());
			direccion.setLocality(buscaCiudadfor.getCiudadDs());
			direccion.setZipCode(this.codigopostalSelected);//modificado por getcodigopostal
			direccion.setNeighborhood(buscaAsentamientofor.getAsentamientoDs());
			direccion.setInteriorNumber(this.planta.getNuminterior());
			direccion.setExteriorNumber(this.planta.getNumexterior());
			direccion.setStreet(this.planta.getCalle());
			sucursal.setName(this.planta.getPlantaDs());
			sucursal.setDescription(this.planta.getPlantaDs());
			sucursal.setAddress(direccion);
	 		FacturamaBL solicitud = new FacturamaBL();
			BranchOfficeViewModel registra = solicitud.registra(sucursal);
			this.planta.setUuid(registra.getId());
			String registro =  daoPlanta.update(planta);
			//Termina llenado de facturama*/
			
			//list.add(planta);
			list = daoPlanta.findall(true);
			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Planta Agregada correctamente" ,  planta.getPlantaDs()));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-planta");
		
		}else {
		
			if (message == null) {
				list.clear();
				daoPlanta.update(planta);
				list = daoPlanta.findall(true);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Planta Modificada" , planta.getPlantaDs()));
				PrimeFaces.current().ajax().update("form:messages", "form:dt-planta");
			} /*else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al agregar planta " + planta.getPlantaDs(), message));
				PrimeFaces.current().ajax().update("form:messages");
			}*/
		}
		
		this.planta = new Planta();
		this.paisSelect = new Paises();
		this.estadoSelect = new Estados();
		this.municipioSelect = new Municipios();
		this.ciudadSelect = new Ciudades();
		//this.asentamientoHumanoSelect = new AsentamientoHumano();
		this.tipoAsentamientoSelected = "";
		this.codigopostalSelected = "";
	};

	public void CJSON() {

	//		FacturamaBL facturamaBO = new FacturamaBL();
	//		List<BranchOfficeViewModel> sucursales= null;
	//		try {
	////			sucursales = facturamaBO.getSucursales();
	//		log.info("sucursales: " + sucursales);
	//		} catch (IOException ex) {
	//			log.error("Problema en la consulta con Facturama...", ex);
	//		}
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
	};

	public void setList(List<Planta> list) {
		this.list = list;
	};

	public List<Usuario> getUsuarios() {
		return usuarios;
	};

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	};

	public Planta getPlanta() {
		return planta;
	};

	public void setPlanta(Planta planta) {
		this.planta = planta;
	};

	public Planta getSeleccion() {
		return seleccion;
	};

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


}
