package mx.com.ferbo.business.egresos.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import mx.com.ferbo.model.categresos.StatusEgreso;
import mx.com.ferbo.model.egresos.ImporteEgreso;
import mx.com.ferbo.util.InventarioException;
import mx.com.ferbo.util.MaquinaStatusBase;
import mx.com.ferbo.util.SetUtils;

public class MaquinaStatusEgreso {

    private final MaquinaStatusBase<StatusEgreso> maquinaStatus;

    private final StatusEgreso BORRADOR;
    private final StatusEgreso REGISTRADO;
    private final StatusEgreso PARCIAL;
    private final StatusEgreso PAGADO;
    private final StatusEgreso CANCELADO;
    private final StatusEgreso EXCEDENTE;
    private final StatusEgreso POR_CONCILIAR;

    public MaquinaStatusEgreso(StatusEgreso borrador, StatusEgreso registrado, 
                               StatusEgreso parcial, StatusEgreso pagado, 
                               StatusEgreso cancelado, StatusEgreso excedente, 
                               StatusEgreso por_conciliar ) {

        this.BORRADOR = borrador;
        this.REGISTRADO = registrado;
        this.PARCIAL = parcial;
        this.PAGADO = pagado;
        this.CANCELADO = cancelado;
        this.EXCEDENTE = excedente;
        this.POR_CONCILIAR = por_conciliar;
                            
        Map<StatusEgreso, Set<StatusEgreso>> transiciones = new HashMap<StatusEgreso, Set<StatusEgreso>>();

        transiciones.put(BORRADOR, SetUtils.setOf(REGISTRADO, CANCELADO));
        transiciones.put(REGISTRADO, SetUtils.setOf(PARCIAL, PAGADO, CANCELADO, POR_CONCILIAR));
        transiciones.put(PARCIAL, SetUtils.setOf(PAGADO, EXCEDENTE, POR_CONCILIAR, CANCELADO));
        transiciones.put(PAGADO, SetUtils.setOf());
        transiciones.put(CANCELADO, SetUtils.setOf());
        transiciones.put(EXCEDENTE, SetUtils.setOf(PAGADO, POR_CONCILIAR));
        transiciones.put(POR_CONCILIAR, SetUtils.setOf(PARCIAL, PAGADO, EXCEDENTE, CANCELADO));

        this.maquinaStatus = new MaquinaStatusBase<StatusEgreso>(transiciones);
    }

    /*
        Valida antes de hacer el cambio de Status.
     */
    public void cambiarStatus(ImporteEgreso importe, StatusEgreso nuevo) throws InventarioException {
        StatusEgreso actual = importe.getStatus();
        try {
            maquinaStatus.validarTransicion(actual, nuevo);
        } catch (IllegalStateException ex) {
            throw new InventarioException("No se puede cambiar el egreso del status " + actual.getNombre() + " al status " + nuevo.getNombre());
        }
        importe.setStatus(nuevo);
    }
}
