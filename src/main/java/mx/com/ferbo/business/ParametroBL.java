package mx.com.ferbo.business;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.n.ParametroDAO;
import mx.com.ferbo.model.Parametro;
import mx.com.ferbo.util.DAOException;
import mx.com.ferbo.util.InventarioException;

@Named
@RequestScoped
public class ParametroBL {
    
    private static final Logger log = LogManager.getLogger(ParametroBL.class);

    @Inject
    private ParametroDAO parametroDAO;

    public Parametro obtenerParametroPorNombre(String nombre) throws InventarioException{
        try {
            return parametroDAO.buscarPorNombre(nombre);
        } catch (DAOException ex) {
           log.warn("Error al buscar el parametro con el nombre {}. {}", nombre,ex);
            throw new InventarioException("Hubo un problema al buscar el parametro con nombre {}. nombre");
        }
    }
}
