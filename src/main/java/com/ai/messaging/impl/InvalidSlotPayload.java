package com.ai.messaging.impl;

import com.ai.domain.Location;
import com.ai.messaging.ExceptionPayload;
import com.ai.messaging.Payload;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class InvalidSlotPayload implements Payload, ExceptionPayload {

    private final Location location;
    private final Exception exception;




}
