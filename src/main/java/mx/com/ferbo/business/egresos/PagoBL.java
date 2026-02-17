package mx.com.ferbo.business.egresos;

import java.util.Date;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import mx.com.ferbo.dao.egresos.PagoDAO;
import mx.com.ferbo.model.catalogos.StatusPago;
import mx.com.ferbo.model.egresos.PagoEgreso;
import mx.com.ferbo.util.InventarioException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Named
@RequestScoped
public class PagoBL {
    
    private static final Logger log = LogManager.getLogger(PagoBL.class);
    
    @Inject
    private PagoDAO dao;
    
    public void validarPago(PagoEgreso pago) throws InventarioException{
        if (pago == null) {
            throw new InventarioException("El pago no puede ser vacío");
        }
        
        if (pago.getImporteEgreso() == null) {
            throw new InventarioException("El pago no tiene asociado un egreso.");
        }
        
        if (pago.getFechaAlta() == null ){
            throw new InventarioException("El pago no tiene una fecha de alta asignada.");
        }
        
        if (pago.getFechaLimite() == null) {
            throw new InventarioException("El pago no tiene una fecha limite para el pago");
        }
        
        if (pago.getImporte() == null) {
            throw new InventarioException("El pago no tiene un importe.");
        }
        
        if (pago.getStatus() == null) {
            throw new InventarioException("El pago no tiene asociado un status.");
        }
        
        if (pago.getStatus().getNombre().equalsIgnoreCase("PAGADO")) {
            if (pago.getFechaPago() == null) {
                throw new InventarioException("El pago no tiene una fecha de cuando se realizo.");
            }
            
            if ("".equalsIgnoreCase(pago.getReferencia())){
                throw new InventarioException("El pago no tiene ninguna referencia.");
            }
        }
        
    }
    
    public String operar(PagoEgreso pago) throws InventarioException {
        String mensaje = "El pago del egeso se ";
        pago.setFechaAlta(new Date());
        try {
            validarPago(pago);
            if (pago.getId() == null) {
                dao.guardar(pago);
                mensaje += "se guardo correctamente";
            } else {
                dao.actualizar(pago);
                mensaje += "actulizo correctamente";
            }
            return mensaje;
        } catch (InventarioException ex) {
            log.warn("Error al operar el pago: {}. {}", pago, ex);
            throw ex;
        }
    }
    
    public String cambiarEstado(PagoEgreso pago, StatusPago status) throws InventarioException {
        if (pago.getStatus().getNombre().equalsIgnoreCase("PAGADO") || pago.getStatus().getNombre().equalsIgnoreCase("CANCELADO")) {
            throw new InventarioException("El stutus del pago no se puede cambiar de: " + pago.getStatus().getNombre());
        }
        pago.setStatus(status);
        return "El pago cambio a estado: " + status.getNombre();
    }
}
