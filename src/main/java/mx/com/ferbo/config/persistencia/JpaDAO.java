package mx.com.ferbo.config.persistencia;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.ferbo.arch.core.config.DAO;

public abstract class JpaDAO<T, ID> implements DAO<T, ID>{

    @PersistenceContext
    protected EntityManager em;

    protected Class<T> entityClass;

    public JpaDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public Optional<T> find(ID id) {
        return Optional.ofNullable(em.find(entityClass, id));
    }

    @Override
    public T save(T entity) {
        return em.merge(entity);
    }

    @Override
    public void delete(T entity) {
        em.remove(em.contains(entity) ? entity : em.merge(entity));
    }

    @Override
    public boolean contains(T entity) {
        return em.contains(entity);
    }

}
