package mx.com.ferbo.controller.empresa;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;

import org.primefaces.PrimeFaces;

import mx.com.ferbo.model.empresa.Empresa;
import mx.com.ferbo.util.FacesUtils;
import mx.com.ferbo.util.InventarioException;

public abstract class AbstractEmpresaBean<T extends Empresa> implements Serializable {

    protected String titulo;
    protected String mensaje;
    protected Boolean estado = Boolean.TRUE;

    protected List<T> lst;
    protected T selected;

    public void initEmpresa(){
        todos();
    }
    
    /* Inicio de getters y setters heredables */
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public List<T> getLst() {
        return lst;
    }

    public void setLst(List<T> lst) {
        this.lst = lst;
    }

    public T getSelected() {
        if (selected ==  null){
            selected = nuevo();
        }
        return selected;
    }

    public void setSelected(T selected) {
        this.selected = selected;
    }
    /* Fin de getters y setters heredables */

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

    public void todos() {
        try {
            lst = cargar();
            addInfo("Los elementos se cargaron exitosamente.");
        } catch (InventarioException ex) {
            mensaje = ex.getMessage();
            logWarn(mensaje, ex);
            addWarn(mensaje);
        } catch (Exception ex) {
            mensaje = ex.getMessage();
            logError(mensaje, ex);
            addError("Huno un problema al cargar los elementos. Contacte con el administrador.");
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
            logError("Error al operar con el elemento seleccionado", ex);
            addError("Hubo un problema al operar con el elemento seleccionado. Contacte Con el admistrador.");
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
