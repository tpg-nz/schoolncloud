package co.tpg.workflow.function.exception;

/**
 * Functional interface for lambda exception throwing
 * @author Andrej
 * @since 2019-10-12
 */
@FunctionalInterface
public interface FunctionWithException<T, R, E extends Exception> {
    R apply(T t) throws E;
}
