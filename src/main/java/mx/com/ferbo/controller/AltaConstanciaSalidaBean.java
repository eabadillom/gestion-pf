package mx.com.ferbo.controller;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.dao.ConstanciaDeDepositoDAO;
import mx.com.ferbo.dao.ConstanciaSalidaDAO;
import mx.com.ferbo.dao.ConstanciaServicioDAO;
import mx.com.ferbo.dao.DetallePartidaDAO;
import mx.com.ferbo.dao.InventarioDAO;
import mx.com.ferbo.dao.PlantaDAO;
import mx.com.ferbo.dao.PrecioServicioDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ConstanciaDeDeposito;
import mx.com.ferbo.model.ConstanciaDeServicio;
import mx.com.ferbo.model.ConstanciaSalida;
import mx.com.ferbo.model.ConstanciaSalidaServicios;
import mx.com.ferbo.model.ConstanciaSalidaServiciosPK;
import mx.com.ferbo.model.ConstanciaServicioDetalle;
import mx.com.ferbo.model.DetalleConstanciaSalida;
import mx.com.ferbo.model.DetallePartida;
import mx.com.ferbo.model.DetallePartidaPK;
import mx.com.ferbo.model.EstadoConstancia;
import mx.com.ferbo.model.Inventario;
import mx.com.ferbo.model.Partida;
import mx.com.ferbo.model.PartidaServicio;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.PrecioServicio;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.JasperReportUtil;
import mx.com.ferbo.util.conexion;

@Named
@ViewScoped

public class AltaConstanciaSalidaBean implements Serializable{
	
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
	
	private DetallePartidaDAO detallePartidaDAO;
	
	private List<Inventario> listaInventario;
	private InventarioDAO inventarioDAO;
	
	private ConstanciaDeServicio constanciaDeServicio;
	private ConstanciaServicioDAO constanciaServicioDAO;
	
	private String numFolio,nombreTransportista,placas,observaciones,temperatura;
	private BigDecimal cantidadServicio;
	private Date fechaSalida;
	private int cantidadTotal;
	private BigDecimal pesoTotal;
	
	private Usuario usuario;
	private FacesContext faceContext;
	private HttpServletRequest httpServletRequest;
	
	public AltaConstanciaSalidaBean() {
		constanciaDeServicio = new ConstanciaDeServicio();
		constanciaServicioDAO = new ConstanciaServicioDAO();
		
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
		
		inventarioDAO = new InventarioDAO();
		listaInventario = new ArrayList<Inventario>();
		
		detallePartidaDAO = new DetallePartidaDAO();
		
		listadoConstanciaSalidaServicios = new ArrayList<>();

		listadoPartida = new ArrayList<Partida>();
		listadoDetalleConstanciaSalida = new ArrayList<>();
		listadoTemp = new ArrayList<>();
		detallePartidaLista = new ArrayList<>();
		listaAuxPartida = new ArrayList<>();
	}
	
