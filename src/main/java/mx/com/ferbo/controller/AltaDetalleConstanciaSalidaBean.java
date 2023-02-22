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

import org.primefaces.PrimeFaces;
import org.primefaces.event.CellEditEvent;

import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.dao.ConstanciaDeDepositoDAO;
import mx.com.ferbo.dao.ConstanciaSalidaDAO;
import mx.com.ferbo.dao.PlantaDAO;
import mx.com.ferbo.dao.PrecioServicioDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ConstanciaDeDeposito;
import mx.com.ferbo.model.ConstanciaSalida;
import mx.com.ferbo.model.ConstanciaSalidaServicios;
import mx.com.ferbo.model.ConstanciaSalidaServiciosPK;
import mx.com.ferbo.model.DetalleConstanciaSalida;
import mx.com.ferbo.model.DetallePartida;
import mx.com.ferbo.model.DetallePartidaPK;
import mx.com.ferbo.model.Partida;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.PrecioServicio;
import mx.com.ferbo.model.Servicio;
import mx.com.ferbo.model.StatusConstanciaSalida;

@Named
@ViewScoped

public class AltaDetalleConstanciaSalidaBean implements Serializable{
	
	private static final long serialVersionUID = -1785488265380235016L;
	
	private List<Cliente> listadoClientes;
	private ClienteDAO clienteDAO;
	private Cliente clienteSelect;
	
	private List<Planta> listadoPlantas;
	private PlantaDAO plantaDAO;
	private Planta plantaSelect;
	
	private List<ConstanciaSalida> listadoConstanciasSalidas;
	private ConstanciaSalidaDAO constanciaSalidaDAO;
	
	private List<ConstanciaDeDeposito> listadoConstanciaDD;
	private ConstanciaDeDepositoDAO constanciaDDDAO;
	
	private List<PrecioServicio> listadoPrecioServicios;
	private PrecioServicioDAO preciosServicioDAO;
	private List<PrecioServicio> serviciosCliente;
	private PrecioServicio servicioClienteSelect;
	
	private List<ConstanciaSalidaServicios> listadoConstanciaSalidaServicios;//listadoConstanciaDepositoDetalle
	
	private Partida partidaSelect;
	private List<Partida> listadoPartida;
	private List<Partida> listaAuxPartida;
	
	private List<DetalleConstanciaSalida> listadoDetalleConstanciaSalida;
	private List<DetalleConstanciaSalida> listadoTemp;
	
	private DetallePartida detallePartida;
	private List<DetallePartida> detallePartidaLista;
	
	private String numFolio,nombreTransportista,placas,observaciones,temperatura;
	private BigDecimal cantidadServicio;
	private Date fechaSalida;
	private int cantidadTotal;
	private BigDecimal pesoTotal;
	
	public AltaDetalleConstanciaSalidaBean() {
		clienteDAO = new ClienteDAO();
		listadoClientes = new ArrayList<>();
		
		plantaDAO = new PlantaDAO();
		listadoPlantas = new ArrayList<>();
		
		constanciaSalidaDAO = new ConstanciaSalidaDAO();
		listadoConstanciasSalidas = new ArrayList<>();
		
		listadoConstanciaDD = new ArrayList<>();
		constanciaDDDAO = new ConstanciaDeDepositoDAO();
		
		listadoPrecioServicios = new ArrayList<>();
		serviciosCliente = new ArrayList<>();
		preciosServicioDAO = new PrecioServicioDAO();
		
		listadoConstanciaSalidaServicios = new ArrayList<>();
		
		listadoPartida = new ArrayList<Partida>();
		listadoDetalleConstanciaSalida = new ArrayList<>();
		listadoTemp = new ArrayList<>();
		detallePartidaLista = new ArrayList<>();
		listaAuxPartida = new ArrayList<>();
	}
	
	@PostConstruct
	public void init() {
		
		listadoClientes = clienteDAO.buscarTodos();
		listadoPlantas = plantaDAO.findall();
		listadoConstanciasSalidas = constanciaSalidaDAO.buscarTodos();
		listadoPrecioServicios = preciosServicioDAO.buscarTodos();
	}

	public List<Cliente> getListadoClientes() {
		return listadoClientes;
	}

	public void setListadoClientes(List<Cliente> listadoClientes) {
		this.listadoClientes = listadoClientes;
	}

