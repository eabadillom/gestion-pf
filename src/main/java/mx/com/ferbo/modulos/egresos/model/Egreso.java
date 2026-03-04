
package mx.com.ferbo.modulos.egresos.model;

public interface Egreso<T> {
    Integer getId();
    void setStatus(T entity);
}
