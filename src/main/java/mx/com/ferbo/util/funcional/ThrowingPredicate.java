package mx.com.ferbo.util.funcional;

/**
 * Predicate que puede lanzar cualquier excepción.
 * 
 * @param <T> tipo de entrada
 * @param <E> tipo de excepción lanzada
 */
@FunctionalInterface
public interface ThrowingPredicate<T, E extends Exception> {
    boolean test(T t) throws E;
}