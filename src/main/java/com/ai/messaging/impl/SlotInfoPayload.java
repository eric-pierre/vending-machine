package com.ai.messaging.impl;

import com.ai.domain.Location;
import com.ai.domain.Slot;
import com.ai.messaging.GenericPayLoad;
import com.ai.messaging.LocationPayload;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SlotInfoPayload implements LocationPayload, GenericPayLoad<Slot> {

    private final Location location;
    private final Slot data;



}
