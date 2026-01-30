package mx.com.ferbo.controller.n.egresos;

import mx.com.ferbo.controller.n.catalogos.AbstractCatalogoBean;
import mx.com.ferbo.model.n.catalogos.Catalogo;
import mx.com.ferbo.util.InventarioException;

public abstract class AbstractCatEgresoBean <T extends Catalogo, P extends Catalogo> extends AbstractCatalogoBean<T> {

    private P padre;

    protected P getPadre(){
        return padre;
    }

    protected void setPadre(P padre){
        this.padre = padre;
    }    
    
    protected abstract void verificarVigenciaHijos(P entidad) throws InventarioException;

}