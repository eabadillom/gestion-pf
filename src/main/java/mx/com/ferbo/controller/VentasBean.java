package mx.com.ferbo.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.dao.EmisoresCFDISDAO;
import mx.com.ferbo.dao.ParametroDAO;
import mx.com.ferbo.dao.VentaDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.EmisoresCFDIS;
import mx.com.ferbo.model.Parametro;
import mx.com.ferbo.model.Ventas;
import mx.com.ferbo.util.FormatUtil;

@Named
@ViewScoped
public class VentasBean implements Serializable {

	private static final long serialVersionUID = -1785488265380235016L;

	private VentaDAO ventaDAO;
	private List<Ventas> listVenta;
	private Ventas venta;

	private ClienteDAO clienteDAO;
	private List<Cliente> listCliente;
	private EmisoresCFDISDAO emisorDAO;
	private List<EmisoresCFDIS> listEmisores;
	
	private BigDecimal total;
	private String montoLetra;
	private boolean ieps;
	
	
	public VentasBean() {
		
		clienteDAO = new ClienteDAO();
		listCliente = new ArrayList<>();
		
		emisorDAO = new EmisoresCFDISDAO();
		listEmisores = new ArrayList<>();
		total = new BigDecimal(0);
		ventaDAO = new VentaDAO();
		listVenta = new ArrayList<>();
		venta = new Ventas();

	}

	@PostConstruct
	public void init() {

		listCliente = clienteDAO.findall();
		listEmisores = emisorDAO.findall(false);
		listVenta = ventaDAO.buscarTodos();
	}
	
	public void save() {
		
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;	
		FormatUtil formato = new FormatUtil();
		
		
		try {			

			//porcentajeSubtotalIva = venta.getSubtotal().multiply(new BigDecimal(p.getValor()));
			//porcentajeSubtotalieps = venta.getSubtotal().multiply((venta.getIeps()));
			
			//total = porcentajeSubtotalIva.add(porcentajeSubtotalieps).add(venta.getSubtotal());
			venta.setFecha(new Date());
			//venta.setIva(porcentajeSubtotalIva);
			//venta.setIeps(porcentajeSubtotalieps);
			//venta.setTotal(total);
			venta.setMontoLetra(formato.getMontoConLetra(venta.getTotal().doubleValue()));
			
			if(ventaDAO.guardar(venta)==null) {
				
				mensaje = "Se guardo con exito"; 
				severity = FacesMessage.SEVERITY_INFO;		

			}else {
				
				mensaje = "Error al guardar registro"; 
				severity = FacesMessage.SEVERITY_ERROR;
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			
			message = new FacesMessage(severity, "Registro venta", mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update(":form:messages",":form:total");
		}
		
	}
	
	public void newVenta() {
		venta = new Ventas();
		
		PrimeFaces.current().ajax().update("form:frm-agregaVenta");		
	}
	

	public List<Ventas> getListVenta() {
		return listVenta;
	}

	public void setListVenta(List<Ventas> listVenta) {
		this.listVenta = listVenta;
	}

	public List<Cliente> getListCliente() {
		return listCliente;
	}

	public void setListCliente(List<Cliente> listCliente) {
		this.listCliente = listCliente;
	}

	public List<EmisoresCFDIS> getListEmisores() {
		return listEmisores;
	}

	public void setListEmisores(List<EmisoresCFDIS> listEmisores) {
		this.listEmisores = listEmisores;
	}

	public Ventas getVenta() {
		return venta;
	}

	public void setVenta(Ventas venta) {
		this.venta = venta;
	}

	public String getMontoLetra() {
		return montoLetra;
	}

	public void setMontoLetra(String montoLetra) {
		this.montoLetra = montoLetra;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public boolean isIeps() {
		return ieps;
	}

	public void setIeps(boolean ieps) {
		this.ieps = ieps;
	}

}
