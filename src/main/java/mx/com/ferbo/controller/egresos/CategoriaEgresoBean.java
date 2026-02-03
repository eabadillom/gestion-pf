package mx.com.ferbo.controller.egresos;

import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.catalogos.CategoriaEgresoBL;
import mx.com.ferbo.model.catalogos.CategoriaEgreso;
import mx.com.ferbo.model.catalogos.TipoEgreso;
import mx.com.ferbo.util.InventarioException;

@Named
@ViewScoped
public class CategoriaEgresoBean extends AbstractCatEgresoBean<CategoriaEgreso, TipoEgreso> {

    private static final long serialVersionUID = 1L;

    @Inject
    private CategoriaEgresoBL bl;

    private static final Logger log = LogManager.getLogger(CategoriaEgresoBean.class);

    @Inject
    private CatConceptoEgresoBean bean;

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
    protected List<CategoriaEgreso> cargar() throws InventarioException {
        return bl.obtenerCateogiasPorTipoYEstado(getPadre(), estado);
    }

    @Override
    protected CategoriaEgreso nuevo() {
        return new CategoriaEgreso();
    }

    @Override
    protected String guardar() throws InventarioException {
        if (selected.getTipoEgreso() == null) {
            selected.setTipoEgreso(getPadre());
        }
        return bl.agregarOActualizar(selected);
    }

    @Override
    protected void verificarVigenciaHijos(TipoEgreso tipo) throws InventarioException {
        bl.verificarExistenciaHijos(tipo);
    }

    public void cambiarVigencia(CategoriaEgreso categoria) {
        try {
            bean.verificarVigenciaHijos(categoria);
            nuevoOExistente(categoria);
            cambiarVigenciaSeleccionado();
        } catch (InventarioException ex) {
            logWarn(ex.getMessage(), ex);
            addWarn(ex.getMessage());
        } catch (Exception ex) {
            logError(ex.getMessage(), ex);
            addError("Error inesperado al cambiar vigencia de la categoria de egreso: " + categoria.getNombre()
                    + ". Contacte al admistrador");
        } finally {
            actualizaciones();
        }
    }

    public void preparar(CategoriaEgreso categoria) {
        nuevoOExistente(categoria);
        bean.setEstado(Boolean.TRUE);
        bean.setPadre(selected);
        bean.setTitulo("Concepto de egreso");
        bean.vigentesONoVigentes();
    }

}
