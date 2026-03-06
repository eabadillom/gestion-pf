package mx.com.ferbo.business.n;

import java.util.List;
import java.util.Optional;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import mx.com.ferbo.dao.n.DetallePartidaDAO;
import mx.com.ferbo.dao.n.PartidaDAO;
import mx.com.ferbo.model.DetallePartida;
import mx.com.ferbo.model.Partida;
import mx.com.ferbo.util.InventarioException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
@Named
@RequestScoped
public class PartidaBL 
{
    private static Logger log = LogManager.getLogger(PartidaBL.class);
    
    @Inject
    private PartidaDAO partidaDAO;
    
    @Inject
    private DetallePartidaDAO detallePartidaDAO;
    
    /*Partida*/
    public Partida buscarPartidaPorId(Integer idPartida) throws InventarioException {
        if(idPartida == null)
            throw new InventarioException("La partida no puede ser vacía");
        
        Optional<Partida> auxPartida = partidaDAO.buscarPorId(idPartida);
        Partida partida = null;
        
        if(auxPartida.isPresent())
            partida = auxPartida.get();
        else
            throw new InventarioException("No se encontro registro con ese identificador");
        
        return partida;
    }
    
    public Partida buscarPartidaConEntrada(Integer idPartida) throws InventarioException {
        if(idPartida == null)
            throw new InventarioException("La partida no puede ser vacía");
        
        return partidaDAO.buscarPorIdConEntrada(idPartida);
    }
    
    /*Detalle Partida*/
    public List<DetallePartida> buscarPorId(Integer partidaCve) throws InventarioException {
        if(partidaCve == null)
            throw new InventarioException("La partida no puede ser vacía");
        
        return detallePartidaDAO.buscarPorPartida(partidaCve);
    }
    
    public void guardarDetallePartida(DetallePartida detallePartida) throws InventarioException {
        detallePartidaDAO.guardar(detallePartida);
    }
    
    public void actualizarDetallePartida(DetallePartida detallePartida) throws InventarioException{
        detallePartidaDAO.actualizar(detallePartida);
    }
    
}
