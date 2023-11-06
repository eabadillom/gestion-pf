package mx.com.ferbo.controller;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

import com.ferbo.facturama.business.CfdiBL;
import com.ferbo.facturama.request.CFDIInfo;
import com.ferbo.facturama.request.IssuerBindingModel;
import com.ferbo.facturama.request.ItemFullBindingModel;
import com.ferbo.facturama.request.ReceiverBindingModel;
import com.ferbo.facturama.request.Tax;
import com.ferbo.facturama.response.CfdiInfoModel;
import com.ferbo.facturama.response.FileViewModel;
import com.ferbo.facturama.tools.FacturamaException;
import com.ferbo.mail.beans.Adjunto;

import mx.com.ferbo.business.FacturamaBL;
import mx.com.ferbo.business.SendMailFacturaBL;
import mx.com.ferbo.dao.AsentamientoHumandoDAO;
import mx.com.ferbo.dao.ClaveUnidadDAO;
import mx.com.ferbo.dao.DomiciliosDAO;
import mx.com.ferbo.dao.FacturaDAO;
import mx.com.ferbo.dao.PrecioServicioDAO;
import mx.com.ferbo.dao.SerieFacturaDAO;
import mx.com.ferbo.model.AsentamientoHumano;
import mx.com.ferbo.model.Aviso;
import mx.com.ferbo.model.Camara;
import mx.com.ferbo.model.ClaveUnidad;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ConstanciaDeDeposito;
import mx.com.ferbo.model.ConstanciaDepositoDetalle;
import mx.com.ferbo.model.ConstanciaFactura;
import mx.com.ferbo.model.ConstanciaFacturaDs;
import mx.com.ferbo.model.ConstanciaServicioDetalle;
import mx.com.ferbo.model.Domicilios;
import mx.com.ferbo.model.Factura;
import mx.com.ferbo.model.FacturaMedioPago;
import mx.com.ferbo.model.MedioPago;
import mx.com.ferbo.model.MetodoPago;
import mx.com.ferbo.model.Parametro;
import mx.com.ferbo.model.Partida;
import mx.com.ferbo.model.PartidaServicio;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.PrecioServicio;
import mx.com.ferbo.model.ProductoConstanciaDs;
import mx.com.ferbo.model.SerieFactura;
import mx.com.ferbo.model.Servicio;
import mx.com.ferbo.model.ServicioConstancia;
import mx.com.ferbo.model.ServicioConstanciaDs;
import mx.com.ferbo.model.TipoCobro;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.FormatUtil;
import mx.com.ferbo.util.InventarioException;
import mx.com.ferbo.util.JasperReportUtil;
import mx.com.ferbo.util.conexion;
import net.sf.jasperreports.engine.JRException;

@Named
@ViewScoped
public class CalculoPrevioBean implements Serializable {

	private static final long serialVersionUID = -1785488265380235016L;
	private static Logger log = LogManager.getLogger(CalculoPrevioBean.class);

	@Inject
	private FacturacionConstanciasBean facturacionBean;

	private FacesContext context;
	private HttpServletRequest request;
	HttpSession session;
 
	private List<ConstanciaFactura> listaEntradas;
	private List<ConstanciaFactura> listaVigencias;
	private List<ConstanciaFacturaDs> listaServicios;
	private List<ConstanciaFacturaDs> listaConstanciaFacturaDs;

	private PrecioServicioDAO precioServicioDAO;
	private AsentamientoHumandoDAO asentamientoDAO;
	private DomiciliosDAO domiciliosDAO;
	private FacturaDAO facturaDAO;
	private ClaveUnidadDAO claveDAO;
	//private FacturacionDepositosDAO facturacionConstanciasDAO;

	private Cliente clienteSelect;
	private Planta plantaSelect;
	private Domicilios domicilioSelect;
	private SerieFactura serieFacturaSelect;
	private Factura factura;
	private ServicioConstancia selectServicioE;
	private ServicioConstancia selectServicioV;
	private ServicioConstanciaDs selectServicioDs;
	private MedioPago medioPago;
	private MetodoPago metodoPago;
	private Parametro iva;
	private AsentamientoHumano asentamientoPlanta,asentamientoCliente;
	private Domicilios domicilioPlanta;

	private Date fechaEmision;
	private BigDecimal cantidad;
	private BigDecimal subTotalEntrada;
	private BigDecimal subTotalVigencias;
	private BigDecimal subTotalServicios;
	private BigDecimal subTotalGeneral;
	private BigDecimal ivaTotal;
	private BigDecimal total;
	private String montoLetra;
	private String moneda;
	private String observaciones;
	
	private Usuario usuario;

