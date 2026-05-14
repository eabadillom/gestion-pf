package mx.com.ferbo.egresos.business.catalogos;

import java.util.List;

import com.ferbo.tools.exception.SystemException;

public interface CatalogoBL<T> {

    T buscarPorClave(String clave)  throws SecurityException;

    List<T> buscarActivos(Boolean activo) throws SystemException;

    void guardar(T entity) throws SystemException;
    
}
