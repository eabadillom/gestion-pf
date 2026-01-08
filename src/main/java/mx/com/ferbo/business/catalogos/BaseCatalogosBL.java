package mx.com.ferbo.business.catalogos;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.n.catalogos.Catalogo;
import mx.com.ferbo.util.DAOException;
import mx.com.ferbo.util.InventarioException;

public abstract class BaseCatalogosBL<MODEL extends Catalogo> {

    protected BaseDAO<MODEL, ?> dao; // DAO genérico
    protected Logger log = LogManager.getLogger(this.getClass().getName());

    public BaseCatalogosBL(BaseDAO<MODEL, ?> dao) {
        this.dao = dao;
    }

    protected void validarGenerico(MODEL model) throws InventarioException {
        if (model == null) {
            throw new InventarioException("El objeto del catálogo no puede ser vacío.");
        }

        if (model.getNombre() == null || model.getNombre().trim().isEmpty()) {
            throw new InventarioException("El objeto del catálogo no tiene ningún nombre asociado.");
        }

        String descripcion = model.getDescripcion();
        if (descripcion != null && descripcion.trim().isEmpty()) {
            throw new InventarioException("El objeto del catálogo debe tener una descripción.");
        }

        if (model.getVigente() == null) {
            throw new InventarioException("No se sabe si el objeto del catálogo está vigente o no.");
        }
    }

    protected abstract void validarEspecifico(MODEL model) throws InventarioException;

    public List<MODEL> getVigentesONoVigentes(boolean vigente) throws InventarioException {
        String sVigente = vigente ? "vigentes" : "no vigentes";
        try {
            return dao.findByVigente(vigente);
        } catch (DAOException ex) {
            log.warn("Error al obtener " + dao.getClass().getSimpleName() + " " + sVigente + ": " + ex.getMessage());
            throw new InventarioException(
                    "Hubo un problema al obtener " + dao.getClass().getSimpleName() + " " + sVigente, ex);
        }
    }

    public MODEL buscarPorNombre(String nombre) throws InventarioException {
        try {
            return dao.buscarPorNombre(nombre);
        } catch (DAOException ex) {
            log.warn("Error al buscar " + dao.getClass().getSimpleName() + " por nombre " + nombre + ": "
                    + ex.getMessage());
            throw new InventarioException(
                    "Hubo un problema al buscar " + dao.getClass().getSimpleName() + " por nombre " + nombre, ex);
        }
    }

    protected void setDao(BaseDAO<MODEL, ?> dao) {
        this.dao = dao;
    }
}
