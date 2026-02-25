
package mx.com.ferbo.business.egresos.util;


import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import mx.com.ferbo.model.egresos.CargoEgreso;
import mx.com.ferbo.util.InventarioException;

public class MaquinaStatusCargo {

    private final MaquinaStatusBase<CargoEgreso> maquinaStatus;

    private final String STATUS_PENDIENTE = "PENDIENTE";
    private final String STATUS_APLICADO = "APLICADO";
    private final String STATUS_PAGADO = "PAGADO";
    private final String STATUS_CANCELADO = "CANCELADO";
    private final String STATUS_CONDONADO = "CONDONADO";

    public MaquinaStatusCargo() {

        Map<String, Set<String>> transiciones = new HashMap<String, Set<String>>();

        transiciones.put(STATUS_PENDIENTE, SetUtils.s(STATUS_APLICADO, STATUS_PAGADO, STATUS_CANCELADO, STATUS_CONDONADO));
        transiciones.put(STATUS_APLICADO, SetUtils.s(STATUS_PAGADO, STATUS_CANCELADO, STATUS_CONDONADO));
        transiciones.put(STATUS_PAGADO, SetUtils.s());     // terminal
        transiciones.put(STATUS_CANCELADO, SetUtils.s());  // terminal
        transiciones.put(STATUS_CONDONADO, SetUtils.s());  // terminal

        this.maquinaStatus = new MaquinaStatusBase<CargoEgreso>(transiciones);
    }

    /**
     * Valida el cambio de status al Cargo
     */
    public void cambiarStatus(CargoEgreso cargo, String nuevo)throws InventarioException {

        String actual = cargo.getStatus().getNombre();

        try {
            maquinaStatus.validarTransicion(actual, nuevo);
        } catch (IllegalStateException ex) {
            throw new InventarioException("No se puede cambiar el cargo del status " + actual + " al status " + nuevo);
        }
    }
}