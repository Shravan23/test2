package com.acs560.freelancing.exception;

/**
 * Exception thrown when an attempt is made to register an email that already exists.
 */
public class EmailAlreadyExistsException extends RuntimeException {
    /**
     * Constructor for EmailAlreadyExistsException.
     *
     * @param message the detail message.
     */
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}

