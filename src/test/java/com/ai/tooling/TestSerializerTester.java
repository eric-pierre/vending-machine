package com.ai.tooling;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Currency;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ai.configuration.misc.MoneyDeserializer;
import com.ai.domain.Money;
import com.ai.domain.MoneyUS;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
class TestSerializerTester {

    @Autowired
    private ObjectMapper mapper;

    @Test
    void serializeMoney() {
        try {
            String json = String.format("{\"value\":%s}", "\"5.0\"");
            InputStream stream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));

            JsonParser parser = mapper.getFactory().createParser(stream);
            DeserializationContext ctxt = mapper.getDeserializationContext();
            parser.nextToken();
            parser.nextToken();
            parser.nextToken();

            MoneyDeserializer deserializer = new MoneyDeserializer(Currency.getInstance("USD"));
            Money money = deserializer.deserialize(parser, ctxt);

            assertEquals(MoneyUS.FIVER, money);
        } catch (IOException e) {
            fail("Exception while testing deserializtion:" + e.getMessage(), e);
        }
    }
}
