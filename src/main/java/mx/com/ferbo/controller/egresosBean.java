package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Init;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.event.ToggleEvent;

import mx.com.ferbo.dao.CategoriaEgresosDAO;
import mx.com.ferbo.dao.EmisoresCFDISDAO;
import mx.com.ferbo.dao.ImporteEgresosDAO;
import mx.com.ferbo.dao.egresosDAO;
import mx.com.ferbo.dao.tipoEgresoDAO;
import mx.com.ferbo.model.CategoriaEgreso;
import mx.com.ferbo.model.EmisoresCFDIS;
import mx.com.ferbo.model.ImporteEgreso;
import mx.com.ferbo.model.TipoEgreso;
import mx.com.ferbo.model.egresos;

@Named
@ViewScoped
public class egresosBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private egresos nuevoEgreso;
	private Date fechaActual;
	private CategoriaEgreso categoriaSelect;
	private TipoEgreso tipoSelect;
	private ImporteEgreso nuevoImporte;
	private ImporteEgreso importeSelected;
	private String importe;
	
	private List<CategoriaEgreso> listaCatEgresos;
	private List<TipoEgreso> listaTipoEgresos;
	private List<EmisoresCFDIS> listaEmisores;
	private List<ImporteEgreso> listaImporteEgreso;
	private List<egresos> listaEgresos;
	private CategoriaEgresosDAO categoriaDAO;
	private tipoEgresoDAO tipoDAO;
	private EmisoresCFDISDAO emisoresDAO;
	private egresosDAO egresosDAO;
	private ImporteEgresosDAO importeEgresosDAO;
	
	public egresosBean() {
		listaEmisores = new ArrayList<>();
		listaCatEgresos = new ArrayList<>();
		listaTipoEgresos = new ArrayList<>();
		listaEgresos = new ArrayList<>();
		categoriaDAO = new CategoriaEgresosDAO();
		tipoDAO = new tipoEgresoDAO();
		emisoresDAO = new EmisoresCFDISDAO();
		egresosDAO = new egresosDAO();
		fechaActual = new Date();
		importeEgresosDAO = new ImporteEgresosDAO();
	}
	
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void Init() {
		nuevoImporte = new ImporteEgreso();
		listaEgresos = egresosDAO.buscarTodos();
		listaEmisores = emisoresDAO.buscarTodos();
		listaCatEgresos = categoriaDAO.findByAll();
		listaTipoEgresos = tipoDAO.findByAll();
		listaImporteEgreso = importeEgresosDAO.buscarTodos();
	}
	
	
	public void nuevoRegistroImporte() {
		nuevoImporte = new ImporteEgreso();
	}
	public void nuevoRegistro() {
		nuevoEgreso = new egresos();
	}
	
	public void handleToggle(ToggleEvent event) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Toggled", "Visibility:" + event.getVisibility());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

	public void guardar() {
		PrimeFaces.current().executeInitScript("PF('dg-agregaConcepto').hide()");
		String msj = egresosDAO.guardar(nuevoEgreso);
		
		if(msj == null) {
			listaEgresos.clear();
			listaEgresos= egresosDAO.buscarTodos();
			FacesContext.getCurrentInstance().addMessage( null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Egreso registrado", null));
			PrimeFaces.current().ajax().update("form:messages","form:messages");
		}else {
			FacesContext.getCurrentInstance().addMessage( null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Egreso no registrado", msj));
			PrimeFaces.current().ajax().update("form:messages");
		}
		nuevoEgreso = new egresos();
	};

	public void guardarImporte() {
		
	}
	public egresos getNuevoEgreso() {
		return nuevoEgreso;
	}


	public void setNuevoEgreso(egresos nuevoEgreso) {
		this.nuevoEgreso = nuevoEgreso;
	}


	public CategoriaEgreso getCategoriaSelect() {
		return categoriaSelect;
	}

	public void setCategoriaSelect(CategoriaEgreso categoriaSelect) {
		this.categoriaSelect = categoriaSelect;
	}

	public TipoEgreso getTipoSelect() {
		return tipoSelect;
	}

	public void setTipoSelect(TipoEgreso tipoSelect) {
		this.tipoSelect = tipoSelect;
	}

	public List<CategoriaEgreso> getListaCatEgresos() {
		return listaCatEgresos;
	}

	public void setListaCatEgresos(List<CategoriaEgreso> listaCatEgresos) {
		this.listaCatEgresos = listaCatEgresos;
	}

	public List<TipoEgreso> getListaTipoEgresos() {
		return listaTipoEgresos;
	}

	public void setListaTipoEgresos(List<TipoEgreso> listaTipoEgresos) {
		this.listaTipoEgresos = listaTipoEgresos;
	}

	public List<EmisoresCFDIS> getListaEmisores() {
		return listaEmisores;
	}

	public void setListaEmisores(List<EmisoresCFDIS> listaEmisores) {
		this.listaEmisores = listaEmisores;
	}
	
	public ImporteEgreso getNuevoImporte() {
		return nuevoImporte;
	}

	public void setNuevoImporte(ImporteEgreso nuevoImporte) {
		this.nuevoImporte = nuevoImporte;
	}

	public List<egresos> getListaEgresos() {
		return listaEgresos;
	}

	public void setListaEgresos(List<egresos> listaEgresos) {
		this.listaEgresos = listaEgresos;
	}

	public String getImporte() {
		return importe;
	}

	public void setImporte(String importe) {
		this.importe = importe;
	}

	public Date getFechaActual() {
		return fechaActual;
	}

	public void setFechaActual(Date fechaActual) {
		this.fechaActual = fechaActual;
	}

	public List<ImporteEgreso> getListaImporteEgreso() {
		return listaImporteEgreso;
	}

	public void setListaImporteEgreso(List<ImporteEgreso> listaImporteEgreso) {
		this.listaImporteEgreso = listaImporteEgreso;
	}

	public ImporteEgreso getImporteSelected() {
		return importeSelected;
	}

	public void setImporteSelected(ImporteEgreso importeSelected) {
		this.importeSelected = importeSelected;
	}
	
	
	
}
