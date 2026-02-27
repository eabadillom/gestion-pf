
package mx.com.ferbo.controller.categresos;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import mx.com.ferbo.business.categresos.StatusDevolucionEgresoBL;
import mx.com.ferbo.model.categresos.StatusDevolucionEgreso;
import mx.com.ferbo.util.InventarioException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Named
@ApplicationScoped
public class StatusDevolucionEgresoBean extends CatEgresoBaseBean <StatusDevolucionEgreso>{
    
    private static final Logger log = LogManager.getLogger(StatusDevolucionEgresoBean.class);
    
    @Inject
    private StatusDevolucionEgresoBL bl;
    
    public StatusDevolucionEgresoBean() {
    }

    @Override
    protected StatusDevolucionEgreso nuevo() {
        return new StatusDevolucionEgreso();
    }

    @Override
    protected List<StatusDevolucionEgreso> cargar() throws InventarioException {
        return bl.vigentesONoVigentes(estado);
    }

    @Override
    protected String guardar() throws InventarioException {
        return "El status devolución de egreso " + bl.agregarOActualizar(selected);
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
