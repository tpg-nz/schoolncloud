package co.tpg.workflow.function.dao;

import co.tpg.workflow.function.dao.exception.BackendException;
import java.util.List;

/**
 * DAO interface to decouple resources and backend.
 * @author Rod
 * @since 2019-10-06
 */
public interface DAO<T,K> {

    // Default page size
    int PAGE_SIZE = 25;

    // CRUD opeartions
    T create(T o) throws BackendException;
    T retrieveById(K key) throws BackendException;
    void update(T o) throws BackendException;
    void delete(T o) throws BackendException;

    // Retrieve paginating result
    List<T> retrieveAll(K lastEvaluatedKey, int pageSize) throws BackendException;
}
