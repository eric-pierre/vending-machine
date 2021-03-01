package com.ai.domain;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@JsonDeserialize(builder = Slot.SlotBuilder.class)
public class Slot implements Serializable {

    private static final long serialVersionUID = -3919420131782066046L;

    private Location location;

    @Setter
    private Integer quantity;

    private Item item;

    public static Slot emptySlot(Location location) {
        return new Slot(location, 0, Item.NONE);
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class SlotBuilder {}

    public void updateQuantity() {
        quantity--;

    }

    public int getQuantity() {
        if (this.quantity == null) {
            return 0;
        }
        return quantity.intValue();
    }

}
