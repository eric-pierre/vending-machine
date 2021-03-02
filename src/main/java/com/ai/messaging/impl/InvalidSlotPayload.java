package com.ai.messaging.impl;

import com.ai.domain.Location;
import com.ai.messaging.ExceptionPayload;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class InvalidSlotPayload implements ExceptionPayload {

    private final Location location;
    private final Exception exception;




}
