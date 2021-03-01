package com.ai;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.ai.domain.Money;
import com.ai.domain.MoneyUS;

class TestDomainObjects {



    @Test
    void testMoney() {

        Money buck = MoneyUS.DOLLAR;

        Money five = MoneyUS.FIVER;

        Money single = five.minus(buck).minus(buck).minus(buck).minus(buck);

        assertEquals(single, buck);
        assertEquals(MoneyUS.FIVER.hashCode(), five.hashCode());

        assertTrue(five.compareTo(buck) > 0);

        Money money = new Money(-1, buck.getCurrency());

        money = money.abs();

        assertEquals(buck, money);


    }

}
