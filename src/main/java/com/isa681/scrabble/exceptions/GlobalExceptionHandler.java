package com.isa681.scrabble.exceptions;

import com.isa681.scrabble.entity.ErrorDetail;
import com.isa681.scrabble.service.GameServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

    Logger myLogger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

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
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
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
        ErrorDetail errorDetails = new ErrorDetail(new Date(), "Invalid data was entered.",
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseBody
    public ResponseEntity<ErrorDetail> handleMethodArgumentTypeMismatchException(Exception exception,
                                                                             WebRequest webRequest){
        ErrorDetail errorDetails = new ErrorDetail(new Date(), "Invalid data was entered.",
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
    // global exceptions
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<ErrorDetail> handleGlobalException(Exception exception,
                                                              WebRequest webRequest){
        myLogger.error(exception.getMessage());
        ErrorDetail errorDetails = new ErrorDetail(new Date(), "An unknown error occurred. Please contact the game admin.",
                webRequest.getDescription(false));

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
