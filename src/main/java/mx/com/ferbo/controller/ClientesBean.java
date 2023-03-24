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

import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.ClienteContactoDAO;
import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.dao.ContactoDAO;
import mx.com.ferbo.dao.MedioCntDAO;
import mx.com.ferbo.dao.TipoMailDAO;
import mx.com.ferbo.dao.TipoTelefonoDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ClienteContacto;
import mx.com.ferbo.model.Contacto;
import mx.com.ferbo.model.Mail;
import mx.com.ferbo.model.MedioCnt;
import mx.com.ferbo.model.Telefono;
import mx.com.ferbo.model.TipoMail;
import mx.com.ferbo.model.TipoTelefono;
import mx.com.ferbo.util.SecurityUtil;

@Named
@ViewScoped
public class ClientesBean implements Serializable {

	private static final long serialVersionUID = 8438449261015571241L;

	private List<Cliente> lstClientes;
	private List<Cliente> lstClientesSelected;
	private List<ClienteContacto> lstClienteContactoSelected;
	private List<TipoMail> lstTipoMail;

	private Cliente clienteSelected;
	private ClienteContacto clienteContactoSelected;
//	private Contacto contactoSelected;
	private MedioCnt medioContactoSelected;

	private ClienteDAO clienteDAO;
	private TipoMailDAO tipoMailDAO;
	
	SecurityUtil util;

	public ClientesBean() {
		lstClienteContactoSelected = new ArrayList<>();
		lstClientesSelected = new ArrayList<>();
		clienteDAO = new ClienteDAO();
		nuevoCliente();
//		contactoSelected = new Contacto();
		clienteContactoSelected = new ClienteContacto();
		medioContactoSelected = new MedioCnt();
		util = new SecurityUtil();
	}

	@PostConstruct
	public void init() {
		consultaClientes();
		consultaCatalogos();
	}

	private void consultaClientes() {
		lstClientes = clienteDAO.buscarTodos();
	}

	private void consultaCatalogos() {
		lstTipoMail = tipoMailDAO.buscarTodos();
	}

	/**
	 * Método para consultar mensaje de eliminación para botón eliminar varios
	 */
	public String getConsultaMensajeBtn() {
		if (clienteSeleccionado()) {
			int size = this.lstClientesSelected.size();
			return size > 1 ? size + " clientes seleccionados" : "1 cliente seleccionado";
		}
		return "Eliminar";
	}

	/**
	 * Método para inicializar objeto tipo Cliente
	 */
	public void nuevoCliente() {
		clienteSelected = new Cliente();
		clienteSelected.setClienteContactoList(new ArrayList<>());
	}

	/**
	 * Método para inicializar objeto tipo Contacto
	 */
	public void nuevoContacto(Cliente clienteSel) {
//		contactoSelected = new Contacto();
		clienteSelected = clienteSel;
		clienteContactoSelected = new ClienteContacto();
		clienteContactoSelected.setIdCliente(clienteSelected);
		medioContactoSelected = new MedioCnt();
	}

	/**
	 * Método para validar si se ha seleccionado uno o varios objetos tipo Cliente
	 */
	public boolean clienteSeleccionado() {
		return this.lstClientesSelected != null && !this.lstClientesSelected.isEmpty();
	}

	public void guardarCliente() {
		if (clienteSelected.getCteCve() == null) {
			if (clienteDAO.guardar(clienteSelected) == null) {
				lstClientes.add(clienteSelected);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Cliente Agregado"));
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error", "Ocurrió un error al intentar guardar el Cliente"));
			}
		} else {
			if (clienteDAO.actualizar(clienteSelected) == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Cliente Actualizado"));
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error", "Ocurrió un error al intentar actualizar el Cliente"));
			}
		}
		PrimeFaces.current().executeScript("PF('dialogCliente').hide()");
		PrimeFaces.current().ajax().update("form:messages", "form:dt-clientes");
	}

	public void eliminarCliente() {
		if (clienteDAO.eliminar(clienteSelected) == null) {
			lstClientes.remove(clienteSelected);
			clienteSelected = null;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Cliente Eliminado"));
			PrimeFaces.current().ajax().update("form:dt-clientes");
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
					"Ocurrió un error al intentar eliminar el Cliente"));
		}
		PrimeFaces.current().ajax().update("form:messages");
	}

	public void eliminarListaCliente() {
		if (clienteDAO.eliminarListado(lstClientesSelected) == null) {
			lstClientes.removeAll(lstClientesSelected);
			lstClientesSelected = null;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Clientes Eliminados"));
			PrimeFaces.current().ajax().update("form:dt-clientes");
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
					"Ocurrió un error al intentar eliminar los Clientes"));
		}
		PrimeFaces.current().ajax().update("form:messages");
	}


	public void consultaContactos(ClienteContacto clienteContacto) {
//		contactoSelected = contacto;
		clienteContactoSelected = clienteContacto;
		PrimeFaces.current().ajax().update("form:dialogEditContacto", "form:pnlEditContacto");
		PrimeFaces.current().executeScript("PF('dialogEditContacto').show();");

	}
	
	public void generaPassword() {
		clienteContactoSelected.setNbPassword(util.getRandomString());
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

	public List<Cliente> getLstClientesSelected() {
		return lstClientesSelected;
	}

	public void setLstClientesSelected(List<Cliente> lstClientesSelected) {
		this.lstClientesSelected = lstClientesSelected;
	}

	public List<ClienteContacto> getLstClienteContactoSelected() {
		return lstClienteContactoSelected;
	}

	public void setLstClienteContactoSelected(List<ClienteContacto> lstClienteContactoSelected) {
		this.lstClienteContactoSelected = lstClienteContactoSelected;
	}

	public Cliente getClienteSelected() {
		return clienteSelected;
	}

	public void setClienteSelected(Cliente clienteSelected) {
		this.clienteSelected = clienteSelected;
	}

	public ClienteContacto getClienteContactoSelected() {
		return clienteContactoSelected;
	}

	public void setClienteContactoSelected(ClienteContacto clienteContactoSelected) {
		this.clienteContactoSelected = clienteContactoSelected;
	}

	public List<TipoMail> getLstTipoMail() {
		return lstTipoMail;
	}

	public void setLstTipoMail(List<TipoMail> lstTipoMail) {
		this.lstTipoMail = lstTipoMail;
	}

	public MedioCnt getMedioContactoSelected() {
		return medioContactoSelected;
	}

	public void setMedioContactoSelected(MedioCnt medioContactoSelected) {
		this.medioContactoSelected = medioContactoSelected;
	}

}
