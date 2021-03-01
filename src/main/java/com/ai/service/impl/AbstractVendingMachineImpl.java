package com.ai.service.impl;

import com.ai.domain.Item;
import com.ai.domain.Location;
import com.ai.domain.Money;
import com.ai.messaging.Payload;
import com.ai.messaging.impl.MachineBootedPayload;
import com.ai.service.Dispatcher;
import com.ai.service.MessageSender;
import com.ai.service.VendingMachine;

public abstract class AbstractVendingMachineImpl implements VendingMachine, MessageSender {

    private final String machineId;

    @Override
    public Payload getInventory() {
        return send(doGetInventory());

    }

    private Dispatcher dispatch;

    AbstractVendingMachineImpl(Dispatcher dispatch, String machineId) {
        this.dispatch = dispatch;
        this.machineId = machineId;
        send(new MachineBootedPayload());
    }


    @Override
    public Payload addMoney(Money money) {
        return send(doAddMoney(money));
    }

    @Override
    public Payload getBalance() {
        return send(doGetBalance());

    }

    @Override
    public Payload getSlotInfo(Location location) {
        return send(doGetSlotInfo(location));

    }

    @Override
    public Payload dispense(Location location) {
        return send(doDispense(location));

    }

    @Override
    public Payload cancelOrder() {
        return send(doCancelOrder());

    }

    @Override
    public Payload addItem(Item item, Location location, int quantity) {
        return send(doAddItem(item, location, quantity));

    }

    @Override
    public Payload send(Payload payload) {
        dispatch.dispatch(this, payload);
        return payload;

    }

    @Override
    public String getId() {
        return this.machineId;

    }

    protected abstract Payload doGetSlotInfo(Location location);

    protected abstract Payload doAddMoney(Money money);

    protected abstract Payload doGetBalance();

    protected abstract Payload doDispense(Location location);

    protected abstract Payload doCancelOrder();

    protected abstract Payload doAddItem(Item item, Location location, int quantity);

    protected abstract Payload doGetInventory();

}
