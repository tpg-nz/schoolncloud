package co.tpg.workflow.exception;

import lombok.*;

/**
 * Processing exception class
 * @author Andrej
 * @since 2019-10-10
 */
@Builder
@Getter
@Setter
@ToString
public class ProcessingException extends Exception {
    private String message;
}
