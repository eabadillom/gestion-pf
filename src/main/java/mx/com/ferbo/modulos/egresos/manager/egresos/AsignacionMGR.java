package mx.com.ferbo.modulos.egresos.manager.egresos;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.inject.Inject;

import mx.com.ferbo.modulos.egresos.business.EgresoBaseBL;
import mx.com.ferbo.modulos.egresos.business.egreso.AsignacionEgresoBL;
import mx.com.ferbo.modulos.egresos.manager.EgresoBaseMGR;
import mx.com.ferbo.modulos.egresos.model.catsecundarios.TipoAsignacionEgreso;
import mx.com.ferbo.modulos.egresos.model.egreso.AsignacionEgreso;
import mx.com.ferbo.modulos.egresos.model.egreso.ImporteEgreso;
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
