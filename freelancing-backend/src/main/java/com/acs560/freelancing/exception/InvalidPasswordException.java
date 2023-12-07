package com.acs560.freelancing.exception;

/**
 * Exception thrown when a password does not meet specified criteria or is incorrect.
 */
public class InvalidPasswordException extends RuntimeException {
    /**
     * Constructor for InvalidPasswordException.
     *
     * @param message the detail message.
     */
    public InvalidPasswordException(String message) {
        super(message);
    }
}

