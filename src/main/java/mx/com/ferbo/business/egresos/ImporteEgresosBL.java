package mx.com.ferbo.business.egresos;

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

import mx.com.ferbo.dao.egresos.ImporteEgresoDAO;
import mx.com.ferbo.model.catalogos.CatConceptoEgreso;
import mx.com.ferbo.model.catalogos.ConceptoEgreso;
import mx.com.ferbo.model.egresos.CargoEgreso;
import mx.com.ferbo.model.egresos.ImporteEgreso;
import mx.com.ferbo.model.egresos.PagoEgreso;
import mx.com.ferbo.model.empresa.NEmisoresCFDIS;
import mx.com.ferbo.util.DAOException;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.InventarioException;

@Named
@RequestScoped
public class ImporteEgresosBL {

    private static final Logger log = LogManager.getLogger(ImporteEgresosBL.class);

    @Inject
    private ImporteEgresoDAO importeEgresoDAO;

    public String agregarOActualizar(ImporteEgreso importeEgreso) throws InventarioException {

        String mensaje;

        try {
            if (importeEgreso.getId() == null) {
                importeEgresoDAO.guardar(importeEgreso);
                mensaje = "El egreso se agrego exitosamente.";
            } else {
                importeEgresoDAO.actualizar(importeEgreso);
                mensaje = "El egreso se actualizo exitosamente.";
            }
            return mensaje;
        } catch (InventarioException ex) {
            String estado = importeEgreso.getId() == null ? "agrego" : "actualizo";
            log.warn("El importe de egreso no se {} satisfactoriamente. {}", estado, ex);
            throw new InventarioException("El importe de egreso no se " + estado + " satisfacoriamente");
        }
    }

    public List<ImporteEgreso> obtenerPorFiltros(
            CatConceptoEgreso concepto,
            NEmisoresCFDIS razon,
            YearMonth mes)
            throws InventarioException {

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

            return importeEgresoDAO.buscarPorFiltros(
                    inicio,
                    fin,
                    idConcepto,
                    idEmisor
            );

        } catch (DAOException ex) {

            log.warn("Error al obtener egresos: {}", ex.getMessage(), ex);

            String detalle = (concepto == null || concepto == null)
                    ? "del mes " + mes
                    : "con concepto " + concepto.getNombre();

            throw new InventarioException(
                    "Hubo un problema al obtener los egresos " + detalle
            );
        }
    }

    public void validarImporteEgreso(ImporteEgreso egreso) throws InventarioException {
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

    public BigDecimal calcularSubtotalSugerido(ImporteEgreso egreso) throws InventarioException {
        if (egreso == null) {
            throw new InventarioException("El egreso no puede ser vacío");
        }

        BigDecimal subtotal = egreso.getSubTotal() != null ? egreso.getSubTotal() : BigDecimal.ZERO;

        if (egreso.getPagos() != null) {
            for (PagoEgreso pago : egreso.getPagos()) {
                if (pago.getImporte() != null) {
                    subtotal = subtotal.subtract(pago.getImporte());
                }
            }
        }

        // Nunca negativo
        if (subtotal.compareTo(BigDecimal.ZERO) < 0) {
            subtotal = BigDecimal.ZERO;
        }

        return subtotal.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calcularSubtotalConCargos(ImporteEgreso egreso) throws InventarioException {
        if (egreso == null) {
            throw new InventarioException("El egreso no puede ser vacío");
        }

        // Partimos del subtotal base
        BigDecimal subtotal = egreso.getSubTotal() != null ? egreso.getSubTotal() : BigDecimal.ZERO;

        // Sumamos los cargos
        if (egreso.getCargos() != null) {
            for (CargoEgreso cargo : egreso.getCargos()) {
                if (cargo.getImporteCargo() != null) {
                    subtotal = subtotal.add(cargo.getImporteCargo());
                }
            }
        }

        // Restamos pagos parciales si quieres que el subtotal ya sea "neto"
        if (egreso.getPagos() != null) {
            for (PagoEgreso pago : egreso.getPagos()) {
                if (pago.getImporte() != null) {
                    subtotal = subtotal.subtract(pago.getImporte());
                }
            }
        }

        // Nunca negativo
        if (subtotal.compareTo(BigDecimal.ZERO) < 0) {
            subtotal = BigDecimal.ZERO;
        }

        return subtotal.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calcularIva(BigDecimal subtotal, ConceptoEgreso concepto) {
        if (subtotal == null) {
            subtotal = BigDecimal.ZERO;
        }

        if (concepto != null && concepto.getCatConcepto().getTieneIVA() && concepto.getPorcentajeIVA() != null) {
            return subtotal.multiply(concepto.getPorcentajeIVA())
                    .setScale(2, RoundingMode.HALF_UP);
        }

        return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calcularIeps(BigDecimal subtotal, ConceptoEgreso concepto) {
        if (subtotal == null) {
            subtotal = BigDecimal.ZERO;
        }

        if (concepto != null && concepto.getCatConcepto().getTieneIEPS()
                && concepto.getPorcentajeIEPS() != null) {
            return subtotal.multiply(concepto.getPorcentajeIEPS())
                    .setScale(2, RoundingMode.HALF_UP);
        }

        return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calcularIvaSugerido(BigDecimal subtotalSugerido, ConceptoEgreso concepto) {
        return calcularIva(subtotalSugerido, concepto); // reutiliza tu función existente
    }

    public BigDecimal calcularIepsSugerido(BigDecimal subtotalSugerido, ConceptoEgreso concepto) {
        return calcularIeps(subtotalSugerido, concepto); // reutiliza tu función existente
    }

    public BigDecimal calcularTotalSugerido(BigDecimal subtotalSugerido, BigDecimal ivaSugerido,
            BigDecimal iepsSugerido) {
        BigDecimal total = (subtotalSugerido != null ? subtotalSugerido : BigDecimal.ZERO)
                .add(ivaSugerido != null ? ivaSugerido : BigDecimal.ZERO)
                .add(iepsSugerido != null ? iepsSugerido : BigDecimal.ZERO);
        return total.setScale(2, RoundingMode.HALF_UP);
    }

    public void validarPago(PagoEgreso pago) throws InventarioException {

        if (pago == null) {
            throw new InventarioException("El pago no puede ser vacío");
        }

        if (pago.getImporte() == null
                || pago.getImporte().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InventarioException("El importe del pago debe ser mayor a cero");
        }

        if (pago.getStatus() == null) {
            throw new InventarioException("Debe seleccionar un estatus de pago");
        }

        if (pago.getFechaPago() == null) {
            throw new InventarioException("Debe indicar la fecha del pago");
        }
    }

    public void validarCargo(CargoEgreso cargo) throws InventarioException {

        if (cargo == null) {
            throw new InventarioException("El cargo no puede ser vacío");
        }

        if (cargo.getImporteCargo() == null
                || cargo.getImporteCargo().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InventarioException("El importe del cargo debe ser mayor a cero");
        }

        if (cargo.getImporteCargo() == null || cargo.getTipoCargo() == null) {
            throw new InventarioException("El cargo debe tener un tipo de cargo");
        }
    }
}
