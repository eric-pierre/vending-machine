package com.ai.service;

import java.util.Collection;
import java.util.Currency;

import com.ai.domain.Money;

public class CurrencyValidator {

    private final Collection<Double> values;

    private final Currency currency;

    public CurrencyValidator(Collection<Double> collection, Currency currency) {
        this.values = collection;
        this.currency = currency;
    }

    public boolean isValid(Money money) {
        return values.contains(money.getValue()) && money.getCurrency().equals(currency);
    }

    public Currency getCurrency() {
        return this.currency;

    }



}
