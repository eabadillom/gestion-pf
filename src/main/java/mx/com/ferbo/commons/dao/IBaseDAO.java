package mx.com.ferbo.commons.dao;

import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.JPAEntity;

/**
 *
 * @author Gabriel Moreno <gabrielmos0309@gmail.com>
 */
public abstract class IBaseDAO<E, ID> {
	
	public EntityManager em = EntityManagerUtil.getEntityManager();
    
    public abstract E buscarPorId(ID id);

    public abstract List<E> buscarTodos();

    public abstract List<E> buscarPorCriterios(E e);

    public abstract String actualizar(E e);

    public abstract String guardar(E e);
    
    public abstract String eliminar(E e);
    
    public abstract String eliminarListado(List<E> listado);

}
