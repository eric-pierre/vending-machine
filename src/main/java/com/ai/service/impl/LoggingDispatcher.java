package com.ai.service.impl;

import java.time.Instant;

import org.slf4j.Logger;

import com.ai.domain.Slot;
import com.ai.exception.BadPayloadException;
import com.ai.messaging.Payload;
import com.ai.messaging.impl.BalanceCheckPayload;
import com.ai.messaging.impl.CancelPayload;
import com.ai.messaging.impl.DispenseItemPayload;
import com.ai.messaging.impl.FailedToAddItemPayload;
import com.ai.messaging.impl.InvalidCurrencyPayload;
import com.ai.messaging.impl.InvalidSlotPayload;
import com.ai.messaging.impl.InventoryPayload;
import com.ai.messaging.impl.ItemAddedPayload;
import com.ai.messaging.impl.MachineBootedPayload;
import com.ai.messaging.impl.MoneyAddedPayload;
import com.ai.messaging.impl.NotEnoughMoneyPayload;
import com.ai.messaging.impl.OutOfStockPayload;
import com.ai.messaging.impl.SlotInfoPayload;
import com.ai.service.Dispatcher;
import com.ai.service.MessageSender;



public class LoggingDispatcher extends AbstractDispatcherImpl implements Dispatcher {

    private final Logger logger;

    public LoggingDispatcher(Logger logger) {
        this.logger = logger;
    }

    @Override
    protected void doDispatch(MessageSender sender, Payload payload) {

        if (payload instanceof BalanceCheckPayload) {
            doBalanceCheck((BalanceCheckPayload) payload);
            return;
        }

        if (payload instanceof NotEnoughMoneyPayload) {
            doNotEnoughMoney((NotEnoughMoneyPayload) payload);
            return;
        }

        if (payload instanceof DispenseItemPayload) {
            doDispenseItem((DispenseItemPayload) payload);
            return;

        }
        if (payload instanceof FailedToAddItemPayload) {
            doFailedToAddItem((FailedToAddItemPayload) payload);
            return;

        }

        if (payload instanceof InvalidCurrencyPayload) {
            doInvalidCurrency((InvalidCurrencyPayload) payload);
            return;

        }

        if (payload instanceof InvalidSlotPayload) {
            doInvalidSlot((InvalidSlotPayload) payload);
            return;

        }

        if (payload instanceof InventoryPayload) {
            doInventory((InventoryPayload) payload);
            return;

        }

        if (payload instanceof ItemAddedPayload) {
            doItemAdded((ItemAddedPayload) payload);
            return;

        }

        if (payload instanceof MoneyAddedPayload) {
            doMoneyAdded((MoneyAddedPayload) payload);
            return;

        }

        if (payload instanceof CancelPayload) {
            doOrderCancelled((CancelPayload) payload);
            return;

        }

        if (payload instanceof OutOfStockPayload) {
            doOutOfStock((OutOfStockPayload) payload);
            return;

        }

        if (payload instanceof SlotInfoPayload) {
            doSlotInformation((SlotInfoPayload) payload);
            return;

        }

        if (payload instanceof MachineBootedPayload) {
            String line = String.format("* AI VendingMachine 3000 Id %s Started succesfully  at %s *",

                    sender.getId(),
                    Instant.now());
            String line2 = "*".repeat(line.length());
            logger.info(line2) ;
            logger.info(line);
            logger.info(line2);
            return;

        }

        throw new BadPayloadException("Bad payload type: " + payload.getClass().getName());

    }

    private void doSlotInformation(SlotInfoPayload payload) {
        Slot data = payload.getData();
        logger.info("Slot: {} - {}, {}, {} ,{}",
                payload.getLocation(),
                data.getItem().getName(),
                data.getItem().getPrice(),
                data.getQuantity(),
                data.getItem().getSku());

    }

    private void doOutOfStock(OutOfStockPayload payload) {
        logger.info("Out of Stock at location {}!", payload.getLocation());

    }

    private void doOrderCancelled(CancelPayload payload) {
        logger.info("Order cancelled. Money returned: {}", payload.getMoney());

    }

    private void doNotEnoughMoney(NotEnoughMoneyPayload payload) {

        logger.info("Not enough money! Item cost {}, balance is {}, need an extra {}  ",
                payload.getItem().getPrice(),
                payload.getBalance(),
                payload.getItem().getPrice().minus(payload.getBalance()));

    }

    private void doMoneyAdded(MoneyAddedPayload payload) {

        logger.info("Added {} for a total of {} ", payload.getMoney(), payload.getBalance());

    }

    private void doItemAdded(ItemAddedPayload payload) {

        logger.info("{} quantity of {} were added at {}",
                payload.getQuantity(),
                payload.getItem().getName(),
                payload.getLocation());

    }

    private void doInventory(InventoryPayload payload) {
        logger.info("inventory requested:");
        payload.getData().stream().forEach(c -> logger.info("* {} {} (SKU: {}) at {}",
                c.getQuantity(),
                c.getItem().getName(),
                c.getItem().getSku(),
                c.getLocation())
        );

    }

    private void doInvalidSlot(InvalidSlotPayload payload) {

        logger.error("Not a valid slot! {} - {}", payload.getLocation(), payload.getException().getMessage());

    }

    private void doInvalidCurrency(InvalidCurrencyPayload payload) {

        logger.info("Bad money inserted... {} ", payload.getMoney());

    }

    private void doFailedToAddItem(FailedToAddItemPayload payload) {

        logger.info("Failed to add {} of {} at location {} because {}",
                payload.getQuantity(),
                payload.getItem().getName(),
                payload.getLocation(),
                payload.getException().getMessage());

    }

    private void doDispenseItem(DispenseItemPayload payload) {

        logger.info("Vending {} valued at {}, at location {}, change returned: {}",
                payload.getItem().getName(),
                payload.getItem().getPrice(),
                payload.getLocation(),
                payload.getMoney()

        );

    }

    private void doBalanceCheck(BalanceCheckPayload payload) {

        logger.info("Current balance is :{}", payload.getBalance());

    }


}
