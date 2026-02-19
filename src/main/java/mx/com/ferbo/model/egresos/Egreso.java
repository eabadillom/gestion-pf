
package mx.com.ferbo.model.egresos;

public interface Egreso<T> {
    Integer getId();
    void setStatus(T entity);
}
