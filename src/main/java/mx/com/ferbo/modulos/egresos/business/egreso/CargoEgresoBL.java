package mx.com.ferbo.modulos.egresos.business.egreso;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import mx.com.ferbo.modulos.egresos.business.EgresoBaseBL;
import mx.com.ferbo.modulos.egresos.business.categreso.StatusCargoEgresoBL;
import mx.com.ferbo.modulos.egresos.business.egreso.util.MaquinaStatusCargo;
import mx.com.ferbo.modulos.egresos.dao.egreso.CargoEgresoDAO;
import mx.com.ferbo.modulos.egresos.model.catsecundarios.StatusCargoEgreso;
import mx.com.ferbo.modulos.egresos.model.egreso.CargoEgreso;
import mx.com.ferbo.modulos.egresos.model.egreso.ImporteEgreso;
import mx.com.ferbo.util.BaseBL;
import mx.com.ferbo.util.DAOException;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.InventarioException;
import mx.com.ferbo.util.validation.IntegerValidationUtils;
import mx.com.ferbo.util.validation.ValidationException;
import mx.com.ferbo.util.validation.helpers.MonetaryValidator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Named
@RequestScoped
public class CargoEgresoBL extends EgresoBaseBL<CargoEgreso, ImporteEgreso, StatusCargoEgreso>
        implements BaseBL<CargoEgreso> {

    private static final Logger log = LogManager.getLogger(CargoEgresoBL.class);

    private final String STATUS_PENDIENTE = "PENDIENTE";
    private final String STATUS_APLICADO = "APLICADO";
    private final String STATUS_PAGADO = "PAGADO";
    private final String STATUS_CANCELADO = "CANCELADO";
    private final String STATUS_CONDONADO = "CONDONADO";

    private StatusCargoEgreso pendiente;
    private StatusCargoEgreso aplicado;
    private StatusCargoEgreso pagado;
    private StatusCargoEgreso cancelado;
    private StatusCargoEgreso condonado;

    @Inject
    private CargoEgresoDAO dao;

    @Inject
    private StatusCargoEgresoBL statusBL;

    private MaquinaStatusCargo maquinaStatus;

    public CargoEgresoBL() {
        try {
            construirMaquinaStatus();
        } catch (InventarioException ex) {
            log.error("Error inicializando máquina de estados", ex);
            throw new RuntimeException("Error crítico de configuración del sistema.", ex);
        }
    }

    @Override
    public CargoEgreso nuevo() {
        return new CargoEgreso();
    }

    @Override
    protected void construirMaquinaStatus() throws InventarioException {
        log.info("Inicia el proceso para construir la maquina de status para cargo de egreso.");
        pendiente = statusBL.buscarPorNombre(STATUS_PENDIENTE);
        aplicado = statusBL.buscarPorNombre(STATUS_APLICADO);
        pagado = statusBL.buscarPorNombre(STATUS_PAGADO);
        cancelado = statusBL.buscarPorNombre(STATUS_CANCELADO);
        condonado = statusBL.buscarPorNombre(STATUS_CONDONADO);
        maquinaStatus = new MaquinaStatusCargo(pendiente, aplicado, pagado, cancelado, condonado);
        log.info("Finaliza el proceso para construir la maquina de status para cargo de egreso.");
    }

    @Override
    protected String nombreHijo() {
        return "cargo de egreso";
    }

    @Override
    protected String nombreHijos() {
        return "cargos de egreso";
    }

    @Override
    protected String nombreCatalogo() {
        return "status de cargo egreso";
    }

    public CargoEgreso obtenerCargoEgreso(CargoEgreso cargo) {
        return nuevoOExistente(cargo);
    }

    private void validarCargoEgreso(CargoEgreso cargo) throws InventarioException {

        log.info("Inicia el proceso para validar el cargo del egreso.");

        ValidationException.requireNonNull(cargo, "El cargo no puede ser vacío.");

        ValidationException.requireNonNull(cargo.getImporteEgreso(), "El cargo no tiene asociado un egreso.");

        ValidationException.requireNonNull(cargo.getTipoCargo(), "El cargo no tiene asosciado ningun tipo de cargo.");

        MonetaryValidator.requireNonNull(cargo.getImporteCargo(), "El importe del cargo");

        MonetaryValidator.requirePositive(cargo.getImporteCargo(), "El importe del cargo");

        ValidationException.requireNonNull(cargo.getStatus(), "El cargo no tiene un status asociado.");

        log.info("Finaliza el proceso para validar el cargo del egreso.");
    }

    private void asignarAntesDeGuardar(ImporteEgreso egreso, CargoEgreso cargo) throws InventarioException {

        log.info("Inicia proceso de asignacion de lo esencial para guardar el cargo del egreso.");

        Date hoy = new Date();

        if (cargo.getId() == null) {
            log.info("Se asigna el egreso al cargo.");
            cargo.setImporteEgreso(egreso);
            log.info("Al cargo se le asigna la fecha de alta: {}", hoy);
            cargo.setFechaAlta(hoy);
            log.info("Al egreso se le asigna el status de pendiente.");
            cargo.setStatus(pendiente);
        }

        log.info("Se actualiza fecha de actualizacion a: {}", hoy);
        cargo.setFechaModificacion(hoy);

        log.info("Finaliza proceso de asignacion de lo esencial para guardar el cargo del egreso.");
    }

    private void asignarStatus(CargoEgreso cargo, StatusCargoEgreso status) throws InventarioException {
        validarHijoYCatalogo(cargo, status);
        ValidationException.requireNonNull(cargo.getStatus(), "El status del egreso no puede ser vacío.");

        log.info("Inicia proceso para camiar {}: {} a {}.", nombreCatalogo(), cargo.getStatus().getNombre(),
                status.getNombre());

        ejecutar(
                () -> maquinaStatus.cambiarStatus(cargo, status),
                "cambiar " + nombreCatalogo());

        log.info("Finaliza procesao para cambiar {}.", nombreCatalogo());
    }

    public void calcularDias(CargoEgreso cargo) throws InventarioException {
        cargo.setNumeroDias(obtenerNumeroDias(cargo));
    }

    private int obtenerNumeroDias(CargoEgreso cargo) throws InventarioException {

        ValidationException.requireNonNull(cargo, "El cargo no puede ser nulo.");

        if (cargo.getFechaAplicacion() == null || cargo.getFechaCalculo() == null) {
            log.warn("No se pueden calcular los días porque alguna fecha es nula.");
            return 0;
        }

        return DateUtil.daysDiff(
                cargo.getFechaAplicacion(),
                cargo.getFechaCalculo());
    }

    public void calcularImporteCargo(CargoEgreso cargo) throws InventarioException {
        cargo.setImporteCargo(obtenerImporteCargo(cargo));
    }

    private BigDecimal obtenerImporteCargo(CargoEgreso cargo) throws InventarioException {

        ValidationException.requireNonNull(cargo, "El cargo no puede ser vacío");

        ValidationException.requireNonNull(cargo.getImporteEgreso(), "El egreso asociado al egreso no puede ser vacío");

        ValidationException.requireNonNull(cargo.getImporteEgreso().getConceptoEgreso(),
                "El concepto del egreso no puede ser vacío.");

        ValidationException.requireNonNull(cargo.getImporteEgreso().getConceptoEgreso().getTotalConceptoEgreso(),
                "El egreso no tiene el total contractuado.");

        MonetaryValidator.requireNonNull(cargo.getPorcentajeTasa(), "El porcentaje de tasa del cargo");
        MonetaryValidator.requirePositive(cargo.getPorcentajeTasa(), "La tasa del cargo");

        IntegerValidationUtils.requireNonNull(cargo.getNumeroDias(), "El número de días del cargo");
        IntegerValidationUtils.requirePositive(cargo.getNumeroDias(), "El número de días del cargo");

        BigDecimal importe = cargo.getImporteEgreso().getConceptoEgreso().getTotalConceptoEgreso();
        BigDecimal tasa = cargo.getPorcentajeTasa();
        BigDecimal dias = BigDecimal.valueOf(cargo.getNumeroDias());

        BigDecimal base = new BigDecimal("36000");

        return importe
                .multiply(tasa)
                .multiply(dias)
                .divide(base, 2, RoundingMode.HALF_UP);
    }

    public void guardarCargoEgreso(ImporteEgreso egreso, CargoEgreso cargo, StatusCargoEgreso status)
            throws InventarioException {

        validarPadreEHijo(egreso, cargo);
        validarHijoYCatalogo(cargo, status);

        boolean esNuevo = cargo.getId() == null;

        asignarAntesDeGuardar(egreso, cargo);

        if (!esNuevo) {
            asignarStatus(cargo, status);
        }

        validarCargoEgreso(cargo);

        String tipoOperacion = esNuevo ? "guardar" : "actualizar";
        String mensaje = tipoOperacion + " " + nombreHijo();

        log.info("Se inicia el proceso para {}", mensaje);

        if (cargo.getId() == null) {
            dao.guardar(cargo);
        } else {
            dao.actualizar(cargo);
        }

        log.info("Se finaliza el proceso para {}", mensaje);

    }

    private List<StatusCargoEgreso> obtenerCargosAplicablesStatus() {
        List<StatusCargoEgreso> lst = new ArrayList<>();
        lst.add(pagado);
        return lst;
    }

    private List<CargoEgreso> obtenerCargosEgresoPorEstatus(ImporteEgreso egreso,
            StatusCargoEgreso status) throws InventarioException, DAOException {
        ValidationException.requireNonNull(egreso, nombrePadre() + " no puede ser vacío");
        ValidationException.requireNonNull(egreso.getId(), nombrePadre() + " no puede ser vacío.");

        List<StatusCargoEgreso> lst;

        if (status == null) {
            lst = obtenerCargosAplicablesStatus();
        } else {
            lst = new ArrayList<>();
            lst.add(status);
        }

        Integer idEgreso = egreso.getId();

        return dao.buscarPorImporteEgresoYStatus(idEgreso, lst);

    }

    public List<CargoEgreso> cargarCargosEgresoPorStatus(
            ImporteEgreso egreso,
            StatusCargoEgreso status) throws InventarioException {

        return operar(() -> obtenerCargosEgresoPorEstatus(egreso, status), "cargar" + nombreHijos() + " y con status.");

    }
}
