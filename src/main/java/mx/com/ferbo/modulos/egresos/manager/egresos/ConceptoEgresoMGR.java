package mx.com.ferbo.modulos.egresos.manager.egresos;

import javax.inject.Inject;

import mx.com.ferbo.modulos.egresos.business.EgresoBaseBL;
import mx.com.ferbo.modulos.egresos.business.egreso.ConceptoEgresoBL;
import mx.com.ferbo.modulos.egresos.manager.EgresoBaseMGR;
import mx.com.ferbo.modulos.egresos.model.catprimarios.CatConceptoEgreso;
import mx.com.ferbo.modulos.egresos.model.catsecundarios.StatusEgreso;
import mx.com.ferbo.modulos.egresos.model.egreso.ConceptoEgreso;
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
