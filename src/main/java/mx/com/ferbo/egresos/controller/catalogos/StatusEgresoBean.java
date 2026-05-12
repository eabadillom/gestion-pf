package mx.com.ferbo.egresos.controller.catalogos;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import mx.com.ferbo.egresos.business.catalogos.CatalogoBL;
import mx.com.ferbo.egresos.business.catalogos.StatusEgresoBL;
import mx.com.ferbo.egresos.model.calogos.StatusEgreso;

@Named 
@ViewScoped
public class StatusEgresoBean extends AbstractCatalogoBean<StatusEgreso> {

    @Inject
    private StatusEgresoBL bl;
        
    public StatusEgresoBean() {
    }

    @Override
    protected CatalogoBL<StatusEgreso> getBL() {
        return bl;
    }

    @Override
    protected StatusEgreso crearNuevo() {
        return new StatusEgreso();
    }
    
}
