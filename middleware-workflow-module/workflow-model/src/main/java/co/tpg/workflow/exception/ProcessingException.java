package co.tpg.workflow.exception;

import lombok.*;

@Builder
@Getter
@Setter
@ToString
public class ProcessingException extends Exception {
    private String message;
}
