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
import mx.com.ferbo.model.Partida;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.PrecioServicio;

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
	
	private List<DetalleConstanciaSalida> listadoDetalleConstanciaSalida;
	
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
	}
	
	@PostConstruct
	public void init() {
		
		listadoClientes = clienteDAO.buscarTodos();
		listadoPlantas = plantaDAO.findall();
		listadoConstanciasSalidas = constanciaSalidaDAO.buscarTodos();
		listadoConstanciaDD = constanciaDDDAO.buscarTodos();
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
		
		
		
		/*ConstanciaDeDeposito constanciaDeDeposito = new ConstanciaDeDeposito();
		constanciaDeDeposito.setFolioCliente(numFolio);
		ConstanciaDepositoDetalle constanciaDD = new ConstanciaDepositoDetalle();
		constanciaDD.setServicioCve(servicioClienteSelect.getServicio());
		constanciaDD.setFolio(constanciaDeDeposito);
		constanciaDD.setServicioCantidad(cantidadServicio);*/
		
	}
	
	public void saveDetalleConstanciaSalida(){
		
		if(partidaSelect==null) {
			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error de Producto","Seleccione un Producto/Partida"));
			
		}
		else{
			DetalleConstanciaSalida detalleConstanciaSalida = new DetalleConstanciaSalida();
			
			detalleConstanciaSalida.setPartidaCve(partidaSelect);
			
			listadoDetalleConstanciaSalida.add(detalleConstanciaSalida);
			listadoPartida.add(partidaSelect);
			this.partidaSelect = null;
		}
		
		PrimeFaces.current().ajax().update("form:messages");
		
	}
	
	/*public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        if (newValue != null && !newValue.equals(oldValue)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }*/
	
	public void calculoPesoSalida() {
		BigDecimal totalP,cantidad,peso,calculo,mul;
		
		for(Partida p: listadoPartida) {
			for(DetalleConstanciaSalida d: listadoDetalleConstanciaSalida) {
				cantidad = new BigDecimal(d.getCantidad());
				peso = p.getPesoTotal();
				//DetalleConstanciaSalida detalleConstanciaSalida = new DetalleConstanciaSalida();
				mul = cantidad.multiply(peso);
				totalP = new BigDecimal(p.getCantidadTotal());
				calculo = mul.divide(totalP,0,RoundingMode.HALF_UP);//MOSTRAR DECIMALES?
				//detalleConstanciaSalida.setPeso(calculo.setScale(2));
				d.setPeso(calculo);
				System.out.println("");
			}
		}
		
		/*DetalleConstanciaSalida detalleConstanciaS = new DetalleConstanciaSalida();
		detalleConstanciaS.setPeso(totalP.divide(cantidad.multiply(peso),2,RoundingMode.HALF_UP));*/
		
		
		/*for(DetalleConstanciaSalida dcs: listadoDetalleConstanciaSalida) {
			cantidad = new BigDecimal(dcs.getCantidad()).setScale(2);
			peso = dcs.getPeso().setScale(2);
			dcs.setPeso(totalP.divide(cantidad.multiply(peso)));
		}*/
		
		
	}
	
	
	//boton continuar es para guardar la constancia salida????
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
	
	

}
