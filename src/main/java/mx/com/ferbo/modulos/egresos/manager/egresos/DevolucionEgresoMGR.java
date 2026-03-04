package mx.com.ferbo.modulos.egresos.manager.egresos;

import javax.inject.Inject;

import mx.com.ferbo.modulos.egresos.business.EgresoBaseBL;
import mx.com.ferbo.modulos.egresos.business.egreso.DevolucionEgresoBL;
import mx.com.ferbo.modulos.egresos.manager.EgresoBaseMGR;
import mx.com.ferbo.modulos.egresos.model.catsecundarios.StatusDevolucionEgreso;
import mx.com.ferbo.modulos.egresos.model.egreso.DevolucionEgreso;
import mx.com.ferbo.modulos.egresos.model.egreso.ImporteEgreso;

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
