package mx.com.ferbo.modulos.egresos.manager;

import java.util.List;

import org.apache.hc.core5.util.Identifiable;
import org.apache.poi.ss.formula.functions.T;

import mx.com.ferbo.modulos.egresos.model.CatEgreso;
import mx.com.ferbo.util.Identificable;
import mx.com.ferbo.util.InventarioException;
import mx.com.ferbo.util.NivelMensaje;
import mx.com.ferbo.util.ResultadoOperacion;
import mx.com.ferbo.util.funcional.ThrowingConsumer;
import mx.com.ferbo.util.funcional.ThrowingSupplierL;

public abstract class CatEgresoBaseMGR<T extends Identificable<?>> {

    protected <T> ResultadoOperacion cargar(
            List<T> listaDestino,
            ThrowingSupplierL<T, InventarioException> proveedorDatos,
            String titulo,
            String mensaje) throws InventarioException {

        List<T> nuevaLista = proveedorDatos.get();

        listaDestino.clear();

        int total = 0;

        if (nuevaLista != null) {
            listaDestino.addAll(nuevaLista);
            total = nuevaLista.size();
        }

        return new ResultadoOperacion(
                titulo,
                mensaje,
                total,
                NivelMensaje.INFO);
    }

    protected <T extends Identificable<?>> ResultadoOperacion guardar(
            T entidad,
            ThrowingConsumer<T, InventarioException> accionGuardar,
            String nombreCatalogo) throws InventarioException {

        boolean esNuevo = (entidad.getId() == null);

        accionGuardar.accept(entidad);

        String titulo = esNuevo
                ? "Guardar " + nombreCatalogo
                : "Actualizar " + nombreCatalogo;

        String mensaje = esNuevo
                ? "El " + nombreCatalogo + " se guardó correctamente."
                : "El " + nombreCatalogo + " se actualizó correctamente.";

        return new ResultadoOperacion(
                titulo,
                mensaje,
                1,
                NivelMensaje.INFO);
    }

    protected <T> ResultadoOperacion cambiarEstado(
            T entidad,
            ThrowingConsumer<T, InventarioException> accionCambioEstado,
            String nombreEntidad,
            String nuevoEstado) throws InventarioException {

        accionCambioEstado.accept(entidad);

        String titulo = "Cambiar estado de " + nombreEntidad;
        String mensaje = "El " + nombreEntidad + " ahora está en estado: " + nuevoEstado + ".";

        return new ResultadoOperacion(
                titulo,
                mensaje,
                1,
                NivelMensaje.INFO);
    }

}
