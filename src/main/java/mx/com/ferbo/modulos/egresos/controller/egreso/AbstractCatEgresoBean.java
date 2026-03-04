package mx.com.ferbo.modulos.egresos.controller.egreso;

import mx.com.ferbo.util.InventarioException;
import mx.com.ferbo.modulos.egresos.controller.categreso.CatEgresoBaseBean;
import mx.com.ferbo.modulos.egresos.model.CatEgreso;

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