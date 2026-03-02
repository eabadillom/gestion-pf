
package mx.com.ferbo.business.egresos;

import java.util.List;
import mx.com.ferbo.dao.egresos.EgresoBaseDAO;
import mx.com.ferbo.model.egresos.Egreso;
import mx.com.ferbo.util.DAOException;
import mx.com.ferbo.util.ValidationUtils;
import mx.com.ferbo.util.InventarioException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import mx.com.ferbo.model.categresos.CatEgreso;

public abstract class EgresoBaseBL<T extends Egreso, P extends Egreso, C extends CatEgreso> {

    protected EgresoBaseDAO<T> dao;
    protected Logger log = LogManager.getLogger(this.getClass().getName());

    public EgresoBaseBL() {
    }

    protected abstract T nuevo();

    protected abstract C statusInicial();

    public abstract String nombreHijo();

    public abstract String nombreHijos();

    public abstract String nombreCatalogo();

    protected abstract void validar(T entity) throws InventarioException;

    protected abstract void antesDeGuardar(T son, P father) throws InventarioException;

    protected abstract void antesDeCambiar(T son, C catalog) throws InventarioException;

    protected void setDao(EgresoBaseDAO<T> dao) {
        this.dao = dao;
    }

    public T nuevoOExistente(T entity) {
        if (entity == null) {
            entity = nuevo();
            entity.setStatus(statusInicial());
        }
        return entity;
    }

    public List<T> obtenerPorImporteEgreso(P father) throws InventarioException {

        ValidationUtils.requireNonNull(father, "El egreso no puede estar vacío.");

        try {
            return dao.buscarPorImporteEgreso(father.getId());
        } catch (DAOException ex) {
            log.warn("Hubo un problema al obtener {} relacionados con el egreso: {}. {}", nombreHijos(), father, ex);
            throw new InventarioException(
                    "Hubo un problema al obtener" + nombreHijos() + " relaciondos con el egreso seleccionado.");
        }
    }

    public List<T> obtenerPorImporteEgresoYStatus(P father, C catalog) throws InventarioException {

        ValidationUtils.requireNonNull(father, nombrePadre() + " no puese ser vacío.");
        ValidationUtils.requireNonNull(catalog, nombreCatalogo() + " no puese ser vacío.");

        try {
            return dao.buscarPorImporteEgresoYStatus(father.getId(), catalog.getNombre());
        } catch (DAOException ex) {
            log.warn("Error al obetener los elementos asociados con el importe egreso de id: {}, y status: {}. {}",
                    father.getId(), catalog.getNombre(), ex);
            throw new InventarioException(
                    "Hubo un problema al obtener la información solicitada por el importe de egreso y el status "
                            + catalog.getNombre() + ".");
        }
    }

    public T operar(T son, P father) throws InventarioException {

        ValidationUtils.requireNonNull(father, nombrePadre() + " no puede ser vacío.");
        ValidationUtils.requireNonNull(son, nombreHijo() + " no puede ser vacío.");

        antesDeGuardar(son, father);
        try {
            validar(son);
            if (son.getId() == null) {
                dao.guardar(son);
            } else {
                dao.actualizar(son);
            }
            return son;
        } catch (InventarioException ex) {
            log.warn("Error al operar {}: {}. {}", nombreHijo(), son, ex);
            throw ex;
        }
    }

    public T cambiar(T son, C catalog) throws InventarioException {
        ValidationUtils.requireNonNull(son, nombreHijo() + " no puede ser vacío.");

        ValidationUtils.requireNonNull(catalog, nombreCatalogo() + " no puede ser vacío");

        antesDeCambiar(son, catalog);

        return son;
    }

    protected String nombrePadre() {
        return "el importe de egreso";
    }
}
