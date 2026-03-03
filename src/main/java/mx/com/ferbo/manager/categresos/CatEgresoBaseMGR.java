package mx.com.ferbo.manager.categresos;

import javax.inject.Inject;

import mx.com.ferbo.business.categresos.TipoAsignacionEgresoBL;
import mx.com.ferbo.business.categresos.TipoCargoEgresoBL;
import mx.com.ferbo.business.categresos.TipoDevolucionEgresoBL;
import mx.com.ferbo.business.categresos.TipoMovimientoEgresoBL;
import mx.com.ferbo.model.categresos.TipoAsignacionEgreso;

public class CatEgresoBaseMGR {

    private static final Boolean vigente = Boolean.TRUE; 

    @Inject
    private TipoAsignacionEgresoBL tipoAsignacionEgresoBL;

    @Inject
    private TipoCargoEgresoBL tipoCargoEgresoBL;

    @Inject
    private TipoDevolucionEgresoBL tipoDevolucionEgresoBL;

    @Inject
    private TipoMovimientoEgresoBL tipoMovimientoEgresoBL;
}
