package mx.com.ferbo.util;

public final class ValidationUtils {

    private ValidationUtils() {}

    public static void requireNonNull(Object obj, String mensaje)
            throws InventarioException {
        if (obj == null) {
            throw new InventarioException(mensaje);
        }
    }

    public static <T> T requireNonNullWithReturn(T obj, String mensaje)
            throws InventarioException {
        if (obj == null) {
            throw new InventarioException(mensaje);
        }
        return obj;
    }
}
