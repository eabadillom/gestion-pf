package mx.com.ferbo.business.n;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import mx.com.ferbo.dao.n.CandadoSalidaDAO;
import mx.com.ferbo.model.CandadoSalida;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.util.InventarioException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
@Named
@RequestScoped
public class CandadoBL 
{
    private static Logger log = LogManager.getLogger(CandadoBL.class);
    
    @Inject
    private CandadoSalidaDAO candadoDAO;
    
    public CandadoSalida obtenerCandadoSalidaPorCliente(Cliente cliente){
        return candadoDAO.buscarPorCliente(cliente.getCteCve());
    }
    
    public void actualizaCandadoSalida(CandadoSalida candadoSalida) throws InventarioException
    {
        int numSalidas = candadoSalida.getNumSalidas() > 0 ? (candadoSalida.getNumSalidas() - 1) : 0;
        boolean isHabilitado = numSalidas > 0 ? true : false;
        candadoSalida.setNumSalidas(numSalidas);
        candadoSalida.setHabilitado(isHabilitado);
        candadoSalida.setSalidaTotal(false);
        candadoDAO.actualizar(candadoSalida);
    }
    
}
