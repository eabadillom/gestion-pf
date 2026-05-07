package mx.com.ferbo.egresos.business.catalogos;

import java.util.List;

import mx.com.ferbo.util.InventarioException;

public interface CatalogoBL<T> {

    T buscarPorClave(String clave)  throws InventarioException;

    List<T> buscarActivos(Boolean activo) throws InventarioException;

    void guardar(T entity) throws InventarioException;
    
}
