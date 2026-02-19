
package mx.com.ferbo.business.egresos;

import java.util.List;
import mx.com.ferbo.dao.egresos.EgresoBaseDAO;
import mx.com.ferbo.model.egresos.Egreso;
import mx.com.ferbo.util.DAOException;
import mx.com.ferbo.util.FacesUtils;
import mx.com.ferbo.util.InventarioException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import mx.com.ferbo.model.categresos.CatEgreso;

public abstract class EgresoBaseBL<T extends Egreso, P extends Egreso, C extends CatEgreso> {
    
    protected EgresoBaseDAO<T> dao;
    protected Logger log = LogManager.getLogger(this.getClass().getName());
    
    public EgresoBaseBL(){}
    
    protected abstract T nuevo();
    protected abstract String nombreHijo();
    protected abstract String nombreHijos();
    protected abstract String nombreCatalogo();
    protected abstract void validar(T entity) throws InventarioException;
    protected abstract void antesDeGuardar(T son, P father) throws InventarioException;
    protected abstract void antesDeCambiar(T son, C catalog) throws InventarioException;
    
    protected void setDao(EgresoBaseDAO<T> dao) {
        this.dao = dao;
    }
    
    public void statusIncial(T son, C catalog){
        
        if (son.getId() == null){
            son.setStatus(catalog);
        } 
        
    }
    
    public List<T> obtenerPorImporteEgreso(P father) throws InventarioException{
         
        if (father == null) {
            throw new InventarioException("El egreso no puede estar vacío.");
        }
        
        try {
            return dao.buscarPorImporteEgreso(father.getId());
        } catch (DAOException ex) { 
            log.warn("Hubo un problema al obtener {} relacionados con el egreso: {}. {}", nombreHijos(), father, ex);
            throw new InventarioException("Hubo un problema al obtener" + nombreHijos() + " relaciondos con el egreso seleccionado.");
        }
        
    }
    
    public String operar(T son, P father) throws InventarioException {
        
        FacesUtils.requireNonNull(father, nombrePadre() + " no puede ser vacío.");
        FacesUtils.requireNonNull(son, nombreHijo() + " no puede ser vacío.");
        
        String mensaje = nombreHijo() + " del egreso se ";
        antesDeGuardar(son, father);
        try {
            validar(son);
            if (son.getId() == null) {
                dao.guardar(son);
                mensaje += "se guardo correctamente";
            } else {
                dao.actualizar(son);
                mensaje += "actulizo correctamente";
            }
            return mensaje;
        } catch (InventarioException ex) {
            log.warn("Error al operar {}: {}. {}", nombreHijo(), son, ex);
            throw ex;
        }
    }
    
    public String cambiar(T son, C catalog) throws InventarioException {
         FacesUtils.requireNonNull(son, nombreHijo() + " no puede ser vacío.");
        
        FacesUtils.requireNonNull(catalog, nombreCatalogo() + " no puede ser vacío");
        
        antesDeCambiar(son, catalog);
        
        return nombreHijo() + " ha cambiado satisfactoriamente a: " + catalog.getNombre();
    } 
    
    protected String nombrePadre() {
        return "el importe de egreso";
    }
}
