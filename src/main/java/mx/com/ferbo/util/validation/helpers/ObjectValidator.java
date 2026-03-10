package mx.com.ferbo.util.validation.helpers;

import mx.com.ferbo.util.validation.Notification;

public final class ObjectValidator {

    private ObjectValidator() {}

    public static void notNull(Notification notification,
                               Object value,
                               String campo) {

        if (value == null) {
            notification.addError(campo, campo + " no puede ser nulo");
        }
    }
}