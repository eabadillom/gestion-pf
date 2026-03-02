package mx.com.ferbo.manager.egresos;

import javax.inject.Inject;

import mx.com.ferbo.business.egresos.ActivoFijoBL;
import mx.com.ferbo.business.egresos.EgresoBaseBL;
import mx.com.ferbo.model.categresos.StatusActivoFijo;
import mx.com.ferbo.model.egresos.ActivoFijo;
import mx.com.ferbo.model.egresos.ImporteEgreso;

public class ActivoFijoMGR extends EgresoBaseMGR<ActivoFijo, ImporteEgreso, StatusActivoFijo>{

    @Inject
    private ActivoFijoBL activoBL;

    @Override
    protected EgresoBaseBL<ActivoFijo, ImporteEgreso, StatusActivoFijo> getBL() {
        return activoBL;
    }

    @Override
    protected Integer getId(ActivoFijo entity) {
        return entity.getId();
    }

}
