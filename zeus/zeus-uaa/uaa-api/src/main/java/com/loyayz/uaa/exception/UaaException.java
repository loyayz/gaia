package com.loyayz.uaa.exception;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public class UaaException extends RuntimeException {

    public UaaException() {
    }

    public UaaException(String message) {
        super(message);
    }

    public UaaException(String message, Throwable cause) {
        super(message, cause);
    }

}
