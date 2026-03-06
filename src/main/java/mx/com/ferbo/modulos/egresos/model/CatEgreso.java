package mx.com.ferbo.modulos.egresos.model;

import mx.com.ferbo.util.Identificable;

public interface CatEgreso<T> extends Identificable<T> {
    String getNombre();
    void setNombre(String nombre);

    String getDescripcion();
    void setDescripcion(String descripcion);

    Boolean getVigente();
    void setVigente(Boolean vigente);
}
