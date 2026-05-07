package mx.com.ferbo.egresos.controller.catalogos;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import mx.com.ferbo.egresos.business.catalogos.CatalogoBL;
import mx.com.ferbo.egresos.business.catalogos.CategoriaEgresoBL;
import mx.com.ferbo.egresos.model.calogos.CategoriaEgreso;

@Named
@ViewScoped
public class CategoriaEgresoBean extends AbstractCatalogoBean<CategoriaEgreso> {
 
    @Inject
    private CategoriaEgresoBL bl;
    
    public CategoriaEgresoBean() {
    }

    @Override
    protected CatalogoBL<CategoriaEgreso> getBL() {
        return bl;
    }

    @Override
    protected CategoriaEgreso crearNuevo() {
        return new CategoriaEgreso();
    }

    
}
