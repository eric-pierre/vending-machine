package com.ai.vending;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.Currency;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import com.ai.domain.Item;
import com.ai.domain.Location;
import com.ai.domain.Money;
import com.ai.domain.MoneyUS;
import com.ai.domain.Slot;
import com.ai.exception.NotEnoughMoneyException;
import com.ai.messaging.BalancePayload;
import com.ai.messaging.Payload;
import com.ai.messaging.impl.CancelPayload;
import com.ai.messaging.impl.DispenseItemPayload;
import com.ai.messaging.impl.FailedToAddItemPayload;
import com.ai.messaging.impl.InvalidCurrencyPayload;
import com.ai.messaging.impl.InvalidSlotPayload;
import com.ai.messaging.impl.InventoryPayload;
import com.ai.messaging.impl.MoneyAddedPayload;
import com.ai.messaging.impl.NotEnoughMoneyPayload;
import com.ai.messaging.impl.OutOfStockPayload;
import com.ai.messaging.impl.SlotInfoPayload;
import com.ai.service.VendingMachine;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
class TestVendingMachine2 {

    @Autowired
    VendingMachine machine;

    @Test
    void testInsertCoins() {

        Payload Payload = machine.addMoney(MoneyUS.QUARTER);

        Payload = machine.addMoney(MoneyEuro.C10);

        assertTrue(Payload instanceof InvalidCurrencyPayload);
        InvalidCurrencyPayload payload = (InvalidCurrencyPayload) Payload;

        assertEquals(MoneyEuro.C10, payload.getMoney());

        @SuppressWarnings("serial")
        Money fakeMoney = new Money(3, Currency.getInstance("USD")) {
        };

        Payload = machine.addMoney(fakeMoney);
        assertTrue(Payload instanceof InvalidCurrencyPayload);
        payload = (InvalidCurrencyPayload) Payload;

        assertEquals(3.0, payload.getMoney().getValue());
    }

    private void addaQuarterMoney(VendingMachine machine, double expected) {
        Payload m = machine.addMoney(MoneyUS.QUARTER);

        assertTrue(m instanceof MoneyAddedPayload);
        BalancePayload payload = (BalancePayload) m;

        assertEquals(expected, payload.getBalance().getValue());
    }

    @Test
    void tryToBuyAcandyBarButNoMoney() {

        double snickersPrice = 3.0d;
        Location snickersAt = Location.fromString("B2");

        addaQuarterMoney(machine, 0.25);
        addaQuarterMoney(machine, 0.50);
        addaQuarterMoney(machine, 0.75);
        addaQuarterMoney(machine, 1);

        Payload Payload = machine.getSlotInfo(snickersAt);

        SlotInfoPayload payload = (SlotInfoPayload) Payload;

        Slot info = payload.getData();
        assertEquals(snickersPrice, info.getItem().getPrice().getValue());

        Payload dispensePayload = machine.dispense(snickersAt);
        assertTrue(dispensePayload instanceof NotEnoughMoneyPayload);

        NotEnoughMoneyPayload p2 = (NotEnoughMoneyPayload) dispensePayload;

        assertEquals("Snickers", p2.getItem().getName());
        assertEquals(3.0, p2.getItem().getPrice().getValue());
        assertEquals(1.0, p2.getBalance().getValue());

        machine.addMoney(MoneyUS.NICKEL);
        dispensePayload = machine.dispense(snickersAt);
        p2 = (NotEnoughMoneyPayload) dispensePayload;
        assertEquals(1.05, p2.getBalance().getValue());

    }

    @Test
    void buyAcandy() {

        buySomeCandy("Almond Joy", 2.0, "A1");
        buySomeCandy("Almond Joy", 2.0, "A1");
        buySomeCandy("Heath", 3.0, "C1");
        buySomeCandy("Twix", 3.1, "D2");

    }

    @Test
    void checkInventory() {
        machine.addMoney(MoneyUS.FIVER);
        Location c2 = Location.fromString("C2");
        machine.dispense(c2);

        Payload Payload = machine.getInventory();

        assertTrue(Payload instanceof InventoryPayload);

        InventoryPayload payload = (InventoryPayload) Payload;

        Collection<Slot> inventory = payload.getData();

        Optional<Slot> thing = inventory.stream().filter(c -> c.getLocation().equals(c2)).findAny();
        assertTrue(thing.isPresent());

        assertEquals(14, thing.get().getQuantity());
    }

    @Test
    void testLoadMachine() {
        Item thing = Item.builder().name("Toblerone").build();
        Payload m = machine.addItem(thing, Location.fromString("R55"), 50);
        assertTrue(m instanceof FailedToAddItemPayload);

        FailedToAddItemPayload payload = (FailedToAddItemPayload) m;

        assertEquals(50, payload.getQuantity());
        assertEquals("Toblerone", payload.getItem().getName());
        assertEquals("R55", payload.getLocation().toString());
        assertEquals("Invalid quantity 50 (Max 15)", payload.getException().getMessage());

    }

