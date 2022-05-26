package sii.internship.lipinski.util.exception;

import org.springframework.http.HttpStatus;

public class NoFreeSeatsAvailableException extends ControllerException{
    public NoFreeSeatsAvailableException(String errorMessage, Integer errorCode) {
        super(errorMessage, errorCode, HttpStatus.CONFLICT);
    }

    public NoFreeSeatsAvailableException(Integer errorCode) {
        super("There are no more free seats available", errorCode, HttpStatus.CONFLICT);
    }
}
