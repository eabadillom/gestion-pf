package mx.com.ferbo.modulos.egresos.business.egreso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import mx.com.ferbo.modulos.egresos.business.EgresoBaseBL;
import mx.com.ferbo.modulos.egresos.business.categreso.StatusPagoEgresoBL;
import mx.com.ferbo.modulos.egresos.business.egreso.util.MaquinaStatusPago;
import mx.com.ferbo.modulos.egresos.dao.egreso.PagoEgresoDAO;
import mx.com.ferbo.modulos.egresos.model.catsecundarios.StatusPagoEgreso;
import mx.com.ferbo.modulos.egresos.model.egreso.ImporteEgreso;
import mx.com.ferbo.modulos.egresos.model.egreso.PagoEgreso;
import mx.com.ferbo.util.BaseBL;
import mx.com.ferbo.util.InventarioException;
import mx.com.ferbo.util.ValidationUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Named
@RequestScoped
public class PagoEgresoBL extends EgresoBaseBL<PagoEgreso, ImporteEgreso, StatusPagoEgreso>
        implements BaseBL<PagoEgreso> {

    private static final Logger log = LogManager.getLogger(PagoEgresoBL.class);

    @Inject
    private PagoEgresoDAO dao;

    @Inject
    private StatusPagoEgresoBL statusBL;

    private MaquinaStatusPago maquinaStatus;

    private final String STATUS_PENDIENTE = "PENDIENTE";
    private final String STATUS_PAGADO = "PAGADO";
    private final String STATUS_PARCIAL = "PARACIAL";
    private final String STATUS_CANCELADO = "CANCELADO";
    private final String STATUS_VENCIDO = "VENCIDO";

    private StatusPagoEgreso pendiente;
    private StatusPagoEgreso pagado;
    private StatusPagoEgreso parcial;
    private StatusPagoEgreso cancelado;
    private StatusPagoEgreso vencido;

    public PagoEgresoBL() {
        try {
            construirMaquinaStatus();
        } catch (InventarioException ex) {
            log.error("Error inicializando máquina de estados", ex);
            throw new RuntimeException("Error crítico de configuración del sistema.", ex);
        }
    }

    @Override
    protected void construirMaquinaStatus() throws InventarioException {
        log.info("Inicia proceso de la construccion de la maquina de status para pago.");
        pendiente = statusBL.buscarPorNombre(STATUS_PENDIENTE);
        pagado = statusBL.buscarPorNombre(STATUS_PAGADO);
        parcial = statusBL.buscarPorNombre(STATUS_PARCIAL);
        cancelado = statusBL.buscarPorNombre(STATUS_CANCELADO);
        vencido = statusBL.buscarPorNombre(STATUS_VENCIDO);
        maquinaStatus = new MaquinaStatusPago(pendiente, pagado, parcial, cancelado, vencido);
        log.info("Finaliza proceso de la construccion de la maquina de status para pago.");
    }

    @Override
    public PagoEgreso nuevo() {
        return new PagoEgreso();
    }

    @Override
    protected String nombreCatalogo() {
        return "status de pago";
    }

    @Override
    protected String nombreHijo() {
        return "pago de egreso";
    }

    @Override
    protected String nombreHijos() {
        return "pagos de egreso";
    }

    private void validarPagoEgreso(PagoEgreso pago) throws InventarioException {

        ValidationUtils.requireNonNull(pago, "El pago no puede ser vacío");

        ValidationUtils.requireNonNull(pago.getImporteEgreso(), "El pago no tiene asociado un egreso.");

        ValidationUtils.requireNonNull(pago.getFechaAlta(), "El pago no tiene una fecha de alta asignada.");

        ValidationUtils.requireNonNull(pago.getFechaLimite(), "El pago no tiene una fecha limite de liquidacion");

        ValidationUtils.requireNonNull(pago.getFechaModificacion(), "El pago no tiene una fecha de modificación.");

        ValidationUtils.requireNonNull(pago.getImporte(), "El pago no tiene un importe.");

        ValidationUtils.requireNonNull(pago.getStatus(), "El pago no tiene asociado un status.");

    }

    private void asgingarAntesDeGuardar(ImporteEgreso egreso, PagoEgreso pago) throws InventarioException {

        Date hoy = new Date();

        if (pago.getId() == null) {
            pago.setImporteEgreso(egreso);
            pago.setFechaAlta(hoy);
            pago.setStatus(pendiente);
        }

        pago.setFechaModificacion(hoy);
    }

    private void asignarStatus(PagoEgreso pago, StatusPagoEgreso status) throws InventarioException {

        validarHijoYCatalogo(pago, status);
        ValidationUtils.requireNonNull(pago.getStatus(), "El status del pago no puede ser vacío.");

        log.info("Inicia proceso para cambiar {}: {} a {}.", nombreCatalogo(), pago.getStatus().getNombre(),
                status.getNombre());

        ejecutar(
                () -> maquinaStatus.cambiarStatus(pago, status),
                "cambiar " + nombreCatalogo());

        log.info("Finaliza procesao para cambiar {}.", nombreCatalogo());

    }

    public PagoEgreso obtenerPagoEgreso(PagoEgreso pago) {
        return BaseBL.super.nuevoOExistente(pago);
    }

    public void guardarPagoEgreso(ImporteEgreso egreso, PagoEgreso pago, StatusPagoEgreso status)
            throws InventarioException {

        validarPadreEHijo(egreso, pago);
        validarHijoYCatalogo(pago, status);

        boolean esNuevo = pago.getId() == null;

        asgingarAntesDeGuardar(egreso, pago);

        if (!esNuevo) {
            asignarStatus(pago, status);
        }

        validarPagoEgreso(pago);

        String tipoOperacion = esNuevo ? "guardar" : "actualizar";
        String mensaje = tipoOperacion + " " + nombreHijo();

        log.info("Se inicia el proceso para {}", mensaje);

        persistir(pago, mensaje);

        log.info("Se finaliza el proceso para {}", mensaje);
    }

    private void persistir(PagoEgreso pago, String mensaje) throws InventarioException {

        if (pago.getId() == null) {
            BaseBL.super.ejecutar(() -> dao.guardar(pago), mensaje);
        } else {
            BaseBL.super.ejecutar(() -> dao.actualizar(pago), mensaje);
        }

    }

    private List<StatusPagoEgreso> obtenerPagosAplicablesStatus() {
        List<StatusPagoEgreso> lst = new ArrayList<>();
        lst.add(parcial);
        lst.add(pagado);
        return lst;
    }

    public List<PagoEgreso> obtenerPagosEgresoPorStatus(
            ImporteEgreso egreso,
            List<StatusPagoEgreso> lstStatus) throws InventarioException {

        ValidationUtils.requireNonNull(egreso, nombrePadre() + " no puede ser vacío");
        ValidationUtils.requireNonNull(egreso.getId(), nombrePadre() + " no puede ser vacío.");
        ValidationUtils.requireNonEmpty(lstStatus, "La lista " + nombreCatalogo() + " no puede ser vacía");

        if (lstStatus.stream().anyMatch(Objects::isNull)) {
            throw new InventarioException("La lista " + nombreCatalogo() + " contiene elementos nulos");
        }

        return obtenerHijos(egreso, lstStatus);

    }

    @Override
    protected List<PagoEgreso> obtenerHijos(ImporteEgreso egreso, List<StatusPagoEgreso> lstStatus)
            throws InventarioException {

        Integer idEgreso = egreso.getId();

        return BaseBL.super.operar(() -> dao.buscarPorImporteEgresoYStatus(idEgreso, lstStatus),
                "cargar " + nombreHijos() + " con el status " + nombreCatalogo());
    }

    public List<PagoEgreso> obtenerPagosAplicables(ImporteEgreso egreso) throws InventarioException {
        return obtenerPagosEgresoPorStatus(egreso, obtenerPagosAplicablesStatus());
    }
}
