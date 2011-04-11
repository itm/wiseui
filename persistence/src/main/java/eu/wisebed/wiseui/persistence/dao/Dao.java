package eu.wisebed.wiseui.persistence.dao;

import java.util.List;

/**
 * Generic interface for a dao that encompasses all typical operations
 * that needs to be carried out on business objects.
 *
 * @param <T> type of the business objects on which the dao shall
 *            operate.
 */
public interface Dao<T> {

    T findById(Integer id);

    T update(T object);

    void persist(T object);

    void remove(T object);

    List<T> findAll();
}
