package com.ai.service.impl;

import com.ai.messaging.Payload;
import com.ai.service.Dispatcher;
import com.ai.service.MessageSender;

import lombok.Getter;
import lombok.Setter;

public abstract class AbstractDispatcherImpl implements Dispatcher {

    @Getter
    @Setter
    private Dispatcher nextDispatcher;

    @Override
    public void dispatch(MessageSender sender, Payload payload) {
        doDispatch(sender, payload);

        if (nextDispatcher != null) {
            nextDispatcher.dispatch(sender, payload);
        }

    }

    protected abstract void doDispatch(MessageSender sender, Payload payload);

}
