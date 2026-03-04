package mx.com.ferbo.modulos.egresos.manager;

import java.util.List;
import java.util.function.Consumer;

import org.apache.poi.ss.formula.functions.T;

import mx.com.ferbo.modulos.egresos.model.CatEgreso;
import mx.com.ferbo.util.InventarioException;

public abstract class CatEgresoBaseMGR<T extends CatEgreso> {

    @FunctionalInterface
    public interface ThrowingSupplier<T> {
        List<T> get() throws InventarioException;
    }

    @FunctionalInterface
    public interface CheckedConsumer<T> {
        void accept(T t) throws InventarioException;
    }

    protected <T> String[] cargarVigentes(
            List<T> listaDestino,
            ThrowingSupplier<T> proveedorDatos,
            String titulo,
            String mensaje) throws InventarioException {

        List<T> nuevaLista = proveedorDatos.get();
        listaDestino.clear();
        listaDestino.addAll(nuevaLista);

        return new String[] { titulo, mensaje };
    }

    protected <T extends CatEgreso> String[] guardarCatalogo(
            T entidad,
            CheckedConsumer<T> accionGuardar,
            String nombreCatalogo) throws InventarioException {

        String titulo;
        String mensaje;

        boolean esNuevo = (entidad.getId() == null);

        accionGuardar.accept(entidad); // ahora puede lanzar InventarioException

        if (esNuevo) {
            titulo = "Guardar " + nombreCatalogo;
            mensaje = "El " + nombreCatalogo + " se guardó correctamente.";
        } else {
            titulo = "Actualizar " + nombreCatalogo;
            mensaje = "El " + nombreCatalogo + " se actualizó correctamente.";
        }

        return new String[] { titulo, mensaje };
    }
}
