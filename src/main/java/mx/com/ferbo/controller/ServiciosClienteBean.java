package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.PrecioServicioDAO;
import mx.com.ferbo.dao.ServicioDAO;
import mx.com.ferbo.dao.UnidadManejoDAO;
import mx.com.ferbo.model.Aviso;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.PrecioServicio;
import mx.com.ferbo.model.Servicio;
import mx.com.ferbo.model.UnidadDeManejo;

@Named
@ViewScoped
public class ServiciosClienteBean implements Serializable {

	private static final long serialVersionUID = -5768146106301267486L;

	private List<Cliente> lstClientes;
	private List<UnidadDeManejo> lstUnidadManejo;
	private List<Servicio> lstServicio;
	private List<PrecioServicio> lstPrecioServicioFiltered;

	private Cliente clienteSelected;
	private PrecioServicio precioServicioSelected;

	private UnidadManejoDAO unidadManejoDAO;
	private ServicioDAO servicioDAO;
	private PrecioServicioDAO precioServicioDAO;

	private FacesContext faceContext;
	private HttpServletRequest request;

	public ServiciosClienteBean() {
		unidadManejoDAO = new UnidadManejoDAO();
		servicioDAO = new ServicioDAO();
		precioServicioDAO = new PrecioServicioDAO();
		lstPrecioServicioFiltered = new ArrayList<>();
	}

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		faceContext = FacesContext.getCurrentInstance();
		request = (HttpServletRequest) faceContext.getExternalContext().getRequest();

		lstClientes = (List<Cliente>) request.getSession(false).getAttribute("clientesActivosList");
		lstUnidadManejo = unidadManejoDAO.buscarTodos();
		lstServicio = servicioDAO.buscarTodos();
	}

	/**
	 * Método para filtrar del listado original por clave de cliente
	 */
	public void filtaListado() {
		lstPrecioServicioFiltered = precioServicioDAO.buscarPorCliente(clienteSelected.getCteCve(), false);
	}

	/**
	 * Método para filtrar del listado original por clave de cliente
	 */
	public void nuevoServicioCliente() {
		precioServicioSelected = new PrecioServicio();
		precioServicioSelected.setCliente(clienteSelected);
		precioServicioSelected.setServicio(new Servicio());
		precioServicioSelected.setUnidad(new UnidadDeManejo());
	}

	/**
	 * Método para guardar objeto tipo PrecioServicio
	 */
	public void guardarPrecioServicio() {
		// Guardar
		if (precioServicioSelected.getId() == null) {
			precioServicioSelected.setAvisoCve(new Aviso(1));
			if (precioServicioDAO.guardar(precioServicioSelected) == null) {
				lstPrecioServicioFiltered.add(precioServicioSelected);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Servicio Agregado"));
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error", "Ocurrió un error al intentar guardar el Servicio"));
			}
		} else {
			// Actualizar
			if (precioServicioDAO.actualizar(precioServicioSelected) == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Servicio Actualizado"));
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error", "Ocurrió un error al intentar actualizar el Servicio"));
			}
		}

		PrimeFaces.current().executeScript("PF('servicioClienteDialog').hide()");
		PrimeFaces.current().ajax().update("form:messages", "form:dt-servicios");
	}

	/**
	 * Método para eliminar objeto tipo PrecioServicio
	 */
	public void eliminarPrecioServicio() {
		if (precioServicioDAO.eliminar(precioServicioSelected) == null) {
			lstPrecioServicioFiltered.remove(this.precioServicioSelected);
			precioServicioSelected = null;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Servicio Eliminado"));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-servicios");
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
					"Ocurrió un error al intentar eliminar el Servicio"));
			PrimeFaces.current().ajax().update("form:messages");
		}
	}

	/**
	 * Getters & Setters
	 */
	public List<Cliente> getLstClientes() {
		return lstClientes;
	}

	public void setLstClientes(List<Cliente> lstClientes) {
		this.lstClientes = lstClientes;
	}

	public Cliente getClienteSelected() {
		return clienteSelected;
	}

	public void setClienteSelected(Cliente clienteSelected) {
		this.clienteSelected = clienteSelected;
	}

	public List<UnidadDeManejo> getLstUnidadManejo() {
		return lstUnidadManejo;
	}

	public void setLstUnidadManejo(List<UnidadDeManejo> lstUnidadManejo) {
		this.lstUnidadManejo = lstUnidadManejo;
	}

	public List<Servicio> getLstServicio() {
		return lstServicio;
	}

	public void setLstServicio(List<Servicio> lstServicio) {
		this.lstServicio = lstServicio;
	}

	public List<PrecioServicio> getLstPrecioServicioFiltered() {
		return lstPrecioServicioFiltered;
	}

	public void setLstPrecioServicioFiltered(List<PrecioServicio> lstPrecioServicioFiltered) {
		this.lstPrecioServicioFiltered = lstPrecioServicioFiltered;
	}

	public PrecioServicio getPrecioServicioSelected() {
		return precioServicioSelected;
	}

	public void setPrecioServicioSelected(PrecioServicio precioServicioSelected) {
		this.precioServicioSelected = precioServicioSelected;
	}

}
