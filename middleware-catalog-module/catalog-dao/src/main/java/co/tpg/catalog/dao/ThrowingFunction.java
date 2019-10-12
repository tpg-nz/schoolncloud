package co.tpg.catalog.dao;

import java.util.function.Function;

/**
 * Functional interface to implement a lambda function that throws BackendException.
 * @author Rod
 * @since 2019-10-06
 */
@FunctionalInterface
public interface ThrowingFunction<T,R,E extends Throwable> {
    R apply(T t) throws E;
    static <T, R, E extends Throwable> Function<T, R> unchecked(ThrowingFunction<T, R, E> f) {
        return t -> {
            try {
                return f.apply(t);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        };
    }
}
