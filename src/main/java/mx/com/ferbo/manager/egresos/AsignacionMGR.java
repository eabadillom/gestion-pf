package mx.com.ferbo.manager.egresos;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.inject.Inject;

import mx.com.ferbo.business.egresos.AsignacionEgresoBL;
import mx.com.ferbo.business.egresos.EgresoBaseBL;
import mx.com.ferbo.model.categresos.TipoAsignacionEgreso;
import mx.com.ferbo.model.egresos.AsignacionEgreso;
import mx.com.ferbo.model.egresos.ImporteEgreso;
import mx.com.ferbo.util.InventarioException;

public class AsignacionMGR extends EgresoBaseMGR<AsignacionEgreso, ImporteEgreso, TipoAsignacionEgreso> {

    @Inject
    private AsignacionEgresoBL asignacionBL;

    @Override
    protected EgresoBaseBL<AsignacionEgreso, ImporteEgreso, TipoAsignacionEgreso> getBL() {
        return asignacionBL;
    }

    @Override
    protected Integer getId(AsignacionEgreso entity) {
        return entity.getId();
    }

    public String calcularImporteAsignacion(ImporteEgreso egreso, AsignacionEgreso asignacion) throws InventarioException {
        BigDecimal importe = asignacionBL.calcularImporte(egreso, asignacion);

        asignacion.setImporte(importe);

        return "El importe asignado se calculó correctamente: " + importe.setScale(2, RoundingMode.HALF_UP);
    }

}
