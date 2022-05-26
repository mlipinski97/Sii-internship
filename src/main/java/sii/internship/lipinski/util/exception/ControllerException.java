package sii.internship.lipinski.util.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class ControllerException extends Exception {
    private final Integer errorCode;
    private final HttpStatus errorStatus;

    protected ControllerException(String errorMessage, Integer errorCode, HttpStatus errorStatus) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorStatus = errorStatus;
    }
}
