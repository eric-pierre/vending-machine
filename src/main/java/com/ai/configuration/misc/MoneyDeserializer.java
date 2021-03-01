package com.ai.configuration.misc;

import java.io.IOException;
import java.util.Currency;

import com.ai.domain.Money;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class MoneyDeserializer extends JsonDeserializer<Money> {

    private final Currency currency;

    public MoneyDeserializer(Currency currency) {
        this.currency = currency;
    }

    @Override
    public Money deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        Double value = node.asDouble();

        return new Money(value, currency);

    }

}
