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
public class CatConceptoEgresoBean
        extends AbstractCatEgresoBean<CatConceptoEgreso, CategoriaEgreso> {

        private static final long serialVersionUID = 1L;

    @Inject
    private CatConceptoEgresoBL bl;

    private static final Logger log = LogManager.getLogger(CatConceptoEgresoBean.class);

    protected CatConceptoEgreso crearNueva() {
        return new CatConceptoEgreso();
    }

    protected void asignarPadre(CatConceptoEgreso entidad) {
        entidad.setCategoriaEgreso(padre);
    }

    protected String guardarConPadre(CatConceptoEgreso entidad) throws InventarioException {
        entidad.setCategoriaEgreso(padre);
        return "Catalogo de concepto de egreso " + bl.agregarOActualizar(entidad);
    }

    @Override
    protected List<CatConceptoEgreso> cargar() throws InventarioException {
        return bl.obtenerPorCategoriaYVigencia(padre, estado);
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
    protected void cargarConPadre(CategoriaEgreso entidad) throws InventarioException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    protected void guardarConPadre(CategoriaEgreso entidad) throws InventarioException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    protected String guardar() throws InventarioException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    protected CatConceptoEgreso nuevo() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
