package mx.com.ferbo.manager.categresos;

import java.util.List;

import javax.inject.Inject;

import mx.com.ferbo.business.categresos.CatConceptoEgresoBL;
import mx.com.ferbo.model.categresos.CatConceptoEgreso;
import mx.com.ferbo.model.categresos.CategoriaEgreso;
import mx.com.ferbo.util.InventarioException;

public class CatConCeptoEgresoMGR {

    @Inject
    private CatConceptoEgresoBL catConceptoEgresoBL;

    public String[] obtenerPorCategoriaYVigente(CategoriaEgreso categoria, List<CatConceptoEgreso> lst) throws InventarioException{
        Boolean vigente = Boolean.TRUE;
        List<CatConceptoEgreso> nuevaLista = catConceptoEgresoBL.obtenerPorCategoriaYVigencia(categoria, vigente);
        lst.clear();
        lst.addAll(nuevaLista);
        
        String mensaje = "Se han cagado exitosamente los conceptos asociados a la cateogira " + categoria.getNombre() + ".";
        String titulo = "Cargar conceptos de egreso";

        return new String[]{titulo, mensaje};
    }

}
