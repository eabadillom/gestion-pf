package mx.com.ferbo.util.funcional;

/**
 * Function que puede lanzar cualquier excepción.
 * 
 * @param <T> tipo de entrada
 * @param <R> tipo de retorno
 * @param <E> tipo de excepción lanzada
 */
@FunctionalInterface
public interface ThrowingFunction<T, R, E extends Exception> {
    R apply(T t) throws E;
}