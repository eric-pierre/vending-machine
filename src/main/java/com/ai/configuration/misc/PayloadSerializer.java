package com.ai.configuration.misc;

import java.io.IOException;

import com.ai.messaging.BalancePayload;
import com.ai.messaging.ExceptionPayload;
import com.ai.messaging.GenericPayLoad;
import com.ai.messaging.ItemPayload;
import com.ai.messaging.LocationPayload;
import com.ai.messaging.MoneyPayload;
import com.ai.messaging.Payload;
import com.ai.messaging.QuantityPayload;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/*
 * As a Poor's man DTO mapper, I use a custom serializer to make the domain
 * objects
 * somewhat presentable
 */

public class PayloadSerializer extends JsonSerializer<Payload> {

    @Override
    public void serialize(Payload value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

        gen.writeStartObject();
        if (value instanceof BalancePayload) {
            BalancePayload bp = (BalancePayload) value;
            gen.writeStringField("balance", bp.getBalance().toString());
        }

        if (value instanceof ExceptionPayload) {
            ExceptionPayload ep = (ExceptionPayload) value;
            gen.writeStringField("error", ep.getException().getMessage());
        }

        if (value instanceof GenericPayLoad<?>) {
            GenericPayLoad<?> gp = (GenericPayLoad<?>) value;
            gen.writeObjectField("data", gp.getData());
        }

        if (value instanceof ItemPayload) {
            ItemPayload ip = (ItemPayload) value;
            gen.writeObjectField("item", ip.getItem());
        }

        if (value instanceof LocationPayload) {
            LocationPayload lb = (LocationPayload) value;
            gen.writeStringField("location", lb.getLocation().toString());
        }

        if (value instanceof MoneyPayload) {
            MoneyPayload mp = (MoneyPayload) value;
            gen.writeObjectField("money", mp.getMoney());
        }

        if ( value instanceof QuantityPayload) {
            QuantityPayload qp = (QuantityPayload) value ;
            gen.writeNumberField("quantity", qp.getQuantity());
        }

        gen.writeEndObject();

    }


}
