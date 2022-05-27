package sii.internship.lipinski.util.exception.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import sii.internship.lipinski.util.exception.*;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({
            LoginTakenException.class,
            LectureNotFoundException.class,
            NoFreeSeatsAvailableException.class,
            LectureSchedulesCollideException.class,
            UserNotFoundException.class,
            LectureRegistrationNotFoundException.class
    })
    public ResponseEntity<ApiErrorResponse> handleException(ControllerException ce) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
                ce.getErrorCode(),
                ce.getLocalizedMessage(),
                LocalDateTime.now(),
                ce.getErrorStatus()
        );
        return new ResponseEntity<>(apiErrorResponse, apiErrorResponse.getErrorStatus());
    }
}
