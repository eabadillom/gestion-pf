package mx.com.ferbo.business.n;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.n.UnidadManejoDAO;
import mx.com.ferbo.model.UnidadDeManejo;
import mx.com.ferbo.util.DAOException;
import mx.com.ferbo.util.InventarioException;

@Named
@RequestScoped
public class UnidadManejoBL {

    private static final Logger log = LogManager.getLogger(UnidadManejoBL.class);

    @Inject
    private UnidadManejoDAO unidadManejoDAO;

    public List<UnidadDeManejo> obtenerUnidadesManejo() throws InventarioException {
        try {
            return unidadManejoDAO.buscarTodos();
        } catch (DAOException ex) {
            log.error("Error al obtener unidades de manejo", ex);
            throw new InventarioException("Ocurrió un error al obtener las unidades de manejo", ex);
        }
    }

}
