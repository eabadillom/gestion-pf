package mx.com.ferbo.util.funcional;

/**
 * BiFunction que puede lanzar cualquier excepción.
 * 
 * @param <T> primer parámetro
 * @param <U> segundo parámetro
 * @param <R> tipo de retorno
 * @param <E> tipo de excepción lanzada
 */
@FunctionalInterface
public interface ThrowingBiFunction<T, U, R, E extends Exception> {
    R apply(T t, U u) throws E;
}
