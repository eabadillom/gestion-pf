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
import mx.com.ferbo.modulos.egresos.factory.PagoEgresoFactory;
import mx.com.ferbo.modulos.egresos.model.Egreso;
import mx.com.ferbo.modulos.egresos.model.catsecundarios.StatusPagoEgreso;
import mx.com.ferbo.modulos.egresos.model.egreso.ImporteEgreso;
import mx.com.ferbo.modulos.egresos.model.egreso.PagoEgreso;
import mx.com.ferbo.util.BaseBL;
import mx.com.ferbo.util.InventarioException;
import mx.com.ferbo.util.MaquinaStatusBase;
import mx.com.ferbo.util.validation.Notification;
import mx.com.ferbo.util.validation.ValidationException;
import mx.com.ferbo.util.validation.Validator;

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
    private PagoEgresoFactory factory;

    private MaquinaStatusBase<StatusPagoEgreso> maquina;

    public PagoEgresoBL() {
        try {
            maquina = factory.crearMaquina();
        } catch (InventarioException ex) {
            log.error("Error inicializando máquina de estados", ex);
            throw new RuntimeException("Error crítico de configuración del sistema.", ex);
        }
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

    private boolean esNuevo(PagoEgreso pago) {
        return pago.getId() == null;
    }

    private void validarPagoEgreso(PagoEgreso pago) throws ValidationException {

        Notification notification = new Notification();
        Validator validator = Validator.of(notification);

        validator.notNull(pago, "pago")
                .notNull(pago.getImporte(), "importe")
                .notNull(pago.getFechaAlta(), "fechaAlta")
                .notNull(pago.getFechaLimite(), "fechaLimite")
                .notNull(pago.getStatus(), "status");

        if (notification.hasErrors()) {
            throw new ValidationException(notification);
        }
    }

    private void asignarAntesDeGuardar(PagoEgreso pago, ImporteEgreso egreso, StatusPagoEgreso statusInicial) {
        log.info("Inicia proceso de asignación de lo esencial para guardar el pago del egreso.");

        Date hoy = new Date();

        if (esNuevo(pago)) {
            log.info("Asignando egreso, fecha de alta y status inicial");
            pago.setImporteEgreso(egreso);
            pago.setFechaAlta(hoy);
            pago.setStatus(statusInicial); // <- ahora viene por parámetro
        }

        // Fecha de modificación siempre se actualiza
        log.info("Actualizando fecha de modificación a: {}", hoy);
        pago.setFechaModificacion(hoy);

        log.info("Finaliza proceso de asignación de lo esencial.");
    }

    private void asignarStatus(PagoEgreso pago, StatusPagoEgreso status) throws InventarioException {
        validarHijoYCatalogo(pago, status);

        ValidationException.requireNonNull(pago.getStatus(), "El status del pago no puede ser vacío.");

        log.info("Inicia cambio de {}: {} -> {}", nombreCatalogo(), pago.getStatus().getNombre(), status.getNombre());

        maquina.conTransicionValida(pago.getStatus(), status, () -> pago.setStatus(status));

        log.info("Finaliza cambio de {}.", nombreCatalogo());
    }

    public PagoEgreso obtenerPagoEgreso(PagoEgreso pago) {
        return nuevoOExistente(pago);
    }

    private void guardarPagoEgreso(ImporteEgreso egreso, PagoEgreso pago, StatusPagoEgreso status)
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

        if (pago.getId() == null) {
            dao.guardar(pago);
        } else {
            dao.actualizar(pago);
        }

        log.info("Se finaliza el proceso para {}", mensaje);
    }

    public void persistirPagoEgreso(ImporteEgreso egreso, PagoEgreso pago, StatusPagoEgreso status)
            throws InventarioException {
        ejecutar(() -> guardarPagoEgreso(egreso, pago, status), "operar" + nombreHijo());
    }

    private List<StatusPagoEgreso> obtenerPagosAplicablesStatus() {
        List<StatusPagoEgreso> lst = new ArrayList<>();
        lst.add(parcial);
        lst.add(pagado);
        return lst;
    }

    public List<PagoEgreso> obtenerPagosEgresoPorStatus(
            ImporteEgreso egreso,
            StatusPagoEgreso status) throws InventarioException {

        ValidationException.requireNonNull(egreso, nombrePadre() + " no puede ser vacío");
        ValidationException.requireNonNull(egreso.getId(), nombrePadre() + " no puede ser vacío.");

        List<StatusPagoEgreso> lstStatus;

        if (status == null) {
            lstStatus = obtenerPagosAplicablesStatus();
        } else {
            lstStatus = new ArrayList<>();
            lstStatus.add(status);
        }
        Integer idEgreso = egreso.getId();

        return operar(() -> dao.buscarPorImporteEgresoYStatus(idEgreso, lstStatus),
                "cargar la pago por su egreso y status");

    }

}
