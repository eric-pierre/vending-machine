package com.ai.messaging.impl;

import com.ai.domain.Money;
import com.ai.messaging.ExceptionPayload;
import com.ai.messaging.MoneyPayload;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class InvalidCurrencyPayload implements MoneyPayload, ExceptionPayload {

    private final Money money;
    private final Exception exception;



}
