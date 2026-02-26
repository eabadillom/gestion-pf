package mx.com.ferbo.business.egresos.util;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;

import mx.com.ferbo.model.categresos.StatusDevolucionEgreso;
import mx.com.ferbo.model.egresos.DevolucionEgreso;
import mx.com.ferbo.util.InventarioException;
import mx.com.ferbo.util.MaquinaStatusBase;
import mx.com.ferbo.util.SetUtils;

public class MaquinaStatusDevolucion {

    private final MaquinaStatusBase<StatusDevolucionEgreso> maquinaStatus;

    private final StatusDevolucionEgreso REGISTRADA;
    private final StatusDevolucionEgreso AUTORIZADA;
    private final StatusDevolucionEgreso APLICADA;
    private final StatusDevolucionEgreso RECHAZADA;
    private final StatusDevolucionEgreso CANCELADA;
    private final StatusDevolucionEgreso EN_PROCESO;

    public MaquinaStatusDevolucion(StatusDevolucionEgreso registrada, StatusDevolucionEgreso autorizada, 
                                   StatusDevolucionEgreso aplicada, StatusDevolucionEgreso rechazada, 
                                   StatusDevolucionEgreso cancelada, StatusDevolucionEgreso en_proceso) {

        this.REGISTRADA = registrada;
        this.AUTORIZADA = autorizada;
        this.APLICADA = aplicada;
        this.RECHAZADA = rechazada;
        this.CANCELADA = cancelada;
        this.EN_PROCESO = en_proceso;

        Map<StatusDevolucionEgreso, Set<StatusDevolucionEgreso>> transiciones = new HashMap<>();

        transiciones.put(REGISTRADA, SetUtils.setOf(EN_PROCESO, AUTORIZADA, RECHAZADA, CANCELADA));
        transiciones.put(EN_PROCESO, SetUtils.setOf(AUTORIZADA, RECHAZADA, CANCELADA));
        transiciones.put(AUTORIZADA, SetUtils.setOf(APLICADA, CANCELADA));
        transiciones.put(APLICADA, SetUtils.setOf()); // terminal
        transiciones.put(RECHAZADA, SetUtils.setOf()); // terminal
        transiciones.put(CANCELADA, SetUtils.setOf()); // terminal

        this.maquinaStatus = new MaquinaStatusBase<>(transiciones);
    }

    /**
     * Valida el cambio de status a la devolución
     */
    public void cambiarStatus(DevolucionEgreso devolucion, StatusDevolucionEgreso nuevo) throws InventarioException {

        StatusDevolucionEgreso actual = devolucion.getStatus();

        try {
            maquinaStatus.validarTransicion(actual, nuevo);
        } catch (IllegalStateException ex) {
            throw new InventarioException("No se puede cambiar la devolución del status " + actual.getNombre() + " al status " + nuevo.getNombre());
        }
        devolucion.setStatus(nuevo);
    }
}
