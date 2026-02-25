package mx.com.ferbo.business.egresos.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import mx.com.ferbo.model.egresos.ImporteEgreso;
import mx.com.ferbo.util.InventarioException;

public class MaquinaStatusEgreso {

    private final MaquinaStatusBase<ImporteEgreso> maquinaStatus;

    private final String STATUS_BORRADOR = "BORRADOR";
    private final String STATUS_REGISTRADO = "REGISTRADO";
    private final String STATUS_PARCIAL = "PARCIAL";
    private final String STATUS_PAGADO = "PAGADO";
    private final String STATUS_CANCELADO = "CANCELADO";
    private final String STATUS_EXCEDENTE = "EXCEDENTE";
    private final String STATUS_POR_CONCILIAR = "POR_CONCILIAR";

    public MaquinaStatusEgreso() {
        Map<String, Set<String>> transiciones = new HashMap<String, Set<String>>();

        transiciones.put(STATUS_BORRADOR, SetUtils.s(STATUS_REGISTRADO, STATUS_CANCELADO));
        transiciones.put(STATUS_REGISTRADO, SetUtils.s(STATUS_PARCIAL, STATUS_PAGADO, STATUS_CANCELADO, STATUS_POR_CONCILIAR));
        transiciones.put(STATUS_PARCIAL, SetUtils.s(STATUS_PAGADO, STATUS_EXCEDENTE, STATUS_POR_CONCILIAR, STATUS_CANCELADO));
        transiciones.put(STATUS_PAGADO, SetUtils.s());
        transiciones.put(STATUS_CANCELADO, SetUtils.s());
        transiciones.put(STATUS_EXCEDENTE, SetUtils.s(STATUS_PAGADO, STATUS_POR_CONCILIAR));
        transiciones.put(STATUS_POR_CONCILIAR, SetUtils.s(STATUS_PARCIAL, STATUS_PAGADO, STATUS_EXCEDENTE, STATUS_CANCELADO));

        this.maquinaStatus = new MaquinaStatusBase<ImporteEgreso>(transiciones);
    }

    /*
        Valida antes de hacer el cambio de Status.
     */
    public void cambiarStatus(ImporteEgreso importe, String nuevo) throws InventarioException {
        String actual = importe.getStatus().getNombre();
        try {
            maquinaStatus.validarTransicion(actual, nuevo);
        } catch (IllegalStateException ex) {
            throw new InventarioException("No se puede cambiar el egreso del status " + actual + " al status " + nuevo);
        }
    }
}
