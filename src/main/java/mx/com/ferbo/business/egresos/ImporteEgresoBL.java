package mx.com.ferbo.business.egresos;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import mx.com.ferbo.business.categresos.StatusEgresoBL;
import mx.com.ferbo.business.egresos.util.MaquinaStatusEgreso;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.egresos.ImporteEgresoDAO;
import mx.com.ferbo.model.categresos.CatConceptoEgreso;
import mx.com.ferbo.model.egresos.ConceptoEgreso;
import mx.com.ferbo.model.egresos.DevolucionEgreso;
import mx.com.ferbo.model.categresos.StatusEgreso;
import mx.com.ferbo.model.egresos.CargoEgreso;
import mx.com.ferbo.model.egresos.ImporteEgreso;
import mx.com.ferbo.model.egresos.PagoEgreso;
import mx.com.ferbo.model.empresa.NEmisoresCFDIS;
import mx.com.ferbo.util.DAOException;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.ValidationUtils;
import mx.com.ferbo.util.InventarioException;

@Named
@RequestScoped
public class ImporteEgresoBL extends EgresoBaseBL<ImporteEgreso, ConceptoEgreso, StatusEgreso> {

    private static final Logger log = LogManager.getLogger(ImporteEgresoBL.class);

    @Inject
    private ImporteEgresoDAO dao;

    @Inject
    private PagoEgresoBL pagoBL;

    @Inject
    private CargoEgresoBL cargoBL;

    @Inject
    private DevolucionEgresoBL devolucionBL;

    @Inject
    private StatusEgresoBL statusBL;

    MaquinaStatusEgreso maquinaStatus;

    private static final BigDecimal CIEN = new BigDecimal("100");

    private final String STATUS_BORRADOR = "BORRADOR";
    private final String STATUS_REGISTRADO = "REGISTRADO";
    private final String STATUS_PARCIAL = "PARACIAL";
    private final String STATUS_PAGADO = "PAGADO";
    private final String STATUS_CANCELADO = "CANCELADO";
    private final String STATUS_EXCEDENTE = "EXCEDENTE";
    private final String STATUS_POR_CONCILIAR = "POR_CONCILIAR";

    private StatusEgreso borrador;
    private StatusEgreso registrado;
    private StatusEgreso parcial;
    private StatusEgreso pagado;
    private StatusEgreso cancelado;
    private StatusEgreso excedente;
    private StatusEgreso por_conciliar;

    public ImporteEgresoBL() {
        try {
            setDao(dao);
            borrador = statusBL.buscarPorNombre(STATUS_BORRADOR);
            registrado = statusBL.buscarPorNombre(STATUS_REGISTRADO);
            parcial = statusBL.buscarPorNombre(STATUS_PARCIAL);
            pagado = statusBL.buscarPorNombre(STATUS_PAGADO);
            cancelado = statusBL.buscarPorNombre(STATUS_CANCELADO);
            excedente = statusBL.buscarPorNombre(STATUS_EXCEDENTE);
            por_conciliar = statusBL.buscarPorNombre(STATUS_POR_CONCILIAR);
            maquinaStatus = new MaquinaStatusEgreso(borrador, registrado, parcial, pagado, cancelado, excedente,
                    por_conciliar);
        } catch (InventarioException ex) {
            log.error("Error inicializando máquina de estados", ex);
            throw new RuntimeException("Error crítico de configuración del sistema.", ex);
        }
    }

    @Override
    public ImporteEgreso nuevo() {
        return new ImporteEgreso();
    }

    @Override
    public String nombreHijo() {
        return "el importe de egreso";
    }

    @Override
    public String nombreHijos() {
        return "los importe de egresos";
    }

    @Override
    protected String nombrePadre() {
        return "el concepto egreso";
    }

    @Override
    public String nombreCatalogo() {
        return "el status";
    }

    @Override
    protected void validar(ImporteEgreso egreso) throws InventarioException {
        if (egreso == null) {
            throw new InventarioException("El egreso no puede ser vacío");
        }
        if (egreso.getSubTotal() == null || egreso.getSubTotal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InventarioException("El subtotal debe ser mayor que cero");
        }
        if (egreso.getConceptoEgreso() == null) {
            throw new InventarioException("Debe asignar un concepto al egreso");
        }
    }

    @Override
    protected void antesDeGuardar(ImporteEgreso importe, ConceptoEgreso concepto) throws InventarioException {

        Date hoy = new Date();

        if (importe.getId() == null) {
            importe.setConceptoEgreso(concepto);
            importe.setFechaAlta(hoy);
            importe.setStatus(registrado);
        }

        procesarSubTotalEgreso(importe);

        procesarTotalEgreso(importe);

        importe.setFechaModificacion(hoy);
    }

    @Override
    protected void antesDeCambiar(ImporteEgreso importe, StatusEgreso status) throws InventarioException {

        ValidationUtils.requireNonNull(importe, "El egreso no puede ser vacío.");
        ValidationUtils.requireNonNull(status, "El nuevo status para el egreso no puede ser vacío.");

        maquinaStatus.cambiarStatus(importe, status);

    }

    @Override
    protected StatusEgreso statusInicial() {
        return borrador;
    }

