package mx.com.ferbo.controller.n.catalogos;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.catalogos.StatusPagoBL;
import mx.com.ferbo.model.n.catalogos.StatusPago;
import mx.com.ferbo.util.InventarioException;

@Named
@RequestScoped
public class StatusPagoBean extends AbstractCatalogoBean<StatusPago> {

    private static final Logger log = LogManager.getLogger(StatusPagoBean.class);

    @Inject
    private StatusPagoBL bl;

    public StatusPagoBean(){

    }

    @PostConstruct
    public void post(){
        titulo = "Status de Pago";
        initCatalogo();
    }

    @Override
    protected List<StatusPago> cargar() throws InventarioException {
        return bl.vigentesONoVigentes(estado);
    }

    @Override
    protected String guardar() throws InventarioException {
        return "El status de pago se " + bl.agregarOActualizar(selected);
    }

    @Override
    protected StatusPago nuevo() {
        return new StatusPago();
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
