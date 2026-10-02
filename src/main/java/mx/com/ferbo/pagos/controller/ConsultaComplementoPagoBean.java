package mx.com.ferbo.pagos.controller;

import java.io.Serializable;
import java.time.LocalTime;
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

import com.ferbo.facturama.tools.FacturamaException;

import mx.com.ferbo.business.ComplementoPagoBL;
import mx.com.ferbo.business.complemento.ComplementoBL;
import mx.com.ferbo.dao.MedioPagoDAO;
import mx.com.ferbo.dao.PagoDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ComplementoPago;
import mx.com.ferbo.model.MedioPago;
import mx.com.ferbo.model.Pago;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.util.DAOException;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.InventarioException;

@Named(value = "consultaCPD")
@ViewScoped
public class ConsultaComplementoPagoBean implements Serializable {

	private static final long serialVersionUID = -3861994975158087620L;
	private static Logger log = LogManager.getLogger(ConsultaComplementoPagoBean.class);
	
    private FacesContext context;
    private HttpServletRequest request;
    private Usuario usuario;
	
	private Date fechaInicio;
	private Date fechaFin;
	
	private Cliente       cliente;
	private List<Cliente> clientes;
	
	private      ComplementoBL    complementoBO;
	private      ComplementoPago  complemento;
	private List<ComplementoPago> complementos;
	
	private PagoDAO    pagoDAO;
	private List<Pago> pagos;
	private Pago       pago;
	
	private List<MedioPago> formasDePago;
	private MedioPagoDAO    formaPagoDAO;
	
	private Date periodoInicio;
	private Date periodoFin;
	
	
	
	@SuppressWarnings("unchecked")
	public ConsultaComplementoPagoBean() {
		this.context  = FacesContext.getCurrentInstance();
		this.request  = (HttpServletRequest) context.getExternalContext().getRequest();
        this.usuario  = (Usuario) request.getSession(false).getAttribute("usuario");
        this.clientes = (List<Cliente>) request.getSession(false).getAttribute("clientesActivosList");
        
		this.complementoBO = new ComplementoBL();
		this.complementos  = new ArrayList<ComplementoPago>();
		this.fechaInicio   = new Date();
		this.fechaFin      = new Date();
		this.pagoDAO       = new PagoDAO();
		this.pago          = new Pago();
		this.formaPagoDAO  = new MedioPagoDAO();
		this.complemento   = complementoBO.crear();
		this.formasDePago  = formaPagoDAO.buscarVigentes(new Date());
		
		this.configuraPeriodoComplementos();
		this.configuraPeriodoPagos();
		this.buscarComplementos();
		
		log.info("El usuario {} entra a la consulta de complementos de pago", this.usuario.getUsuario());
	}
	
	public void configuraPeriodoComplementos() {
		Integer dia = null;
		this.fechaFin = new Date();
		DateUtil.setTime(this.fechaFin, 23, 59, 59, 999);
		dia = DateUtil.getDia(this.fechaFin);
		
		if(dia <= 5) {
			this.fechaInicio = DateUtil.addMonth(this.fechaFin, -1);
			this.fechaInicio = DateUtil.getFirstDayOfMonth(this.fechaInicio);
			DateUtil.setTime(this.fechaInicio, 0, 0, 0, 0);
		} else {
			this.fechaInicio = DateUtil.getFirstDayOfMonth(this.fechaFin);
		}
	}
	
	public void configuraPeriodoPagos() {
		Integer dia = null;
		this.periodoFin = new Date();
		DateUtil.setTime(this.periodoFin, 23, 59, 59, 999);
		dia = DateUtil.getDia(this.periodoFin);
		
		if(dia <= 5) {
			this.periodoInicio = DateUtil.addMonth(this.periodoFin, -1);
			this.periodoInicio = DateUtil.getFirstDayOfMonth(this.periodoInicio);
			DateUtil.setTime(this.periodoInicio, 0, 0, 0, 0);
		} else {
			this.periodoInicio = DateUtil.getFirstDayOfMonth(this.periodoFin);
		}
	}
	
	@PostConstruct
	public void init() {
	}
	
	public void buscarComplementos() {
		log.info("Buscando complementos de pago...");
		this.complementos = this.complementoBO.buscarPor(this.fechaInicio, this.fechaFin);
	}
	
