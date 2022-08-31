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
import mx.com.ferbo.dao.ProductoClienteDAO;
import mx.com.ferbo.dao.ProductoDAO;
import mx.com.ferbo.dao.ServicioDAO;
import mx.com.ferbo.dao.StatusFacturaDAO;
import mx.com.ferbo.dao.TipoPagoDAO;
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
	 * Objetos auxiliares
	 */
	private String renderAvisosTable;

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
		
	}

	@PostConstruct
	public void init() {
		lstClientes = clienteDAO.buscarTodos();
		lstServicios = servicioDAO.buscarTodos();
		lstCategoria = categoriaDAO.buscarTodos();
		lstPlanta = plantaDAO.findall();
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
		PrimeFaces.current().ajax().update("form:dt-avisos");
		hasCuotaMinima = false;
		cuotaMinimaSelected = new CuotaMinima();

	}

	/**
	 * Procesamiento de datos
	 */
	public void estableceCuotaMinima() {
		cuotaMinimaSelected.setCliente(clienteSelected);
		if (isHasCuotaMinima()) {
			if(this.buscaCuotaMinima()) {
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
		if(!hasCuotaMinima) {
			this.estableceCuotaMinima();
		}
		PrimeFaces.current().ajax().update("form:dt-avisos", "form:monto-minimo");
	}

	private void buscaDomicilioCliente() {
		List<ClienteDomicilios> listClienteDomicilios = clienteDomiciliosDAO.buscaPorCliente(clienteSelected);
		if (!listClienteDomicilios.isEmpty()) {
			clienteDomicilioSelected = listClienteDomicilios.get(0);
		}else {
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
		avisoDAO.guardar(avisoSelected);
		
	}
	
	public void actualizaAviso() {
		
	}
	
	public void eliminaAviso() {
		
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

}