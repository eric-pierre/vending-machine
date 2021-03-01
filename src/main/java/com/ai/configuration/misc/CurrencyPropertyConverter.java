package com.ai.configuration.misc;

import java.util.Currency;

import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

// doesn't work... see
// https://github.com/spring-projects/spring-boot/issues/13285

@Component
@ConfigurationPropertiesBinding
public class CurrencyPropertyConverter implements Converter<String, Currency> {

    @Override
    public Currency convert(String source) {
        return Currency.getInstance(source);

    }




}
