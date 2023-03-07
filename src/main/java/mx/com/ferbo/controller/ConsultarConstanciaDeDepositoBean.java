package mx.com.ferbo.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.dao.ConstanciaDeDepositoDAO;
import mx.com.ferbo.dao.ConstanciaDepositoDetalleDAO;
import mx.com.ferbo.dao.PrecioServicioDAO;
import mx.com.ferbo.dao.ProductoClienteDAO;
import mx.com.ferbo.dao.UnidadDeProductoDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ConstanciaDeDeposito;
import mx.com.ferbo.model.ConstanciaDepositoDetalle;
import mx.com.ferbo.model.DetallePartida;
import mx.com.ferbo.model.Partida;
import mx.com.ferbo.model.PrecioServicio;
import mx.com.ferbo.model.Producto;
import mx.com.ferbo.model.ProductoPorCliente;
import mx.com.ferbo.model.Servicio;
import mx.com.ferbo.model.UnidadDeProducto;
import mx.com.ferbo.util.EntityManagerUtil;

@Named
@ViewScoped
public class ConsultarConstanciaDeDepositoBean implements Serializable{
	
	private static final long serialVersionUID = -3109002730694247052L;
	
	private Date fechaInicial;
	private Date fechaFinal;
	private Date fechaCaducidad;
	
	private String folio;
	private BigDecimal piezasTarima;
	
	private List<BigDecimal> piezas;
	
	private ClienteDAO clienteDAO;
	private List<Cliente> listadoClientes;
	private Cliente cliente;
	
	private List<ProductoPorCliente> listadoProductoPorCliente;
	private ProductoClienteDAO pdtoPorCliDAO;
	private Producto productoSelect;
	
	private Partida partidaSelect;
	
	private ConstanciaDeDepositoDAO constanciaDeDepositoDAO;
	private List<ConstanciaDeDeposito> listadoConstanciaDeDepositos;
	private ConstanciaDeDeposito selectConstanciaDD;
	
	private UnidadDeProductoDAO unidadDeProductoDAO;
	
	private List<PrecioServicio> listadoPrecioServicio;
	private PrecioServicio precioServicio;
	private PrecioServicioDAO precioServicioDAO;
	
	private List<ConstanciaDepositoDetalle> listadoConstanciaDepositoDetalle;
	private ConstanciaDepositoDetalleDAO constanciaDepositoDetalleDAO;
	private ConstanciaDepositoDetalle constanciaSelect;
	
	private Servicio servicioSelected;
	
	private BigDecimal servicioCantidad,cantidadServicio;
	
	private String otro,pedimento,contenedor,lote,tarima,temperatura,observaciones;
	
	public ConsultarConstanciaDeDepositoBean() {
		
		constanciaDeDepositoDAO = new ConstanciaDeDepositoDAO();
		listadoConstanciaDeDepositos = new ArrayList<ConstanciaDeDeposito>();
		
		clienteDAO = new ClienteDAO();
		listadoClientes = new ArrayList<Cliente>();
		
		listadoProductoPorCliente = new ArrayList<>();
		pdtoPorCliDAO = new ProductoClienteDAO();
		
		unidadDeProductoDAO = new UnidadDeProductoDAO();
		
		listadoPrecioServicio = new ArrayList<PrecioServicio>();
		precioServicioDAO = new PrecioServicioDAO();
		
		listadoConstanciaDepositoDetalle = new ArrayList<ConstanciaDepositoDetalle>();
		constanciaDepositoDetalleDAO = new ConstanciaDepositoDetalleDAO();
		
		piezas = new ArrayList<BigDecimal>();
	}

	@PostConstruct
	public void init() {
		
		listadoClientes = clienteDAO.buscarTodos();
		
		fechaInicial = new Date();
		fechaFinal = new Date();
		fechaCaducidad = new Date();
		folio = "";
		
		
	}

	public Date getFechaInicial() {
		return fechaInicial;
	}

