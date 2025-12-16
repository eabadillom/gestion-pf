package mx.com.ferbo.controller;

import java.io.Serializable;
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
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.FacesUtils;
import mx.com.ferbo.util.InventarioException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;
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
    
    private Ventas venta;
    private List<Ventas> listVentas;
    
    private Cliente clienteSelected;
    private List<Cliente> listClientes;
    
    private EmisoresCFDIS emisoresCFDIS;
    private List<EmisoresCFDIS> listEmisores;
    
    private Date fechaInicio;
    private Date fechaFin;
    
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
        
        listClientes = (List<Cliente>) request.getSession(false).getAttribute("clientesActivosList");
        //listClientes = clienteDAO.buscarTodos();
        listEmisores = emisoresCFDISDAO.buscarTodos(false);
    }
    
    public void buscarVentas(){
        Integer idCliente = null;
        Integer idEmisor= null;
        try {
            log.info("Entrando a buscar con los parametros dados");
            if(this.clienteSelected != null)
                idCliente = this.clienteSelected.getCteCve();
            
            if(this.emisoresCFDIS != null)
                idEmisor = this.emisoresCFDIS.getCd_emisor();
            
            DateUtil.setTime(this.fechaInicio, 0, 0, 0);
            DateUtil.setTime(this.fechaFin, 11, 59, 59);
            
            this.listVentas = ventasDAO.buscarPorParametro(idCliente, idEmisor, this.fechaInicio, this.fechaFin);
            FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, "Consultar", "Se cargo las ventas del periodo solicitado");
        } catch(Exception ex) {
            log.error("Problema en la búsqueda de ventas...", ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Consultar", "Error al cargar las venta del periodo, favor de consultar con el administrador de sistemas");
        } finally{
            PrimeFaces.current().ajax().update("form:messages");
        }
    }
    
    public void cargaDetalle(){
        try{
            if(this.venta == null)
                throw new InventarioException("No se encontro la venta seleccionada");
            
            log.info("Editando una venta: {}", this.venta.getConcepto());
            
            FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, "Venta", "Se cargo con exito el detalle de la venta");
        } catch(InventarioException ex) {
            log.error("Problema en la carga de una venta...", ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, "Venta", ex.getMessage());
        } catch(Exception ex) {
            log.error("Problema en la carga de una venta...", ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Venta", "Error al cargar el detalle de la venta, favor de consultar con el administrador de sistemas");
        } finally{
            PrimeFaces.current().ajax().update("form:messages");
        }
    }

    public Ventas getVenta() {
        return venta;
    }

    public void setVenta(Ventas venta) {
        this.venta = venta;
    }

    public List<Ventas> getListVentas() {
        return listVentas;
    }

    public void setListVentas(List<Ventas> listVentas) {
        this.listVentas = listVentas;
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
