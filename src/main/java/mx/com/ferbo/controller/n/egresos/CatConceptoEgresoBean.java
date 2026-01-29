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
        titulo = "Conceptos de egreso";
        if (selected.getCategoriaEgreso() == null){
            selected.setCategoriaEgreso(padre);
        }
        return bl.agregarOActualizar(selected);
    }

    @Override
    protected List<CatConceptoEgreso> cargar() throws InventarioException {
        return bl.obtenerPorCategoriaYVigencia(padre, estado);
    }

    @Override
    protected void verificarVigenciaHijos(CategoriaEgreso entidad) throws InventarioException {
        bl.verificarExistenciaHijos(entidad);
    }

    @Override
    protected void asignarHijos() throws InventarioException {
       titulo = "Conceptos de egreso";
       lst = bl.obtenerPorCategoriaYVigencia(padre, Boolean.TRUE);
    }

    @Override
    protected CatConceptoEgreso createNewSelected() {
       return new CatConceptoEgreso();
    }
    
    public void cambiarVigenciaSeleccionado(CatConceptoEgreso categoria) {
        titulo = "Conceptos de egreso";
        selected = categoria;
        super.cambiarVigenciaSeleccionado();
    }

    @Override
    public void limpiarSelect() {
        selected = null;
    }
}
