package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import mx.com.ferbo.dao.n.EmisoresCFDISDAO;
import mx.com.ferbo.dao.n.SerieComplementoPagoDAO;
import mx.com.ferbo.dao.n.StatusSerieComplementoDAO;
import mx.com.ferbo.model.EmisoresCFDIS;
import mx.com.ferbo.model.SerieComplementoPago;
import mx.com.ferbo.model.StatusSerieComplemento;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.util.DAOException;
import mx.com.ferbo.util.InventarioException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

@Named
@ViewScoped
public class SerieComplementoBean implements Serializable
{
    private static final long serialVersionUID = 1;
    private static final Logger log = LogManager.getLogger(SerieComplementoBean.class);
    
    @Inject
    private SerieComplementoPagoDAO serieComplementoDAO;
    
    @Inject
    private EmisoresCFDISDAO emisorDAO;
    
    @Inject
    private StatusSerieComplementoDAO statusSerieCompDAO;
    
    private List<SerieComplementoPago> listSerieComplemento;
    private List<StatusSerieComplemento> listStatusSerieComp;
    private List<EmisoresCFDIS> listEmisores;

    private Usuario usuario;
    private FacesContext faceContext;
    private HttpServletRequest httpServletRequest;
    
    private SerieComplementoPago serieComplementoSelected;
    private StatusSerieComplemento statusSerieComplemento;
    private EmisoresCFDIS emisorSelected;
    
    private String serie = null;
    private String numero = null; 
    
    @PostConstruct
    public void init() {
        try {
            faceContext = FacesContext.getCurrentInstance();
            httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
            usuario = (Usuario) httpServletRequest.getSession(false).getAttribute("usuario");
            
            listStatusSerieComp = statusSerieCompDAO.buscarTodos();
            listEmisores = emisorDAO.buscarTodos(true);
            consultarComplementoPago();
            log.info("El usuario {} entra a Ingresos / Series de Complementos de Pagos...", this.usuario.getUsuario());
        } catch(Exception ex) {
            log.error("Problema con el inicio del controller...", ex);
        }
    }
    
    public void consultarComplementoPago() throws DAOException {
        listSerieComplemento = (emisorSelected == null) ? serieComplementoDAO.buscarTodos() : serieComplementoDAO.buscarSeriesPorEmisor(emisorSelected.getCd_emisor());
    }
    
    public void openNew() {
        log.info("Inicializando complemento de pago...");
        serieComplementoSelected = new SerieComplementoPago();
        serieComplementoSelected.setEmisor((emisorSelected == null) ? null : emisorSelected);
        serie = null;
        numero = null;
        statusSerieComplemento = null;
    }
    
    public void filtrarSeriesComplementos() {
        FacesMessage message = null;
        FacesMessage.Severity severity = null;
        String mensaje = null;
        String titulo = "Serie complemento de pago";
        try {
            consultarComplementoPago(); 
            
            log.info("El usuario {} a filtrado las serie complementos de pago...", this.usuario.getUsuario());
        } catch (Exception ex) {
            log.error("Problema para consultar la lista de serie complementos de pago...", ex);
            mensaje = "Ocurrió un problema al obtener la serie de complemento de pago.";
            severity = FacesMessage.SEVERITY_ERROR;
        } finally {
            if(mensaje != null) {
                message = new FacesMessage(severity, titulo, mensaje);
                FacesContext.getCurrentInstance().addMessage(null, message);
                PrimeFaces.current().ajax().update("form:messages");
            }
        }
    }
    
    public synchronized void guardar() {
        FacesMessage message = null;
        FacesMessage.Severity severity = null;
        String mensaje = null;
        String titulo = "Serie complemento de pago";
        try {
            log.info("El usuario {} a entrado a guardar una serie complemento de pago...", this.usuario.getUsuario());
            
            if(emisorSelected == null) {
                throw new InventarioException("Debe seleccionar a un emisor.");
            }
            
            if(serie == null) {
                throw new InventarioException("Debe agregar una serie");
            }
            
            if(numero == null) {
                throw new InventarioException("Debe agregar un numero");
            }
            
            if(statusSerieComplemento == null) {
                throw new InventarioException("Debe seleccionar una status");
            }
            
            for(SerieComplementoPago serieComp: listSerieComplemento){
                if(serieComp.getNumero().equals(numero) && serieComp.getSerie().equals(serie)) {
                    throw new InventarioException("Ya se encuentra registrado el complemento de pago");
                }
            }
            
            serieComplementoSelected = new SerieComplementoPago();
            serieComplementoSelected.setEmisor(emisorSelected);
            serieComplementoSelected.setSerie(serie);
            serieComplementoSelected.setNumero(numero);
            serieComplementoSelected.setStatusSerie(statusSerieComplemento);
            
            serieComplementoDAO.guardar(serieComplementoSelected);
            log.info("El usuario {} a guardado una serie complemento de pago...", this.usuario.getUsuario());
            mensaje = "Se guardó correctamente.";
            
            severity = FacesMessage.SEVERITY_INFO;
            serie = null;
            numero = null;
            emisorSelected = null;
            statusSerieComplemento = null;
            serieComplementoSelected = null;
            consultarComplementoPago();
        } catch (InventarioException ex) {
            log.info("Error al guardar la información de la serie complemento de pago", ex);
            mensaje = ex.getMessage();
            severity = FacesMessage.SEVERITY_WARN;
            FacesContext.getCurrentInstance().validationFailed();
        } catch (Exception ex) {
            log.error("Problema para guardar la información de la serie complemento de pago...", ex);
            mensaje = "Ocurrió un problema al guardar la información.";
            severity = FacesMessage.SEVERITY_ERROR;
            FacesContext.getCurrentInstance().validationFailed();
        } finally {
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update("form:messages", "form:dtSerieComplementos");
        }
    }
    
