package com.ai.vending;

import java.util.Currency;

import com.ai.domain.Money;

public class MoneyEuro extends Money {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private MoneyEuro(double value) {
        super(value, Currency.getInstance("EUR"));
    }

    public static final Money C10 = new MoneyEuro(0.1);


}
