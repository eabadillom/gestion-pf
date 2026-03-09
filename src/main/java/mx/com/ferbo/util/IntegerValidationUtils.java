package mx.com.ferbo.util;

public final class IntegerValidationUtils {

    private IntegerValidationUtils() {
    }

    public static void requireNonNull(Integer value, String mensaje) {
        if (value == null) {
            throw new IllegalArgumentException(mensaje + " no puede ser nulo");
        }
    }

    public static void requirePositive(Integer value, String mensaje) {
        requireNonNull(value, mensaje);
        if (value <= 0) {
            throw new IllegalArgumentException(mensaje + " debe ser positivo");
        }
    }

    public static void requireNonNegative(Integer value, String mensaje) {
        requireNonNull(value, mensaje);
        if (value < 0) {
            throw new IllegalArgumentException(mensaje + " no puede ser negativo");
        }
    }

    public static void requireInRange(Integer value, int min, int max, String mensaje) {
        requireNonNull(value, mensaje);
        if (value < min || value > max) {
            throw new IllegalArgumentException(
                    mensaje + " debe estar entre " + min + " y " + max);
        }
    }

}