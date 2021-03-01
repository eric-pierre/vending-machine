package com.ai.configuration;

import java.util.Collection;
import java.util.Currency;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.ai.domain.MachineLayout;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "vending")

public class ApplicationProperties {

    private Currency currency;
    private Layout layout;
    private String machineId;

    private String inventoryStore;

    private Collection<Double> denominations;

    public void setCurrency(String currency) {
        this.currency = Currency.getInstance(currency);
    }

    public Currency getCurrency() {
        return this.currency;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    public Layout getLayout() {
        return this.layout;
    }

    public MachineLayout getMachineLayout() {
        return MachineLayout.builder()
                .columns(layout.columns)
                .rows(layout.rows)
                .itemCount(layout.itemCount)
                .build();
    }

    public Collection<Double> getDenominations() {
        return denominations;
    }

    public void setDenominations(Collection<Double> denominations) {
        this.denominations = denominations;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getInventoryStore() {
        return inventoryStore;
    }

    public void setInventoryStore(String inventoryStore) {
        this.inventoryStore = inventoryStore;
    }

    public static class Layout {

        private int columns;
        private int rows;
        private int itemCount;

        public int getColumns() {
            return columns;
        }

        public void setColumns(int columns) {
            this.columns = columns;
        }

        public int getRows() {
            return rows;
        }

        public void setRows(int rows) {
            this.rows = rows;
        }

        public int getItemCount() {
            return itemCount;
        }

        public void setItemCount(int itemCount) {
            this.itemCount = itemCount;
        }

    }

}