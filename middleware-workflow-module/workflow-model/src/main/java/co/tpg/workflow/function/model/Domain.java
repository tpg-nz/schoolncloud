package co.tpg.workflow.function.model;

/**
 * Generic interface to domain model classes.
 * @author Rod
 * @since 2019-03-04
 */
public interface Domain<T> {
    T getId();
    void setId(T id);
}
