package mx.com.ferbo.util;

import java.math.BigDecimal;

public final class MonetaryValidationUtils {

    private MonetaryValidationUtils() { }

    public static void requireNonNull(BigDecimal value, String mensaje) {
        if (value == null) {
            throw new IllegalArgumentException(mensaje + " no puede ser nulo");
        }
    }

    public static void requirePositive(BigDecimal value, String mensaje) {
        requireNonNull(value, mensaje);
        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(mensaje + " debe ser positivo");
        }
    }

    public static void requireNonNegative(BigDecimal value, String mensaje) {
        requireNonNull(value, mensaje);
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(mensaje + " no puede ser negativo");
        }
    }

    public static void requireInRange(BigDecimal value, BigDecimal min, BigDecimal max, String mensaje) {
        requireNonNull(value, mensaje);
        if (value.compareTo(min) < 0 || value.compareTo(max) > 0) {
            throw new IllegalArgumentException(mensaje + " debe estar entre " + min + " y " + max);
        }
    }

}
