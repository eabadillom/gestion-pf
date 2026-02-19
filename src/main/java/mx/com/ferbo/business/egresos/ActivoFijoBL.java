package mx.com.ferbo.business.egresos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import mx.com.ferbo.dao.egresos.ActivoFijoDAO;
import mx.com.ferbo.model.categresos.StatusActivoFijo;
import mx.com.ferbo.model.egresos.ActivoFijo;
import mx.com.ferbo.model.egresos.ImporteEgreso;
import mx.com.ferbo.util.InventarioException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Named
@RequestScoped
public class ActivoFijoBL extends EgresoBaseBL<ActivoFijo, ImporteEgreso, StatusActivoFijo> {

    private static final Logger log = LogManager.getLogger();

    @Inject
    private ActivoFijoDAO dao;

    public ActivoFijoBL() {
        setDao(dao);
    }
    
    @Override
    protected ActivoFijo nuevo(){
        return new ActivoFijo();
    }

    @Override
    protected String nombreHijo() {
        return "el activo fijo";
    }

    @Override
    protected String nombreHijos() {
        return "el activo fijo";
    }

    @Override
    protected String nombreCatalogo() {
        return "el status";
    }

    @Override
    protected void validar(ActivoFijo activo) throws InventarioException {
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

    @Override
    protected void antesDeGuardar(ActivoFijo activo, ImporteEgreso importe) throws InventarioException {
        
        if (activo.getImporteEgreso() == null){
            activo.setImporteEgreso(importe);
        }
    }

    @Override
    protected void antesDeCambiar(ActivoFijo activo, StatusActivoFijo status) throws InventarioException {
        if ("Dado de Baja".equalsIgnoreCase(activo.getStatus().getNombre()) || "Descartado".equalsIgnoreCase(activo.getStatus().getNombre()) || "Vendido".equalsIgnoreCase(activo.getStatus().getNombre())) {
            throw new InventarioException("El status actuaal del activo fijo, ya no puede ser cambiado.");
        }

        activo.setStatus(status);
    }
}
