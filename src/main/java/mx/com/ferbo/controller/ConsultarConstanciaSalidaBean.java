package mx.com.ferbo.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Base64;
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
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.event.CaptureEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.ferbo.mail.beans.Adjunto;

import mx.com.ferbo.business.SendMailTicketSalida;
import mx.com.ferbo.dao.ConstanciaSalidaDAO;
import mx.com.ferbo.dao.DetalleConstanciaSalidaDAO;
import mx.com.ferbo.dao.PartidaDAO;
import mx.com.ferbo.dao.StatusConstanciaSalidaDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ConstanciaSalida;
import mx.com.ferbo.model.DetalleConstanciaSalida;
import mx.com.ferbo.model.Partida;
import mx.com.ferbo.model.StatusConstanciaSalida;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.ImageTool;
import mx.com.ferbo.util.InventarioException;
import mx.com.ferbo.util.JasperReportUtil;
import mx.com.ferbo.util.conexion;

@Named
@ViewScoped
public class ConsultarConstanciaSalidaBean implements Serializable{
	
	private static final long serialVersionUID = -3109002730694247052L;
	private static Logger log = LogManager.getLogger(ConsultarConstanciaSalidaBean.class);
	
	private ConstanciaSalidaDAO constanciaSalidaDAO;
	private List<ConstanciaSalida> listadoConstanciaSalida;
	
	private DetalleConstanciaSalidaDAO detalleCSDAO;
	private List<DetalleConstanciaSalida> listadoDetalleConstanciaS;
	
	private Date fechaInicial;
	private Date fechaFinal;
	private String folio;
	private Integer idPlanta;
	
	private List<Cliente> listadoClientes;
	private Cliente cliente;
	private PartidaDAO partidaDAO;
	
	private ConstanciaSalida constanciaSelect;
	private StatusConstanciaSalida statusCancelada;
	private StatusConstanciaSalidaDAO statusDAO;
	
	private FacesContext faceContext;
	private HttpServletRequest httpServletRequest;
	private Usuario usuario;
        
	private StreamedContent file;
	private String imagenBase64;
	private byte[] imagen;
	private String mensaje;
	private Integer realWidth;
	private Integer realHeight;
	
	public ConsultarConstanciaSalidaBean() {
		listadoConstanciaSalida = new ArrayList<>();
		listadoClientes = new ArrayList<Cliente>();
		listadoDetalleConstanciaS = new ArrayList<>();
	}
	
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		
		faceContext = FacesContext.getCurrentInstance();
		httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
		usuario = (Usuario) httpServletRequest.getSession(false).getAttribute("usuario");
		
		if(usuario.getPerfil() == 1 || usuario.getPerfil() == 4) {
			this.fechaInicial = new Date();
			DateUtil.setTime(fechaInicial, 0, 0, 0, 0);
			
			this.fechaFinal = new Date();
			DateUtil.setTime(fechaFinal, 23, 59, 59, 999);
			
			this.idPlanta = usuario.getIdPlanta();
		}
		
		constanciaSalidaDAO = new ConstanciaSalidaDAO();
		detalleCSDAO = new DetalleConstanciaSalidaDAO();
		partidaDAO = new PartidaDAO();
		statusDAO = new StatusConstanciaSalidaDAO();
		
		listadoClientes = (List<Cliente>) httpServletRequest.getSession(false).getAttribute("clientesActivosList");
		if(listadoClientes.size() == 1)
			this.cliente = listadoClientes.get(0);
		constanciaSelect = new ConstanciaSalida();
		statusCancelada = statusDAO.buscarPorId(StatusConstanciaSalida.STATUS_CANCELADA);
		
