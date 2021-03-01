package com.ai.domain;

import java.util.Currency;


public class MoneyUS extends Money {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private MoneyUS(double value) {
        super(value, Currency.getInstance("USD"));
    }



    public static final Money NICKEL = new MoneyUS(0.05);
    public static final Money DIME = new MoneyUS(0.1);
    public static final Money QUARTER = new MoneyUS(0.25);
    public static final Money DOLLAR = new MoneyUS(1);
    public static final Money FIVER = new MoneyUS(5);

}
