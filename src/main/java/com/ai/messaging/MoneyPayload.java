package com.ai.messaging;

import com.ai.domain.Money;

public interface MoneyPayload extends Payload {

    Money getMoney();

}
