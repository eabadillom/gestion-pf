package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.AvisoDAO;
import mx.com.ferbo.dao.CategoriaDAO;
import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.dao.ClienteDomiciliosDAO;
import mx.com.ferbo.dao.CuotaMinimaDAO;
import mx.com.ferbo.dao.DomiciliosDAO;
import mx.com.ferbo.dao.PlantaDAO;
import mx.com.ferbo.dao.PrecioServicioDAO;
import mx.com.ferbo.dao.ServicioDAO;
import mx.com.ferbo.dao.UdCobroDAO;
import mx.com.ferbo.dao.UnidadDeManejoDAO;
import mx.com.ferbo.model.Aviso;
import mx.com.ferbo.model.Categoria;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ClienteDomicilios;
import mx.com.ferbo.model.CuotaMinima;
import mx.com.ferbo.model.Domicilios;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.PrecioServicio;
import mx.com.ferbo.model.Servicio;
import mx.com.ferbo.model.UdCobro;
import mx.com.ferbo.model.UnidadDeManejo;
import mx.com.ferbo.util.InventarioException;

@Named
@ViewScoped
public class AvisosComercialBean implements Serializable {

	private static final long serialVersionUID = -626048119540963939L;
	private static Logger log = Logger.getLogger(AvisosComercialBean.class);

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
	private Aviso aviso;
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
//	private List<Servicio> lstServicios;
	private List<Servicio> lstServiciosAviso;

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

	private List<Planta> lstPlanta;
	private PlantaDAO plantaDAO;

	/**
	 * Objetos para Categoria
	 */
	private Integer categoriaSelected;
	private CategoriaDAO categoriaDAO;
	private List<Categoria> lstCategoria;
	
	/**
	 * Objetos para Unidad de Cobro
	 */
	private UdCobro udCobroSelected;
	private UdCobroDAO udCobroDAO;
	
	/**
	 * Objetos para unidad de Manejo
	 */
	private UnidadDeManejo unidadDeManejoSelected;
	private UnidadDeManejoDAO unidadDeManejoDAO;

	/**
	 * Objetos auxiliares
	 */
	private String renderAvisosTable;
	private String avisoTipoFacturacion;
	private Integer plantaCveSelected;

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
		cuotaMinimaSelected = new CuotaMinima();
		hasCuotaMinima = false;
		cuotaMinimaDAO = new CuotaMinimaDAO();
		lstCuotaMinima = new ArrayList<>();
		this.setRenderAvisosTable("false");
		categoriaDAO = new CategoriaDAO();
		plantaDAO = new PlantaDAO();
		precioServicioDAO = new PrecioServicioDAO();
		lstPrecioServicio = new ArrayList<>();
		
