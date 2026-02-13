
package mx.com.ferbo.controller.catalogos;

import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import mx.com.ferbo.business.catalogos.StatusCargoEgresoBL;
import mx.com.ferbo.model.catalogos.StatusCargoEgreso;
import mx.com.ferbo.util.InventarioException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Named
@ViewScoped
public class StatusCargoEgresoBean extends AbstractCatalogoBean <StatusCargoEgreso>{
    
    private static final Logger log = LogManager.getLogger(StatusCargoEgresoBean.class);
    
    private StatusCargoEgresoBL bl;
    
    public StatusCargoEgresoBean(){
    }
    
    @Inject
    public void init(){
        titulo = "Status Cargo Egreso";
        initCatalogo();
    }

    @Override
    protected StatusCargoEgreso nuevo() {
        return new StatusCargoEgreso();
    }

    @Override
    protected List<StatusCargoEgreso> cargar() throws InventarioException {
        return bl.vigentesONoVigentes(estado);
    }

    @Override
    protected String guardar() throws InventarioException {
        return bl.agregarOActualizar(selected);
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
