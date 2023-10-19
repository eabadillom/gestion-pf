package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.BancoDAO;
import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.dao.PagoDAO;
import mx.com.ferbo.dao.TipoPagoDAO;
import mx.com.ferbo.model.Bancos;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.Pago;
import mx.com.ferbo.model.TipoPago;

@Named
@ViewScoped

public class IngresosActualizacionBean implements Serializable{

	private static final long serialVersionUID = -626048119540963939L;
	private static Logger log = LogManager.getLogger(IngresosActualizacionBean.class);
	
	private Date startDate;
	private Date endDate;	
	
	private PagoDAO pagofactDAO;
	private ClienteDAO clienteDAO;
	private TipoPagoDAO tipoPagoDAO;
	private BancoDAO bancoDAO;
	
	
	private List<Pago> listaPago;
	private List<Cliente> listaCtes;
	private List<TipoPago> listatipoPago;
	private List<Bancos> listaBancos;
	
	private Pago pagoSelected;
	private Cliente cteSelect;
	
	public IngresosActualizacionBean() {
		clienteDAO = new ClienteDAO();
		pagofactDAO = new PagoDAO();
		tipoPagoDAO = new TipoPagoDAO();
		bancoDAO = new BancoDAO();
		
		listaPago = new ArrayList<Pago>();
		listaCtes = new ArrayList<Cliente>();
		
		pagoSelected = new Pago();
		cteSelect = new Cliente();
		
		
	}
	
	@PostConstruct
	public void init(){
		
		listaCtes = clienteDAO.findall();
		listaBancos = bancoDAO.buscarTodos();
		listatipoPago = tipoPagoDAO.buscarTodos();
		this.startDate = new Date();
		this.endDate = new Date();
		
	}
	
	
	public void updatePago() {
		
		System.out.println(pagoSelected);
		
		String messages = null;
		Severity severity = null;		
		
		try {
			
			if(pagofactDAO.actualizar(pagoSelected)==null) {
				severity = FacesMessage.SEVERITY_INFO;
				messages = "El pago fue actualizado";
			}
			
		} catch (Exception e) {
			severity = FacesMessage.SEVERITY_ERROR;
			messages = "Error al actualizar pago";
			log.error(e.getMessage());
		}
		
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Actualización", messages));
		PrimeFaces.current().ajax().update("form:messages");
	}
	
	public void deletePago() {
		
		String mensaje = null;
		Severity severity = null;
		
		try {
			
			if(pagofactDAO.eliminar(pagoSelected)==null) {
				mensaje = "El pago fue eliminado correctamente";
				severity = FacesMessage.SEVERITY_INFO;
				filtraPagos();
			}
			
		} catch (Exception e) {
			mensaje = "Ocurrio un error al querer eliminar el pago";
			severity = FacesMessage.SEVERITY_ERROR;
			log.error(e.getMessage());
		}
		
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Eliminar", mensaje));
		PrimeFaces.current().ajax().update("form:messages");
	}
	
	public void filtraPagos() {
		
		listaPago = pagofactDAO.buscaPorClienteFechas(cteSelect, startDate, endDate);
		
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public List<Pago> getListaPago() {
		return listaPago;
	}

	public void setListaPago(List<Pago> listaPago) {
		this.listaPago = listaPago;
	}

	public Pago getPagoSelected() {
		return pagoSelected;
	}

	public void setPagoSelected(Pago pagoSelected) {
		this.pagoSelected = pagoSelected;
	}

	public Cliente getCteSelect() {
		return cteSelect;
	}

	public void setCteSelect(Cliente cteSelect) {
		this.cteSelect = cteSelect;
	}

	public List<Cliente> getListaCtes() {
		return listaCtes;
	}

	public void setListaCtes(List<Cliente> listaCtes) {
		this.listaCtes = listaCtes;
	}

	public List<TipoPago> getListatipoPago() {
		return listatipoPago;
	}

	public void setListatipoPago(List<TipoPago> listatipoPago) {
		this.listatipoPago = listatipoPago;
	}

	public List<Bancos> getListaBancos() {
		return listaBancos;
	}

	public void setListaBancos(List<Bancos> listaBancos) {
		this.listaBancos = listaBancos;
	}
	
	
	
	
}
