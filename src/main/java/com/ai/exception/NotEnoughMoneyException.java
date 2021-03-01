package com.ai.exception;


public class NotEnoughMoneyException extends VendingException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public NotEnoughMoneyException(String message) {
        super(message);
    }

}
