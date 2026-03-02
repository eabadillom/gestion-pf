package mx.com.ferbo.manager.egresos;

import javax.inject.Inject;

import mx.com.ferbo.business.egresos.ConceptoEgresoBL;
import mx.com.ferbo.business.egresos.EgresoBaseBL;
import mx.com.ferbo.model.categresos.CatConceptoEgreso;
import mx.com.ferbo.model.categresos.StatusEgreso;
import mx.com.ferbo.model.egresos.ConceptoEgreso;
import mx.com.ferbo.util.InventarioException;

public class ConceptoEgresoMGR extends EgresoBaseMGR<ConceptoEgreso, CatConceptoEgreso, StatusEgreso> {

    @Inject
    private ConceptoEgresoBL conceptoBL;

    @Override
    protected EgresoBaseBL<ConceptoEgreso, CatConceptoEgreso, StatusEgreso> getBL() {
        return conceptoBL;
    }

    @Override
    protected Integer getId(ConceptoEgreso entity) {
        return entity.getId();
    }

    public String aplicarCatalogo(ConceptoEgreso concepto, CatConceptoEgreso catConcepto)
            throws InventarioException {

        if (concepto == null) {
            return "No hay concepto para actualizar.";
        }

        if (catConcepto == null) {
            return "Debe seleccionar un concepto del catálogo.";
        }

        conceptoBL.extraerDeCatalogo(concepto, catConcepto);

        return "Se aplicaron correctamente las configuraciones del catálogo al egreso.";
    }

}
