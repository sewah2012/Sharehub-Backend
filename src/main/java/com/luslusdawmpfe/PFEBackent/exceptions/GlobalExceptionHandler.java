package com.luslusdawmpfe.PFEBackent.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    public static final String DEFAULT_INTERNAL_SERVER_ERROR_MESSAGE = "An error occured while processing request";

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> entityNotFoundExceptionHandler(Exception ex){
        log.error(ex.getMessage(), ex);
        return buildErrorResponse(Objects.nonNull(ex.getMessage()) ? ex.getMessage() : DEFAULT_INTERNAL_SERVER_ERROR_MESSAGE, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityAlreadyExistException.class)
    @ResponseStatus(HttpStatus.ALREADY_REPORTED)
    public ResponseEntity<Object> entityAlreadyExistException(Exception ex){
        log.error(ex.getMessage(), ex);
        return buildErrorResponse(Objects.nonNull(ex.getMessage()) ? ex.getMessage() : DEFAULT_INTERNAL_SERVER_ERROR_MESSAGE, HttpStatus.ALREADY_REPORTED);
    }

    @ExceptionHandler({Throwable.class, Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> internalExceptionHandler(Exception ex){
        log.error(ex.getMessage(), ex);
        return buildErrorResponse(Objects.nonNull(ex.getMessage()) ? ex.getMessage() : DEFAULT_INTERNAL_SERVER_ERROR_MESSAGE,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    private ResponseEntity<Object> buildErrorResponse(String s, HttpStatus internalServerError) {
        return ResponseEntity.status(internalServerError).body(ExceptionResponse.builder()
                .message(s).status(internalServerError).build());
    }


}
