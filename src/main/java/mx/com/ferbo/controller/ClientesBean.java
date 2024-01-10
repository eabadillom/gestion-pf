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
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.ClienteContactoDAO;
import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.dao.MedioCntDAO;
import mx.com.ferbo.dao.MedioPagoDAO;
import mx.com.ferbo.dao.MetodoPagoDAO;
import mx.com.ferbo.dao.PlantaDAO;
import mx.com.ferbo.dao.RegimenFiscalDAO;
import mx.com.ferbo.dao.TipoMailDAO;
import mx.com.ferbo.dao.TipoTelefonoDAO;
import mx.com.ferbo.dao.UsoCfdiDAO;
import mx.com.ferbo.model.CandadoSalida;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ClienteContacto;
import mx.com.ferbo.model.Contacto;
import mx.com.ferbo.model.Mail;
import mx.com.ferbo.model.MedioCnt;
import mx.com.ferbo.model.MedioPago;
import mx.com.ferbo.model.MetodoPago;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.RegimenFiscal;
import mx.com.ferbo.model.SerieConstancia;
import mx.com.ferbo.model.SerieConstanciaPK;
import mx.com.ferbo.model.Telefono;
import mx.com.ferbo.model.TipoMail;
import mx.com.ferbo.model.TipoTelefono;
import mx.com.ferbo.model.UsoCfdi;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.util.ClienteUtil;
import mx.com.ferbo.util.InventarioException;
import mx.com.ferbo.util.SecurityUtil;

@Named
@ViewScoped
public class ClientesBean implements Serializable {

	private static final long serialVersionUID = 8438449261015571241L;
	private static Logger log = LogManager.getLogger(ClientesBean.class);

	private List<Cliente> lstClientes;
	private List<Cliente> lstClientesSelected;
	private List<ClienteContacto> lstClienteContactoSelected;
	private List<TipoMail> lstTipoMail;
	private List<TipoTelefono> lstTipoTelefono;
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
	private TipoTelefonoDAO tipoTelefonoDAO;
	private RegimenFiscalDAO regimenFiscalDAO;
	private UsoCfdiDAO usoCfdiDAO;
	private MetodoPagoDAO metodoPagoDAO;
	private MedioPagoDAO medioPagoDAO;
	private ClienteContactoDAO clienteContactoDAO;
	private MedioCntDAO medioCntDAO;
	private String newPassword;
	private String confirmPassword;
	private PlantaDAO plantaDAO;
	private Usuario usuario;
	private FacesContext faceContext;
    private HttpServletRequest httpServletRequest;
	
	
	SecurityUtil util;

	public ClientesBean() {
		lstClienteContactoSelected = new ArrayList<>();
		lstClientesSelected = new ArrayList<>();
		lstMetodoPago = new ArrayList<>();
		
		clienteDAO = new ClienteDAO();
		tipoMailDAO = new TipoMailDAO();
		tipoTelefonoDAO = new TipoTelefonoDAO();
		nuevoCliente();
		clienteContactoSelected = new ClienteContacto();
		medioContactoSelected = new MedioCnt();
		util = new SecurityUtil();
		regimenFiscalDAO = new RegimenFiscalDAO();
		usoCfdiDAO = new UsoCfdiDAO();
		metodoPagoDAO = new MetodoPagoDAO();
		medioPagoDAO = new MedioPagoDAO();
		clienteContactoDAO = new ClienteContactoDAO();
		medioCntDAO = new MedioCntDAO();
		plantaDAO = new PlantaDAO();
	}

	@PostConstruct
	public void init() {
		faceContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        this.usuario = (Usuario) httpServletRequest.getSession(true).getAttribute("usuario");
		
        log.info("El usuario {} ingresa al catálogo de clientes.", usuario.getUsuario());
        
		consultaClientes();
		consultaCatalogos();
		log.info("");
	}

	private void consultaClientes() {
		lstClientes = clienteDAO.buscarTodos(true);
	}

	private void consultaCatalogos() {
		this.lstTipoMail = tipoMailDAO.buscarTodos();
		this.lstTipoTelefono = tipoTelefonoDAO.buscarTodos();
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
		clienteSelected.setHabilitado(true);
		clienteSelected.setClienteContactoList(new ArrayList<>());
		this.cdRegimenFiscalSelected = null;
		this.cdUsoCfdiSelected = null;
		this.cdMetodoPagoSelected = null;
		this.idMedioPagoSelected = null;
	}

