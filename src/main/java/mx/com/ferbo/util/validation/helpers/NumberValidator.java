package mx.com.ferbo.util.validation.helpers;

import mx.com.ferbo.util.validation.Notification;

public final class NumberValidator {

    private NumberValidator() {}

    public static void positive(Notification notification,
                                Integer value,
                                String campo) {

        if (value == null) {
            notification.addError(campo, campo + " no puede ser nulo");
            return;
        }

        if (value <= 0) {
            notification.addError(campo, campo + " debe ser positivo");
        }
    }
}