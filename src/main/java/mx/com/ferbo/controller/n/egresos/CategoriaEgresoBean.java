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

    protected CategoriaEgreso crearNueva() {
        return new CategoriaEgreso();
    }

    protected void asignarPadre(CategoriaEgreso entidad) {
        entidad.setTipoEgreso(padre);
    }

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

    public void cargarCategorias(TipoEgreso padre){
        try{
            TipoEgreso father = (padre == null) ? new TipoEgreso() : padre;
            setPadre(father);
            lst = cargar();addInfo("Las categorias se cargarón exitosamente");
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
    protected void cargarConPadre(TipoEgreso entidad) throws InventarioException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    protected void guardarConPadre(TipoEgreso entidad) throws InventarioException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    protected String guardar() throws InventarioException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    protected CategoriaEgreso nuevo() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
