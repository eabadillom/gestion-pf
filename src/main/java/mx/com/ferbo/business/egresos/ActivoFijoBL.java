package mx.com.ferbo.business.egresos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import mx.com.ferbo.dao.egresos.ActivoFijoDAO;
import mx.com.ferbo.model.catalogos.StatusActivoFijo;
import mx.com.ferbo.model.egresos.ActivoFijo;
import mx.com.ferbo.util.InventarioException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Named
@RequestScoped
public class ActivoFijoBL {
    
    private static final Logger log = LogManager.getLogger();
    
    @Inject
    private ActivoFijoDAO dao;
    
    public void validarActivoFijo(ActivoFijo activo) throws InventarioException {
        
        if (activo == null) {
            throw new InventarioException("El activo fijo no puede ser nulo.");
        }
        
        if (activo.getDescripcion() == null || activo.getDescripcion().trim().isEmpty()) {
            throw new InventarioException("El activo fijo debe tener una descripción.");
        }
        
        if (activo.getImporte() == null || activo.getImporte().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InventarioException("El activo fijo debe tener un valor monetario mayor a cero.");
        }
        
        if (activo.getVidaUtil() == null || activo.getVidaUtil() < 1) {
            throw new InventarioException("El activo fijo debe tener un número mayor a cero.");
        }
        
        Date fecha = activo.getFechaAdquisicion();
        
        LocalDate fechaAdquisicion = fecha.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        
        if (activo.getFechaAdquisicion() == null
                || fechaAdquisicion.isAfter(LocalDate.now())) {
            throw new InventarioException("La fecha de adquisición no es válida.");
        }
        
        if (activo.getStatus() == null) {
            throw new InventarioException("El activo fijo no tiene asociado un status.");
        }
    }
    
    public String operar(ActivoFijo activo) throws InventarioException {
        
        String mensaje = "El activo fijo se ha ";
        try {
            
            if (activo.getId() == null) {
                dao.guardar(activo);
                mensaje += " guardado exitosamente.";
            } else {
                dao.actualizar(activo);
                mensaje += " actualizado exitosamente.";
            }
            return mensaje;
        } catch (InventarioException ex) {
            log.warn("Erro al operar el activo fijo: {}. {}", activo.getDescripcion(), ex);
            throw ex;
        }
        
    }
    
    public String cambiarEstado(ActivoFijo activo, StatusActivoFijo status) throws InventarioException {
        if ("Dado de Baja".equalsIgnoreCase(activo.getStatus().getNombre()) || "Descartado".equalsIgnoreCase(activo.getStatus().getNombre()) || "Vendido".equalsIgnoreCase(activo.getStatus().getNombre())) {
            throw new InventarioException("El status actuaal del activo fijo, ya no puede ser cambiado.");
        }
        
        activo.setStatus(status);
        
        return "El status del activo fijo ha cambiado satisfactoriamente a: " + status.getNombre();
    }
}
