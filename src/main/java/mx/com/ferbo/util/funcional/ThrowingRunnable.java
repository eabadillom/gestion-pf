package mx.com.ferbo.util.funcional;

/**
 * Runnable que puede lanzar cualquier excepción.
 * 
 * @param <E> tipo de excepción lanzada
 * 
 * No recibe ningun tipo de argumento, y no devuelve nada.
 */
@FunctionalInterface
public interface ThrowingRunnable<E extends Exception> {
    void run() throws E;
}