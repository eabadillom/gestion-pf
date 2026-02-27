package mx.com.ferbo.manager.egresos;

import javax.inject.Inject;

import mx.com.ferbo.business.egresos.CargoEgresoBL;
import mx.com.ferbo.model.egresos.CargoEgreso;
import mx.com.ferbo.model.egresos.ImporteEgreso;
import mx.com.ferbo.util.InventarioException;

public class CargoEgresoMGR {

    @Inject
    private CargoEgresoBL cargoBL;

    public String guardar(CargoEgreso cargo, ImporteEgreso egreso) throws InventarioException { 
        return cargoBL.operar(cargo, egreso);
    }
}
