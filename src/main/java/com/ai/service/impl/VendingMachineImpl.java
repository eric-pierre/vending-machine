package com.ai.service.impl;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ai.configuration.ApplicationProperties;
import com.ai.domain.Item;
import com.ai.domain.Location;
import com.ai.domain.Money;
import com.ai.domain.Slot;
import com.ai.exception.InvalidCurrenyException;
import com.ai.exception.InvalidSlotException;
import com.ai.exception.NotEnoughMoneyException;
import com.ai.exception.OutOfStockException;
import com.ai.messaging.Payload;
import com.ai.messaging.impl.BalanceCheckPayload;
import com.ai.messaging.impl.CancelPayload;
import com.ai.messaging.impl.DispenseItemPayload;
import com.ai.messaging.impl.FailedToAddItemPayload;
import com.ai.messaging.impl.InvalidCurrencyPayload;
import com.ai.messaging.impl.InvalidSlotPayload;
import com.ai.messaging.impl.InventoryPayload;
import com.ai.messaging.impl.ItemAddedPayload;
import com.ai.messaging.impl.MoneyAddedPayload;
import com.ai.messaging.impl.NotEnoughMoneyPayload;
import com.ai.messaging.impl.OutOfStockPayload;
import com.ai.messaging.impl.SlotInfoPayload;
import com.ai.service.CurrencyValidator;
import com.ai.service.Dispatcher;
import com.ai.service.Inventory;
import com.ai.service.MessageSender;
import com.ai.service.VendingMachine;

@Component
class VendingMachineImpl extends AbstractVendingMachineImpl implements VendingMachine, MessageSender {

    @Autowired
    CurrencyValidator validator;
    @Autowired
    Inventory inventory;

    private Money balance;

    @Autowired
    public VendingMachineImpl(ApplicationProperties config, Dispatcher sender) {
        super(sender, config.getMachineId());
        this.balance = new Money(0d, config.getCurrency());

    }

    @Override
    protected Payload doAddMoney(Money money) {
        if (!validator.isValid(money)) {
            return new InvalidCurrencyPayload(money, new InvalidCurrenyException(money));
        }
        balance = balance.add(money.getValue());
        return new MoneyAddedPayload(balance, money);

    }

    @Override
    protected Payload doDispense(Location location) {
        Item item = null;
        try {
            item = inventory.getItem(location);
        } catch (InvalidSlotException e) {
            return new InvalidSlotPayload(location, e);
        } catch (OutOfStockException e) {
            return new OutOfStockPayload(location);

        }

        Money change = balance.minus(item.getPrice());
        if (change.getValue() < 0) {
            return new NotEnoughMoneyPayload(item, balance, new NotEnoughMoneyException("Not enough Money"));

        }

        balance = new Money(0, balance.getCurrency());
        return new DispenseItemPayload(item, change, location);

    }

    @Override
    protected Payload doGetBalance() {
        return new BalanceCheckPayload(this.balance);
    }

    @Override
    protected Payload doGetSlotInfo(Location location) {
        try {
            Slot slot = inventory.getSlot(location);
            return new SlotInfoPayload(location, slot);
        } catch (InvalidSlotException e) {
            return new InvalidSlotPayload(location, e);
        }
    }

    @Override
    protected Payload doCancelOrder() {
        CancelPayload payload = new CancelPayload(this.balance);
        this.balance = new Money(0d, validator.getCurrency());
        return payload;
    }

    @Override
    protected Payload doAddItem(Item item, Location location, int quantity) {
        try {
            this.inventory.fillSlot(item, location, quantity);
            return new ItemAddedPayload(item, quantity, location);
        } catch (InvalidSlotException e) {
            return FailedToAddItemPayload
                    .builder()
                    .exception(e)
                    .location(location)
                    .quantity(quantity)
                    .item(item)
                    .build();

        }
    }

    @Override
    protected Payload doGetInventory() {
        Collection<Slot> info = inventory.getStock();
        return new InventoryPayload(info);
    }

}