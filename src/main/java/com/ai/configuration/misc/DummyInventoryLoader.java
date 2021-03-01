package com.ai.configuration.misc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.ai.configuration.ApplicationProperties;
import com.ai.domain.Slot;
import com.ai.service.VendingMachine;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Profile("test")
@Slf4j
public class DummyInventoryLoader implements ApplicationListener<ContextRefreshedEvent> {

    private ObjectMapper mapper;
    private VendingMachine machine;
    private ApplicationProperties props;

    DummyInventoryLoader(ApplicationProperties props, ObjectMapper mapper, VendingMachine machine) {
        this.mapper = mapper;
        this.machine = machine;
        this.props = props;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        String inventoryStore = props.getInventoryStore();
        if (inventoryStore == null) {
            log.info("vending.inventory-store not specified; dummy inventory not available");
            return;
        }

        InputStream stream = getClass().getResourceAsStream(inventoryStore);

        if (stream == null) {
            log.info("Inventory resource {} is not found in the classpath- skipping inventory loading", inventoryStore);
            return;
        }

        log.info("Loading the inventory with dummy data....", inventoryStore);

        Collection<Slot> slots;
        try {
            slots = mapper.readValue(stream, new TypeReference<Collection<Slot>>() {
            });
        } catch (IOException e) {
            log.error("Inventory config '{}' can't be loaded.", e);
            return;
        }

        for (Slot slot : slots) {
            machine.addItem(slot.getItem(), slot.getLocation(), 15);
        }

    }



}
