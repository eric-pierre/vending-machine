package com.ai.messaging.impl;

import com.ai.domain.Money;
import com.ai.messaging.BalancePayload;
import com.ai.messaging.MoneyPayload;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MoneyAddedPayload implements MoneyPayload, BalancePayload {

    private final Money balance;
    private final Money money;




}
