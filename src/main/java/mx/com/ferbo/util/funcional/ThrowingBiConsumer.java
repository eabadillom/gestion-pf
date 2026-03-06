package mx.com.ferbo.util.funcional;

/**
 * BiConsumer que puede lanzar cualquier excepción.
 * 
 * @param <T> primer parámetro
 * @param <U> segundo parámetro
 * @param <E> tipo de excepción lanzada
 */
@FunctionalInterface
public interface ThrowingBiConsumer<T, U, E extends Exception> {
    void accept(T t, U u) throws E;
}