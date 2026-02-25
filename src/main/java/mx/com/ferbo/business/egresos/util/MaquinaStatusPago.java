package mx.com.ferbo.business.egresos.util;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import mx.com.ferbo.model.egresos.PagoEgreso;
import mx.com.ferbo.util.InventarioException;

public class MaquinaStatusPago {

    private final MaquinaStatusBase<PagoEgreso> maquinaStatus;

    private final String STATUS_PENDIENTE = "PENDIENTE";
    private final String STATUS_PAGADO = "PAGADO";
    private final String STATUS_PARCIAL = "PARCIAL";
    private final String STATUS_CANCELADO = "CANCELADO";
    private final String STATUS_VENCIDO = "VENCIDO";

    public MaquinaStatusPago() {
        Map<String, Set<String>> transiciones = new HashMap<String, Set<String>>();

        transiciones.put(STATUS_PENDIENTE, SetUtils.s(STATUS_PARCIAL, STATUS_PAGADO, STATUS_CANCELADO, STATUS_VENCIDO));
        transiciones.put(STATUS_PARCIAL, SetUtils.s(STATUS_PAGADO, STATUS_CANCELADO, STATUS_VENCIDO));
        transiciones.put(STATUS_PAGADO, SetUtils.s()); // terminal
        transiciones.put(STATUS_CANCELADO, SetUtils.s()); // terminal
        transiciones.put(STATUS_VENCIDO, SetUtils.s(STATUS_PAGADO, STATUS_CANCELADO));

        this.maquinaStatus = new MaquinaStatusBase<PagoEgreso>(transiciones);
    }

    /**
     * Valida el cambio de status al Pago
     */
    public void cambiarStatus(PagoEgreso pago, String nuevo) throws InventarioException {
        String actual = pago.getStatus().getNombre();
        try {
            maquinaStatus.validarTransicion(actual, nuevo);
        } catch (IllegalStateException ex) {
            throw new InventarioException("No se puede cambiar el pago del status " + actual + " al status " + nuevo);
        }
    }
}
