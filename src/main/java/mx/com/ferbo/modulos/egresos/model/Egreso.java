
package mx.com.ferbo.modulos.egresos.model;

import mx.com.ferbo.util.Identificable;

public interface Egreso<T, C> extends Identificable<T> {
    void setStatus(C entity);
}
