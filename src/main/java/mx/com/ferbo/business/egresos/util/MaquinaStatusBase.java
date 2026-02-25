package mx.com.ferbo.business.egresos.util;

import java.util.Map;
import java.util.Set;
import java.util.Collections;

public class MaquinaStatusBase<T> {

    private final Map<String, Set<String>> transiciones;

    public MaquinaStatusBase(Map<String, Set<String>> transiciones) {
        this.transiciones = transiciones;
    }

    /**
     * Valida si se puede cambiar de estado.
     * @param actual Estado actual
     * @param nuevo Estado al que se desea pasar
     * @throws IllegalStateException si la transición no es válida
     */
    public void validarTransicion(String actual, String nuevo) {
        Set<String> posibles = transiciones.getOrDefault(actual, Collections.<String>emptySet());
        if (!posibles.contains(nuevo)) {
            throw new IllegalStateException("No se puede cambiar de " + actual + " a " + nuevo );
        }
    }
}