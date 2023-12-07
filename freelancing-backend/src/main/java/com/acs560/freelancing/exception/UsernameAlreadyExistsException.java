package com.acs560.freelancing.exception;

/**
 * Exception thrown when an attempt is made to register a username that already exists.
 */
public class UsernameAlreadyExistsException extends RuntimeException {
    /**
     * Constructor for UsernameAlreadyExistsException.
     *
     * @param message the detail message.
     */
    public UsernameAlreadyExistsException(String message) {
        super(message);
    }
}
