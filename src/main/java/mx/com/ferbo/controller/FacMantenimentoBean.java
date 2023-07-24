package mx.com.ferbo.controller;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.dao.FacMantenimientoDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.Factura;

@Named
@ViewScoped
public class FacMantenimentoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Cliente> listClientes;
	private ClienteDAO daoCliente;
	private Cliente clienteSelect;

	private List<Factura> listFac;
	private FacMantenimientoDAO daoFac;
	private Factura seleccion;

	private Date de;
	private Date hasta;

	public FacMantenimentoBean() {
		seleccion = new Factura();
		daoCliente = new ClienteDAO();
		daoFac = new FacMantenimientoDAO();
		listClientes = daoCliente.buscarTodos();
		listFac = new ArrayList<Factura>();

		hasta = GregorianCalendar.getInstance().getTime();
	};

	public void findFacture() {
		listFac = daoFac.findDacturas(clienteSelect);
	};

	public void cancelFacture() {
		PrimeFaces.current().executeScript("PF('dg-delete').hide()");
		String message = daoFac.update(seleccion);

		if (message == null) {
			listFac.clear();
			listFac = daoFac.findDacturas(clienteSelect);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Factura " + seleccion.getNumero() + " cancelada", null));
			PrimeFaces.current().ajax().update("form:messages", "form:dtSerieFac");
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al cancelar la factura", message));
			PrimeFaces.current().ajax().update("form:messages");
		}
		seleccion = new Factura();
	};

	public List<Cliente> getListClientes() {
		return listClientes;
	};

	public void setListClientes(List<Cliente> listClientes) {
		this.listClientes = listClientes;
	};

	public Cliente getClienteSelect() {
		return clienteSelect;
	};

	public void setClienteSelect(Cliente clienteSelect) {
		this.clienteSelect = clienteSelect;
	};

	public List<Factura> getListFac() {
		return listFac;
	};

	public void setListFac(List<Factura> listFac) {
		this.listFac = listFac;
	};

	public Factura getSeleccion() {
		return seleccion;
	};

	public void setSeleccion(Factura seleccion) {
		this.seleccion = seleccion;
	};

	public Date getDe() {
		return de;
	};

	public void setDe(Date de) {
		this.de = de;
	};

	public Date getHasta() {
		return hasta;
	};

	public void setHasta(Date hasta) {
		this.hasta = hasta;
	};

}
