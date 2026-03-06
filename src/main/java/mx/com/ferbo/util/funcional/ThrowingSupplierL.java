package mx.com.ferbo.util.funcional;

import java.util.List;

/**
 * Supplier que puede lanzar cualquier excepción.
 * 
 * @param <T> tipo de retorno
 * @param <E> tipo de excepción lanzada
 */
@FunctionalInterface
public interface ThrowingSupplierL<T, E extends Exception> {
    List<T> get() throws E;
}
