package mx.com.ferbo.manager.egresos;

import java.util.List;

import javax.inject.Inject;

import mx.com.ferbo.business.egresos.ActivoFijoBL;
import mx.com.ferbo.model.egresos.ActivoFijo;
import mx.com.ferbo.model.egresos.ImporteEgreso;
import mx.com.ferbo.util.InventarioException;

public class ActivoFijoMGR {

    @Inject
    private ActivoFijoBL activoBL;

    public String guardar(ActivoFijo activo, ImporteEgreso egreso) throws InventarioException {
        return activoBL.operar(activo, egreso);
    }

    public List<ActivoFijo> obtenerLista(ImporteEgreso egreso) throws InventarioException {
        return activoBL.obtenerPorImporteEgreso(egreso);
    }
}
