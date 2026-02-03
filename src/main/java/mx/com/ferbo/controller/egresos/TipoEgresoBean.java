package mx.com.ferbo.controller.egresos;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.catalogos.TipoEgresoBL;
import mx.com.ferbo.controller.catalogos.AbstractCatalogoBean;
import mx.com.ferbo.model.catalogos.TipoEgreso;
import mx.com.ferbo.util.InventarioException;

@Named
@ViewScoped
public class TipoEgresoBean extends AbstractCatalogoBean<TipoEgreso> {

    private static final long serialVersionUID = 1L;

    private static final Logger log = LogManager.getLogger(TipoEgreso.class);

    @Inject
    private TipoEgresoBL bl;

    @Inject
    private CategoriaEgresoBean bean;

    @PostConstruct
    public void init() {
        titulo = "Tipo de Egreso";
        super.vigentesONoVigentes();
        initCatalogo();
    }

    @Override
    protected List<TipoEgreso> cargar() throws InventarioException {
        return bl.vigentesONoVigentes(estado);
    }

    @Override
    protected String guardar() throws InventarioException {
        return "El tipo de egreso " + bl.agregarOActualizar(selected);
    }

    @Override
    protected TipoEgreso nuevo() {
        return new TipoEgreso();
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

    public void cambiarVigencia(TipoEgreso tipo) {
        try {
            bean.verificarVigenciaHijos(tipo);
            nuevoOExistente(tipo);
            cambiarVigenciaSeleccionado();
        } catch (InventarioException ex) {
            logWarn(ex.getMessage(), ex);
            addWarn(ex.getMessage());
        } catch (Exception ex) {
            logError(ex.getMessage(), ex);
            addError("Error inesperado al cambiar vigencia del tipo de egreso: " + tipo.getNombre()
                    + ". Contacte al admistrador");
        } finally {
            actualizaciones();
        }
    }

    public void preparar(TipoEgreso tipo) {
        nuevoOExistente(tipo);
        bean.setEstado(Boolean.TRUE);
        bean.setPadre(selected);
        bean.setTitulo("Categoria de egreso");
        bean.vigentesONoVigentes();
    }

}
