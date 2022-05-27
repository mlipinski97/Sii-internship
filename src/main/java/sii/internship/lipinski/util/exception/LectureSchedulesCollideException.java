package sii.internship.lipinski.util.exception;

import org.springframework.http.HttpStatus;

public class LectureSchedulesCollideException extends ControllerException{
    public LectureSchedulesCollideException(String errorMessage, Integer errorCode) {
        super(errorMessage, errorCode, HttpStatus.CONFLICT);
    }
    public LectureSchedulesCollideException(Integer errorCode) {
        super("Chosen lecture hours collide with another lecture", errorCode, HttpStatus.CONFLICT);
    }
}
