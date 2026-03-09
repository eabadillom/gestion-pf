package mx.com.ferbo.modulos.egresos.business.egreso;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.modulos.egresos.business.EgresoBaseBL;
import mx.com.ferbo.modulos.egresos.business.categreso.StatusEgresoBL;
import mx.com.ferbo.modulos.egresos.business.egreso.util.MaquinaStatusEgreso;
import mx.com.ferbo.modulos.egresos.dao.egreso.ImporteEgresoDAO;
import mx.com.ferbo.modulos.egresos.model.catprimarios.CatConceptoEgreso;
import mx.com.ferbo.modulos.egresos.model.catsecundarios.StatusEgreso;
import mx.com.ferbo.modulos.egresos.model.egreso.CargoEgreso;
import mx.com.ferbo.modulos.egresos.model.egreso.ConceptoEgreso;
import mx.com.ferbo.modulos.egresos.model.egreso.DevolucionEgreso;
import mx.com.ferbo.modulos.egresos.model.egreso.ImporteEgreso;
import mx.com.ferbo.modulos.egresos.model.egreso.PagoEgreso;
import mx.com.ferbo.modulos.empresa.model.EmisorCFDI;
import mx.com.ferbo.util.BaseBL;
import mx.com.ferbo.util.DAOException;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.ValidationUtils;
import mx.com.ferbo.util.InventarioException;

