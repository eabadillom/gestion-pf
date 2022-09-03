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

import mx.com.ferbo.dao.AvisoDAO;
import mx.com.ferbo.dao.BancoDAO;
import mx.com.ferbo.dao.CategoriaDAO;
import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.dao.ClienteDomiciliosDAO;
import mx.com.ferbo.dao.CuotaMinimaDAO;
import mx.com.ferbo.dao.DomiciliosDAO;
import mx.com.ferbo.dao.FacturaDAO;
import mx.com.ferbo.dao.PagoDAO;
import mx.com.ferbo.dao.PlantaDAO;
import mx.com.ferbo.dao.PrecioServicioDAO;
import mx.com.ferbo.dao.ProductoClienteDAO;
import mx.com.ferbo.dao.ProductoDAO;
import mx.com.ferbo.dao.ServicioDAO;
import mx.com.ferbo.dao.StatusFacturaDAO;
import mx.com.ferbo.dao.TipoPagoDAO;
import mx.com.ferbo.dao.UdCobroDAO;
import mx.com.ferbo.model.Aviso;
import mx.com.ferbo.model.Bancos;
import mx.com.ferbo.model.Categoria;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ClienteDomicilios;
import mx.com.ferbo.model.CuotaMinima;
import mx.com.ferbo.model.Domicilios;
import mx.com.ferbo.model.Factura;
import mx.com.ferbo.model.Pago;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.PrecioServicio;
import mx.com.ferbo.model.Producto;
import mx.com.ferbo.model.ProductoPorCliente;
import mx.com.ferbo.model.Servicio;
import mx.com.ferbo.model.StatusFactura;
import mx.com.ferbo.model.TipoPago;
import mx.com.ferbo.model.UdCobro;

@Named
@ViewScoped
public class AvisosComercialBean implements Serializable {

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
	 * Objetos para avisos
	 */
	private List<Aviso> lstAvisos;
	private Aviso avisoSelected;
	private AvisoDAO avisoDAO;

	/**
	 * Objetos para Domicilios
	 */
	private Domicilios domicilios;
	private DomiciliosDAO domiciliosDAO;

	/**
	 * Objetos para Cliente Domicilios
	 */
	private ClienteDomicilios clienteDomicilioSelected;
	private ClienteDomiciliosDAO clienteDomiciliosDAO;

	/**
	 * Objetos para cuota mínima
	 */
	private CuotaMinima cuotaMinimaSelected;
	private CuotaMinimaDAO cuotaMinimaDAO;
	private boolean hasCuotaMinima;
	private List<CuotaMinima> lstCuotaMinima;

	/**
	 * Objetos para Servicios
	 */
	private Servicio servicioSelected;
	private ServicioDAO servicioDAO;
	private List<Servicio> lstServicios;

	/**
	 * Objetos para Servicios por cliente
	 */
	private PrecioServicio precioServicioSelected;
	private PrecioServicioDAO precioServicioDAO;
	private List<PrecioServicio> lstPrecioServicio;
	private List<PrecioServicio> lstPrecioServicioSelected;
	private List<PrecioServicio> lstPrecioServicioAviso;

	/**
	 * Objetos para Plantas
	 */

	private Planta plantaSelected;
	private List<Planta> lstPlanta;
	private PlantaDAO plantaDAO;

	/**
	 * Objetos para Categoria
	 */
	private Categoria categoriaSelected;
	private CategoriaDAO categoriaDAO;
	private List<Categoria> lstCategoria;
	
	/**
	 * Objetos para Unidad de Cobro
	 */
	private UdCobro udCobroSelected;
	private List<UdCobro> lstUdCobro;
	private UdCobroDAO udCobroDAO;

	/**
	 * Objetos auxiliares
	 */
	private String renderAvisosTable;
	private boolean avisoPo;
	private boolean avisoPed;
	private boolean avisoSAP;
	private boolean avisoLote;
	private boolean avisoCad;
	private boolean avisoTarima;
	private boolean avisoOtro;
	private String avisoTemp;
	private String avisoObservaciones;
	private int avisoVigencia;
	private BigDecimal avisoValSeg;
	private int avisoPlazo;

