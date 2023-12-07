package com.acs560.freelancing.exception;

import javax.security.sasl.AuthenticationException;

/**
 * Exception thrown when authentication credentials are invalid.
 */
public class BadCredentialsException extends AuthenticationException {
    /**
     * Constructor for BadCredentialsException.
     *
     * @param msg the detail message.
     */
    public BadCredentialsException(String msg) {
        super(msg);
    }
}
