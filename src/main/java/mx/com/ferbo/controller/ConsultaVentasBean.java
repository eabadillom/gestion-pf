package mx.com.ferbo.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.sql.Connection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import mx.com.ferbo.dao.n.EmisoresCFDISDAO;
import mx.com.ferbo.dao.n.PlantaDAO;
import mx.com.ferbo.dao.n.VentaDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.DetalleVenta;
import mx.com.ferbo.model.EmisoresCFDIS;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.model.Ventas;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.FacesUtils;
import mx.com.ferbo.util.InventarioException;
import mx.com.ferbo.util.JasperReportUtil;
import mx.com.ferbo.util.conexion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author alberto
 */
@Named(value = "consultaVentasBean")
@ViewScoped
public class ConsultaVentasBean implements Serializable 
{
    private static Logger log = LogManager.getLogger(ConsultaVentasBean.class);
    private static final long serialVersionUID = -1L;
    
    @Inject
    private VentaDAO ventasDAO;
    
    @Inject
    private EmisoresCFDISDAO emisoresCFDISDAO;
    
    @Inject
    private PlantaDAO plantaDAO;
    
    private Ventas ventaSelected;
    private List<Ventas> listVentas;
    
    private DetalleVenta detalleVenta;
    private List<DetalleVenta> listDetalleVenta;
    
    private Planta plantaSelected;
    private List<Planta> listPlantas;
    
    private Cliente clienteSelected;
    private List<Cliente> listClientes;
    
    private EmisoresCFDIS emisoresCFDIS;
    private List<EmisoresCFDIS> listEmisores;
    
    private Date fechaInicio;
    private Date fechaFin;
    private Date mesActual;
    
    private Usuario usuario;
    private FacesContext faceContext;
    private HttpServletRequest request;
    
    private StreamedContent file;

    public ConsultaVentasBean() {
        this.fechaFin = new Date();
        this.fechaInicio = DateUtil.moverFechaSemanaAtras(this.fechaFin);
    }
    
    @PostConstruct
    public void init() 
    {
        faceContext = FacesContext.getCurrentInstance();
        request = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        usuario = (Usuario) request.getSession(false).getAttribute("usuario");
        log.info("El usuario {} entra a consultar ventas", this.usuario.getUsuario());
        
        this.listPlantas = this.plantaDAO.buscarTodos(false);
        listClientes = (List<Cliente>) request.getSession(false).getAttribute("clientesActivosList");
        listEmisores = emisoresCFDISDAO.buscarTodos(false);
        this.mesActual = new Date();
    }
    
    public void seleccionarEmisor()
    {
        this.emisoresCFDIS = this.plantaSelected.getIdEmisoresCFDIS();
    }
    
    public void buscarVentas(){
        Integer idCliente;
        Integer idEmisor;
        try {
            log.info("Entrando a buscar la venta con los parametros dados");
            
            if(this.plantaSelected == null){
                throw new InventarioException("Error, debes seleccionar una planta");
            } 
            
            if(this.clienteSelected != null)
                idCliente = this.clienteSelected.getCteCve();
            else
                idCliente = null;
            
            if(this.emisoresCFDIS != null)
                idEmisor = this.emisoresCFDIS.getCd_emisor();
            else
                idEmisor = null;
            
            DateUtil.setTime(this.fechaInicio, 0, 0, 0);
            DateUtil.setTime(this.fechaFin, 11, 59, 59);
            
            this.listVentas = ventasDAO.buscarPorParametro(idCliente, idEmisor, this.fechaInicio, this.fechaFin);
            FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, "Consultar", "Se cargo la venta del periodo solicitado");
        } catch(InventarioException ex) {
            log.error("Problema en la carga de una venta...", ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, "Consultar", ex.getMessage());
        } catch(Exception ex) {
            log.error("Problema en la búsqueda de ventas...", ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Consultar", "Error al cargar las venta del periodo, favor de consultar con el administrador de sistemas");
        } finally{
            PrimeFaces.current().ajax().update("form:messages");
        }
    }
    
    public void cargaDetalleVenta(){
        try{
            if(this.ventaSelected == null)
                throw new InventarioException("Error al abrir la venta seleccionada");
            
            this.listDetalleVenta = this.ventaSelected.getDetalles();
            
            log.info("Editando una venta");
            
            FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, "Consulta", "Se cargo el detalle de la venta");
        } catch(InventarioException ex) {
            log.error("Problema en la carga de una venta...", ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, "Consulta", ex.getMessage());
        } catch(Exception ex) {
            log.error("Problema en la carga de una venta...", ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Consulta", "Error al cargar el detalle de la venta, favor de consultar con el administrador de sistemas");
        } finally{
            PrimeFaces.current().ajax().update("form:messages");
        }
    }
    
