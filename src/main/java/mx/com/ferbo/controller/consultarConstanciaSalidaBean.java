package mx.com.ferbo.controller;


import java.io.File;
import java.io.Serializable;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.dao.ConstanciaSalidaDAO;
import mx.com.ferbo.dao.DetalleConstanciaSalidaDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ConstanciaSalida;
import mx.com.ferbo.model.ConstanciaSalidaServicios;
import mx.com.ferbo.model.DetalleConstanciaSalida;
import mx.com.ferbo.model.Partida;
import mx.com.ferbo.model.StatusConstanciaSalida;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.JasperReportUtil;
import mx.com.ferbo.util.conexion;

@Named
@ViewScoped

public class consultarConstanciaSalidaBean implements Serializable{
	
	private static final long serialVersionUID = -3109002730694247052L;
	
	private ConstanciaSalidaDAO constanciaSalidaDAO;
	private List<ConstanciaSalida> listadoConstanciaSalida;
	
	private DetalleConstanciaSalidaDAO detalleCSDAO;
	private List<DetalleConstanciaSalida> listadoDetalleConstanciaS;
	
	private Date fechaInicial;
	private Date fechaFinal;
	private String folio;
	
	private List<Cliente> listadoClientes;
	private ClienteDAO clienteDAO;
	private Cliente cliente;
	
	private ConstanciaSalida constanciaSelect;
	
	public consultarConstanciaSalidaBean() {
		constanciaSalidaDAO = new ConstanciaSalidaDAO();
		listadoConstanciaSalida = new ArrayList<>();
		
		listadoClientes = new ArrayList<Cliente>();
		clienteDAO = new ClienteDAO();
		
		detalleCSDAO = new DetalleConstanciaSalidaDAO();
		listadoDetalleConstanciaS = new ArrayList<>();
		
	}
	
	
	@PostConstruct
	public void init() {
		
		listadoClientes = clienteDAO.buscarTodos();
		
		fechaInicial = new Date();
		fechaFinal = new Date();
	}
	
	public void buscarConstanciaSalida() {
		EntityManager em = EntityManagerUtil.getEntityManager();
		EntityTransaction transaction = em.getTransaction();
		transaction.begin();
		constanciaSalidaDAO.setEm(em);
		
		System.out.println( getFechaInicial() + "" + getFechaFinal());
		System.out.println( fechaFinal + "" + fechaInicial);//si trae las fechas dadas por el usuario
		
		listadoConstanciaSalida = constanciaSalidaDAO.buscarPorCriterios(folio,fechaInicial, fechaFinal, cliente.getCteCve());
		
		for (ConstanciaSalida constanciaSalida : listadoConstanciaSalida) {
			List<ConstanciaSalidaServicios> alConstanciaSalidaS = constanciaSalida.getConstanciaSalidaServiciosList();
			alConstanciaSalidaS.size();
			List<DetalleConstanciaSalida> alConstanciaDD = constanciaSalida.getDetalleConstanciaSalidaList();
			alConstanciaDD.size();
		}
		
		transaction.commit();
		em.close();
	}

	public void imprimirTicket(){
		
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
			constancia.setNumero(constanciaSelect.getNumero());
			connection = EntityManagerUtil.getConnection();
			parameters.put("REPORT_CONNECTION", connection);
			parameters.put("NUMERO", constancia.getNumero());
			parameters.put("LogoPath", imgFile.getPath());
			jasperReportUtil.createPdf(filename, parameters, reportFile.getPath());
			   
		} catch (Exception e) {
			e.printStackTrace();
			message = String.format("No se pudo imprimir el folio %s", constancia.getNumero());
			severity = FacesMessage.SEVERITY_INFO;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity,"Error en impresion",message));
			PrimeFaces.current().ajax().update("form:messages");
			
		}finally {
			conexion.close((Connection) connection);
		}
		
		
	}

	public void cancelarConstancia() {
		
		Date fechaSalida;
		int coincidencias = 0;
		
		for(DetalleConstanciaSalida d: constanciaSelect.getDetalleConstanciaSalidaList()) {
			Partida buscarPartida = d.getPartidaCve();
			listadoDetalleConstanciaS = detalleCSDAO.buscarPorPartidaCve(buscarPartida);
			
			for(DetalleConstanciaSalida detalle: listadoDetalleConstanciaS) {
				
				if(buscarPartida.equals(detalle.getPartidaCve())) {
					fechaSalida = detalle.getConstanciaCve().getFecha();
					
					if(constanciaSelect.getFecha().before(fechaSalida)) {	
						coincidencias = coincidencias + 1;
					}
				}
			}
			if(coincidencias >= 1 ) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No Cancelada", "La Constancia De Salida: " +constanciaSelect.getNumero()+ " tiene fechas posteriores"));
				break;
			}
		}
		
		if(coincidencias==0) {
			constanciaSalidaDAO.actualizarStatus(constanciaSelect);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Cancelada", "La Constancia De Salida "+constanciaSelect.getNumero()+" fue cancelada"));
		}
		PrimeFaces.current().ajax().update("form:messages");
	}

	//metodo get y setter
	
	public List<ConstanciaSalida> getListadoConstanciaSalida() {
		return listadoConstanciaSalida;
	}

	public void setListadoConstanciaSalida(List<ConstanciaSalida> listadoConstanciaSalida) {
		this.listadoConstanciaSalida = listadoConstanciaSalida;
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


	public String getFolio() {
		return folio;
	}


	public void setFolio(String folio) {
		this.folio = folio;
	}


	public List<Cliente> getListadoClientes() {
		return listadoClientes;
	}


	public void setListadoClientes(List<Cliente> listadoClientes) {
		this.listadoClientes = listadoClientes;
	}


	public Cliente getCliente() {
		return cliente;
	}


	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}


	public ConstanciaSalida getConstanciaSelect() {
		return constanciaSelect;
	}


	public void setConstanciaSelect(ConstanciaSalida constanciaSelect) {
		this.constanciaSelect = constanciaSelect;
	}
	
	
	
	
}