		log.info("El usuario {} ingresa a la consulta de constancias de salida.", this.usuario.getUsuario());
	}
	
	public void buscarConstanciaSalida() {
		if(folio != null && "".equalsIgnoreCase(folio.trim()))
			this.folio = null;
		
		if("".equalsIgnoreCase(this.folio))
			this.folio = null;
		
		if(listadoClientes.size() == 1)
			this.cliente = listadoClientes.get(0);
		
		listadoConstanciaSalida = constanciaSalidaDAO.buscar((cliente == null ? null : cliente.getCteCve()), this.idPlanta, fechaInicial, fechaFinal, folio);
	}
	
	public void cargaDetalle() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Carga de información...";
		
		try {
			this.constanciaSelect = constanciaSalidaDAO.buscarPorId(this.constanciaSelect.getId(), true);
			log.info("Cargando constancia de salida: {}", this.constanciaSelect.getNumero());
			this.imagenBase64 = null;
			this.mensaje = null;
			PrimeFaces.current().executeScript("iniciarCamara()");
		} catch (Exception ex) {
			log.error("Problema para cargar la información de la constancia...", ex);
			mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;
			
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
		} finally {
			PrimeFaces.current().ajax().update("form:messages","form:dlg-constancia", "form:dlg-partidas", "form:dlg-servicios");
			
		}
	}

	public void imprimirTicket() {
		String jasperPath = "/jasper/ConstanciaSalida.jrxml";
		String filename = String.format("ticket-salida_%s.pdf", constanciaSelect.getNumero());
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
                        byte[] bytes = jasperReportUtil.createPDF(parameters, reportFile.getPath());
			InputStream input = new ByteArrayInputStream(bytes);
                        this.file = DefaultStreamedContent.builder().contentType("application/pdf").name(filename).stream(() -> input).build();
                        log.info("Reporte generado {}...", filename);
		} catch (Exception e) {
                        log.info("Problema para imprimir el ticket...", e);
			message = String.format("No se pudo imprimir el folio %s", (constancia == null ? "" : constancia.getNumero()));
			severity = FacesMessage.SEVERITY_INFO;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity,"Error en impresion",message));
			PrimeFaces.current().ajax().update("form:messages");
			
		}finally {
			conexion.close((Connection) connection);
		}
	}

	public void cancelarConstancia() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Cancelar constancia";
		
		Date fechaSalida;
		int coincidencias = 0;
		
		String observaciones = null;
		String resultUpdate = null;
		
		try {
			log.info("Iniciando la cancelacion de la constancia de salida: {}", constanciaSelect.getNumero());
                        
			if(this.constanciaSelect.getStatus().getId() == 2)
				throw new InventarioException("La constancia ya se encuentra cancelada");
			
			for(DetalleConstanciaSalida d: constanciaSelect.getDetalleConstanciaSalidaList()) {
				Partida buscarPartida =  partidaDAO.buscarPorId(d.getPartidaCve().getPartidaCve(), true);
				log.debug("PartidaCve: {}",d.getPartidaCve().getPartidaCve());
				listadoDetalleConstanciaS = detalleCSDAO.buscarPorPartidaCve(buscarPartida, true);
				
				for(DetalleConstanciaSalida detalle: listadoDetalleConstanciaS) {
					
					if(buscarPartida.equals(detalle.getPartidaCve())) {
						fechaSalida = detalle.getConstanciaCve().getFecha();
						
						if(constanciaSelect.getFecha().before(fechaSalida)) {	
							coincidencias = coincidencias + 1;
						}
					}
				}
				if(coincidencias >= 1 ) {
					throw new InventarioException("La Constancia De Salida: " +constanciaSelect.getNumero()+ " tiene salidas posteriores");
				}
			}
			
			if(coincidencias==0) {
				observaciones = String.format("CONSTANCIA CANCELADA EL DIA %s", DateUtil.getString(new Date(), DateUtil.FORMATO_DD_MM_YYYY));
				constanciaSelect.setStatus(statusCancelada);
				constanciaSelect.setObservaciones(observaciones);
				resultUpdate = constanciaSalidaDAO.actualizarStatus(constanciaSelect);
			}
			
			if(resultUpdate != null) {
				throw new InventarioException("Ocurrió un problema para actualizar la constancia");
			}
			
			if(folio != null && "".equalsIgnoreCase(folio.trim()))
				this.folio = null;
			
			if("".equalsIgnoreCase(this.folio))
				this.folio = null;
			
			listadoConstanciaSalida = constanciaSalidaDAO.buscar((cliente == null ? null : cliente.getCteCve()), this.idPlanta, fechaInicial, fechaFinal, folio);
			log.info("La constancia de salida {} se cancelo exitosamente.", constanciaSelect.getNumero());
			
			mensaje = String.format("La constancia de salida %s fue cancelada", constanciaSelect.getNumero());
			severity = FacesMessage.SEVERITY_INFO;
		} catch (InventarioException ex) {	
			log.error("Problema para cancelar la constancia de salida...", ex);
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} catch (Exception ex) {
			log.error("Problema para cargar la información de la constancia...", ex);
			mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			
			PrimeFaces.current().ajax().update("form:messages", "form:dt-ConstanciaSalida");
		}
	}
	
	public void recibirInfoImagen() {
		log.info("Height: {}, Width: {}", this.realHeight, this.realWidth);
		Boolean esVertical = realHeight > realWidth;
		
		if(esVertical.booleanValue())
			log.info("Es vertical");
		else
			log.info("Es horizontal");
		PrimeFaces.current().ajax().update("form:camera");
	}
	
	public void capturar(CaptureEvent event) {
        try {
            byte[] imageData = event.getData();
            
            this.imagen = ImageTool.resizeImage(imageData, this.realWidth, this.realHeight);
            
            if (imageData != null && imageData.length > 0) {
                imagenBase64 = "data:image/png;base64," + Base64.getEncoder().encodeToString(this.imagen);
                mensaje = "Foto capturada correctamente.";
            } else {
                mensaje = "No se recibió imagen.";
            }

        } catch (Exception e) {
            mensaje = "Error al procesar la imagen: " + e.getMessage();
        } finally {
        	PrimeFaces.current().executeScript("PF('dialogFotografia').hide()");
        }
    }
	
	public void enviar() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Confirmación";
		
		SendMailTicketSalida sendBO = null;
		
		try {
			sendBO = new SendMailTicketSalida(constanciaSelect.getClienteCve().getCteCve());
			sendBO.setFolio(this.constanciaSelect.getNumero());
			sendBO.setLoggedUser(this.usuario);
			sendBO.addAttachment("no-name.jpg", Adjunto.TP_ARCHIVO_JPEG, this.imagen);
			sendBO.send();
			
			PrimeFaces.current().executeScript("PF('dlgDetalles').hide()");
			mensaje = "El mensaje se envió correctamente.";
			severity = FacesMessage.SEVERITY_INFO;
		} catch(Exception ex) {
			log.error("Problema para enviar el correo electrónico...", ex);
			mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}
	
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
	
	public Usuario getUsuario() {
		return usuario;
	}
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public StreamedContent getFile() {
		return file;
	}

	public void setFile(StreamedContent file) {
		this.file = file;
	}
	public String getImagenBase64() {
		return imagenBase64;
	}

	public void setImagenBase64(String imagenBase64) {
		this.imagenBase64 = imagenBase64;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public Integer getRealWidth() {
		return realWidth;
	}

	public void setRealWidth(Integer realWidth) {
		this.realWidth = realWidth;
	}

	public Integer getRealHeight() {
		return realHeight;
	}

	public void setRealHeight(Integer realHeight) {
		this.realHeight = realHeight;
	}

    
}