    public void exportarPDF()
    {
        log.info("Exportando a PDF");
        String jasperPath = "/jasper/ComprobanteVenta.jrxml";
        String filename = "ReporteVenta" +mesActual+ ".pdf";
        String images = "/images/logo.jpeg";
        File reportFile = null;
        File imgfile = null;
        JasperReportUtil jasperReportUtil = new JasperReportUtil();
        Map<String, Object> parameters = null;
        Connection connection = null;
        parameters = new HashMap<String, Object>();

        try {
            URL resource = getClass().getResource(jasperPath);
            URL resourceimg = getClass().getResource(images);
            String file = resource.getFile();
            String img = resourceimg.getFile();
            reportFile = new File(file);
            imgfile = new File(img);
            
            Integer idVenta = null;
            Integer idPlanta = null;
            
            if(this.ventaSelected.getCdEmisor() == null){
                throw new InventarioException("Error al obtener la venta");
            } else {
                idVenta = this.ventaSelected.getIdVentas();
            }
            
            if(this.plantaSelected.getPlantaCve() == null){
                throw new InventarioException("Error al obtener la planta");
            } else {
                idPlanta = this.plantaSelected.getPlantaCve();
            }

            connection = EntityManagerUtil.getConnection();
            parameters.put("REPORT_CONNECTION", connection);
            parameters.put("IDVENTA", idVenta);
            parameters.put("IDPLANTA", idPlanta);
            parameters.put("LogoPath", imgfile.getPath());
            byte[] bytes = jasperReportUtil.createPDF(parameters, reportFile.getPath());
            InputStream input = new ByteArrayInputStream(bytes);
            this.file = DefaultStreamedContent.builder().contentType("application/pdf").name(filename).stream(() -> input).build();
            log.info("Reporte generado {}...", filename);
            FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, "Venta", "Ticket generado correctamente");
        } catch (InventarioException ex) {
            log.info("Error al generar el ticket: ",ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, "Venta", ex.getMessage());
        } catch (Exception ex) {
            log.error("Problema general...", ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Venta", "No se puedo imprimir el ticket");
        } finally {
            conexion.close((Connection) connection);
            PrimeFaces.current().ajax().update("form:messages");
        }
    }

    public Ventas getVentaSelected() {
        return ventaSelected;
    }

    public void setVentaSelected(Ventas venta) {
        this.ventaSelected = venta;
    }

    public List<Ventas> getListVentas() {
        return listVentas;
    }

    public void setListVentas(List<Ventas> listVentas) {
        this.listVentas = listVentas;
    }

    public DetalleVenta getDetalleVenta() {
        return detalleVenta;
    }

    public void setDetalleVenta(DetalleVenta detalleVenta) {
        this.detalleVenta = detalleVenta;
    }

    public List<DetalleVenta> getListDetalleVenta() {
        return listDetalleVenta;
    }

    public void setListDetalleVenta(List<DetalleVenta> listDetalleVenta) {
        this.listDetalleVenta = listDetalleVenta;
    }

    public Planta getPlantaSelected() {
        return plantaSelected;
    }

    public void setPlantaSelected(Planta plantaSelected) {
        this.plantaSelected = plantaSelected;
    }

    public List<Planta> getListPlantas() {
        return listPlantas;
    }

    public void setListPlantas(List<Planta> listPlantas) {
        this.listPlantas = listPlantas;
    }

    public Cliente getClienteSelected() {
        return clienteSelected;
    }

    public void setClienteSelected(Cliente clienteSelected) {
        this.clienteSelected = clienteSelected;
    }

    public List<Cliente> getListClientes() {
        return listClientes;
    }

    public void setListClientes(List<Cliente> listClientes) {
        this.listClientes = listClientes;
    }

    public EmisoresCFDIS getEmisoresCFDIS() {
        return emisoresCFDIS;
    }

    public void setEmisoresCFDIS(EmisoresCFDIS emisoresCFDIS) {
        this.emisoresCFDIS = emisoresCFDIS;
    }

    public List<EmisoresCFDIS> getListEmisores() {
        return listEmisores;
    }

    public void setListEmisores(List<EmisoresCFDIS> listEmisores) {
        this.listEmisores = listEmisores;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public StreamedContent getFile() {
        return file;
    }

    public void setFile(StreamedContent file) {
        this.file = file;
    }
    
}
