package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import mx.com.ferbo.dao.CiudadesDAO;
import mx.com.ferbo.dao.ColoniasDAO;
import mx.com.ferbo.dao.EstadosDAO;
import mx.com.ferbo.dao.MunicipiosDAO;
import mx.com.ferbo.dao.PaisesDAO;
import mx.com.ferbo.model.AsentamientoHumano;
import mx.com.ferbo.model.Ciudades;
import mx.com.ferbo.model.Estados;
import mx.com.ferbo.model.Municipios;
import mx.com.ferbo.model.Paises;

@Named
@ViewScoped
public class ColoniasBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private AsentamientoHumano asentamientoHumanoSelect;
	
	private List<AsentamientoHumano> listaAsentamientoHumano;
	private List<AsentamientoHumano> listaAsentamientoHumanoSelected;
	
	private List<Paises> listaPaises;
	private List<Estados> listaEstados;
	private List<Municipios> listaMunicipios;
	private List<Ciudades> listaCiudades;
	
	private ColoniasDAO coloniasDao;
	private PaisesDAO paisesDao;
	
	public ColoniasBean() {
		coloniasDao = new ColoniasDAO();
		paisesDao = new PaisesDAO();
		listaAsentamientoHumanoSelected = new ArrayList<>();
	}
	
	@PostConstruct
	public void init() {
		listaAsentamientoHumano = coloniasDao.buscarTodos();
		listaPaises = paisesDao.buscarTodos();
	}

	public void nuevaColonia() {
		
	}
	
}

