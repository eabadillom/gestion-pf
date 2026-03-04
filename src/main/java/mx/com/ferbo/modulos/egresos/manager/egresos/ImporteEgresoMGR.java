package mx.com.ferbo.modulos.egresos.manager.egresos;

import java.time.YearMonth;
import java.util.List;

import javax.inject.Inject;

import mx.com.ferbo.modulos.egresos.business.EgresoBaseBL;
import mx.com.ferbo.modulos.egresos.business.egreso.ImporteEgresoBL;
import mx.com.ferbo.modulos.egresos.manager.EgresoBaseMGR;
import mx.com.ferbo.modulos.egresos.model.catprimarios.CatConceptoEgreso;
import mx.com.ferbo.modulos.egresos.model.catsecundarios.StatusEgreso;
import mx.com.ferbo.modulos.egresos.model.egreso.ConceptoEgreso;
import mx.com.ferbo.modulos.egresos.model.egreso.ImporteEgreso;
import mx.com.ferbo.modulos.empresa.model.EmisorCFDI;
import mx.com.ferbo.util.DAOException;
import mx.com.ferbo.util.InventarioException;

public class ImporteEgresoMGR extends EgresoBaseMGR<ImporteEgreso, ConceptoEgreso, StatusEgreso> {

    @Inject
    private ImporteEgresoBL egresoBL;

    @Override
    protected EgresoBaseBL<ImporteEgreso, ConceptoEgreso, StatusEgreso> getBL() {
        return egresoBL;
    }

    @Override
    protected Integer getId(ImporteEgreso entity) {
        return entity.getId();
    }

    public String cargar(Integer id, ImporteEgreso egreso) throws InventarioException {
        egreso = egresoBL.nuevoOExistente(id);

        String mensaje;
        if (id == null) {
            mensaje = "Preparando " + egresoBL.nombreHijo() + " nuevo...";
        } else {
            mensaje = egresoBL.nombreHijo() + " se ha cargado para edición";
        }
        return mensaje;
    }

    public String obtenerPorFiltros(
            CatConceptoEgreso concepto,
            EmisorCFDI razon,
            YearMonth mes,
            List<ImporteEgreso> egresos)
            throws InventarioException, DAOException {

        List<ImporteEgreso> nuevaLista = egresoBL.obtenerPorFiltros(concepto, razon, mes);

        egresos.clear();
        egresos.addAll(nuevaLista);

        if (nuevaLista.isEmpty()) {
            return "No se encontraron egresos con los filtros seleccionados.";
        }

        return "Se encontraron " + nuevaLista.size() + " egresos.";
    }

    public String desglosarImpuestos(ImporteEgreso egreso) throws InventarioException {

        egreso = egresoBL.desglosarIvaIeps(egreso);

        return "Impuestos desglosados correctamente: IVA=" + egreso.getIva() + ", IEPS=" + egreso.getIeps();
    }
}
