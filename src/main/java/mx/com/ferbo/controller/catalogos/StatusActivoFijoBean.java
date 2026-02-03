package mx.com.ferbo.controller.catalogos;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.catalogos.StatusActivoFijoBL;
import mx.com.ferbo.model.catalogos.StatusActivoFijo;
import mx.com.ferbo.util.InventarioException;

@Named
@ViewScoped
public class StatusActivoFijoBean extends AbstractCatalogoBean<StatusActivoFijo>{

    private static final Logger log = LogManager.getLogger(StatusActivoFijoBean.class);

    @Inject
    private StatusActivoFijoBL bl;

    public StatusActivoFijoBean(){

    }

    @PostConstruct
    public void init() {
        titulo = "Status Activo Fijo";
        initCatalogo();
    }


    @Override
    protected List<StatusActivoFijo> cargar() throws InventarioException {
        return bl.vigentesONoVigentes(estado);
    }


    @Override
    protected String guardar() throws InventarioException {
        return "El status de activo fijo se " + bl.agregarOActualizar(selected);
    }


    @Override
    protected StatusActivoFijo nuevo() {
        return new StatusActivoFijo();
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
