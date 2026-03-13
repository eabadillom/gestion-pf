package mx.com.ferbo.business.salidas;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import mx.com.ferbo.dao.n.SalidaDAO;
import mx.com.ferbo.dao.n.ServiciosSalidaDAO;
import mx.com.ferbo.dao.n.StatusSalidaDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.Salida;
import mx.com.ferbo.model.ServiciosSalida;
import mx.com.ferbo.model.StatusSalida;
import mx.com.ferbo.ui.OrdenDeSalidas;
import mx.com.ferbo.util.DAOException;
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
public class SalidasBL 
{
    private static Logger log = LogManager.getLogger(SalidasBL.class);
    
    @Inject
    private SalidaDAO salidasDAO;
    
    @Inject
    private ServiciosSalidaDAO serviciosSalidasDAO;
    
    @Inject
    private StatusSalidaDAO statusSalidaDAO;
    
    public static final String TP_ENVIADO = "E";
    public static final String TP_ACEPTADO = "A";
    public static final String TP_CANCELADO = "C";
    
    public StatusSalida obtenerStatusEnviado() throws DAOException {
        return statusSalidaDAO.findByClave(TP_ENVIADO);
    }
    
    public StatusSalida obtenerStatusAceptado() throws DAOException {
        return statusSalidaDAO.findByClave(TP_ACEPTADO);
    }
    
    public List<String> obtenerFolios(Cliente cliente, Date fecha, Integer idPlanta) 
    {
        List<String> folios = new ArrayList();
        StatusSalida stSalida = null;
        
        try{
            FacesUtils.requireNonNull(cliente, "Error al obtener el cliente");
            FacesUtils.requireNonNull(fecha, "Error al obtener la fecha");
            FacesUtils.requireNonNull(idPlanta, "Error al obtener la planta");
            
            stSalida = this.obtenerStatusEnviado();
            List<Salida> listSalida = salidasDAO.findByParametros(stSalida.getClave(), fecha, cliente.getCteCve(), idPlanta);
            
            if(listSalida.isEmpty())
                return folios;
            
            for(Salida auxSalida : listSalida)
            {
                folios.add(auxSalida.getFolioSalida());
            }
        } catch(Exception ex){
            log.error("Error al cargar los folios de salida...", ex);
        }        
        
        return folios;
    }
    
    public void actualizarSalida(Salida salida) throws DAOException, InventarioException  {
        StatusSalida auxStatusSalida = obtenerStatusAceptado();
        salida.setStatus(auxStatusSalida);
        salida.setFechaModificacion(new Date());
        actualizar(salida);
    }
    
    public List<OrdenDeSalidas> obtenerInventario(String folioSalida, Date fecha){
        List<OrdenDeSalidas> listOrdenSalidas = new ArrayList();
        StatusSalida stSalida = null;
        
        try{
            if(fecha == null)
                throw new InventarioException("Error al obtener la fecha");
            
            if(folioSalida == null)
                throw new InventarioException("Error al obtener el folio de salida");
            
            stSalida = this.obtenerStatusEnviado();
            listOrdenSalidas = salidasDAO.buscarInventarioSalida(stSalida.getClave(), folioSalida, fecha);
        } catch(Exception ex){
            log.error("Error al cargar los folios de salida...", ex);
        }
        
        return listOrdenSalidas;
    }
    
    public Salida obtenerSalidaPorFolio(String folioSalida) throws DAOException {
        return salidasDAO.findByFolioSalida(folioSalida);
    }
    
    public Integer totalSalidasPorCliente(String clave, Date fechaSalida, Integer idPlanta){
        return salidasDAO.totalSalidasPorCliente(clave, fechaSalida, idPlanta);
    }
    
    public List<ServiciosSalida> obtenerServiciosPorFolioSalida(String folioSalida) throws DAOException {
        return serviciosSalidasDAO.buscarPorFolioSalida(folioSalida);
    }
    
    public void actualizar(Salida salida) throws InventarioException {
        FacesUtils.requireNonNull(salida, "Error al actualizar la salida");
        salidasDAO.actualizar(salida);
    }
    
}
