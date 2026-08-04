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
import mx.com.ferbo.model.EmisoresCFDIS;
import mx.com.ferbo.model.SerieComplementoPago;
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
    
    private List<SerieComplementoPago> listSerieComplemento;
    private List<EmisoresCFDIS> listEmisores;

    private Usuario usuario;
    private FacesContext faceContext;
    private HttpServletRequest httpServletRequest;
    
    private SerieComplementoPago serieComplementoSelected;
    private EmisoresCFDIS emisorSelected;
    
    @PostConstruct
    public void init() {
        try {
            faceContext = FacesContext.getCurrentInstance();
            httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
            usuario = (Usuario) httpServletRequest.getSession(false).getAttribute("usuario");

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
    }
    
    public void filtrarSeriesComplementos() {
        FacesMessage message = null;
        FacesMessage.Severity severity = null;
        String mensaje = null;
        String titulo = "Serie complemento de pago";
        try {
            consultarComplementoPago(); 
            
            log.info("El usuario {} a filtrado los complementos de pago...", this.usuario.getUsuario());
            mensaje = "La información se guardó correctamente.";
            severity = FacesMessage.SEVERITY_INFO;
        } catch (Exception ex) {
            log.error("Problema para consultar la lista de serie complementos de pago...", ex);
            mensaje = "Ocurrió un problema al guardar la información.";
            severity = FacesMessage.SEVERITY_ERROR;
        } finally {
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update("form:messages");
        }
    }
    
    public void guardar() {
        FacesMessage message = null;
        FacesMessage.Severity severity = null;
        String mensaje = null;
        String titulo = "Serie complemento de pago";
        try {
            if(serieComplementoSelected == null)
                throw new InventarioException("Ocurrió un problema al guardar la información.");
            
            if(serieComplementoSelected.getId() == null) {
                serieComplementoDAO.guardar(serieComplementoSelected);
                log.info("El usuario {} a guardado un complemento de pago...", this.usuario.getUsuario());
                mensaje = "La información se guardó correctamente.";
            } else {
                serieComplementoDAO.actualizar(serieComplementoSelected);
                log.info("El usuario {} a actualizado un complemento de pago...", this.usuario.getUsuario());
                mensaje = "La información se actualizo correctamente.";
            }
            
            consultarComplementoPago();
            severity = FacesMessage.SEVERITY_INFO;
        } catch (InventarioException ex) {
            mensaje = ex.getMessage();
            severity = FacesMessage.SEVERITY_WARN;
        } catch (Exception ex) {
            log.error("Problema para guardar la información de la serie complemento de pago...", ex);
            mensaje = "Ocurrió un problema al guardar la información.";
            severity = FacesMessage.SEVERITY_ERROR;
        } finally {
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update("form:messages");
        }
    }
    
    public void eliminar() {
        FacesMessage message = null;
        FacesMessage.Severity severity = null;
        String mensaje = null;
        String titulo = "Serie complemento de pago";
        try {
            if(serieComplementoSelected == null)
                throw new InventarioException("Ocurrió un problema al guardar la información.");
            
            serieComplementoDAO.eliminar(serieComplementoSelected);
            
            consultarComplementoPago();
            log.info("El usuario {} a eliminado un complemento de pago...", this.usuario.getUsuario());
            mensaje = "La información se elimino correctamente.";
            severity = FacesMessage.SEVERITY_INFO;
        } catch (InventarioException ex) {
            mensaje = ex.getMessage();
            severity = FacesMessage.SEVERITY_WARN;
        } catch (Exception ex) {
            log.error("Problema para actualizar la información de la serie complemento de pago...", ex);
            mensaje = "Ocurrió un problema al guardar la información.";
            severity = FacesMessage.SEVERITY_ERROR;
        } finally {
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update("form:messages");
        }
    }

    public List<SerieComplementoPago> getListSerieComplemento() {
        return listSerieComplemento;
    }

    public void setListSerieComplemento(List<SerieComplementoPago> listSerieComplemento) {
        this.listSerieComplemento = listSerieComplemento;
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

    public EmisoresCFDIS getEmisorSelected() {
        return emisorSelected;
    }

    public void setEmisorSelected(EmisoresCFDIS emisorSelected) {
        this.emisorSelected = emisorSelected;
    }
    
}
