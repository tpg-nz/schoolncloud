package co.tpg.workflow.dao.exception;

/**
 * Exception class to decouple resources and backend.
 * @author Rod
 * @since 2019-10-06
 */
public class BackendException extends Exception {
    public BackendException(String message) {
        super(message);
    }

    public BackendException(String message, Throwable cause) {
        super(message, cause);
    }
}
