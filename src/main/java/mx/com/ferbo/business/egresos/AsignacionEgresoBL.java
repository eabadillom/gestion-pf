package mx.com.ferbo.business.egresos;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import mx.com.ferbo.dao.egresos.AsignacionEgresoDAO;
import mx.com.ferbo.model.categresos.TipoAsignacionEgreso;
import mx.com.ferbo.model.egresos.AsignacionEgreso;
import mx.com.ferbo.model.egresos.ImporteEgreso;
import mx.com.ferbo.util.InventarioException;

@Named
@ApplicationScoped
public class AsignacionEgresoBL extends EgresoBaseBL<AsignacionEgreso, ImporteEgreso, TipoAsignacionEgreso> {

    @Inject
    private AsignacionEgresoDAO dao;

    private static final BigDecimal CIEN = BigDecimal.valueOf(100);

    public AsignacionEgresoBL() {
        setDao(dao);
    }

    @Override
    public AsignacionEgreso nuevo() {
        return new AsignacionEgreso();
    }

    @Override
    public String nombreHijo() {
        return "la asignación";
    }

    @Override
    public String nombreHijos() {
        return "las asignaciones";
    }

    @Override
    public String nombreCatalogo() {
        return "el tipo";
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

        Date hoy = new Date();

        if (asignacion.getId() == null) {
            asignacion.setImporteEgreso(importe);
            asignacion.setFechaAlta(hoy);
        }

        asignacion.setFechaModificacion(hoy);
    }

    @Override
    protected void antesDeCambiar(AsignacionEgreso asingacion, TipoAsignacionEgreso Tipo) throws InventarioException {
       // Méotodo sin implementar para que compla el contrato
    }

    public BigDecimal calcularImporte(ImporteEgreso egreso,
            AsignacionEgreso asignacion)
            throws InventarioException {

        if (egreso == null
                || asignacion == null
                || egreso.getConceptoEgreso() == null
                || egreso.getConceptoEgreso().getTotalConceptoEgreso() == null
                || asignacion.getPorcentaje() == null) {

            throw new InventarioException("Datos insuficientes para calcular el importe.");
        }

        return egreso.getConceptoEgreso()
                .getTotalConceptoEgreso()
                .multiply(asignacion.getPorcentaje())
                .divide(CIEN, 2, RoundingMode.HALF_UP);
    }

    @Override
    protected TipoAsignacionEgreso statusInicial() {
        // No se utiliza en esta clase; retorna objeto vacío para cumplir contrato
        return new TipoAsignacionEgreso();
    }

}
