package mx.com.ferbo.modulos.egresos.manager.categresos;

import java.util.List;

import javax.inject.Inject;

import mx.com.ferbo.modulos.egresos.business.catprimarios.TipoEgresoBL;
import mx.com.ferbo.modulos.egresos.model.catprimarios.TipoEgreso;
import mx.com.ferbo.util.InventarioException;

public class TipoEgresoMGR {

    @Inject
    private TipoEgresoBL tipoEgresoBL;

    public String[] obtenerTodosVigentes(List<TipoEgreso> lst) throws InventarioException {
        Boolean vigente = Boolean.TRUE;
        List<TipoEgreso> nuevaLista = tipoEgresoBL.vigentesONoVigentes(vigente);
        lst.clear();
        lst.addAll(nuevaLista);
        
        String mensaje = "Se han cagado exitosamente los tipos de egreso vigentes.";
        String titulo = "Cargar tipos de egreso";

        return new String[]{titulo, mensaje};
    }
}
