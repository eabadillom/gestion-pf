package mx.com.ferbo.util;

import mx.com.ferbo.util.funcional.ThrowingRunnable;
import mx.com.ferbo.util.funcional.ThrowingSupplier;

public interface BaseBL<T extends Identificable<?>> {

    public T nuevo();

    default T nuevoOExistente(T entity) {
        return (entity.getId() == null) ? nuevo() : entity;
    }

    default <R> R operar (
            ThrowingSupplier<R, ? extends Exception> proveedorDatos,
            String operacion) throws InventarioException {

        try {
            return proveedorDatos.get();
        } catch (Exception ex) {
            throw new InventarioException(
                    "Hubo un problema al momento de " + operacion, ex);
        }
    }

    default void ejecutar(
            ThrowingRunnable<? extends Exception> operacion,
            String descripcion) throws InventarioException {

        try {
            operacion.run();
        } catch (Exception ex) {
            throw new InventarioException(
                    "Hubo un problema al momento de " + descripcion, ex);
        }
    }
}
