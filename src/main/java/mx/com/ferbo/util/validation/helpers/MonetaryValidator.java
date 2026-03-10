package mx.com.ferbo.util.validation.helpers;

import java.math.BigDecimal;

import mx.com.ferbo.util.validation.Notification;

public final class MonetaryValidator {

    private MonetaryValidator() {}

    public static void positive(Notification notification,
                                BigDecimal value,
                                String campo) {

        if (value == null) {
            notification.addError(campo, campo + " no puede ser nulo");
            return;
        }

        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            notification.addError(campo, campo + " debe ser mayor que cero");
        }
    }
}