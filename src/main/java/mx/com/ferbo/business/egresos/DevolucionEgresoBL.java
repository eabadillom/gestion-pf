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

    private StatusDevolucionEgreso registrada;
    private StatusDevolucionEgreso autorizada;
    private StatusDevolucionEgreso aplicada;
    private StatusDevolucionEgreso rechazada;
    private StatusDevolucionEgreso cancelada;
    private StatusDevolucionEgreso en_proceso;

    @Inject
    private DevolucionEgresoDAO dao;

    @Inject
    private StatusDevolucionEgresoBL statusBL;

    private MaquinaStatusDevolucion maquinaStatus;

    @PostConstruct
    public void init() {

        try {
            setDao(dao);
            registrada = statusBL.buscarPorNombre(STATUS_REGISTRADA);
            autorizada = statusBL.buscarPorNombre(STATUS_AUTORIZADA);
            aplicada = statusBL.buscarPorNombre(STATUS_APLICADA);
            rechazada = statusBL.buscarPorNombre(STATUS_RECHAZADA);
            cancelada = statusBL.buscarPorNombre(STATUS_CANCELADA);
            en_proceso = statusBL.buscarPorNombre(STATUS_EN_PROCESO);
            maquinaStatus = new MaquinaStatusDevolucion(registrada, autorizada, aplicada, rechazada, cancelada,
                    en_proceso);
        } catch (InventarioException ex) {
            log.error("Error inicializando máquina de estados", ex);
            throw new RuntimeException("Error crítico de configuración del sistema.", ex);
        }
    }

    @Override
    public DevolucionEgreso nuevo() {
        return new DevolucionEgreso();
    }

    @Override
    public String nombreHijo() {
        return "la devolución del pago";
    }

    @Override
    public String nombreHijos() {
        return "las devoluciones del pago";
    }

    @Override
    public String nombreCatalogo() {
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

        Date hoy = new Date();

        if (devolucion.getId() == null) {
            devolucion.setImporteEgreso(importe);
            devolucion.setFechaAlta(hoy);
            devolucion.setStatus(registrada);
        }

        devolucion.setFechaModificacion(hoy);
    }

    @Override
    protected void antesDeCambiar(DevolucionEgreso devolucion, StatusDevolucionEgreso status)
            throws InventarioException {

        FacesUtils.requireNonNull(devolucion, "La devolución del egreso no puede ser vacía.");
        FacesUtils.requireNonNull(status, "El nuevo status para la devolución no puede ser vacío.");

        maquinaStatus.cambiarStatus(devolucion, status);

    }

    public StatusDevolucionEgreso aplicable() throws InventarioException {
        return autorizada;
    }

    @Override
    protected StatusDevolucionEgreso statusInicial() {
        return registrada;
    }

}
