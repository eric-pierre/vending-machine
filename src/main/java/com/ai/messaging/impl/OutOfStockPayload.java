package com.ai.messaging.impl;

import com.ai.domain.Location;
import com.ai.messaging.LocationPayload;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OutOfStockPayload implements LocationPayload {

    private final Location location;



}
