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
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.BancoDAO;
import mx.com.ferbo.dao.FacturaDAO;
import mx.com.ferbo.dao.PagoDAO;
import mx.com.ferbo.dao.StatusFacturaDAO;
import mx.com.ferbo.dao.TipoPagoDAO;
import mx.com.ferbo.model.Bancos;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.Factura;
import mx.com.ferbo.model.Pago;
import mx.com.ferbo.model.StatusFactura;
import mx.com.ferbo.model.TipoPago;
import mx.com.ferbo.util.InventarioException;

@Named
@ViewScoped

public class IngresosActualizacionBean implements Serializable{

	private static final long serialVersionUID = -626048119540963939L;
	private static Logger log = LogManager.getLogger(IngresosActualizacionBean.class);
	
	private Date startDate;
	private Date endDate;	
	
	private PagoDAO pagoDAO;
	private TipoPagoDAO tipoPagoDAO;
	private BancoDAO bancoDAO;
	private FacturaDAO facturaDAO;
	private StatusFacturaDAO sfDAO;
	private StatusFactura statusPorCobrar;
	private StatusFactura statusPagada;
	private StatusFactura statusPagoParcial;
	
	
	private List<Pago> listaPago;
	private List<Cliente> listaCtes;
	private List<TipoPago> listatipoPago;
	private List<Bancos> listaBancos;
	
	private Pago pagoSelected;
	private Cliente cteSelect;
	
	private BigDecimal totalPagos;
	
	private FacesContext context;
	private HttpServletRequest request;
	
	public IngresosActualizacionBean() {
		listaPago = new ArrayList<Pago>();
		listaCtes = new ArrayList<Cliente>();
		pagoSelected = new Pago();
		cteSelect = new Cliente();
	}
	
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init(){
		context = FacesContext.getCurrentInstance();
		request = (HttpServletRequest) context.getExternalContext().getRequest();
		pagoDAO = new PagoDAO();
		tipoPagoDAO = new TipoPagoDAO();
		bancoDAO = new BancoDAO();
		sfDAO = new StatusFacturaDAO();
		facturaDAO = new FacturaDAO();
		
		listaCtes = (List<Cliente>) request.getSession(false).getAttribute("clientesActivosList");
		listaBancos = bancoDAO.buscarTodos();
		listatipoPago = tipoPagoDAO.buscarTodos();
		this.startDate = new Date();
		this.endDate = new Date();
		
		statusPorCobrar = sfDAO.buscarPorId(StatusFactura.STATUS_POR_COBRAR);
		statusPagada = sfDAO.buscarPorId(StatusFactura.STATUS_PAGADA);
		statusPagoParcial = sfDAO.buscarPorId(StatusFactura.STATUS_PAGO_PARCIAL);
	}
	
