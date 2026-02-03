package mx.com.ferbo.controller.catalogos;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.catalogos.TipoAsignacionBL;
import mx.com.ferbo.model.catalogos.TipoAsignacion;
import mx.com.ferbo.util.InventarioException;

@Named
@ViewScoped
public class TipoAsignacionBean extends AbstractCatalogoBean<TipoAsignacion> {

    private static final Logger log = LogManager.getLogger(TipoAsignacionBean.class);

    @Inject
    private TipoAsignacionBL bl;

    public TipoAsignacionBean(){

    }

    @PostConstruct
    public void init() {
        titulo = "Tipo asignación";
        initCatalogo();
    }

    @Override
    protected List<TipoAsignacion> cargar() throws InventarioException {
        return bl.vigentesONoVigentes(estado);
    }

    @Override
    protected String guardar() throws InventarioException {
        return "El tipo de asignación se " + bl.agregarOActualizar(selected);
    }

    @Override
    protected TipoAsignacion nuevo() {
        return new TipoAsignacion();
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
