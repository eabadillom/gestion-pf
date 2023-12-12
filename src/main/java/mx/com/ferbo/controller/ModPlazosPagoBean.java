package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.ModPlazosPagoDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.Factura;

@Named
@ViewScoped
public class ModPlazosPagoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Cliente> listClientes;
	private Cliente clienteSelect;

	private List<Factura> listFac;
	private ModPlazosPagoDAO daoFac;
	private List<Factura> facSelect;

	private int modNumber;

	private Date actual = GregorianCalendar.getInstance().getTime();
	private Date de;
	private Date hasta;
	
	private FacesContext faceContext;
    private HttpServletRequest request;

	public ModPlazosPagoBean() {
		modNumber = 1;
		daoFac = new ModPlazosPagoDAO();
		
		facSelect = new ArrayList<Factura>();
	};
	
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		faceContext = FacesContext.getCurrentInstance();
        request = (HttpServletRequest) faceContext.getExternalContext().getRequest();
		
		listClientes = (List<Cliente>) request.getSession(false).getAttribute("clientesActivosList");
		de = new Date();
		hasta = new Date();
	}
	
	public boolean hasClient() {
		return this.clienteSelect != null;
	};

	public void findFacture() {
		listFac = daoFac.findDacturas(clienteSelect, de, hasta);
	};

	public boolean hasSelected() {
		return this.facSelect != null && !this.facSelect.isEmpty();
	};

	public String getModBoton() {
		int size = this.facSelect.size();
		return "(" + size + ") Modificar";
	};

	public void update() {
		String message = null;
		for(Factura factura : facSelect) {
			message = daoFac.updatePlazo(factura, modNumber);
		}

		if (message == null) {
			listFac.clear();
			listFac = daoFac.findDacturas(clienteSelect, de, hasta);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Se modificaron plazos de pago", null));
			PrimeFaces.current().ajax().update("form:messages", "form:dtSerieFac");
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al cmodificar plazos", message));
			PrimeFaces.current().ajax().update("form:messages");
		}
		facSelect = new ArrayList<Factura>();
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

	public int getModNumber() {
		return modNumber;
	};

	public void setModNumber(int modNumber) {
		this.modNumber = modNumber;
	};

	public List<Factura> getFacSelect() {
		return facSelect;
	};

	public void setFacSelect(List<Factura> facSelect) {
		this.facSelect = facSelect;
	};

	public Date getActual() {
		return actual;
	};

	public void setActual(Date actual) {
		this.actual = actual;
	};

	public Date getDe() {
		return de;
	};

	public void setDe(Date de) {
		if (de.compareTo(hasta) > 0) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Fecha Invalida", "La fecha seleccionada debe ser menor a la fecha final"));
			PrimeFaces.current().ajax().update("form:messages");
			this.de = this.actual;
			this.hasta = this.actual;
		} else {
			this.de = de;
		}
	};

	public Date getHasta() {
		return hasta;
	};

	public void setHasta(Date hasta) {
		if (de.compareTo(hasta) > 0) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Fecha Invalida", "La fecha seleccionada debe ser mayor a la fecha inicial"));
			PrimeFaces.current().ajax().update("form:messages");
			this.de = this.actual;
			this.hasta = this.actual;
		} else {
			this.hasta = hasta;
		}
	};

}
