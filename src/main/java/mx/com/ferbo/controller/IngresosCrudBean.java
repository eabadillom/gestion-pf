package mx.com.ferbo.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.MatchMode;
import org.primefaces.omega.domain.CustomerStatus;

import ch.qos.logback.core.status.Status;
import mx.com.ferbo.dao.BancoDAO;
import mx.com.ferbo.dao.ClienteDAO;
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
import mx.com.ferbo.util.EntityManagerUtil;

@Named
@ViewScoped
public class IngresosCrudBean implements Serializable {

	private static final long serialVersionUID = -626048119540963939L;
	private static Logger log = Logger.getLogger(IngresosCrudBean.class);

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
		
		
		private Cliente cteSelect;
		private Factura facturaSelect;
		private Pago pagoSelected;
		private Bancos bancoSelect;
		private TipoPago tipoPago;
		private StatusFactura sfpagoParcial;
		private StatusFactura sfporCobrar;
		private StatusFactura sfPagada;
		
		
		private List<Cliente> listaCtes;
		private List<Factura> listaFactura;
		private List<Factura> listaFacturaPago;
		private List<Pago> listaPago;
		private List<Bancos> listaBancos;
		private List<TipoPago> listatipoPago;
		
		private ClienteDAO cteDAO;
		private FacturaDAO facturaDAO;
		private PagoDAO pagofactDAO;
		private BancoDAO bancoDAO;
		private TipoPagoDAO tipoPagoDAO;
		private StatusFacturaDAO statusFacturaDAO;
		
		public IngresosCrudBean() {
			cteDAO = new ClienteDAO();
			facturaDAO = new FacturaDAO();
			pagofactDAO = new PagoDAO();
			bancoDAO = new BancoDAO();
			tipoPagoDAO = new TipoPagoDAO();
			statusFacturaDAO = new StatusFacturaDAO();

			listaCtes = new ArrayList<Cliente>();
			listaFactura = new ArrayList<Factura>();
			listaPago = new ArrayList<Pago>();
			listaBancos = new ArrayList<Bancos>();
			listatipoPago = new ArrayList<TipoPago>();
			listaFacturaPago = new ArrayList<Factura>();

			cteSelect = new Cliente();
			facturaSelect = new Factura();
			pagoSelected = new Pago();
			bancoSelect = new Bancos();

		}
		
		@PostConstruct
		public void init() {
			fecha = new Date();
			listaCtes = cteDAO.buscarTodos();
			//listaFactura = facturaDAO.buscarTodos();
			//listaPago = pagofactDAO.buscarTodos();
			listaBancos = bancoDAO.buscarTodos();
			listatipoPago = tipoPagoDAO.buscarTodos();
			sfPagada = statusFacturaDAO.buscarPorId(3);
			sfpagoParcial = statusFacturaDAO.buscarPorId(4);
			sfporCobrar = statusFacturaDAO.buscarPorId(1);
		}
		
		public void filtroCte() {
			listaFactura = facturaDAO.buscarTodos();
			String message = null;
			Severity severity = null;
			EntityManager manager = null;
			Cliente cte = null;
			StatusFactura sf = new StatusFactura();
			this.cteSelect = cteDAO.buscarPorId(idCte);
			listaFactura.clear();
			log.info("Entrando a filtrar cliente...");
			try {
				if(pagoParcial == true) {
					StatusFactura sfpagoParcial = new StatusFactura();
					sfpagoParcial.setId(4);
					listaFactura.addAll(facturaDAO.buscarPorCteStatus(sfpagoParcial, cteSelect));
					message = "Pago Parcial";
					severity = FacesMessage.SEVERITY_INFO;
				}
				if(porCobrar == true) {
					StatusFactura sfporCobrar = new StatusFactura();
					sfporCobrar.setId(1);
					listaFactura.addAll(facturaDAO.buscarPorCteStatus(sfporCobrar, cteSelect));
					message = "Por Cobrar.";
					severity = FacesMessage.SEVERITY_INFO;
				}
						
					
			}catch(Exception ex) {
				log.error("Problema para recuperar los datos del cliente.", ex);
				message = ex.getMessage();
				severity = FacesMessage.SEVERITY_ERROR;
			}finally {
				
				if(porCobrar == false && pagoParcial == false) {
					message = "Seleccione el tipo de pago.";
					severity = FacesMessage.SEVERITY_INFO;
				}
				
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Cliente", message));
				PrimeFaces.current().ajax().update("form:messages", "form:dt-Factura");
			}
		}

