package mx.com.ferbo.model;

import java.util.List;

import mx.com.ferbo.dao.CamaraDAO;
import mx.com.ferbo.dao.PlantaDAO;
import mx.com.ferbo.dao.PosicionCamaraDAO;

public class InventarioDetalle extends Inventario {
	private List<Planta> listaplanta;
	private List<Camara> listacamara;
	private List<Posicion> listaposicion;

	
	
	public InventarioDetalle() {

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

}
