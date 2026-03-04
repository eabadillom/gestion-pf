package mx.com.ferbo.modulos.egresos.manager.catsecundarios;

import java.util.List;

import javax.inject.Inject;

import mx.com.ferbo.modulos.egresos.business.categreso.TipoAsignacionEgresoBL;
import mx.com.ferbo.modulos.egresos.manager.CatEgresoBaseMGR;
import mx.com.ferbo.modulos.egresos.model.catsecundarios.TipoAsignacionEgreso;
import mx.com.ferbo.util.InventarioException;

public class TipoAsignacionMGR extends CatEgresoBaseMGR<TipoAsignacionEgreso> {

    @Inject
    private TipoAsignacionEgresoBL tipoAsignacionEgresoBL;

    public String[] cargarTipoAsignacionesVigentes(List<TipoAsignacionEgreso> lst, Boolean vigente)
            throws InventarioException {

        return cargarVigentes(
                lst,
                () -> tipoAsignacionEgresoBL.vigentesONoVigentes(vigente),
                "Cargar tipos de asignación",
                "Se han cargado exitosamente los tipos de asignación.");
    }

    public String[] guadarStatuPagoEgreso(TipoAsignacionEgreso tipoAsignacion) throws InventarioException {

        return guardarCatalogo(
                tipoAsignacion,
                tipoAsignacionEgresoBL::agregarOActualizar,
                "tipo de asignación");

    }

}
