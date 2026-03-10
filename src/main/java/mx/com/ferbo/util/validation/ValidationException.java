package mx.com.ferbo.util.validation;

import mx.com.ferbo.util.InventarioException;

public class ValidationException extends InventarioException {

    private final Notification notification;

    public ValidationException(Notification notification) {
        super("Se encontraron errores de validación");
        this.notification = notification;
    }

    public Notification getNotification() {
        return notification;
    }
}
