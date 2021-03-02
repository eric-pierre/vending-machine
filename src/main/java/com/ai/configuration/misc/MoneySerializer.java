package com.ai.configuration.misc;

import java.io.IOException;

import com.ai.domain.Money;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;


public class MoneySerializer extends JsonSerializer<Money> {

    @Override
    public void serialize(Money value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(value.toString());

    }


}
