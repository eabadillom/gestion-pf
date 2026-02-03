
package mx.com.ferbo.controller.empresa;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.empresa.NEmisoresCFDISBL;
import mx.com.ferbo.model.empresa.NEmisoresCFDIS;
import mx.com.ferbo.util.InventarioException;

@Named
@ViewScoped
public class NEmisoresCFDISBean extends AbstractEmpresaBean<NEmisoresCFDIS> {
    private static final long serialVersionUID = 1L;

    private static final Logger log = LogManager.getLogger(NEmisoresCFDISBean.class);

    @Inject
    private NEmisoresCFDISBL bl;

    @PostConstruct
    public void init() {
        setTitulo("Emisores");
        super.initEmpresa();
    }

    @Override
    protected NEmisoresCFDIS nuevo() {
        return new NEmisoresCFDIS();
    }

    @Override
    protected List<NEmisoresCFDIS> cargar() throws InventarioException {
        return bl.obtenerTodos();
    }

    @Override
    protected String guardar() throws InventarioException {
        return bl.agregarOActualizar(getSelected());
    }

    @Override
    protected void logInfo(String msg) {
        log.info(msg);
    }

    @Override
    protected void logWarn(String msg, Exception ex) {
        log.warn(msg, ex);
    }

    @Override
    protected void logError(String msg, Exception ex) {
        log.error(msg, ex);
    }

}
