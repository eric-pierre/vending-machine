package com.ai.messaging.impl;

import com.ai.domain.Money;
import com.ai.messaging.BalancePayload;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BalanceCheckPayload implements BalancePayload {

    private final Money balance;



}
