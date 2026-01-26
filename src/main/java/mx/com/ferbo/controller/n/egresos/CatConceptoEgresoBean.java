package mx.com.ferbo.controller.n.egresos;

import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.catalogos.CatConceptoEgresoBL;
import mx.com.ferbo.model.n.catalogos.CatConceptoEgreso;
import mx.com.ferbo.model.n.catalogos.CategoriaEgreso;
import mx.com.ferbo.util.InventarioException;

@Named
@ViewScoped
public class CatConceptoEgresoBean extends AbstractCatEgresoBean<CatConceptoEgreso, CategoriaEgreso> {

    private static final long serialVersionUID = 1L;

    @Inject
    private CatConceptoEgresoBL bl;

    private static final Logger log = LogManager.getLogger(CatConceptoEgresoBean.class);

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
    protected CatConceptoEgreso crearNueva() {
        return new CatConceptoEgreso();
    }

    @Override
    protected void asignarPadre() {
        selected.setCategoriaEgreso(padre);
    }

    @Override
    protected String guardarConPadre() throws InventarioException {
        selected.setCategoriaEgreso(padre);
        return bl.agregarOActualizar(selected);
    }

    @Override
    protected List<CatConceptoEgreso> cargar() throws InventarioException {
        return bl.obtenerPorCategoriaYVigencia(padre, estado);
    }

    @Override
    public void cargarHijos(CategoriaEgreso categoria) {
        try {
            titulo = "gresos";
            setPadre((categoria == null) ? new CategoriaEgreso() : categoria);
            lst = cargar();
            addInfo("Los conceptos se cargaron de forma exitosa.");
        } catch (InventarioException ex) {
            logWarn(ex.getMessage(), ex);
            addWarn(ex.getMessage());
        } catch (Exception ex) {
            logError(ex.getMessage(), ex);
            addError(ex.getMessage());
        } finally {
            actualizaciones();
        }
    }

    @Override
    protected void verificarVigenciaHijos(CategoriaEgreso categoria) {
        titulo = "Categoria";
        try {
            List<CatConceptoEgreso> conceptos = bl.obtenerPorCategoriaYVigencia(padre, true);
            if (conceptos.size() > 0) {
                throw new InventarioException("No se puede cancelar la categoria de egreso por tener conceptos vigentes.");
            }
            addInfo("Se procede a cambiar a no vigente.");
        } catch (InventarioException ex) {
            logWarn(ex.getMessage(), ex);
            addWarn(ex.getMessage());
        } catch (Exception ex){
            logError(ex.getMessage(), ex);
            addError("Error desconocido al cambiar la vigencia de la categoria de egreso: " + categoria.getNombre() );
        } finally {
            actualizaciones();
        }
    }
}
