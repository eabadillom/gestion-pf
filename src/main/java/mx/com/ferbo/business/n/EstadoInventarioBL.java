package mx.com.ferbo.business.n;

import java.util.List;
import java.util.Optional;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import mx.com.ferbo.dao.n.EstadoInventarioDAO;
import mx.com.ferbo.model.EstadoInventario;
import mx.com.ferbo.util.InventarioException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
@Named
@RequestScoped
public class EstadoInventarioBL 
{
    private static final Logger log = LogManager.getLogger(EstadoInventarioBL.class);
    
    @Inject
    private EstadoInventarioDAO estadoInventarioDAO;
    
    public EstadoInventario buscarPorId(Integer idEstadoInventario) throws InventarioException{
        if(idEstadoInventario == null)
            throw new InventarioException("El cliente no puede ser vacía");
        
        Optional<EstadoInventario> estadoInventario = estadoInventarioDAO.buscarPorId(idEstadoInventario);
        EstadoInventario auxEstadoInventario = null;
        
        if(estadoInventario.isPresent())
            auxEstadoInventario = estadoInventario.get();
        else
            throw new InventarioException("No se encontro registro con ese identificador");
        
        return auxEstadoInventario;
    }
    
    public List<EstadoInventario> buscarTodos()
    {
        return estadoInventarioDAO.buscarTodos();
    }
    
}
