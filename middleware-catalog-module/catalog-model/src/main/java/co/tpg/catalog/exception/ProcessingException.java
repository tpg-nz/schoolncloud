package co.tpg.catalog.exception;

import lombok.*;

@Builder
@Getter
@Setter
@ToString
public class ProcessingException extends Exception {
    private String message;
}
