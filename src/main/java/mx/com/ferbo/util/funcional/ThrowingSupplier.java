package mx.com.ferbo.util.funcional;

/**
 * Supplier que puede lanzar cualquier excepción.
 * 
 * @param <T> tipo de retorno
 * @param <E> tipo de excepción lanzada
 */
@FunctionalInterface
public interface ThrowingSupplier<T, E extends Exception> {
    T get() throws E;
}
