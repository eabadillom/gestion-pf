package mx.com.ferbo.util.funcional;

/**
 * Consumer que puede lanzar cualquier excepción.
 * 
 * @param <T> tipo de entrada
 * @param <E> tipo de excepción lanzada
 */
@FunctionalInterface
public interface ThrowingConsumer<T, E extends Exception> {
    void accept(T t) throws E;
}