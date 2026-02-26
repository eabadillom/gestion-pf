package mx.com.ferbo.business.egresos;

import java.util.Date;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import mx.com.ferbo.business.categresos.StatusPagoEgresoBL;
import mx.com.ferbo.business.egresos.util.MaquinaStatusPago;
import mx.com.ferbo.dao.egresos.PagoEgresoDAO;
import mx.com.ferbo.model.categresos.StatusPagoEgreso;
import mx.com.ferbo.model.egresos.ImporteEgreso;
import mx.com.ferbo.model.egresos.PagoEgreso;
import mx.com.ferbo.util.FacesUtils;
import mx.com.ferbo.util.InventarioException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Named
@RequestScoped
public class PagoEgresoBL extends EgresoBaseBL<PagoEgreso, ImporteEgreso, StatusPagoEgreso> {

    private static final Logger log = LogManager.getLogger(PagoEgresoBL.class);

    @Inject
    private PagoEgresoDAO dao;

    @Inject
    private ImporteEgresoBL importeBL;

    @Inject
    private CargoEgresoBL cargoBL;

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
            setDao(dao);
            pendiente = statusBL.buscarPorNombre(STATUS_PENDIENTE);
            pagado = statusBL.buscarPorNombre(STATUS_PAGADO);
            parcial = statusBL.buscarPorNombre(STATUS_PARCIAL);
            cancelado = statusBL.buscarPorNombre(STATUS_CANCELADO);
            vencido = statusBL.buscarPorNombre(STATUS_VENCIDO);
            maquinaStatus = new MaquinaStatusPago(pendiente, pagado, parcial, cancelado, vencido);
        } catch (InventarioException ex) {
            throw new RuntimeException();
        }
    }

    @Override
    protected PagoEgreso nuevo() {
        return new PagoEgreso();
    }

    @Override
    protected String nombreHijo() {
        return "el pago";
    }

    @Override
    protected String nombreHijos() {
        return "los pagos";
    }

    @Override
    protected void validar(PagoEgreso pago) throws InventarioException {
        if (pago == null) {
            throw new InventarioException("El pago no puede ser vacío");
        }

        if (pago.getImporteEgreso() == null) {
            throw new InventarioException("El pago no tiene asociado un egreso.");
        }

        if (pago.getFechaAlta() == null) {
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

            if ("".equalsIgnoreCase(pago.getReferencia())) {
                throw new InventarioException("El pago no tiene ninguna referencia.");
            }
        }

    }

    @Override
    protected void antesDeGuardar(PagoEgreso pago, ImporteEgreso importe) throws InventarioException {

        Date hoy = new Date();

        if (pago.getId() == null) {
            pago.setImporteEgreso(importe);
            pago.setFechaAlta(hoy);
            pago.setStatus(estadoInicialInicial());
        }

        pago.setFechaModificacion(hoy);
    }

    @Override
    public void antesDeCambiar(PagoEgreso pago, StatusPagoEgreso status) throws InventarioException {

        FacesUtils.requireNonNull(pago, "El pago del egreso no puede ser vacío.");
        FacesUtils.requireNonNull(status, "El nuevo status del pago no puede ser vacío.");

        maquinaStatus.cambiarStatus(pago, status);

        pago.setStatus(status);

    }

    @Override
    protected String nombreCatalogo() {
        return "el status";
    }

    public StatusPagoEgreso estadoInicialInicial() throws InventarioException {
        return pendiente;
    }

    public StatusPagoEgreso aplicable() throws InventarioException {
        return pagado;
    }

}
