package com.ai.messaging;

import com.ai.domain.Item;

public interface ItemPayload extends Payload {

    Item getItem();
}
