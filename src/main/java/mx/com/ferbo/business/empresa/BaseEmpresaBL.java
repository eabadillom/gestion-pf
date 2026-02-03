package mx.com.ferbo.business.empresa;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.empresa.Empresa;
import mx.com.ferbo.util.DAOException;
import mx.com.ferbo.util.InventarioException;

public abstract class BaseEmpresaBL<MODEL extends Empresa> {

    protected BaseDAO<MODEL, ?> dao;
    protected Logger log = LogManager.getLogger(this.getClass().getName());

    public BaseEmpresaBL() {

    }

    protected void setDao(BaseDAO<MODEL, ?> dao) {
        this.dao = dao;
    }

    public String agregarOActualizar(MODEL model) throws InventarioException {
        // validarGenerico(model);
        // validarEspecifico(model);

        if (model.getId() == null) {
            dao.guardar(model);
            return "se agrego exitosamente";
        } else {
            dao.actualizar(model);
            return "se aztualizo exitosamente";
        }
    }

    public List<MODEL> obtenerTodos() throws InventarioException {
        try {
            return dao.buscarTodos();
        } catch (DAOException ex) {
            throw new InventarioException(
                    "Hubo un problema al momento de obtener los registros de " + this.getClass().getSimpleName(), ex);
        }
    }

}
