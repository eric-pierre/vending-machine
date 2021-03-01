package com.ai.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import com.ai.domain.Item;
import com.ai.domain.Location;
import com.ai.domain.MachineLayout;
import com.ai.domain.Slot;
import com.ai.exception.InvalidSlotException;
import com.ai.exception.OutOfStockException;
import com.ai.exception.SlotOverflowException;

public class Inventory {

    private final Slot[][] stock;

    private final MachineLayout layout;

    public Inventory(MachineLayout layout) {
        if (layout == null) {
            throw new IllegalArgumentException("Layout undefined!: ");
        }
        stock = new Slot[layout.columns()][layout.rows()];

        this.layout = layout;
        for (int i = 0; i < layout.columns(); i++) {
            for (int j = 0; j < layout.rows(); j++) {
                Location location = Location.builder().column(i).row(j).build();
                stock[i][j] = Slot.emptySlot(location);
            }
        }

    }

    private InvalidSlotException genericSlotException(Location location) {
        return new InvalidSlotException("Invalid location: " + location);
    }



    public Item getItem(Location location) throws InvalidSlotException, OutOfStockException {
        Slot slot = getSlot(location);
        if (slot == null) {
            throw genericSlotException(location);
        }

        if (slot.getQuantity() == 0) {
            throw new OutOfStockException("Nothing at " + location, slot);
        }
        slot.updateQuantity();
        return slot.getItem();
    }

    public Slot getSlot(Location location) throws InvalidSlotException {

        if (location.getRow() >= layout.rows()
                || location.getColumn() >= layout.columns()) {
            throw genericSlotException(location);
        }

        return stock[location.getColumn()][location.getRow()];

    }

    private void setSlot(Location location, Slot slot) {
        stock[location.getColumn()][location.getRow()] = slot;
    }

    public void fillSlot(Item item, Location location, int quantity) throws InvalidSlotException {
        if (quantity > layout.itemCount()) {
            String message = String.format("Invalid quantity %d (Max %d)",
                    quantity,
                    layout.itemCount());
            throw new SlotOverflowException(message);
        }

        Slot slot = getSlot(location);
        if (slot == null) {
            throw new InvalidSlotException("Invalid location " + location);
        }

        if (slot.getQuantity() == 0) {
            Slot newSlot = Slot.builder()
                    .item(item)
                    .location(location)
                    .quantity(quantity).build();
            setSlot(location, newSlot);
            return;
        }

        if (!slot.getItem().equals(item)) {
            String message = String
                    .format("Slot at location %s already contains '%s', and can't be loaded with '%s'",
                            location, slot.getItem().getName(), item.getName());
            throw new InvalidSlotException(message);
        }

        int qty = slot.getQuantity() + quantity;

        if (qty > layout.itemCount()) {
            String message = String
                    .format("Capacity at location %s exceed", location);
            throw new SlotOverflowException(message);
        }
        slot.setQuantity(qty);

    }

    public Collection<Slot> getStock() {

        return Arrays.stream(stock)
                .flatMap(Arrays::stream)
                .collect(Collectors.toSet());

    }

}
