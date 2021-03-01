package com.ai.messaging;

import com.ai.domain.Money;

public interface BalancePayload extends Payload {

    Money getBalance();

}
