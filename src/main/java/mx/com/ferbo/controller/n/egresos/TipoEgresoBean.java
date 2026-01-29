package mx.com.ferbo.controller.n.egresos;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.catalogos.TipoEgresoBL;
import mx.com.ferbo.controller.n.catalogos.AbstractCatalogoBean;
import mx.com.ferbo.model.n.catalogos.TipoEgreso;
import mx.com.ferbo.util.InventarioException;

@Named
@ViewScoped
public class TipoEgresoBean extends AbstractCatalogoBean<TipoEgreso> {

    private static final long serialVersionUID = 1L;

    private static final Logger log = LogManager.getLogger(TipoEgreso.class);

    @Inject
    private TipoEgresoBL bl;

    @Inject
    private CategoriaEgresoBean categoriaBean;

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
            categoriaBean.verificarVigenciaHijos(tipo);
            super.nuevoOExistente(tipo);
            super.cambiarVigenciaSeleccionado();
        } catch (InventarioException ex) {
            logWarn(ex.getMessage(), ex);
            addWarn(ex.getMessage());
        } catch (Exception ex) {
            logError(ex.getMessage(), ex);
            addError("Error inesperado al cambiar vigencia del tipo de egreso: " + tipo.getNombre() + ". Contacte al admistrador");
        } finally {
            actualizaciones();
        }
    }

    public void preparar(TipoEgreso tipo) {
        try {
            super.nuevoOExistente(tipo);
            categoriaBean.setEstado(Boolean.TRUE);
            categoriaBean.setPadre(selected);
            categoriaBean.asignarHijos();
        } catch (InventarioException ex) {
            logWarn(ex.getMessage(), ex);
            addWarn(ex.getMessage());
        } catch (Exception ex) {
            logError(ex.getMessage(), ex);
            addError("Error inesperado al cargar las categorias del tipo de egreso: " + tipo.getNombre() + ". Conctacte al administrador.");
        } finally {
            actualizaciones();
        }
    }

    @Override
    protected TipoEgreso createNewSelected() {
        return new TipoEgreso();
    }

    @Override
    public void limpiarSelect() {
        selected = null;
    }
}
