
package mx.com.ferbo.modulos.egresos.controller.categreso;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import mx.com.ferbo.modulos.egresos.business.categreso.TipoDevolucionEgresoBL;
import mx.com.ferbo.modulos.egresos.model.catsecundarios.TipoDevolucionEgreso;
import mx.com.ferbo.util.InventarioException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Named
@ApplicationScoped
public class TipoDevolucionEgresoBean extends CatEgresoBaseBean <TipoDevolucionEgreso>{
    
    private static final Logger log = LogManager.getLogger(TipoDevolucionEgresoBean.class);
    
    @Inject
    private TipoDevolucionEgresoBL bl;
    
    public TipoDevolucionEgresoBean() {}

    @Override
    protected TipoDevolucionEgreso nuevo() {
        return new TipoDevolucionEgreso();
    }

    @Override
    protected List<TipoDevolucionEgreso> cargar() throws InventarioException {
        return bl.vigentesONoVigentes(estado);
    }

    @Override
    protected String guardar() throws InventarioException {
        return "el tipo de devolución para el egreso se " + bl.agregarOActualizar(selected) ;
    }

    @Override
    protected void logInfo(String msg) {
        log.info("{}", msg);
    }

    @Override
    protected void logWarn(String msg, Exception ex) {
        log.warn("{}. {}", msg, ex);
    }

    @Override
    protected void logError(String msg, Exception ex) {
        log.error("{}. {}", msg, ex);
    }
    
}
