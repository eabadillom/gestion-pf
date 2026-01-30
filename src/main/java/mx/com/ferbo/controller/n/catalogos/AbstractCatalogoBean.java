package mx.com.ferbo.controller.n.catalogos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;

import javax.faces.application.FacesMessage;

import org.primefaces.PrimeFaces;

import mx.com.ferbo.model.n.catalogos.Catalogo;
import mx.com.ferbo.util.FacesUtils;
import mx.com.ferbo.util.InventarioException;

public abstract class AbstractCatalogoBean<T extends Catalogo> implements Serializable {

    protected String titulo;
    protected String mensaje;
    protected Boolean estado = Boolean.TRUE;

    protected List<T> lst;
    protected T selected;
    
    public void initCatalogo() {
        vigentesONoVigentes();
    }

    /* Inicia Getters y Setters */
    public List<T> getLst() {
        return lst;
    }

    public void setLst(List<T> lst) {
        this.lst = lst;
    }

    public T getSelected() {
        if (selected == null) {
            selected = nuevo();
        }
        return selected;
    }

    public void setSelected(T selected) {
        this.selected = selected;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /* Finaliza Getters y Setters */

    /* Incian métodos abstractos */
    protected abstract T nuevo();

    protected abstract List<T> cargar() throws InventarioException;

    protected abstract String guardar() throws InventarioException;

    protected abstract void logInfo(String msg);

    protected abstract void logWarn(String msg, Exception ex);

    protected abstract void logError(String msg, Exception ex);
    /* Finalizan métodos abstractos */

    /* Inician métodos reutilizables */

    public void nuevoOExistente(T entity) {
        selected = (entity != null) ? entity : nuevo();
    }

    public void vigentesONoVigentes() {
        try {
            lst = cargar();
            addInfo("La lista se cargó exitosamente.");
        } catch (InventarioException ex) {
            lst = new ArrayList<>();
            addWarn(ex.getMessage());
        } catch (Exception ex) {
            lst = new ArrayList<>();
            logError("Error desconocido al obtener la información.", ex);
            addError("Hubo un problema al obtener la información. Contacte con el admistrador.");
        } finally {
            actualizaciones();
        }
    }

    public void operar() {
        try {
            mensaje = guardar();
            addInfo(mensaje);
        } catch (InventarioException ex) {
            logWarn(ex.getMessage(), ex);
            addWarn(ex.getMessage());
        } catch (Exception ex) {
            logError("Error al operar con el catálogo", ex);
            addError("Hubo un problema al operar con el catálogo. Contacte Con el admistrador.");
        } finally {
            actualizaciones();
        }
    }

    public void cambiarVigenciaSeleccionado() {
        selected.setVigente(!selected.getVigente());
        try {
            guardar();
            addInfo(selected.getVigente() ? "Elemento habilitado" : "Elemento deshabilitado");
            vigentesONoVigentes();
        } catch (InventarioException ex) {
            logWarn(ex.getMessage(), ex);
            addWarn(ex.getMessage());
        } catch (Exception ex) {
            logError("Error al cambiar el estado", ex);
            addError("Hubo un problema al cambiar el estado. Contacte Con el admistrador.");
        } finally {
            actualizaciones();
        }
    }

    public void actualizaciones() {
        PrimeFaces.current().ajax().update("form:messages");
    }

    protected void addInfo(String msg) {
        FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, titulo, msg);
    }

    protected void addWarn(String msg) {
        FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, titulo, msg);
    }

    protected void addError(String msg) {
        FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, titulo, msg);
    }

    /* Finalizan métodos reutilizables */
}
