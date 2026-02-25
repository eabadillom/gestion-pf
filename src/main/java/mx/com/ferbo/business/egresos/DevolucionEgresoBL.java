package mx.com.ferbo.business.egresos;

import java.util.Date;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import mx.com.ferbo.business.categresos.StatusDevolucionEgresoBL;
import mx.com.ferbo.business.egresos.util.MaquinaStatusDevolucion;
import mx.com.ferbo.dao.egresos.DevolucionEgresoDAO;
import mx.com.ferbo.model.categresos.StatusDevolucionEgreso;
import mx.com.ferbo.model.egresos.DevolucionEgreso;
import mx.com.ferbo.model.egresos.ImporteEgreso;
import mx.com.ferbo.util.FacesUtils;
import mx.com.ferbo.util.InventarioException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Named
@RequestScoped
public class DevolucionEgresoBL extends EgresoBaseBL<DevolucionEgreso, ImporteEgreso, StatusDevolucionEgreso> {

    private static final Logger log = LogManager.getLogger(DevolucionEgresoBL.class);

    private final String STATUS_REGISTRADA = "REGISTRADA";
    private final String STATUS_AUTORIZADA = "AUTORIZADA";
    private final String STATUS_APLICADA = "APLICADA";
    private final String STATUS_RECHAZADA = "RECHAZADA";
    private final String STATUS_CANCELADA = "CANCELADA";
    private final String STATUS_EN_PROCESO = "EN_PROCESO";

    @Inject
    private DevolucionEgresoDAO dao;
    
    @Inject
    private StatusDevolucionEgresoBL statusBL;
    
    private MaquinaStatusDevolucion maquinaStatus;

    @PostConstruct
    public void init() {
        setDao(dao);
        maquinaStatus = new MaquinaStatusDevolucion();
    }

    @Override
    protected DevolucionEgreso nuevo() {
        return new DevolucionEgreso();
    }

    @Override
    protected String nombreHijo() {
        return "la devolución del pago";
    }

    @Override
    protected String nombreHijos() {
        return "las devoluciones del pago";
    }

    @Override
    protected String nombreCatalogo() {
        return "el status";
    }

    @Override
    protected void validar(DevolucionEgreso devolucion) throws InventarioException {
        if (devolucion.getImporteDevolucion() == null) {
            throw new InventarioException("La devolución no tiene ningun importe asociado.");
        }

        if (devolucion.getTipo() == null) {
            throw new InventarioException("No hay un tipo de devolución asociado.");
        }

        if (devolucion.getStatus() == null) {
            throw new InventarioException("La devolución  no tiene asociado un extatus.");
        }

        if (devolucion.getFechaAlta() == null) {
            throw new InventarioException("La devolución no tiene una fecha de alta.");
        }

        if (devolucion.getFechaModificacion() == null) {
            throw new InventarioException("La devolución no tiene una fecha de modificación.");
        }

        if (devolucion.getImporteDevolucion() == null) {
            throw new InventarioException("La devolución no está asociado a ningun importe de egreso.");
        }

    }

    @Override
    protected void antesDeGuardar(DevolucionEgreso devolucion, ImporteEgreso importe) throws InventarioException {

        if (devolucion.getImporteEgreso() == null) {
            devolucion.setImporteEgreso(importe);
        }

        Date hoy = new Date();

        if (devolucion.getId() == null) {
            devolucion.setFechaAlta(hoy);
        }

        devolucion.setFechaModificacion(hoy);
    }

    @Override
    protected void antesDeCambiar(DevolucionEgreso devolucion, StatusDevolucionEgreso status) throws InventarioException {
        
        FacesUtils.requireNonNull(devolucion, "La devolución del egreso no puede ser vacía.");

        FacesUtils.requireNonNull(devolucion.getStatus(), "La devolución no tiene asociado un status.");

        FacesUtils.requireNonNull(status, "El status a cambiar no puede ser vacío.");
        
        FacesUtils.requireNonNull(status.getNombre(), "El nombre del nuevo status no puede ser vacío.");

        maquinaStatus.cambiarStatus(devolucion, status.getNombre());

        devolucion.setStatus(status);
    }

    @Override
    protected StatusDevolucionEgreso estadoInicialInicial() throws InventarioException {
        return statusBL.buscarPorNombre(STATUS_REGISTRADA);
    }

    @Override
    protected StatusDevolucionEgreso aplicable() throws InventarioException {
        return statusBL.buscarPorNombre(STATUS_APLICADA);
    }

}
