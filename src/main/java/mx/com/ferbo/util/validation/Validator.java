package mx.com.ferbo.util.validation;

import java.math.BigDecimal;

import mx.com.ferbo.util.validation.helpers.MonetaryValidator;
import mx.com.ferbo.util.validation.helpers.NumberValidator;
import mx.com.ferbo.util.validation.helpers.ObjectValidator;
import mx.com.ferbo.util.validation.helpers.TextValidator;

public class Validator {
    private final Notification notification;

    private Validator(Notification notification) {
        this.notification = notification;
    }

    public static Validator of(Notification notification) {
        return new Validator(notification);
    }

    public Validator notNull(Object value, String campo) {
        ObjectValidator.notNull(notification, value, campo);
        return this;
    }

    public Validator notBlank(String value, String campo) {
        TextValidator.notBlank(notification, value, campo);
        return this;
    }

    public Validator positive(Integer value, String campo) {
        NumberValidator.positive(notification, value, campo);
        return this;
    }

    public Validator positive(BigDecimal value, String campo) {
        MonetaryValidator.positive(notification, value, campo);
        return this;
    }
}
