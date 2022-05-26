package sii.internship.lipinski.util.exception;

import org.springframework.http.HttpStatus;

public class LoginTakenException extends ControllerException{
    public LoginTakenException(String errorMessage, Integer errorCode) {
        super(errorMessage, errorCode, HttpStatus.CONFLICT);
    }

    public LoginTakenException(Integer errorCode) {
        super("Given login is already in use", errorCode, HttpStatus.CONFLICT);
    }
}