	public ClienteDAO getClienteDAO() {
		return clienteDAO;
	}

	public void setClienteDAO(ClienteDAO clienteDAO) {
		this.clienteDAO = clienteDAO;
	}

	public Cliente getClienteSelect() {
		return clienteSelect;
	}

	public void setClienteSelect(Cliente clienteSelect) {
		this.clienteSelect = clienteSelect;
	}

	public List<Planta> getListadoPlantas() {
		return listadoPlantas;
	}

	public void setListadoPlantas(List<Planta> listadoPlantas) {
		this.listadoPlantas = listadoPlantas;
	}

	public PlantaDAO getPlantaDAO() {
		return plantaDAO;
	}

	public void setPlantaDAO(PlantaDAO plantaDAO) {
		this.plantaDAO = plantaDAO;
	}

	public Planta getPlantaSelect() {
		return plantaSelect;
	}

	public void setPlantaSelect(Planta plantaSelect) {
		this.plantaSelect = plantaSelect;
	}

	public Date getFechaSalida() {
		return fechaSalida;
	}

	public void setFechaSalida(Date fechaSalida) {
		this.fechaSalida = fechaSalida;
	}

	public String getNumFolio() {
		return numFolio;
	}

	public void setNumFolio(String numFolio) {
		this.numFolio = numFolio;
	}
	
	public List<ConstanciaSalida> getListadoConstanciasSalidas() {
		return listadoConstanciasSalidas;
	}

	public void setListadoConstanciasSalidas(List<ConstanciaSalida> listadoConstanciasSalidas) {
		this.listadoConstanciasSalidas = listadoConstanciasSalidas;
	}

	public ConstanciaSalidaDAO getConstanciaSalidaDAO() {
		return constanciaSalidaDAO;
	}

	public void setConstanciaSalidaDAO(ConstanciaSalidaDAO constanciaSalidaDAO) {
		this.constanciaSalidaDAO = constanciaSalidaDAO;
	}
	
	public List<ConstanciaDeDeposito> getListadoConstanciaDD() {
		return listadoConstanciaDD;
	}

	public void setListadoConstanciaDD(List<ConstanciaDeDeposito> listadoConstanciaDD) {
		this.listadoConstanciaDD = listadoConstanciaDD;
	}

	public ConstanciaDeDepositoDAO getConstanciaDDDAO() {
		return constanciaDDDAO;
	}

	public void setConstanciaDDDAO(ConstanciaDeDepositoDAO constanciaDDDAO) {
		this.constanciaDDDAO = constanciaDDDAO;
	}

	public List<PrecioServicio> getListadoPrecioServicios() {
		return listadoPrecioServicios;
	}

	public void setListadoPrecioServicios(List<PrecioServicio> listadoPrecioServicios) {
		this.listadoPrecioServicios = listadoPrecioServicios;
	}

	public PrecioServicioDAO getPreciosServicioDAO() {
		return preciosServicioDAO;
	}

	public void setPreciosServicioDAO(PrecioServicioDAO preciosServicioDAO) {
		this.preciosServicioDAO = preciosServicioDAO;
	}

	public List<PrecioServicio> getServiciosCliente() {
		return serviciosCliente;
	}

	public void setServiciosCliente(List<PrecioServicio> serviciosCliente) {
		this.serviciosCliente = serviciosCliente;
	}

	public PrecioServicio getServicioClienteSelect() {
		return servicioClienteSelect;
	}

	public void setServicioClienteSelect(PrecioServicio servicioClienteSelect) {
		this.servicioClienteSelect = servicioClienteSelect;
	}

	public List<ConstanciaSalidaServicios> getListadoConstanciaSalidaServicios() {
		return listadoConstanciaSalidaServicios;
	}

	public void setListadoConstanciaSalidaServicios(List<ConstanciaSalidaServicios> listadoConstanciaSalidaServicios) {
		this.listadoConstanciaSalidaServicios = listadoConstanciaSalidaServicios;
	}

	public BigDecimal getCantidadServicio() {
		return cantidadServicio;
	}

	public void setCantidadServicio(BigDecimal cantidadServicio) {
		this.cantidadServicio = cantidadServicio;
	}

	public Partida getPartidaSelect() {
		return partidaSelect;
	}

	public void setPartidaSelect(Partida partidaSelect) {
		this.partidaSelect = partidaSelect;
	}

