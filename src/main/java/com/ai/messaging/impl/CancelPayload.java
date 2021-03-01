package com.ai.messaging.impl;

import com.ai.domain.Money;
import com.ai.messaging.MoneyPayload;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CancelPayload implements MoneyPayload {

    private final Money money;



}
