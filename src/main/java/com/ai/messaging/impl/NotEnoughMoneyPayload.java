package com.ai.messaging.impl;

import com.ai.domain.Item;
import com.ai.domain.Money;
import com.ai.messaging.BalancePayload;
import com.ai.messaging.ExceptionPayload;
import com.ai.messaging.ItemPayload;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NotEnoughMoneyPayload implements ItemPayload, BalancePayload, ExceptionPayload {

    private final Item item;
    private final Money balance;
    private final Exception exception;



}
