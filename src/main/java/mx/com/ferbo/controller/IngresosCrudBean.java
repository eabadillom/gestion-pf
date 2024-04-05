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
import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.dao.FacturaDAO;
import mx.com.ferbo.dao.PagoDAO;
import mx.com.ferbo.dao.ParametroDAO;
import mx.com.ferbo.dao.StatusFacturaDAO;
import mx.com.ferbo.dao.TipoPagoDAO;
import mx.com.ferbo.model.Bancos;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.Factura;
import mx.com.ferbo.model.Pago;
import mx.com.ferbo.model.Parametro;
import mx.com.ferbo.model.StatusFactura;
import mx.com.ferbo.model.TipoPago;
import mx.com.ferbo.ui.PagoUI;
import mx.com.ferbo.util.FormatUtil;
import mx.com.ferbo.util.InventarioException;

@Named
@ViewScoped
public class IngresosCrudBean implements Serializable {

	private static final long serialVersionUID = -626048119540963939L;
	private static Logger log = LogManager.getLogger(IngresosCrudBean.class);

	private Integer idCte;
	private BigDecimal totalSaldo;
	private boolean porCobrar;
	private boolean pagoParcial;
	private BigDecimal cantidadApagar;
	private String referencia;
	private BigDecimal sumaTotal;
	private BigDecimal restaTotal;
	private Date fecha;
	private Integer bancoCve;
	private Integer tipoP;

	private BigDecimal subtotalGlobal;
	private BigDecimal ivaGlobal;
	private BigDecimal totalGlobal;

	private Cliente cteSelect;
	private Factura facturaSelect;
	private Pago pagoSelected;
	private Bancos bancoSelect;
	private TipoPago tipoPago;
	private StatusFactura sfpagoParcial;
	private StatusFactura sfporCobrar;
	private StatusFactura sfPagada;
	private PagoUI pagoUISelected;

	private List<Cliente> listaCtes;
	private List<Factura> listaFactura;
	private List<Factura> listaFacturaPago;
	private List<PagoUI> listaPago;
	private List<Bancos> listaBancos;
	private List<TipoPago> listatipoPago;

	private ClienteDAO cteDAO;
	private FacturaDAO facturaDAO;
	private PagoDAO pagofactDAO;
	private BancoDAO bancoDAO;
	private TipoPagoDAO tipoPagoDAO;
	private StatusFacturaDAO statusFacturaDAO;
	private Parametro pIVA;
	private ParametroDAO parametroDAO;
	private BigDecimal iva;
	private String montoLetra;

	private FacesContext faceContext;
	private HttpServletRequest httpServletRequest;

