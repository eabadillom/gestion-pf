package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.dao.MedioPagoDAO;
import mx.com.ferbo.dao.MetodoPagoDAO;
import mx.com.ferbo.dao.RegimenFiscalDAO;
import mx.com.ferbo.dao.TipoMailDAO;
import mx.com.ferbo.dao.UsoCfdiDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ClienteContacto;
import mx.com.ferbo.model.MedioCnt;
import mx.com.ferbo.model.MedioPago;
import mx.com.ferbo.model.MetodoPago;
import mx.com.ferbo.model.RegimenFiscal;
import mx.com.ferbo.model.TipoMail;
import mx.com.ferbo.model.UsoCfdi;
import mx.com.ferbo.util.ClienteUtil;
import mx.com.ferbo.util.InventarioException;
import mx.com.ferbo.util.SecurityUtil;

@Named
@ViewScoped
public class ClientesBean implements Serializable {

	private static final long serialVersionUID = 8438449261015571241L;
	private static Logger log = Logger.getLogger(ClientesBean.class);

	private List<Cliente> lstClientes;
	private List<Cliente> lstClientesSelected;
	private List<ClienteContacto> lstClienteContactoSelected;
	private List<TipoMail> lstTipoMail;
	private List<RegimenFiscal> lstRegimenFiscal;
	private List<UsoCfdi> lstUsoCfdi;
	private List<MetodoPago> lstMetodoPago;
	private List<MedioPago> lstMedioPago; //lista forma de pago
	
	private Cliente clienteSelected;
	private ClienteContacto clienteContactoSelected;
	private MedioCnt medioContactoSelected;
	private String cdRegimenFiscalSelected;
	private String cdUsoCfdiSelected;
	private String cdMetodoPagoSelected;
	private Integer idMedioPagoSelected;
	

	private ClienteDAO clienteDAO;
	private TipoMailDAO tipoMailDAO;
	private RegimenFiscalDAO regimenFiscalDAO;
	private UsoCfdiDAO usoCfdiDAO;
	private MetodoPagoDAO metodoPagoDAO;
	private MedioPagoDAO medioPagoDAO;
	
	SecurityUtil util;

