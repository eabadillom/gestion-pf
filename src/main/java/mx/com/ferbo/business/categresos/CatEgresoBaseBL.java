package mx.com.ferbo.business.categresos;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.categresos.CatEgresoBaseDAO;
import mx.com.ferbo.util.DAOException;
import mx.com.ferbo.util.InventarioException;
import mx.com.ferbo.model.categresos.CatEgreso;

public abstract class CatEgresoBaseBL<T extends CatEgreso> {

    protected CatEgresoBaseDAO<T> dao;
    protected Logger log = LogManager.getLogger(this.getClass().getName());

    public CatEgresoBaseBL() {}

    protected void setDao(CatEgresoBaseDAO<T> dao) {
        this.dao = dao;
    }

    protected void validarGenerico(T entity) throws InventarioException {
        if (entity == null) {
            throw new InventarioException("El objeto del catálogo no puede ser vacío.");
        }

        if (entity.getNombre() == null || entity.getNombre().trim().isEmpty()) {
            throw new InventarioException("El objeto del catálogo no tiene ningún nombre asociado.");
        }

        String descripcion = entity.getDescripcion();
        if (descripcion != null && descripcion.trim().isEmpty()) {
            throw new InventarioException("El objeto del catálogo debe tener una descripción.");
        }

        if (entity.getVigente() == null) {
            throw new InventarioException("No se sabe si el objeto del catálogo está vigente o no.");
        }
    }

    protected abstract void validarEspecifico(T entity) throws InventarioException;

    public List<T> vigentesONoVigentes(boolean vigente) throws InventarioException {
        String sVigente = vigente ? "vigentes" : "no vigentes";
        try {
            return dao.findByVigente(vigente);
        } catch (DAOException ex) {
            log.warn("Error al obtener los datos" + sVigente + ": " + ex.getMessage());
            throw new InventarioException(
                    "Hubo un problema al obtener los datos " + sVigente, ex);
        }
    }

    public T buscarPorNombre(String nombre) throws InventarioException {
        try {
            return dao.buscarPorNombre(nombre);
        } catch (DAOException ex) {
            log.warn("Error al buscar el registro por nombre " + nombre + ": "
                    + ex.getMessage());
            throw new InventarioException(
                    "Hubo un problema al buscar el registro por nombre " + nombre, ex);
        }
    }

    public String agregarOActualizar(T entity) throws InventarioException {
        validarGenerico(entity);
        validarEspecifico(entity);

        if (entity.getId() == null) {
            dao.guardar(entity);
            return "se agrego exitosamente";
        } else {
            dao.actualizar(entity);
            return "se aztualizo exitosamente";
        }
    }
}
