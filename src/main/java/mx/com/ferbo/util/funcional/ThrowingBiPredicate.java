package mx.com.ferbo.util.funcional;

/**
 * BiPredicate que puede lanzar cualquier excepción.
 * 
 * @param <T> primer parámetro
 * @param <U> segundo parámetro
 * @param <E> tipo de excepción lanzada
 */
@FunctionalInterface
public interface ThrowingBiPredicate<T, U, E extends Exception> {
    boolean test(T t, U u) throws E;
}
