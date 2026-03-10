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
import mx.com.ferbo.util.validation.ValidationException;
import mx.com.ferbo.util.validation.helpers.MonetaryValidator;

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

    private void validarAsignacioneEgreso(AsignacionEgreso asignacion) throws InventarioException {

        ValidationException.requireNonNull(asignacion, "La asignación no puede ser vacía");

        ValidationException.requireNonNull(asignacion.getImporteEgreso(), "La asignación no tiene asociado ningun egreso.");

        ValidationException.requireNonNull(asignacion.getTipoAsignacion(), "La asignación no tiene asociado un tipo.");

        ValidationException.requireNonNull(asignacion.getImporte(), "La asignación no tiene ningun inporte");

        MonetaryValidator.requirePositive(asignacion.getImporte(), "el impor de la asinación");

    }

    private void antesDeGuardar(ImporteEgreso egreso, AsignacionEgreso asignacion) throws InventarioException {

        Date hoy = new Date();

        if (asignacion.getId() == null) {
            asignacion.setImporteEgreso(egreso);
            asignacion.setFechaAlta(hoy);
        }

        asignacion.setFechaModificacion(hoy);
    }

    public void guardarAsignacionEgreso(ImporteEgreso egreso, AsignacionEgreso asignacion) throws InventarioException{

        validarPadreEHijo(egreso, asignacion);

        boolean esNuevo = asignacion.getId() == null;

        antesDeGuardar(egreso, asignacion);

        validarAsignacioneEgreso(asignacion);

        if (esNuevo) {
            dao.guardar(asignacion);
        } else {
            dao.actualizar(asignacion);
        }

    }

    private BigDecimal calcularImporte(ImporteEgreso egreso,
            AsignacionEgreso asignacion) throws InventarioException {

        if (egreso == null
                || asignacion == null
                || egreso.getConceptoEgreso() == null
                || egreso.getConceptoEgreso().getTotalConceptoEgreso() == null
                || asignacion.getPorcentaje() == null) {

            throw new InventarioException("Datos insuficientes para calcular el importe.");
        }

        MonetaryValidator.requirePositive(asignacion.getPorcentaje(), "el procentaje de la asignación");

        return egreso.getConceptoEgreso()
                .getTotalConceptoEgreso()
                .multiply(asignacion.getPorcentaje())
                .divide(CIEN, 2, RoundingMode.HALF_UP);
    }

    public void obtenerImporteAsignacion(ImporteEgreso egreso,
            AsignacionEgreso asignacion) throws InventarioException {
        asignacion.setImporte(calcularImporte(egreso, asignacion));
    }

    public List<AsignacionEgreso> obtenerAsgianaciones(ImporteEgreso egreso) throws InventarioException {
        ValidationException.requireNonNull(egreso, "El egreso no puede ser vacío.");
        ValidationException.requireNonNull(egreso.getId(), "El egreso aun no esta guaradado en el sistema.");

        Integer idEgreso = egreso.getId();

        return operar(() -> dao.buscarPorImporteEgreso(idEgreso), "cargar las" + nombreHijos());
    }
}
