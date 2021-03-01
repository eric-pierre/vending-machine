package com.ai.messaging.impl;

import com.ai.domain.Item;
import com.ai.domain.Location;
import com.ai.messaging.ItemPayload;
import com.ai.messaging.LocationPayload;
import com.ai.messaging.QuantityPayload;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ItemAddedPayload implements QuantityPayload, ItemPayload, LocationPayload {

    private final Item item;
    private final int quantity;
    private final Location location;



}