	/**
	 * Constructores
	 */
	public AvisosComercialBean() {
		clienteDAO = new ClienteDAO();
		domiciliosDAO = new DomiciliosDAO();
		servicioDAO = new ServicioDAO();
		clienteDomiciliosDAO = new ClienteDomiciliosDAO();
		avisoDAO = new AvisoDAO();
		lstClientes = new ArrayList<>();
		lstAvisos = new ArrayList<>();
		avisoSelected = new Aviso();
		cuotaMinimaSelected = new CuotaMinima();
		hasCuotaMinima = false;
		cuotaMinimaDAO = new CuotaMinimaDAO();
		lstCuotaMinima = new ArrayList<>();
		lstServicios = new ArrayList<>();
		this.setRenderAvisosTable("false");
		categoriaDAO = new CategoriaDAO();
		plantaDAO = new PlantaDAO();
		precioServicioDAO = new PrecioServicioDAO();
		lstPrecioServicio = new ArrayList<>();
		lstUdCobro = new ArrayList<>();
		udCobroDAO = new UdCobroDAO();
		avisoPo=false;
		avisoPed=false;
		avisoSAP=false;
		avisoLote=false;
		avisoCad=false;
		avisoTarima=false;
		avisoOtro=false;
		avisoTemp="";
		avisoObservaciones="";
		avisoVigencia=0;
		avisoValSeg=new BigDecimal(0);
		avisoPlazo = 0;

	}

	@PostConstruct
	public void init() {
		lstClientes = clienteDAO.buscarTodos();
		// lstServicios = servicioDAO.buscarTodos();
		lstCategoria = categoriaDAO.buscarTodos();
		lstPlanta = plantaDAO.findall();
		lstUdCobro = udCobroDAO.buscarTodos();
	}

	/**
	 * Métodos de filtrado
	 */
	public void filtraAvisos() {
		avisoSelected.setCteCve(clienteSelected);
		lstAvisos = avisoDAO.buscarPorCliente(avisoSelected);
		buscaDomicilioCliente();
		this.setRenderAvisosTable("true");
		buscaCuotaMinima();
		buscaServicios();
		PrimeFaces.current().ajax().update("form:dt-avisos");
		hasCuotaMinima = false;
		cuotaMinimaSelected = new CuotaMinima();

	}

	public void buscaServicios() {
		List<Servicio> lstServAux = new ArrayList<>();
		precioServicioSelected = new PrecioServicio();
		precioServicioSelected.setCliente(clienteSelected);
		lstPrecioServicio = precioServicioDAO.buscarPorCriterios(precioServicioSelected);
		for (PrecioServicio p : lstPrecioServicio) {
			lstServAux.add(p.getServicio());
		}
		lstServAux = lstServAux.stream().distinct().collect(Collectors.toList());
		lstServicios = lstServAux;

	}

	/**
	 * Procesamiento de datos
	 */
	public void estableceCuotaMinima() {
		cuotaMinimaSelected.setCliente(clienteSelected);
		if (isHasCuotaMinima()) {
			if (this.buscaCuotaMinima()) {
				cuotaMinimaDAO.actualizar(cuotaMinimaSelected);
			}
			cuotaMinimaDAO.guardar(cuotaMinimaSelected);
		} else {
			cuotaMinimaDAO.eliminar(cuotaMinimaSelected);
			cuotaMinimaSelected = new CuotaMinima();
			hasCuotaMinima = false;
		}
	}

	public void cambiaCuotaMinima() {
		if (!hasCuotaMinima) {
			this.estableceCuotaMinima();
		}
		PrimeFaces.current().ajax().update("form:dt-avisos", "form:monto-minimo");
	}

	private void buscaDomicilioCliente() {
		List<ClienteDomicilios> listClienteDomicilios = clienteDomiciliosDAO.buscaPorCliente(clienteSelected);
		if (!listClienteDomicilios.isEmpty()) {
			clienteDomicilioSelected = listClienteDomicilios.get(0);
		} else {
			clienteDomicilioSelected = new ClienteDomicilios();
		}
	}

	public boolean buscaCuotaMinima() {
		cuotaMinimaSelected.setCliente(clienteSelected);
		lstCuotaMinima = cuotaMinimaDAO.buscarPorCriterios(cuotaMinimaSelected);
		if (!lstCuotaMinima.isEmpty()) {
			cuotaMinimaSelected = lstCuotaMinima.get(0);
			this.setHasCuotaMinima(true);
			return true;
		}
		return false;
	}

