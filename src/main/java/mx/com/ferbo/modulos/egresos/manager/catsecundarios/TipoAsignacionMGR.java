package mx.com.ferbo.modulos.egresos.manager.catsecundarios;

import java.util.List;

import javax.inject.Inject;

import mx.com.ferbo.modulos.egresos.business.categreso.TipoAsignacionEgresoBL;
import mx.com.ferbo.modulos.egresos.model.catsecundarios.TipoAsignacionEgreso;
import mx.com.ferbo.util.BaseMGR;
import mx.com.ferbo.util.InventarioException;
import mx.com.ferbo.util.messaging.ResultadoOperacion;

public class TipoAsignacionMGR extends BaseMGR {

    @Inject
    private TipoAsignacionEgresoBL tipoAsignacionEgresoBL;

    public ResultadoOperacion cargarTipoAsignacionesVigentes(List<TipoAsignacionEgreso> lst, Boolean vigente)
            throws InventarioException {

        return cargar(
                lst,
                () -> tipoAsignacionEgresoBL.vigentesONoVigentes(vigente),
                "Cargar tipos de asignación",
                "Se han cargado exitosamente los tipos de asignación.");
    }

    public ResultadoOperacion guadarStatuPagoEgreso(TipoAsignacionEgreso tipoAsignacion) throws InventarioException {

        return guardar(
                tipoAsignacion,
                t -> tipoAsignacionEgresoBL.agregarOActualizar(tipoAsignacion),
                "tipo de asignación");

    }

    

}
