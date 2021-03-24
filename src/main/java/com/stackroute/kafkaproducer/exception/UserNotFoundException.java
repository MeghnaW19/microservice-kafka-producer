package com.stackroute.kafkaproducer.exception;


/**
 * Custom exception to handle user not found condition
 */
public class UserNotFoundException extends Exception {

    public UserNotFoundException(String message) {
        super(message);
    }
}
