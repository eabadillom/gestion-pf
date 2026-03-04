package mx.com.ferbo.modulos.egresos.manager.egresos;

import javax.inject.Inject;

import mx.com.ferbo.modulos.egresos.business.EgresoBaseBL;
import mx.com.ferbo.modulos.egresos.business.egreso.ActivoFijoBL;
import mx.com.ferbo.modulos.egresos.manager.EgresoBaseMGR;
import mx.com.ferbo.modulos.egresos.model.catsecundarios.StatusActivoFijo;
import mx.com.ferbo.modulos.egresos.model.egreso.ActivoFijo;
import mx.com.ferbo.modulos.egresos.model.egreso.ImporteEgreso;

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
