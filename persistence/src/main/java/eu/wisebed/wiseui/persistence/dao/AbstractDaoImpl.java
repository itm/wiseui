package eu.wisebed.wiseui.persistence.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Abstract dao that provides implementations for the typical data
 * access operations.
 *
 * @param <T> Type of the business objects (BOs) on which the DAO shall
 *            operate.
 */
public abstract class AbstractDaoImpl<T> implements Dao<T> {

    private Class<T> persistentClass;

    private EntityManager entityManager;

    public AbstractDaoImpl(final Class<T> persistentClass) {
        this.persistentClass = persistentClass;
    }

    @Override
    public void persist(final T t) {
        entityManager.persist(t);
    }

    @Override
    public T update(final T t) {
        return entityManager.merge(t);
    }

    @Override
    public void remove(final T t) {
        entityManager.remove(t);
    }

    @Override
    public T findById(final Integer id) {
        return entityManager.find(this.persistentClass, id);
    }

    @Override
    public List<T> findAll() {
        return entityManager.createQuery(
                String.format("SELECT x FROM %s x", persistentClass.getName()), persistentClass).getResultList();
    }

    public Class<T> getPersistentClass() {
        return persistentClass;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setPersistentClass(final Class<T> persistentClass) {
        this.persistentClass = persistentClass;
    }

    /**
     * Used by Spring to inject the configured {@link javax.persistence.EntityManager}.
     *
     * @param entityManager JPA {@link javax.persistence.EntityManager} object
     */
    @PersistenceContext
    public void setEntityManager(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
