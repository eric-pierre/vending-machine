package com.ai.exception;


public class BadPayloadException extends RuntimeException {

    private static final long serialVersionUID = 4587007926533942157L;

    public BadPayloadException(String message) {
        super(message);
    }

}
