package mx.com.ferbo.business.catalogos;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.catalogos.Catalogo;
import mx.com.ferbo.util.DAOException;
import mx.com.ferbo.util.InventarioException;

public abstract class BaseCatalogosBL<MODEL extends Catalogo> {

    protected BaseDAO<MODEL, ?> dao;
    protected Logger log = LogManager.getLogger(this.getClass().getName());

    public BaseCatalogosBL(){
        
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

    public List<MODEL> vigentesONoVigentes(boolean vigente) throws InventarioException {
        String sVigente = vigente ? "vigentes" : "no vigentes";
        try {
            return dao.findByVigente(vigente);
        } catch (DAOException ex) {
            log.warn("Error al obtener los datos" + sVigente + ": " + ex.getMessage());
            throw new InventarioException(
                    "Hubo un problema al obtener los datos " + sVigente, ex);
        }
    }

    public MODEL buscarPorNombre(String nombre) throws InventarioException {
        try {
            return dao.buscarPorNombre(nombre);
        } catch (DAOException ex) {
            log.warn("Error al buscar el registro por nombre " + nombre + ": "
                    + ex.getMessage());
            throw new InventarioException(
                    "Hubo un problema al buscar el registro por nombre " + nombre, ex);
        }
    }

    public String agregarOActualizar(MODEL model) throws InventarioException {
        validarGenerico(model);
        validarEspecifico(model);

        if (model.getId() == null){
            dao.guardar(model);
            return "se agrego exitosamente";
        } else {
            dao.actualizar(model);
            return "se aztualizo exitosamente";
        }
    }

    protected void setDao(BaseDAO<MODEL, ?> dao) {
        this.dao = dao;
    }
}
