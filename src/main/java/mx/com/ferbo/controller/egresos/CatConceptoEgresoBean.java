package mx.com.ferbo.controller.egresos;

import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.catalogos.CatConceptoEgresoBL;
import mx.com.ferbo.model.catalogos.CatConceptoEgreso;
import mx.com.ferbo.model.catalogos.CategoriaEgreso;
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
    protected List<CatConceptoEgreso> cargar() throws InventarioException {
        return bl.obtenerPorCategoriaYVigencia(getPadre(), estado);
    }

    @Override
    protected CatConceptoEgreso nuevo() {
        return new CatConceptoEgreso();
    }

    @Override
    protected String guardar() throws InventarioException {
        if (selected.getCategoriaEgreso() == null) {
            selected.setCategoriaEgreso(getPadre());
        }
        return bl.agregarOActualizar(selected);
    }

    @Override
    protected void verificarVigenciaHijos(CategoriaEgreso entidad) throws InventarioException {
        bl.verificarExistenciaHijos(entidad);
    }
    
    public void cambiarVigenciaSeleccionado(CatConceptoEgreso categoria) {
        titulo = "Conceptos de egreso";
        selected = categoria;
        super.cambiarVigenciaSeleccionado();
    }

}
