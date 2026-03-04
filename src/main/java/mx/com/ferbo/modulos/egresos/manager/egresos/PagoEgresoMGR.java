package mx.com.ferbo.modulos.egresos.manager.egresos;

import javax.inject.Inject;

import mx.com.ferbo.modulos.egresos.business.EgresoBaseBL;
import mx.com.ferbo.modulos.egresos.business.egreso.PagoEgresoBL;
import mx.com.ferbo.modulos.egresos.manager.EgresoBaseMGR;
import mx.com.ferbo.modulos.egresos.model.catsecundarios.StatusPagoEgreso;
import mx.com.ferbo.modulos.egresos.model.egreso.ImporteEgreso;
import mx.com.ferbo.modulos.egresos.model.egreso.PagoEgreso;

public class PagoEgresoMGR extends EgresoBaseMGR <PagoEgreso, ImporteEgreso, StatusPagoEgreso>{

    @Inject
    private PagoEgresoBL pagoBL;

    @Override
    protected EgresoBaseBL<PagoEgreso, ImporteEgreso, StatusPagoEgreso> getBL() {
        return pagoBL;
    }

    @Override
    protected Integer getId(PagoEgreso entity) {
        return entity.getId();
    }

}