	public IngresosCrudBean() {
		cteDAO = new ClienteDAO();
		facturaDAO = new FacturaDAO();
		pagofactDAO = new PagoDAO();
		bancoDAO = new BancoDAO();
		tipoPagoDAO = new TipoPagoDAO();
		statusFacturaDAO = new StatusFacturaDAO();
		parametroDAO = new ParametroDAO();

		listaCtes = new ArrayList<Cliente>();
		listaFactura = new ArrayList<Factura>();
		listaPago = new ArrayList<PagoUI>();
		listaBancos = new ArrayList<Bancos>();
		listatipoPago = new ArrayList<TipoPago>();
		listaFacturaPago = new ArrayList<Factura>();

		cteSelect = new Cliente();
		facturaSelect = new Factura();
		pagoSelected = new Pago();
		bancoSelect = new Bancos();

	}

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		fecha = new Date();
		faceContext = FacesContext.getCurrentInstance();
		httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
		listaCtes = (List<Cliente>) httpServletRequest.getSession(false).getAttribute("clientesActivosList");
		listaBancos = bancoDAO.buscarTodos();
		listatipoPago = tipoPagoDAO.buscarTodos();
		sfPagada = statusFacturaDAO.buscarPorId(3);
		sfpagoParcial = statusFacturaDAO.buscarPorId(4);
		sfporCobrar = statusFacturaDAO.buscarPorId(1);
		pIVA = parametroDAO.buscarPorNombre("IVA");
		iva = new BigDecimal(pIVA.getValor()).setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	public void filtroCte() {
		String message = null;
		Severity severity = null;

		try {
			log.info("Entrando a filtrar cliente {}", this.idCte);
			this.cteSelect = cteDAO.buscarPorId(idCte);
			if (this.cteSelect == null)
				throw new InventarioException("Seleccione un cliente.");

			this.listaFactura.clear();

			if (pagoParcial == true) {
				StatusFactura sfpagoParcial = new StatusFactura();
				sfpagoParcial.setId(4);
				listaFactura.addAll(facturaDAO.buscarPorCteStatus(sfpagoParcial, cteSelect));
				message = "Pago Parcial";
				severity = FacesMessage.SEVERITY_INFO;
			}
			if (porCobrar == true) {
				StatusFactura sfporCobrar = new StatusFactura();
				sfporCobrar.setId(1);
				listaFactura.addAll(facturaDAO.buscarPorCteStatus(sfporCobrar, cteSelect));
				message = "Por Cobrar.";
				severity = FacesMessage.SEVERITY_INFO;
			}

			if (listaFactura.size() > 0) {
				message = "Seleccione la factura que desea pagar.";
				severity = FacesMessage.SEVERITY_INFO;
			}
		} catch (InventarioException ex) {
			message = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} catch (Exception ex) {
			log.error("Problema para recuperar los datos del cliente.", ex);
			message = "Ocurrió un problema para consultar las facturas del cliente.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			if (severity != null && message != null)
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Cliente", message));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-Factura");
		}
	}

	public void calculoFactura() {
		log.debug("Factura: " + facturaSelect);
		List<Pago> listaPagos = new ArrayList<>();
		listaPagos = pagofactDAO.buscarPorFactura(facturaSelect.getId());
		BigDecimal sumaTotal = new BigDecimal(0);
		for (Pago p : listaPagos) {
			sumaTotal = sumaTotal.add(p.getMonto());
		}
		log.debug("Suma total de pagos: {}", sumaTotal);
		BigDecimal totalFactura = facturaSelect.getTotal();
		restaTotal = totalFactura.subtract(sumaTotal);

	}

	public void agregaPagoFactura() {
		String message = null;
		Severity severity = null;
		BigDecimal saldo = BigDecimal.ZERO;
		Pago pg = null;
		PagoUI pagoUI = null;

		try {
			log.debug("Factura: {}", this.facturaSelect);
			log.debug(this.pagoSelected);
			pg = new Pago();

			bancoSelect = bancoDAO.buscarPorId(bancoCve);
			tipoPago = tipoPagoDAO.buscarPorId(tipoP);
			pg.setBanco(bancoSelect);
			pg.setMonto(cantidadApagar);
			pg.setFactura(facturaSelect);
			pg.setTipo(tipoPago);
			pg.setReferencia(referencia);
			pg.setFecha(fecha);

			pagoUI = new PagoUI();
			pagoUI.setPago(pg);

			saldo = facturaSelect.getTotal();
			for (Pago p : facturaSelect.getPagoList()) {
				saldo = saldo.subtract(p.getMonto());
			}
			pagoUI.setSaldoAnterior(new BigDecimal(saldo.toString()));
			saldo = saldo.subtract(pg.getMonto());

			if (saldo.compareTo(facturaSelect.getTotal()) > 0)
				throw new InventarioException("El importe a pagar no debe ser mayor que el saldo de la factura.");

			facturaSelect.getPagoList().add(pg);
			listaPago.add(pagoUI);

			if (saldo.compareTo(BigDecimal.ZERO) > 0) // Quiere decir que el saldo es mayor que cero
				facturaSelect.setStatus(sfpagoParcial);
			if (saldo.compareTo(BigDecimal.ZERO) <= 0) // Quere decir que el saldo es igual a 0, es decir, que ya se
														// liquidó la factura. También considera que el pago sea mayor
														// al monto de la factura, por lo que el cliente tendría un
														// saldo a favor.
				facturaSelect.setStatus(sfPagada);

			pagoUI.setSaldo(saldo);

			totalGlobal = new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP);

			for (PagoUI p : listaPago) {
				totalGlobal = totalGlobal.add(p.getPago().getMonto());
			}
			subtotalGlobal = totalGlobal.divide(new BigDecimal(1).setScale(2, BigDecimal.ROUND_HALF_UP).add(iva),
					BigDecimal.ROUND_HALF_UP);
			ivaGlobal = subtotalGlobal.multiply(iva).setScale(2, BigDecimal.ROUND_HALF_UP);

			FormatUtil formato = new FormatUtil();

			montoLetra = formato.getMontoConLetra(totalGlobal.doubleValue());

			bancoCve = null;
			tipoP = null;
			referencia = null;
			cantidadApagar = null;

			log.debug("Lista pago: {}", this.listaPago);

		} catch (InventarioException ex) {
			message = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} catch (Exception ex) {
			log.error("Problema para recuperar los datos del cliente.", ex);
			message = "Ocurrió un problema para consultar las facturas del cliente.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			if (severity != null && message != null)
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Cliente", message));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-Factura");
		}
	}

	public void eliminaPagoFactura(PagoUI pagoUI) {
		String message = null;
		Severity severity = null;

		Pago pago = null;
		Factura factura = null;

		try {

			if (pagoUI == null)
				throw new InventarioException("Debe indicar un pago.");

			log.info("Eliminando pago: {}", pagoUI);

			pago = pagoUI.getPago();
			factura = pago.getFactura();
			factura.getPagoList().remove(pago);

			listaPago.remove(pagoUI);

			totalGlobal = new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP);

			for (PagoUI p : listaPago) {
				totalGlobal = totalGlobal.add(p.getPago().getMonto());
			}
			subtotalGlobal = totalGlobal.divide(new BigDecimal(1).setScale(2, BigDecimal.ROUND_HALF_UP).add(iva),
					BigDecimal.ROUND_HALF_UP);
			ivaGlobal = subtotalGlobal.multiply(iva).setScale(2, BigDecimal.ROUND_HALF_UP);

			FormatUtil formato = new FormatUtil();

			montoLetra = formato.getMontoConLetra(totalGlobal.doubleValue());

			message = "Pago eliminado.";
			severity = FacesMessage.SEVERITY_INFO;

		} catch (InventarioException ex) {
			log.error("Problema para eliminar el pago...", ex);
			message = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} catch (Exception ex) {
			log.error("Problema para eliminar el pago...", ex);
			message = "Ocurrió un problema para eliminar el pago seleccionado...";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			if (severity != null && message != null)
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Cliente", message));
			PrimeFaces.current().ajax().update("form:messages", "form:detallesFacturacion");
		}
	}

	public void savePago() {
		String message = null;
		Severity severity = null;

		Factura f = null;
		try {

			if (listaPago.isEmpty())
				throw new InventarioException("Debe registrar al menos un pago.");

			for (PagoUI p : listaPago) {
				if (p.getPago().getId() != null)
					continue;

				if (p.getPago().getTipo() == null || p.getPago().getTipo().getId() == null) {
					String msg = String.format("El pago debe tener un tipo de pago: Facutura %s-%s",
							p.getPago().getFactura().getNomSerie(), p.getPago().getFactura().getNumero());
					throw new InventarioException(msg);
				}

				pagofactDAO.guardar(p.getPago());
				f = facturaDAO.buscarPorId(p.getPago().getFactura().getId());
				f.setStatus(p.getPago().getFactura().getStatus());
				facturaDAO.actualizaStatus(f);
			}

			listaFactura.clear();
			listaPago.clear();
			idCte = null;
			cteSelect = null;
			pagoParcial = false;
			porCobrar = false;

			severity = FacesMessage.SEVERITY_INFO;
			message = "El pago se genero correctamente";

		} catch (InventarioException ex) {
			log.error("Se generó un problema al registrar el pago...", ex);
			message = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} catch (Exception e) {
			log.error("Problema para generar el pago.", e);
			message = "Se genero un problema al registrar el pago.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(severity, "Informacion de Pago", message));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-ingAlta", "form:dt-Factura",
					"form:detallesFacturacion", "form:Cliente", "form:statusPagoParcial", "form:statusPorCobrar");
		}
	}

	public Integer getIdCte() {
		return idCte;
	}

	public void setIdCte(Integer idCte) {
		this.idCte = idCte;
	}

	public List<Cliente> getListaCtes() {
		return listaCtes;
	}

	public void setListaCtes(List<Cliente> listaCtes) {
		this.listaCtes = listaCtes;
	}

	public Cliente getCteSelect() {
		return cteSelect;
	}

	public void setCteSelect(Cliente cteSelect) {
		this.cteSelect = cteSelect;
	}

	public List<Factura> getListaFactura() {
		return listaFactura;
	}

	public void setListaFactura(List<Factura> listaFactura) {
		this.listaFactura = listaFactura;
	}

	public Factura getFacturaSelect() {
		return facturaSelect;
	}

	public void setFacturaSelect(Factura facturaSelect) {
		this.facturaSelect = facturaSelect;
	}

	public boolean isPorCobrar() {
		return porCobrar;
	}

	public void setPorCobrar(boolean porCobrar) {
		this.porCobrar = porCobrar;
	}

	public boolean isPagoParcial() {
		return pagoParcial;
	}

	public void setPagoParcial(boolean pagoParcial) {
		this.pagoParcial = pagoParcial;
	}

	public Pago getPagofactSelect() {
		return pagoSelected;
	}

	public void setPagofactSelect(Pago pagofactSelect) {
		this.pagoSelected = pagofactSelect;
	}

	public List<PagoUI> getListaPago() {
		return listaPago;
	}

	public void setListaPago(List<PagoUI> listaPago) {
		this.listaPago = listaPago;
	}

	public BigDecimal getCantidadApagar() {
		return cantidadApagar;
	}

	public void setCantidadApagar(BigDecimal cantidadApagar) {
		this.cantidadApagar = cantidadApagar;
	}

	public Bancos getBancoSelect() {
		return bancoSelect;
	}

	public void setBancoSelect(Bancos bancoSelect) {
		this.bancoSelect = bancoSelect;
	}

	public List<Bancos> getListaBancos() {
		return listaBancos;
	}

	public void setListaBancos(List<Bancos> listaBancos) {
		this.listaBancos = listaBancos;
	}

	public BigDecimal getTotalSaldo() {
		return totalSaldo;
	}

	public void setTotalSaldo(BigDecimal totalSaldo) {
		this.totalSaldo = totalSaldo;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public BigDecimal getSumaTotal() {
		return sumaTotal;
	}

	public void setSumaTotal(BigDecimal sumaTotal) {
		this.sumaTotal = sumaTotal;
	}

	public BigDecimal getRestaTotal() {
		return restaTotal;
	}

	public void setRestaTotal(BigDecimal restaTotal) {
		this.restaTotal = restaTotal;
	}

	public TipoPago getTipoPago() {
		return tipoPago;
	}

	public void setTipoPago(TipoPago tipoPago) {
		this.tipoPago = tipoPago;
	}

	public List<TipoPago> getListatipoPago() {
		return listatipoPago;
	}

	public void setListatipoPago(List<TipoPago> listatipoPago) {
		this.listatipoPago = listatipoPago;
	}

	public List<Factura> getListaFacturaPago() {
		return listaFacturaPago;
	}

	public void setListaFacturaPago(List<Factura> listaFacturaPago) {
		this.listaFacturaPago = listaFacturaPago;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public StatusFactura getSfpagoParcial() {
		return sfpagoParcial;
	}

	public void setSfpagoParcial(StatusFactura sfpagoParcial) {
		this.sfpagoParcial = sfpagoParcial;
	}

	public StatusFactura getSfporCobrar() {
		return sfporCobrar;
	}

	public void setSfporCobrar(StatusFactura sfporCobrar) {
		this.sfporCobrar = sfporCobrar;
	}

	public Integer getBancoCve() {
		return bancoCve;
	}

	public void setBancoCve(Integer bancoCve) {
		this.bancoCve = bancoCve;
	}

	public Integer getTipoP() {
		return tipoP;
	}

	public void setTipoP(Integer tipoP) {
		this.tipoP = tipoP;
	}

	public Pago getPagoSelected() {
		return pagoSelected;
	}

	public void setPagoSelected(Pago pagoSelected) {
		this.pagoSelected = pagoSelected;
	}

	public PagoUI getPagoUISelected() {
		return pagoUISelected;
	}

	public void setPagoUISelected(PagoUI pagoUISelected) {
		this.pagoUISelected = pagoUISelected;
	}

	public BigDecimal getSubtotalGlobal() {
		return subtotalGlobal;
	}

	public void setSubtotalGlobal(BigDecimal subtotalGlobal) {
		this.subtotalGlobal = subtotalGlobal;
	}

	public BigDecimal getIvaGlobal() {
		return ivaGlobal;
	}

	public void setIvaGlobal(BigDecimal ivaGlobal) {
		this.ivaGlobal = ivaGlobal;
	}

	public BigDecimal getTotalGlobal() {
		return totalGlobal;
	}

	public void setTotalGlobal(BigDecimal totalGlobal) {
		this.totalGlobal = totalGlobal;
	}

	public String getMontoLetra() {
		return montoLetra;
	}

	public void setMontoLetra(String montoLetra) {
		this.montoLetra = montoLetra;
	}

}
