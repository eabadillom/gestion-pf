package mx.com.ferbo.pagos.controller;

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

import mx.com.ferbo.business.complemento.ComplementoBL;
import mx.com.ferbo.business.n.ClienteBL;
import mx.com.ferbo.dao.PagoDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ComplementoPago;
import mx.com.ferbo.model.Pago;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.util.DAOException;
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
//	private ClienteBL clientesBO;
	private Cliente cliente;
	private List<Cliente> clientes;
	private ComplementoBL complementoBO;
	private ComplementoPago complemento;
	private List<ComplementoPago> complementos;
	private PagoDAO pagoDAO;
	private List<Pago> pagos;
	private Date periodoInicio;
	private Date periodoFin;
	
	
	
	@SuppressWarnings("unchecked")
	public ConsultaComplementoPagoBean() {
		context = FacesContext.getCurrentInstance();
		request = (HttpServletRequest) context.getExternalContext().getRequest();
        usuario = (Usuario) request.getSession(false).getAttribute("usuario");
        this.clientes = (List<Cliente>) request.getSession(false).getAttribute("clientesActivosList");
        
		this.complementoBO = new ComplementoBL();
		this.complementos = new ArrayList<ComplementoPago>();
		this.fechaInicio = new Date();
		this.fechaFin = new Date();
		this.pagoDAO = new PagoDAO();
		this.complemento = complementoBO.crear();
	}
	
	@PostConstruct
	public void init() {
		//En este método no se debería agregar código, debido a que existe componentes de PrimeFaces que lanzan múltiples invocaciones a este método.
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
				.filter(p -> this.complemento.getListPagos().contains(p) == false)
				.collect(Collectors.toList());
	}
	
	public void agregarPagoPendiente(Pago pago) {
		log.info("Agregando el pago al complemento...");
		if(this.complemento.getListPagos() == null)
			this.complemento.setListPagos(new ArrayList<Pago>());
		
		pago.setComplementoPago(this.complemento);
		this.complemento.getListPagos().add(pago);
		PrimeFaces.current().ajax().update("form:pnl-complemento", "form:pnl-agregar-pago");
	}
	
	public void eliminarPago(Pago pago) {
		pago.setComplementoPago(null);
		this.complemento.getListPagos().remove(pago);
		pagoDAO.actualizar(pago);
	}
	
	public void actualizar() {
		String title = "Complementos";
        String message = null;
        Severity severity = null;
        
        try {
        	complementoBO.actualizar(this.complemento);
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
		
		PrimeFaces.current().executeScript("PF('dlgComplemento').hide()");
		
	}
	
	public void timbrar(ComplementoPago complemento) {
		
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
	
	
	

}