	public List<Partida> getListadoPartida() {
		return listadoPartida;
	}

	public void setListadoPartida(List<Partida> listadoPartida) {
		this.listadoPartida = listadoPartida;
	}

	public String getNombreTransportista() {
		return nombreTransportista;
	}

	public void setNombreTransportista(String nombreTransportista) {
		this.nombreTransportista = nombreTransportista;
	}

	public String getPlacas() {
		return placas;
	}

	public void setPlacas(String placas) {
		this.placas = placas;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getTemperatura() {
		return temperatura;
	}

	public void setTemperatura(String temperatura) {
		this.temperatura = temperatura;
	}

	public List<DetalleConstanciaSalida> getListadoDetalleConstanciaSalida() {
		return listadoDetalleConstanciaSalida;
	}

	public void setListadoDetalleConstanciaSalida(List<DetalleConstanciaSalida> listadoDetalleConstanciaSalida) {
		this.listadoDetalleConstanciaSalida = listadoDetalleConstanciaSalida;
	}
	
	public int getCantidadTotal() {
		return cantidadTotal;
	}

	public void setCantidadTotal(int cantidadTotal) {
		this.cantidadTotal = cantidadTotal;
	}

	public BigDecimal getPesoTotal() {
		return pesoTotal;
	}

	public void setPesoTotal(BigDecimal pesoTotal) {
		this.pesoTotal = pesoTotal;
	}

	public List<DetalleConstanciaSalida> getListadoTemp() {
		return listadoTemp;
	}

	public void setListadoTemp(List<DetalleConstanciaSalida> listadoTemp) {
		this.listadoTemp = listadoTemp;
	}
	
	public DetallePartida getDetallePartida() {
		return detallePartida;
	}

	public void setDetallePartida(DetallePartida detallePartida) {
		this.detallePartida = detallePartida;
	}

	public List<DetallePartida> getDetallePartidaLista() {
		return detallePartidaLista;
	}

	public void setDetallePartidaLista(List<DetallePartida> detallePartidaLista) {
		this.detallePartidaLista = detallePartidaLista;
	}

	public void validar() {
		
		//System.out.println(listadoConstanciasSalidas);
		int contador = 0;
		for(ConstanciaSalida cs: listadoConstanciasSalidas) {
			if(cs.getNumero().equals(numFolio)){
				FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,"ERROR FOLIO","El folio no esta disponible"));
				this.numFolio = null;
				PrimeFaces.current().ajax().update("form:folio");
				break;
			}
			contador = contador + 1;
			if(contador == listadoConstanciasSalidas.size()) {
				FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,"FOLIO DISPONIBLE","El folio esta disponible"));
			}
		}
		PrimeFaces.current().ajax().update("form:messages");
		
	}
	
	public void servicioCliente() {
		
		serviciosCliente.clear();
		serviciosCliente = listadoPrecioServicios.stream()
								.filter(s -> clienteSelect != null
								?(s.getCliente().getCteCve().intValue()==clienteSelect.getCteCve().intValue())
								:false)
								.collect(Collectors.toList());
		
		listadoConstanciaDD = constanciaDDDAO.buscarPorCliente(clienteSelect.getCteCve());
		
	}
	
	public void saveServicio() {
		
		//CONSTANCIA_SALIDA_SRV
		ConstanciaSalidaServicios constanciaSalidaServicios = new ConstanciaSalidaServicios();
		ConstanciaSalidaServiciosPK constanciaSalidaServiciosPK = new ConstanciaSalidaServiciosPK();
		
		//constanciaSalidaServiciosPK.setConstanciaSalidaCve();
		constanciaSalidaServiciosPK.setServicioCve(servicioClienteSelect.getServicio());
		
		constanciaSalidaServicios.setConstanciaSalidaServiciosPK(constanciaSalidaServiciosPK);
		constanciaSalidaServicios.setNumCantidad(cantidadServicio);
		
		listadoConstanciaSalidaServicios.add(constanciaSalidaServicios);
		System.out.println(listadoConstanciaSalidaServicios);
		
		
	}
	
	public void saveDetalleConstanciaS(){
		
		if(partidaSelect==null) {
			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error de Producto","Seleccione un Producto/Partida"));
			
		}
		else{
			listadoPartida.clear();
			DetalleConstanciaSalida detalleConstanciaSalida = new DetalleConstanciaSalida();
			
			detalleConstanciaSalida.setPartidaCve(partidaSelect);
			
			listadoDetalleConstanciaSalida.add(detalleConstanciaSalida);
			listadoPartida.add(partidaSelect);
			listaAuxPartida.add(partidaSelect);
			detallePartida = partidaSelect.getDetallePartidaList().get(0);
			detallePartidaLista.add(detallePartida);//detallepartida de cada partidaSelect
			this.partidaSelect = null;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"Modificar registro agregado","Modifica piezas a salir antes de agregar otro producto"));
		}
		
		PrimeFaces.current().ajax().update("form:messages","form:dt-detalleConstanciaSalida");
		
	}
	
	
	public void calculoPesoSalida() {
		BigDecimal totalP,cantidad,peso,calculo,mul;
		
		/*for(DetalleConstanciaSalida d: listadoDetalleConstanciaSalida) {
			totalP = new BigDecimal(d.getPartidaCve().getCantidadTotal());
			peso = d.getPartidaCve().getPesoTotal();
			cantidad = new BigDecimal(d.getCantidad());
			mul = cantidad.multiply(peso);
			calculo = mul.divide(totalP,0,RoundingMode.HALF_UP);
			d.setPeso(calculo);
			metodo para probra mañana como lo tenia en una sola dataTable 
		}*/
		
		DetalleConstanciaSalida detalleT = new DetalleConstanciaSalida();
		
		for(Partida p: listadoPartida) {
			
			totalP = new BigDecimal(p.getCantidadTotal());//piezas totales
			peso = p.getPesoTotal();//peso total de todas las piezas 
			//List<DetalleConstanciaSalida> detalleConstanciaSalidas = new ArrayList<DetalleConstanciaSalida>();
			
			for(DetalleConstanciaSalida d: listadoDetalleConstanciaSalida) {
				cantidad = new BigDecimal(d.getCantidad());//numero de piezas a salir
				
				mul = cantidad.multiply(peso);
				calculo = mul.divide(totalP,0,RoundingMode.HALF_UP);
				d.setPeso(calculo);
				//detalleConstanciaSalidas.add(d);
				listadoTemp.add(d);
				detalleT = d;
				//PrimeFaces.current().ajax().update("form:dt-detalleConstanciaSalida");
			}
			
		}
		
		listadoDetalleConstanciaSalida.remove(detalleT);
		
		System.out.println(listadoDetalleConstanciaSalida);
		PrimeFaces.current().ajax().update("form:dt-detalleConstanciaSalida","form:dt-salidas");
		
		
	}
	
	
	
	public void saveConstanciaSalida() {
		
		//GUARDO CONSTANCIA SALIDA
		ConstanciaSalida constanciaSalida = new ConstanciaSalida();
		
		constanciaSalida.setFecha(fechaSalida);
		constanciaSalida.setNumero(numFolio);
		constanciaSalida.setClienteCve(clienteSelect);
		constanciaSalida.setNombreCte(clienteSelect.getCteNombre());
		//constanciaSalida.setStatus();
		//constanciaSalida.setObservaciones();
		//pendiente agregar al model los campos de nombre y placas transporte
		
	}
	
	public void saveDetalleConstanciaSalida() {
		//METODO PARA GUARDAR INFORMACION EN LA BASE DE DATOS
		
		ConstanciaSalida cs = new ConstanciaSalida();
		
		//StatusConstanciaSalida status = new StatusConstanciaSalida();
		cs.setFecha(fechaSalida);
		cs.setNumero(numFolio);
		cs.setClienteCve(clienteSelect);
		cs.setNombreCte(clienteSelect.getCteNombre());
		cs.setStatus(null);
		cs.setObservaciones(observaciones);
		cs.setNombreTransportista(nombreTransportista);
		cs.setPlacasTransporte(placas);
		//cs.setConstanciaSalidaServiciosList(new ArrayList<>());
		//cs.setConstanciaSalidaServiciosList(listadoConstanciaSalidaServicios);
		cs.setConstanciaSalidaServiciosList(listadoConstanciaSalidaServicios);
		cs.setDetalleConstanciaSalidaList(listadoDetalleConstanciaSalida);
		
 		for(ConstanciaSalidaServicios c: listadoConstanciaSalidaServicios) {
 			c.getConstanciaSalidaServiciosPK().setConstanciaSalidaCve(cs);
			
 			c.setIdConstancia(cs);
 			c.setServicioCve(c.getConstanciaSalidaServiciosPK().getServicioCve());
			
		}
 		
 		for(DetalleConstanciaSalida d: listadoTemp) {
 			for(Partida p: listaAuxPartida) {
 				
 				if(d.getPartidaCve().equals(p)) {
		 			d.setConstanciaCve(cs);
		 			d.setPartidaCve(p);
		 			d.setCamaraCve(p.getCamaraCve().getCamaraCve());
		 			d.setUnidad(p.getUnidadDeProductoCve().getUnidadDeManejoCve().getUnidadDeManejoDs());
		 			d.setProducto(p.getUnidadDeProductoCve().getProductoCve().getProductoDs());
		 			d.setFolioEntrada(p.getFolio().toString());//es el folio de constancia de deposito????
		 			d.setCamaraCadena(p.getCamaraCve().getCamaraDs());
		 			d.setDetallePartida(p.getDetallePartidaList().get(0));
		 			//d.setTemperatura();
 				}
 			}
 			
 			/*int detalleNuevo = detallePartida.getDetallePartidaPK().getDetPartCve() + 1;
 			
 			DetallePartidaPK detallePartidaPK = new DetallePartidaPK();
 			detallePartidaPK.setDetPartCve(detalleNuevo);
 			detallePartidaPK.setPartidaCve(partidaSelect);
 			
 			DetallePartida detallePartida = new DetallePartida();
 			
 			detallePartida = this.detallePartida;
 			
 			detallePartida.setDetallePartidaPK(detallePartidaPK);
 			detallePartida.setDetallePartida(this.detallePartida);
 			
 			//guardando detalle constancia salida en la BD
 			
 			DetalleConstanciaSalida detalleConstanciaSalida = new DetalleConstanciaSalida();
 			detalleConstanciaSalida.setConstanciaCve(cs);
 			detalleConstanciaSalida.setPartidaCve(partidaSelect);
 			detalleConstanciaSalida.setCamaraCve(partidaSelect.getCamaraCve().getCamaraCve()); 
 			detalleConstanciaSalida.setCantidad(d.getCantidad());
 			detalleConstanciaSalida.setPeso(d.getPeso());
 			detalleConstanciaSalida.setUnidad(d.getPartidaCve().getUnidadDeCobro().getUnidadDeManejoDs());
 			detalleConstanciaSalida.setProducto(d.getPartidaCve().getUnidadDeProductoCve().getProductoCve().getProductoDs());
 			detalleConstanciaSalida.setFolioEntrada(String.valueOf(this.detallePartida.getDetallePartidaPK().getDetPartCve())); //coresponde el dato al numero de id de la tabla detallePartida???
 			detalleConstanciaSalida.setCamaraCadena(partidaSelect.getCamaraCve().getCamaraDs()); 
 			detalleConstanciaSalida.setTemperatura(d.getTemperatura());*/
 			
 			
 		}
 		
		
 		//constanciaSalidaDAO.guardar(cs);
		
		
		
		//guardando detalle constancia salida en la BD
		/*
		DetalleConstanciaSalida detalleConstanciaSalida = new DetalleConstanciaSalida();
		detalleConstanciaSalida.setConstanciaCve(cs);
		//detalleConstanciaSalida.setPartidaCve(partidaSelect);
		//detalleConstanciaSalida.setCamaraCve(); es un int, deberia de ser de tipo CAMARA?
		//detalleConstanciaSalida.setCantidad(null);
		//detalleConstanciaSalida.setPeso(null);
		//detalleConstanciaSalida.setUnidad();
		//detalleConstanciaSalida.setProducto(null);
		//detalleConstanciaSalida.setFolioEntrada(); trae el dato mi listadoConstanciaDD
		//detalleConstanciaSalida.setCamaraCadena(); 
		detalleConstanciaSalida.setTemperatura(temperatura);*/
		
		
		
		//cssPK.setServicioCve(servicioClienteSelect.getServicio());///checar este seteo para seguir con los demas
		//cssPK.setServicioCve(); como guardar la lista de servicios
		
		
		//cssPK.setServicioCve();
		
		//ConstanciaSalidaServicios css = new ConstanciaSalidaServicios();
		
		//DetalleConstanciaSalida dcs = new DetalleConstanciaSalida();
		
		
		
		
	}
	
	

}
