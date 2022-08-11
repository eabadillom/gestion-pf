package mx.com.ferbo.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.BancoDAO;
import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.dao.FacturaDAO;
import mx.com.ferbo.dao.PagoDAO;
import mx.com.ferbo.dao.ProductoClienteDAO;
import mx.com.ferbo.dao.ProductoDAO;
import mx.com.ferbo.dao.StatusFacturaDAO;
import mx.com.ferbo.dao.TipoPagoDAO;
import mx.com.ferbo.model.Bancos;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.Factura;
import mx.com.ferbo.model.Pago;
import mx.com.ferbo.model.Producto;
import mx.com.ferbo.model.ProductoPorCliente;
import mx.com.ferbo.model.StatusFactura;
import mx.com.ferbo.model.TipoPago;

@Named
@ViewScoped
public class IngresosCrudBean implements Serializable {

	/**
	 * @author Juan_Cervantes
	 */

	private static final long serialVersionUID = -626048119540963939L;

	/**
	 * Objetos para clientes
	 */
	private List<Cliente> lstClientes;
	private Cliente clienteSelected;
	private ClienteDAO clienteDAO;

	/**
	 * Objetos para bancos
	 */
	private List<Bancos> lstBancos;
	private Bancos bancosSelected;
	private BancoDAO bancosDAO;

	/**
	 * Objetos para facturas
	 */
	private List<Factura> lstFactura;
	private List<Factura> lstFacturaFiltered;
	private List<Factura> lstFacturaSelected;
	private Factura facturaSelected;
	private FacturaDAO facturaDAO;

	/**
	 * Objetos para statusFacturas
	 */
	private List<StatusFactura> lstStatusFactura;
	private StatusFactura statusFacturaSelected;
	private StatusFacturaDAO statusFacturaDAO;

	/**
	 * Objetos para Pagos
	 */
	private List<Pago> lstPago;
	private Pago pagoSelected;
	private PagoDAO pagoDAO;

	/**
	 * Objetos para tipo Pagos
	 */
	private List<TipoPago> lstTipoPago;
	private TipoPago tipoPagoSelected;
	private TipoPagoDAO tipoPagoDAO;

	/**
	 * Objetos auxiliares
	 */
	private Date customDate;
	private String startDate = "";
	private String endDate = "";
	private BigDecimal pagosTotales;
	private BigDecimal montoPago;

	/**
	 * Objetos para Productos
	 */
	private List<Producto> listProducto;
	private Producto productoSelected;
	private ProductoDAO productoDAO;

	/**
	 * Constructores
	 */

	public IngresosCrudBean() {
		clienteDAO = new ClienteDAO();
		bancosDAO = new BancoDAO();
		facturaDAO = new FacturaDAO();
		statusFacturaDAO = new StatusFacturaDAO();
		pagoDAO = new PagoDAO();
		tipoPagoDAO = new TipoPagoDAO();
		lstClientes = new ArrayList<>();
		lstBancos = new ArrayList<>();
		lstStatusFactura = new ArrayList<>();
		lstFactura = new ArrayList<>();
		lstPago = new ArrayList<>();
		lstFacturaFiltered = new ArrayList<>();
		lstFacturaSelected = new ArrayList<>();
		lstTipoPago = new ArrayList<>();

	}

	@PostConstruct
	public void init() {
		lstClientes = clienteDAO.buscarTodos();
		lstBancos = bancosDAO.buscarTodos();
		lstFactura = facturaDAO.buscarTodos();
		lstStatusFactura = statusFacturaDAO.buscarTodos();
		lstPago = pagoDAO.buscarTodos();
		lstTipoPago = tipoPagoDAO.buscarTodos();
	}

	/**
	 * Metodos de filtrado
	 */
	public void filtraListadoClientes() {
		if (lstFacturaFiltered.isEmpty() || lstFacturaFiltered == null) {
			lstFacturaFiltered = lstFactura;
		}
		lstFacturaFiltered = lstFactura.stream()
				.filter(ps -> clienteSelected != null
						? (ps.getCliente().getCteCve().intValue() == clienteSelected.getCteCve().intValue())
						: false)
				.collect(Collectors.toList());
	}

