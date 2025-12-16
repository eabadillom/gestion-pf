package mx.com.ferbo.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import mx.com.ferbo.dao.n.EmisoresCFDISDAO;
import mx.com.ferbo.dao.n.VentaDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.EmisoresCFDIS;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.model.Ventas;
import mx.com.ferbo.util.FacesUtils;
import mx.com.ferbo.util.FormatUtil;
import mx.com.ferbo.util.InventarioException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;
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
    
    private Ventas venta;
    
    private Cliente clienteSelected;
    private List<Cliente> listClientes;
    
    private EmisoresCFDIS emisoresCFDIS;
    private List<EmisoresCFDIS> listEmisores;
    
    private BigDecimal total;
    private String montoLetra;
    
    private Usuario usuario;
    private FacesContext faceContext;
    private HttpServletRequest request;
    
    private StreamedContent file;
    
    public AltasVentasBean(){   
    }
    
    @PostConstruct
    public void init() 
    {
        faceContext = FacesContext.getCurrentInstance();
        request = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        usuario = (Usuario) request.getSession(false).getAttribute("usuario");
        log.info("El usuario {} entra a alta ventas.", this.usuario.getUsuario());
        
        listClientes = (List<Cliente>) request.getSession(false).getAttribute("clientesActivosList");
        //listClientes = clienteDAO.buscarTodos();
        listEmisores = emisoresCFDISDAO.buscarTodos(false);
        
        this.crearVenta();
    }
    
    public void crearVenta() 
    {
        venta = new Ventas();
        this.clienteSelected = new Cliente();
        this.emisoresCFDIS = new EmisoresCFDIS();
    }
    
    public void guardar()
    {
        FormatUtil formato = new FormatUtil();
        try {
            log.info("Iniciando el guardado de una venta");
            
            if(this.venta== null)
                throw new InventarioException("Error al guardar una venta");
            
            if(this.clienteSelected == null)
                throw new InventarioException("Error al seleccionar un cliente");
            
            if(this.emisoresCFDIS == null)
                throw new InventarioException("Error al seleccionar un emisor");
            
            venta.setCteCve(clienteSelected);
            venta.setCdEmisor(emisoresCFDIS);
            venta.setFecha(new Date());
            venta.setMontoLetra(formato.getMontoConLetra(venta.getTotal().doubleValue()));
            
            ventasDAO.guardar(venta);
            FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, "Venta", "Se guardo con exito");
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
    
    public void imprimirTicket(){
        log.info("Entrando a imprimir ticket");
    }

    public Ventas getVenta() {
        return venta;
    }

    public void setVenta(Ventas venta) {
        this.venta = venta;
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
