package mx.com.ferbo.egresos.controller.catalogos;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import org.primefaces.PrimeFaces;

import mx.com.ferbo.egresos.business.catalogos.CatalogoBL;
import mx.com.ferbo.model.Usuario;

public abstract class AbstractCatalogoBean<T> implements Serializable {

    protected List<T> items;
    protected T selected;
    protected Boolean mostrarInactivos = Boolean.TRUE;

    protected String titulo;
    protected String inicioLeyenda;
    protected Usuario usuario;

    protected abstract CatalogoBL<T> getBL();

    protected abstract T crearNuevo();
    protected abstract void cargaInicial();

    @PostConstruct
    public void init() {
        cargaInicial();
    }

    public void buscar() {
        items = getBL().buscarActivos(mostrarInactivos);
    }

    public void toggleActivos() {
        mostrarInactivos = !mostrarInactivos;
        buscar();
    }

    public void nuevoOExistente(T entidad) {
        selected = (entidad == null) ? crearNuevo() : entidad;
    }

    protected void actualizarMensajes() {
        PrimeFaces.current().ajax().update("form:messages");
    }

    protected void actualizarTabla() {
        PrimeFaces.current().ajax().update("form:tabla");
    }

    public void seleccionar(T item) {
        this.selected = item;
    }

    public void limpiarSeleccion() {
        this.selected = null;
    }

    // Getters y Setters
    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public T getSelected() {
        return selected;
    }

    public void setSelected(T selected) {
        this.selected = selected;
    }

    public boolean isMostrarInactivos() {
        return mostrarInactivos;
    }

    public void setMostrarInactivos(Boolean mostrarInactivos) {
        this.mostrarInactivos = mostrarInactivos;
    }

}
