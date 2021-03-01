package com.ai.messaging.impl;

import com.ai.domain.Item;
import com.ai.domain.Location;
import com.ai.domain.Money;
import com.ai.messaging.ItemPayload;
import com.ai.messaging.LocationPayload;
import com.ai.messaging.MoneyPayload;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DispenseItemPayload implements ItemPayload, MoneyPayload, LocationPayload {

    private final Item item;
    private final Money money;
    private final Location location;


}
