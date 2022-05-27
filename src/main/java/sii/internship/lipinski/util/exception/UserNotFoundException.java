package sii.internship.lipinski.util.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends ControllerException{
    public UserNotFoundException(String errorMessage, Integer errorCode) {
        super(errorMessage, errorCode, HttpStatus.NOT_FOUND);
    }
    public UserNotFoundException(Integer errorCode) {
        super("User could not be found", errorCode, HttpStatus.NOT_FOUND);
    }
}
