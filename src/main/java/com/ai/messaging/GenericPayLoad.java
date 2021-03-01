package com.ai.messaging;


public interface GenericPayLoad<T> extends Payload {

    T getData();

}
