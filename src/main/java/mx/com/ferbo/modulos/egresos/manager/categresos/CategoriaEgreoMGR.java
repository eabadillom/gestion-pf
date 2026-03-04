package mx.com.ferbo.modulos.egresos.manager.categresos;

import java.util.List;

import javax.inject.Inject;

import mx.com.ferbo.modulos.egresos.business.catprimarios.CategoriaEgresoBL;
import mx.com.ferbo.modulos.egresos.model.catprimarios.CategoriaEgreso;
import mx.com.ferbo.modulos.egresos.model.catprimarios.TipoEgreso;
import mx.com.ferbo.util.InventarioException;

public class CategoriaEgreoMGR {

    @Inject
    private CategoriaEgresoBL categoriaEgresoBL;
    
    public String[] obtenerPorTipoEgresoYVigente(TipoEgreso tipo, List<CategoriaEgreso> lst) throws InventarioException {
        Boolean vigente = Boolean.TRUE;
        List<CategoriaEgreso> nuevaLista = categoriaEgresoBL.obtenerCateogiasPorTipoYEstado(tipo, vigente);
        lst.clear();
        lst.addAll(nuevaLista);
        
        String mensaje = "Se han cagado exitosamente las categorias asociada al tipo " + tipo.getNombre() + ".";
        String titulo = "Cargar categorias de egreso";

        return new String[]{titulo, mensaje};
    }
}
