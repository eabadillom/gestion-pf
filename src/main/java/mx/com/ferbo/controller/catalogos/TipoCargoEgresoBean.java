package mx.com.ferbo.controller.catalogos;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.categresos.TipoCargoEgresoBL;
import mx.com.ferbo.model.categresos.TipoCargoEgreso;
import mx.com.ferbo.util.InventarioException;

@Named
@ViewScoped
public class TipoCargoEgresoBean extends CatEgresoBaseBean<TipoCargoEgreso> {

    private static final Logger log = LogManager.getLogger(TipoCargoEgresoBean.class);

    @Inject
    private TipoCargoEgresoBL bl;

    public TipoCargoEgresoBean(){

    }

    @PostConstruct
    public void init(){
        titulo = "Tipo de cargo";
        initCatalogo();
    }

    @Override
    protected List<TipoCargoEgreso> cargar() throws InventarioException {
        return bl.vigentesONoVigentes(estado);
    }

    @Override
    protected String guardar() throws InventarioException {
        return "El tipo de cargo se " + bl.agregarOActualizar(selected);
    }

    @Override
    protected TipoCargoEgreso nuevo() {
        return new TipoCargoEgreso();
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
