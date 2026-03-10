package mx.com.ferbo.util;

import java.util.List;

import mx.com.ferbo.util.funcional.ThrowingConsumer;
import mx.com.ferbo.util.funcional.ThrowingSupplier;
import mx.com.ferbo.util.messaging.NivelMensaje;
import mx.com.ferbo.util.messaging.ResultadoOperacion;
import mx.com.ferbo.util.validation.ValidationException;
import mx.com.ferbo.util.funcional.ThrowingRunnable;

public interface BaseMGR {

    // -----------------------
    // Método genérico para cargar listas
    // -----------------------
    default <T> ResultadoOperacion cargar(
            List<T> listaDestino,
            ThrowingSupplier<List<T>, InventarioException> proveedorDatos,
            String titulo,
            String mensaje) throws InventarioException {

        try {
            List<T> nuevaLista = proveedorDatos.get();

            listaDestino.clear();

            int total = (nuevaLista != null) ? nuevaLista.size() : 0;
            if (nuevaLista != null) listaDestino.addAll(nuevaLista);

            return ResultadoOperacion.info(titulo, mensaje, total);

        } catch (ValidationException ex) {
            return ResultadoOperacion.error(
                    "Error de validación",
                    "Se encontraron errores al cargar la lista",
                    ex.getNotification().getMensajes()
            );
        }
    }

    // -----------------------
    // Método genérico para guardar/actualizar
    // -----------------------
    default <T extends Identificable<?>> ResultadoOperacion guardar(
            T entidad,
            ThrowingConsumer<T, InventarioException> accionGuardar,
            String nombreEntidad) throws InventarioException {

        boolean esNuevo = (entidad.getId() == null);

        try {
            accionGuardar.accept(entidad);

        } catch (ValidationException ex) {
            return ResultadoOperacion.error(
                    "Error de validación",
                    "Se encontraron errores en " + nombreEntidad,
                    ex.getNotification().getMensajes()
            );
        }

        String titulo = (esNuevo ? "Guardar " : "Actualizar ") + nombreEntidad;
        String mensaje = (esNuevo
                ? "El " + nombreEntidad + " se guardó correctamente."
                : "El " + nombreEntidad + " se actualizó correctamente.");

        return ResultadoOperacion.exito(titulo, mensaje, 1);
    }

    // -----------------------
    // Método genérico para cambiar estado
    // -----------------------
    default <T> ResultadoOperacion cambiarEstado(
            T entidad,
            ThrowingConsumer<T, InventarioException> accionCambioEstado,
            String nombreEntidad,
            String accion) throws InventarioException {

        try {
            accionCambioEstado.accept(entidad);

        } catch (ValidationException ex) {
            return ResultadoOperacion.error(
                    "Error de validación",
                    "Se encontraron errores al " + accion.toLowerCase() + " " + nombreEntidad,
                    ex.getNotification().getMensajes()
            );
        }

        String titulo = accion + " " + nombreEntidad;
        String mensaje = "El " + nombreEntidad + " se " + accion.toLowerCase() + " correctamente.";

        return ResultadoOperacion.exito(titulo, mensaje, 1);
    }

    // -----------------------
    // Método genérico para ejecutar cualquier operación
    // -----------------------
    default ResultadoOperacion ejecutarOperacion(
            ThrowingRunnable<InventarioException> accion,
            String titulo,
            String mensaje) throws InventarioException {

        try {
            accion.run();

            return ResultadoOperacion.exito(titulo, mensaje, 1);

        } catch (ValidationException ex) {
            return ResultadoOperacion.error(
                    "Error de validación",
                    "Se encontraron errores al ejecutar la operación",
                    ex.getNotification().getMensajes()
            );
        }
    }
}