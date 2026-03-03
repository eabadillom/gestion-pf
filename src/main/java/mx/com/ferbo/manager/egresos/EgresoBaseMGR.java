package mx.com.ferbo.manager.egresos;

import java.util.List;

import mx.com.ferbo.business.egresos.EgresoBaseBL;
import mx.com.ferbo.model.categresos.CatEgreso;
import mx.com.ferbo.model.egresos.Egreso;
import mx.com.ferbo.util.InventarioException;

public abstract class EgresoBaseMGR  <T extends Egreso ,P extends Egreso, S extends CatEgreso>{
    
    protected abstract EgresoBaseBL<T, P, S> getBL();

    public String[] cargar(T entity) {
        entity = getBL().nuevoOExistente(entity);

        String mensaje;
        if (getId(entity) == null) {
            mensaje = "Preparando " + getBL().nombreHijo() + " nuevo...";
        } else {
            mensaje = getBL().nombreHijo() + " se ha cargado para edición";
        }
        return new String[]{"Cargar " + getBL().nombreHijo(), mensaje};
    }

    public String[] guardar(T entity, P padre) throws InventarioException {
        boolean esNuevo = (getId(entity) == null);
        getBL().operar(entity, padre);
        String mensaje;
        if (esNuevo) {
            mensaje = getBL().nombreHijo() + " del egreso se guardó correctamente";
        } else {
            mensaje = getBL().nombreHijo() + " del egreso se actualizó correctamente";
        }
        return new String[] {"Guardar/Actualizar " + getBL().nombreHijo(), mensaje};
    }

    public String[] obtenerLista(P padre, List<T> lista) throws InventarioException {
        List<T> nuevaLista = getBL().obtenerPorImporteEgreso(padre);
        lista.clear();
        lista.addAll(nuevaLista);

        String mensaje = "Se han cargado satisfactoriamente " + getBL().nombreHijos();

        return new String[]{"Cargar " + getBL().nombreHijos(), mensaje} ;
    }

    public String [] cambiarStatus(T entity, S status) throws InventarioException {
        entity = getBL().cambiar(entity, status);
        String mensaje = "El status de " + getBL().nombreHijo() + " se actualizó correctamente";
        return new String[] {"Cambiar status de " + getBL().nombreHijo(), mensaje};
    }

    // Método para obtener ID de T (puede ser abstracto o usar reflection según convenga)
    protected abstract Integer getId(T entity);
}