	public void cargaInfo(ComplementoPago complemento) {
		String title = "Complementos";
        String message = null;
        Severity severity = null;
        
        try {
        	this.complemento = complementoBO.cargar(complemento.getId());
        } catch (InventarioException ex) {
            message = ex.getMessage();
            severity = FacesMessage.SEVERITY_WARN;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, title, message));
        } catch (Exception ex) {
            log.error("Problema para recuperar los datos del cliente.", ex);
            message = "Ocurrió un problema para consultar las facturas del cliente.";
            severity = FacesMessage.SEVERITY_ERROR;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, title, message));
        } finally {
            PrimeFaces.current().ajax().update("form:messages", "form:dt-complementos");
        }
		
	}
	
	public Boolean editar() {
		if(this.complemento == null)
			return Boolean.FALSE;
		
		if(this.complemento.getIdPac() == null)
			return Boolean.FALSE;
		
		return Boolean.TRUE;
	}
	
	public void cargarPagosPendientes() {
		log.info("Cargando facturas pendientes...");
		
		try {
			this.pagos = this.pagoDAO.buscar(this.complemento.getEmisor().getNb_rfc(), this.complemento.getReceptor().getCteCve(), this.periodoInicio, this.periodoFin, "PPD");
		} catch (DAOException e) {
			log.error("Problema para obtener la lista de pagos...");
		}
		
	}
	
	public List<Pago> pagosPendientes() {
		if(this.pagos == null) {
			log.info("No hay pagos registrados para el periodo seleccionado.");
			return new ArrayList<Pago>();
		}
		
		return this.pagos.stream()
				.filter(p -> p.getTipo().getId() != 5) //Elimina los pagos que provienen de Notas de crédito.
				.filter(p -> this.complemento.getListPagos().contains(p) == false)
				.collect(Collectors.toList());
	}
	
	public void solicitarFormaDePago(Pago pago) {
		this.pago = pago;
	}
	
	public void agregarPagoPendiente() {
		String title = null;
        String message = null;
        Severity severity = null;
        
		try {
			log.info("Agregando el pago al complemento...");
			
			log.info("Parcialidad del pago {}: {}", this.pago, this.pago.getParcialidad());
			
			if(this.pago.getParcialidad() == null)
				complementoBO.configuraParcialidad(this.pago);
			
			if(this.pago.getHora() == null)
				this.pago.setHora(LocalTime.MIDNIGHT);
			
			if(this.complemento.getListPagos() == null)
				this.complemento.setListPagos(new ArrayList<Pago>());
			
			this.pago.setComplementoPago(this.complemento);
			this.complemento.getListPagos().add(pago);
			
			
			PrimeFaces.current().ajax().update("form:pnl-complemento", "form:pnl-agregar-pago");
			PrimeFaces.current().executeScript("PF('dlgFormaPago').hide()");
			
			title = "Pago agregado";
			message = "Se agregó al complemento de pago";
			severity = FacesMessage.SEVERITY_INFO;
		} catch(InventarioException ex) {
			log.warn("{}", ex.getMessage());
			title = "Aviso";
			message = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} catch(Exception ex) {
			log.error("Problema para agregar el pago...", ex);
			title = "Error";
			message = "Ocurrió un problema al agregar el pago.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, title, message));
			PrimeFaces.current().ajax().update("form:messages");
		}
	}
	
	public void eliminarPago(Pago pago) {
		pago.setComplementoPago(null);
		this.complemento.getListPagos().remove(pago);
		pagoDAO.actualizar(pago);
	}
	
	public void actualizar() {
		String title = null;
        String message = null;
        Severity severity = null;
        
        try {
        	this.complementoBO.actualizar(this.complemento);
        	
        	title = "Operación correcta";
        	message = "Se actualizó su complemento de pago";
        	severity = FacesMessage.SEVERITY_INFO;
        } catch (InventarioException ex) {
        	log.warn("{}", ex.getMessage());
        	title = "Aviso";
            message = ex.getMessage();
            severity = FacesMessage.SEVERITY_WARN;
        } catch (Exception ex) {
            log.error("Problema para recuperar los datos del cliente.", ex);
            title = "Error";
            message = "Ocurrió un problema en la actualización del complemento de pago";
            severity = FacesMessage.SEVERITY_ERROR;
        } finally {
        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, title, message));
            PrimeFaces.current().ajax().update("form:messages", "form:dt-complementos");
        }
		
		PrimeFaces.current().executeScript("PF('dlgComplemento').hide()");
		
	}
	
	public void timbrar(ComplementoPago complemento) {
		String title = null;
        String message = null;
        Severity severity = null;
		
		ComplementoPago complementoPago;
		try {
			log.info("timbrando...");
			complementoPago = complementoBO.cargar(complemento.getId());
			
			ComplementoPagoBL complementoPagoBL = new ComplementoPagoBL(complementoPago);
            complementoPagoBL.timbrar();
            complementoPagoBL.sendMail();
            
            title = "Operación correcta";
        	message = "Se actualizó su complemento de pago";
        	severity = FacesMessage.SEVERITY_INFO;
		} catch(FacturamaException ex) {
			log.warn("Problema en la comunicación con Facturama: {}", ex.getMessage());
			title = "Aviso de Facturama";
			message = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} catch (InventarioException ex) {
			log.warn("{}", ex.getMessage());
			title = "Aviso";
			message = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} catch (Exception ex) {
			log.error("Problema con el timbrado del complemento de pago...", ex);
			title = "Error";
			message = "Ocurrió un problema con el timbrado del complemento de pago.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, title, message));
            PrimeFaces.current().ajax().update("form:messages", "form:dt-complementos");
		}
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public ComplementoPago getComplemento() {
		return complemento;
	}

	public void setComplemento(ComplementoPago complemento) {
		this.complemento = complemento;
	}

	public List<ComplementoPago> getComplementos() {
		return complementos;
	}

	public void setComplementos(List<ComplementoPago> complementos) {
		this.complementos = complementos;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public List<Cliente> getClientes() {
		return clientes;
	}

	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
	}

	public List<Pago> getPagos() {
		return pagos;
	}

	public void setPagos(List<Pago> pagos) {
		this.pagos = pagos;
	}

	public Date getPeriodoInicio() {
		return periodoInicio;
	}

	public void setPeriodoInicio(Date periodoInicio) {
		this.periodoInicio = periodoInicio;
	}

	public Date getPeriodoFin() {
		return periodoFin;
	}

	public void setPeriodoFin(Date periodoFin) {
		this.periodoFin = periodoFin;
	}

	public List<MedioPago> getFormasDePago() {
		return formasDePago;
	}

	public void setFormasDePago(List<MedioPago> formasDePago) {
		this.formasDePago = formasDePago;
	}

	public Pago getPago() {
		return pago;
	}

	public void setPago(Pago pago) {
		this.pago = pago;
	}
}
