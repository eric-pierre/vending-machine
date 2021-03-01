package com.ai.service;

import com.ai.messaging.Payload;

public interface MessageSender {

    Payload send(Payload payload);

    String getId();
}
