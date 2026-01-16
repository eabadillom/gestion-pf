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
public class CategoriaEgresoBean extends AbstractCatEgresoBean<CategoriaEgreso, TipoEgreso>{

        private static final long serialVersionUID = 1L;

    @Inject
    private CategoriaEgresoBL bl;

    private static final Logger log = LogManager.getLogger(CategoriaEgresoBean.class);

    @Override
    protected CategoriaEgreso crearNueva() {
        return new CategoriaEgreso();
    }

    @Override
    protected void asignarPadre(CategoriaEgreso entidad) {
        entidad.setTipoEgreso(padre);
    }

    @Override
    protected String guardarConPadre(CategoriaEgreso entidad) throws InventarioException {
        entidad.setTipoEgreso(padre);
        return "Categoria de egreso " + bl.agregarOActualizar(entidad);
    }

    @Override
    protected List<CategoriaEgreso> cargar() throws InventarioException {
        return bl.obtenerCateogiasPorTipoYEstado(padre, estado);
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
