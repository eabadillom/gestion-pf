package mx.com.ferbo.wrapper;

import java.util.List;

import mx.com.ferbo.model.categresos.CatConceptoEgreso;
import mx.com.ferbo.model.categresos.CategoriaEgreso;
import mx.com.ferbo.model.categresos.StatusActivoFijo;
import mx.com.ferbo.model.categresos.StatusCargoEgreso;
import mx.com.ferbo.model.categresos.StatusDevolucionEgreso;
import mx.com.ferbo.model.categresos.StatusEgreso;
import mx.com.ferbo.model.categresos.StatusPagoEgreso;
import mx.com.ferbo.model.categresos.TipoAsignacionEgreso;
import mx.com.ferbo.model.categresos.TipoCargoEgreso;
import mx.com.ferbo.model.categresos.TipoDevolucionEgreso;
import mx.com.ferbo.model.categresos.TipoDocumentoEgreso;
import mx.com.ferbo.model.categresos.TipoEgreso;
import mx.com.ferbo.model.categresos.TipoMovimientoEgreso;

public class CatEgresoWRP {

    private TipoEgreso tipoSelected;
    private List<TipoEgreso> lstTipos;
    
    private CategoriaEgreso categoriaSelected;
    private List<CategoriaEgreso> lstCategorias;

    private CatConceptoEgreso catConceptoSelected;
    private List<CatConceptoEgreso> lstCatConceptos;

    private TipoAsignacionEgreso tipoAsingancionSelected;
    private List<TipoAsignacionEgreso> lstTiposAsignacion;

    private TipoCargoEgreso tipoCargoSelected;
    private List<TipoCargoEgreso> lstTiposCargo;

    private TipoDevolucionEgreso tipoDevolucionSelected;
    private List<TipoDevolucionEgreso> lstTiposDevolucion;

    private TipoDocumentoEgreso tipoDocumentoSelected;
    private List<TipoDocumentoEgreso> lstTiposDocumento;

    private TipoMovimientoEgreso tipoMovimientoSelected;
    private List<TipoMovimientoEgreso> lstTiposMovimiento;

    private StatusActivoFijo statusActivoFijoSelected;
    private List<StatusActivoFijo> lstStatusActivoFijo;

    private StatusCargoEgreso statusCargoEgresoSelected;
    private List<StatusCargoEgreso> lstStatusCargoEgreso;

    private StatusDevolucionEgreso statusDevolucionEgresoSelected;
    private List<StatusDevolucionEgreso> lstStatusDevolucionEgreso;

    private StatusPagoEgreso statusPagoEgresoSelected;
    private List<StatusPagoEgreso> lstStatusPagoEgreso;

    private StatusEgreso statusEgresoSelected;
    private List<StatusEgreso> lstStatusEgreso;
}