	@PostConstruct
	public void init() {
		
		faceContext = FacesContext.getCurrentInstance();
		httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
		usuario = (Usuario) httpServletRequest.getSession(false).getAttribute("usuario");
		
		listadoClientes = clienteDAO.buscarTodos();
		
		if((usuario.getPerfil() == 1)||(usuario.getPerfil() == 4)) {
			listadoPlantas.add(plantaDAO.buscarPorId(usuario.getIdPlanta()));
		}else {
			listadoPlantas = plantaDAO.findall();
		}
		
		
		listadoConstanciasSalidas = constanciaSalidaDAO.buscarTodos();
		listadoPrecioServicios = preciosServicioDAO.buscarTodos();
		
		plantaSelect = listadoPlantas.get(0);
		
		fechaSalida = new Date();
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
	
	public List<Inventario> getListaInventario() {
		return listaInventario;
	}

	public void setListaInventario(List<Inventario> listaInventario) {
		this.listaInventario = listaInventario;
	}

	public InventarioDAO getInventarioDAO() {
		return inventarioDAO;
	}

	public void setInventarioDAO(InventarioDAO inventarioDAO) {
		this.inventarioDAO = inventarioDAO;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
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
		listaInventario.clear();
		serviciosCliente = listadoPrecioServicios.stream()
								.filter(s -> clienteSelect != null
								?(s.getCliente().getCteCve().intValue()==clienteSelect.getCteCve().intValue())
								:false)
								.collect(Collectors.toList());
		
		listaInventario = inventarioDAO.buscarPorCliente(getClienteSelect(),getPlantaSelect());
		
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
			
			//OBTENCION DEL ULTIMO DETALLEPARTIDA DE LA PARTIDA
			
			for(DetallePartida dp: partidaSelect.getDetallePartidaList()) {
				detallePartida = dp;
			}
			
			//detallePartida = partidaSelect.getDetallePartidaList().get(0);
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
				listadoTemp.add(d);
				detalleT = d;
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
		cs.setConstanciaSalidaServiciosList(listadoConstanciaSalidaServicios);
		cs.setDetalleConstanciaSalidaList(listadoTemp);
		
 		for(ConstanciaSalidaServicios c: listadoConstanciaSalidaServicios) {
 			c.getConstanciaSalidaServiciosPK().setConstanciaSalidaCve(cs);
			
 			c.setIdConstancia(cs);
 			c.setServicioCve(c.getConstanciaSalidaServiciosPK().getServicioCve());
			
		}
 		
 		for(DetalleConstanciaSalida d: listadoTemp) {
 			for(Partida p: listaAuxPartida) {
 				
 				if(d.getPartidaCve().equals(p)) {
 					
		 			d.setConstanciaCve(cs);
		 			d.setCamaraCve(p.getCamaraCve().getCamaraCve());
		 			d.setUnidad(p.getUnidadDeProductoCve().getUnidadDeManejoCve().getUnidadDeManejoDs());
		 			d.setProducto(p.getUnidadDeProductoCve().getProductoCve().getProductoDs());
		 			d.setFolioEntrada(p.getFolio().getFolioCliente());
		 			d.setCamaraCadena(p.getCamaraCve().getCamaraDs());
		 			
		 			
		 			int size = p.getDetallePartidaList().size();
		 			System.out.println(size);
	 				d.setDetallePartida(p.getDetallePartidaList().get(size-1));
		 			
		 			for(DetallePartida dp: detallePartidaLista) {
		 				
		 				if(d.getDetallePartida().equals(dp)) {
		 					int detalleNuevo = dp.getDetallePartidaPK().getDetPartCve() + 1;
		 					int cantidadRestante;
		 					BigDecimal pesoRestante;
		 					
		 					cantidadRestante = dp.getCantidadUManejo() - d.getCantidad();
		 					pesoRestante = dp.getCantidadUMedida().subtract(d.getPeso());
		 					
		 					DetallePartidaPK detallePartidaPK = new DetallePartidaPK();
		 					detallePartidaPK.setDetPartCve(detalleNuevo);
		 					detallePartidaPK.setPartidaCve(p);
		 					
		 					DetallePartida detalle = new DetallePartida();
		 					detalle.setDetallePartidaPK(detallePartidaPK);
		 					detalle.setDetallePartida(dp);
		 					detalle.setDtpPedimento(dp.getDtpPedimento());
		 					detalle.setDtpSAP(dp.getDtpSAP());
		 					detalle.setDtpLote(dp.getDtpLote());
		 					detalle.setDtpMP(dp.getDtpMP());
		 					detalle.setDtpPO(dp.getDtpPO());      
		 					detalle.setCantidadUManejo(cantidadRestante);
		 					detalle.setUMedidaCve(dp.getUMedidaCve());
		 					detalle.setCantidadUMedida(pesoRestante);
		 					detalle.setDtpCaducidad(dp.getDtpCaducidad());
		 					detalle.setPartida(p);		 
		 					
		 					d.setDetallePartida(detalle);
		 					
		 					if(detallePartidaDAO.guardar(d.getDetallePartida())==null) {
			 					System.out.println("registro correcto detalle partida");
			 				}
		 					
		 				}
		 			}
		 			break;
 				}
 				
 			}
 		}
		
 		constanciaSalidaDAO.guardar(cs); //REGISTRO LA CONTSANCIA SALIDA
 		
 		if(!(cs.getConstanciaSalidaServiciosList().isEmpty())) {
 			
 			EstadoConstancia estadoConstancia = new EstadoConstancia();
 			estadoConstancia.setEdoCve(1);
 			estadoConstancia.setDescripcion("NUEVA");
 			
 			constanciaDeServicio.setCteCve(clienteSelect);
 			constanciaDeServicio.setFecha(fechaSalida);
 			constanciaDeServicio.setNombreTransportista(nombreTransportista);
 			constanciaDeServicio.setPlacasTransporte(placas);
 			constanciaDeServicio.setObservaciones(observaciones);
 			constanciaDeServicio.setFolioCliente("S"+cs.getNumero());
 			constanciaDeServicio.setValorDeclarado(new BigDecimal(1));
 			constanciaDeServicio.setStatus(estadoConstancia);
 			
 			List<ConstanciaServicioDetalle> constanciaServicioDetalles = new ArrayList<ConstanciaServicioDetalle>(); 
 			List<PartidaServicio> partidaServicios = new ArrayList<PartidaServicio>(); 
 			
 			for(ConstanciaSalidaServicios css:listadoConstanciaSalidaServicios) {
 				ConstanciaServicioDetalle constanciaServicioDetalle = new ConstanciaServicioDetalle();
 				constanciaServicioDetalle.setServicioCve(css.getServicioCve());
 				constanciaServicioDetalle.setFolio(constanciaDeServicio);
 				constanciaServicioDetalle.setServicioCantidad(css.getNumCantidad());
 				constanciaServicioDetalles.add(constanciaServicioDetalle);
 			}
 			constanciaDeServicio.setConstanciaServicioDetalleList(new ArrayList<>());
 			constanciaDeServicio.setConstanciaServicioDetalleList(constanciaServicioDetalles);
 			
 			for(DetalleConstanciaSalida dcs: listadoTemp) {
 				
 				PartidaServicio partidaS = new PartidaServicio();
 				partidaS.setFolio(constanciaDeServicio);
 				partidaS.setCantidadDeCobro(dcs.getPeso());
 				partidaS.setCantidadTotal(dcs.getCantidad());
 				partidaS.setProductoCve(dcs.getPartidaCve().getUnidadDeProductoCve().getProductoCve());
 				partidaS.setUnidadDeManejoCve(dcs.getPartidaCve().getUnidadDeProductoCve().getUnidadDeManejoCve());
 				partidaS.setUnidadDeCobro(dcs.getPartidaCve().getUnidadDeProductoCve().getUnidadDeManejoCve());
 				partidaServicios.add(partidaS);
 			}
 			
 			constanciaDeServicio.setPartidaServicioList(new ArrayList<>());
 			constanciaDeServicio.setPartidaServicioList(partidaServicios);
 			
 			if(constanciaServicioDAO.guardar(constanciaDeServicio)==null) {
 				System.out.println("se guardo correctamente la constancia de servicio");
 			}
 			
 		}
 		
 		
 		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"CONSTANCIA DE SALIDA", "Se registro de forma correcta"));
 		PrimeFaces.current().ajax().update("form:messages");
		
	}
	
	public void nuevoRegistro() {
		
		clienteSelect = new Cliente();
		plantaSelect = listadoPlantas.get(0);
		partidaSelect = new Partida();
		fechaSalida = new Date();
		numFolio = "";
		detallePartida = new DetallePartida();
		listadoDetalleConstanciaSalida = new ArrayList<DetalleConstanciaSalida>();
		detallePartidaLista = new ArrayList<DetallePartida>();
		
		listaInventario = new ArrayList<>();
		listadoTemp = new ArrayList<>();
		listadoPartida = new ArrayList<>();
		listadoConstanciaSalidaServicios = new ArrayList<>();
		listaAuxPartida = new ArrayList<Partida>();
		
		servicioClienteSelect = new PrecioServicio();
		cantidadServicio = new BigDecimal(0);
		nombreTransportista = "";
		placas = "";
		observaciones = "";
		
		
		PrimeFaces.current().ajax().update("form:dt-inventario","form:dt-servicio");
		
	}
	
	public void imprimirTicket() throws Exception{
		
		String jasperPath = "/jasper/ConstanciaSalida.jrxml";
		String filename = "ticket.pdf";
		String images = "/images/logoF.png";
		String message = null;
		Severity severity = null;
		ConstanciaSalida constancia = null;
		File reportFile = new File(jasperPath);
		File imgFile = null;
		JasperReportUtil jasperReportUtil = new JasperReportUtil();
		Map<String, Object> parameters = new HashMap<String, Object>();
		Connection connection = null;
		parameters = new HashMap<String, Object>();
		try {
			
			URL resource = getClass().getResource(jasperPath);//verifica si el recurso esta disponible 
			URL resourceimg = getClass().getResource(images); 
			String file = resource.getFile();//retorna la ubicacion del archivo
			String img = resourceimg.getFile();
			reportFile = new File(file);//crea un archivo
			imgFile = new File(img);
			constancia = new ConstanciaSalida();
			constancia.setNumero(this.numFolio);
			numFolio = String.valueOf(getNumFolio());
			connection = EntityManagerUtil.getConnection();
			parameters.put("REPORT_CONNECTION", connection);
			parameters.put("NUMERO", numFolio);
			parameters.put("LogoPath", imgFile.getPath());
			jasperReportUtil.createPdf(filename, parameters, reportFile.getPath());
			   
		} catch (Exception e) {
			e.printStackTrace();
			message = String.format("No se pudo imprimir el folio %s", this.numFolio);
			severity = FacesMessage.SEVERITY_INFO;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity,"Error en impresion",message));
			PrimeFaces.current().ajax().update("form:messages");
			
		}finally {
			conexion.close((Connection) connection);
		}
		
		
	}
	
}
