package co.tpg.catalog.dao;

import co.tpg.catalog.dao.exception.BackendException;

import java.util.List;

/**
 * DAO interface to decouple resources and backend.
 * @author Rod
 * @since 2019-10-06
 */
public interface DAO<T,K> {
    int PAGE_SIZE = 25;

    T create(T o) throws BackendException;
    T retrieveById(K key) throws BackendException;
    // To see how pagination works in DynamoDB go to: https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/Query.html#Query.Limit
    List<T> retrieveAll(K lastEvaluatedKey,int pageSize) throws BackendException;
    void update(T o) throws BackendException;
    void delete(T o) throws BackendException;
}
