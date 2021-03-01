package com.ai.exception;


public class InvalidLocationException extends RuntimeException {

    private static final long serialVersionUID = 8755211538162322562L;

    public InvalidLocationException(String message) {
        super(message);
    }

}
