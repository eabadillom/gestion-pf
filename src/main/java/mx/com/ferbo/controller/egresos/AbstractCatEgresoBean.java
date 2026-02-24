package mx.com.ferbo.controller.egresos;

import mx.com.ferbo.controller.catalogos.CatEgresoBaseBean;
import mx.com.ferbo.util.InventarioException;
import mx.com.ferbo.model.categresos.CatEgreso;

public abstract class AbstractCatEgresoBean <T extends CatEgreso, P extends CatEgreso> extends CatEgresoBaseBean<T> {

    private P padre;

    protected P getPadre(){
        return padre;
    }

    protected void setPadre(P padre){
        this.padre = padre;
    }    
    
    protected abstract void verificarVigenciaHijos(P entidad) throws InventarioException;

}