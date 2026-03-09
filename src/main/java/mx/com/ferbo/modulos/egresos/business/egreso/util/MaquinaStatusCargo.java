
package mx.com.ferbo.modulos.egresos.business.egreso.util;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;

import mx.com.ferbo.modulos.egresos.model.catsecundarios.StatusCargoEgreso;
import mx.com.ferbo.modulos.egresos.model.egreso.CargoEgreso;
import mx.com.ferbo.util.InventarioException;
import mx.com.ferbo.util.MaquinaStatusBase;
import mx.com.ferbo.util.SetUtils;

public class MaquinaStatusCargo {

    private final MaquinaStatusBase<StatusCargoEgreso> maquinaStatus;

    private final StatusCargoEgreso PENDIENTE;
    private final StatusCargoEgreso APLICADO;
    private final StatusCargoEgreso PAGADO;
    private final StatusCargoEgreso CANCELADO;
    private final StatusCargoEgreso CONDONADO;

    public MaquinaStatusCargo(StatusCargoEgreso pendiente, StatusCargoEgreso aplicado,
            StatusCargoEgreso pagado, StatusCargoEgreso cancelado,
            StatusCargoEgreso condonado) {

        this.PENDIENTE = pendiente;
        this.APLICADO = aplicado;
        this.PAGADO = pagado;
        this.CANCELADO = cancelado;
        this.CONDONADO = condonado;

        Map<StatusCargoEgreso, Set<StatusCargoEgreso>> transiciones = new HashMap<>();

        transiciones.put(PENDIENTE, SetUtils.setOf(APLICADO, PAGADO, CANCELADO, CONDONADO));
        transiciones.put(APLICADO, SetUtils.setOf(PAGADO, CANCELADO, CONDONADO));
        transiciones.put(PAGADO, SetUtils.setOf()); // terminal
        transiciones.put(CANCELADO, SetUtils.setOf()); // terminal
        transiciones.put(CONDONADO, SetUtils.setOf()); // terminal

        this.maquinaStatus = new MaquinaStatusBase<>(transiciones);
    }

    /**
     * Valida y cambia el status del Cargo
     */
    public void cambiarStatus(CargoEgreso cargo, StatusCargoEgreso nuevo) throws InventarioException {

        maquinaStatus.conTransicionValida(
                cargo.getStatus(),
                nuevo,
                () -> cargo.setStatus(nuevo));
    }
}