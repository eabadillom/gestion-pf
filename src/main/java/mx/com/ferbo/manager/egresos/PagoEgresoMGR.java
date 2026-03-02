package mx.com.ferbo.manager.egresos;

import javax.inject.Inject;

import mx.com.ferbo.business.egresos.EgresoBaseBL;
import mx.com.ferbo.business.egresos.PagoEgresoBL;
import mx.com.ferbo.model.categresos.StatusPagoEgreso;
import mx.com.ferbo.model.egresos.ImporteEgreso;
import mx.com.ferbo.model.egresos.PagoEgreso;

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