    @Test
    void testBalance() {
        Payload Payload;
        Location payday = Location.fromString("D1");
        double payDayCost = 2.8;
        double inserted = 5.0;

        double change = inserted - payDayCost;
        machine.addMoney(MoneyUS.FIVER);

        Payload = machine.getBalance();
        assertTrue(Payload instanceof BalancePayload);

        BalancePayload payload = (BalancePayload) Payload;
        Money balance = payload.getBalance();
        assertEquals(MoneyUS.FIVER, balance);

        Payload = machine.dispense(payday);
        assertTrue(Payload instanceof DispenseItemPayload);

        DispenseItemPayload payload2 = (DispenseItemPayload) Payload;

        assertEquals(change, payload2.getMoney().getValue());
        payload = (BalancePayload) machine.getBalance();

        assertEquals(0, payload.getBalance().getValue());

    }

    @Test
    void testMoney() {
        Money five = MoneyUS.FIVER;
        Money one = MoneyUS.DOLLAR;

        Money four = five.minus(one);
        assertEquals(4, four.getValue());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> four.minus(MoneyEuro.C10));

        assertEquals("Invalid currency (EUR) for use with this Money (USD)", exception.getMessage());

    }

    @Test
    void runOutOfCandy() {
        Location a1 = Location.fromString("A1");
        Payload Payload = machine.getSlotInfo(a1);

        assertTrue(Payload instanceof SlotInfoPayload);

        SlotInfoPayload p = (SlotInfoPayload) Payload;

        int quantity = p.getData().getQuantity();

        for (int i = 0; i < quantity; i++) {
            Payload = machine.getSlotInfo(a1);
            SlotInfoPayload payload = (SlotInfoPayload) Payload;

            assertEquals(quantity - i, payload.getData().getQuantity());

            machine.addMoney(MoneyUS.FIVER);
            machine.dispense(a1);

        }

        machine.addMoney(MoneyUS.FIVER);
        Payload = machine.dispense(a1);

        assertTrue(Payload instanceof OutOfStockPayload);

        OutOfStockPayload payload = (OutOfStockPayload) Payload;
        assertEquals(a1, payload.getLocation());
    }

    @Test
    void doSomethingSilly() {
        Location z20 = Location.fromString("Z20");

        Payload Payload = machine.getSlotInfo(z20);

        assertTrue(Payload instanceof InvalidSlotPayload);
        InvalidSlotPayload payload = (InvalidSlotPayload) Payload;

        assertEquals("Invalid location: Z20", payload.getException().getMessage());
    }

    @Test
    void cancelOrder() {
        for (int i = 0; i < 15; i++) {
            machine.addMoney(MoneyUS.FIVER);
        }
        Payload Payload = machine.cancelOrder();

        assertTrue(Payload instanceof CancelPayload);
        CancelPayload payload = (CancelPayload) Payload;

        assertEquals(75, payload.getMoney().getValue());

        Payload = machine.getBalance();

        assertTrue(Payload instanceof BalancePayload);
        BalancePayload p = (BalancePayload) Payload;
        assertEquals(0, p.getBalance().getValue());

    }

    @Test
    void testNoMoneyException() {

        Payload payload = machine.getBalance();

        assertTrue(payload instanceof BalancePayload);
        assertEquals(0d, ((BalancePayload) payload).getBalance().getValue());

        payload = machine.dispense(Location.fromString("A1"));
        assertTrue(payload instanceof NotEnoughMoneyPayload);
        NotEnoughMoneyPayload noMoney = (NotEnoughMoneyPayload) payload;
        assertTrue(noMoney.getException() instanceof NotEnoughMoneyException);

    }

    private void buySomeCandy(String candyName, double candyCost, String location) {

        Location loc = Location.fromString(location);

        machine.addMoney(MoneyUS.DOLLAR);
        machine.addMoney(MoneyUS.DOLLAR);
        Payload m1 = machine.addMoney(MoneyUS.DOLLAR);

        assertTrue(m1 instanceof MoneyAddedPayload);
        MoneyAddedPayload payload = (MoneyAddedPayload) m1;

        assertEquals(3.0, payload.getBalance().getValue());
        assertEquals(1.0, payload.getMoney().getValue());

        Payload disp = machine.dispense(loc);

        if (candyCost > 3) {
            assertTrue(disp instanceof NotEnoughMoneyPayload);
            NotEnoughMoneyPayload p2 = (NotEnoughMoneyPayload) disp;


            assertEquals(candyName, p2.getItem().getName());
            assertEquals(3, p2.getBalance().getValue());

        } else {
            assertTrue(disp instanceof DispenseItemPayload);
            DispenseItemPayload p2 = (DispenseItemPayload) disp;

            assertEquals(3 - candyCost, p2.getMoney().getValue());
            assertEquals(location, p2.getLocation().toString());
            assertEquals(candyName, p2.getItem().getName());

        }

    }

}
