package mx.com.ferbo.controller.catalogos;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.categresos.StatusPagoEgresoBL;
import mx.com.ferbo.model.categresos.StatusPagoEgreso;
import mx.com.ferbo.util.InventarioException;

@Named
@ViewScoped
public class StatusPagoEgresoBean extends CatEgresoBaseBean<StatusPagoEgreso> {

    private static final Logger log = LogManager.getLogger(StatusPagoEgresoBean.class);

    @Inject
    private StatusPagoEgresoBL bl;

    public StatusPagoEgresoBean(){

    }

    @PostConstruct
    public void post(){
        titulo = "Status de Pago";
        initCatalogo();
    }

    @Override
    protected List<StatusPagoEgreso> cargar() throws InventarioException {
        return bl.vigentesONoVigentes(estado);
    }

    @Override
    protected String guardar() throws InventarioException {
        return "El status de pago se " + bl.agregarOActualizar(selected);
    }

    @Override
    protected StatusPagoEgreso nuevo() {
        return new StatusPagoEgreso();
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
