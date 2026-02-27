package mx.com.ferbo.manager.egresos;

import java.util.List;

import javax.inject.Inject;

import mx.com.ferbo.business.egresos.DevolucionEgresoBL;
import mx.com.ferbo.model.egresos.DevolucionEgreso;
import mx.com.ferbo.model.egresos.ImporteEgreso;
import mx.com.ferbo.util.InventarioException;

public class DevolucionEgresoMGR {

    @Inject
    private DevolucionEgresoBL devolucionBL;

    public String guardar(DevolucionEgreso devolucion, ImporteEgreso egreso) throws InventarioException { 
        return devolucionBL.operar(devolucion, egreso);
    }

    public List<DevolucionEgreso> obtenerLista(ImporteEgreso egreso) throws InventarioException{
        return devolucionBL.obtenerPorImporteEgreso(egreso);
    }

}
