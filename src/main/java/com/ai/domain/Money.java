package com.ai.domain;

import java.io.Serializable;
import java.util.Currency;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
public class Money implements Comparable<Money>, Serializable {

    private static final long serialVersionUID = -6854248908807088996L;
    public static final Money NONE = new Money(Double.MIN_NORMAL, null);
    @Getter
    private final Double value;
    @Getter
    private final Currency currency;

    public Money(double value, Currency currency) {
        this.value = Math.round(value * 100.0) / 100.0;
        this.currency = currency;
    }

    public Money add(double value) {
        return new Money(this.value + value, this.currency);

    }

    public Money minus(Money money) {
        validate(money);
        return new Money(value - money.value, currency);
    }

    public Money abs() {
        return new Money(Math.abs(this.value), this.currency);
    }

    @Override
    public int compareTo(Money o) {
        validate(o);
        return this.value.compareTo(o.value);
    }

    @Override
    public String toString() {
        return String.format("%s%.2f", this.currency.getSymbol(), this.value);
    }

    private void validate(Money m) {
        if (!m.getCurrency().equals(this.getCurrency())) {
            String message = String.format("Invalid currency (%s) for use with this Money (%s)",
                    m.getCurrency().getCurrencyCode(),
                    this.getCurrency().getCurrencyCode());
            throw new IllegalArgumentException(message);
        }
    }


}
