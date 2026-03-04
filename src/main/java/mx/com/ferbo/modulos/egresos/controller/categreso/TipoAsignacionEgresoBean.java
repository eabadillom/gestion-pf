package mx.com.ferbo.modulos.egresos.controller.categreso;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.modulos.egresos.business.categreso.TipoAsignacionEgresoBL;
import mx.com.ferbo.modulos.egresos.model.catsecundarios.TipoAsignacionEgreso;
import mx.com.ferbo.util.InventarioException;

@Named
@ViewScoped
public class TipoAsignacionEgresoBean extends CatEgresoBaseBean<TipoAsignacionEgreso> {

    private static final Logger log = LogManager.getLogger(TipoAsignacionEgresoBean.class);

    @Inject
    private TipoAsignacionEgresoBL bl;

    public TipoAsignacionEgresoBean(){

    }

    @PostConstruct
    public void init() {
        titulo = "Tipo asignación";
        initCatalogo();
    }

    @Override
    protected List<TipoAsignacionEgreso> cargar() throws InventarioException {
        return bl.vigentesONoVigentes(estado);
    }

    @Override
    protected String guardar() throws InventarioException {
        return "El tipo de asignación se " + bl.agregarOActualizar(selected);
    }

    @Override
    protected TipoAsignacionEgreso nuevo() {
        return new TipoAsignacionEgreso();
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