	public void filtraPagos() {
		listaPago = pagoDAO.buscaPorClienteFechas(cteSelect, startDate, endDate);
		this.totalPagos = new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP);
		for(Pago pago : listaPago) {
			if(pago.getTipo().getId() == 5)
				continue;
			this.totalPagos = this.totalPagos.add(pago.getMonto());
		}
	}
	
	public void cargaInfoPago() {
		log.debug("Pago: {}", this.pagoSelected);
	}
	
	
	public void updatePago() {
		String messages = null;
		Severity severity = null;
		
		Pago pago = null;
		Factura factura = null;
		BigDecimal saldo = null;
		
		String respuesta = null;
		
		try {
			log.debug("Pago: {}", pagoSelected);
			
			respuesta = pagoDAO.actualizar(pagoSelected);
			if(respuesta != null) {
				throw new InventarioException("Ocurrió un problema al actualizar el pago.");
			}
			
			pago = pagoDAO.buscarPorId(this.pagoSelected.getId(), true);
			factura = pago.getFactura();
			saldo = factura.getTotal();
			
			for(Pago p : factura.getPagoList()) {
				saldo = saldo.subtract(p.getMonto());
			}
			
			if(saldo.compareTo(BigDecimal.ZERO) > 0 && saldo.compareTo(factura.getTotal()) < 0) {
				factura.setStatus(statusPagoParcial);
			} else if(saldo.compareTo(BigDecimal.ZERO) > 0 && saldo.compareTo(factura.getTotal()) == 0) {
				factura.setStatus(statusPorCobrar);
			} else if(saldo.compareTo(BigDecimal.ZERO) == 0) {
				factura.setStatus(statusPagada);
			} else {
				String msg = String.format("La suma de todos los pagos de la factura %s-%s excede el monto total.", factura.getNomSerie(), factura.getNumero());
				throw new InventarioException(msg);
			}
			
			facturaDAO.actualizaStatus(factura);
			
			listaPago = pagoDAO.buscaPorClienteFechas(cteSelect, startDate, endDate);
			
			severity = FacesMessage.SEVERITY_INFO;
			messages = "El pago se actualizó correctamente.";
			
		} catch(InventarioException ex){	
			log.error("Ocurrió un problema al actualizar el pago...", ex);
			severity = FacesMessage.SEVERITY_ERROR;
			messages = ex.getMessage();
		} catch (Exception ex) {
			log.error("Ocurrió un problema al actualizar el pago...", ex);
			severity = FacesMessage.SEVERITY_ERROR;
			messages = "Error al actualizar pago";
		}
		
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Actualización", messages));
		PrimeFaces.current().ajax().update("form:messages", "dt-pagos");
	}
	
	public void deletePago() {
		
		String mensaje = null;
		Severity severity = null;
		
		
		String respuesta = null;
		Pago pago = null;
		Factura factura = null;
		Integer idFactura = null;
		BigDecimal saldo = null;
		
		try {
			
			pago = pagoDAO.buscarPorId(this.pagoSelected.getId(), true);
			idFactura = pago.getFactura().getId();
			
			respuesta = pagoDAO.eliminar(pagoSelected);
			
			if(respuesta != null) {
				log.error("Problema al eliminar el pago " + respuesta);
				throw new InventarioException("Ocurrió un problema para eliminar el pago.");
			}
			
			factura = facturaDAO.buscarPorId(idFactura, true);
			saldo = factura.getTotal();
			
			for(Pago p : factura.getPagoList()) {
				saldo = saldo.subtract(p.getMonto());
			}
			
			if(saldo.compareTo(BigDecimal.ZERO) > 0 && saldo.compareTo(factura.getTotal()) < 0) {
				factura.setStatus(statusPagoParcial);
			} else if(saldo.compareTo(BigDecimal.ZERO) > 0 && saldo.compareTo(factura.getTotal()) == 0) {
				factura.setStatus(statusPorCobrar);
			} else if(saldo.compareTo(BigDecimal.ZERO) == 0) {
				factura.setStatus(statusPagada);
			} else {
				String msg = String.format("La suma de todos los pagos de la factura %s-%s excede el monto total.", factura.getNomSerie(), factura.getNumero());
				throw new InventarioException(msg);
			}
			
			respuesta = facturaDAO.actualizaStatus(factura);
			
			if(respuesta != null) {
				log.info("Problema al actualizar el status de la factura {}-{}: {}", factura.getNomSerie(), factura.getNumero(), respuesta);
				throw new InventarioException("Ocurrió un problema para actualizar el status de la factura " + factura.getNomSerie() + " - " + factura.getNumero());
			}
			
			filtraPagos();
			
			mensaje = "El pago fue eliminado correctamente";
			severity = FacesMessage.SEVERITY_INFO;
		} catch(InventarioException ex) {
			log.error("Ocurrió un problema al eliminar el pago...", ex);
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
		} catch (Exception ex) {
			log.error("Ocurrió un problema al eliminar el pago...", ex);
			mensaje = "Ocurrio un problema al eliminar el pago.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Eliminar pago", mensaje));
			PrimeFaces.current().ajax().update("form:messages");
		}
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

	public BigDecimal getTotalPagos() {
		return totalPagos;
	}

	public void setTotalPagos(BigDecimal totalPagos) {
		this.totalPagos = totalPagos;
	}
	
	
	
	
}
