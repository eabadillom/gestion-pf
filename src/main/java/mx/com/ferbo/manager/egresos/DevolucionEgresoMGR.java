package mx.com.ferbo.manager.egresos;

import javax.inject.Inject;

import mx.com.ferbo.business.egresos.DevolucionEgresoBL;
import mx.com.ferbo.business.egresos.EgresoBaseBL;
import mx.com.ferbo.model.categresos.StatusDevolucionEgreso;
import mx.com.ferbo.model.egresos.DevolucionEgreso;
import mx.com.ferbo.model.egresos.ImporteEgreso;

public class DevolucionEgresoMGR extends EgresoBaseMGR <DevolucionEgreso, ImporteEgreso, StatusDevolucionEgreso>{

    @Inject
    private DevolucionEgresoBL devolucionBL;

    @Override
    protected EgresoBaseBL<DevolucionEgreso, ImporteEgreso, StatusDevolucionEgreso> getBL() {
        return devolucionBL;
    }

    @Override
    protected Integer getId(DevolucionEgreso entity) {
        return entity.getId();
    }

    
}
