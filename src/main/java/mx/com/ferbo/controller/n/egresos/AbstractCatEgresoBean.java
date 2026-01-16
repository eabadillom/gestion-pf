package mx.com.ferbo.controller.n.egresos;

import mx.com.ferbo.controller.n.catalogos.AbstractCatalogoBean;
import mx.com.ferbo.model.n.catalogos.Catalogo;
import mx.com.ferbo.util.InventarioException;

public abstract class AbstractCatEgresoBean <T extends Catalogo, P extends Catalogo> extends AbstractCatalogoBean<T> {

    protected P padre;

    public P getPadre(){
        return padre;
    }

    public void setPadre(P padre){
        this.padre = padre;
    }

    @Override
    protected T nuevo(){
        T entidad = crearNueva();
        asignarPadre(entidad);
        return entidad;
    }

    @Override
    protected String guardar() throws InventarioException {
        asignarPadre(selected);
        return guardarConPadre(selected);
    }
    
    protected abstract T crearNueva();
    protected abstract void asignarPadre(T entidad);
    protected abstract String guardarConPadre(T entidad) throws InventarioException;
}
