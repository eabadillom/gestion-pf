package mx.com.ferbo.util.validation.helpers;

import mx.com.ferbo.util.validation.Notification;

public final class TextValidator {

    private TextValidator() {}

    public static void notBlank(Notification notification,
                                String value,
                                String campo) {

        if (value == null || value.trim().isEmpty()) {
            notification.addError(campo, campo + " no puede estar vacío");
        }
    }

    public static void maxLength(Notification notification,
                                 String value,
                                 int max,
                                 String campo) {

        if (value != null && value.length() > max) {
            notification.addError(
                    campo,
                    campo + " no puede tener más de " + max + " caracteres");
        }
    }
}