    public synchronized void actualizar() {
        FacesMessage message = null;
        FacesMessage.Severity severity = null;
        String mensaje = null;
        String titulo = "Serie complemento de pago";
        try {
            log.info("El usuario {} a entrado a actualizar una serie complemento de pago...", this.usuario.getUsuario());
            
            if(emisorSelected == null) {
                throw new InventarioException("Debe agregar un emisor.");
            }
            
            if(serie == null) {
                throw new InventarioException("Debe agregar una serie");
            }
            
            if(numero == null) {
                throw new InventarioException("Debe agregar un numero");
            }
            
            if(statusSerieComplemento == null) {
                throw new InventarioException("Debe seleccionar una status");
            }
            
            serieComplementoSelected.setEmisor(emisorSelected);
            serieComplementoSelected.setSerie(serie);
            serieComplementoSelected.setNumero(numero);
            serieComplementoSelected.setStatusSerie(statusSerieComplemento);
            
            serieComplementoDAO.actualizar(serieComplementoSelected);
            log.info("El usuario {} a actualizado una serie complemento de pago...", this.usuario.getUsuario());
            mensaje = "Se actualizo correctamente.";
            
            severity = FacesMessage.SEVERITY_INFO;
            serie = null;
            numero = null;
            emisorSelected = null;
            statusSerieComplemento = null;
            serieComplementoSelected = null;
            consultarComplementoPago();
        } catch (InventarioException ex) {
            log.info("Error al actualizar la información de la serie complemento de pago", ex);
            mensaje = ex.getMessage();
            severity = FacesMessage.SEVERITY_WARN;
            FacesContext.getCurrentInstance().validationFailed();
        } catch (Exception ex) {
            log.error("Problema para actualizar la información de la serie complemento de pago...", ex);
            mensaje = "Ocurrió un problema al actualizar la información.";
            severity = FacesMessage.SEVERITY_ERROR;
            FacesContext.getCurrentInstance().validationFailed();
        } finally {
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update("form:messages", "form:dtSerieComplementos");
        }
    }
    
    public void actualizarComplemento(SerieComplementoPago serieCompPago) {
        try {
            this.serieComplementoSelected = serieCompPago;
            log.info("Serie complemento de pago seleccionado: {}", serieComplementoSelected.toString());
            
            this.serie = serieComplementoSelected.getSerie();
            this.numero = serieComplementoSelected.getNumero();
            this.emisorSelected = serieComplementoSelected.getEmisor();
            this.statusSerieComplemento = serieComplementoSelected.getStatusSerie();
        } catch (Exception ex) {
            log.error("Problema para obtener la información de la serie de complemento de pago...", ex);
        } finally {
            PrimeFaces.current().ajax().update("form:messages", "form:panelActualizarComplemento");
        }
    }
    
    public void cerrarDialogo() {
        serie = null;
        numero = null;
        emisorSelected = null;
        statusSerieComplemento = null;
        serieComplementoSelected = null;
    }

    public List<SerieComplementoPago> getListSerieComplemento() {
        return listSerieComplemento;
    }

    public void setListSerieComplemento(List<SerieComplementoPago> listSerieComplemento) {
        this.listSerieComplemento = listSerieComplemento;
    }

    public List<StatusSerieComplemento> getListStatusSerieComp() {
        return listStatusSerieComp;
    }

    public void setListStatusSerieComp(List<StatusSerieComplemento> listStatusSerieComp) {
        this.listStatusSerieComp = listStatusSerieComp;
    }

    public List<EmisoresCFDIS> getListEmisores() {
        return listEmisores;
    }

    public void setListEmisores(List<EmisoresCFDIS> listEmisores) {
        this.listEmisores = listEmisores;
    }

    public SerieComplementoPago getSerieComplementoSelected() {
        return serieComplementoSelected;
    }

    public void setSerieComplementoSelected(SerieComplementoPago serieComplementoSelected) {
        this.serieComplementoSelected = serieComplementoSelected;
    }

    public StatusSerieComplemento getStatusSerieComplemento() {
        return statusSerieComplemento;
    }

    public void setStatusSerieComplemento(StatusSerieComplemento statusSerieComplemento) {
        this.statusSerieComplemento = statusSerieComplemento;
    }

    public EmisoresCFDIS getEmisorSelected() {
        return emisorSelected;
    }

    public void setEmisorSelected(EmisoresCFDIS emisorSelected) {
        this.emisorSelected = emisorSelected;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
    
}