    public ImporteEgreso nuevoOExistente(Integer id) throws InventarioException {
        ImporteEgreso egreso;
        if (id == null) {
            egreso = nuevo();
            egreso.setStatus(statusInicial());
            return egreso;
        }
        return dao.buscarPorId(id).orElseThrow(
                () -> new InventarioException("No se enecontro ningun egreso asociado con el identificador: " + id));
    }

    public List<ImporteEgreso> obtenerPorFiltros(
            CatConceptoEgreso concepto,
            NEmisoresCFDIS razon,
            YearMonth mes)
            throws InventarioException, DAOException {

        try {

            LocalDate lInicio = mes.atDay(1);
            LocalDate lFin = mes.atEndOfMonth();

            Date inicio = DateUtil.toDate(lInicio);
            Date fin = DateUtil.toDate(lFin);

            Integer idConcepto = null;
            Integer idEmisor = null;

            if (concepto != null && concepto != null) {
                idConcepto = concepto.getId();
            }

            if (razon != null) {
                idEmisor = razon.getId();
            }

            return dao.buscarPorFiltros(
                    inicio,
                    fin,
                    idConcepto,
                    idEmisor);

        } catch (DAOException ex) {

            log.warn("Error al obtener egresos: {}", ex.getMessage(), ex);

            String detalle = (concepto == null || concepto == null)
                    ? "del mes " + mes
                    : "con concepto " + concepto.getNombre();

            throw new InventarioException(
                    "Hubo un problema al obtener los egresos " + detalle);
        }
    }

    public ImporteEgreso desglosarIvaIeps(ImporteEgreso egreso) throws InventarioException {

        if (egreso == null || egreso.getConceptoEgreso() == null
                || egreso.getConceptoEgreso().getTotalConceptoEgreso() == null) {
            throw new InventarioException("Datos insuficientes para desglosar impuestos.");
        }

        BigDecimal total = egreso.getConceptoEgreso().getTotalConceptoEgreso();

        BigDecimal porcentajeIEPS = egreso.getConceptoEgreso().getPorcentajeIEPS() != null
                ? egreso.getConceptoEgreso().getPorcentajeIEPS()
                : BigDecimal.ZERO;
        BigDecimal porcentajeIVA = egreso.getConceptoEgreso().getPorcentajeIVA() != null
                ? egreso.getConceptoEgreso().getPorcentajeIVA()
                : BigDecimal.ZERO;

        BigDecimal tasaIEPS = porcentajeIEPS.divide(CIEN, 6, RoundingMode.HALF_UP);
        BigDecimal tasaIVA = porcentajeIVA.divide(CIEN, 6, RoundingMode.HALF_UP);

        // Factor combinado
        BigDecimal factor = BigDecimal.ONE.add(tasaIEPS).multiply(BigDecimal.ONE.add(tasaIVA));

        // Calculamos la base
        BigDecimal base = total.divide(factor, 6, RoundingMode.HALF_UP);

        // Calculamos cada impuesto
        BigDecimal ieps = base.multiply(tasaIEPS);
        BigDecimal iva = base.add(ieps).multiply(tasaIVA);

        // Guardamos en el objeto egreso
        egreso.setIeps(ieps.setScale(2, RoundingMode.HALF_UP));
        egreso.setIva(iva.setScale(2, RoundingMode.HALF_UP));

        return egreso;
    }

    public void procesarSubTotalEgreso(ImporteEgreso importe) throws InventarioException {
        List<PagoEgreso> pagos = pagoBL.obtenerPorImporteEgresoYStatus(importe, pagoBL.aplicable());

        BigDecimal subTotal = BigDecimal.ZERO;

        for (PagoEgreso pago : pagos) {
            subTotal = subTotal.add(pago.getImporte());
        }

        BigDecimal totalAux = subTotal.add(importe.getIeps().add(importe.getIva()));

        int comparacion = totalAux.compareTo(importe.getConceptoEgreso().getTotalConceptoEgreso());

        StatusEgreso status = parcial;

        if (comparacion > 0) {
            status = por_conciliar;
        }

        if (comparacion == 0) {
            status = pagado;
        }

        antesDeCambiar(importe, status);

        importe.setSubTotal(subTotal);

    }

    public void procesarTotalEgreso(ImporteEgreso importe) throws InventarioException {

        List<CargoEgreso> cargos = cargoBL.obtenerPorImporteEgresoYStatus(importe, cargoBL.aplicable());
        List<DevolucionEgreso> devoluciones = devolucionBL.obtenerPorImporteEgresoYStatus(importe,
                devolucionBL.aplicable());

        BigDecimal subtotal = importe.getSubTotal();
        BigDecimal total = subtotal;

        for (CargoEgreso cargo : cargos) {
            total = total.add(cargo.getImporteCargo().add(cargo.getImporteIEPS().add(cargo.getImporteIVA())));
        }

        for (DevolucionEgreso devolucion : devoluciones) {
            total = total.subtract(devolucion.getImporteDevolucion());
        }

        total = total.add(importe.getIeps());

        total = total.add(importe.getIva());

        int comparacion = total.compareTo(importe.getConceptoEgreso().getTotalConceptoEgreso());

        StatusEgreso status = importe.getStatus();

        if (comparacion == 0) {
            status = pagado;
        }

        if (comparacion > 0) {
            status = excedente;
        }

        antesDeCambiar(importe, status);

        importe.setTotal(total);

    }
}
