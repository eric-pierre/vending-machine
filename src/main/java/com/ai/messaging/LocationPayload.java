package com.ai.messaging;

import com.ai.domain.Location;

public interface LocationPayload extends Payload {

    Location getLocation();
}
