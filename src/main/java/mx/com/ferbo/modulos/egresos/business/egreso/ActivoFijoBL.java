package mx.com.ferbo.modulos.egresos.business.egreso;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import mx.com.ferbo.modulos.egresos.business.EgresoBaseBL;
import mx.com.ferbo.modulos.egresos.business.categreso.StatusActivoFijoBL;
import mx.com.ferbo.modulos.egresos.business.egreso.util.MaquinaStatusActivoFijo;
import mx.com.ferbo.modulos.egresos.dao.egreso.ActivoFijoDAO;
import mx.com.ferbo.modulos.egresos.model.catsecundarios.StatusActivoFijo;
import mx.com.ferbo.modulos.egresos.model.egreso.ActivoFijo;
import mx.com.ferbo.modulos.egresos.model.egreso.ImporteEgreso;
import mx.com.ferbo.util.BaseBL;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.IntegerValidationUtils;
import mx.com.ferbo.util.InventarioException;
import mx.com.ferbo.util.MonetaryValidationUtils;
import mx.com.ferbo.util.TextValidationUtils;
import mx.com.ferbo.util.ValidationUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Named
@RequestScoped
public class ActivoFijoBL extends EgresoBaseBL<ActivoFijo, ImporteEgreso, StatusActivoFijo>
        implements BaseBL<ActivoFijo> {

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
            construirMaquinaStatus();
        } catch (InventarioException ex) {
            log.error("Error inicializando máquina de estados", ex);
            throw new RuntimeException("Error crítico de configuración del sistema.", ex);
        }
    }

    @Override
    protected String nombreCatalogo() {
        return "status de activo fijo";
    }

    @Override
    protected String nombreHijo() {
        return "activo fijo";
    }

    @Override
    protected String nombreHijos() {
        return "activos fijos";
    }

    @Override
    public ActivoFijo nuevo() {
        return new ActivoFijo();
    }

    @Override
    protected void construirMaquinaStatus() throws InventarioException {
        log.info("Inicia el proceso que construye la maquina de status para el acivo fijo.");
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
        maquinaStatus = new MaquinaStatusActivoFijo(en_uso, en_reparacion, inactivo, en_baja, vendido, obsoleto,
                dado_de_baja, en_revaluacion, recibido, descartado);
        log.info("Finaliza el proceso que construye la maquina de status para el acivo fijo.");
    }

    private void validarActivoFijo(ActivoFijo activo) throws InventarioException {

        log.info("Inicia proceso para validar el activo fijo.");

        ValidationUtils.requireNonNull(activo, "El activo fijo no puede ser nulo.");

        TextValidationUtils.requireNonNull(activo.getDescripcion(), "La descripción del activo fijo");

        TextValidationUtils.requireNonBlank(activo.getDescripcion(), "La descripción del activo fijo");

        MonetaryValidationUtils.requireNonNull(activo.getImporte(), "El importe del activo fijo");

        MonetaryValidationUtils.requirePositive(activo.getImporte(), "El importe del activo fijo");

        IntegerValidationUtils.requireNonNull(activo.getVidaUtil(), "La vida util del activo fijo");

        IntegerValidationUtils.requirePositive(activo.getVidaUtil(), "La vida util del activo fijo");

        Date fecha = activo.getFechaAdquisicion();

        Date hoy = new Date();

        DateUtil.resetTime(fecha);

        DateUtil.resetTime(hoy);

        ValidationUtils.requireNonNull(fecha, "La fecha de adquisición no puede ser nula.");

        if (fecha.after(hoy)) {
            throw new InventarioException("La fecha de adquisición no puede ser una fecha a futuro.");
        }

        ValidationUtils.requireNonNull(activo.getFechaAlta(), "El activo fijo no tiene una fecha de alta.");

        ValidationUtils.requireNonNull(activo.getFechaModificacion(),
                "El activo fijo no tiene una fecha de modificación.");

        ValidationUtils.requireNonNull(activo.getStatus(), "El activo fijo no tiene asociado un status.");

        log.info("Finaliza proceso para validar el activo fijo.");
    }

    private void agsignarAntesDeGuardar(ImporteEgreso importe, ActivoFijo activo) throws InventarioException {

        log.info("Inicia proceso de asignacion de lo esenciaal para guardar el activo fijo.");

        Date hoy = new Date();

        if (activo.getId() == null) {
            log.info("Se asigna el egreso al activo fijo.");
            activo.setImporteEgreso(importe);
            log.info("Al activo fijo se le asigna la fecha de alta: {}", hoy);
            activo.setFechaAlta(hoy);
            log.info("Al activo fijo se le asigna el status de registrado.");
            activo.setStatus(recibido);
        }
        log.info("Se actualiza fecha de actualizacion a: {}", hoy);
        activo.setFechaModificacion(hoy);

        log.info("Finaliza proceso de asignacion de lo esencial para guardar el activo fijo.");
    }

    private void asignarStatus(ActivoFijo activo, StatusActivoFijo status) throws InventarioException {

        validarHijoYCatalogo(activo, status);
        ValidationUtils.requireNonNull(activo.getStatus(), "El status de activo fijo es nulo.");

        log.info("Inicia proceso para camiar {}: {} a {}.", nombreCatalogo(), activo.getStatus().getNombre(),
                status.getNombre());
        ejecutar(() -> maquinaStatus.cambiarStatus(activo, status), "cambiar" + nombreCatalogo());

        log.info("Finaliza procesao para cambiar {}.", nombreCatalogo());
    }

    public void guardarActivoFijo(ImporteEgreso egreso, ActivoFijo activo, StatusActivoFijo status)
            throws InventarioException {
        validarPadreEHijo(egreso, activo);
        validarHijoYCatalogo(activo, status);

        boolean esNuevo = activo.getId() == null;

        agsignarAntesDeGuardar(egreso, activo);

        if (!esNuevo) {
            asignarStatus(activo, status);
        }

        validarActivoFijo(activo);

        String tipoOperacion = esNuevo ? "guardar" : "actualizar";
        String mensaje = tipoOperacion + " " + nombreHijo();

        log.info("Se inicia el proceso para {}", mensaje);

        if (activo.getId() == null) {
            dao.guardar(activo);
        } else {
            dao.actualizar(activo);
        }

        log.info("Se finaliza el proceso para {}", mensaje);
    }

    public ActivoFijo obtenerActivoFijo(ActivoFijo activo) {
        return nuevoOExistente(activo);
    }

    public List<ActivoFijo> obtenrActivoFijoPorEgreso(ImporteEgreso egreso) throws InventarioException {

        ValidationUtils.requireNonNull(egreso, nombrePadre() + " no puede ser vacío");
        ValidationUtils.requireNonNull(egreso.getId(), nombrePadre() + " no está guardado en el sistema.");

        Integer idEgreso = egreso.getId();

        return operar(() -> dao.buscarPorImporteEgreso(idEgreso), "cargar " + nombreHijos());
    }
}
