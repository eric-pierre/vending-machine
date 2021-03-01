package com.ai.service;

import com.ai.messaging.Payload;

public interface Dispatcher {

    void dispatch(MessageSender sender, Payload payload);


}
