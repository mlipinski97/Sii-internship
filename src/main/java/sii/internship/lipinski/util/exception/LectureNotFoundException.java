package sii.internship.lipinski.util.exception;

import org.springframework.http.HttpStatus;

public class LectureNotFoundException extends ControllerException{
    public LectureNotFoundException(String errorMessage, Integer errorCode) {
        super(errorMessage, errorCode, HttpStatus.NOT_FOUND);
    }
    public LectureNotFoundException(Integer errorCode) {
        super("Lecture could not be found", errorCode, HttpStatus.NOT_FOUND);
    }
}
