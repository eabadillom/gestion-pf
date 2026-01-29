package mx.com.ferbo.controller.n.egresos;

import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.catalogos.CategoriaEgresoBL;
import mx.com.ferbo.model.n.catalogos.CategoriaEgreso;
import mx.com.ferbo.model.n.catalogos.TipoEgreso;
import mx.com.ferbo.util.InventarioException;

@Named
@ViewScoped
public class CategoriaEgresoBean extends AbstractCatEgresoBean<CategoriaEgreso, TipoEgreso> {

    private static final long serialVersionUID = 1L;

    @Inject
    private CategoriaEgresoBL bl;

    private static final Logger log = LogManager.getLogger(CategoriaEgresoBean.class);

    @Inject
    private CatConceptoEgresoBean conceptoBean;

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
        return bl.obtenerCateogiasPorTipoYEstado(padre, estado);
    }

    @Override
    protected CategoriaEgreso crearNueva() {
        return new CategoriaEgreso();
    }

    @Override
    protected void asignarPadre() {
        selected.setTipoEgreso(padre);
    }

    @Override
    protected String guardarConPadre() throws InventarioException {
        if (selected.getTipoEgreso() == null){
            selected.setTipoEgreso(padre);
        }
        return bl.agregarOActualizar(selected);
    }

    @Override
    protected void verificarVigenciaHijos(TipoEgreso tipo) throws InventarioException {
        bl.verificarExistenciaHijos(tipo);
    }

    @Override
    protected void asignarHijos() throws InventarioException {
        lst = bl.obtenerCateogiasPorTipoYEstado(padre, true);
    }

    public void cambiarVigencia(CategoriaEgreso categoria) {
        try {
            titulo = "Categoria de Egreso";
            conceptoBean.verificarVigenciaHijos(categoria);
            super.nuevoOExistente(categoria);
            super.cambiarVigenciaSeleccionado();
        } catch (InventarioException ex) {
            logWarn(ex.getMessage(), ex);
            addWarn(ex.getMessage());
        } catch (Exception ex) {
            logError(ex.getMessage(), ex);
            addError("Error inesperado al cambiar vigencia de la categoria de egreso: " + categoria.getNombre() + ". Contacte al admistrador");
        } finally {
            actualizaciones();
        }
    }

    public void preparar(CategoriaEgreso categoria) {
        try {
            titulo = "Categoria de Egreso";
            super.nuevoOExistente(categoria);
            conceptoBean.setEstado(Boolean.TRUE);
            conceptoBean.setPadre(selected);
            conceptoBean.asignarHijos();
        } catch (InventarioException ex) {
            logWarn(ex.getMessage(), ex);
            addWarn(ex.getMessage());
        } catch (Exception ex) {
            logError(ex.getMessage(), ex);
            addError("Error inesperado al obtener los conceptos asociados con la categoria: " + categoria.getNombre() + ". Contacte con el admistrador.");
        } finally {
            actualizaciones();
        }
    }

    @Override
    protected CategoriaEgreso createNewSelected() {
        return new CategoriaEgreso();
    }

    @Override
    public void limpiarSelect() {
        selected = null;
    }
}
