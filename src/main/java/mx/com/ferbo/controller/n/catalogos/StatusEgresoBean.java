
package mx.com.ferbo.controller.n.catalogos;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import mx.com.ferbo.business.catalogos.StatusEgresoBL;
import mx.com.ferbo.model.n.catalogos.StatusEgreso;
import mx.com.ferbo.util.InventarioException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Named
@ViewScoped
public class StatusEgresoBean extends AbstractCatalogoBean<StatusEgreso> {
    
    private static final Logger log = LogManager.getLogger(StatusEgresoBean.class);
    
    @Inject 
    private StatusEgresoBL bl;
    
    public StatusEgresoBean() {
        
    }
    
     @PostConstruct
    public void init() {
        titulo = "Status Egreso";
        initCatalogo();
    }

    @Override
    protected List<StatusEgreso> cargar() throws InventarioException {
        return bl.vigentesONoVigentes(estado);
    }

    @Override
    protected String guardar() throws InventarioException {
       return "Status de egreso se " + bl.agregarOActualizar(selected);
    }

    @Override
    protected StatusEgreso nuevo() {
        return new StatusEgreso();
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
