package mx.com.ferbo.business.egresos.util;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;

import mx.com.ferbo.model.categresos.StatusPagoEgreso;
import mx.com.ferbo.model.egresos.PagoEgreso;
import mx.com.ferbo.util.InventarioException;
import mx.com.ferbo.util.MaquinaStatusBase;
import mx.com.ferbo.util.SetUtils;

public class MaquinaStatusPago {

    private final MaquinaStatusBase<StatusPagoEgreso> maquinaStatus;

    private final StatusPagoEgreso PENDIENTE;
    private final StatusPagoEgreso PAGADO;
    private final StatusPagoEgreso PARCIAL;
    private final StatusPagoEgreso CANCELADO;
    private final StatusPagoEgreso VENCIDO;

    public MaquinaStatusPago(StatusPagoEgreso pendiente, StatusPagoEgreso pagado, 
                             StatusPagoEgreso parcial, StatusPagoEgreso cancelado, 
                             StatusPagoEgreso vencido ) {

        this.PENDIENTE = pendiente;
        this.PAGADO = pagado;
        this.PARCIAL = parcial;
        this.CANCELADO = cancelado;
        this.VENCIDO = vencido;

        Map<StatusPagoEgreso, Set<StatusPagoEgreso>> transiciones = new HashMap<StatusPagoEgreso, Set<StatusPagoEgreso>>();

        transiciones.put(PENDIENTE, SetUtils.setOf(PARCIAL, PAGADO, CANCELADO, VENCIDO));
        transiciones.put(PARCIAL, SetUtils.setOf(PAGADO, CANCELADO, VENCIDO));
        transiciones.put(PAGADO, SetUtils.setOf()); // terminal
        transiciones.put(CANCELADO, SetUtils.setOf()); // terminal
        transiciones.put(VENCIDO, SetUtils.setOf(PAGADO, CANCELADO));

        this.maquinaStatus = new MaquinaStatusBase<StatusPagoEgreso>(transiciones);
    }

    /**
     * Valida el cambio de status al Pago
     */
    public void cambiarStatus(PagoEgreso pago, StatusPagoEgreso nuevo) throws InventarioException {
        StatusPagoEgreso actual = pago.getStatus();
        try {
            maquinaStatus.validarTransicion(actual, nuevo);
        } catch (IllegalStateException ex) {
            throw new InventarioException("No se puede cambiar el pago del status " + actual + " al status " + nuevo);
        }
        pago.setStatus(nuevo);
    }
}