	public void guardaAviso() {
		List<PrecioServicio> psLst = new ArrayList<>();
		PrecioServicio nPs = new PrecioServicio();
		nPs.setCliente(clienteSelected);
		nPs.setAvisoCve(avisoSelected);
		nPs.setServicio(servicioSelected);
		psLst.add(nPs);
		avisoSelected.setPrecioServicioList(psLst);
		Date fechaActual = new Date();
		avisoSelected.setAvisoFecha(fechaActual);
		avisoSelected.setAvisoTpFacturacion("");
		avisoSelected.setCategoriaCve(categoriaSelected);
		avisoSelected.setAvisoPo(avisoPo);
		avisoSelected.setAvisoPedimento(avisoPed);
		avisoSelected.setAvisoSap(avisoSAP);
		avisoSelected.setAvisoLote(avisoLote);
		avisoSelected.setAvisoCaducidad(avisoCad);
		avisoSelected.setAvisoTarima(avisoTarima);
		avisoSelected.setAvisoOtro(avisoOtro);
		avisoSelected.setAvisoTemp(avisoTemp);
		avisoSelected.setAvisoObservaciones(avisoObservaciones);
		avisoSelected.setAvisoVigencia(avisoVigencia);
		avisoSelected.setAvisoValSeg(avisoValSeg);
		avisoSelected.setAvisoPlazo(avisoPlazo);
		avisoDAO.guardar(avisoSelected);
		for(PrecioServicio ps : psLst) {
			ps.setAvisoCve(avisoSelected);
			precioServicioDAO.actualizar(ps);
		}
		precioServicioDAO.guardar(nPs); 
		filtraAvisos();
		PrimeFaces.current().ajax().update("form:dt-avisos");
	}

	public void actualizaAviso() {
		List<PrecioServicio> psLst = new ArrayList<>();
		PrecioServicio nPs = new PrecioServicio();
		nPs.setCliente(clienteSelected);
		nPs.setAvisoCve(avisoSelected);
		nPs.setServicio(servicioSelected);
		psLst = precioServicioDAO.buscarPorCriterios(nPs);		
		avisoSelected.setPrecioServicioList(psLst);
		Date fechaActual = new Date();
		avisoSelected.setAvisoFecha(fechaActual);
		avisoSelected.setAvisoTpFacturacion("");
		avisoSelected.setCategoriaCve(categoriaSelected);
		avisoSelected.setAvisoPo(avisoPo);
		avisoSelected.setAvisoPedimento(avisoPed);
		avisoSelected.setAvisoSap(avisoSAP);
		avisoSelected.setAvisoLote(avisoLote);
		avisoSelected.setAvisoCaducidad(avisoCad);
		avisoSelected.setAvisoTarima(avisoTarima);
		avisoSelected.setAvisoOtro(avisoOtro);
		avisoSelected.setAvisoTemp(avisoTemp);
		avisoSelected.setAvisoObservaciones(avisoObservaciones);
		avisoSelected.setAvisoVigencia(avisoVigencia);
		avisoSelected.setAvisoValSeg(avisoValSeg);
		avisoSelected.setAvisoPlazo(avisoPlazo);
		for(PrecioServicio ps : psLst) {
			ps.setAvisoCve(avisoSelected);
			precioServicioDAO.actualizar(ps);
		}
		avisoDAO.actualizar(avisoSelected);		
		PrimeFaces.current().ajax().update("form:dt-avisos");
	}

