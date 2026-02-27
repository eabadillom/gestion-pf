package mx.com.ferbo.manager.egresos;

import java.time.YearMonth;
import java.util.List;

import javax.inject.Inject;

import mx.com.ferbo.business.egresos.ImporteEgresoBL;
import mx.com.ferbo.model.categresos.CatConceptoEgreso;
import mx.com.ferbo.model.egresos.ConceptoEgreso;
import mx.com.ferbo.model.egresos.ImporteEgreso;
import mx.com.ferbo.model.empresa.NEmisoresCFDIS;
import mx.com.ferbo.util.DAOException;
import mx.com.ferbo.util.InventarioException;

public class EgresoMGR {

    @Inject
    private ImporteEgresoBL egresoBL;

    public ImporteEgreso nuevoOExistente(Integer id) {
        if (id == null) {
            ImporteEgreso egreso = egresoBL.nuevo();
            egreso.setStatus(egresoBL.estadoBorrador());
            return egreso;
        }
        return egresoBL.obtenerPorId(id);
    }

    public String guardar(ImporteEgreso egreso, ConceptoEgreso concepto) throws InventarioException {
        return egresoBL.operar(egreso, concepto);
    }

    public List<ImporteEgreso> obtenerLista(CatConceptoEgreso concepto, NEmisoresCFDIS razon, YearMonth mes)
            throws InventarioException, DAOException {
        return egresoBL.obtenerPorFiltros(concepto, razon, mes);
    }

}
