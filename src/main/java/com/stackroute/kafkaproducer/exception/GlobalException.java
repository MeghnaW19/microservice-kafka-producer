package com.stackroute.kafkaproducer.exception;

import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalException extends Exception {
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> userAlreadyExistException(final UserAlreadyExistsException e) {
        return new ResponseEntity<>(new Gson().toJson(e.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> userAlreadyExistException(final UserNotFoundException e) {
        return new ResponseEntity<>(new Gson().toJson(e.getMessage()), HttpStatus.CONFLICT);
    }
}
