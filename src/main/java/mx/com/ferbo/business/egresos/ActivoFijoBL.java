package mx.com.ferbo.business.egresos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import mx.com.ferbo.business.categresos.StatusActivoFijoBL;
import mx.com.ferbo.business.egresos.util.MaquinaStatusActivoFijo;
import mx.com.ferbo.dao.egresos.ActivoFijoDAO;
import mx.com.ferbo.model.categresos.StatusActivoFijo;
import mx.com.ferbo.model.egresos.ActivoFijo;
import mx.com.ferbo.model.egresos.ImporteEgreso;
import mx.com.ferbo.util.FacesUtils;
import mx.com.ferbo.util.InventarioException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Named
@RequestScoped
public class ActivoFijoBL extends EgresoBaseBL<ActivoFijo, ImporteEgreso, StatusActivoFijo> {

    private static final Logger log = LogManager.getLogger();

    @Inject
    private ActivoFijoDAO dao;

    @Inject
    private StatusActivoFijoBL statusBL;

    private MaquinaStatusActivoFijo maquinaStatus;

    private final String STATUS_EN_USO = "EN_USO";
    private final String STATUS_EN_REPARACION = "EN_REPARACIÓN/MANTENIMIENTO";
    private final String STATUS_INACTIVO = "INACTIVO";
    private final String STATUS_EN_BAJA = "EN_BAJA";
    private final String STATUS_VENDIDO = "VENDIDO";
    private final String STATUS_OBSOLETO = "OBSOLETO";
    private final String STATUS_DADO_DE_BAJA = "DADO_DE_BAJA";
    private final String STATUS_EN_REVALUACION = "EN_REVALUACIÓN";
    private final String STATUS_RECIBIDO = "RECIBIDO";
    private final String STATUS_DESCARTADO = "DESCARTADO";

    private StatusActivoFijo en_uso;
    private StatusActivoFijo en_reparacion;
    private StatusActivoFijo inactivo;
    private StatusActivoFijo en_baja;
    private StatusActivoFijo vendido;
    private StatusActivoFijo obsoleto;
    private StatusActivoFijo dado_de_baja;
    private StatusActivoFijo en_revaluacion;
    private StatusActivoFijo recibido;
    private StatusActivoFijo descartado;

    public ActivoFijoBL() {
        try {
        setDao(dao);
        en_uso = statusBL.buscarPorNombre(STATUS_EN_USO);
        en_reparacion = statusBL.buscarPorNombre(STATUS_EN_REPARACION);
        inactivo = statusBL.buscarPorNombre(STATUS_INACTIVO);
        en_baja = statusBL.buscarPorNombre(STATUS_EN_BAJA);
        vendido = statusBL.buscarPorNombre(STATUS_VENDIDO);
        obsoleto = statusBL.buscarPorNombre(STATUS_OBSOLETO);
        dado_de_baja = statusBL.buscarPorNombre(STATUS_DADO_DE_BAJA);
        en_revaluacion = statusBL.buscarPorNombre(STATUS_EN_REVALUACION);
        recibido = statusBL.buscarPorNombre(STATUS_RECIBIDO);
        descartado = statusBL.buscarPorNombre(STATUS_DESCARTADO);
        maquinaStatus = new MaquinaStatusActivoFijo(en_uso, en_reparacion, inactivo, en_baja, vendido, obsoleto, dado_de_baja, en_revaluacion, recibido, descartado);
        } catch (InventarioException ex) {
            log.error("Error inicializando máquina de estados", ex);
            throw new RuntimeException("Error crítico de configuración del sistema.", ex);
        }
    }

    @Override
    protected ActivoFijo nuevo() {
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

        Date hoy = new Date();

        if (activo.getId() == null) {
            activo.setImporteEgreso(importe);
            activo.setFechaAlta(hoy);
            activo.setStatus(recibido);
        }

        activo.setFechaModificacion(hoy);
    }

    @Override
    protected void antesDeCambiar(ActivoFijo activo, StatusActivoFijo status) throws InventarioException {
        
        FacesUtils.requireNonNull(activo, "El activo fijo no puede ser vacío.");
        FacesUtils.requireNonNull(status, "El nuevo status para el activo fijo no puede ser vacío.");
        
        maquinaStatus.cambiarStatus(activo, status);
    }
}
