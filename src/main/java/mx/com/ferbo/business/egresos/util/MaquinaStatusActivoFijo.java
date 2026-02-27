package mx.com.ferbo.business.egresos.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import mx.com.ferbo.model.categresos.StatusActivoFijo;
import mx.com.ferbo.model.egresos.ActivoFijo;
import mx.com.ferbo.util.InventarioException;
import mx.com.ferbo.util.MaquinaStatusBase;
import mx.com.ferbo.util.SetUtils;

public class MaquinaStatusActivoFijo {

    private final MaquinaStatusBase<StatusActivoFijo> maquinaStatus;

    private final StatusActivoFijo EN_USO;
    private final StatusActivoFijo EN_REPARACION;
    private final StatusActivoFijo INACTIVO;
    private final StatusActivoFijo EN_BAJA;
    private final StatusActivoFijo VENDIDO;
    private final StatusActivoFijo OBSOLETO;
    private final StatusActivoFijo DADO_DE_BAJA;
    private final StatusActivoFijo EN_REVALUACION;
    private final StatusActivoFijo RECIBIDO;
    private final StatusActivoFijo DESCARTADO;

    public MaquinaStatusActivoFijo(StatusActivoFijo en_uso, StatusActivoFijo en_reparacion,
                                   StatusActivoFijo inactivo, StatusActivoFijo en_baja,
                                   StatusActivoFijo vendido, StatusActivoFijo obsoleto,
                                   StatusActivoFijo dado_de_baja, StatusActivoFijo en_revaluacion,
                                   StatusActivoFijo recibido, StatusActivoFijo descartado) {

    this.EN_USO = en_uso;
    this.EN_REPARACION = en_reparacion;
    this.INACTIVO = inactivo;
    this.EN_BAJA = en_baja;
    this.VENDIDO = vendido;
    this.OBSOLETO = obsoleto;
    this.DADO_DE_BAJA = dado_de_baja;
    this.EN_REVALUACION = en_revaluacion;
    this.RECIBIDO = recibido;
    this.DESCARTADO = descartado;

    Map<StatusActivoFijo, Set<StatusActivoFijo>> transiciones = new HashMap<>();

    // Flujo inicial
        transiciones.put(RECIBIDO, SetUtils.setOf(EN_REVALUACION, EN_USO));
        transiciones.put(EN_REVALUACION, SetUtils.setOf(EN_USO, INACTIVO));

        // En operación
        transiciones.put(EN_USO, SetUtils.setOf(EN_REPARACION, INACTIVO, OBSOLETO, EN_BAJA));
        transiciones.put(EN_REPARACION, SetUtils.setOf(EN_USO, INACTIVO, OBSOLETO));
        transiciones.put(INACTIVO, SetUtils.setOf(EN_USO, EN_BAJA, OBSOLETO));

        // Proceso de salida
        transiciones.put(OBSOLETO, SetUtils.setOf(EN_BAJA));
        transiciones.put(EN_BAJA, SetUtils.setOf(DADO_DE_BAJA, VENDIDO, DESCARTADO));

        // Estados terminales
        transiciones.put(DADO_DE_BAJA, SetUtils.setOf());
        transiciones.put(VENDIDO, SetUtils.setOf());
        transiciones.put(DESCARTADO, SetUtils.setOf());

    this.maquinaStatus = new MaquinaStatusBase<>(transiciones);
    }

    public void cambiarStatus(ActivoFijo activo, StatusActivoFijo nuevo) throws InventarioException{
        StatusActivoFijo actual = activo.getStatus();

        try {
            maquinaStatus.validarTransicion(actual, nuevo);
        } catch (IllegalStateException ex) {
            throw new InventarioException("No se puede cambiar el activo fijo del status " + actual.getNombre() + " al status " + nuevo.getNombre());
        }
        activo.setStatus(nuevo);
    }
}