	/**
	 * Método para inicializar objeto tipo Contacto
	 */
	public void nuevoContacto(Cliente clienteSel) {
		clienteContactoSelected = new ClienteContacto();
		clienteContactoSelected.setFhAlta(new Date());
		clienteContactoSelected.setStHabilitado(true);
		clienteContactoSelected.setStUsuario("A");
		Contacto contacto = new Contacto();
		clienteContactoSelected.setIdContacto(contacto);
		contacto.setClienteContactoList(this.clienteSelected.getClienteContactoList());
		
		clienteSelected = clienteSel;
		clienteSelected.add(clienteContactoSelected);
		medioContactoSelected = new MedioCnt();
	}

	/**
	 * Método para validar si se ha seleccionado uno o varios objetos tipo Cliente
	 */
	public boolean clienteSeleccionado() {
		return this.lstClientesSelected != null && !this.lstClientesSelected.isEmpty();
	}

	public void guardarCliente() {
		List<Planta> plantaList = null;
		
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
			
			//CANDADO SALIDA
			plantaList = plantaDAO.findall(true);
			
			CandadoSalida candadoSalida = new CandadoSalida();
			candadoSalida.setHabilitado(true);
			candadoSalida.setCliente(clienteSelected);
			candadoSalida.setNumSalidas(1);
			
			clienteSelected.setCandadoSalida(candadoSalida);
			
			for(Planta planta : plantaList) {
				
				SerieConstanciaPK serieConstanciaPK_I = new SerieConstanciaPK();
				serieConstanciaPK_I.setCliente(clienteSelected);
				serieConstanciaPK_I.setPlanta(planta);
				serieConstanciaPK_I.setTpSerie("I");
				SerieConstancia serieConstanciaI = new SerieConstancia();
				serieConstanciaI.setSerieConstanciaPK(serieConstanciaPK_I);
				serieConstanciaI.setNuSerie(1);
				clienteSelected.addSerieConstancia(serieConstanciaI);
				planta.add(serieConstanciaI);
				
				SerieConstanciaPK serieConstanciaPK_O = new SerieConstanciaPK();
				serieConstanciaPK_O.setCliente(clienteSelected);
				serieConstanciaPK_O.setPlanta(planta);
				serieConstanciaPK_O.setTpSerie("O");
				SerieConstancia serieConstanciaO = new SerieConstancia();
				serieConstanciaO.setSerieConstanciaPK(serieConstanciaPK_O);
				serieConstanciaO.setNuSerie(1);
				clienteSelected.addSerieConstancia(serieConstanciaO);
				planta.add(serieConstanciaO);
				
				SerieConstanciaPK serieConstanciaPK_T = new SerieConstanciaPK();
				serieConstanciaPK_T.setCliente(clienteSelected);
				serieConstanciaPK_T.setPlanta(planta);
				serieConstanciaPK_T.setTpSerie("T");
				SerieConstancia serieConstanciaT = new SerieConstancia();
				serieConstanciaT.setSerieConstanciaPK(serieConstanciaPK_T);
				serieConstanciaT.setNuSerie(1);
				clienteSelected.addSerieConstancia(serieConstanciaT);
				planta.add(serieConstanciaT);
				
				SerieConstanciaPK serieConstanciaPK_S = new SerieConstanciaPK();
				serieConstanciaPK_S.setCliente(clienteSelected);
				serieConstanciaPK_S.setPlanta(planta);
				serieConstanciaPK_S.setTpSerie("S");
				SerieConstancia serieConstanciaS = new SerieConstancia();
				serieConstanciaS.setSerieConstanciaPK(serieConstanciaPK_S);
				serieConstanciaS.setNuSerie(1);
				clienteSelected.addSerieConstancia(serieConstanciaS);
				planta.add(serieConstanciaS);
			}
			
			if (clienteDAO.guardar(clienteSelected) == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Cliente Agregado"));
				log.info("El usuario {} ha registrado el cliente {}.", this.usuario.getUsuario(), this.clienteSelected);
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error", "Ocurrió un error al intentar guardar el Cliente"));
				log.info("El usuario {} ha actualizado el cliente {}.", this.usuario.getUsuario(), this.clienteSelected);
			}
		} else {
			if (clienteDAO.actualizar(clienteSelected) == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Cliente Actualizado"));
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error", "Ocurrió un error al intentar actualizar el Cliente"));
			}
		}
		
