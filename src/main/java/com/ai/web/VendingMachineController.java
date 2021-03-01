package com.ai.web;

import java.util.Currency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ai.domain.Location;
import com.ai.domain.Money;
import com.ai.messaging.Payload;
import com.ai.service.VendingMachine;

@RestController
public class VendingMachineController {

    @Autowired
    private VendingMachine machine;

    @GetMapping("/balance")
    public Payload getBalance() {
        return machine.getBalance();
    }

    @GetMapping("/inventory")
    public Payload getInventory() {
        return machine.getInventory();
    }

    @GetMapping("/dispense")
    public Payload getInventory(@RequestParam(name = "loc") String location) {
        return machine.dispense(Location.fromString(location));
    }

    @GetMapping("/insertMoney")
    public Payload insertMoney(@RequestParam(name = "c") Currency currency, @RequestParam(name = "v") double value) {
        return machine.addMoney(new Money(value, currency));
    }

    @GetMapping("/cancel")
    public Payload cancel() {
        return machine.cancelOrder();
    }

    @GetMapping("/checkItem")
    public Payload checkItem(@RequestParam(name = "loc") String location) {
        return machine.getSlotInfo(Location.fromString(location));
    }

}
