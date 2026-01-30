package mx.com.ferbo.controller.n.catalogos;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.catalogos.TipoCargoBL;
import mx.com.ferbo.model.n.catalogos.TipoCargo;
import mx.com.ferbo.util.InventarioException;

@Named
@RequestScoped
public class TipoCargoBean extends AbstractCatalogoBean<TipoCargo> {

    private static final Logger log = LogManager.getLogger(TipoCargoBean.class);

    @Inject
    private TipoCargoBL bl;

    public TipoCargoBean(){

    }

    @PostConstruct
    public void init(){
        titulo = "Tipo de cargo";
        initCatalogo();
    }

    @Override
    protected List<TipoCargo> cargar() throws InventarioException {
        return bl.vigentesONoVigentes(estado);
    }

    @Override
    protected String guardar() throws InventarioException {
        return "El tipo de cargo se " + bl.agregarOActualizar(selected);
    }

    @Override
    protected TipoCargo nuevo() {
        return new TipoCargo();
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
