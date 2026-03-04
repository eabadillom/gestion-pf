package mx.com.ferbo.modulos.empresa.bussines;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.modulos.empresa.dao.EmpresaBaseDAO;
import mx.com.ferbo.modulos.empresa.model.Empresa;
import mx.com.ferbo.util.DAOException;
import mx.com.ferbo.util.InventarioException;

public abstract class EmpresaBaseBL<T extends Empresa> {

    protected EmpresaBaseDAO<T> dao;
    protected Logger log = LogManager.getLogger(this.getClass().getName());

    public EmpresaBaseBL() {}

    protected void setDao(EmpresaBaseDAO<T> dao) {
        this.dao = dao;
    }

    public String agregarOActualizar(T entity) throws InventarioException {
        // validarGenerico(model);
        // validarEspecifico(model);

        if (entity.getId() == null) {
            dao.guardar(entity);
            return "se agrego exitosamente";
        } else {
            dao.actualizar(entity);
            return "se aztualizo exitosamente";
        }
    }

    public List<T> obtenerTodos() throws InventarioException {
        try {
            return dao.buscarTodos();
        } catch (DAOException ex) {
            throw new InventarioException(
                    "Hubo un problema al momento de obtener los registros de " + this.getClass().getSimpleName(), ex);
        }
    }

}
