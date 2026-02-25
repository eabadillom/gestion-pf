package mx.com.ferbo.business.egresos.util;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import mx.com.ferbo.model.egresos.DevolucionEgreso;
import mx.com.ferbo.util.InventarioException;

public class MaquinaStatusDevolucion {

    private final MaquinaStatusBase<DevolucionEgreso> maquinaStatus;

    private final String STATUS_REGISTRADA = "REGISTRADA";
    private final String STATUS_AUTORIZADA = "AUTORIZADA";
    private final String STATUS_APLICADA = "APLICADA";
    private final String STATUS_RECHAZADA = "RECHAZADA";
    private final String STATUS_CANCELADA = "CANCELADA";
    private final String STATUS_EN_PROCESO = "EN_PROCESO";

    public MaquinaStatusDevolucion() {

        Map<String, Set<String>> transiciones = new HashMap<String, Set<String>>();

        transiciones.put(STATUS_REGISTRADA, SetUtils.s(STATUS_EN_PROCESO, STATUS_AUTORIZADA, STATUS_RECHAZADA, STATUS_CANCELADA));
        transiciones.put(STATUS_EN_PROCESO, SetUtils.s(STATUS_AUTORIZADA, STATUS_RECHAZADA, STATUS_CANCELADA));
        transiciones.put(STATUS_AUTORIZADA, SetUtils.s(STATUS_APLICADA, STATUS_CANCELADA));
        transiciones.put(STATUS_APLICADA, SetUtils.s()); // terminal
        transiciones.put(STATUS_RECHAZADA, SetUtils.s()); // terminal
        transiciones.put(STATUS_CANCELADA, SetUtils.s()); // terminal

        this.maquinaStatus = new MaquinaStatusBase<DevolucionEgreso>(transiciones);
    }

    /**
     * Valida el cambio de status a la devolución
     */
    public void cambiarStatus(DevolucionEgreso devolucion, String nuevo) throws InventarioException {

        String actual = devolucion.getStatus().getNombre();

        try {
            maquinaStatus.validarTransicion(actual, nuevo);
        } catch (IllegalStateException ex) {
            throw new InventarioException("No se puede cambiar la devolución del status " + actual + " al status " + nuevo);
        }

    }
}