	public void filtraListadoBancos() {
		if (bancosSelected != null) {
			if (lstFacturaFiltered.isEmpty() || lstFacturaFiltered == null) {
				lstFacturaFiltered = lstFactura;
			}
			List<Factura> lstFacturaAux = new ArrayList<>();
			for (Factura f : lstFacturaFiltered) {
				boolean borraFactura = true;
				if (!f.getPagoList().isEmpty() && f.getPagoList() != null) {
					for (Pago p : f.getPagoList()) {
						if (p.getBanco() != null && p.getBanco().getClave() == bancosSelected.getClave()) {
							borraFactura = false;
						}
					}
				}
				if (borraFactura) {
					lstFacturaAux.add(f);
				}
			}
			lstFacturaFiltered.removeAll(lstFacturaAux);
		}
	}

	public void filtraListadoStatus() {
		if (statusFacturaSelected != null) {
			if (lstFacturaFiltered.isEmpty() || lstFacturaFiltered == null) {
				lstFacturaFiltered = lstFactura;
			}
			lstFacturaFiltered = lstFacturaFiltered.stream()
					.filter(ps -> statusFacturaSelected != null
							? (ps.getStatus().getId().intValue() == statusFacturaSelected.getId().intValue())
							: false)
					.collect(Collectors.toList());
		}
	}

	public void filtraListadoFecha() {
		if (lstFacturaFiltered.isEmpty() || lstFacturaFiltered == null) {
			lstFacturaFiltered = lstFactura;
		}
		List<Factura> lstFacturaAux = new ArrayList<>();
		if (customDate != null) {
			for (Factura f : lstFacturaFiltered) {
				if (f.getFecha().compareTo(customDate) != 0) {
					System.out.println(f.getFecha());
					System.out.println(customDate);
					lstFacturaAux.add(f);
				}
			}
			lstFacturaFiltered.removeAll(lstFacturaAux);
		} /*
			 * Date fechaSelected = generaFecha(customDate); lstFacturaFiltered =
			 * lstFacturaFiltered.stream() .filter(ps -> customDate != null ? (ps.getFecha()
			 * == customDate) : false) .collect(Collectors.toList());
			 */
	}

	public void filtraSaldos() {
		for (int i = 0; i < lstFacturaFiltered.size(); i++) {
			BigDecimal totalAux = lstFacturaFiltered.get(i).getTotal()
					.subtract(calculaSaldoTotal(lstFacturaFiltered.get(i)));
			if (totalAux.compareTo(BigDecimal.ZERO)==0) {
				StatusFactura statusAux = new StatusFactura();
				statusAux.setId(3);
				lstFacturaFiltered.get(i).setStatus(statusAux);
				facturaDAO.actualizaStatus(lstFacturaFiltered.get(i));
			}
		}
		filtraFacturas();
	}

	public void filtraFacturas() {
		filtraListadoClientes();
		filtraListadoStatus();
		filtraListadoFecha();
		PrimeFaces.current().ajax().update("form:dt-facturas");
	}

	public void limpiaFiltro() {
		clienteSelected = null;
		bancosSelected = null;
		statusFacturaSelected = null;
		customDate = null;
		lstFacturaSelected = null;
		PrimeFaces.current().ajax().update("form:dt-facturas");

	}

	public void calculaPago() {
		BigDecimal pagoTotAux = new BigDecimal(0);
		for (Factura f : lstFacturaFiltered) {
			for (Pago p : f.getPagoList()) {
				pagoTotAux = pagoTotAux.add(p.getMonto());

			}
		}
		pagosTotales = pagoTotAux;
	}

