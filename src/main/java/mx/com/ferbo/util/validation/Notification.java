package mx.com.ferbo.util.validation;

import java.util.ArrayList;
import java.util.List;

import mx.com.ferbo.util.messaging.Mensaje;
import mx.com.ferbo.util.messaging.NivelMensaje;

public class Notification {

    private final List<Mensaje> mensajes = new ArrayList<>();

    public void addError(String campo, String mensaje) {
        mensajes.add(new Mensaje(campo, mensaje, NivelMensaje.ERROR));
    }

    public void addWarning(String mensaje) {
        mensajes.add(new Mensaje(null, mensaje, NivelMensaje.WARNING));
    }

    public boolean hasErrors() {
        return mensajes.stream()
                .anyMatch(m -> m.getNivel() == NivelMensaje.ERROR);
    }

    public List<Mensaje> getMensajes() {
        return mensajes;
    }
}