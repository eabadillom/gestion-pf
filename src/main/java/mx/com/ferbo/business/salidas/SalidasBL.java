package mx.com.ferbo.business.salidas;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.tools.exception.SystemException;
import com.ferbo.tools.exception.ValidationException;
import com.ferbo.tools.util.date.DateFormatter;
import java.text.ParseException;

import mx.com.ferbo.dao.n.ClienteDAO;
import mx.com.ferbo.dao.n.SalidaDAO;
import mx.com.ferbo.dao.n.ServiciosSalidaDAO;
import mx.com.ferbo.dao.n.StatusSalidaDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.Salida;
import mx.com.ferbo.model.SalidaDetalle;
import mx.com.ferbo.model.ServiciosSalida;
import mx.com.ferbo.model.StatusSalida;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.ui.OrdenDeSalidas;
import mx.com.ferbo.ui.SalidaDetalleUI;
import mx.com.ferbo.util.DAOException;
import mx.com.ferbo.util.FacesUtils;
import mx.com.ferbo.util.InventarioException;

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
    
    @Inject
    private ClienteDAO clienteDAO;
    
    public static final String TP_ENVIADO = "E";
    public static final String TP_ACEPTADO = "A";
    public static final String TP_CANCELADO = "C";
    
    public StatusSalida obtenerStatusEnviado() throws DAOException {
        return statusSalidaDAO.findByClave(TP_ENVIADO);
    }
    
    public StatusSalida obtenerStatusAceptado() throws DAOException {
        return statusSalidaDAO.findByClave(TP_ACEPTADO);
    }

    public StatusSalida obtenerStatusCancelado() throws DAOException {
        return statusSalidaDAO.findByClave(TP_CANCELADO);
    }
    
    public Salida buscar(String folioSalida) throws InventarioException {
        return salidasDAO.buscar(folioSalida)
        		.orElseThrow(() -> new InventarioException("Folio no encontrado."));
    }
    
    public List<Salida> buscarEnviadas(Cliente cliente) {
    	List<Salida> salidas;
    	
    	try {
    		salidas = salidasDAO.buscarPorStatus(cliente, new Date(), this.obtenerStatusEnviado());
    		
    	} catch(Exception ex) {
    		salidas = new ArrayList<Salida>();
    	}
    	
    	return salidas;
    }
    
    public List<Cliente> getListaClientesPendientes(Planta planta) {
    	List<Cliente> clientes = null;
    	StatusSalida statusEnviada;
    	
		try {
			statusEnviada = statusSalidaDAO.findByClave(TP_ENVIADO);
			clientes = clienteDAO.buscarPorOrdenesDeSalida(planta, statusEnviada, new Date());
		} catch (DAOException ex) {
			log.error("Problema para obtener la lista de clientes con ordenes de retiro pendientes...", ex);
		}
		
		return clientes;
	}
    
    public List<String> obtenerFolios(Cliente cliente, Date fecha, Integer idPlanta) 
    {
        List<String> folios = new ArrayList<String>();
        StatusSalida stSalida = null;
        
        try {
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
    
    public List<SalidaDetalleUI> toUI (List<SalidaDetalle> listaDetalles) {
    	List<SalidaDetalleUI> detalles = new ArrayList<SalidaDetalleUI>();
    	
    	for(SalidaDetalle d : listaDetalles) {
    		SalidaDetalleUI dUI = new SalidaDetalleUI();
    		dUI.setIdSalidaDetalle(d.getIdSalidaDetalle());
    		dUI.setSalida(d.getSalida());
    		dUI.setPartida(d.getPartida());
    		dUI.setCantidad(d.getCantidad());
    		dUI.setPesoAprox(d.getPesoAprox());
    		
    		detalles.add(dUI);
    	}
    	
    	return detalles;
    }
    
    @Deprecated
    public List<OrdenDeSalidas> obtenerInventario(String folioSalida, Date fecha){
        List<OrdenDeSalidas> listOrdenSalidas = new ArrayList<OrdenDeSalidas>();
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
    
    public List<Salida> consultarSalidas(Cliente cliente, LocalDate fechaInicio, LocalDate fechaFin) throws ParseException, SystemException, ValidationException {
        
        String fechaInicioString = DateFormatter.format(fechaInicio, "yyyy-MM-dd");
        Date fecchaInicioDate = DateFormatter.parseToDate(fechaInicioString, "yyyy-MM-dd");
        
        String fechaFinString = DateFormatter.format(fechaFin, "yyyy-MM-dd");
        Date fecchaFinDate = DateFormatter.parseToDate(fechaFinString, "yyyy-MM-dd");
        
        Integer idCliente = (cliente == null) ? null : cliente.getCteCve();
         
        return salidasDAO.buscarPorClientePeriodo(idCliente, fecchaInicioDate, fecchaFinDate);
    }

    public Map<String, Object> calcularPesoYCantidadTotales(Salida salida) {
        if (salida == null) {
            throw new ValidationException(
                    "Error al seleccionar la salida, intente nuevamente. Si el problema persiste, contacte al soporte del sistema");
        }

        Integer totalCantidad = 0;
        BigDecimal totalPeso = BigDecimal.ZERO;
        List<SalidaDetalle> listSalidaDetalle = salida.getListSalidaDetalle();

        for (SalidaDetalle aux : listSalidaDetalle) {
            totalCantidad = totalCantidad + aux.getCantidad();
            totalPeso = totalPeso.add(aux.getPesoAprox());
        }

        Map<String, Object> totales = new HashMap<>();
        totales.put("cantidad", totalCantidad);
        totales.put("peso", totalPeso);

        return totales;
    }

    public void cancelarOrden(Usuario usuario, Salida salida, StatusSalida cancelado) throws InventarioException, ValidationException {

        if (usuario == null) {
            throw new ValidationException("El usuario no puede ser vacío");
        }

        if (usuario.getPerfil() != 2 && usuario.getPerfil() != 3) {
            throw new InventarioException("No tiene los permisos suficientes para cancelar la orden de salida");
        }


        if (salida == null) {
            throw new ValidationException("La salida a cancelar no puede ser vacía");
        }

        String statusActual = salida.getStatus().getClave().trim().toUpperCase();

        if ("A".equalsIgnoreCase(statusActual)) {
            throw new InventarioException("La orden de salida ya se atendio y por lo tanto no se puede cancelar");
        }

        if ("C".equalsIgnoreCase(statusActual)) {
            throw new InventarioException("La orden de salida ya se encuentra cancelada");
        }

        String statusCancelado = cancelado.getClave().trim().toUpperCase();

        if (!"C".equalsIgnoreCase(statusCancelado)) {
            throw new InventarioException("El status a asignar, no es de cancelación");
        }

        salida.setStatus(cancelado);

        salidasDAO.actualizar(salida);
    }

}