	public CalculoPrevioBean() {

		listaConstanciaFacturaDs = new ArrayList<>();
		listaEntradas = new ArrayList<>();
		listaServicios = new ArrayList<>();
		listaVigencias = new ArrayList<>();

		clienteSelect = new Cliente();
		plantaSelect = new Planta();
		domicilioSelect = new Domicilios();
		serieFacturaSelect = new SerieFactura();
		factura = new Factura();
		asentamientoPlanta = new AsentamientoHumano();
		selectServicioE = new ServicioConstancia();
		selectServicioV = new ServicioConstancia();
		selectServicioDs = new ServicioConstanciaDs();

		precioServicioDAO = new PrecioServicioDAO();
		asentamientoDAO = new AsentamientoHumandoDAO();
		domiciliosDAO = new DomiciliosDAO();
		facturaDAO = new FacturaDAO();
		claveDAO = new ClaveUnidadDAO();
	}

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		
		subTotalServicios = new BigDecimal(0);
		subTotalEntrada = new BigDecimal(0);
		subTotalVigencias = new BigDecimal(0);
		subTotalGeneral = new BigDecimal(0);
		ivaTotal = new BigDecimal(0);
		total = new BigDecimal(0);

		try {
			context = FacesContext.getCurrentInstance();
			request = (HttpServletRequest) context.getExternalContext().getRequest();
			session = request.getSession(false);
			listaEntradas = (List<ConstanciaFactura>) session.getAttribute("entradas");
			listaVigencias = (List<ConstanciaFactura>) session.getAttribute("vigencias");
			listaServicios = (List<ConstanciaFacturaDs>) session.getAttribute("servicios");
			clienteSelect = (Cliente) session.getAttribute("cliente");
			plantaSelect = (Planta) session.getAttribute("plantaSelect");
			factura = (Factura) session.getAttribute("factura");
			fechaEmision = (Date) session.getAttribute("fechaEmision");
			iva = (Parametro) session.getAttribute("iva"); 
			medioPago = (MedioPago) session.getAttribute("medioPago");
			metodoPago = (MetodoPago) session.getAttribute("metodoPago");
			domicilioSelect = (Domicilios) session.getAttribute("domicilioSelect");
			serieFacturaSelect = (SerieFactura) session.getAttribute("serieFacturaSelect");
			moneda = (String) session.getAttribute("moneda");
			observaciones = (String) session.getAttribute("Observaciones");
			this.usuario = (Usuario) session.getAttribute("usuario");
			

			if (listaEntradas.isEmpty()) {
				listaEntradas = new ArrayList<>();
			}

			if (listaVigencias.isEmpty()) {
				listaVigencias = new ArrayList<>();
			}

			if (listaServicios.isEmpty()) {
				listaServicios = new ArrayList<>();
			}

			procesarServicios();
			procesarEntradas();
			procesarVigencias();
			sumaGeneral();
			cargaDomicilio();
			

		} catch (Exception e) {
			System.out.println("ERROR Aqui" + e.getLocalizedMessage());
		}

	}
	public void NuevaConstancia() {
		FacesMessage message = null;
			try {
				ExternalContext externalContext = FacesContext.getCurrentInstance()
					    .getExternalContext();
				externalContext.redirect(externalContext.getRequestContextPath() + "/protected/catalogos/facturacionConstancias.xhtml");
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	public FacturacionConstanciasBean getFacturacionBean() {
		return facturacionBean;
	}

	public void setFacturacionBean(FacturacionConstanciasBean facturacionBean) {
		this.facturacionBean = facturacionBean;
	}

	public List<ConstanciaFactura> getListaEntradas() {
		return listaEntradas;
	}

	public void setListaEntradas(List<ConstanciaFactura> listaEntradas) {
		this.listaEntradas = listaEntradas;
	}

	public List<ConstanciaFactura> getListaVigencias() {
		return listaVigencias;
	}

	public void setListaVigencias(List<ConstanciaFactura> listaVigencias) {
		this.listaVigencias = listaVigencias;
	}

	public List<ConstanciaFacturaDs> getListaServicios() {
		return listaServicios;
	}

	public void setListaServicios(List<ConstanciaFacturaDs> listaServicios) {
		this.listaServicios = listaServicios;
	}

	public Cliente getClienteSelect() {
		return clienteSelect;
	}

	public void setClienteSelect(Cliente clienteSelect) {
		this.clienteSelect = clienteSelect;
	}

	public List<ConstanciaFacturaDs> getListaConstanciaFacturaDs() {
		return listaConstanciaFacturaDs;
	}

	public void setListaConstanciaFacturaDs(List<ConstanciaFacturaDs> listaConstanciaFacturaDs) {
		this.listaConstanciaFacturaDs = listaConstanciaFacturaDs;
	}

	public Date getFechaEmision() {
		return fechaEmision;
	}

	public void setFechaEmision(Date fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	public BigDecimal getCantidad() {
		return cantidad;
	}

	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}

	public Planta getPlantaSelect() {
		return plantaSelect;
	}

	public void setPlantaSelect(Planta plantaSelect) {
		this.plantaSelect = plantaSelect;
	}

	public Factura getFactura() {
		return factura;
	}

	public void setFactura(Factura factura) {
		this.factura = factura;
	}

	public ServicioConstanciaDs getSelectServicioDs() {
		return selectServicioDs;
	}

	public void setSelectServicioDs(ServicioConstanciaDs selectServicioDs) {
		this.selectServicioDs = selectServicioDs;
	}

	public ServicioConstancia getSelectServicioV() {
		return selectServicioV;
	}

	public void setSelectServicioV(ServicioConstancia selectServicioV) {
		this.selectServicioV = selectServicioV;
	}

	public ServicioConstancia getSelectServicioE() {
		return selectServicioE;
	}

	public void setSelectServicioE(ServicioConstancia selectServicioE) {
		this.selectServicioE = selectServicioE;
	}

	public BigDecimal getSubTotalEntrada() {
		return subTotalEntrada;
	}

	public void setSubTotalEntrada(BigDecimal subTotalEntrada) {
		this.subTotalEntrada = subTotalEntrada;
	}

	public BigDecimal getSubTotalVigencias() {
		return subTotalVigencias;
	}

	public void setSubTotalVigencias(BigDecimal subTotalVigencias) {
		this.subTotalVigencias = subTotalVigencias;
	}
	
	public BigDecimal getSubTotalServicios() {
		return subTotalServicios;
	}

	public void setSubTotalServicios(BigDecimal subTotalServicios) {
		this.subTotalServicios = subTotalServicios;
	}
	
	public BigDecimal getSubTotalGeneral() {
		return subTotalGeneral;
	}

	public void setSubTotalGeneral(BigDecimal subTotalGeneral) {
		this.subTotalGeneral = subTotalGeneral;
	}

	public BigDecimal getIvaTotal() {
		return ivaTotal;
	}

	public void setIvaTotal(BigDecimal ivaTotal) {
		this.ivaTotal = ivaTotal;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public String getMontoLetra() {
		return montoLetra;
	}

	public void setMontoLetra(String montoLetra) {
		this.montoLetra = montoLetra;
	} 

	public MedioPago getMedioPago() {
		return medioPago;
	}

	public void setMedioPago(MedioPago medioPago) {
		this.medioPago = medioPago;
	}

	public MetodoPago getMetodoPago() {
		return metodoPago;
	}

	public void setMetodoPago(MetodoPago metodoPago) {
		this.metodoPago = metodoPago;
	}
	
	public Domicilios getDomicilioSelect() {
		return domicilioSelect;
	}

	public void setDomicilioSelect(Domicilios domicilioSelect) {
		this.domicilioSelect = domicilioSelect;
	}

	public SerieFactura getSerieFacturaSelect() {
		return serieFacturaSelect;
	}

	public void setSerieFacturaSelect(SerieFactura serieFacturaSelect) {
		this.serieFacturaSelect = serieFacturaSelect;
	}

	public AsentamientoHumano getAsentamientoPlanta() {
		return asentamientoPlanta;
	}

	public void setAsentamientoPlanta(AsentamientoHumano asentamientoPlanta) {
		this.asentamientoPlanta = asentamientoPlanta;
	}

	public AsentamientoHumano getAsentamientoCliente() {
		return asentamientoCliente;
	}

	public void setAsentamientoCliente(AsentamientoHumano asentamientoCliente) {
		this.asentamientoCliente = asentamientoCliente;
	}

	public Domicilios getDomicilioPlanta() {
		return domicilioPlanta;
	}

	public void setDomicilioPlanta(Domicilios domicilioPlanta) {
		this.domicilioPlanta = domicilioPlanta;
	}

	public void procesarServicios() {

		BigDecimal importe = new BigDecimal(0);
		BigDecimal sumTmp;
		Integer idS = 0,idP = 0;
		
		for (ConstanciaFacturaDs cfd : listaServicios) {

			List<ConstanciaServicioDetalle> listaConstanciaSD = cfd.getConstanciaDeServicio()
					.getConstanciaServicioDetalleList();
			List<PartidaServicio> listaPartidaServicio = cfd.getConstanciaDeServicio().getPartidaServicioList();

			cfd.setServicioConstanciaDsList(new ArrayList<>());
			cfd.setProductoConstanciaDsList(new ArrayList<>());

			for (ConstanciaServicioDetalle csd : listaConstanciaSD) {

				cantidad = new BigDecimal(0);

				List<PrecioServicio> listaPrecioS = csd.getServicioCve().getPrecioServicioList();
				PrecioServicio precioServicio = getPrecioServicio(clienteSelect.getCteCve(), listaPrecioS);

				ServicioConstanciaDs servicioConstanciaDs = new ServicioConstanciaDs();
				
				servicioConstanciaDs.setId(idS);
				servicioConstanciaDs.setDescripcion(csd.getServicioCve().getServicioDs());
				servicioConstanciaDs.setConstancia(cfd);

				if (precioServicio.getPrecio() != null) {// modificado por error null al llamar al metodo
															// getPrecioServicio
					cantidad = precioServicio.getPrecio().setScale(2);// variable agregada
					importe = csd.getServicioCantidad().multiply(cantidad);

					servicioConstanciaDs.setCosto(importe.setScale(2));
					servicioConstanciaDs.setTarifa(precioServicio.getPrecio().setScale(2));
				}
				servicioConstanciaDs.setCodigo(csd.getServicioCve().getServicioCod());
				servicioConstanciaDs.setUdCobro("SRV");
				servicioConstanciaDs.setCdUnidad(csd.getServicioCve().getCdUnidad());
				servicioConstanciaDs.setCantidad(csd.getServicioCantidad());

				cfd.getServicioConstanciaDsList().add(servicioConstanciaDs);
				idS++;
			}

			for (PartidaServicio ps : listaPartidaServicio) {

				ProductoConstanciaDs productoConstanciaDs = new ProductoConstanciaDs();

				productoConstanciaDs.setId(idP);
				productoConstanciaDs.setDescripcion(ps.getProductoCve().getProductoDs());
				productoConstanciaDs.setConstancia(cfd);
				productoConstanciaDs.setCatidadCobro(ps.getCantidadDeCobro());
				productoConstanciaDs.setUnidadCobro(ps.getUnidadDeCobro().getUnidadDeManejoDs());
				productoConstanciaDs.setCantidadManejo(new BigDecimal(ps.getCantidadTotal()).setScale(2));
				productoConstanciaDs.setUnidadManejo(ps.getUnidadDeManejoCve().getUnidadDeManejoDs());
				
				cfd.getProductoConstanciaDsList().add(productoConstanciaDs);
				idP++;
			}
			
			cfd.setFactura(factura);
			sumTmp = subTotalServicios(cfd.getServicioConstanciaDsList());
			subTotalServicios = subTotalServicios.add(sumTmp);
		
		}

	}

	private PrecioServicio getPrecioServicio(Integer idCliente, List<PrecioServicio> listaPrecioS) {

		List<PrecioServicio> listaPrecioSTemp = null;
		PrecioServicio precioServicio = new PrecioServicio();

		listaPrecioSTemp = listaPrecioS.stream().filter(ps -> ps.getCliente().getCteCve().intValue() == idCliente)
				.collect(Collectors.toList());

		if (!(listaPrecioSTemp.isEmpty())) { // modificado por error de retornar objeto precioServicio en null
			Optional<PrecioServicio> precioServicioMax = listaPrecioSTemp.stream()
					.max((i, j) -> i.getAvisoCve().getAvisoCve().compareTo(j.getAvisoCve().getAvisoCve()));
			precioServicio = precioServicioMax.get();// colocar condicional, si y solo si precioServicioMax != null
		}

		return precioServicio;
	}

	// ENTRADAS (CONSTANCIA DE DEPOSITO)

	public void procesarEntradas() {

		Integer id = 0;
		BigDecimal sumTmp;
		
		for (ConstanciaFactura cf : listaEntradas) {

			ConstanciaDeDeposito cdd = cf.getFolio();
			// listaServiciosConstancias = new ArrayList<>();
			Aviso aviso = cdd.getAvisoCve();
			Camara camara = cdd.getPartidaList().get(0).getCamaraCve();
			String tipoFacturacion = aviso.getAvisoTpFacturacion();
			cf.setServicioConstanciaList(new ArrayList<>());

			for (ConstanciaDepositoDetalle cs : cdd.getConstanciaDepositoDetalleList()) {

				// System.out.println(cs.getServicioCve().getCobro().getId());
				Servicio servicio = cs.getServicioCve();
				TipoCobro tipoCobro = servicio.getCobro();
				ServicioConstancia sc = new ServicioConstancia();

				BigDecimal importe = new BigDecimal(0);
				BigDecimal cantidad = null;

				/*PrecioServicio precioServicio = precioServicioDAO.busquedaServicio(cdd.getAvisoCve().getAvisoCve(),
						clienteSelect.getCteCve(), servicio.getServicioCve());*/
				List <PrecioServicio> listaPrecioServicio = precioServicioDAO.busquedaServicio(cdd.getAvisoCve().getAvisoCve(),
						clienteSelect.getCteCve(), servicio.getServicioCve());
				PrecioServicio precioServicio = null;
				if(!listaPrecioServicio.isEmpty()) {
				precioServicio = listaPrecioServicio.get(0);
				
				switch (tipoCobro.getId()) {

				case 1:
				case 2:

					cantidad = cs.getServicioCantidad();
					importe = cantidad.multiply(precioServicio.getPrecio());
					sc.setCosto(importe.setScale(2));
					sc.setUnidadMedida("SRV");
					System.out.println("El tipo cobro es 1 o 2 y su importe es: " + importe);
					break;

				case 3:
				case 4:

					cantidad = getCantidadPartidas(cdd.getPartidaList(), tipoFacturacion);

					importe = cantidad.multiply(precioServicio.getPrecio());
					sc.setCosto(importe.setScale(2));
					System.out.println("El tipo de cobro es 3 o 4 y su importe es entradas: " + importe);

					if (aviso.getAvisoTpFacturacion().equals("T")) {
						sc.setUnidadMedida("TRM");
					} else {
						sc.setUnidadMedida("KG");
					}

					break;

				default:

					System.out.println("No existe el tipo de cobro");
					break;
				}

				sc.setId(id);
				sc.setConstancia(cf);
				sc.setTarifa(precioServicio.getPrecio().setScale(2));
				sc.setBaseCargo(cantidad.setScale(2));
				sc.setDescripcion(cs.getServicioCve().getServicioDs());
				sc.setPlantaCve(plantaSelect.getPlantaCve());
				sc.setPlantaDs(plantaSelect.getPlantaDs());
				sc.setPlantaAbrev(plantaSelect.getPlantaAbrev());
				sc.setPlantaCod(plantaSelect.getPlantaCod());

				
				sc.setCamaraCve(camara.getCamaraCve());
				sc.setCamaraDs(camara.getCamaraDs());
				sc.setCamaraAbrev(camara.getCamaraAbrev());
				sc.setCodigo(precioServicio.getServicio().getServicioCod());
				sc.setCdUnidad(precioServicio.getServicio().getCdUnidad());
				/*
				 * listaServiciosConstancias.add(sc);
				 * cf.setServicioConstanciaList(listaServiciosConstancias);
				 */
				cf.getServicioConstanciaList().add(sc); // datos a mostrar row ex--
				// listaServiciosEntradas.add(sc);
				id++;

			}
			}
			//Camara camara = cdd.getPartidaList().get(0).getCamaraCve();
			
			cf.setFactura(factura);
			cf.setPlantaCve(plantaSelect.getPlantaCve());
			cf.setPlantaDs(plantaSelect.getPlantaDs());
			cf.setPlantaAbrev(plantaSelect.getPlantaAbrev());
			cf.setCamaraCve(camara.getCamaraCve());
			cf.setCamaraDs(camara.getCamaraDs());
			cf.setCamaraAbrev(camara.getCamaraAbrev());
			
			sumTmp = subTotalEntradaVigencias(cf.getServicioConstanciaList()).setScale(2);
			subTotalEntrada = subTotalEntrada.add(sumTmp);
			
			cdd.setConstanciaFacturaList(new ArrayList<>());
			cdd.getConstanciaFacturaList().addAll(listaEntradas);
			cdd.getConstanciaFacturaList().addAll(listaVigencias);
			
		}
	}

	public BigDecimal getCantidadPartidas(List<Partida> listaPartidas, String tipoFacturacion) {

		BigDecimal cantidad = new BigDecimal(0).setScale(3, BigDecimal.ROUND_HALF_UP);

		for (Partida p : listaPartidas) {

			if (tipoFacturacion.equals("T")) {
				cantidad = cantidad.add(p.getNoTarimas());
				// cantidad = p.getNoTarimas();
			} else {
				cantidad = cantidad.add(p.getPesoTotal());
				log.info("Peso total: {}" + p.getPesoTotal());
				log.info("Cantidad: " + cantidad);
			}

		}

		return cantidad;
	}

	// VIGENCIAS (CONSTANCIA DE DEPOSITO)

	public void procesarVigencias() {

		Integer id = 0;
		BigDecimal importe = new BigDecimal(0);
		BigDecimal sumTmp;
		// List<ServicioConstancia> listaServiciosConstancias = null;

		for (ConstanciaFactura cf : listaVigencias) {

			ConstanciaDeDeposito cdd = cf.getFolio();
			// listaServiciosConstancias = new ArrayList<>();
			Aviso aviso = cdd.getAvisoCve();
			Camara camara = cdd.getPartidaList().get(0).getCamaraCve();
			String tipoFacturacion = aviso.getAvisoTpFacturacion();
			cf.setServicioConstanciaList(new ArrayList<>());

			for (ConstanciaDepositoDetalle cs : cdd.getConstanciaDepositoDetalleList()) {

				// System.out.println(cs.getServicioCve().getCobro().getId());
				Servicio servicio = cs.getServicioCve();
				TipoCobro tipoCobro = servicio.getCobro();
				ServicioConstancia sc = new ServicioConstancia();

				BigDecimal cantidad = null;

				List<PrecioServicio> listaPrecioServicio = precioServicioDAO.busquedaServicio(cdd.getAvisoCve().getAvisoCve(),
						clienteSelect.getCteCve(), servicio.getServicioCve());
				
				if(!listaPrecioServicio.isEmpty()) {
				PrecioServicio precioServicio = listaPrecioServicio.get(0);
				
				switch (tipoCobro.getId()) {

				case 1:
				case 2:
				case 3:
					break;
				case 4:

					cantidad = getCantidadPartidas(cdd.getPartidaList(), tipoFacturacion);
					importe = cantidad.multiply(precioServicio.getPrecio()).setScale(2, BigDecimal.ROUND_HALF_UP);
					log.debug("El tipo de cobro es 3 o 4 y su importe es: " + importe);
					sc.setCosto(importe);
					

					if (aviso.getAvisoTpFacturacion().equals("T")) {
						sc.setUnidadMedida("TRM");
					} else {
						sc.setUnidadMedida("KG");
					}

					break;

				default:

					System.out.println("No existe el tipo de cobro");
					break;
				}
				sc.setId(id);
				sc.setConstancia(cf);
				sc.setTarifa(precioServicio.getPrecio().setScale(2));
				if(cantidad!=null) {
					sc.setBaseCargo(cantidad.setScale(2));
				}
				sc.setDescripcion(cs.getServicioCve().getServicioDs());
				sc.setPlantaCve(plantaSelect.getPlantaCve());
				sc.setPlantaDs(plantaSelect.getPlantaDs());
				sc.setPlantaAbrev(plantaSelect.getPlantaAbrev());
				sc.setPlantaCod(plantaSelect.getPlantaCod());

				
				sc.setCamaraCve(camara.getCamaraCve());
				sc.setCamaraDs(camara.getCamaraDs());
				sc.setCamaraAbrev(camara.getCamaraAbrev());				
				sc.setCodigo(precioServicio.getServicio().getServicioCod());
				sc.setCdUnidad(precioServicio.getServicio().getCdUnidad());

				if (tipoCobro.getId() == 4) {
					cf.getServicioConstanciaList().add(sc);					
					id++;
				}
				/*
				 * listaServiciosConstancias.add(sc);
				 * cf.setServicioConstanciaList(listaServiciosConstancias);//datos a mostrar row
				 * ex--
				 */
				}
			}
			
			//Camara camara = cdd.getPartidaList().get(0).getCamaraCve();
			
			cf.setFactura(factura);
			cf.setFactura(factura);
			cf.setPlantaCve(plantaSelect.getPlantaCve());
			cf.setPlantaDs(plantaSelect.getPlantaDs());
			cf.setPlantaAbrev(plantaSelect.getPlantaAbrev());
			cf.setCamaraCve(camara.getCamaraCve());
			cf.setCamaraDs(camara.getCamaraDs());
			cf.setCamaraAbrev(camara.getCamaraAbrev());
			
			cdd.setConstanciaFacturaList(new ArrayList<>());
			cdd.getConstanciaFacturaList().addAll(listaEntradas);
			cdd.getConstanciaFacturaList().addAll(listaVigencias);
			
			sumTmp = subTotalEntradaVigencias(cf.getServicioConstanciaList()).setScale(2);
			subTotalVigencias = subTotalVigencias.add(sumTmp);
			
		}
	}

	
	public void eliminarServicioEntrada() {

		for (ConstanciaFactura cf : listaEntradas) {
			
			if(cf.getFolio().getFolioCliente().equals(selectServicioE.getConstancia().getFolio().getFolioCliente())) {
			
				for (ServicioConstancia sc : cf.getServicioConstanciaList()) {
	
					if (sc.getDescripcion().equals(selectServicioE.getDescripcion())) {
						cf.getServicioConstanciaList().remove(selectServicioE);
						break;
					}
					
				}
			}
			
		}
		
		recalculoEntradas();
		
		PrimeFaces.current().ajax().update("form:dt-selectedEntradas:dt-serviciosEntrada");

	}
	
	
	public void eliminarServicioVigencia() {
		
		for (ConstanciaFactura cf : listaVigencias) {
			
			if(cf.getFolio().getFolioCliente().equals(selectServicioV.getConstancia().getFolio().getFolioCliente())) {
				for (ServicioConstancia sc : cf.getServicioConstanciaList()) {
						if (sc.getDescripcion().equals(selectServicioV.getDescripcion())) {
							cf.getServicioConstanciaList().remove(selectServicioV);
							break;
						}
					}
			}
			
		}
		
		recalculoVigencias();
		
		PrimeFaces.current().ajax().update("form:dt-selectVigencias:dt-serviciosVigencia");
		
	}
	
	public void eliminarServicioDs() {
		
		for(ConstanciaFacturaDs cfd: listaServicios) {
			
			if(cfd.getFolioCliente().equals(selectServicioDs.getConstancia().getFolioCliente())) {
			
				for(ServicioConstanciaDs sc: cfd.getServicioConstanciaDsList()) {
					
					if(sc.getDescripcion().equals(selectServicioDs.getDescripcion())) {
						cfd.getServicioConstanciaDsList().remove(selectServicioDs);
						break;
					}
				}
			}
			
		}
		
		recalculoServicioDs();//llamo al metodo ya con lista actualizada 
		
		PrimeFaces.current().ajax().update("form:dt-constanciaFacturaDs:dt-serviciosDs");
		
	}
	
	//Calculo el subTotal antes de eliminar un servicio
	
	public BigDecimal subTotalEntradaVigencias(List<ServicioConstancia> lista) {
		
		BigDecimal subTotal = new BigDecimal(0);
		
		for(ServicioConstancia sc: lista) {
			subTotal = subTotal.add(sc.getCosto());
		}
		
		return subTotal;
	}
	
	public BigDecimal subTotalServicios(List<ServicioConstanciaDs> lista) {
		BigDecimal subTotal = new BigDecimal(0);
		
		for(ServicioConstanciaDs scd: lista) {
			subTotal = subTotal.add(scd.getCosto());
		}
		
		return subTotal;
		
	}
	
	public void recalculoServicioDs() {//calculo el subtotal con la lista actualizada despues de eliminar un servicio 
		
		subTotalServicios = new BigDecimal(0);
		BigDecimal sum;
		
		for(ConstanciaFacturaDs cf: listaServicios) {
			
			sum = subTotalServicios(cf.getServicioConstanciaDsList());	
			subTotalServicios = subTotalServicios.add(sum);
		}
		
		sumaGeneral();
		
	}
	
	public void recalculoEntradas() {
		
		subTotalEntrada = new BigDecimal(0);
		
		BigDecimal sum;
		
		for(ConstanciaFactura cf: listaEntradas) {
			
			sum = subTotalEntradaVigencias(cf.getServicioConstanciaList());
			subTotalEntrada = subTotalEntrada.add(sum);
			
		}
		
		sumaGeneral();
		
	}
	
	public void recalculoVigencias() {
		
		subTotalVigencias = new BigDecimal(0);
		
		BigDecimal sum;
		
		for(ConstanciaFactura cf: listaVigencias) {
			
			sum = subTotalEntradaVigencias(cf.getServicioConstanciaList());
			subTotalVigencias = subTotalVigencias.add(sum);
			
		}
		
		sumaGeneral();
		
	}
	
	public void sumaGeneral() {
		
		subTotalGeneral = subTotalEntrada.add(subTotalServicios.add(subTotalVigencias)).setScale(2,BigDecimal.ROUND_HALF_UP);
		
		ivaTotal = subTotalGeneral.multiply(new BigDecimal(iva.getValor())).setScale(2, BigDecimal.ROUND_HALF_UP);
		
		total = subTotalGeneral.add(ivaTotal).setScale(2,BigDecimal.ROUND_HALF_UP);
		
		FormatUtil formato = new FormatUtil();
		
		montoLetra = formato.getMontoConLetra(total.doubleValue());
		
		factura.setSubtotal(subTotalGeneral);
		factura.setIva(ivaTotal);
		factura.setTotal(total);
		factura.setMontoLetra(montoLetra);
		factura.setConstanciaFacturaList(new ArrayList<>());
		factura.setConstanciaFacturaDsList(new ArrayList<>());
		
		factura.getConstanciaFacturaList().addAll(listaEntradas);
		
		factura.getConstanciaFacturaList().addAll(listaVigencias);
		
		factura.setConstanciaFacturaDsList(listaServicios);
			
	}
	
	public void reload() throws IOException {
	    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	    ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
	}
	public void cargaDomicilio() {
		
		asentamientoCliente = asentamientoDAO.buscarPorAsentamiento(domicilioSelect.getCiudades().getCiudadesPK().getPaisCve(),
			    domicilioSelect.getCiudades().getMunicipios().getEstados().getEstadosPK().getEstadoCve(), 
				domicilioSelect.getCiudades().getMunicipios().getMunicipiosPK().getMunicipioCve(),
				domicilioSelect.getCiudades().getCiudadesPK().getCiudadCve(), domicilioSelect.getDomicilioColonia());
		
		asentamientoPlanta = asentamientoDAO.buscarPorAsentamiento(plantaSelect.getIdPais(), plantaSelect.getIdEstado(), plantaSelect.getIdMunicipio(), plantaSelect.getIdCiudad(), plantaSelect.getIdAsentamiento());
		
		domicilioPlanta = domiciliosDAO.buscarPorAsentamiento(plantaSelect.getIdPais(), plantaSelect.getIdEstado(), plantaSelect.getIdMunicipio(), plantaSelect.getIdCiudad(), plantaSelect.getIdAsentamiento());
		
		System.out.println(asentamientoPlanta); 
		
	}
	
	
	public void saveFactura() {
		
		//haciendo null id de los servicios constancias de constancias de deposito 
		
		for(ConstanciaFactura cf: listaEntradas) {
			for(ServicioConstancia sc: cf.getServicioConstanciaList()) {
				sc.setId(null);
			}
		}
		
		for(ConstanciaFactura cf: listaVigencias) {
			for(ServicioConstancia sc: cf.getServicioConstanciaList()) {
				sc.setId(null);
			}
		}
		
		//haciendo null id de serviciosDs  y productosDs de constancia servicios 
		
		for(ConstanciaFacturaDs cfd: listaServicios) {
			for(ServicioConstanciaDs scd: cfd.getServicioConstanciaDsList()) {
				scd.setId(null);
			}
			
			for(ProductoConstanciaDs pcd: cfd.getProductoConstanciaDsList()) {
				pcd.setId(null);
			}	
			
		}
		
		try {
			
			if(factura.getId()==null) {
				
				
				SerieFacturaDAO serieDAO = new SerieFacturaDAO();
				
				this.serieFacturaSelect.setNumeroActual(serieFacturaSelect.getNumeroActual() + 1);
				
				facturaDAO.guardar(factura);
				
				serieDAO.update(this.serieFacturaSelect);
				
				
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Registro Exitoso", "La factura se guardo correctamente"));
				PrimeFaces.current().ajax().update("form:messages");
			}
			
			//cerrarSesion();
			
		} catch (Exception e) {
			System.out.println("Error al guardar Factura" + e.getMessage());
		}
		
		
		
	}
	
	public void jasper() throws JRException, IOException, SQLException {
		String jasperPath = "/jasper/Factura.jrxml";
		String filename = "Factura " + fechaEmision + ".pdf";
		String images = "/images/logo.jpeg";
		String message = null;
		Severity severity = null;
		Factura sf = null;
		File reportFile = new File(jasperPath);
		File imgfile = null;
		JasperReportUtil jasperReportUtil = new JasperReportUtil();
		Map<String, Object> parameters = new HashMap<String, Object>();
		Connection connection = null;
		parameters = new HashMap<String, Object>();
		try {
			
			if (this.factura.getId() == null)
				throw new InventarioException("Debe Guardar la Factura primero");
			
			URL resource = getClass().getResource(jasperPath);
			URL resourceimg = getClass().getResource(images);
			String file = resource.getFile();
			String img = resourceimg.getFile();
			reportFile = new File(file);
			imgfile = new File(img);
			//log.info(reportFile.getPath());
			sf = new Factura();
			Integer num = factura.getId();
			connection = EntityManagerUtil.getConnection();
			parameters.put("REPORT_CONNECTION", connection);
			parameters.put("idFactura", num);
			parameters.put("imagen", imgfile.getPath());
			//log.info("Parametros: " + parameters.toString());
			jasperReportUtil.createPdf(filename, parameters, reportFile.getPath());
		} catch (InventarioException ex) {
			ex.fillInStackTrace();
			//log.error("Problema general...", ex);
			message = String.format("Guarda primero la factura para poder realizar la impresion");
			severity = FacesMessage.SEVERITY_ERROR;
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(severity, "Error en impresion", message));
			PrimeFaces.current().ajax().update("form:messages");
		} finally {
			conexion.close((Connection) connection);
		}

	}
	
	public String paginaFactura() {
		
		session.removeAttribute("entradas");
		session.removeAttribute("vigencias");
		session.removeAttribute("servicios");
		session.removeAttribute("cliente");
		session.removeAttribute("plantaSelect");
		session.removeAttribute("factura");
		session.removeAttribute("fechaEmision");
		session.removeAttribute("iva");
		session.removeAttribute("medioPago");
		session.removeAttribute("metodoPago");
		session.removeAttribute("domicilioSelect");
		session.removeAttribute("serieFacturaSelect");
		
		if(factura.getId()!=null) {
			facturacionBean.init();
		}
		
		return "facturacionConstancias.xhtml?faces-redirect=true";
		
	}
	
	public void timbrado() throws InventarioException {
		String message = null;
		Severity severity = null;
		
		FacturamaBL facturamaBO = new FacturamaBL(factura.getId(), this.usuario);
		
		try {
			
			
			log.info("Timbrando factura: {}...", factura);
			facturamaBO.timbrar();
			facturamaBO.sendMail();
			log.info("Timbrado completado correctamente.");
			severity = FacesMessage.SEVERITY_INFO;
			message = "El timbrado se generó correctamente";
		} catch (FacturamaException e) {
			severity = FacesMessage.SEVERITY_ERROR;
			message = e.getMessage();
			e.printStackTrace();
		}catch (Exception ex) {
			//log.error("Problema para obtener los servicios del cliente.", ex);
			ex.printStackTrace();
			message = "Problema con la información de servicios.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			if(severity == null)
				severity = FacesMessage.SEVERITY_FATAL;
			if(message == null)
				message = "Ocurrió un error con la actualización de la factura.";
			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Timbrado CFDI", message));
			PrimeFaces.current().ajax().update("form:messages");
		}
		
	}
}
