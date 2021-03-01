package com.ai.service;

import com.ai.domain.Item;
import com.ai.domain.Location;
import com.ai.domain.Money;
import com.ai.messaging.Payload;

public interface VendingMachine {

    Payload addMoney(Money money);

    Payload getBalance();

    Payload getSlotInfo(Location location);

    Payload dispense(Location location);

    Payload cancelOrder();

    Payload addItem(Item item, Location location, int quantity);

    Payload getInventory();

}
