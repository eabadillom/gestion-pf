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
	private List<TipoTelefono> lstTipoTelefono;

	private Cliente clienteSelected;
	private ClienteContacto clienteContactoSelected;
//	private Contacto contactoSelected;
	private MedioCnt medioContactoSelected;

	private ClienteDAO clienteDAO;
	private ClienteContactoDAO clienteContactoDAO;
	private ContactoDAO contactoDAO;
	private TipoMailDAO tipoMailDAO;
	private TipoTelefonoDAO tipoTelefonoDAO;
	private MedioCntDAO medioCntDAO;
	
	SecurityUtil util;

	public ClientesBean() {
		lstClienteContactoSelected = new ArrayList<>();
		lstClientesSelected = new ArrayList<>();
		clienteDAO = new ClienteDAO();
		clienteContactoDAO = new ClienteContactoDAO();
		contactoDAO = new ContactoDAO();
		tipoMailDAO = new TipoMailDAO();
		tipoTelefonoDAO = new TipoTelefonoDAO();
		medioCntDAO = new MedioCntDAO();
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
		lstTipoTelefono = tipoTelefonoDAO.buscarTodos();
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

	public void guardarContacto() {
		if (clienteContactoSelected.getId() == null) {
			if (clienteContactoDAO.guardar(clienteContactoSelected) == null) {
				clienteContactoSelected.setNbPassword(util.getSHA512(clienteContactoSelected.getNbPassword()));
				clienteSelected.getClienteContactoList().add(clienteContactoSelected);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Contacto Agregado"));
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error", "Ocurrió un error al intentar guardar el Contacto"));
			}
			PrimeFaces.current().executeScript("PF('dialogAddContacto').hide()");
		} else {
			if (clienteContactoDAO.actualizar(clienteContactoSelected) == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Contacto Actualizado"));
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error", "Ocurrió un error al intentar actualizar el Contacto"));
			}
			PrimeFaces.current().executeScript("PF('dialogEditContacto').hide()");
		}
		PrimeFaces.current().ajax().update("form:messages", "form:dt-clientes");
	}

	public void eliminarClienteContacto() {
		if (clienteContactoDAO.eliminar(clienteContactoSelected) == null) {
			clienteSelected.getClienteContactoList().remove(clienteContactoSelected);
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
					"Ocurrió un error al intentar eliminar el Contacto"));
		}
		PrimeFaces.current().ajax().update("form:messages", "form:dt-clientes", "form:dt-clientes:dtContactos");

	}

	public void consultaContactos(ClienteContacto clienteContacto) {
//		contactoSelected = contacto;
		clienteContactoSelected = clienteContacto;
		PrimeFaces.current().ajax().update("form:dialogEditContacto", "form:pnlEditContacto");
		PrimeFaces.current().executeScript("PF('dialogEditContacto').show();");

	}

	public void nuevoMedio() {
		medioContactoSelected = new MedioCnt();
		medioContactoSelected.setIdMail(new Mail());
		medioContactoSelected.getIdMail().setTpMail(new TipoMail());
		medioContactoSelected.setIdTelefono(new Telefono());
		medioContactoSelected.getIdTelefono().setTpTelefono(new TipoTelefono());
	}

	public void modificaMedio(MedioCnt medio) {
		medioContactoSelected = medio;
	}

	public void guardaMedioContacto() {
		if (medioContactoSelected.getIdMedio() == null) {
			medioContactoSelected.setIdContacto(clienteContactoSelected.getIdContacto());
			if (medioCntDAO.guardaMedioCnt(medioContactoSelected) == null) {
				if(clienteContactoSelected.getIdContacto().getMedioCntList() == null) {
					clienteContactoSelected.getIdContacto().setMedioCntList(new ArrayList<>());
				}
				clienteContactoSelected.getIdContacto().getMedioCntList().add(medioContactoSelected);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Medio de contacto Agregado"));
			}else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error", "Ocurrió un error al intentar guardar el medio de contacto"));
			}

		}else {
			if(medioCntDAO.actualizar(medioContactoSelected) == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Medio de contacto Actualizado"));
			}else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error", "Ocurrió un error al intentar actualizar el medio de contacto"));
			}
		}
		PrimeFaces.current().executeScript("PF('dialogMedioContacto').hide();");
		PrimeFaces.current().ajax().update("form:messages", "form:pnlEditContacto");
	}
	
	
	public void eliminarMedioContacto() {
		if(medioCntDAO.eliminar(medioContactoSelected) == null) {
			clienteContactoSelected.getIdContacto().getMedioCntList().remove(medioContactoSelected);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Medio de contacto Eliminado"));
			
		}else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error", "Ocurrió un error al intentar eliminar el medio de contacto"));
		}
		PrimeFaces.current().ajax().update("form:messages", "form:pnlEditContacto");
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

	public List<TipoTelefono> getLstTipoTelefono() {
		return lstTipoTelefono;
	}

	public void setLstTipoTelefono(List<TipoTelefono> lstTipoTelefono) {
		this.lstTipoTelefono = lstTipoTelefono;
	}

	public MedioCnt getMedioContactoSelected() {
		return medioContactoSelected;
	}

	public void setMedioContactoSelected(MedioCnt medioContactoSelected) {
		this.medioContactoSelected = medioContactoSelected;
	}

}
