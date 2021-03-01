package com.ai.exception;

import com.ai.domain.Money;

public class InvalidCurrenyException extends Exception {

    private static final long serialVersionUID = 5247542494532415293L;

    public InvalidCurrenyException(Money money) {
        super(money + " is not a valid currency");

    }

}
