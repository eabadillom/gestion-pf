package mx.com.ferbo.business.constancias;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import mx.com.ferbo.dao.n.ConstanciaSalidaDAO;
import mx.com.ferbo.dao.n.StatusConstanciaSalidaDAO;
import mx.com.ferbo.model.ConstanciaSalida;
import mx.com.ferbo.model.StatusConstanciaSalida;
import mx.com.ferbo.util.FacesUtils;
import mx.com.ferbo.util.InventarioException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
@Named
@RequestScoped
public class ConstanciaSalidaBL 
{
    private static Logger log = LogManager.getLogger(ConstanciaSalidaBL.class);
    
    @Inject
    private ConstanciaSalidaDAO constanciaSalidaDAO;
    
    @Inject
    private StatusConstanciaSalidaDAO statusConstanciaSalidaDAO;
    
    private final Integer idStatusConstancia = 1;
    
    public String buscarPorFolioSalida(String folioSalida) {
        return constanciaSalidaDAO.buscarPorNumero(folioSalida);
    }
    
    public void guardar(ConstanciaSalida constanciaSalida) throws InventarioException {
        FacesUtils.requireNonNullWithReturn(constanciaSalida, "La contancia no puede ser vacía");
        
        if (constanciaSalida.getStatus().getId() == 2){
            throw new InventarioException("La constancia de salida ya está cancelada");
        }
        
        constanciaSalidaDAO.guardar(constanciaSalida);
    }
    
    public StatusConstanciaSalida buscarStatusConstancia(){
        return statusConstanciaSalidaDAO.buscarConstanciaPorId(idStatusConstancia);
    }
    
}
