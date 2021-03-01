package com.ai.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ai.domain.Item;
import com.ai.domain.Location;
import com.ai.domain.MachineLayout;
import com.ai.domain.MoneyUS;
import com.ai.exception.InvalidSlotException;
import com.ai.exception.OutOfStockException;
import com.ai.exception.SlotOverflowException;
import com.ai.service.Inventory;

class InventoryTest {

    Inventory inventory;
    Item almondJoy;
    Item toblerone;

    Location a1 = Location.fromString("A1");
    Location a2 = Location.fromString("A2");
    Location b2 = Location.fromString("B2");

    @BeforeEach
    void setup() {
        MachineLayout layout = MachineLayout.builder()
                .columns(5)
                .rows(2)
                .itemCount(15)
                .build();

        almondJoy = Item.builder()
                .name("Almond Joy")
                .price(MoneyUS.DIME)
                .sku(1234).build();

        toblerone = Item.builder()
                .name("Toblerone")
                .price(MoneyUS.FIVER)
                .sku(1234).build();

        a1 = Location.fromString("A1");
        b2 = Location.fromString("B2");
        a2 = Location.fromString("A2");

        inventory = new Inventory(layout);
    }



    @Test
    void testGetSlot() {
        try {
            inventory.fillSlot(almondJoy, a1, 1);
        } catch (InvalidSlotException e) {
            fail("Adding item failed ", e);
        }
        Item item = null;
        try {
            item = inventory.getItem(a1);
        } catch (Exception e) {
            fail("retreiving item", e);
        }

        assertEquals("Almond Joy", item.getName());

        Exception exception = assertThrows(OutOfStockException.class, () -> inventory.getItem(a1));
        assertEquals("Nothing at A1", exception.getMessage());

    }

    @Test
    void testFillSlot() {

        try {
            inventory.fillSlot(almondJoy, a1, 15);
            inventory.fillSlot(almondJoy, a2, 5);
            inventory.fillSlot(almondJoy, a2, 5);
            inventory.fillSlot(almondJoy, a2, 5);

            inventory.fillSlot(almondJoy, b2, 5);

        } catch (InvalidSlotException e) {
            fail("Adding item failed " + e.getMessage(), e);
        }
        Exception exception;
        exception = assertThrows(SlotOverflowException.class, () -> inventory.fillSlot(almondJoy, a1, 15));
        assertEquals("Capacity at location A1 exceed", exception.getMessage());

        exception = assertThrows(SlotOverflowException.class, () -> inventory.fillSlot(almondJoy, a2, 1));
        assertEquals("Capacity at location A2 exceed", exception.getMessage());

        exception = assertThrows(InvalidSlotException.class, () -> inventory.fillSlot(toblerone, b2, 1));
        assertEquals("Slot at location B2 already contains 'Almond Joy', and can't be loaded with 'Toblerone'", exception.getMessage());

    }

}
