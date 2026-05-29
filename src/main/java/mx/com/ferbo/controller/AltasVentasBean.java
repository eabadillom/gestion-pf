package mx.com.ferbo.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
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
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.FacesUtils;
import mx.com.ferbo.util.FormatUtil;
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
@Named(value = "altasVentasBean")
@ViewScoped
public class AltasVentasBean implements Serializable 
{
    private static Logger log = LogManager.getLogger(AltasVentasBean.class);
    private static final long serialVersionUID = -1L;
    
    @Inject
    private VentaDAO ventasDAO;
    
    @Inject
    private EmisoresCFDISDAO emisoresCFDISDAO;
    
    @Inject
    private PlantaDAO plantaDAO;
    
    private Ventas ventaSelected;
    private DetalleVenta detalleVenta;
    
    private Planta plantaSelected;
    private List<Planta> listPlantas;
    
    private EmisoresCFDIS emisoresCFDIS;
    private List<EmisoresCFDIS> listEmisores;
    
    private Cliente clienteSelected;
    private List<Cliente> listClientes;
    
    private BigDecimal total = BigDecimal.ZERO;
    private String montoLetra;
    private Date mesActual;
    
    private Usuario usuario;
    private FacesContext faceContext;
    private HttpServletRequest request;
    
    private StreamedContent file;
    
    public AltasVentasBean(){   
    }
    
    @PostConstruct
    public void init() 
    {
        this.faceContext = FacesContext.getCurrentInstance();
        this.request = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        this.usuario = (Usuario) request.getSession(false).getAttribute("usuario");
        log.info("El usuario {} entra a alta ventas.", this.usuario.getUsuario());
        
        this.listPlantas = this.plantaDAO.buscarTodos(false);
        this.listClientes = (List<Cliente>) request.getSession(false).getAttribute("clientesActivosList");
        this.listEmisores = this.emisoresCFDISDAO.buscarTodos(false);
        this.mesActual = new Date();
        
        this.crearVenta();
    }
    
    public void seleccionarEmisor()
    {
        this.emisoresCFDIS = (this.plantaSelected != null) ? this.plantaSelected.getIdEmisoresCFDIS() : null;
    }
    
    public void crearVenta() 
    {
        this.ventaSelected = new Ventas();
        this.ventaSelected.setDetalles(new ArrayList());
        this.detalleVenta = new DetalleVenta();
        this.total = BigDecimal.ZERO;
        this.clienteSelected = new Cliente();
        this.emisoresCFDIS = new EmisoresCFDIS();
        this.plantaSelected = new Planta();
    }
    
    public void crearDetalleVenta(){
        this.detalleVenta = new DetalleVenta();
        log.info("Creando un concepto de la venta");
    }
    
    public void agregaConcepto()
    {
        Integer cantidad = this.detalleVenta.getCantidad();
        BigDecimal precioUnitario = this.detalleVenta.getPrecioUnitario();
        BigDecimal subTotal = precioUnitario.multiply(BigDecimal.valueOf(cantidad));
        
        this.detalleVenta.setSubtotal(subTotal);
        this.detalleVenta.setVenta(this.ventaSelected);
        this.ventaSelected.getDetalles().add(this.detalleVenta);
        this.calcularTotal();
        log.info("Agregando concepto a la venta");
    }
    
    public void calcularTotal() 
    {
        this.total = this.ventaSelected.getDetalles().stream()
            .map(d -> d.getSubtotal())
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        log.info("Calculando el total de la venta");
    }
    
    public void eliminarDetalle(DetalleVenta item)
    {
        this.ventaSelected.getDetalles().remove(item);
        this.calcularTotal();
        log.info("Eliminando un concepto de la venta");
    }
    
    public void guardar()
    {
        FormatUtil formato = new FormatUtil();
        try {
            log.info("Iniciando el guardado de una venta");
            
            if(this.ventaSelected == null)
                throw new InventarioException("Error al guardar una venta");
            
            if(this.clienteSelected == null)
                throw new InventarioException("Error al seleccionar un cliente");
            
            if(this.emisoresCFDIS == null)
                throw new InventarioException("Error al seleccionar un emisor");
            
            this.ventaSelected.setCteCve(this.clienteSelected);
            this.ventaSelected.setCdEmisor(this.emisoresCFDIS);
            this.ventaSelected.setFecha(new Date());
            this.calcularTotal();
            this.ventaSelected.setTotal(this.total);
            this.ventaSelected.setMontoLetra(formato.getMontoConLetra(this.total.doubleValue()));
            
            this.ventasDAO.guardar(this.ventaSelected);
            FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, "Venta", "Se guardo con exito");
            log.info("Guardando una venta");
        } catch (InventarioException ex) {
            log.info("Error al guardar una venta: ",ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, "Venta", ex.getMessage());
        } catch (Exception e) {
            log.error("Error: ", e);
            FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Venta", "Error al registrar la venta, favor de contactar al administrador de sistemas");
        } finally {
            PrimeFaces.current().ajax().update(":form:messages");
        }
    }
    
    public void exportarPDF()
    {
        log.info("Exportando a PDF");
        String jasperPath = "/jasper/TicketVenta.jrxml";
        String filename = "Venta" +mesActual+ ".pdf";
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

    public void setVentaSelected(Ventas ventaSelected) {
        this.ventaSelected = ventaSelected;
    }

    public DetalleVenta getDetalleVenta() {
        return detalleVenta;
    }

    public void setDetalleVenta(DetalleVenta detalleVenta) {
        this.detalleVenta = detalleVenta;
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

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getMontoLetra() {
        return montoLetra;
    }

    public void setMontoLetra(String montoLetra) {
        this.montoLetra = montoLetra;
    }

    public StreamedContent getFile() {
        return file;
    }

    public void setFile(StreamedContent file) {
        this.file = file;
    }
    
}
