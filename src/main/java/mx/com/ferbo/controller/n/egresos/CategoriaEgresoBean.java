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
    public void cargarHijos(TipoEgreso tipo) {
        try {
            titulo = "Categorias";
            setPadre((tipo == null) ? new TipoEgreso() : tipo);
            lst = cargar();
            addInfo("Las categorias se cargarón exitosamente");
        } catch (InventarioException ex) {
            logWarn(ex.getMessage(), ex);
            addWarn("Hubo un problema al buscar las categorias asociadas al tipo de egreso.");
        } catch (Exception ex) {
            logError(ex.getMessage(), ex);
            addError("Error desconocido al buscar las categorias. Contacte con el administrador.");
        } finally {
            super.actualizaciones();
        }
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
        selected.setTipoEgreso(padre);
        return bl.agregarOActualizar(selected);
    }

    @Override
    public void verificarVigenciaHijos(TipoEgreso tipo) throws InventarioException {

        List<CategoriaEgreso> categorias
                = bl.obtenerCateogiasPorTipoYEstado(tipo, true);

        if (!categorias.isEmpty()) {
            throw new InventarioException(
                    "No se puede cancelar el tipo de egreso por tener categorias vigentes."
            );
        }
    }
}
