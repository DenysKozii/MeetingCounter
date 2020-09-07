package org.example.meetingcounter.exception;

import org.example.meetingcounter.dto.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseStatus handlerClientError(Exception e){
        e.printStackTrace();
        return new ResponseStatus(HttpStatus.NOT_FOUND.value(), e.getMessage());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseStatus handlerPageNotFound(Exception e){
        e.printStackTrace();
        return new ResponseStatus(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseStatus handlerServerError(Exception e){
        e.printStackTrace();
        return new ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
