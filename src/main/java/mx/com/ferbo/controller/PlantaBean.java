package mx.com.ferbo.controller;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.CiudadesDAO;
import mx.com.ferbo.dao.EntidadPostalDAO;
import mx.com.ferbo.dao.EstadosDAO;
import mx.com.ferbo.dao.MunicipiosDAO;
import mx.com.ferbo.dao.PaisesDAO;
import mx.com.ferbo.dao.PlantaDAO;
import mx.com.ferbo.dao.TipoAsentamientoDAO;
import mx.com.ferbo.dao.UsuarioDAO;
import mx.com.ferbo.model.AsentamientoHumano;
import mx.com.ferbo.model.AsentamientoHumanoPK;
import mx.com.ferbo.model.Ciudades;
import mx.com.ferbo.model.CiudadesPK;
import mx.com.ferbo.model.EntidadPostal;
import mx.com.ferbo.model.Estados;
import mx.com.ferbo.model.EstadosPK;
import mx.com.ferbo.model.Municipios;
import mx.com.ferbo.model.MunicipiosPK;
import mx.com.ferbo.model.Paises;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.TipoAsentamiento;
import mx.com.ferbo.model.Usuario;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class PlantaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private PlantaDAO daoPlanta;
	private PaisesDAO daoPaises;
	private EstadosDAO daoEstados;
	private MunicipiosDAO daoMunicipios;
	private CiudadesDAO ciudadesDao;
	private EntidadPostalDAO entidadPostalDao;
	private TipoAsentamiento tipoAsentamientoSelect; 
	private TipoAsentamientoDAO tipoAsentamientoDao;

	private List<Planta> list;
	private List<Usuario> usuarios;
	private List<Paises> listaPaises;
	private List<Estados> estadosList;
	private List<Municipios> listaMunicipios;
	private List<Ciudades> listaCiudades;
	private List<EntidadPostal> listaEntidadPostal;
	private List<TipoAsentamiento> listaTipoAsentamiento;

	private Paises paisSelect;
	private EstadosPK estadoPkSelect;
	private Estados estadoSelect;
	private MunicipiosPK municipioPkSelect;
	private Municipios municipioSelect;
	private EntidadPostal entidadPostalSelect;
	private CiudadesPK ciudadPKSelect;
	private Ciudades ciudadSelect;
	private AsentamientoHumanoPK asentamientoHumanoPKSelect;
	private AsentamientoHumano asentamientoHumanoSelect;

	private int idMunicipio;
	private int idPais;
	private int idEstado;
	private int idCiudad;
	private int idEntidadPostal;
	private int idTipoAsentamiento;
	private int idAsentamiento;
	private String PlantaDs;
	private String PlantaAbrev;
	private String PlantaSufijo;
	private int IdUsuario;
	
	private Planta planta;
	private Planta seleccion;

	public PlantaBean() {
		
		daoPlanta = new PlantaDAO();
		daoPaises = new PaisesDAO();
		daoEstados = new EstadosDAO();
		daoMunicipios = new MunicipiosDAO();
		ciudadesDao = new CiudadesDAO();
		entidadPostalDao = new EntidadPostalDAO();
		tipoAsentamientoDao = new TipoAsentamientoDAO();
		
		list = daoPlanta.findall();
		usuarios = daoPlanta.getUsuarios();
		listaPaises = daoPaises.findall();
		estadosList = daoEstados.findall();
		listaMunicipios = daoMunicipios.findall();
		listaCiudades = ciudadesDao.findall();
		listaTipoAsentamiento = tipoAsentamientoDao.findall();
		planta = new Planta();
		seleccion = new Planta();
		
	};
	
	@PostConstruct
	public void init() {
		listaPaises = daoPaises.buscarTodos();
		listaEntidadPostal = entidadPostalDao.buscarTodos();
		listaTipoAsentamiento = tipoAsentamientoDao.buscarTodos();

		
		this.paisSelect = new Paises();
		this.estadoSelect = new Estados();
		this.estadoPkSelect = new EstadosPK();
		this.municipioSelect = new Municipios();
		this.municipioPkSelect = new MunicipiosPK();
		this.ciudadPKSelect = new CiudadesPK();
		this.ciudadSelect = new Ciudades();
		this.entidadPostalSelect = new EntidadPostal();
		this.asentamientoHumanoPKSelect = new AsentamientoHumanoPK();
		this.asentamientoHumanoSelect = new AsentamientoHumano();
		this.tipoAsentamientoSelect = new TipoAsentamiento();
	}

		public void handleContrySelect() {
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
			listaTipoAsentamiento = tipoAsentamientoDao.findall();
		}
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

	public void openNew() {
		planta = new Planta();
	};

	public void save() {
		PrimeFaces.current().executeScript("PF('dg-agrega').hide()");
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
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Planta Agregada" + planta.getPlantaDs(), null));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-planta");
		} else {
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al agregar " + planta.getPlantaDs(), message));
			PrimeFaces.current().ajax().update("form:messages");
		}
		this.planta = new Planta();
	};

	public void update() {
		PrimeFaces.current().executeScript("PF('dg-modifica').hide()");
		planta.setIdPais(this.idPais);
		planta.setIdEstado(this.idEstado);
		planta.setIdMunicipio(this.idMunicipio);
		planta.setIdCiudad(this.idCiudad);
		planta.setIdAsentamiento(this.idAsentamiento);
		planta.setTipoasentamiento(this.idTipoAsentamiento);
		String message = daoPlanta.update(planta);

		if (message == null) {
			list.clear();
			list = daoPlanta.findall();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Planta Modificada" + planta.getPlantaDs(), null));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-planta");
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al modificar " + planta.getPlantaDs(), message));
			PrimeFaces.current().ajax().update("form:messages");
		}
		this.planta = new Planta();
	};

	public void delete() {
		PrimeFaces.current().executeScript("PF('dg-delete').hide()");
		String message = daoPlanta.delete(planta);

		if (message == null) {
			list.remove(this.planta);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Planta Eliminado " + planta.getPlantaDs(), null));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-planta");
		} else {
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
	public void setIdPais(int idPais) {
		this.idPais = idPais;
	}

	public int getIdAsentamiento() {
		return idAsentamiento;
	}

	public void setIdAsentamiento(int idAsentamiento) {
		this.idAsentamiento = idAsentamiento;
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

	public List<EntidadPostal> getListaEntidadPostal() {
		return listaEntidadPostal;
	}

	public void setListaEntidadPostal(List<EntidadPostal> listaEntidadPostal) {
		this.listaEntidadPostal = listaEntidadPostal;
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

	public int getIdEntidadPostal() {
		return idEntidadPostal;
	}

	public void setIdEntidadPostal(int idEntidadPostal) {
		this.idEntidadPostal = idEntidadPostal;
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

	public EntidadPostal getEntidadPostalSelect() {
		return entidadPostalSelect;
	}

	public void setEntidadPostalSelect(EntidadPostal entidadPostalSelect) {
		this.entidadPostalSelect = entidadPostalSelect;
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

	
	public int getIdCiudad() {
		return idCiudad;
	}

	public void setIdCiudad(int idCiudad) {
		this.idCiudad = idCiudad;
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
	public Integer getIdPais() {
		return idPais;
	}
	public void setIdPais(Integer idPais) {
		this.idPais = idPais;
	}
	public int getIdEstado() {
		return idEstado;
	}
	public void setIdEstado(int idEstado) {
		this.idEstado = idEstado;
	}
	public int getIdMunicipio() {
		return idMunicipio;
	}

	public void setIdMunicipio(int idMunicipio) {
		this.idMunicipio = idMunicipio;
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
	};

}
