package mx.com.ferbo.modulos.egresos.manager.egresos;

import java.util.List;

import javax.inject.Inject;

import mx.com.ferbo.modulos.egresos.business.egreso.ActivoFijoBL;
import mx.com.ferbo.modulos.egresos.model.egreso.ActivoFijo;
import mx.com.ferbo.modulos.egresos.model.egreso.ImporteEgreso;
import mx.com.ferbo.util.BaseMGR;
import mx.com.ferbo.util.InventarioException;
import mx.com.ferbo.util.messaging.ResultadoOperacion;

public class ActivoFijoMGR implements BaseMGR {

    @Inject
    private ActivoFijoBL activoBL;

    public ResultadoOperacion obtnerPorImporteEgreso(ImporteEgreso importeEgreso, List<ActivoFijo> lst)
            throws InventarioException {
        return BaseMGR.super.cargar(
                lst,
                () -> activoBL.obtenerPorImporteEgreso(importeEgreso),
                "Cargar activos fijo de egreso",
                "Se han cargado exitosamente los avtibos fijos.");
    }
}
