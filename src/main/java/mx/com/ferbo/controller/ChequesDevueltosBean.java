package mx.com.ferbo.controller;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import mx.com.ferbo.dao.ChequesDevueltosDAO;
import mx.com.ferbo.model.ChequeDevuelto;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.Factura;

@Named
@ViewScoped
public class ChequesDevueltosBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private ChequesDevueltosDAO result;
	private List<ChequeDevuelto> chequesDevueltos;
	
	private Cliente clienteSelect;
	private List<Factura> listFac;
	
	


	public ChequesDevueltosBean() {
		result = new ChequesDevueltosDAO();
		this.chequesDevueltos = result.findAll();
	}
	
	public void findFactura() {
		listFac = result.findDacturas(clienteSelect);
	}


	public List<ChequeDevuelto> getAll() {
		return result.findAll();
	}
	
	public void statusFactura() {
		
	}


	public ChequesDevueltosDAO getResult() {
		return result;
	}


	public void setResult(ChequesDevueltosDAO result) {
		this.result = result;
	}


	public List<ChequeDevuelto> getChequesDevueltos() {
		return chequesDevueltos;
	}


	public void setChequesDevueltos(List<ChequeDevuelto> chequesDevueltos) {
		this.chequesDevueltos = chequesDevueltos;
	}


	public Cliente getClienteSelect() {
		return clienteSelect;
	}


	public void setClienteSelect(Cliente clienteSelect) {
		this.clienteSelect = clienteSelect;
	}

	public List<Factura> getListFac() {
		return listFac;
	}

	public void setListFac(List<Factura> listFac) {
		this.listFac = listFac;
	}
	
	
	
	
	
	

	


	
}