		public void calculoFactura() {
			System.out.println("Factura: " + facturaSelect);
			List<Pago> listaPagos = new ArrayList<>();
			listaPagos = pagofactDAO.buscarPorFactura(facturaSelect.getId());
			BigDecimal sumaTotal = new BigDecimal(0);
			for(Pago p: listaPagos) {
				sumaTotal = sumaTotal.add(p.getMonto());
				System.out.println("Suma total de pagos: " + sumaTotal);
			}
			BigDecimal totalFactura = facturaSelect.getTotal();
			restaTotal = totalFactura.subtract(sumaTotal); 

		}
		
	
		public void agregaPagoFactura() {
			BigDecimal saldo = BigDecimal.ZERO;
			System.out.println(this.pagoSelected);			
			Pago pg = new Pago();
			
			bancoSelect = bancoDAO.buscarPorId(bancoCve);
			tipoPago = tipoPagoDAO.buscarPorId(tipoP);
			pg.setBanco(bancoSelect);
			pg.setMonto(cantidadApagar);
			pg.setFactura(facturaSelect);
			pg.setTipo(tipoPago);
			pg.setReferencia(referencia);
			pg.setFecha(fecha);
			facturaSelect.getPagoList().add(pg);
			int indexPago = facturaSelect.getPagoList().size() -1;
			Pago getListaPago = facturaSelect.getPagoList().get(indexPago);
			listaPago.add(getListaPago);	
			//listaFacturaPago.add(facturaSelect);

			saldo = facturaSelect.getTotal();
			for(Pago p : facturaSelect.getPagoList()) {
				saldo = saldo.subtract(p.getMonto());
			}
			
			if(saldo.compareTo(BigDecimal.ZERO) > 0) //Quiere decir que el saldo es mayor que cero
				facturaSelect.setStatus(sfpagoParcial);
			if(saldo.compareTo(BigDecimal.ZERO) <= 0) //Quere decir que el saldo es igual a 0, es decir, que ya se liquidó la factura. También considera que el pago sea mayor al monto de la factura, por lo que el cliente tendría un saldo a favor.
				facturaSelect.setStatus(sfPagada);
			
			bancoCve = null;
			tipoP = null;
			referencia = null;
			cantidadApagar = null;
		}
		
		public void savePago() {
			String message = null;
			Severity severity = null;
			try {
				
				if(!listaPago.isEmpty()) {//DUDA SOLO SE GUARDAN DATOS EN PAGO????
					for(Pago p: listaPago) {					
						pagofactDAO.guardar(p);					
					}
				}
				//facturaDAO.actualizar(facturaSelect);//ERROR GUARDA LA ULTIMA FACTURA SELECCIONADA Y NO TODOS LOS REGISTROS
				
				
				severity = FacesMessage.SEVERITY_INFO;
				message = "El pago se genero correctamente";
				listaPago.clear();
			} catch (Exception e) { 
				log.error("Problema para generar el pago.", e);
				e.printStackTrace();
				message = "Se genero un problema al generar pago.";
				severity = FacesMessage.SEVERITY_ERROR;
			} finally {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(severity, "Informacion de Pago", message));
				PrimeFaces.current().ajax().update("form:messages", "form:dt-ingAlta", "form:dt-Factura");
				
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

		public List<Pago> getListaPago() {
			return listaPago;
		}

		public void setListaPago(List<Pago> listaPago) {
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



}
