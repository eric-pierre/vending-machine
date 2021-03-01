package com.ai.exception;

import com.ai.domain.Slot;

public class OutOfStockException extends VendingException {


    private static final long serialVersionUID = 1L;
    private final Slot slot;

    public OutOfStockException(String message, Slot slot) {
        super(message);
        this.slot = slot;
    }

    public Slot getSlot() {
        return slot;
    }

}
