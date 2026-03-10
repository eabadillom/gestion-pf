package mx.com.ferbo.util;

import mx.com.ferbo.util.funcional.ThrowingRunnable;
import mx.com.ferbo.util.funcional.ThrowingSupplier;
import mx.com.ferbo.util.messaging.NivelMensaje;
import mx.com.ferbo.util.messaging.ResultadoOperacion;
import mx.com.ferbo.util.validation.ValidationException;

/**
 * Interfaz base para BL que maneja operaciones con Resultados y validaciones.
 *
 * @param <T> tipo de entidad
 */
public interface BaseBL<T extends Identificable<?>> {

    // Crear nueva entidad
    T nuevo();

    // Devuelve una nueva entidad si id es null, sino la existente
    default T nuevoOExistente(T entity) {
        return (entity.getId() == null) ? nuevo() : entity;
    }

    /**
     * Ejecuta un proveedor de datos y devuelve ResultadoOperacion.
     * Captura ValidationException y la convierte en ResultadoOperacion con errores.
     */
    default <R> ResultadoOperacion operar(
            ThrowingSupplier<R, ? extends Exception> proveedorDatos,
            String descripcion) throws InventarioException {

        try {
            proveedorDatos.get();
            return ResultadoOperacion.exito(descripcion, "Se completó correctamente", 1);

        } catch (Exception ex) {
            if (ex instanceof ValidationException) {
                ValidationException vex = (ValidationException) ex;
                return ResultadoOperacion.builder()
                        .titulo("Error de validación")
                        .mensaje("Se encontraron errores al " + descripcion)
                        .nivel(NivelMensaje.ERROR)
                        .addMensajes(vex.getNotification().getMensajes())
                        .build();
            }
            // Cualquier otra excepción se lanza como InventarioException
            throw new InventarioException("Hubo un problema al " + descripcion, ex);
        }
    }

    /**
     * Ejecuta un Runnable de negocio y devuelve ResultadoOperacion.
     * Captura ValidationException y la convierte en ResultadoOperacion con errores.
     */
    default ResultadoOperacion ejecutar(
            ThrowingRunnable<? extends Exception> operacion,
            String descripcion) throws InventarioException {

        try {
            operacion.run();
            return ResultadoOperacion.exito(descripcion, "Se completó correctamente", 1);

        } catch (Exception ex) {
            if (ex instanceof ValidationException) {
                ValidationException vex = (ValidationException) ex;
                return ResultadoOperacion.builder()
                        .titulo("Error de validación")
                        .mensaje("Se encontraron errores al " + descripcion)
                        .nivel(NivelMensaje.ERROR)
                        .addMensajes(vex.getNotification().getMensajes())
                        .build();
            }
            throw new InventarioException("Hubo un problema al " + descripcion, ex);
        }
    }

}