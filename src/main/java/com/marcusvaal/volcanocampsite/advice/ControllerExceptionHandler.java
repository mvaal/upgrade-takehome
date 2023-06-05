package com.marcusvaal.volcanocampsite.advice;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.format.DateTimeParseException;
import java.util.List;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> methodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
        BindingResult result = ex.getBindingResult();
        List<String> fieldErrors = result.getFieldErrors().stream()
                .map(f -> String.format("%s - %s", f.getField(), f.getDefaultMessage()))
                .toList();
        return new ResponseEntity<>(new ErrorResponse(String.join("\n", fieldErrors)), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    public ResponseEntity<ErrorResponse> httpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest request) {
        if (ex.getCause() instanceof InvalidFormatException) {
            if (ex.getCause().getCause() instanceof DateTimeParseException) {
                String errorMessage = String.format("Date format must be MM/dd/yyyy: %s", ex.getCause().getCause().getMessage());
                return new ResponseEntity<>(new ErrorResponse(errorMessage), HttpStatus.BAD_REQUEST);
            }
        }
        String errorMessage = "Unknown Exception, please contact support.";
        return new ResponseEntity<>(new ErrorResponse(errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public final ResponseEntity<ErrorResponse> dataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
        if (ex.getCause() instanceof ConstraintViolationException) {
            if(ex.getCause().getMessage().contains("RESERVATION")) {
                String errorMessage = String.format("Booking is attempting to be scheduled on an existing reserved date.", ex.getCause().getCause().getMessage());
                return new ResponseEntity<>(new ErrorResponse(errorMessage), HttpStatus.BAD_REQUEST);
            }
        }
        String errorMessage = "Unknown Exception, please contact support.";
        return new ResponseEntity<>(new ErrorResponse(errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public final ResponseEntity<ErrorResponse> httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex, WebRequest request) {
        String errorMessage = "Unknown Exception, please contact support.";
        return new ResponseEntity<>(new ErrorResponse(errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}