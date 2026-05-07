package mx.com.ferbo.egresos.controller.catalogos;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;

import com.ferbo.tools.exception.BusinessException;

import mx.com.ferbo.egresos.business.catalogos.CatalogoBL;
import mx.com.ferbo.util.InventarioException;

public abstract class AbstractCatalogoBean<T> implements Serializable {

    protected List<T> items;
    protected T selected;
    protected Boolean mostrarInactivos = Boolean.FALSE;

    protected abstract CatalogoBL<T> getBL();

    protected abstract T crearNuevo();

    @PostConstruct
    public void init() {
        buscar();
    }

    public void buscar() {
        try {
            items = getBL().buscarActivos(mostrarInactivos ? null : Boolean.TRUE);
        } catch (InventarioException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void toggleActivos() {
        mostrarInactivos = !mostrarInactivos;
        buscar();
    }

    public void nuevoOExistente(T entidad) {
        selected = (entidad == null) ? crearNuevo() : entidad;
    }

    public void guardar() {
        try {
            getBL().guardar(selected);
            // Mensaje para PrimeFaces
            buscar();
            selected = null;
        } catch (BusinessException | InventarioException ex) {
            // Mensaje para PrimeFaces
        }
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
