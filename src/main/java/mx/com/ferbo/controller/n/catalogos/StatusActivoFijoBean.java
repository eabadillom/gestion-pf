package mx.com.ferbo.controller.n.catalogos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

import mx.com.ferbo.business.catalogos.StatusActivoFijoBL;
import mx.com.ferbo.model.n.catalogos.StatusActivoFijo;
import mx.com.ferbo.util.FacesUtils;
import mx.com.ferbo.util.InventarioException;

@Named
@ViewScoped
public class StatusActivoFijoBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger log = LogManager.getLogger(StatusActivoFijoBean.class);

    @Inject
    private StatusActivoFijoBL statusActivoFijoBL;

    private List<StatusActivoFijo> lstStatus;
    private StatusActivoFijo statusSelected;
    private String title;
    private String mensaje;

    @PostConstruct
    public void init() {
        title = "Status Activo Fijo";
        try {
            lstStatus = statusActivoFijoBL.getVigentesONoVigentes(true);
        } catch (InventarioException ex) {
            log.warn("Error al cargar status activo fijo", ex);
            lstStatus = new ArrayList<>(); // estado seguro
        } catch (Exception ex) {
            log.error("Error inesperado al cargar status activo fijo", ex);
            lstStatus = new ArrayList<>();
        }
    }

    public void nuevoOExistemte(StatusActivoFijo status) {
        if (status != null) {
            statusSelected = status;
        } else {
            statusSelected = new StatusActivoFijo();
        }
    }

    public void vigentesONoVigentes(boolean nuevoEstado) {
        try {
            lstStatus = statusActivoFijoBL.getVigentesONoVigentes(nuevoEstado);
            mensaje = "La lista de status se cargo exitosamente.";
            FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, title, mensaje);
        } catch (InventarioException ex) {
            log.warn(ex);
            mensaje = ex.getMessage();
            FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, title, mensaje);
        } catch (Exception ex) {
            log.error("Error desconocido: {}", ex);
            mensaje = "Hubo un problema desconocido al obtenr los status. Contacte al administrador del sistema.";
            FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, title, mensaje);
        } finally {
            PrimeFaces.current().ajax().update("form:messages");
        }
    }

    public void operarStustus() {
        mensaje = "El status de activo fijo se ";
        try {
            mensaje += statusActivoFijoBL.agregarOActualizar(statusSelected);
            FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, title, mensaje);
        } catch (InventarioException ex) {
            log.warn(ex.getMessage());
            mensaje = ex.getMessage();
            FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, title, mensaje);
        } catch (Exception ex) {
            mensaje = "Hubo un problema al operar con el status. Contacte con el administrador.";
            FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, title, mensaje);
        } finally {
            PrimeFaces.current().ajax().update("form:messages");
        }
    }

    public List<StatusActivoFijo> getLstStatus() {
        return lstStatus;
    }

    public void setLstStatus(List<StatusActivoFijo> lstStatus) {
        this.lstStatus = lstStatus;
    }

    public StatusActivoFijo getStatusSelected() {
        return statusSelected;
    }

    public void setStatusSelected(StatusActivoFijo statusSelected) {
        this.statusSelected = statusSelected;
    }

}
