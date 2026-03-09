package mx.com.ferbo.modulos.egresos.business.egreso;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import mx.com.ferbo.modulos.egresos.business.EgresoBaseBL;
import mx.com.ferbo.modulos.egresos.dao.egreso.AsignacionEgresoDAO;
import mx.com.ferbo.modulos.egresos.model.catsecundarios.TipoAsignacionEgreso;
import mx.com.ferbo.modulos.egresos.model.egreso.AsignacionEgreso;
import mx.com.ferbo.modulos.egresos.model.egreso.ImporteEgreso;
import mx.com.ferbo.util.BaseBL;
import mx.com.ferbo.util.InventarioException;
import mx.com.ferbo.util.MonetaryValidationUtils;
import mx.com.ferbo.util.ValidationUtils;

@Named
@ApplicationScoped
public class AsignacionEgresoBL extends EgresoBaseBL<AsignacionEgreso, ImporteEgreso, TipoAsignacionEgreso>
        implements BaseBL<AsignacionEgreso> {

    @Inject
    private AsignacionEgresoDAO dao;

    private static final BigDecimal CIEN = BigDecimal.valueOf(100);

    public AsignacionEgresoBL() {
    }

    @Override
    public AsignacionEgreso nuevo() {
        return new AsignacionEgreso();
    }

    @Override
    protected void construirMaquinaStatus() throws InventarioException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'consturirMaquinaStatus'");
    }

    @Override
    protected String nombreCatalogo() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'nombreCatalogo'");
    }

    @Override
    protected String nombreHijo() {
        return "aginación de egeso";
    }

    @Override
    protected String nombreHijos() {
        return "asignaciones de egreso";
    }

    @Override
    protected List<AsignacionEgreso> obtenerHijos(ImporteEgreso father, List<TipoAsignacionEgreso> catalog)
            throws InventarioException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'obtenerHijos'");
    }

    private void validar(AsignacionEgreso asignacion) throws InventarioException {

        ValidationUtils.requireNonNull(asignacion, "La asignación no puede ser vacía");

        ValidationUtils.requireNonNull(asignacion.getImporteEgreso(), "La asignación no tiene asociado ningun egreso.");

        ValidationUtils.requireNonNull(asignacion.getTipoAsignacion(), "La asignación no tiene asociado un tipo.");

        ValidationUtils.requireNonNull(asignacion.getImporte(), "La asignación no tiene ningun inporte");
        
        MonetaryValidationUtils.requirePositive(asignacion.getImporte(), "el impor de la asinación");

    }

    private void antesDeGuardar(AsignacionEgreso asignacion, ImporteEgreso importe) throws InventarioException {

        Date hoy = new Date();

        if (asignacion.getId() == null) {
            asignacion.setImporteEgreso(importe);
            asignacion.setFechaAlta(hoy);
        }

        asignacion.setFechaModificacion(hoy);
    }

    public void calcularImporte(ImporteEgreso egreso,
            AsignacionEgreso asignacion)
            throws InventarioException {

        if (egreso == null
                || asignacion == null
                || egreso.getConceptoEgreso() == null
                || egreso.getConceptoEgreso().getTotalConceptoEgreso() == null
                || asignacion.getPorcentaje() == null) {

            throw new InventarioException("Datos insuficientes para calcular el importe.");
        }

        egreso.getConceptoEgreso()
                .getTotalConceptoEgreso()
                .multiply(asignacion.getPorcentaje())
                .divide(CIEN, 2, RoundingMode.HALF_UP);
    }

}
