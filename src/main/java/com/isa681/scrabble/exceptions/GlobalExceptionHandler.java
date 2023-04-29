package com.isa681.scrabble.exceptions;

import com.isa681.scrabble.entity.ErrorDetail;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(InvaildMoveException.class)
    public ResponseEntity<ErrorDetail> handleInvalidMoveException(InvaildMoveException exception,
                                                                       WebRequest webRequest){
        ErrorDetail errorDetails = new ErrorDetail(new Date(), exception.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidUserDetailsException.class)
    public ResponseEntity<ErrorDetail> handleInvalidUserDetailsException(InvaildMoveException exception,
                                                                  WebRequest webRequest){
        ErrorDetail errorDetails = new ErrorDetail(new Date(), exception.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

    // global exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetail> handleGlobalException(Exception exception,
                                                              WebRequest webRequest){
        ErrorDetail errorDetails = new ErrorDetail(new Date(), exception.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
