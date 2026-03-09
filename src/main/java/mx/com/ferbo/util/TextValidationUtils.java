package mx.com.ferbo.util;

public final class TextValidationUtils {

    private TextValidationUtils() {
    }

    public static void requireNonNull(String value, String mensaje) {
        if (value == null) {
            throw new IllegalArgumentException(mensaje + " no puede ser nulo");
        }
    }

    public static void requireNonBlank(String value, String mensaje) {
        requireNonNull(value, mensaje);
        if (value.trim().isEmpty()) {
            throw new IllegalArgumentException(mensaje + " no puede estar vacío");
        }
    }

    public static void requireMinLength(String value, int minLength, String mensaje) {
        requireNonBlank(value, mensaje);
        if (value.length() < minLength) {
            throw new IllegalArgumentException(
                    mensaje + " debe tener al menos " + minLength + " caracteres");
        }
    }

    public static void requireMaxLength(String value, int maxLength, String mensaje) {
        requireNonBlank(value, mensaje);
        if (value.length() > maxLength) {
            throw new IllegalArgumentException(
                    mensaje + " no puede tener más de " + maxLength + " caracteres");
        }
    }

    public static void requireLengthInRange(String value, int min, int max, String mensaje) {
        requireNonBlank(value, mensaje);
        int length = value.length();

        if (length < min || length > max) {
            throw new IllegalArgumentException(
                    mensaje + " debe tener entre " + min + " y " + max + " caracteres");
        }
    }

    public static void requireMatches(String value, String regex, String mensaje) {
        requireNonBlank(value, mensaje);
        if (!value.matches(regex)) {
            throw new IllegalArgumentException(mensaje + " tiene un formato inválido");
        }
    }

}