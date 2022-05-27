package sii.internship.lipinski.util.exception;

import org.springframework.http.HttpStatus;

public class LectureRegistrationNotFoundException extends ControllerException{
    public LectureRegistrationNotFoundException(String errorMessage, Integer errorCode) {
        super(errorMessage, errorCode, HttpStatus.NOT_FOUND);
    }
    public LectureRegistrationNotFoundException(Integer errorCode) {
        super("Registration for lecture could not be found", errorCode, HttpStatus.NOT_FOUND);
    }
}
