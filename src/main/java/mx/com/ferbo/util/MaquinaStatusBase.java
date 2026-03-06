package mx.com.ferbo.util;

import java.util.Map;
import java.util.Set;
import java.util.Collections;

public class MaquinaStatusBase<T> {

    private final Map<T, Set<T>> transiciones;

    public MaquinaStatusBase(Map<T, Set<T>> transiciones) {
        this.transiciones = transiciones;
    }

    public void conTransicionValida(T actual, T nuevo, Runnable accion) {

        Set<T> posibles = transiciones.getOrDefault(actual, Collections.emptySet());

        if (!posibles.contains(nuevo)) {
            throw new IllegalStateException(
                    "No se puede cambiar de " + actual + " a " + nuevo);
        }

        accion.run();
    }
}