	public void eliminaAviso() {
		PrecioServicio nPs = new PrecioServicio();
		nPs.setCliente(clienteSelected);
		nPs.setAvisoCve(avisoSelected);
		nPs.setServicio(servicioSelected);
		//precioServicioDAO.eliminar(nPs);
		avisoDAO.eliminar(avisoSelected);
		lstAvisos.remove(avisoSelected);
		PrimeFaces.current().ajax().update("form:dt-avisos");
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

	public List<Aviso> getLstAvisos() {
		return lstAvisos;
	}

	public void setLstAvisos(List<Aviso> lstAvisos) {
		this.lstAvisos = lstAvisos;
	}

	public Aviso getAvisoSelected() {
		return avisoSelected;
	}

	public void setAvisoSelected(Aviso avisoSelected) {
		this.avisoSelected = avisoSelected;
	}

	public AvisoDAO getAvisoDAO() {
		return avisoDAO;
	}

	public void setAvisoDAO(AvisoDAO avisoDAO) {
		this.avisoDAO = avisoDAO;
	}

	public Domicilios getDomicilios() {
		return domicilios;
	}

	public void setDomicilios(Domicilios domicilios) {
		this.domicilios = domicilios;
	}

	public DomiciliosDAO getDomiciliosDAO() {
		return domiciliosDAO;
	}

	public void setDomiciliosDAO(DomiciliosDAO domiciliosDAO) {
		this.domiciliosDAO = domiciliosDAO;
	}

	public ClienteDomicilios getClienteDomicilioSelected() {
		return clienteDomicilioSelected;
	}

	public void setClienteDomicilioSelected(ClienteDomicilios clienteDomicilioSelected) {
		this.clienteDomicilioSelected = clienteDomicilioSelected;
	}

	public ClienteDomiciliosDAO getClienteDomiciliosDAO() {
		return clienteDomiciliosDAO;
	}

	public void setClienteDomiciliosDAO(ClienteDomiciliosDAO clienteDomiciliosDAO) {
		this.clienteDomiciliosDAO = clienteDomiciliosDAO;
	}

	public CuotaMinima getCuotaMinimaSelected() {
		return cuotaMinimaSelected;
	}

	public void setCuotaMinimaSelected(CuotaMinima cuotaMinimaSelected) {
		this.cuotaMinimaSelected = cuotaMinimaSelected;
	}

	public CuotaMinimaDAO getCuotaMinimaDAO() {
		return cuotaMinimaDAO;
	}

	public void setCuotaMinimaDAO(CuotaMinimaDAO cuotaMinimaDAO) {
		this.cuotaMinimaDAO = cuotaMinimaDAO;
	}

	public boolean isHasCuotaMinima() {
		return hasCuotaMinima;
	}

	public void setHasCuotaMinima(boolean hasCuotaMinima) {
		this.hasCuotaMinima = hasCuotaMinima;
	}

	public String getRenderAvisosTable() {
		return renderAvisosTable;
	}

	public void setRenderAvisosTable(String renderAvisosTable) {
		this.renderAvisosTable = renderAvisosTable;
	}

	public List<CuotaMinima> getLstCuotaMinima() {
		return lstCuotaMinima;
	}

	public void setLstCuotaMinima(List<CuotaMinima> lstCuotaMinima) {
		this.lstCuotaMinima = lstCuotaMinima;
	}

	public Servicio getServicioSelected() {
		return servicioSelected;
	}

	public void setServicioSelected(Servicio servicioSelected) {
		this.servicioSelected = servicioSelected;
	}

	public ServicioDAO getServicioDAO() {
		return servicioDAO;
	}

	public void setServicioDAO(ServicioDAO servicioDAO) {
		this.servicioDAO = servicioDAO;
	}

	public List<Servicio> getLstServicios() {
		return lstServicios;
	}

	public void setLstServicios(List<Servicio> lstServicios) {
		this.lstServicios = lstServicios;
	}

	public Planta getPlantaSelected() {
		return plantaSelected;
	}

	public void setPlantaSelected(Planta plantaSelected) {
		this.plantaSelected = plantaSelected;
	}

	public List<Planta> getLstPlanta() {
		return lstPlanta;
	}

	public void setLstPlanta(List<Planta> lstPlanta) {
		this.lstPlanta = lstPlanta;
	}

	public PlantaDAO getPlantaDAO() {
		return plantaDAO;
	}

	public void setPlantaDAO(PlantaDAO plantaDAO) {
		this.plantaDAO = plantaDAO;
	}

	public CategoriaDAO getCategoriaDAO() {
		return categoriaDAO;
	}

	public void setCategoriaDAO(CategoriaDAO categoriaDAO) {
		this.categoriaDAO = categoriaDAO;
	}

	public Categoria getCategoriaSelected() {
		return categoriaSelected;
	}

	public void setCategoriaSelected(Categoria categoriaSelected) {
		this.categoriaSelected = categoriaSelected;
	}

	public List<Categoria> getLstCategoria() {
		return lstCategoria;
	}

	public void setLstCategoria(List<Categoria> lstCategoria) {
		this.lstCategoria = lstCategoria;
	}

	public PrecioServicio getPrecioServicioSelected() {
		return precioServicioSelected;
	}

	public void setPrecioServicioSelected(PrecioServicio precioServicioSelected) {
		this.precioServicioSelected = precioServicioSelected;
	}

	public PrecioServicioDAO getPrecioServicioDAO() {
		return precioServicioDAO;
	}

	public void setPrecioServicioDAO(PrecioServicioDAO precioServicioDAO) {
		this.precioServicioDAO = precioServicioDAO;
	}

	public List<PrecioServicio> getLstPrecioServicio() {
		return lstPrecioServicio;
	}

	public void setLstPrecioServicio(List<PrecioServicio> lstPrecioServicio) {
		this.lstPrecioServicio = lstPrecioServicio;
	}

	public boolean isAvisoPo() {
		return avisoPo;
	}

	public void setAvisoPo(boolean avisoPo) {
		this.avisoPo = avisoPo;
	}

	public boolean isAvisoPed() {
		return avisoPed;
	}

	public void setAvisoPed(boolean avisoPed) {
		this.avisoPed = avisoPed;
	}

	public boolean isAvisoSAP() {
		return avisoSAP;
	}

	public void setAvisoSAP(boolean avisoSAP) {
		this.avisoSAP = avisoSAP;
	}

	public boolean isAvisoLote() {
		return avisoLote;
	}

	public void setAvisoLote(boolean avisoLote) {
		this.avisoLote = avisoLote;
	}

	public boolean isAvisoCad() {
		return avisoCad;
	}

	public void setAvisoCad(boolean avisoCad) {
		this.avisoCad = avisoCad;
	}

	public boolean isAvisoTarima() {
		return avisoTarima;
	}

	public void setAvisoTarima(boolean avisoTarima) {
		this.avisoTarima = avisoTarima;
	}

	public boolean isAvisoOtro() {
		return avisoOtro;
	}

	public void setAvisoOtro(boolean avisoOtro) {
		this.avisoOtro = avisoOtro;
	}

	public String getAvisoTemp() {
		return avisoTemp;
	}

	public void setAvisoTemp(String avisoTemp) {
		this.avisoTemp = avisoTemp;
	}

	public String getAvisoObservaciones() {
		return avisoObservaciones;
	}

	public void setAvisoObservaciones(String avisoObservaciones) {
		this.avisoObservaciones = avisoObservaciones;
	}

	public int getAvisoVigencia() {
		return avisoVigencia;
	}

	public void setAvisoVigencia(int avisoVigencia) {
		this.avisoVigencia = avisoVigencia;
	}

	public BigDecimal getAvisoValSeg() {
		return avisoValSeg;
	}

	public void setAvisoValSeg(BigDecimal avisoValSeg) {
		this.avisoValSeg = avisoValSeg;
	}

	public int getAvisoPlazo() {
		return avisoPlazo;
	}

	public void setAvisoPlazo(int avisoPlazo) {
		this.avisoPlazo = avisoPlazo;
	}

	public List<PrecioServicio> getLstPrecioServicioSelected() {
		return lstPrecioServicioSelected;
	}

	public void setLstPrecioServicioSelected(List<PrecioServicio> lstPrecioServicioSelected) {
		this.lstPrecioServicioSelected = lstPrecioServicioSelected;
	}

	public List<PrecioServicio> getLstPrecioServicioAviso() {
		return lstPrecioServicioAviso;
	}

	public void setLstPrecioServicioAviso(List<PrecioServicio> lstPrecioServicioAviso) {
		this.lstPrecioServicioAviso = lstPrecioServicioAviso;
	}

	public UdCobro getUdCobroSelected() {
		return udCobroSelected;
	}

	public void setUdCobroSelected(UdCobro udCobroSelected) {
		this.udCobroSelected = udCobroSelected;
	}

	public List<UdCobro> getLstUdCobro() {
		return lstUdCobro;
	}

	public void setLstUdCobro(List<UdCobro> lstUdCobro) {
		this.lstUdCobro = lstUdCobro;
	}

	public UdCobroDAO getUdCobroDAO() {
		return udCobroDAO;
	}

	public void setUdCobroDAO(UdCobroDAO udCobroDAO) {
		this.udCobroDAO = udCobroDAO;
	}

}