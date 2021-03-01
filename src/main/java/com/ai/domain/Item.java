package com.ai.domain;

import java.io.Serializable;

import com.ai.configuration.misc.ItemDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonDeserialize(using = ItemDeserializer.class)
public class Item implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public static final Item NONE = new Item(null, null, null);
    private String name;
    @JsonProperty("SKU")
    private Integer sku;
    @JsonUnwrapped()
    @Builder.Default
    private Money price = Money.NONE;


    @JsonPOJOBuilder(withPrefix = "")
    public static class ItemBuilder {}




}