	/**
	 * Métodos para procesar el pago
	 */
	public void procesaPago() {
		List<String> errores = new ArrayList<>();
		for (Pago p : lstPago) {
			// Se elimina dato de saldo de la factura
			p.getFactura().setObservacion("");
			p.setFecha(new Date());
			errores.add(pagoDAO.guardar(p));
		}
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Pago Procesado"));
		for (String e : errores) {
			if (e != null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error al guardar el Pago"));
			}
		}
		filtraSaldos();
		PrimeFaces.current().ajax().update("form:messages form:dt-facturas form:panel-pago");
	}

	/**
	 * Métodos de control
	 */
	public boolean hasFacturaSelected() {
		return this.lstFacturaSelected != null && !this.lstFacturaSelected.isEmpty();
	}

	public String getBotonPagoMessage() {
		if (hasFacturaSelected()) {
			int size = this.lstFacturaSelected.size();
			return size > 1 ? "Pagar " + size + " Facturas" : "Pagar 1 Factura";
		}
		return "Pago";
	}

	public void actualizaBoton() {
		lstPago = new ArrayList<>();
		for (Factura f : lstFacturaSelected) {
			Pago pagoAux = new Pago();
			this.calculaSaldo();
			pagoAux.setFactura(f);
			BigDecimal saldoAux = calculaSaldoTotal(f);
			if (saldoAux == new BigDecimal(0)) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error al guardar el Pago"));
			}
			lstPago.add(pagoAux);
		}
		PrimeFaces.current().ajax().update("form:panel-pago");
	}

	/**
	 * Método para calcular saldo Se utiliza un campo de Factura que no es enviado a
	 * base de datos para poder mostrarlo en el campo de Saldo sin necesidad de
	 * agregar campos al objeto Factura
	 */
	public void calculaSaldo() {
		if (!lstFacturaSelected.isEmpty() && lstFacturaSelected != null) {
			for (Factura f : lstFacturaSelected) {
				BigDecimal saldo = calculaSaldoTotal(f);
				// Se usa el campo Observación para mostrar el saldo
				saldo = f.getTotal().subtract(saldo);
				f.setObservacion(saldo.toString());
			}
		}
	}

	// List<Pago> pagoFactura = pagoDAO.buscaPorFactura(f);

	private BigDecimal calculaSaldoTotal(Factura f) {
		BigDecimal saldo = new BigDecimal(0);
		List<Pago> pagoFactura = pagoDAO.buscaPorFactura(f);
		for (Pago p : pagoFactura) {
			saldo = saldo.add(p.getMonto());
		}
		return saldo;
	}
	
	/**
	 * Método para actualizar monto a pagar total
	 */
	
	public void actualizaMontoPago() {
		montoPago = new BigDecimal(0);
		for(Pago p: lstPago) {
			if(p.getMonto()!=null) {
				montoPago = montoPago.add(p.getMonto());
			}
		}
		PrimeFaces.current().ajax().update("form:montoPago");
//		montoPago = new BigDecimal(0);
	}

	/**
	 * Getters y Setters
	 */
	public List<Cliente> getLstClientes() {
		return lstClientes;
	}

	public void setLstClientes(List<Cliente> lstClientes) {
		this.lstClientes = lstClientes;
	}

	public Cliente getClienteSelected() {
		return clienteSelected;
	}

	public void setClienteSelected(Cliente clienteSelected) {
		this.clienteSelected = clienteSelected;
	}

	public ClienteDAO getClienteDAO() {
		return clienteDAO;
	}

	public void setClienteDAO(ClienteDAO clienteDAO) {
		this.clienteDAO = clienteDAO;
	}

	public List<Bancos> getLstBancos() {
		return lstBancos;
	}

	public void setLstBancos(List<Bancos> lstBancos) {
		this.lstBancos = lstBancos;
	}

	public Bancos getBancosSelected() {
		return bancosSelected;
	}

	public void setBancosSelected(Bancos bancosSelected) {
		this.bancosSelected = bancosSelected;
	}

	public BancoDAO getBancosDAO() {
		return bancosDAO;
	}

	public void setBancosDAO(BancoDAO bancosDAO) {
		this.bancosDAO = bancosDAO;
	}

	public List<Factura> getLstFactura() {
		return lstFactura;
	}

	public void setLstFactura(List<Factura> lstFactura) {
		this.lstFactura = lstFactura;
	}

	public Factura getFacturaSelected() {
		return facturaSelected;
	}

	public void setFacturaSelected(Factura facturaSelected) {
		this.facturaSelected = facturaSelected;
	}

	public FacturaDAO getFacturaDAO() {
		return facturaDAO;
	}

	public void setFacturaDAO(FacturaDAO facturaDAO) {
		this.facturaDAO = facturaDAO;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public List<Producto> getListProducto() {
		return listProducto;
	}

	public void setListProducto(List<Producto> listProducto) {
		this.listProducto = listProducto;
	}

	public Producto getProductoSelected() {
		return productoSelected;
	}

	public void setProductoSelected(Producto productoSelected) {
		this.productoSelected = productoSelected;
	}

	public ProductoDAO getProductoDAO() {
		return productoDAO;
	}

	public void setProductoDAO(ProductoDAO productoDAO) {
		this.productoDAO = productoDAO;
	}

	public Date getCustomDate() {
		return customDate;
	}

	public void setCustomDate(Date customDate) {
		this.customDate = customDate;
	}

	public List<StatusFactura> getLstStatusFactura() {
		return lstStatusFactura;
	}

	public void setLstStatusFactura(List<StatusFactura> lstStatusFactura) {
		this.lstStatusFactura = lstStatusFactura;
	}

	public StatusFactura getStatusFacturaSelected() {
		return statusFacturaSelected;
	}

	public void setStatusFacturaSelected(StatusFactura statusFacturaSelected) {
		this.statusFacturaSelected = statusFacturaSelected;
	}

	public StatusFacturaDAO getStatusFacturaDAO() {
		return statusFacturaDAO;
	}

	public void setStatusFacturaDAO(StatusFacturaDAO statusFacturaDAO) {
		this.statusFacturaDAO = statusFacturaDAO;
	}

	public List<Pago> getLstPago() {
		return lstPago;
	}

	public void setLstPago(List<Pago> lstPago) {
		this.lstPago = lstPago;
	}

	public Pago getPagoSelected() {
		return pagoSelected;
	}

	public void setPagoSelected(Pago pagoSelected) {
		this.pagoSelected = pagoSelected;
	}

	public PagoDAO getPagoDAO() {
		return pagoDAO;
	}

	public void setPagoDAO(PagoDAO pagoDAO) {
		this.pagoDAO = pagoDAO;
	}

	public List<TipoPago> getLstTipoPago() {
		return lstTipoPago;
	}

	public void setLstTipoPago(List<TipoPago> lstTipoPago) {
		this.lstTipoPago = lstTipoPago;
	}

	public TipoPago getTipoPagoSelected() {
		return tipoPagoSelected;
	}

	public void setTipoPagoSelected(TipoPago tipoPagoSelected) {
		this.tipoPagoSelected = tipoPagoSelected;
	}

	public TipoPagoDAO getTipoPagoDAO() {
		return tipoPagoDAO;
	}

	public void setTipoPagoDAO(TipoPagoDAO tipoPagoDAO) {
		this.tipoPagoDAO = tipoPagoDAO;
	}

	public BigDecimal getPagosTotales() {
		return pagosTotales;
	}

	public void setPagosTotales(BigDecimal pagosTotales) {
		this.pagosTotales = pagosTotales;
	}

	public List<Factura> getLstFacturaFiltered() {
		return lstFacturaFiltered;
	}

	public void setLstFacturaFiltered(List<Factura> lstFacturaFiltered) {
		this.lstFacturaFiltered = lstFacturaFiltered;
	}

	public List<Factura> getLstFacturaSelected() {
		return lstFacturaSelected;
	}

	public void setLstFacturaSelected(List<Factura> lstFacturaSelected) {
		this.lstFacturaSelected = lstFacturaSelected;
	}

	public BigDecimal getMontoPago() {
		return montoPago;
	}

	public void setMontoPago(BigDecimal montoPago) {
		this.montoPago = montoPago;
	}

}