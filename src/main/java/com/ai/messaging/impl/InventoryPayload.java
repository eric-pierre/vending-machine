package com.ai.messaging.impl;

import java.util.Collection;

import com.ai.domain.Slot;
import com.ai.messaging.GenericPayLoad;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class InventoryPayload implements GenericPayLoad<Collection<Slot>> {

    private final Collection<Slot> data;


}
