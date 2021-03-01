package com.ai.configuration.misc;

import java.io.IOException;
import java.util.Currency;

import com.ai.domain.Item;
import com.ai.domain.Money;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class ItemDeserializer extends StdDeserializer<Item> {

    private static final long serialVersionUID = 994811634016843041L;

    protected ItemDeserializer(Class<Item> vc) {
        super(vc);
    }

    ItemDeserializer() {
        this(Item.class);
    }

    @Override
    public Item deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        String name = node.get("name").asText();
        double value = node.get("value").asDouble();
        int sku = node.get("SKU").asInt();
        String curr = node.get("currency").asText();

        Money money = new Money(value, Currency.getInstance(curr));

        return Item.builder().name(name).price(money).sku(sku).build();



    }

}
