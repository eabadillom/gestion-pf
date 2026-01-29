package mx.com.ferbo.controller.n.catalogos;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.catalogos.TipoDocumentoBL;
import mx.com.ferbo.model.n.catalogos.TipoDocumento;
import mx.com.ferbo.util.InventarioException;

@Named
@RequestScoped
public class TipoDocumentoBean extends AbstractCatalogoBean<TipoDocumento> {

    private static final Logger log = LogManager.getLogger(TipoDocumentoBean.class);

    @Inject
    private TipoDocumentoBL tipoDocumentoBL;

    public TipoDocumentoBean(){

    }

    @PostConstruct
    public void init(){
        titulo = "Tipo de documento";
        initCatalogo();
    }

    @Override
    protected List<TipoDocumento> cargar() throws InventarioException {
        return tipoDocumentoBL.vigentesONoVigentes(estado);
    }

    @Override
    protected String guardar() throws InventarioException {
        return "El tipo de documento se " + tipoDocumentoBL.agregarOActualizar(selected);
    }

    @Override
    protected TipoDocumento nuevo() {
        return new TipoDocumento();
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

    @Override
    protected TipoDocumento createNewSelected() {
        return new TipoDocumento();
    }

}
