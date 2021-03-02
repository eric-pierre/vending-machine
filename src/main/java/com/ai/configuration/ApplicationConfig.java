package com.ai.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ai.configuration.misc.MoneyDeserializer;
import com.ai.configuration.misc.MoneySerializer;
import com.ai.configuration.misc.PayloadSerializer;
import com.ai.domain.Money;
import com.ai.messaging.Payload;
import com.ai.service.CurrencyValidator;
import com.ai.service.Dispatcher;
import com.ai.service.Inventory;
import com.ai.service.VendingMachine;
import com.ai.service.impl.LoggingDispatcher;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

@Configuration
public class ApplicationConfig {

    @Bean
    CurrencyValidator validator(ApplicationProperties props) {
        return new CurrencyValidator(props.getDenominations(), props.getCurrency());
    }

    @Bean
    public ObjectMapper mapper(ApplicationProperties props) {
        ObjectMapper mapper = new ObjectMapper();

        SimpleModule module = new SimpleModule();
        module.addDeserializer(Money.class, new MoneyDeserializer(props.getCurrency()));
        module.addSerializer(Money.class, new MoneySerializer());

        module.addSerializer(Payload.class, new PayloadSerializer());

        mapper.setSerializationInclusion(Include.NON_EMPTY);

        mapper.registerModule(module);
        return mapper;
    }


    @Bean
    public Inventory inventory(ApplicationProperties props) {
        return new Inventory(props.getMachineLayout());
    }

    @Bean
    public Dispatcher dispatcher() {

        Logger thelog = LoggerFactory.getLogger(VendingMachine.class);
        return new LoggingDispatcher(thelog);
    }



}