	public void setFechaInicial(Date fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	public Date getFechaFinal() {
		return fechaFinal;
	}

	public void setFechaFinal(Date fechaFinal) {
		this.fechaFinal = fechaFinal;
	}
	
	public ConstanciaDeDepositoDAO getConstanciaDeDepositoDAO() {
		return constanciaDeDepositoDAO;
	}

	public void setConstanciaDeDepositoDAO(ConstanciaDeDepositoDAO constanciaDeDepositoDAO) {
		this.constanciaDeDepositoDAO = constanciaDeDepositoDAO;
	}
	
	public String getFolio() {
		return folio;
	}

	public void setFolio(String folio) {
		this.folio = folio;
	}

	public List<ConstanciaDeDeposito> getListadoConstanciaDeDepositos() {
		return listadoConstanciaDeDepositos;
	}

	public void setListadoConstanciaDeDepositos(List<ConstanciaDeDeposito> listadoConstanciaDeDepositos) {
		this.listadoConstanciaDeDepositos = listadoConstanciaDeDepositos;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public List<Cliente> getListadoClientes() {
		return listadoClientes;
	}

	public void setListadoClientes(List<Cliente> listadoClientes) {
		this.listadoClientes = listadoClientes;
	}

	public ConstanciaDeDeposito getSelectConstanciaDD() {
		if(selectConstanciaDD!=null) {
			calculoPxT();
			
			//TODO OPTIMIZAR CARGA DE INFORMACION
			listadoPrecioServicio = precioServicioDAO.buscarPorAviso(selectConstanciaDD.getAvisoCve(), selectConstanciaDD.getCteCve());
			listadoConstanciaDepositoDetalle = constanciaDepositoDetalleDAO.buscarPorFolio(selectConstanciaDD);
			
			//System.out.println(listadoPrecioServicio);
			
		}
		
		return selectConstanciaDD;
	}

	public void setSelectConstanciaDD(ConstanciaDeDeposito selectConstanciaDD) {
		this.selectConstanciaDD = selectConstanciaDD;
	}

	public BigDecimal getPiezasTarima() {
		return piezasTarima;
	}

	public void setPiezasTarima(BigDecimal piezasTarima) {
		this.piezasTarima = piezasTarima;
	}

	public List<ProductoPorCliente> getListadoProductoPorCliente() {
		return listadoProductoPorCliente;
	}

	public void setListadoProductoPorCliente(List<ProductoPorCliente> listadoProductoPorCliente) {
		this.listadoProductoPorCliente = listadoProductoPorCliente;
	}

	public Producto getProductoSelect() {
		return productoSelect;
	}

	public void setProductoSelect(Producto productoSelect) {
		this.productoSelect = productoSelect;
	}

	public Partida getPartidaSelect() {
		return partidaSelect;
	}

	public void setPartidaSelect(Partida partidaSelect) {
		this.partidaSelect = partidaSelect;
	}
	
	public Date getFechaCaducidad() {
		return fechaCaducidad;
	}

	public void setFechaCaducidad(Date fechaCaducidad) {
		this.fechaCaducidad = fechaCaducidad;
	}

	public String getOtro() {
		return otro;
	}

	public void setOtro(String otro) {
		this.otro = otro;
	}

	public String getPedimento() {
		return pedimento;
	}

	public void setPedimento(String pedimento) {
		this.pedimento = pedimento;
	}

	public String getContenedor() {
		return contenedor;
	}

	public void setContenedor(String contenedor) {
		this.contenedor = contenedor;
	}

	public String getLote() {
		return lote;
	}

	public void setLote(String lote) {
		this.lote = lote;
	}

	public String getTarima() {
		return tarima;
	}

	public void setTarima(String tarima) {
		this.tarima = tarima;
	}

	public List<PrecioServicio> getListadoPrecioServicio() {
		return listadoPrecioServicio;
	}

	public void setListadoPrecioServicio(List<PrecioServicio> listadoPrecioServicio) {
		this.listadoPrecioServicio = listadoPrecioServicio;
	}
	
	public List<ConstanciaDepositoDetalle> getListadoConstanciaDepositoDetalle() {
		return listadoConstanciaDepositoDetalle;
	}

	public void setListadoConstanciaDepositoDetalle(List<ConstanciaDepositoDetalle> listadoConstanciaDepositoDetalle) {
		this.listadoConstanciaDepositoDetalle = listadoConstanciaDepositoDetalle;
	}

	public Servicio getServicioSelected() {
		return servicioSelected;
	}

	public void setServicioSelected(Servicio servicioSelected) {
		this.servicioSelected = servicioSelected;
	}
	
	public BigDecimal getServicioCantidad() {
		return servicioCantidad;
	}

	public void setServicioCantidad(BigDecimal servicioCantidad) {
		this.servicioCantidad = servicioCantidad;
	}

	public ConstanciaDepositoDetalle getConstanciaSelect() {
		return constanciaSelect;
	}

	public void setConstanciaSelect(ConstanciaDepositoDetalle constanciaSelect) {
		this.constanciaSelect = constanciaSelect;
	}

	public BigDecimal getCantidadServicio() {
		return cantidadServicio;
	}

	public void setCantidadServicio(BigDecimal cantidadServicio) {
		this.cantidadServicio = cantidadServicio;
	}

	public String getTemperatura() {
		return temperatura;
	}

	public void setTemperatura(String temperatura) {
		this.temperatura = temperatura;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public List<BigDecimal> getPiezas() {
		return piezas;
	}

	public void setPiezas(List<BigDecimal> piezas) {
		this.piezas = piezas;
	}

	public void buscarConstanciaDD() {
		
		EntityManager em = EntityManagerUtil.getEntityManager();
		EntityTransaction transaction = em.getTransaction();
		transaction.begin();
		constanciaDeDepositoDAO.setEm(em);
		
		System.out.println( getFechaInicial() + "" + getFechaFinal());
		System.out.println( fechaFinal + "" + fechaInicial);//si trae las fechas dadas por el usuario
		
		listadoConstanciaDeDepositos = constanciaDeDepositoDAO.buscarPorCriterios(folio,fechaInicial, fechaFinal, cliente.getCteCve());
		
		for (ConstanciaDeDeposito constanciaDeDeposito : listadoConstanciaDeDepositos) {
			List<Partida> alPartidas = constanciaDeDeposito.getPartidaList();
			alPartidas.size();//permite recuperar la lista de partidas de la Constancia de Deposito
			List<ConstanciaDepositoDetalle> alConstanciaDD = constanciaDeDeposito.getConstanciaDepositoDetalleList();
			alConstanciaDD.size();
			for(Partida p: alPartidas) {
				List<DetallePartida> listadoDetallePartida = p.getDetallePartidaList();
				listadoDetallePartida.size();
			}
		}
		
		transaction.commit();
		em.close();
		
		listadoProductoPorCliente.clear();
		ProductoPorCliente productoPorCliente = new ProductoPorCliente();
		productoPorCliente.setCteCve(cliente);
		listadoProductoPorCliente = pdtoPorCliDAO.buscarPorCriterios(productoPorCliente);
		
	
	}
	
	public void calculoPxT() {
		
			List<Partida> listadoPartidas = selectConstanciaDD.getPartidaList();
			for(Partida p: listadoPartidas) {
				BigDecimal unidadT = new BigDecimal(p.getCantidadTotal()).setScale(2);
				BigDecimal tarimas = unidadT.divide(p.getNoTarimas(),2,RoundingMode.HALF_UP);		
				this.piezasTarima = new BigDecimal(tarimas.intValue()).setScale(2);
				piezas.add(piezasTarima);//arraylist donde se guardan los calculos al tener dos partidas
			}
		
	}
	
	public void updateConstanciaDD() {
		
		
		
		UnidadDeProducto unidadDeProducto = new UnidadDeProducto();
		unidadDeProducto = partidaSelect.getUnidadDeProductoCve();
		unidadDeProducto.setProductoCve(productoSelect);
		
		if(unidadDeProductoDAO.actualizar(unidadDeProducto) == null && constanciaDeDepositoDAO.actualizar(selectConstanciaDD)==null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Actualizacion","Partida Actualizada"));
		}
		
		
		PrimeFaces.current().ajax().update("form:messages","form:dt-partida");
	}
	
	public void updateDetallePartida() {
		//tomar el ultimo detalle de la partida selecionada y modificarle en po,etc solo a ese ultimo registro
		//partidaSelect nos trae el objeto partida seleccionado 
		
		List<DetallePartida> listadoDetallePartida = partidaSelect.getDetallePartidaList();
		int size = listadoDetallePartida.size();
		
		DetallePartida detallePartida = listadoDetallePartida.get(size - 1);//obtengo el ultimo detalle de la Partidas
		
		detallePartida.setDtpPO(otro);//otro
		detallePartida.setDtpPedimento(pedimento);
		detallePartida.setDtpSAP(contenedor);//contenedor
		detallePartida.setDtpLote(lote);
		detallePartida.setDtpCaducidad(fechaCaducidad);
		//detallePartida.setDtpPO(null);//otro
		//detallePartida.setDtpTarimas(tarima);//duda ¿?
		
		if(constanciaDeDepositoDAO.actualizar(this.selectConstanciaDD) == null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Actualizacion","Detalle de Partida Actualizada"));
		}
		
		PrimeFaces.current().ajax().update("form:messages");
		
	}
	
	public void updateServicio() {
		
		//List<ConstanciaDepositoDetalle> listaConstanciaDepositoDetalles = selectConstanciaDD.getConstanciaDepositoDetalleList();
		
		for(ConstanciaDepositoDetalle c: selectConstanciaDD.getConstanciaDepositoDetalleList()) {
			
			if(c.getConstanciaDepositoDetalleCve()==constanciaSelect.getConstanciaDepositoDetalleCve()) {
				c.setServicioCantidad(servicioCantidad);
			}
			
		}
		
		if(constanciaDeDepositoDAO.actualizar(this.selectConstanciaDD) == null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Actualizacion","Servicio Actualizado"));
		}
		
		PrimeFaces.current().ajax().update("form:messages");
	}
	
	public void saveServicio() {
		
		List<ConstanciaDepositoDetalle> lisConstanciaDepositoDetalles = selectConstanciaDD.getConstanciaDepositoDetalleList();
		
		ConstanciaDepositoDetalle conDepositoDetalle = new ConstanciaDepositoDetalle();
		conDepositoDetalle.setFolio(selectConstanciaDD);
		conDepositoDetalle.setServicioCantidad(cantidadServicio);
		conDepositoDetalle.setServicioCve(servicioSelected);
		
		
		if(constanciaDepositoDetalleDAO.guardar(conDepositoDetalle)== null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Agregado","Servicio Agregado"));
		}
		
		PrimeFaces.current().ajax().update("form:messages","form:dt-ConstanciaDepositoDetalle");
		
	}
	
	public void deleteServicio() {
		
		if(constanciaDepositoDetalleDAO.eliminar(this.constanciaSelect)== null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Eliminado","Servicio Eliminado"));
		}
		
		PrimeFaces.current().ajax().update("form:messages","form:dt-ConstanciaDepositoDetalle");
		
	}
	
	public void updateDatosGenerales() {
		
		selectConstanciaDD.setTemperatura(temperatura);
		selectConstanciaDD.setObservaciones(observaciones);
		
		if(constanciaDeDepositoDAO.actualizar(this.selectConstanciaDD) == null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Actualizacion","Constancia De Deposito Actualizada"));
			temperatura = "";
			observaciones = "";
		}
		
		PrimeFaces.current().ajax().update("form:messages");
	}
	

}