		udCobroDAO = new UdCobroDAO();
		unidadDeManejoDAO = new UnidadDeManejoDAO();
		lstPrecioServicioSelected = new ArrayList<>();
		


	}

	@PostConstruct
	public void init() {
		lstClientes = clienteDAO.buscarTodos();
		lstCategoria = categoriaDAO.buscarTodos();
		categoriaSelected = 1;
		lstPlanta = plantaDAO.findall();
	}

	public void filtraAvisos() {
		lstAvisos = avisoDAO.buscarPorCliente(clienteSelected.getCteCve());
		this.setRenderAvisosTable("true");
		PrimeFaces.current().ajax().update("form:dt-avisos");
	}

	public void buscaPrecioServicioAviso(Aviso a) {
		Aviso aviso = avisoDAO.buscarPorId(a.getAvisoCve(), true);
		System.out.println(a.hashCode());
		System.out.println(aviso.hashCode());
		this.aviso = aviso;
		this.avisoSelected = aviso;
		System.out.println(this.avisoSelected.hashCode());
		//this.avisoSelected = a;
		this.plantaCveSelected = avisoSelected.getPlantaCve().getPlantaCve();
		
		Aviso aviso_1 = new Aviso();
		aviso_1.setAvisoCve(1);
		PrecioServicio precioServicioAux = new PrecioServicio();
		
		precioServicioAux = new PrecioServicio();
		precioServicioAux.setCliente(clienteSelected);
		precioServicioAux.setAvisoCve(aviso_1);
		lstPrecioServicio = precioServicioDAO.buscarDisponibles(clienteSelected.getCteCve(), avisoSelected.getAvisoCve());
		
		precioServicioAux = new PrecioServicio();
		precioServicioAux.setCliente(clienteSelected);
		precioServicioAux.setAvisoCve(avisoSelected);
		lstPrecioServicioAviso = precioServicioDAO.buscarPorCriterios(precioServicioAux);
		PrimeFaces.current().ajax().update("soPlantaAct");

	}
	
	public List<Servicio> getLstServiciosAviso() {
		return lstServiciosAviso;
	}

	public void setLstServiciosAviso(List<Servicio> lstServiciosAviso) {
		this.lstServiciosAviso = lstServiciosAviso;
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
	
	public void nuevoAviso() {
		Categoria categoria = null;
		
		categoria = categoriaDAO.buscarPorId(this.categoriaSelected);
		this.avisoSelected = new Aviso();
		this.categoriaSelected = 1;
		this.avisoSelected.setCteCve(clienteSelected);
		this.avisoSelected.setCategoriaCve(categoria);
	}

	public void guardaAviso() {
		Date fechaActual = new Date();
		avisoSelected.setAvisoFecha(fechaActual);
		Categoria categoria = categoriaDAO.buscarPorId(categoriaSelected);
		avisoSelected.setCategoriaCve(categoria);
		Planta planta = plantaDAO.buscarPorId(this.plantaCveSelected);
		avisoSelected.setPlantaCve(planta);
		avisoDAO.guardar(avisoSelected);
		filtraAvisos();
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Servicio Agregado"));
		PrimeFaces.current().ajax().update("form:messages","dt-avisos");
	}

	public void actualizaAviso() {
		/*PrecioServicio nPs = new PrecioServicio();
		nPs.setCliente(clienteSelected);
		nPs.setAvisoCve(avisoSelected);
		nPs.setServicio(servicioSelected);*/
		/*for(PrecioServicio ps : lstPrecioServicioAviso) {//los precios servicios de aviso A12 
			ps.setAvisoCve(avisoSelected);
			precioServicioDAO.actualizar(ps);
		}*/		
		avisoSelected.setPrecioServicioList(lstPrecioServicioAviso);
		Date fechaActual = new Date();
		avisoSelected.setAvisoFecha(fechaActual);
		/*Categoria categoria = categoriaDAO.buscarPorId(categoriaSelected);
		avisoSelected.setCategoriaCve(categoria);*/
		Planta planta = plantaDAO.buscarPorId(this.plantaCveSelected);
		avisoSelected.setPlantaCve(planta);
		avisoSelected.setAvisoValSeg(avisoSelected.getAvisoValSeg());
		avisoSelected.setAvisoTpFacturacion(avisoSelected.getAvisoTpFacturacion());
		avisoSelected.setAvisoVigencia(avisoSelected.getAvisoVigencia());
		avisoSelected.setAvisoPlazo(avisoSelected.getAvisoPlazo());
		//nPs.getAvisoCve();
		//precioServicioDAO.actualizar(nPs);
		avisoDAO.actualizar(avisoSelected);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Servicio Actualizado"));
		PrimeFaces.current().executeScript("PF('addAvisoDialog').hide()");
		PrimeFaces.current().ajax().update("form:messages","form:dt-avisos");
		resetSelectedItems();
	}
	
	public void resetSelectedItems() {
		this.plantaCveSelected = null;
		this.categoriaSelected = null;
	}

	public void eliminaAviso() {
		PrecioServicio nPs = new PrecioServicio();
		nPs.setCliente(clienteSelected);
		nPs.setAvisoCve(avisoSelected);
		nPs.setServicio(servicioSelected);
		precioServicioDAO.eliminar(nPs);
		avisoDAO.eliminar(avisoSelected);
		lstAvisos.remove(avisoSelected);
		PrimeFaces.current().ajax().update("form:dt-avisos");
	}
	
	public void eliminaServicio(PrecioServicio ps) {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String eliminar = null;
		
		try {
			eliminar = precioServicioDAO.eliminar(ps);
			if(eliminar != null) {
				throw new InventarioException("Problema para eliminar el servicio seleccionado.");
			}
			this.lstPrecioServicioAviso = precioServicioDAO.buscarPorAviso(avisoSelected, clienteSelected);
			
			severity = FacesMessage.SEVERITY_INFO;
			mensaje = "Servicio eliminado correctamente.";
		} catch (InventarioException ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
		} catch (Exception ex) {
			mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			message = new FacesMessage(severity, "Catálogo de clientes", mensaje);
	        FacesContext.getCurrentInstance().addMessage(null, message);
	        PrimeFaces.current().ajax().update(":form:messages", "dt-servicioAviso");
		}
		
	}
	
	public void agregaServicio() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;		
		try {
			
			for(PrecioServicio ps :lstPrecioServicioSelected) {
				PrecioServicio precioServicio = new PrecioServicio();
				precioServicio.setAvisoCve(this.avisoSelected);
				precioServicio.setCliente(clienteSelected);
				precioServicio.setPrecio(ps.getPrecio());
				precioServicio.setServicio(ps.getServicio());
				precioServicio.setUnidad(ps.getUnidad());
				
				//TEST
				ps.equals(precioServicio); //no importa
				//verificar si el nuevo precio servicio ya existe en la tabla precio servicio
				List<PrecioServicio> lstTmpPrecioS =  lstPrecioServicioAviso.stream()
						.filter(
								p -> precioServicio.equals(p)
						)
						.collect(Collectors.toList());
				
				if(lstTmpPrecioS.size() > 0) //La lista lstPrecioServicioAviso ya tiene el precioServicio? no contiene a precio servicio
					continue; //entra pero sigue con la iteracion siguiente 
				
				if(precioServicioDAO.guardar(precioServicio) != null)
					throw new InventarioException("Problema para actualizar los servicios.");
				
				System.out.println(this.avisoSelected.hashCode());
				//this.avisoSelected.add(precioServicio); //para que ?
			}
			//avisoDAO.actualizar(avisoSelected);
			/*if(precioServicioDAO.guardar(precioServicioSelected) != null)
				throw new InventarioException("Problema para actualizar los servicios.");*/
			
			this.buscaPrecioServicioAviso(avisoSelected);
			
			severity = FacesMessage.SEVERITY_INFO;
			mensaje = "Servicio agregado correctamente.";
		} catch (InventarioException ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
		} catch (Exception ex) {
			log.error("Problema para guardar servicio(s) ...", ex);
			mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			message = new FacesMessage(severity, "Catálogo de clientes", mensaje);
	        FacesContext.getCurrentInstance().addMessage(null, message);
	        //PrimeFaces.current().ajax().update(":form:messages", "dt-servicioAviso");
	        PrimeFaces.current().ajax().update("form:dt-avisos", "form:panel-actAviso", "form:dt-servicioAviso", "form:dt-servicioSinAviso", "messages");
		}
		
		
	}
	
	public void remueveAviso() {		
		precioServicioDAO.eliminarListado(lstPrecioServicioSelected);
		PrimeFaces.current().ajax().update("form:dt-avisos", "form:panel-actAviso");

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

	public Integer getCategoriaSelected() {
		return categoriaSelected;
	}

	public void setCategoriaSelected(Integer categoriaSelected) {
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

	public UdCobroDAO getUdCobroDAO() {
		return udCobroDAO;
	}

	public void setUdCobroDAO(UdCobroDAO udCobroDAO) {
		this.udCobroDAO = udCobroDAO;
	}

	public UnidadDeManejo getUnidadDeManejoSelected() {
		return unidadDeManejoSelected;
	}

	public void setUnidadDeManejoSelected(UnidadDeManejo unidadDeManejoSelected) {
		this.unidadDeManejoSelected = unidadDeManejoSelected;
	}

	public UnidadDeManejoDAO getUnidadDeManejoDAO() {
		return unidadDeManejoDAO;
	}

	public void setUnidadDeManejoDAO(UnidadDeManejoDAO unidadDeManejoDAO) {
		this.unidadDeManejoDAO = unidadDeManejoDAO;
	}

	public String getAvisoTipoFacturacion() {
		return avisoTipoFacturacion;
	}

	public void setAvisoTipoFacturacion(String avisoTipoFacturacion) {
		this.avisoTipoFacturacion = avisoTipoFacturacion;
	}

	public Integer getPlantaCveSelected() {
		return plantaCveSelected;
	}

	public void setPlantaCveSelected(Integer plantaCveSelected) {
		this.plantaCveSelected = plantaCveSelected;
	}

	public Aviso getAviso() {
		return aviso;
	}

	public void setAviso(Aviso aviso) {
		this.aviso = aviso;
	}

}