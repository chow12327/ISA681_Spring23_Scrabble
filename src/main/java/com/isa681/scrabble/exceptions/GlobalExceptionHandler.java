package com.isa681.scrabble.exceptions;

import com.isa681.scrabble.entity.ErrorDetail;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(InvalidMoveException.class)
    @ResponseBody
    public ResponseEntity<ErrorDetail> handleInvalidMoveException(InvalidMoveException exception,
                                                                       WebRequest webRequest){
        ErrorDetail errorDetails = new ErrorDetail(new Date(), exception.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidUserDetailsException.class)
    @ResponseBody
    public ResponseEntity<ErrorDetail> handleInvalidUserDetailsException(InvalidUserDetailsException exception,
                                                                  WebRequest webRequest){
        ErrorDetail errorDetails = new ErrorDetail(new Date(), exception.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(GameNotFoundException.class)
    @ResponseBody
    public ResponseEntity<ErrorDetail> handleGameNotFoundException(GameNotFoundException exception,
                                                                         WebRequest webRequest){
        ErrorDetail errorDetails = new ErrorDetail(new Date(), exception.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(UnauthorizedAccessException.class)
    @ResponseBody
    public ResponseEntity<ErrorDetail> handleUnauthorizedAccessException(UnauthorizedAccessException exception,
                                                                   WebRequest webRequest){
        ErrorDetail errorDetails = new ErrorDetail(new Date(), exception.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(ResourceCannotBeCreatedException.class)
    @ResponseBody
    public ResponseEntity<ErrorDetail> handleResourceCannotBeCreatedException(ResourceCannotBeCreatedException exception,
                                                                         WebRequest webRequest){
        ErrorDetail errorDetails = new ErrorDetail(new Date(), exception.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.SERVICE_UNAVAILABLE);
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public ResponseEntity<ErrorDetail> handleHttpMessageNotReadableException(Exception exception,
                                                             WebRequest webRequest){
        ErrorDetail errorDetails = new ErrorDetail(new Date(), "An Invalid character was entered.",
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }


    // global exceptions
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<ErrorDetail> handleGlobalException(Exception exception,
                                                              WebRequest webRequest){
        ErrorDetail errorDetails = new ErrorDetail(new Date(), exception.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
