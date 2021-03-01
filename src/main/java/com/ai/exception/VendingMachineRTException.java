package com.ai.exception;


public class VendingMachineRTException extends RuntimeException {

    private static final long serialVersionUID = -6568328148076241778L;

    public VendingMachineRTException(Exception e) {
        super(e);
    }

}
