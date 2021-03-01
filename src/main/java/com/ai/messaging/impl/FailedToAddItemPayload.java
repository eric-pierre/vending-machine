package com.ai.messaging.impl;

import com.ai.domain.Item;
import com.ai.domain.Location;
import com.ai.messaging.ExceptionPayload;
import com.ai.messaging.ItemPayload;
import com.ai.messaging.LocationPayload;
import com.ai.messaging.QuantityPayload;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FailedToAddItemPayload implements LocationPayload, ItemPayload, ExceptionPayload, QuantityPayload {

    private final Location location ;
    private final Item item ;
    private final int quantity;
    private final Exception exception;








}
