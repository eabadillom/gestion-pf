package mx.com.ferbo.modulos.egresos.factory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import mx.com.ferbo.modulos.egresos.business.categreso.StatusPagoEgresoBL;
import mx.com.ferbo.modulos.egresos.model.catsecundarios.StatusPagoEgreso;
import mx.com.ferbo.util.AbstractFactoryMaquinaStatus;
import mx.com.ferbo.util.InventarioException;
import mx.com.ferbo.util.MaquinaStatusBase;
import mx.com.ferbo.util.SetUtils;

@RequestScoped
public class PagoEgresoFactory implements AbstractFactoryMaquinaStatus<StatusPagoEgreso> {

    @Inject StatusPagoEgresoBL statusBL;

    @Override
    public MaquinaStatusBase<StatusPagoEgreso> crearMaquina() throws InventarioException {
        StatusPagoEgreso pendiente = statusBL.buscarPorNombre("PENDIENTE");
        StatusPagoEgreso pagado = statusBL.buscarPorNombre("PAGADO");
        StatusPagoEgreso parcial = statusBL.buscarPorNombre("PARCIAL");
        StatusPagoEgreso cancelado = statusBL.buscarPorNombre("CANCELADO");
        StatusPagoEgreso vencido = statusBL.buscarPorNombre("VENCIDO");

        Map<StatusPagoEgreso, Set<StatusPagoEgreso>> transiciones = new HashMap<>();
        transiciones.put(pendiente, SetUtils.setOf(parcial, pagado, cancelado, vencido));
        transiciones.put(parcial, SetUtils.setOf(pagado, cancelado, vencido));
        transiciones.put(pagado, SetUtils.setOf());
        transiciones.put(cancelado, SetUtils.setOf());
        transiciones.put(vencido, SetUtils.setOf(pagado, cancelado));

        return new MaquinaStatusBase<>(transiciones);
    }

}
