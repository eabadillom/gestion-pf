package mx.com.ferbo.controller;

import org.primefaces.PrimeFaces;


import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import mx.com.ferbo.dao.CamaraDAO;
import mx.com.ferbo.dao.PlantaDAO;
import mx.com.ferbo.dao.PosicionCamaraDAO;
import mx.com.ferbo.model.Camara;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.Posicion;

@Named
@ViewScoped
public class PosicionCamaraBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private PosicionCamaraDAO result;
	private List<Posicion> posiciones;
	private Posicion selectPosicion;

	private List<Planta> plantas;
	private PlantaDAO plantasDao;
	private CamaraDAO camaraDao;
	private List<Camara> camaras;
	private Camara camaraSelect;
	private Planta plantaSelect;
	private Planta plantaAgregarSelect;
	private List<Camara> camaraPorPlanta;
	private List<Camara> camaraPorPlantaAgregar;
	private List<Posicion> listaPosiciones;
	private Posicion checkHabilitado;

	public Posicion getCheckHabilitado() {
		return checkHabilitado;
	}

	public void setCheckHabilitado(Posicion checkHabilitado) {
		this.checkHabilitado = checkHabilitado;
	}

	private Posicion nuevaPosicion;

	public PosicionCamaraBean() {
		plantasDao = new PlantaDAO();
		camaraDao = new CamaraDAO();
		result = new PosicionCamaraDAO();
		this.posiciones = result.findAll();
		plantas = plantasDao.findall();
		camaras = camaraDao.buscarTodos();
		camaraPorPlanta = new ArrayList<Camara>();
		camaraPorPlantaAgregar = new ArrayList<Camara>();
		listaPosiciones = posiciones;
		checkHabilitado = new Posicion();
	}

	public void filtraListado() {
		camaraPorPlanta.clear();
		camaraPorPlanta = camaras.stream()
				.filter(ps -> plantaSelect != null
						? (ps.getPlantaCve().getPlantaCve().intValue() == plantaSelect.getPlantaCve().intValue())
						: false)
				.collect(Collectors.toList());
		System.out.println("Productos Cliente Filtrados:" + camaraPorPlanta.toString() + "---------------------------------------------------------------------------------------");
		//PrimeFaces.current().ajax().update("form:busqueda");
	}

	public void filtrarPosiciones() {

		Posicion posicion = new Posicion();
		posicion.setPlanta(plantaSelect);
		posicion.setCamara(camaraSelect);
		listaPosiciones = result.buscarPorCriterios(posicion);
		listaPosiciones = posiciones.stream()
				.filter(pc -> plantaSelect != null
						? (pc.getPlanta().getPlantaCve() == camaraSelect.getPlantaCve().getPlantaCve())
						: false)
				.collect(Collectors.toList());
		System.out.println("posicion planta -----------------------------------------------------" + listaPosiciones);
		listaPosiciones = posiciones.stream()
				.filter(cs -> camaraSelect != null
						? (cs.getCamara().getCamaraCve() == camaraSelect.getCamaraCve().intValue())
						: false)
				.collect(Collectors.toList());

		System.out.println("camaras-----------------------------------------------------------" + listaPosiciones);
//		PrimeFaces.current().ajax().update("form:dt-posiciones");
	}

	public void openNew() {
		nuevaPosicion = new Posicion();
		nuevaPosicion.setPlanta(new Planta());
		nuevaPosicion.setCamara(new Camara());

		System.out.println(nuevaPosicion + "************************POSICION");

	}

	public void save() {
		
		PrimeFaces.current().executeScript("PF('dg-agrega').hide()");
		String message = result.save(nuevaPosicion);

		if (message == null) {
			posiciones.clear();
			posiciones = result.findAll();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "posicion nueva agregada ", null));
			PrimeFaces.current().ajax().update("form:messages", "form:dtposiciones");
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al agregar ", message));
			PrimeFaces.current().ajax().update("form:messages");
		}
		this.nuevaPosicion = new Posicion();
		this.nuevaPosicion.setPlanta(new Planta());
		this.nuevaPosicion.setCamara(new Camara());
	}
	
	public void filtrarAgregar() {
		camaraPorPlantaAgregar.clear();
		camaraPorPlantaAgregar = camaras.stream()
				.filter(ps -> nuevaPosicion != null
						? (ps.getPlantaCve().getPlantaCve().intValue() == nuevaPosicion.getPlanta().getPlantaCve().intValue())
						: false)
				.collect(Collectors.toList());
		System.out.println("Productos Cliente Filtrados:" + camaraPorPlantaAgregar.toString() + "---------------------------------------------------------------------------------------");
		//PrimeFaces.current().ajax().update("form:busqueda");
	}
	
	public void check() {
		System.out.println(checkHabilitado.getHabilitada());
		System.out.println("------------------------------------------------------------------------------");
		if(checkHabilitado.getHabilitada() == true) {
			checkHabilitado.setHabilitada(false);
			System.out.println(checkHabilitado.getHabilitada() + "cambio a falso");
		}else {
			checkHabilitado.setHabilitada(true);
			System.out.println(checkHabilitado.getHabilitada() + "cambio a true");
		}
		
		String message = result.actualizar(checkHabilitado);
		if (message == null) {
			posiciones.clear();
			posiciones = result.findAll();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "se habilito ", null));
			PrimeFaces.current().ajax().update("form:messages", "form:dtposiciones");
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo habilitar ", message));
			PrimeFaces.current().ajax().update("form:messages");
		}
		this.checkHabilitado = new Posicion();
		this.checkHabilitado.setPlanta(new Planta());
		this.checkHabilitado.setCamara(new Camara());
	}
	
	

	public Planta getPlantaAgregarSelect() {
		return plantaAgregarSelect;
	}

	public void setPlantaAgregarSelect(Planta plantaAgregarSelect) {
		this.plantaAgregarSelect = plantaAgregarSelect;
	}

	public List<Camara> getCamaraPorPlantaAgregar() {
		return camaraPorPlantaAgregar;
	}

	public void setCamaraPorPlantaAgregar(List<Camara> camaraPorPlantaAgregar) {
		this.camaraPorPlantaAgregar = camaraPorPlantaAgregar;
	}

	public Posicion getNuevaPosicion() {
		return nuevaPosicion;
	}

	public void setNuevaPosicion(Posicion nuevaPosicion) {
		this.nuevaPosicion = nuevaPosicion;
	}

	public List<Posicion> getListaPosiciones() {
		return listaPosiciones;
	}

	public void setListaPosiciones(List<Posicion> listaPosiciones) {
		this.listaPosiciones = listaPosiciones;
	}

	public List<Camara> getCamaraPorPlanta() {
		return camaraPorPlanta;
	}

	public void setCamaraPorPlanta(List<Camara> camaraPorPlanta) {
		this.camaraPorPlanta = camaraPorPlanta;
	}

	public PlantaDAO getPlantasDao() {
		return plantasDao;
	}

	public void setPlantasDao(PlantaDAO plantasDao) {
		this.plantasDao = plantasDao;
	}

	public CamaraDAO getCamaraDao() {
		return camaraDao;
	}

	public void setCamaraDao(CamaraDAO camaraDao) {
		this.camaraDao = camaraDao;
	}

	public List<Planta> getPlantas() {
		return plantas;
	}

	public void setPlantas(List<Planta> plantas) {
		this.plantas = plantas;
	}

	public List<Camara> getCamaras() {
		return camaras;
	}

	public void setCamaras(List<Camara> camaras) {
		this.camaras = camaras;
	}

	public Camara getCamaraSelect() {
		return camaraSelect;
	}

	public void setCamaraSelect(Camara camaraSelect) {
		this.camaraSelect = camaraSelect;
	}

	public Planta getPlantaSelect() {
		return plantaSelect;
	}

	public void setPlantaSelect(Planta plantaSelect) {
		this.plantaSelect = plantaSelect;
	}

	public List<Posicion> getAll() {
		return result.findAll();
	}

	public PosicionCamaraDAO getResult() {
		return result;
	}

	public void setResult(PosicionCamaraDAO result) {
		this.result = result;
	}

	public List<Posicion> getPosiciones() {
		return posiciones;
	}

	public void setPosiciones(List<Posicion> posiciones) {
		this.posiciones = posiciones;
	}

	public Posicion getSelectPosicion() {
		return selectPosicion;
	}

	public void setSelectPosicion(Posicion selectPosicion) {
		this.selectPosicion = selectPosicion;
	}

	/*
	 * public Planta getOne(int id) { Planta p = new Planta(); p =
	 * result.findOne(id);
	 * 
	 * System.out.println("---------------------------------");
	 * System.out.println(p.getPlantaDs());
	 * 
	 * return p; }
	 * 
	 * public void deleteOne(Planta p) { result.delete(p); principal.remove(p); //
	 * return "/protected/catalogos/plantas.xhtml"; }
	 * 
	 * public List<Planta> getPrincipal() { return principal; }
	 * 
	 * public void setPrincipal(List<Planta> principal) { this.principal =
	 * principal; }
	 */
}
