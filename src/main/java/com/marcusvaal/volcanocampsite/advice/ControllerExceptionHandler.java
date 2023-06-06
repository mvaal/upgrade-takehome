package com.marcusvaal.volcanocampsite.advice;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.format.DateTimeParseException;
import java.util.List;

@RestControllerAdvice
public class ControllerExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> methodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        BindingResult result = ex.getBindingResult();
        List<String> fieldErrors = result.getFieldErrors().stream()
                .map(f -> String.format("%s - %s", f.getField(), f.getDefaultMessage()))
                .toList();
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST, String.join("\n", fieldErrors), request.getServletPath()), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    public ResponseEntity<ErrorResponse> httpMessageNotReadableException(HttpMessageNotReadableException ex, HttpServletRequest request) {
        if (ex.getCause() instanceof InvalidFormatException) {
            if (ex.getCause().getCause() instanceof DateTimeParseException) {
                String errorMessage = String.format("Date format must be MM/dd/yyyy: %s", ex.getCause().getCause().getMessage());
                return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST, errorMessage, request.getServletPath()), HttpStatus.BAD_REQUEST);
            }
        }
        return handleAllExceptions(ex, request);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public final ResponseEntity<ErrorResponse> dataIntegrityViolationException(DataIntegrityViolationException ex, HttpServletRequest request) {
        if (ex.getCause() instanceof ConstraintViolationException) {
            if (ex.getCause().getMessage().contains("RESERVATION")) {
                String errorMessage = "Booking is attempting to be scheduled on an existing reserved date";
                return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST, errorMessage, request.getServletPath()), HttpStatus.BAD_REQUEST);
            }
        }
        return handleAllExceptions(ex, request);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public final ResponseEntity<ErrorResponse> httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request.getServletPath()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex, HttpServletRequest request) {
        logger.error("Unhandled Error Exception: {}", request, ex);
        String errorMessage = "Unknown Exception, please contact support.";
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage, request.getContextPath()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}