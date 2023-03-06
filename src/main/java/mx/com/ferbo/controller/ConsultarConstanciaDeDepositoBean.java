package mx.com.ferbo.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
import mx.com.ferbo.dao.PrecioServicioDAO;
import mx.com.ferbo.dao.ProductoClienteDAO;
import mx.com.ferbo.dao.UnidadDeProductoDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ConstanciaDeDeposito;
import mx.com.ferbo.model.DetallePartida;
import mx.com.ferbo.model.Partida;
import mx.com.ferbo.model.PrecioServicio;
import mx.com.ferbo.model.Producto;
import mx.com.ferbo.model.ProductoPorCliente;
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
	
	
	private String otro,pedimento,contenedor,lote,tarima;
	
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
	}

	@PostConstruct
	public void init() {
		
		listadoClientes = clienteDAO.buscarTodos();
		listadoPrecioServicio = precioServicioDAO.buscarTodos();
		
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
		
		//Servicios Por Cliente
		/*listaServicioUnidad.clear();
		PrecioServicio precioServicio = new PrecioServicio();
		precioServicio.setCliente(cliente);
		listaServicioUnidad = listadoPrecioServicio.stream()
								.filter(ps -> clienteSelect != null
								?(ps.getCliente().getCteCve().intValue()==clienteSelect.getCteCve().intValue())
								:false)
								.collect(Collectors.toList());*/
		
	
	}
	
	public void calculoPxT() {
		
			List<Partida> listadoPartidas = selectConstanciaDD.getPartidaList();
			for(Partida p: listadoPartidas) {
				BigDecimal unidadT = new BigDecimal(p.getCantidadTotal()).setScale(2);
				BigDecimal tarimas = unidadT.divide(p.getNoTarimas(),2,RoundingMode.HALF_UP);		
				this.piezasTarima = new BigDecimal(tarimas.intValue()).setScale(2);
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
	
	
	

}
