package mx.com.ferbo.manager.egresos;

import java.util.List;

import javax.inject.Inject;

import mx.com.ferbo.business.egresos.PagoEgresoBL;
import mx.com.ferbo.model.egresos.ImporteEgreso;
import mx.com.ferbo.model.egresos.PagoEgreso;
import mx.com.ferbo.util.InventarioException;

public class PagoEgresoMGR {

    @Inject
    private PagoEgresoBL pagoBl;

    public String guardar(PagoEgreso pago, ImporteEgreso egreso) throws InventarioException {
        return pagoBl.operar(pago, egreso);
    }

    public List<PagoEgreso> obtenerLista(ImporteEgreso egreso) throws InventarioException {
        return pagoBl.obtenerPorImporteEgreso(egreso);
    }
    
}
