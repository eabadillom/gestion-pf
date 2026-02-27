package mx.com.ferbo.business.egresos;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import mx.com.ferbo.business.categresos.StatusCargoEgresoBL;
import mx.com.ferbo.business.egresos.util.MaquinaStatusCargo;
import mx.com.ferbo.dao.egresos.CargoEgresoDAO;
import mx.com.ferbo.model.egresos.ConceptoEgreso;
import mx.com.ferbo.model.categresos.StatusCargoEgreso;
import mx.com.ferbo.model.egresos.CargoEgreso;
import mx.com.ferbo.model.egresos.ImporteEgreso;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.FacesUtils;
import mx.com.ferbo.util.InventarioException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Named
@RequestScoped
public class CargoEgresoBL extends EgresoBaseBL<CargoEgreso, ImporteEgreso, StatusCargoEgreso> {

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
        setDao(dao);
            pendiente = statusBL.buscarPorNombre(STATUS_PENDIENTE);
            aplicado = statusBL.buscarPorNombre(STATUS_APLICADO);
            pagado = statusBL.buscarPorNombre(STATUS_PAGADO);
            cancelado = statusBL.buscarPorNombre(STATUS_CANCELADO);
            condonado = statusBL.buscarPorNombre(STATUS_CONDONADO);
            maquinaStatus = new MaquinaStatusCargo(pendiente, aplicado, pagado, cancelado, condonado);
        } catch (InventarioException ex) {
            log.error("Error inicializando máquina de estados", ex);
            throw new RuntimeException("Error crítico de configuración del sistema.", ex);
        } 
    }
    
    @Override
    public CargoEgreso nuevo(){
        return new CargoEgreso();
    }

    @Override
    protected String nombreHijo() {
        return "el cargo";
    }

    @Override
    protected String nombreHijos() {
        return "los cargos";
    }
    
     @Override
    protected String nombreCatalogo() {
        return "el status";
    }

    @Override
    protected void validar(CargoEgreso cargo) throws InventarioException {
        if (cargo == null) {
            throw new InventarioException("El cargo no puede ser vacío.");
        }

        if (cargo.getImporteEgreso() == null) {
            throw new InventarioException("El cargo no tiene asociado un egreso.");
        }

        if (cargo.getTipoCargo() == null) {
            throw new InventarioException("El cargo no tiene asosciado ningun tipo de cargo.");
        }

        if (cargo.getStatus() == null) {
            throw new InventarioException("El cargo no tiene un status asociado.");
        }

        if (cargo.getImporteCargo() == null || cargo.getImporteCargo().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InventarioException("El importe del cargo no pude ser vacío o cero.");
        }
    }

    @Override
    protected void antesDeGuardar(CargoEgreso cargo, ImporteEgreso importe) throws InventarioException {
        
        Date hoy = new Date();

        if (cargo.getId() == null) {
            cargo.setImporteEgreso(importe);
            cargo.setFechaAlta(hoy);
            cargo.setStatus(pendiente);
        }
        
        cargo.setFechaModificacion(hoy);
    }
    
    @Override
    protected void antesDeCambiar(CargoEgreso cargo, StatusCargoEgreso status) throws InventarioException {
        FacesUtils.requireNonNull(cargo, "El cargo al egreso no puede ser vacía");
        FacesUtils.requireNonNull(status, "El nuevo status para el cargo no puede ser vacío.");

        maquinaStatus.cambiarStatus(cargo, status);
    }

    public StatusCargoEgreso aplicable() throws InventarioException {
        return pagado;
    }

    public Integer calcularDias(CargoEgreso cargo) {

        if (cargo.getFechaAplicacion() == null) {
            log.warn("La fecha de aplicación es vacia");
            return 0;
        }

        if (cargo.getFechaCalculo() == null) {
            log.warn("La fecha de calculo es vacía.");
            return 0;
        }
        return DateUtil.daysDiff(cargo.getFechaAplicacion(), cargo.getFechaCalculo());
    }

    public BigDecimal calcularCargo(CargoEgreso cargo, ConceptoEgreso concepto) throws InventarioException {

        if (concepto.getTotalConceptoEgreso() == null) {
            throw new InventarioException("No se tiene el importe a cubrir del egreso.");
        }

        if (cargo.getPorcentajeTasa() == null) {
            throw new InventarioException("El cargo no tiene ningun porcentaje de tasa.");
        }

        if (cargo.getNumeroDias() == null || cargo.getNumeroDias() < 0) {
            throw new InventarioException("El cargo no tinene ningun número de días valido.");
        }

        BigDecimal importe = concepto.getTotalConceptoEgreso();
        BigDecimal tasa = cargo.getPorcentajeTasa();
        BigDecimal dias = new BigDecimal(cargo.getNumeroDias());

        if (tasa.compareTo(BigDecimal.ZERO) > 0
                && dias.compareTo(BigDecimal.ZERO) > 0) {

            BigDecimal base = new BigDecimal("36000");

            return importe
                    .multiply(tasa)
                    .multiply(dias)
                    .divide(base, 2, RoundingMode.HALF_UP);
        }

        return BigDecimal.ZERO;
    } 

}
