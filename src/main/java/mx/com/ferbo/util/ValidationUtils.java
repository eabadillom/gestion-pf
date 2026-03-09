package mx.com.ferbo.util;

import java.util.Collection;
import java.util.List;

public final class ValidationUtils {

    private ValidationUtils() {
    }

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

    public static <T> void requireNonEmpty(Collection<T> collection, String message)
            throws InventarioException {

        if (collection == null || collection.isEmpty()) {
            throw new InventarioException(message);
        }

    }
}
