package mx.com.ferbo.egresos.business.catalogos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import com.ferbo.tools.exception.BusinessException;

import mx.com.ferbo.egresos.model.calogos.StatusEgreso;
import mx.com.ferbo.egresos.util.MaquinaStatusBase;
import mx.com.ferbo.egresos.util.SetUtils;

@Named
@RequestScoped
public class MaquinaStatusEgreso {

    private MaquinaStatusBase<StatusEgreso> maquina;

    private StatusEgreso cancelado;
    private StatusEgreso pendiente;
    private StatusEgreso procesado;

    private void asinarStatusExistentes(List<StatusEgreso> lstStatus) {
        for (StatusEgreso status : lstStatus) {
            switch (status.getClave()) {
                case "CAN":
                    cancelado = status;
                    break;

                case "PEN":
                    pendiente = status;
                    break;

                case "PRO":
                    procesado = status;
                    break;

                default:
                    throw new BusinessException("El status de egreso no tiene una clave existente.");
            }
        }
    }

    private void construirMaquiaStatusEgreso() {

        Map<StatusEgreso, Set<StatusEgreso>> transiciones = new HashMap<StatusEgreso, Set<StatusEgreso>>();

        transiciones.put(pendiente, SetUtils.setOf(cancelado, procesado));
        transiciones.put(procesado, SetUtils.setOf());
        transiciones.put(cancelado, SetUtils.setOf());

        maquina = new MaquinaStatusBase<StatusEgreso>(transiciones);

    }

    public void valiarCambioStatus(List<StatusEgreso> lstStatus, StatusEgreso actual, StatusEgreso nuevo) {

        if (lstStatus == null || lstStatus.isEmpty()) {
            throw new BusinessException("No se obtuvo la lista de status de egreso.");
        }

        asinarStatusExistentes(lstStatus);

        if (actual == null && nuevo != null) {
            if (!nuevo.getClave().equals(pendiente.getClave()) && !nuevo.getClave().equals(procesado.getClave())) {
                throw new BusinessException("No se puede asignar un status no valido a un nuevo egreso.");
            }
        } else {
            construirMaquiaStatusEgreso();
            maquina.transicionValida(actual, nuevo, actual.getNombre(), nuevo.getNombre());
        }

    }
}