	public ClientesBean() {
		lstClienteContactoSelected = new ArrayList<>();
		lstClientesSelected = new ArrayList<>();
		clienteDAO = new ClienteDAO();
		tipoMailDAO = new TipoMailDAO();
		nuevoCliente();
		clienteContactoSelected = new ClienteContacto();
		medioContactoSelected = new MedioCnt();
		util = new SecurityUtil();
		regimenFiscalDAO = new RegimenFiscalDAO();
		usoCfdiDAO = new UsoCfdiDAO();
		metodoPagoDAO = new MetodoPagoDAO();
		medioPagoDAO = new MedioPagoDAO();
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
		this.lstTipoMail = tipoMailDAO.buscarTodos();
		this.lstMetodoPago = metodoPagoDAO.buscarVigentes(new Date());
		this.lstMedioPago = medioPagoDAO.buscarVigentes(new Date());
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
	
	public void cargaInfoCliente() {
		
		log.info("Cargando información del cliente: " + this.clienteSelected);
		if("M".equals(clienteSelected.getTipoPersona())) {
			lstRegimenFiscal = regimenFiscalDAO.buscarPorPersonaMoral();
			lstUsoCfdi = usoCfdiDAO.buscaPorPersonaMoral();
		}
		if("F".equals(clienteSelected.getTipoPersona())) {
			lstRegimenFiscal = regimenFiscalDAO.buscarPorPersonaFisica();
			lstUsoCfdi = usoCfdiDAO.buscaPorPersonaFisica();
		}
		
		this.cdRegimenFiscalSelected = null;
		if(this.clienteSelected.getRegimenFiscal() != null) {
			this.cdRegimenFiscalSelected = this.clienteSelected.getRegimenFiscal().getCd_regimen();
		}
		
		this.cdUsoCfdiSelected = null;
		if(this.clienteSelected.getUsoCfdi() != null) {
			this.cdUsoCfdiSelected = this.clienteSelected.getUsoCfdi().getCdUsoCfdi();
		}
		
		if(this.clienteSelected.getMetodoPago() != null) {
			this.cdMetodoPagoSelected = this.clienteSelected.getMetodoPago().getCdMetodoPago();
		}
		
		this.idMedioPagoSelected = null;
		if(this.clienteSelected.getFormaPago() != null) {
			
			final String fp = this.clienteSelected.getFormaPago();
			
			List<MedioPago> lst = this.lstMedioPago.stream().filter(c -> fp.equals(c.getFormaPago()))
					.collect(Collectors.toList());
			if(lst != null && lst.size() > 0) {
				this.idMedioPagoSelected = lst.get(0).getMpId();
			}
		}
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
		final String rf = this.cdRegimenFiscalSelected;
		List<RegimenFiscal> lstRF = lstRegimenFiscal.stream().filter(r -> r.getCd_regimen().equals(rf))
			.collect(Collectors.toList());
		
		if(lstRF != null && lstRF.size() > 0) {
			this.clienteSelected.setRegimenFiscal(lstRF.get(0));
		}
		
		final String uso = this.cdUsoCfdiSelected;
		List<UsoCfdi> lstUsoCFDITmp = this.lstUsoCfdi.stream().filter(u -> uso.equals(uso))
			.collect(Collectors.toList());
		
		if(lstUsoCFDITmp != null && lstUsoCFDITmp.size() > 0) {
			this.clienteSelected.setUsoCfdi(lstUsoCFDITmp.get(0));
		}
		
		final String mp = this.cdMetodoPagoSelected;
		List<MetodoPago> lstMetodoPagoTmp = this.lstMetodoPago.stream().filter(m -> m.getCdMetodoPago().equals(mp))
			.collect(Collectors.toList());
		if(lstMetodoPagoTmp != null && lstMetodoPagoTmp.size() > 0) {
			this.clienteSelected.setMetodoPago(lstMetodoPagoTmp.get(0));
		}
		
		final Integer idMPTmp = this.idMedioPagoSelected;
		List<MedioPago> lstMedioPagoTmp = this.lstMedioPago.stream().filter(m -> m.getMpId() == idMPTmp)
			.collect(Collectors.toList());
		if(lstMedioPagoTmp != null && lstMedioPagoTmp.size() > 0) {
			this.clienteSelected.setFormaPago(lstMedioPagoTmp.get(0).getFormaPago());			
		}
		
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
		clienteContactoSelected = clienteContacto;
		
		PrimeFaces.current().ajax().update("form:dialogEditContacto", "form:pnlEditContacto");
		PrimeFaces.current().executeScript("PF('dialogEditContacto').show();");

	}
	
	public void generaPassword() {
		clienteContactoSelected.setNbPassword(util.getRandomString());
	}
	
	public void regimenSelect() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		
		try {
			
			if(ClienteUtil.validarRFC(this.clienteSelected.getTipoPersona(), this.clienteSelected.getCteRfc()) == false ) {
				mensaje = "El RFC es incorrecto";
				throw new InventarioException("El RFC es incorrecto");
			}
			
			if("M".equals(this.clienteSelected.getTipoPersona())) {	
				lstRegimenFiscal = regimenFiscalDAO.buscarPorPersonaMoral();
				lstUsoCfdi = usoCfdiDAO.buscaPorPersonaMoral();
				log.info(String.format("Lista de regimenes para personas morales: (%d) ", lstRegimenFiscal.size()));
			}else if("F".equals(this.clienteSelected.getTipoPersona())) {
				lstRegimenFiscal = regimenFiscalDAO.buscarPorPersonaFisica();
				lstUsoCfdi = usoCfdiDAO.buscaPorPersonaFisica();
				log.info(String.format("Lista de regimenes para personas físicas: (%d) ", lstRegimenFiscal.size()));
			}
			severity = FacesMessage.SEVERITY_INFO;
			mensaje = "Seleccione el régimen fiscal y uso del CFDI.";
			
		} catch (InventarioException ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
		} catch (Exception ex) {
			mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			message = new FacesMessage(severity, "Catálogo de clientes", mensaje);
	        FacesContext.getCurrentInstance().addMessage(null, message);
	        PrimeFaces.current().ajax().update(":form:messages");
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

	public List<RegimenFiscal> getLstRegimenFiscal() {
		return lstRegimenFiscal;
	}

	public void setLstRegimenFiscal(List<RegimenFiscal> lstRegimenFiscal) {
		this.lstRegimenFiscal = lstRegimenFiscal;
	}

	public List<UsoCfdi> getLstUsoCfdi() {
		return lstUsoCfdi;
	}

	public void setLstUsoCfdi(List<UsoCfdi> lstUsoCfdi) {
		this.lstUsoCfdi = lstUsoCfdi;
	}

	public List<MetodoPago> getLstMetodoPago() {
		return lstMetodoPago;
	}

	public void setLstMetodoPago(List<MetodoPago> lstMetodoPago) {
		this.lstMetodoPago = lstMetodoPago;
	}

	public List<MedioPago> getLstMedioPago() {
		return lstMedioPago;
	}

	public void setLstMedioPago(List<MedioPago> lstMedioPago) {
		this.lstMedioPago = lstMedioPago;
	}

	public String getCdRegimenFiscalSelected() {
		return cdRegimenFiscalSelected;
	}

	public void setCdRegimenFiscalSelected(String cdRegimenFiscalSelected) {
		this.cdRegimenFiscalSelected = cdRegimenFiscalSelected;
	}

	public String getCdUsoCfdiSelected() {
		return cdUsoCfdiSelected;
	}

	public void setCdUsoCfdiSelected(String cdUsoCfdiSelected) {
		this.cdUsoCfdiSelected = cdUsoCfdiSelected;
	}

	public String getCdMetodoPagoSelected() {
		return cdMetodoPagoSelected;
	}

	public void setCdMetodoPagoSelected(String cdMetodoPagoSelected) {
		this.cdMetodoPagoSelected = cdMetodoPagoSelected;
	}

	public Integer getIdMedioPagoSelected() {
		return idMedioPagoSelected;
	}

	public void setIdMedioPagoSelected(Integer idMedioPagoSelected) {
		this.idMedioPagoSelected = idMedioPagoSelected;
	}

}
