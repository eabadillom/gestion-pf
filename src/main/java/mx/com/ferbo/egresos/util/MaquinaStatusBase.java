package mx.com.ferbo.egresos.util;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import com.ferbo.tools.exception.BusinessException;

public class MaquinaStatusBase<T> {

    private final Map<T, Set<T>> transiciones;

    public MaquinaStatusBase(Map<T, Set<T>> transiciones) {
        this.transiciones = transiciones;
    }

    public void transicionValida(T actual, T nuevo, String nombreActual, String nombreNuevo) {

        Set<T> posibles = transiciones.getOrDefault(actual, Collections.emptySet());

        if (!posibles.contains(nuevo)) {
            throw new BusinessException(
                    "No se puede cambiar de " + nombreActual + " a " + nombreNuevo);
        }
    }
}