		consultaClientes();
		this.cdRegimenFiscalSelected = null;
		this.cdUsoCfdiSelected = null;
		this.cdMetodoPagoSelected = null;
		this.idMedioPagoSelected = null;
		PrimeFaces.current().executeScript("PF('dialogCliente').hide()");
		PrimeFaces.current().ajax().update("form:messages", "form:dt-clientes");
	}

	public void eliminarCliente() {
		//if (clienteDAO.eliminar(clienteSelected) == null) {
		if (clienteDAO.eliminar(clienteSelected.getCteCve()) == null) {
			this.consultaClientes();
			clienteSelected = null;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Cliente Eliminado"));
			PrimeFaces.current().ajax().update("form:dt-clientes");
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error","Favor de eliminar contacto(s) del cliente a eliminar"));
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
	
	public void nuevoMedio() {
		System.out.println("Nuevo medio de contacto...");
		this.medioContactoSelected = new MedioCnt();
		
	}
	
	public void addTipoMedioContacto() {
		if( "t".equalsIgnoreCase(this.medioContactoSelected.getTpMedio()) ) {
			Telefono telefono = new Telefono();
			this.medioContactoSelected.setIdTelefono(telefono);
			this.medioContactoSelected.setIdMail(null);
		} else if ("m".equalsIgnoreCase(this.medioContactoSelected.getTpMedio())) {
			Mail mail = new Mail();
			this.medioContactoSelected.setIdMail(mail);
			this.medioContactoSelected.setIdTelefono(null);
		}
	}
	
	public void validarRFC() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		
		try {
			
			if(ClienteUtil.validarRFC(this.clienteSelected.getTipoPersona(), this.clienteSelected.getCteRfc()) == false ) {
				mensaje = "El RFC es incorrecto";
				throw new InventarioException("El RFC es incorrecto");
			}
			
			severity = FacesMessage.SEVERITY_INFO;
			mensaje = "RFC Correcto";
			
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
	
	public void regimenSelect() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		
		try {
			
			if("M".equals(this.clienteSelected.getTipoPersona())) {	
				lstRegimenFiscal = regimenFiscalDAO.buscarPorPersonaMoral();
				lstUsoCfdi = usoCfdiDAO.buscaPorPersonaMoral();
				log.info(String.format("Lista de regimenes para personas morales: (%d) ", lstRegimenFiscal.size()));
			}else if("F".equals(this.clienteSelected.getTipoPersona())) {
				lstRegimenFiscal = regimenFiscalDAO.buscarPorPersonaFisica();
				lstUsoCfdi = usoCfdiDAO.buscaPorPersonaFisica();
				log.info(String.format("Lista de regimenes para personas físicas: (%d) ", lstRegimenFiscal.size()));
			} else {
				throw new InventarioException("El tipo de persona es incorrecto.");
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
	
	public void guardarContacto() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		
		Cliente cliente = null;
		
		try {
			cliente = this.clienteContactoSelected.getIdCliente();
			cliente = clienteDAO.buscarPorId(cliente.getCteCve(), true);
			
			if(this.clienteContactoSelected == null)
				throw new InventarioException("No hay un contacto seleccionado.");
			
			int indexOf = cliente.getClienteContactoList().indexOf(this.clienteContactoSelected);
			if(indexOf < 0) {
				cliente.add(clienteContactoSelected);
				clienteDAO.actualizar(cliente);
				this.clienteSelected = clienteDAO.buscarPorId(cliente.getCteCve());
			}else {
				
				for(ClienteContacto clienteContacto: cliente.getClienteContactoList()) {
				
					if(clienteContactoSelected.equals(clienteContacto)) {
											
						clienteContacto.setIdContacto(clienteContactoSelected.getIdContacto());
						clienteContacto.setNbUsuario(clienteContactoSelected.getNbUsuario());
						clienteContacto.setNbPassword(clienteContactoSelected.getNbPassword());
						clienteContacto.setStUsuario(clienteContactoSelected.getStUsuario());
						clienteContacto.setStHabilitado(clienteContactoSelected.getStHabilitado());
						clienteContacto.setRecibeFacturacion(clienteContactoSelected.getRecibeFacturacion());
						clienteContacto.setRecibeInventario(clienteContactoSelected.getRecibeInventario());
						
						clienteDAO.actualizar(cliente);
					}
				}
			}
			this.consultaClientes();
			PrimeFaces.current().executeScript("PF('dialogAddContacto').hide()");
			PrimeFaces.current().executeScript("PF('dialogEditContacto').hide()");
			
			severity = FacesMessage.SEVERITY_INFO;
			mensaje = "Contacto actualizado correctamente.";
		} catch (InventarioException ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
		} catch (Exception ex) {
			log.error(ex);
			mensaje = "Ha ocurrido un problema al eliminar el contacto.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			message = new FacesMessage(severity, "Catálogo de clientes", mensaje);
	        FacesContext.getCurrentInstance().addMessage(null, message);
	        PrimeFaces.current().ajax().update("pnlContacto", "pnlEditContacto", "dt-clientes", "messages");
	          
		}
	}
	
	public void eliminarContacto() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		
		Cliente cliente = null;
		
		try {
			if(this.clienteContactoSelected == null)
				throw new InventarioException("No hay un contacto seleccionado.");
			
			cliente = this.clienteContactoSelected.getIdCliente();
			cliente = clienteDAO.buscarPorId(cliente.getCteCve(), true);
			
			int indexOf = cliente.getClienteContactoList().indexOf(this.clienteContactoSelected);
			if(indexOf < 0)
				throw new InventarioException("El contacto seleccionado es incorrecto.");
			
			cliente.remove(clienteContactoSelected);
			String respuesta = clienteDAO.actualizar(cliente);
			
			if(respuesta != null)
				throw new InventarioException("Ocurrió un problema al eliminar al contacto del cliente.");
			
			this.clienteSelected = clienteDAO.buscarPorId(cliente.getCteCve());
			this.consultaClientes();
			
			severity = FacesMessage.SEVERITY_INFO;
			mensaje = "Contacto eliminado correctamente.";
		} catch (InventarioException ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
		} catch (Exception ex) {
			mensaje = "Ha ocurrido un problema al eliminar el contacto.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			message = new FacesMessage(severity, "Catálogo de clientes", mensaje);
	        FacesContext.getCurrentInstance().addMessage(null, message);
	        PrimeFaces.current().ajax().update(":form:messages", "dtContactos");
		}
		
		
	}
	
	public void guardaMedioContacto() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		
		try {
			if(this.medioContactoSelected == null)
				throw new InventarioException("No hay un medio de contacto seleccionado.");
			
			List<MedioCnt> medioCntList = this.clienteContactoSelected.getIdContacto().getMedioCntList();
			if(medioCntList == null)
				medioCntList = new ArrayList<MedioCnt>();
			medioCntList.add(this.medioContactoSelected);
			
			if("T".equalsIgnoreCase(medioContactoSelected.getTpMedio()))
				this.medioContactoSelected.getIdTelefono().setMedioCntList(medioCntList);
			if("M".equalsIgnoreCase(medioContactoSelected.getTpMedio()))
				this.medioContactoSelected.getIdMail().setMedioCntList(medioCntList);
			
			Contacto contacto = this.clienteContactoSelected.getIdContacto();
			medioContactoSelected.setIdContacto(contacto);
			medioContactoSelected.setIdMedio(null);
			medioCntDAO.actualizar(medioContactoSelected);//cambie por guardar el metodo actualizar
			
			medioCntList = medioCntDAO.buscarPorCriterios(medioContactoSelected);
			
			this.clienteContactoSelected.getIdContacto().setMedioCntList(medioCntList);
			
			severity = FacesMessage.SEVERITY_INFO;
			mensaje = "Medio de contacto registrado correctamente.";
		} catch (InventarioException ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
		} catch (Exception ex) {
			mensaje = "Ha ocurrido un problema al eliminar el contacto.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			message = new FacesMessage(severity, "Catálogo de clientes", mensaje);
	        FacesContext.getCurrentInstance().addMessage(null, message);
	        PrimeFaces.current().ajax().update(":form:messages", ":form:dtMedioContacto");
		}
	}
	
	public void eliminarMedioContacto() {
		
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		
		try {
			if( this.medioContactoSelected == null )
				throw new InventarioException("No hay un medio de contacto seleccionado.");
			
			List<MedioCnt> medioCntList = medioCntDAO.buscarPorCriterios(medioContactoSelected);
			
			medioCntList.remove(medioContactoSelected);
			
			this.clienteContactoSelected.getIdContacto().setMedioCntList(medioCntList);
			
			String actualizar = clienteContactoDAO.actualizar(this.clienteContactoSelected);
			log.debug("Respuesta del DAO actualización cliente contacto: {}", actualizar);
			
			severity = FacesMessage.SEVERITY_INFO;
			mensaje = "Medio de contacto eliminado correctamente.";
		} catch (InventarioException ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
		} catch (Exception ex) {
			mensaje = "Ha ocurrido un problema al eliminar el medio de contacto.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			message = new FacesMessage(severity, "Catálogo de clientes", mensaje);
	        FacesContext.getCurrentInstance().addMessage(null, message);
	        PrimeFaces.current().ajax().update(":form:messages", "dtMedioContacto");
		}
	}
	
	public void setPassword(ClienteContacto contacto) {
		log.debug("Cliente contacto: {}", contacto);
		this.clienteContactoSelected = contacto;
		this.newPassword = null;
		this.confirmPassword = null;
	}
	
	public void validateNewPassword() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		
		try {
			if(this.newPassword == null)
				throw new InventarioException("Debe indicar una contraseña nueva.");
			
			if(this.confirmPassword == null)
				throw new InventarioException("Debe confirmar su contraseña nueva.");
			
			if(newPassword.equals(confirmPassword) == false)
				throw new InventarioException("Su nueva contraseña no coincide en los dos campos.");
			
			mensaje = "Su nueva contraseña coincide correctamente.";
			severity = FacesMessage.SEVERITY_INFO;
		} catch(InventarioException ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
		} catch (Exception ex) {
			log.error("Problema con la emisión de salidas...", ex);
			mensaje = "Su solicitud no se pudo generar.\nFavor de comunicarse con el administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			message = new FacesMessage(severity, "Ajustes...", mensaje);
	        FacesContext.getCurrentInstance().addMessage(null, message);
	        PrimeFaces.current().ajax().update(":form:messages");
		}
	}
	
	public void changePassword() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		
		String newPasswordSHA512 = null;
		
		
		try {
			if(this.newPassword == null || "".equalsIgnoreCase(this.newPassword.trim()))
				throw new InventarioException("Debe indicar su nueva contraseña");
			
			if(this.confirmPassword == null || "".equalsIgnoreCase(this.confirmPassword.trim()))
				throw new InventarioException("Debe confirmar su nueva contraseña");
			
			util.checkPassword(this.newPassword);
			
			//TODO Por seguridad, se deben salar las contraseñas.
			newPasswordSHA512 = util.getSHA512(this.newPassword);
			
			this.clienteContactoSelected.setNbPassword(newPasswordSHA512);
			this.clienteContactoSelected.setStUsuario("A");;
			
			this.clienteContactoDAO.actualizar(clienteContactoSelected);
			
			log.info("Usuario actualizado");
			
			this.newPassword = null;
			this.confirmPassword = null;
			
			mensaje = "Su contraseña se actualizó correctamente.";
			severity = FacesMessage.SEVERITY_INFO;
			
			PrimeFaces.current().executeScript("PF('dlgPassword').hide()");
		} catch(InventarioException ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} catch(Exception ex) {
			log.error("Problema con la emisión de salidas...", ex);
			mensaje = "Su solicitud no se pudo generar.\nFavor de comunicarse con el administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			message = new FacesMessage(severity, "Ajustes...", mensaje);
	        FacesContext.getCurrentInstance().addMessage(null, message);
	        PrimeFaces.current().ajax().update(":form:messages", ":form:newPassword", ":form:confirmPassword");
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

	public List<TipoTelefono> getLstTipoTelefono() {
		return lstTipoTelefono;
	}

	public void setLstTipoTelefono(List<TipoTelefono> lstTipoTelefono) {
		this.lstTipoTelefono = lstTipoTelefono;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

}
