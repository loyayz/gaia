package com.loyayz.gaia.exception;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public class OperationDeniedException extends RuntimeException {

    public OperationDeniedException() {
    }

    public OperationDeniedException(String message) {
        super(message);
    }

    public OperationDeniedException(String message, Throwable cause) {
        super(message, cause);
    }

}
