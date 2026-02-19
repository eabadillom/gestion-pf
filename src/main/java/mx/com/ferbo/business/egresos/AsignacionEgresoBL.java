package mx.com.ferbo.business.egresos;

import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import mx.com.ferbo.dao.egresos.AsignacionDAO;
import mx.com.ferbo.model.categresos.TipoAsignacion;
import mx.com.ferbo.model.egresos.AsignacionEgreso;
import mx.com.ferbo.model.egresos.ImporteEgreso;
import mx.com.ferbo.util.InventarioException;

@Named
@ApplicationScoped
public class AsignacionEgresoBL extends EgresoBaseBL<AsignacionEgreso, ImporteEgreso, TipoAsignacion> {

    @Inject
    private AsignacionDAO dao;
    
    private static final BigDecimal CIEN = BigDecimal.valueOf(100);

    public AsignacionEgresoBL() {
        setDao(dao);
    }
    
    @Override
    protected AsignacionEgreso nuevo(){
        return new AsignacionEgreso();
    }

    @Override
    protected String nombreHijo() {
        return "la asignación";
    }

    @Override
    protected String nombreHijos() {
        return "las asignaciones";
    }

    @Override
    protected void validar(AsignacionEgreso entity) throws InventarioException {

        if (entity == null) {
            throw new InventarioException("La asignación no puede ser vacía");
        }

        if (entity.getImporteEgreso() == null) {
            throw new InventarioException("La asignación no tiene asociado ningun egreso.");
        }

        if (entity.getTipoAsignacion() == null) {
            throw new InventarioException("La asignación no tiene asociado un tipo.");
        }

        if (entity.getImporte() == null || entity.getImporte().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InventarioException("La asignación no tiene un importe valido.");
        }
    }

    @Override
    protected void antesDeGuardar(AsignacionEgreso asignacion, ImporteEgreso importe) throws InventarioException {
        
        if (asignacion.getImporteEgreso() == null) {
            asignacion.setImporteEgreso(importe);
        }
        
    }

    public BigDecimal calcularImporte(ImporteEgreso entity,
            AsignacionEgreso asignacion)
            throws InventarioException {

        if (entity == null
                || asignacion == null
                || entity.getConceptoEgreso() == null
                || entity.getConceptoEgreso().getTotalConceptoEgreso() == null
                || asignacion.getPorcentaje() == null) {

            throw new InventarioException("Datos insuficientes para calcular el importe.");
        }

        return entity.getConceptoEgreso()
                .getTotalConceptoEgreso()
                .multiply(asignacion.getPorcentaje())
                .divide(CIEN, 2, RoundingMode.HALF_UP);
    }
    
    @Override
    protected String nombreCatalogo() {
        return "el tipo";
    }

    @Override
    protected void antesDeCambiar(AsignacionEgreso entity, TipoAsignacion catalog) throws InventarioException {
        entity.setTipoAsignacion(catalog);
    }

}
