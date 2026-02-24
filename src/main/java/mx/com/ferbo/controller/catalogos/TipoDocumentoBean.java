package mx.com.ferbo.controller.catalogos;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.categresos.TipoDocumentoEgresoBL;
import mx.com.ferbo.model.categresos.TipoDocumentoEgreso;
import mx.com.ferbo.util.InventarioException;

@Named
@ViewScoped
public class TipoDocumentoBean extends CatEgresoBaseBean<TipoDocumentoEgreso> {

    private static final Logger log = LogManager.getLogger(TipoDocumentoBean.class);

    @Inject
    private TipoDocumentoEgresoBL bl;

    public TipoDocumentoBean(){

    }

    @PostConstruct
    public void init(){
        titulo = "Tipo de documento";
        initCatalogo();
    }

    @Override
    protected List<TipoDocumentoEgreso> cargar() throws InventarioException {
        return bl.vigentesONoVigentes(estado);
    }

    @Override
    protected String guardar() throws InventarioException {
        return "El tipo de documento se " + bl.agregarOActualizar(selected);
    }

    @Override
    protected TipoDocumentoEgreso nuevo() {
        return new TipoDocumentoEgreso();
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