@Named
@RequestScoped
public class ImporteEgresoBL extends EgresoBaseBL<ImporteEgreso, ConceptoEgreso, StatusEgreso>
        implements BaseBL<ImporteEgreso> {

    private static final Logger log = LogManager.getLogger(ImporteEgresoBL.class);

    @Inject
    private ImporteEgresoDAO dao;

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
            construirMaquinaStatus();
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
    protected String nombreHijo() {
        return "el egreso";
    }

    @Override
    protected String nombreHijos() {
        return "los egresos";
    }

    @Override
    protected String nombrePadre() {
        return "el concepto de egreso";
    }

    @Override
    public String nombreCatalogo() {
        return "el status del egreso";
    }

    @Override
    protected void construirMaquinaStatus() throws InventarioException {
        log.info("Inicia el proceso que construye la maquina de status para egreso.");
        borrador = statusBL.buscarPorNombre(STATUS_BORRADOR);
        registrado = statusBL.buscarPorNombre(STATUS_REGISTRADO);
        parcial = statusBL.buscarPorNombre(STATUS_PARCIAL);
        pagado = statusBL.buscarPorNombre(STATUS_PAGADO);
        cancelado = statusBL.buscarPorNombre(STATUS_CANCELADO);
        excedente = statusBL.buscarPorNombre(STATUS_EXCEDENTE);
        por_conciliar = statusBL.buscarPorNombre(STATUS_POR_CONCILIAR);

        maquinaStatus = new MaquinaStatusEgreso(borrador, registrado, parcial, pagado, cancelado, excedente,
                por_conciliar);
        log.info("Finaliza el proceso que construye la maquina de status para egreso.");
    }

    private void asignarAntesDeGuardar(ConceptoEgreso concepto, ImporteEgreso egreso) {

        log.info("Inicia proceso de asignacion de lo esenciaal para guardar el egreso.");

        Date hoy = new Date();

        if (egreso.getId() == null) {
            log.info("Se asigna el concepto al egreso.");
            egreso.setConceptoEgreso(concepto);
            log.info("Al egreso se le asigna la fecha de alta: {}", hoy);
            egreso.setFechaAlta(hoy);
            log.info("Al areso se le asigna el status de registrado.");
            egreso.setStatus(registrado);
        }

        log.info("Se actualiza fecha actualizacion a: {}", hoy);
        egreso.setFechaModificacion(hoy);
        log.info("Finaliza proceso de asignacion de lo esenciaal para guardar el egreso.");
    }

    private void validarEgreso(ImporteEgreso egreso) throws InventarioException {
        log.info("Inicia proceso que valida las propiedades del egreso.");

        ValidationUtils.requireNonNull(egreso, "El egreso no puede ser vacío");

        ValidationUtils.requireNonNull(egreso.getConceptoEgreso(), "Debe asignar un concepto al egreso");

        ValidationUtils.requireNonNull(egreso.getEmisor(), "Debe asignar un emisor al egreso.");

        ValidationUtils.requireNonNull(egreso.getFechaAlta(), "El egreso no tiene una fecha de alta.");

        ValidationUtils.requireNonNull(egreso.getFechaModificacion(), "El egreso no tiene una fecha de modificación.");

        ValidationUtils.requireNonNull(egreso.getFechaLimitePago(),
                "El egreso no tiene una fecha para el limitie de liquidación");

        ValidationUtils.requireNonNull(egreso.getNumeroPagos(), "El egreso no tiene un número de pagos.");

        ValidationUtils.requireNonNull(egreso.getIeps(), "El egreso no tiene definido el IVA.");

        ValidationUtils.requireNonNull(egreso.getIeps(), "El egreso no tiene definido el IEPS");

        ValidationUtils.requireNonNull(egreso.getMedioPago(), "El egreso no tiene asignado una forma de pago.");

        ValidationUtils.requireNonNull(egreso.getMetodoPago(), "El egreso no tiene definido un método de pago.");

        ValidationUtils.requireNonNull(egreso.getSubTotal(), "El sub total del egreso no esta definido.");

        ValidationUtils.requireNonNull(egreso.getTotal(), "El total del egreso no esta definido.");

        ValidationUtils.requireNonNull(egreso.getStatus(), "El egreso no tiene un status asignado.");

        log.info("Finaliza proceso que valida las propiedades del egreso.");
    }

    public void guardarEgreso(ConceptoEgreso concepto, ImporteEgreso egreso) throws InventarioException {

        super.validarPadreEHijo(concepto, egreso);

        asignarAntesDeGuardar(concepto, egreso);

        validarEgreso(egreso);

        String tipoOperciaon = (egreso.getId() == null) ? "guardar" : "actualizar";

        String mensaje = tipoOperciaon + " " + nombreHijo();

        log.info("Se inicia el proceso para " + mensaje);
        if (egreso.getId() == null) {
            BaseBL.super.ejecutar(() -> dao.guardar(egreso), mensaje);
        } else {
            BaseBL.super.ejecutar(() -> dao.actualizar(egreso), mensaje);
        }
        log.info("Se finaliza el proceso para " + mensaje);

    }

    private void asignarStatus(ImporteEgreso egreso, StatusEgreso status) throws InventarioException {

        super.validarHijoYCatalogo(egreso, status);
        ValidationUtils.requireNonNull(egreso.getStatus(), "El status del egreso no puede ser vacío.");

        log.info("Inicia proceso para camiar {}: {} a {}.", nombreCatalogo(), egreso.getStatus().getNombre(),
                status.getNombre());

        ejecutar(
                () -> maquinaStatus.cambiarStatus(egreso, status),
                "cambiar " + nombreCatalogo());

        log.info("Finaliza procesao para cambiar el status del egreso.");
    }

    public List<ImporteEgreso> obtenerPorFiltros(
            CatConceptoEgreso concepto,
            EmisorCFDI emisor,
            YearMonth mes)
            throws InventarioException, DAOException {

        log.info("Inicia proceso para obtener los egresos por filtros.");
        ValidationUtils.requireNonNull(concepto, "El concepto no puede ser vacio");
        ValidationUtils.requireNonNull(concepto.getId(), "El concepto aún no se encuentra guardado.");
        ValidationUtils.requireNonNull(emisor, "El emisor no puede ser vacío. ");
        ValidationUtils.requireNonNull(emisor.getId(), "El emisor aún no se encuentra guardado.");
        ValidationUtils.requireNonNull(mes, "La el periodo no puede ser vacío.");

        LocalDate lInicio = mes.atDay(1);
        LocalDate lFin = mes.atEndOfMonth();

        Date inicio = DateUtil.toDate(lInicio);
        Date fin = DateUtil.toDate(lFin);

        Integer idConcepto = concepto.getId();
        Integer idEmisor = emisor.getId();

        String detalle = (concepto == null || concepto == null)
                ? "del mes " + mes
                : "con concepto." + concepto.getNombre();

        String mensaje = "obtener " + nombreHijos() + " " + detalle;

        log.info("Finaliza proceso para obtener los egresos por filtros.");
        return BaseBL.super.operar(() -> dao.buscarPorFiltros(inicio, fin, idConcepto, idEmisor), mensaje);

    }

    public ImporteEgreso obtenerEgreso(Integer id) throws InventarioException {
        if (id == null) {
            return nuevo();
        }

        return BaseBL.super.operar(
                () -> dao.buscarPorId(id).orElseThrow(
                        () -> new InventarioException("No se encontro ningun egreso con el identificador: " + id)),
                "se obtuvo el egreso seleccionado.");

    }

    public void desglosarIvaIeps(ImporteEgreso egreso) throws InventarioException {

        log.info("Incia proceso para desglosar y asignar el IVA e IPES en caso de ser necesario.");
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

        BigDecimal factor = BigDecimal.ONE.add(tasaIEPS).multiply(BigDecimal.ONE.add(tasaIVA));

        BigDecimal base = total.divide(factor, 6, RoundingMode.HALF_UP);

        BigDecimal ieps = base.multiply(tasaIEPS);
        BigDecimal iva = base.add(ieps).multiply(tasaIVA);

        egreso.setIeps(ieps.setScale(2, RoundingMode.HALF_UP));
        egreso.setIva(iva.setScale(2, RoundingMode.HALF_UP));

        log.info("El egreso quedo con importe de IVA de: {}.", egreso.getIva());
        log.info("El egreso quedo con un impor de IEPS de: {}.", egreso.getIeps());

        log.info("Finaliza proceso para desglosar y asignar el IVA e IPES.");
    }

    private StatusEgreso determinarStatus(ImporteEgreso importe, BigDecimal totalCalculado) {
        log.info("Inicia proceso para determinar el status que se le asignara al egreso.");
        BigDecimal totalConcepto = importe.getConceptoEgreso().getTotalConceptoEgreso();
        log.info("Total contractuado: {}, Total calculado: {}", totalConcepto, totalCalculado);
        if (totalCalculado.compareTo(totalConcepto) == 0) {
            return pagado;
        } else if (totalCalculado.compareTo(totalConcepto) > 0) {
            return excedente;
        } else {
            return parcial;
        }
    }

    public void procesarEgreso(
            ImporteEgreso egreso,
            List<PagoEgreso> pagos,
            List<CargoEgreso> cargos,
            List<DevolucionEgreso> devoluciones) throws InventarioException {

        ejecutar(() -> {

            log.info("Inicia calculo de subtotal del egreso.");
            BigDecimal subTotal = pagos.stream()
                    .map(PagoEgreso::getImporte)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            egreso.setSubTotal(subTotal);
            log.info("Finaliza calculo de subtotal del egreso obteniendo: {}.", egreso.getSubTotal());

            BigDecimal total = subTotal;

            log.info("Inicia calculo de total del egreso.");
            total = total.add(
                    cargos.stream()
                            .map(c -> c.getImporteCargo().add(c.getImporteIEPS().add(c.getImporteIVA())))
                            .reduce(BigDecimal.ZERO, BigDecimal::add));

            total = total.subtract(
                    devoluciones.stream()
                            .map(DevolucionEgreso::getImporteDevolucion)
                            .reduce(BigDecimal.ZERO, BigDecimal::add));

            total = total.add(egreso.getIeps()).add(egreso.getIva());

            egreso.setTotal(total);
            log.info("Finaliza calaculo de total del egreso obteniendo: {}.", egreso.getTotal());

            StatusEgreso status = determinarStatus(egreso, total);
            log.info("Finaliza proceso para determinar el status que se le asignara al egreso.");

            asignarStatus(egreso, status);

        }, "procesar egreso completo");
    }

    @Override
    protected List<ImporteEgreso> obtenerHijos(ConceptoEgreso father, List<StatusEgreso> catalog)
            throws InventarioException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'obtenerHijos'");
    }
}
